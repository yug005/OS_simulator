package deadlock;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResourceAllocationGraph {
    // processRequests: P -> resources it is waiting for.
    private final Map<String, Set<String>> processRequests;
    // resourceAssignments: R -> process currently holding it.
    private final Map<String, String> resourceAssignments;

    public ResourceAllocationGraph() {
        this.processRequests = new HashMap<>();
        this.resourceAssignments = new HashMap<>();
    }

    public void addRequestEdge(String processId, String resourceId) {
        // Process request edge add karte hain (P -> R).
        processRequests.computeIfAbsent(processId, k -> new HashSet<>()).add(resourceId);
    }

    public void addAssignmentEdge(String resourceId, String processId) {
        // Resource assignment edge add karte hain (R -> P).
        resourceAssignments.put(resourceId, processId);
    }

    public Map<String, Set<String>> getProcessRequests() {
        return Collections.unmodifiableMap(processRequests);
    }

    public Map<String, String> getResourceAssignments() {
        return Collections.unmodifiableMap(resourceAssignments);
    }

    public Set<String> getProcesses() {
        // Process list: request map se: jo wait kar rahe hain.
        return processRequests.keySet();
    }
}
