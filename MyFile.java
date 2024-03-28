package file_system;

import memory.Block;

import java.util.List;

public class MyFile {
    private String name;
    private int size;
    private int requiredBlocks;
    private int startBlock;
    private int endBlock;
    private List<String> content;

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

    public int getEndBlock() {
        return endBlock;
    }

    public List<String> getContent() {
        return content;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public void setEndBlock(int endBlock) {
        this.endBlock = endBlock;
    }

    @Override
    public String toString() {
        return name;
    }
}
