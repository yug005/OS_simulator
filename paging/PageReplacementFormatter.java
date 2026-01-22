package paging;

import java.util.ArrayList;
import java.util.List;

public final class PageReplacementFormatter {
    private PageReplacementFormatter() {
    }

    public static String formatReport(String algorithmName, int[] pageReferenceString, int frameCount, PageReplacementResult result) {
        // Formatter ka goal: clean report + steps + summary.
        if (algorithmName == null) {
            algorithmName = "(unknown)";
        }
        if (pageReferenceString == null) {
            pageReferenceString = new int[0];
        }
        if (result == null) {
            result = new PageReplacementResult(0, 0, List.of());
        }

        int totalPages = pageReferenceString.length;
        int faults = result.getTotalPageFaults();
        int hits = result.getTotalHits();
        // Hit ratio = hits / total references.
        double hitRatio = totalPages == 0 ? 0.0 : hits / (double) totalPages;

        StringBuilder out = new StringBuilder();
        out.append("Algorithm: ").append(algorithmName).append(System.lineSeparator());
        out.append("Total Pages: ").append(totalPages).append(System.lineSeparator());
        out.append("Frames: ").append(frameCount).append(System.lineSeparator());

        List<String> stepLogs = result.getStepLogs();
        if (stepLogs != null && !stepLogs.isEmpty()) {
            out.append(System.lineSeparator());
            out.append("Steps:").append(System.lineSeparator());
            out.append(formatSteps(stepLogs));
        }

        out.append(System.lineSeparator());
        out.append("Summary:").append(System.lineSeparator());
        out.append("Page Faults: ").append(faults).append(System.lineSeparator());
        out.append("Total Hits: ").append(hits).append(System.lineSeparator());
        out.append("Hit Ratio: ").append(String.format("%.2f", hitRatio)).append(System.lineSeparator());

        return out.toString();
    }

    public static String formatSteps(List<String> stepLogs) {
        // Steps ko align karne se output readable lagta hai.
        if (stepLogs == null || stepLogs.isEmpty()) {
            return "(no steps)";
        }

        List<ParsedStep> parsed = new ArrayList<>(stepLogs.size());
        int maxFrameLen = 0;

        for (String line : stepLogs) {
            ParsedStep step = ParsedStep.tryParse(line);
            parsed.add(step);
            if (step != null && step.frames != null) {
                maxFrameLen = Math.max(maxFrameLen, step.frames.length());
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stepLogs.size(); i++) {
            ParsedStep step = parsed.get(i);
            if (step == null) {
                sb.append(stepLogs.get(i)).append(System.lineSeparator());
                continue;
            }

            String framesPadded = padRight(step.frames, maxFrameLen);
            sb.append(String.format("%2d. Page: %-3s  Frames: %s  %s", i + 1, step.page, framesPadded, step.outcome))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static String padRight(String s, int width) {
        // Align columns: chhote string ko spaces se pad karte hain.
        if (s == null) {
            s = "";
        }
        if (s.length() >= width) {
            return s;
        }
        StringBuilder sb = new StringBuilder(width);
        sb.append(s);
        while (sb.length() < width) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static final class ParsedStep {
        private final String page;
        private final String frames;
        private final String outcome;

        private ParsedStep(String page, String frames, String outcome) {
            this.page = page;
            this.frames = frames;
            this.outcome = outcome;
        }

        private static ParsedStep tryParse(String line) {
            // FIFO/LRU logs ka format parse karke columns nikalte hain.
            if (line == null) {
                return null;
            }

            // Expected format (from FIFO/LRU):
            // Page: X → Frames: [..] → Hit/Fault
            String arrow = "→";
            String marker1 = "Page:";
            String marker2 = "Frames:";

            int pageIdx = line.indexOf(marker1);
            int framesIdx = line.indexOf(marker2);
            if (pageIdx < 0 || framesIdx < 0) {
                return null;
            }

            int firstArrow = line.indexOf(arrow);
            int secondArrow = firstArrow < 0 ? -1 : line.indexOf(arrow, firstArrow + arrow.length());
            if (firstArrow < 0 || secondArrow < 0) {
                return null;
            }

            String pagePart = line.substring(pageIdx + marker1.length(), firstArrow).trim();
            String framesPart = line.substring(framesIdx + marker2.length(), secondArrow).trim();
            String outcomePart = line.substring(secondArrow + arrow.length()).trim();

            if (pagePart.isEmpty() || framesPart.isEmpty() || outcomePart.isEmpty()) {
                return null;
            }

            return new ParsedStep(pagePart, framesPart, outcomePart);
        }
    }
}
