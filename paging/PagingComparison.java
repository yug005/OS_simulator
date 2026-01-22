package paging;

public class PagingComparison {
    public static void main(String[] args) {
    // Same input pe FIFO vs LRU compare karna purpose hai.
        runCase(
                "Case A (your FIFO validation)",
                new int[]{1, 3, 0, 3, 5, 6},
                3
        );

        // Classic OS example that usually shows FIFO vs LRU difference
        runCase(
                "Case B (classic LRU vs FIFO)",
                new int[]{7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2},
                3
        );

        // Another common reference string often used in textbooks/interviews
        runCase(
                "Case C (textbook reference string)",
                new int[]{1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5},
                3
        );
    }

    private static void runCase(String label, int[] pages, int frames) {
        // Dono algorithms ko same pages + frames dena (fair comparison).
        FIFOPageReplacement fifo = new FIFOPageReplacement();
        LRUPageReplacement lru = new LRUPageReplacement();

        PageReplacementResult fifoResult = fifo.simulate(pages, frames);
        PageReplacementResult lruResult = lru.simulate(pages, frames);

        System.out.println("Paging Comparison (same input, same frames)");
        System.out.println(label);
        System.out.println("Pages: " + pagesToString(pages));
        System.out.println();

        System.out.println(PageReplacementFormatter.formatReport("FIFO", pages, frames, fifoResult));
        System.out.println(PageReplacementFormatter.formatReport("LRU", pages, frames, lruResult));
    }

    private static String pagesToString(int[] pages) {
        // Helper: reference string ko readable format me print karte hain.
        if (pages == null || pages.length == 0) {
            return "(empty)";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pages.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(pages[i]);
        }
        return sb.toString();
    }

    // Summary printing is handled by PageReplacementFormatter.
}
