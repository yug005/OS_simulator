package memory;

public class MemoryBlock {
    // blockId = unique identifier, debugging ke liye helpful.
    private int blockId;
    // startAddress = memory me block ka start location.
    private int startAddress;
    // size = block ka total size.
    private int size;
    // isFree = block available hai ya allocated.
    private boolean isFree;
    // allocatedProcessId = kis process ko block diya hai.
    private Integer allocatedProcessId;
    // requestedSize = process ne kitna size manga tha (internal frag calculate karne ke liye).
    private Integer requestedSize;

    public MemoryBlock(int blockId, int startAddress, int size) {
        this.blockId = blockId;
        this.startAddress = startAddress;
        this.size = size;
        this.isFree = true;
        this.allocatedProcessId = null;
        this.requestedSize = null;
    }

    /**
     * Legacy constructor kept for convenience.
     * Uses blockId = -1.
     */
    public MemoryBlock(int startAddress, int size) {
        this(-1, startAddress, size);
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        // Free karte time allocated process + requested size clear karna zaroori hai.
        isFree = free;
        if (free) {
            this.allocatedProcessId = null;
            this.requestedSize = null;
        }
    }

    public Integer getAllocatedProcessId() {
        return allocatedProcessId;
    }

    public void setAllocatedProcessId(Integer allocatedProcessId) {
        // Allocation ke saath isFree flag update karna simple & safe banata hai.
        this.allocatedProcessId = allocatedProcessId;
        this.isFree = allocatedProcessId == null;
        if (allocatedProcessId == null) {
            this.requestedSize = null;
        }
    }

    public Integer getRequestedSize() {
        return requestedSize;
    }

    public void setRequestedSize(Integer requestedSize) {
        this.requestedSize = requestedSize;
    }

    @Override
    public String toString() {
        return "MemoryBlock{" +
                "blockId=" + blockId +
                ", startAddress=" + startAddress +
                ", size=" + size +
                ", isFree=" + isFree +
                ", allocatedProcessId=" + allocatedProcessId +
                ", requestedSize=" + requestedSize +
                '}';
    }
}
