package synchronization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateTransitionGraph {
    private final Map<State, List<State>> transitions;

    public StateTransitionGraph() {
        // Graph: fromState -> allowed next states.
        this.transitions = new HashMap<>();
    }

    public void addTransition(State from, State to) {
        // Allowed transition add karte hain, taaki later validation ho sake.
        transitions.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public boolean isValidTransition(State from, State to) {
        // Runtime check: kya from -> to allowed hai ya nahi.
        return transitions.getOrDefault(from, Collections.emptyList()).contains(to);
    }

    public List<State> getTransitionsFrom(State from) {
        return transitions.containsKey(from)
                ? Collections.unmodifiableList(transitions.get(from))
                : Collections.emptyList();
    }

    public Map<State, List<State>> getTransitions() {
        Map<State, List<State>> copy = new HashMap<>();
        for (Map.Entry<State, List<State>> entry : transitions.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }
}
