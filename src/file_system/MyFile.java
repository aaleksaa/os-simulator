package file_system;

import memory.Block;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MyFile {
    private String name;
    private int size;
    private int requiredBlocks;
    private int startBlock;
    private List<String> content;

    public static final Comparator<MyFile> compareByStartBlock = (o1, o2) -> Integer.compare(o1.startBlock, o2.startBlock);

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

    @Override
    public String toString() {
        return name + " " + startBlock + " " + requiredBlocks;
    }
}
