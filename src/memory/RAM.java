package memory;

import cpu.Process;
import cpu.ProcessState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RAM {
    private final int size;
    private List<Frame> frames = new ArrayList<>();
    private List<Integer> freeFrames = new ArrayList<>();
    private int frameSize;
    private int numberOfFrames;
    private Queue<Process> readyQueue = new LinkedList<>();
    private Process currentProcess = null;

    public RAM() {
        this.size = 4096;
        this.frameSize = 32;
        this.numberOfFrames = 128;
        init(numberOfFrames);
    }

    public RAM(int size, int frameSize) {
        this.size = size;
        this.frameSize = frameSize;
        this.numberOfFrames = size / frameSize;
        init(numberOfFrames);
    }

    private void init(int n) {
        for (int i = 0; i < n; i++) {
            String binaryNumber = decToBinary(i);
            String newBinary = "";

            for (int j = 0; j < powerOfTwo(n) - binaryNumber.length(); j++)
                newBinary += "0";

            newBinary += binaryNumber;
            frames.add(new Frame(newBinary));
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

    public void load(Process process) {
        List<Page> pages = process.getPages();
        int numberOfPages = pages.size();
        int pageCounter = 0;

        if (freeFrames.size() < numberOfPages)
            throw new IllegalArgumentException("Not enough space for process!");

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
        for (int i = 0; i < pageTable.size(); i++) {
            int index = Integer.parseInt(pageTable.get(i), 2);
            frames.get(index).free();
            freeFrames.add(index);
        }
    }

    private static String decToBinary(int n) {
        String binaryNumber = "";
        int[] binaryNum = new int[1000];
        int i = 0;

        while (n > 0) {
            binaryNum[i] = n % 2;
            n /= 2;
            i++;
        }

        for (int j = i - 1; j >= 0; j--)
            binaryNumber += String.valueOf(binaryNum[j]);

        return binaryNumber;
    }


    private static int powerOfTwo(int size) {
        int i = 1;
        int counter = 0;

        while (i <= size) {
            i *= 2;
            counter++;
        }

        return (i / 2 == size) ? --counter : -1;
    }
}
