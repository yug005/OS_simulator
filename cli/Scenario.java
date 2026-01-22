package cli;

public class Scenario {
    // scenarioName + description = UI friendly detail
    private final String scenarioName;
    private final String description;
    // logic = runnable task (scenario ka actual execution)
    private final Runnable logic;

    public Scenario(String scenarioName, String description, Runnable logic) {
        this.scenarioName = scenarioName;
        this.description = description;
        this.logic = logic;
    }

    public String getName() {
        return scenarioName;
    }

    public String getDescription() {
        return description;
    }

    public void execute() {
        // Null guard: agar logic missing ho to crash nahi hoga.
        if (logic != null) {
            logic.run();
        }
    }
}
