package memory;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    public int size;
    private List<Frame> frames = new ArrayList<>();
    private List<Integer> freeFrames = new ArrayList<>();
    private int frameSize;
    private int numberOfFrames;

    public RAM(int size) {
        this.size = size;
        this.frameSize = 32;
        this.numberOfFrames = size / frameSize;
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
}
