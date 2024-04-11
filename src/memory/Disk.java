package memory;

import file_system.MyFile;

import java.util.ArrayList;
import java.util.List;

public class Disk {
    private final int size;
    private List<Block> blocks;
    private List<MyFile> files;

    public Disk() {
        this.size = 512;
        this.blocks = new ArrayList<>();
        this.files = new ArrayList<>();

        for (int i = 0; i < size / Block.SIZE; i++)
            blocks.add(new Block(i));
    }

    private int maxNumberOfFreeContiguousBlocks() {
        int max = -1;

        for (int i = 0; i < blocks.size(); i++) {
            int current = 0;

            if (blocks.get(i).isAllocated())
                continue;

            for (int j = i; j < blocks.size(); j++) {
                if (blocks.get(j).isAllocated())
                    break;
                current++;
            }

            if (current > max)
                max = current;
        }

        return max;
    }

    private int findStartBlock(int length) {
        if (files.isEmpty())
            return 0;

        int startIndex = -1;

        for (int i = 0; i < blocks.size(); i++) {
            int current = 0;

            if (blocks.get(i).isAllocated())
                continue;

            for (int j = i; j < blocks.size(); j++) {
                if (blocks.get(j).isAllocated() || current == length)
                    break;
                current++;
            }

            if (current == length) {
                startIndex = i;
                break;
            }
        }

        return startIndex;
    }

    public void allocateFile(MyFile file) {
        if (file.getRequiredBlocks() > maxNumberOfFreeContiguousBlocks())
            throw new IllegalArgumentException("Not enough space for this file!\n");

        int requiredBlocks = file.getRequiredBlocks();
        int startBlock = findStartBlock(requiredBlocks);

        for (int i = startBlock; i < startBlock + requiredBlocks; i++)
            blocks.get(i).setAllocated(true);

        files.add(file);
        file.setStartBlock(startBlock);
        file.setEndBlock(startBlock + requiredBlocks);
    }

    public void deallocateFile(MyFile file) {
        for (int i = file.getStartBlock(); i < file.getEndBlock(); i++)
            blocks.get(i).setAllocated(false);
        files.remove(file);
    }

    public String printDisk() {
        StringBuilder sb = new StringBuilder("Name Start Length\n");

        for (MyFile file : files)
            sb.append(file.getName()).append(" ").append(file.getStartBlock()).append(" ").append(file.getRequiredBlocks()).append("\n");

        return sb.toString();
    }

    public MyFile getFileByName(String name) {
        for (MyFile file : files)
            if (file.getName().equals(name))
                return file;
        return null;
    }
}
