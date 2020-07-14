package com.coy.gupaoedu.study.jdk8.file;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 文件系统
 * <p>
 * 使用静态的 FileSystems 工具类获取"默认"的文件系统，但你同样也可以在 Path 对象上调用 getFileSystem() 以获取创建该 Path 的文件系统。
 * 你可以获得给定 URI 的文件系统，还可以构建新的文件系统(对于支持它的操作系统)。
 * <p>
 * 一个 FileSystem 对象也能生成 WatchService 和 PathMatcher 对象。
 *
 * @author chenck
 * @date 2020/7/14 12:31
 */
public class FileSystemTest {
    static void show(String id, Object o) {
        System.out.println(id + ": " + o);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        FileSystem fsys = FileSystems.getDefault();
        for (FileStore fs : fsys.getFileStores()) {
            show("File Store", fs);
        }
        for (Path rd : fsys.getRootDirectories()) {
            show("Root Directory", rd);
        }
        show("Separator", fsys.getSeparator());
        show("UserPrincipalLookupService", fsys.getUserPrincipalLookupService());
        show("isOpen", fsys.isOpen());
        show("isReadOnly", fsys.isReadOnly());
        show("FileSystemProvider", fsys.provider());
        show("File Attribute Views", fsys.supportedFileAttributeViews());
    }
}
