package com.coy.gupaoedu.study.jdk8.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/**
 * 路径监听
 * 通过 WatchService 可以设置一个进程对目录中的更改做出响应。
 *
 * @author chenck
 * @date 2020/7/14 12:33
 */
public class PathWatcherTest {
    static Path test = Paths.get("test");

    static void delTxtFiles() {
        try {
            Files.walk(test)
                    .filter(f -> f.toString().endsWith(".txt"))
                    .forEach(f -> {
                        try {
                            System.out.println("deleting " + f);
                            Files.delete(f);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        DirectoriesTest.refreshTestDir();
        DirectoriesTest.populateTestDir();
        Files.createFile(test.resolve("Hello.txt"));

        // 从 FileSystem 中得到了 WatchService 对象，我们将其注册到 test 路径以及我们感兴趣的项目的变量参数列表中，
        // 可以选择 ENTRY_CREATE，ENTRY_DELETE 或 ENTRY_MODIFY(其中创建和删除不属于修改)。
        WatchService watcher = FileSystems.getDefault().newWatchService();
        test.register(watcher, ENTRY_DELETE);

        Executors.newSingleThreadScheduledExecutor()
                .schedule(PathWatcherTest::delTxtFiles, 250, TimeUnit.MILLISECONDS);

        // watcher.take() 的调用会在发生某些事情之前停止所有操作
        // watcher.take() 将等待并阻塞在这里。当目标事件发生时，会返回一个包含 WatchEvent 的 Watchkey 对象。展示的这三种方法是能对 WatchEvent 执行的全部操作。
        WatchKey key = watcher.take();
        for (WatchEvent evt : key.pollEvents()) {
            System.out.println("evt.context(): " + evt.context() +
                    "\nevt.count(): " + evt.count() +
                    "\nevt.kind(): " + evt.kind());
            System.exit(0);
        }
    }
}
