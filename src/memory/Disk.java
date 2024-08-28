package memory;
 
import file_system.MyFile;
 
import java.util.*;
 
public class Disk {
    private final int size;
    private final List<Block> blocks;
    private final NavigableMap<Integer, Integer> blocksTable;
    private final Set<MyFile> files;
 
    public Disk() {
        this.size = 4096;
        this.blocks = new ArrayList<>();
        this.blocksTable = new TreeMap<>();
        this.files = new TreeSet<>(MyFile.compareByStartBlock);
 
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
 
    public Set<MyFile> getFiles() {
        return files;
    }
 
    private int findStartBlock(int length) {
        if (files.isEmpty())
            return 0;

        return blocksTable.entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= length)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
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
        file.setStartBlock(startBlock);
        files.add(file);
    }
 
    private void updateBlocksTable(int startBlock, int length) {
        Map.Entry<Integer, Integer> prev = blocksTable.lowerEntry(startBlock);
        Map.Entry<Integer, Integer> next = blocksTable.higherEntry(startBlock);

        System.out.println(prev);
        System.out.println(next);

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
        updateBlocksTable(startBlock, requiredBlocks);
    }
 
    public MyFile getFileByName(String name) {
        return files.stream()
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
 
    public void printDisk() {
        System.out.printf("%-20s\t\t %-5s\t\t\t %-5s\n", "NAME", "START", "LENGTH");
        System.out.println("-------------------------------------------------------------");
        files.forEach(System.out::print);
    }
}
