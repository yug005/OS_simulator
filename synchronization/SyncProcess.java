package synchronization;

public class SyncProcess {
    private final String processId;
    private State state;

    public SyncProcess(String processId, State initialState) {
        this.processId = processId;
        this.state = initialState;
    }

    public String getProcessId() {
        return processId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        // Direct state update, mostly internal use.
        this.state = state;
    }

    public boolean transitionTo(StateTransitionGraph graph, State newState, int time) {
        // Transition validate karna important hai, isse invalid jumps catch hote hain.
        if (graph != null && !graph.isValidTransition(this.state, newState)) {
            System.out.println("Time " + time + ": " + processId + " " + state + " -> " + newState + " (INVALID)");
            return false;
        }
        // Valid transition: timeline log ban jata hai, analysis easy hota hai.
        System.out.println("Time " + time + ": " + processId + " " + state + " -> " + newState);
        this.state = newState;
        return true;
    }

    @Override
    public String toString() {
        return "SyncProcess{" +
                "processId='" + processId + '\'' +
                ", state=" + state +
                '}';
    }
}
