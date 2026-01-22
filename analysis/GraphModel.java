package analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GraphModel {
    // adj = algorithm -> (other algorithm -> metric diff weight)
    private final Map<String, Map<String, Double>> adj;

    public GraphModel() {
        this.adj = new HashMap<>();
    }

    public void addEdge(String fromAlgorithm, String toAlgorithm, double weight) {
        // Directed edge: fromAlgorithm -> toAlgorithm with weight.
        adj.computeIfAbsent(fromAlgorithm, k -> new HashMap<>()).put(toAlgorithm, weight);
    }

    public Map<String, Map<String, Double>> getAdjacency() {
        // Read-only copy so external mutation na ho.
        Map<String, Map<String, Double>> copy = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : adj.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableMap(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }
}
