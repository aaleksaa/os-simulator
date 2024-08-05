package memory;
 
import file_system.MyFile;
 
import java.util.*;
 
public class Disk {
    private final int size;
    private List<Block> blocks;
    private NavigableMap<Integer, Integer> blocksTable;
    private List<MyFile> files;
 
    public Disk() {
        this.size = 512;
        this.blocks = new ArrayList<>();
        this.blocksTable = new TreeMap<>();
        this.files = new ArrayList<>();
 
        for (int i = 0; i < size / Block.SIZE; i++)
            blocks.add(new Block(i));
 
        blocksTable.put(0, size / Block.SIZE);
    }
 
    public int getSize() {
        return size;
    }
 
    public List<Block> getBlocks() {
        return blocks;
    }
 
    public Map<Integer, Integer> getBlocksTable() {
        return blocksTable;
    }
 
    public List<MyFile> getFiles() {
        return files;
    }
 
    private int findStartBlock(int length) {
        if (files.isEmpty())
            return 0;
 
        for (Map.Entry<Integer, Integer> entry : blocksTable.entrySet())
            if (entry.getValue() >= length)
                return entry.getKey();
 
        return -1;
    }
 
    private void updateFreeBlocks(int startBlock, int length) {
        blocksTable.put(startBlock + length, blocksTable.get(startBlock) - length);
        blocksTable.put(startBlock, 0);
    }
 
    public void allocateFile(MyFile file) {
        int requiredBlocks = file.getRequiredBlocks();
        int startBlock = findStartBlock(requiredBlocks);
 
        if (startBlock == -1)
            throw new IllegalArgumentException("Not enough space for this file!\n");
 
        for (int i = startBlock; i < startBlock + requiredBlocks; i++)
            blocks.get(i).setAllocated(true);
 
        updateFreeBlocks(startBlock, requiredBlocks);
        files.add(file);
        file.setStartBlock(startBlock);
    }
 
    public void update(int startBlock, int length) {
        Map.Entry<Integer, Integer> prev = blocksTable.lowerEntry(startBlock);
        Map.Entry<Integer, Integer> next = blocksTable.higherEntry(startBlock);
 
        if (prev == null && next != null)
            blocksTable.put(startBlock, length);
        else if (prev != null && next == null)
            blocksTable.put(startBlock, length);
        else {
            if (prev.getValue() != 0 && next.getValue() == 0) {
                blocksTable.put(prev.getKey(), prev.getValue() + length);
                blocksTable.remove(startBlock);
            } else if (prev.getValue() == 0 && next.getValue() != 0) {
                blocksTable.put(startBlock, next.getValue() + length);
                blocksTable.remove(next.getKey());
            } else
                blocksTable.put(startBlock, length);
        }
    }
 
    public void deallocateFile(MyFile file) {
        int startBlock = file.getStartBlock();
        int requiredBlocks = file.getRequiredBlocks();
 
        for (int i = startBlock; i < startBlock + requiredBlocks; i++)
            blocks.get(i).setAllocated(false);
 
        files.remove(file);
        update(startBlock, requiredBlocks);
    }
 
    public MyFile getFileByName(String name) {
        for (MyFile file : files)
            if (file.getName().equals(name))
                return file;
        return null;
    }
 
    public String printDisk() {
        StringBuilder sb = new StringBuilder("Name Start Length\n");
 
        for (MyFile file : files)
            sb.append(file).append("\n");
 
        return sb.toString();
    }
}
