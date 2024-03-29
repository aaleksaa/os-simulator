package file_system;

import memory.Disk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {
    private Directory root;
    private Directory current;
    private static final String ROOT_PATH = "root";

    public FileSystem(Disk disk) {
        root = new Directory(ROOT_PATH, null);
        current = root;
        createTree(root, new File(ROOT_PATH), disk);
    }

    public Directory getRoot() {
        return root;
    }

    public Directory getCurrent() {
        return current;
    }

    private void createTree(Directory dir, File root, Disk disk) {
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                Directory newDir = new Directory(child.getName(), dir);
                dir.addChildDirectory(newDir);
                createTree(newDir, child, disk);
            } else {
                try {
                    dir.addChildFile(new MyFile(child.getName(), (int) child.length(), Files.readAllLines(Path.of(child.getPath()))), disk);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void changeDirectory(String name) {
        if (name.equals("..") && current != root)
            current = current.getParent();
        else {
            Directory childDir = current.getChildDirectoryByName(name);

            if (childDir == null)
                throw new IllegalArgumentException("Directory " + name + " does not exist!\n");

            current = childDir;
        }
    }

    public void makeDirectory(String name) {
        current.addChildDirectory(new Directory(name, current));
    }


    public String listFiles() {
        return current.toString();
    }
}
