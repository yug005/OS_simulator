package cli;

import java.util.Scanner;

public class InputParser {
    private final Scanner scanner;

    public InputParser() {
        // Single scanner instance, repeated input ke liye.
        this.scanner = new Scanner(System.in);
    }

    public int readIntInRange(String prompt, int min, int max) {
        // Range validation, invalid input ko handle karta hai.
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value < min || value > max) {
                    System.out.println("Please enter a value between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public int readNonNegative(String prompt) {
        // Negative inputs ko reject karna basic defensive coding hai.
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value < 0) {
                    System.out.println("Please enter a non-negative number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public int readTrack(String prompt, int diskSize) {
        // Disk track limit check: 0 to diskSize-1.
        int maxTrack = diskSize - 1;
        return readIntInRange(prompt, 0, maxTrack);
    }
}
