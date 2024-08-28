package memory;

import cpu.Process;
import cpu.ProcessState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RAM {
    private final int size;
    private final List<Frame> frames;
    private final List<Integer> freeFrames;
    private final int numberOfFrames;

    public RAM(int size) {
        this.frames = new ArrayList<>();
        this.freeFrames = new ArrayList<>();
        this.size = size;
        this.numberOfFrames = size / Frame.SIZE;
        init(numberOfFrames);
    }

    private void init(int n) {
        for (int i = 0; i < n; i++) {
            String hexNumber = Integer.toHexString(i).toUpperCase();
            frames.add(new Frame(hexNumber));
            freeFrames.add(i);
        }
    }

    public int getSize() {
        return size;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public List<Integer> getFreeFrames() {
        return freeFrames;
    }

    public void load(Process process) throws IllegalArgumentException {
        List<Page> pages = process.getPages();
        int pageCounter = 0;

        if (freeFrames.size() < pages.size())
            throw new IllegalArgumentException("Not enough space for process!\n");

        while (pageCounter < pages.size()) {
            int index = freeFrames.get(0);
            process.addToPageTable(frames.get(index).getFrameNumber());
            frames.get(index).setPage(pages.get(pageCounter++));
            freeFrames.remove(0);
        }

        process.setState(ProcessState.READY);
    }

    public int getBytesAllocated() {
        return frames.stream().filter(Frame::isAllocated).mapToInt(Frame::getBytesAllocated).sum();
    }

    public String getInstruction(String frameNumber, int pageIndex) {
        int frameIndex = Integer.parseInt(frameNumber, 16);
        Frame frame = frames.get(frameIndex);
        Page page = frame.getPage();

        return page.getContent().get(pageIndex);
    }

    public void remove(Process process) {
        List<String> pageTable = process.getPageTable();

        for (String page : pageTable) {
            int index = Integer.parseInt(page, 16);
            frames.get(index).free();
            freeFrames.add(index);
        }

        freeFrames.sort(Integer::compareTo);
    }

    public void printMemory() {
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s%n", "TOTAL", "USED", "FREE");
        System.out.printf("%-22s %-22s %-22s%n", size, getBytesAllocated(), (size - getBytesAllocated()));
        System.out.println("------------------------------------------------------------------------");
        frames.stream().filter(Frame::isAllocated).forEach(System.out::println);
    }
}
