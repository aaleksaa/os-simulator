package file_system;

import memory.Disk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileSystem {
    private static final String ROOT_PATH = "root";
    private static final String RESULTS_PATH = "results";
    private final Directory root;
    private Directory current;
    private final Disk disk;

    public FileSystem(Disk disk) {
        root = new Directory(ROOT_PATH, null);
        current = root;
        this.disk = disk;
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

    public void listFiles() {
        current.printDirectoryContent();
    }

    public void changeDirectory(String name) {
        if (name.equals("..") && current != root) {
            current = current.getParent();
        }
        else {
            Directory childDir = current.getChildDirectoryByName(name);

            if (childDir == null)
                throw new IllegalArgumentException("Directory " + name + " does not exist!\n");

            current = childDir;
        }
    }

    public void makeDirectory(String name) {
        Directory dir = new Directory(name, current);
        current.addChildDirectory(dir);
        Path path = Paths.get(dir.getAbsolutePath());

        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFileOrDirectory(String name) {
        MyFile file = current.getChildFileByName(name);
        Directory directory = current.getChildDirectoryByName(name);

        if (file != null)
            current.removeChildFile(current, file, disk);
        else if (directory != null)
            current.removeChildDirectory(directory, disk);
        else
            throw new IllegalArgumentException("Directory or file with name " + name + " does not exist in " + current.getName());
    }

    public void renameDirectory(String oldName, String newName) {
        Directory dir = current.getChildDirectoryByName(oldName);

        if (dir == null)
            throw new IllegalArgumentException("Directory with name \"" + oldName + "\" does not exist in \"" + current.getName() + "\"!");
        if (current.getChildDirectoryByName(newName) != null)
            throw new IllegalArgumentException("Directory with name \"" + newName + "\" is already in \"" + current.getName() + "\"!");

        Path oldDirPath = Paths.get(current.getName(), oldName);
        Path newDirPath = Paths.get(current.getName(), newName);

        try {
            dir.setName(newName);
            System.out.println("Directory " + oldName + " successfully renamed to " + newName + "!");
            Files.move(oldDirPath, newDirPath);
        } catch (IOException e) {
            System.err.println("Error with renaming directory!");
        }
    }

    public void addFileToResultsDir(String processName, String content) {
        Directory resultDir = root.getChildDirectoryByName(RESULTS_PATH);

        if (resultDir == null) {
            Path path = Paths.get(root.getAbsolutePath(), "results");
            resultDir = new Directory("results", root);
            root.addChildDirectory(resultDir);

            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String filename = UUID.randomUUID().toString().replace("-", "").substring(0, 4) + "-" + processName + "-output.txt";
        Path path = Paths.get(resultDir.getAbsolutePath(), filename);

        try {
            Files.createFile(path);
            Files.writeString(path, content, StandardCharsets.UTF_8);

            resultDir.addChildFile(new MyFile(filename, (int) Files.size(path), Files.readAllLines(path)), disk);
            System.out.println(filename + " created!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
