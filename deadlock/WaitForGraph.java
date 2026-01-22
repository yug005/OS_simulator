package deadlock;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WaitForGraph {
    // edges: P1 -> P2 means P1 is waiting on resource held by P2.
    private final Map<String, Set<String>> edges;

    public WaitForGraph() {
        this.edges = new HashMap<>();
    }

    public void addEdge(String fromProcess, String toProcess) {
        // Self-edge ignore, warna trivial cycle ho jayega.
        if (fromProcess.equals(toProcess)) {
            return;
        }
        edges.computeIfAbsent(fromProcess, k -> new HashSet<>()).add(toProcess);
    }

    public Map<String, Set<String>> getEdges() {
        return Collections.unmodifiableMap(edges);
    }

    public Set<String> getProcesses() {
        return edges.keySet();
    }

    public static WaitForGraph fromResourceAllocationGraph(ResourceAllocationGraph rag) {
        // RAG -> WFG conversion: P -> R -> P becomes P -> P.
        WaitForGraph wfg = new WaitForGraph();
        Map<String, Set<String>> requests = rag.getProcessRequests();
        Map<String, String> assignments = rag.getResourceAssignments();

        for (Map.Entry<String, Set<String>> entry : requests.entrySet()) {
            String processId = entry.getKey();
            for (String resourceId : entry.getValue()) {
                String assignedTo = assignments.get(resourceId);
                if (assignedTo != null) {
                    // Agar resource kisi aur ke paas hai, to wait-for edge add karo.
                    wfg.addEdge(processId, assignedTo);
                }
            }
        }

        return wfg;
    }
}
