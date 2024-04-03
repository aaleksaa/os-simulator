package memory;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    private final int size;
    private List<Frame> frames = new ArrayList<>();
    private List<Integer> freeFrames = new ArrayList<>();
    private int frameSize;
    private int numberOfFrames;

    public RAM() {
        this.size = 4096;
        this.frameSize = 32;
        this.numberOfFrames = 128;
        init(numberOfFrames);
    }

    public RAM(int size, int frameSize) {
        this.size = 4096;
        this.frameSize = frameSize;
        this.numberOfFrames = size / frameSize;
    }

    private void init(int n) {
        for (int i = 0; i < n; i++) {
            String binaryNumber = decToBinary(i);
            String newBinary = "";

            for (int j = 0; j < powerOfTwo(n - binaryNumber.length()); j++)
                newBinary += "0";

            newBinary += binaryNumber;
            frames.add(new Frame(newBinary));
        }

        for (int i = 0; i < n; i++)
            freeFrames.add(i);
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

    public static void main(String[] args) {
        RAM ram = new RAM();
        for (Frame f : ram.frames)
            System.out.println(f);
    }
}
