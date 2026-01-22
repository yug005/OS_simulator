package disk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadMovementGraph {
    // moves: fromTrack -> list of toTrack (movement history).
    private final Map<Integer, List<Integer>> moves;

    public HeadMovementGraph() {
        this.moves = new HashMap<>();
    }

    public void addMove(int fromTrack, int toTrack) {
        // Directed move record: from -> to.
        moves.computeIfAbsent(fromTrack, k -> new ArrayList<>()).add(toTrack);
    }

    public void recordSequence(int headStart, List<Integer> serviceOrder) {
        // Service order ko edge list me convert karte hain.
        int head = headStart;
        for (Integer next : serviceOrder) {
            if (next == null) {
                continue;
            }
            addMove(head, next);
            head = next;
        }
    }

    public Map<Integer, List<Integer>> getMoves() {
        Map<Integer, List<Integer>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : moves.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        return Collections.unmodifiableMap(copy);
    }

    @Override
    public String toString() {
        return "HeadMovementGraph{" +
                "moves=" + moves +
                '}';
    }
}
