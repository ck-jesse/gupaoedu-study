package com.coy.gupaoedu.study.jdk8.file;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

/**
 * 文件查找
 *
 * @author chenck
 * @date 2020/7/14 12:39
 */
public class FindFileTest {

    public static void main(String[] args) throws Exception {
        Path test = Paths.get("test");
        DirectoriesTest.refreshTestDir();
        DirectoriesTest.populateTestDir();

        // Creating a *directory*, not a file:
        Files.createDirectory(test.resolve("dir.tmp"));

        // 在 matcher 中，glob 表达式开头的 **/ 表示“当前目录及所有子目录”，这在当你不仅仅要匹配当前目录下特定结尾的 Path 时非常有用。
        // 单 * 表示“任何东西”，然后是一个点，然后大括号表示一系列的可能性---我们正在寻找以 .tmp 或 .txt 结尾的东西。
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher("glob:**/*.{tmp,txt}");
        Files.walk(test)
                .filter(matcher::matches)
                .forEach(System.out::println);
        System.out.println("***************");
        System.out.println();

        // matcher2 只使用 *.tmp，通常不匹配任何内容，但是添加 map() 操作会将完整路径减少到末尾的名称。
        PathMatcher matcher2 = FileSystems.getDefault()
                .getPathMatcher("glob:*.tmp");
        Files.walk(test)
                .map(Path::getFileName)
                .filter(matcher2::matches)
                .forEach(System.out::println);
        System.out.println("***************");
        System.out.println();

        // 要只查找文件，必须像在最后 files.walk() 中那样对其进行筛选。
        Files.walk(test) // Only look for files
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .filter(matcher2::matches)
                .forEach(System.out::println);
    }
}
