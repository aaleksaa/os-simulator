package memory;

import cpu.Process;
import cpu.ProcessState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RAM {
    private final int size;
    private List<Frame> frames;
    private List<Integer> freeFrames;
    private int frameSize;
    private int numberOfFrames;
    private Queue<Process> readyQueue;
    private Process currentProcess;

    public RAM() {
        this.size = 4096;
        this.frameSize = 32;
        this.numberOfFrames = 128;
        this.frames = new ArrayList<>();
        this.freeFrames = new ArrayList<>();
        this.readyQueue = new LinkedList<>();
        init(numberOfFrames);
    }

    public RAM(int size, int frameSize) {
        this.frames = new ArrayList<>();
        this.freeFrames = new ArrayList<>();
        this.readyQueue = new LinkedList<>();
        this.size = size;
        this.frameSize = frameSize;
        this.numberOfFrames = size / frameSize;
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

    public int getFrameSize() {
        return frameSize;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public List<Integer> getFreeFrames() {
        return freeFrames;
    }

    public void load(Process process) throws IllegalArgumentException {
        List<Page> pages = process.getPages();
        int numberOfPages = pages.size();
        int pageCounter = 0;

        if (freeFrames.size() < numberOfPages)
            throw new IllegalArgumentException("Not enough space for process!\n" + numberOfPages);

        while (pageCounter < numberOfPages) {
            int index = freeFrames.get(0);
            process.addToPageTable(frames.get(index).getFrameNumber());
            frames.get(index).setPage(pages.get(pageCounter++));
            freeFrames.remove(0);
        }

        readyQueue.add(process);
        process.setState(ProcessState.READY);
    }

    public void remove(Process process) {
        List<String> pageTable = process.getPageTable();
        for (String s : pageTable) {
            int index = Integer.parseInt(s, 16);
            frames.get(index).free();
            freeFrames.add(index);
        }
    }

    public String printMemory() {
        StringBuilder sb = new StringBuilder();

        for (Frame frame : frames)
            sb.append(frame).append("\n");

        return sb.toString();
    }
}
