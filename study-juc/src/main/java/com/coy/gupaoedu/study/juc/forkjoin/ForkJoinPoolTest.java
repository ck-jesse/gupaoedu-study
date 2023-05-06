package com.coy.gupaoedu.study.juc.forkjoin;


import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 文档：http://ifeve.com/tag/forkjoinpool/
 * <p>
 * ForkJoin使用分而治之的思想，把一个大任务拆分成一个个小任务，然后再聚合，得到最终结果。这有点像Hadoop中的MapReduce。还支持工作窃取。
 * Fork就是拆分，Join就是合并。当然，在使用ForkJoinPool构建异步模型时，对于不需要合并(Join)的任务也非常适用。
 * 通过工作窃取（work-stealing），可以充分利用CPU多核进行复杂计算，避免线程进入阻塞或闲置状态。这对java的并发处理能力是很大的提升，同时也避免了大范围使用锁带来的问题。
 * ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），里面存放的对象是任务（ForkJoinTask）。
 * 每个工作线程在运行中产生新的任务（通常是因为调用了 fork()）时，会放入工作队列的队尾，并且工作线程在处理自己的工作队列时，使用的是 LIFO 方式，也就是说每次从队尾取出任务来执行。
 * 使用后进先出 —— LIFO用来处理每个工作线程的自己任务，但是使用先进先出 —— FIFO规则用于获取别的任务，这是一种被广泛使用的进行递归Fork/Join设计的一种调优手段。
 * 让窃取任务的线程从队列拥有者相反的方向进行操作会减少线程竞争。同样体现了递归分治算法的大任务优先策略。
 * <p>
 * ForkJoinPool 中的任务分为两种：
 * 1、本地提交的任务：submission task，如：execute/submit提交的任务
 * 2、fork出来的子任务：work task
 * execute() 方法提交的Runnable类型任务，将会转换为ForkJoinPool类型任务，但由于Runnable类型任务是不可切分的，所以这类型任务无法获得任务拆分(Fork)这方面的收益，不过任然可以获得任务窃取带来的好处和性能提升
 * <p>
 * <p>
 * Java 7开始引入了一种新的Fork/Join线程池，它可以执行一种特殊的任务：把一个大任务拆成多个小任务并行执行。
 * 我们举个例子：如果要计算一个超大数组的和，最简单的做法是用一个循环在一个线程内完成： *
 * ┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
 * 还有一种方法，可以把数组拆成两部分，分别计算，最后加起来就是最终结果，这样可以用两个线程并行执行：
 * <p>
 * ┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
 * ┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
 * 如果拆成两部分还是很大，我们还可以继续拆，用4个线程并行执行：
 * <p>
 * ┌─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┘
 * ┌─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┘
 * ┌─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┘
 * ┌─┬─┬─┬─┬─┬─┐
 * └─┴─┴─┴─┴─┴─┘
 * 这就是Fork/Join任务的原理：判断一个任务是否足够小，如果是，直接计算，否则，就分拆成几个小任务分别计算。这个过程可以反复“裂变”成一系列小任务。
 *
 * @author chenck
 * @date 2022/6/9 22:48
 */
public class ForkJoinPoolTest {

    /**
     * 1、什么是工作窃取?
     * 假设有A、B两个线程执行一个任务，A比较快，把活儿干完了，这时候A可以把B的一部分活接过来。这样总体来说会加快任务执行速度。
     */

    public static void main(String[] args) throws Exception {
        // 创建2000个随机数组成的数组:
        long[] array = new long[2000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = random();
            expectedSum += array[i];
        }
        System.out.println("Expected sum: " + expectedSum);
        // fork/join:
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }

    static Random random = new Random(0);

    static long random() {
        return random.nextInt(10000);
    }

}

/**
 * 自定义ForkJoinPool 汇总Task
 *
 * @author chenck
 * @date 2022/6/10 22:46
 */
class SumTask extends RecursiveTask<Long> {
    static final int THRESHOLD = 100;// 拆分子任务的阈值
    long[] array;
    int start;
    int end;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // 如果任务足够小,直接计算:
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += this.array[i];
                // 故意放慢计算速度:
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }
            return sum;
        }
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d, %s ", start, end, start, middle, middle, end, Thread.currentThread().getName()));
        // “分裂”子任务:
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        // invokeAll会并行运行两个子任务:
        invokeAll(subtask1, subtask2);
        // 获得子任务的结果:
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();
        // 汇总结果:
        Long result = subresult1 + subresult2;
        System.out.println("result = " + subresult1 + " + " + subresult2 + " ==> " + result + " " + Thread.currentThread().getName());
        return result;
    }
}

