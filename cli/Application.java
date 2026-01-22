package cli;

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String[] args) {
                // CLI entry point: menu + scenario routing.
        Menu menu = new Menu();
        InputParser input = new InputParser();
        ScenarioRunner runner = new ScenarioRunner();

        List<Scenario> scenarios = buildScenarios();

        while (true) {
                        // Main loop: show menu, get input, run scenario.
            menu.showMainMenu();
            int choice = input.readIntInRange("Select an option: ", 0, 6);

            if (choice == 0) {
                System.out.println("Exiting.");
                break;
            }

                        // choice -> scenario list index
            Scenario scenario = scenarios.get(choice - 1);
            System.out.println("Scenario: " + scenario.getName());
            System.out.println(scenario.getDescription());
            runner.run(scenario);
            System.out.println();
        }
    }

    private static List<Scenario> buildScenarios() {
                // Predefined demo scenarios, future me actual logic attach ho sakta hai.
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new Scenario(
                "High CPU Burst Load",
                "Simulates heavy CPU scheduling workload.",
                () -> System.out.println("CPU Scheduling scenario placeholder")
        ));
        scenarios.add(new Scenario(
                "Paging Pressure",
                "Simulates high page fault pressure.",
                () -> System.out.println("Paging scenario placeholder")
        ));
        scenarios.add(new Scenario(
                "Memory Fragmentation Stress",
                "Simulates fragmentation-heavy allocation patterns.",
                () -> System.out.println("Memory allocation scenario placeholder")
        ));
        scenarios.add(new Scenario(
                "Disk Heavy Workload",
                "Simulates heavy disk requests.",
                () -> System.out.println("Disk scheduling scenario placeholder")
        ));
        scenarios.add(new Scenario(
                "Deadlock Situation",
                "Simulates potential deadlock in synchronization.",
                () -> System.out.println("Synchronization scenario placeholder")
        ));
        scenarios.add(new Scenario(
                "Performance Analysis",
                "Compares algorithm performance across modules.",
                () -> System.out.println("Performance analysis scenario placeholder")
        ));
        return scenarios;
    }
}
