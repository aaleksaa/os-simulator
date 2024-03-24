package memory;

public class Block {
    public static final int SIZE = 4;
    private int startAddress;
    private boolean allocated;

    public Block(int startAddress) {
        this.startAddress = startAddress;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }
}
