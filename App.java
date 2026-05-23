import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("==================================================");
        System.out.println("         ATM SYSTEM INITIALIZING         ");
        System.out.println("==================================================");
        
        ATM atm = new ATM();
        atm.loadAccountsFromFile("accounts.csv");
        
        boolean continueRunning = true;
        
        while (continueRunning) {
            System.out.println("\n==================================================");
            System.out.println("            MAIN MENU                    ");
            System.out.println("==================================================");
            System.out.println("1. Login to ATM");
            System.out.println("2. Exit");
            System.out.println("==================================================");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    atm.loginMenu();
                    break;
                case "2":
                    System.out.println("\nThank you for using our ATM System!");
                    System.out.println("Goodbye!");
                    continueRunning = false;
                    break;
                default:
                    System.out.println("✗ Invalid option! Please choose 1 or 2.");
            }
        }
        
        scanner.close();
    }
}