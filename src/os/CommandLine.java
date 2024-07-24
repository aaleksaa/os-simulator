package os;


import file_system.FileSystem;
import memory.Disk;


public class CommandLine {
    public static void cd(FileSystem fileSystem, String name) {
        try {
            fileSystem.changeDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void mkdir(FileSystem fileSystem, String name) {
        try {
            fileSystem.makeDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void dir(FileSystem fileSystem) {
        System.out.println(fileSystem.listFiles());
    }

    public static void rm(FileSystem fileSystem, Disk disk, String name) {
        try {
            fileSystem.removeFileOrDirectory(name, disk);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void ren(FileSystem fileSystem, String oldName, String newName) {
        try {
            fileSystem.renameDirectory(oldName, newName);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void exit() {
        System.exit(0);
    }
}
