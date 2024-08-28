package memory;

public class Frame {
    public static final int SIZE = 32;
    private final String frameNumber;
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

    public void setPage(Page page) {
        this.page = page;
        allocated = true;
        bytesAllocated = page.getTotalSize();
    }

    public void free() {
        page = null;
        bytesAllocated = 0;
        allocated = false;
    }

    @Override
    public String toString() {
        return frameNumber + " " + (page == null ? "[]" : page);
    }
}
