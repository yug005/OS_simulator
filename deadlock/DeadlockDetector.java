package deadlock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeadlockDetector {

    public static Set<String> detectDeadlockedProcesses(WaitForGraph wfg) {
        // DFS cycle detection: cycle = deadlock.
        Set<String> deadlocked = new HashSet<>();
        Map<String, Set<String>> edges = wfg.getEdges();

        Map<String, VisitState> state = new HashMap<>();
        for (String node : edges.keySet()) {
            state.put(node, VisitState.UNVISITED);
        }

        for (String node : edges.keySet()) {
            if (state.get(node) == VisitState.UNVISITED) {
                // Unvisited node se DFS start.
                dfs(node, edges, state, new HashSet<>(), deadlocked);
            }
        }

        return deadlocked;
    }

    public static void printDeadlockedProcesses(Set<String> deadlocked) {
        // Simple console print for results.
        if (deadlocked == null || deadlocked.isEmpty()) {
            System.out.println("No deadlock detected.");
            return;
        }
        System.out.println("Deadlocked processes: " + deadlocked);
    }

    private static void dfs(
            String node,
            Map<String, Set<String>> edges,
            Map<String, VisitState> state,
            Set<String> recursionStack,
            Set<String> deadlocked
    ) {
        // DFS recursion stack track karta hai cycle detection ke liye.
        state.put(node, VisitState.VISITING);
        recursionStack.add(node);

        for (String next : edges.getOrDefault(node, Set.of())) {
            if (state.getOrDefault(next, VisitState.UNVISITED) == VisitState.UNVISITED) {
                dfs(next, edges, state, recursionStack, deadlocked);
            } else if (recursionStack.contains(next)) {
                // Cycle found: stack ke saare nodes deadlocked maan lo.
                deadlocked.addAll(recursionStack);
            }
        }

        recursionStack.remove(node);
        state.put(node, VisitState.VISITED);
    }

    private enum VisitState {
        UNVISITED,
        VISITING,
        VISITED
    }
}
