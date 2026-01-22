package cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionTimelineGraph {
    // edges: event -> next events (happens-before)
    private final Map<String, List<String>> edges;
    // events list: insertion order preserve for timeline.
    private final List<String> events;

    public ExecutionTimelineGraph() {
        this.edges = new HashMap<>();
        this.events = new ArrayList<>();
    }

    public void addEvent(String eventId) {
        // Basic guard: empty events skip.
        if (eventId == null || eventId.isBlank()) {
            return;
        }
        events.add(eventId);
    }

    public void addEdge(String fromEvent, String toEvent) {
        // Directed edge: from -> to.
        if (fromEvent == null || toEvent == null || fromEvent.equals(toEvent)) {
            return;
        }
        edges.computeIfAbsent(fromEvent, k -> new ArrayList<>()).add(toEvent);
    }

    public void recordSequence(List<String> sequence) {
        // Sequence ko edges me convert: e1->e2->e3.
        if (sequence == null || sequence.isEmpty()) {
            return;
        }
        String prev = null;
        for (String event : sequence) {
            addEvent(event);
            if (prev != null) {
                addEdge(prev, event);
            }
            prev = event;
        }
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public Map<String, List<String>> getEdges() {
        Map<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : edges.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    @Override
    public String toString() {
        return "ExecutionTimelineGraph{" +
                "events=" + events +
                ", edges=" + edges +
                '}';
    }
}
