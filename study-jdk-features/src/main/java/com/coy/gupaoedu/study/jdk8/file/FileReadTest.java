package com.coy.gupaoedu.study.jdk8.file;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件读取
 * <p>
 * Java 7 和 8 对于处理文件和目录的类库做了大量改进。使用文件现在很简单，甚至很有趣，这是你以前永远想不到的。
 * 一定要研究 java.nio.file 的 Javadocs，尤其是 java.nio.file.Files 这个类。
 *
 * @author chenck
 * @date 2020/7/14 13:57
 */
public class FileReadTest {

    /**
     * Files.readAllLines() 一次读取整个文件（因此，“小”文件很有必要），产生一个List<String>。
     */
    @Test
    public void readAllLines() throws Exception {
        Files.readAllLines(
                Paths.get("pom.xml"))
                .stream()
                .filter(line -> !line.startsWith("//"))
                .map(line -> line.substring(0, line.length()))
                .forEach(System.out::println);
    }

    /**
     * 如果文件大小有问题怎么办？ 比如说：
     * 1.文件太大，如果你一次性读完整个文件，你可能会耗尽内存。
     * 2.您只需要在文件的中途工作以获得所需的结果，因此读取整个文件会浪费时间。
     * Files.lines() 方便地将文件转换为行的 Stream：
     */
    @Test
    public void lines() throws Exception {
        // 示例代码做了流式处理，跳过 13 行，然后选择下一行并将其打印出来。
        Files.lines(Paths.get("pom.xml"))
                .skip(13)
                .findFirst()
                .ifPresent(System.out::println);
    }
}
