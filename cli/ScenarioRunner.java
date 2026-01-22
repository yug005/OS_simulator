package cli;

public class ScenarioRunner {
    public void run(Scenario scenario) {
        // User ko clarity: kaunsa scenario run ho raha hai.
        System.out.println("Running: " + scenario.getName());
        scenario.execute();
    }
}
