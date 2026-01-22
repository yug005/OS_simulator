package paging;

public class FIFOValidation {
    public static void main(String[] args) {
        // Simple validation: chhota example to quickly verify FIFO.
        int[] pages = {1, 3, 0, 3, 5, 6};
        int frames = 3;

        FIFOPageReplacement fifo = new FIFOPageReplacement();
        PageReplacementResult result = fifo.simulate(pages, frames);

        System.out.println("FIFO Validation");
        System.out.println("Pages: 1 3 0 3 5 6");
        System.out.println("Frames: 3");
        System.out.println();

        for (String log : result.getStepLogs()) {
            System.out.println(log);
        }

        System.out.println();
        System.out.println("Total Faults: " + result.getTotalPageFaults());
        System.out.println("Total Hits: " + result.getTotalHits());
    }
}
