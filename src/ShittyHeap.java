public class ShittyHeap {
    private int[] tasks;
    private int size;

    public ShittyHeap(int size) {
        tasks = new int[size];
        this.size = 0;
        tasks[0] = 0;
    }

    public int getMin() {
        if (isEmpty()) throw new HeapException("Heap is empty you moron");
        else
            return tasks[1];
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    private int getLeftChildIndex(int nodeIndex) {
        return 2 * nodeIndex;
    }

    private int getRightChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 1;
    }

    private int getParentIndex(int nodeIndex) {
        return nodeIndex / 2;
    }

    public class HeapException extends RuntimeException {
        public HeapException (String message) {
            super(message);
        }
    }
}
