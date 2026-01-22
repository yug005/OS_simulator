package cli;

public class Menu {
    public void showMainMenu() {
        // Simple numbered menu, future modules ke liye extendable.
        System.out.println("1. CPU Scheduling");
        System.out.println("2. Paging");
        System.out.println("3. Memory Allocation");
        System.out.println("4. Disk Scheduling");
        System.out.println("5. Synchronization");
        System.out.println("6. Performance Analysis");
        System.out.println("0. Exit");
    }
}
