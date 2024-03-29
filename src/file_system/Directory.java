package file_system;

import memory.Disk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Directory implements Comparable<Directory> {
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

    public boolean isEmpty() {
        return files.isEmpty() && directories.isEmpty();
    }

    public Directory getChildDirectoryByName(String name) {
        for (Directory dir : directories)
            if (dir.getName().equals(name))
                return dir;
        return null;
    }


    public void addChildDirectory(Directory dir) {
        if (directories.contains(dir))
            throw new IllegalArgumentException("Directory " + dir.getName() + " is already in " + name + "!\n");
        directories.add(dir);
    }

    public void removeChildDirectory(Directory dir) {
        if (!directories.contains(dir))
            throw new IllegalArgumentException("Directory " + dir.getName() + " does not exist!\n");
        directories.remove(dir);
    }

    public void addChildFile(MyFile file, Disk disk) {
        files.add(file);
        //disk.allocateFile(file);
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Directory " + name + " is empty!";

        StringBuilder sb = new StringBuilder();
        sb.append("Current directory ").append(name).append("\n");
        sb.append("Type").append("\t\t").append("Name").append("\n");

        for (Directory dir : directories)
            sb.append("Directory").append("\t").append(dir.getName()).append("\n");

        for (MyFile file : files)
            sb.append("File").append("\t\t").append(file).append("\n");

        return sb.toString();
    }

    @Override
    public int compareTo(Directory o) {
        return name.compareTo(o.name);
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
}
