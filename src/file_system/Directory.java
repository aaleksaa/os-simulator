package file_system;

import memory.Disk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Directory {
    private String name;
    private Directory parent;
    private List<Directory> directories = new ArrayList<>();
    private List<MyFile> files = new ArrayList<>();

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }

    public List<Directory> getDirectories() {
        return directories;
    }

    public List<MyFile> getFiles() {
        return files;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public void setDirectories(List<Directory> directories) {
        this.directories = directories;
    }

    public void setFiles(List<MyFile> files) {
        this.files = files;
    }

    public boolean isEmpty() {
        return files.isEmpty() && directories.isEmpty();
    }

    public Directory getChildDirectoryByName(String name) {
        return directories.stream()
                .filter(directory -> directory.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public MyFile getChildFileByName(String name) {
        return files.stream()
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addChildDirectory(Directory dir) {
        if (directories.contains(dir))
            throw new IllegalArgumentException("Directory " + dir.getName() + " is already in " + name + "!\n");
        directories.add(dir);
    }

    public void addChildFile(MyFile file, Disk disk) {
        try {
            disk.allocateFile(file);
            files.add(file);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeChildDirectory(Directory dir, Disk disk) {
        dir.getFiles().forEach(file -> removeChildFile(dir, file, disk));
        dir.getDirectories().forEach(childDir -> removeChildDirectory(childDir, disk));
        directories.remove(dir);

        try {
            Files.delete(dir.toPath());
        } catch (IOException e) {
            System.err.println("Failed to delete directory: " + dir.getAbsolutePath());
        }
    }

    public void removeChildFile(Directory dir, MyFile file, Disk disk) {
        files.remove(file);
        disk.deallocateFile(file);

        try {
            Files.deleteIfExists(Paths.get(dir.getAbsolutePath(), file.getName()));
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + file.getName());
        }
    }

    public Path toPath() {
        return parent == null ? Paths.get(name) : parent.toPath().resolve(name);
    }

    public String getAbsolutePath() {
        return toPath().toAbsolutePath().toString();
    }

    public void printDirectoryContent() {
        if (isEmpty())
            System.out.println("Directory " + name + " is empty!");
        else {
            System.out.println("Content of " + name);
            System.out.printf("%-20s\t\t %-20s%n", "TYPE", "NAME");

            directories.forEach(System.out::print);
            files.forEach(MyFile::printFile);
        }
    }

    @Override
    public String toString() {
        return String.format("%-20s\t %-20s%n", "Directory", name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Directory directory = (Directory) obj;
        return Objects.equals(name, directory.name) && Objects.equals(parent, directory.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent);
    }
}
