package memory;

public class Frame {
    private String frameNumber;
    private boolean allocated;
    private int bytesAllocated;
    private Page page;

    public Frame(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public int getBytesAllocated() {
        return bytesAllocated;
    }

    public Page getPage() {
        return page;
    }

    @Override
    public String toString() {
        return frameNumber;
    }
}
