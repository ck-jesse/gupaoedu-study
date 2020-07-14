package com.coy.gupaoedu.study.jdk8.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author chenck
 * @date 2020/7/14 12:18
 */
public class Rmdir {

    /**
     * 删除目录树
     */
    public static void rmdir(Path dir) throws IOException {
        // "walking" 目录树意味着遍历每个子目录和文件。
        // Visitor 设计模式提供了一种标准机制来访问集合中的每个对象，然后你需要提供在每个对象上执行的操作。
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

            // 在访问目录中条目之前在目录上运行
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return super.preVisitDirectory(dir, attrs);
            }

            // 调用无法访问的文件
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return super.visitFileFailed(file, exc);
            }

            // 运行目录中的每一个文件。
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            // 在访问目录中条目之后在目录上运行，包括所有的子目录
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
