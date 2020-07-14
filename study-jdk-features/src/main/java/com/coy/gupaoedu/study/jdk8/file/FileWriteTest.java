package com.coy.gupaoedu.study.jdk8.file;

import org.junit.Test;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * 文件写入
 *
 * @author chenck
 * @date 2020/7/14 14:00
 */
public class FileWriteTest {
    static Random rand = new Random(47);
    static final int SIZE = 1000;

    /**
     * Files.write() 被重载以写入 byte 数组或任何 Iterable 对象
     */
    @Test
    public void write() throws Exception {
        // Write bytes to a file:
        byte[] bytes = new byte[SIZE];
        rand.nextBytes(bytes);
        Files.write(Paths.get("bytes.dat"), bytes);
        System.out.println("bytes.dat: " + Files.size(Paths.get("bytes.dat")));

        // Write an iterable to a file:
        List<String> lines = Files.readAllLines(Paths.get(".gitignore"));
        Files.write(Paths.get("Cheese.txt"), lines);
        System.out.println("Cheese.txt: " + Files.size(Paths.get("Cheese.txt")));
    }

    /**
     * Files.lines() 对于把文件处理行的传入流时非常有用，但是如果你想在 Stream 中读取，处理或写入怎么办？
     */
    @Test
    public void streamInAndOut() {
        // try-with-resources
        try (
                Stream<String> input = Files.lines(Paths.get("pom.xml"));// 读取文件
                PrintWriter output = new PrintWriter("pom.txt")// 写入文件
        ) {

            input.map(String::toUpperCase)// 转换为大写
                    .forEachOrdered(output::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
