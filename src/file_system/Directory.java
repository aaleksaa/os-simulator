package file_system;

import memory.Disk;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Directory {
    private String name;
    private Directory parent;
    private List<Directory> directories;
    private List<MyFile> files;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
        this.directories = new ArrayList<>();
        this.files = new ArrayList<>();
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
                .filter(d -> d.getName().equals(name))
                .findFirst()
                .orElse(null);
    }


    public void addChildDirectory(Directory dir) {
        if (directories.contains(dir))
            throw new IllegalArgumentException("Directory " + dir.getName() + " is already in " + name + "!\n");
        directories.add(dir);
    }

    public void removeChildDirectory(Directory dir, Disk disk) {
        Iterator<Directory> dirIterator = dir.getDirectories().iterator();
        while (dirIterator.hasNext()) {
            Directory childDir = dirIterator.next();
            removeChildDirectory(childDir, disk);
            dirIterator.remove();
        }

        Iterator<MyFile> fileIterator = dir.getFiles().iterator();
        while (fileIterator.hasNext()) {
            MyFile file = fileIterator.next();
            removeChildFile(file, disk);
            fileIterator.remove();
        }

        directories.remove(dir);
    }


    public void addChildFile(MyFile file, Disk disk) {
        files.add(file);
        disk.allocateFile(file);
    }

    public void printDirectory() {
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
        return String.format("%-20s\t %-20s%n", "DIRECTORY", name);
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
        return Objects.hash(name, parent, directories, files);
    }

    public MyFile getChildFileByName(String name) {
        return files.stream()
                .filter(f -> f.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Path toPath() {
        return parent == null ? Paths.get(name) : parent.toPath().resolve(name);
    }

    public String getAbsolutePath() {
        return toPath().toAbsolutePath().toString();
    }

    public void removeChildFile(MyFile file, Disk disk) {
        files.remove(file);
        disk.deallocateFile(file);
    }
}