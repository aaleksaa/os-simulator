package file_system;

import memory.Block;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MyFile {
    private final String name;
    private final int size;
    private final List<String> content;
    private int startBlock;
    private final int requiredBlocks;

    public static final Comparator<MyFile> compareByStartBlock = Comparator.comparingInt(o -> o.startBlock);

    public MyFile(String name, int size, List<String> content) {
        this.name = name;
        this.size = size;
        this.requiredBlocks = size % Block.SIZE != 0 ? (size / Block.SIZE) + 1 : size / Block.SIZE;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getRequiredBlocks() {
        return requiredBlocks;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public List<String> getContent() {
        return content;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public void printFile() {
        System.out.printf("%-20s\t\t %-20s%n", "File", name);
    }

    @Override
    public String toString() {
        return String.format("%-20s\t\t %-10s\t\t\t %-5s\n", name, startBlock, requiredBlocks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MyFile myFile = (MyFile) o;
        return name.equals(myFile.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
