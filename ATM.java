import java.util.*;
import java.io.*;

public class ATM {
    private HashMap<String, Account> accounts;
    private Scanner scanner;
    private Account loggedInAccount;
    
    public ATM() {
        accounts = new HashMap<>();
        scanner = new Scanner(System.in);
        loggedInAccount = null;
    }
    
    public void loadAccountsFromFile(String filename) {
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("File " + filename + " not found!");
            System.out.println("Creating sample accounts file...");
            createSampleAccountsFile(filename);
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine && (line.contains("account") || line.contains("Account"))) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String accountNumber = data[0].trim();
                    String pin = data[1].trim();
                    double balance = Double.parseDouble(data[2].trim());
                    String holderName = data[3].trim();
                    
                    Account account = new Account(accountNumber, pin, balance, holderName);
                    accounts.put(accountNumber, account);
                    count++;
                }
            }
            System.out.println("✓ Loaded " + count + " accounts from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing balance: " + e.getMessage());
        }
    }
    
    private void createSampleAccountsFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("account_number,pin,balance,holder_name");
            writer.println("1234567890,1234,5000.00,John Doe");
            writer.println("0987654321,4321,3500.50,Jane Smith");
            writer.println("1111222233,1111,10000.00,Bob Johnson");
            writer.println("4444555566,2222,2500.75,Alice Brown");
            writer.println("7777888899,3333,8200.00,Charlie Wilson");
            writer.println("5555666677,5678,12500.00,Sarah Davis");
            writer.println("8888999900,8901,3200.25,Michael Lee");
            
            System.out.println("✓ Created sample accounts file: " + filename);
            loadAccountsFromFile(filename);
        } catch (IOException e) {
            System.out.println("Error creating sample file: " + e.getMessage());
        }
    }
    
    public boolean validateCredentials(String accountNumber, String pin) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.getPin().equals(pin)) {
            loggedInAccount = account;
            return true;
        }
        return false;
    }
    
    public void loginMenu() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.println("\n==================================================");
            System.out.println("          WELCOME TO ATM MACHINE          ");
            System.out.println("==================================================");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine();
            
            System.out.print("Enter PIN (4 digits): ");
            String pin = scanner.nextLine();
            
            if (validateCredentials(accountNumber, pin)) {
                System.out.println("\n✓ Login Successful!");
                System.out.println("Welcome, " + loggedInAccount.getAccountHolderName() + "!");
                atMenu();
                return;
            } else {
                attempts++;
                System.out.println("\n✗ Invalid account number or PIN!");
                System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
            }
        }
        
        System.out.println("\n⚠ Too many failed attempts. Account locked!");
        System.out.println("Please contact your bank.");
    }
    
    public void atMenu() {
        int choice;
        
        do {
            System.out.println("\n==================================================");
            System.out.println("                ATM MENU                  ");
            System.out.println("==================================================");
            System.out.println("Welcome: " + loggedInAccount.getAccountHolderName());
            System.out.println("--------------------------------------------------");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Account Information");
            System.out.println("5. Change PIN");
            System.out.println("6. Logout");
            System.out.println("==================================================");
            System.out.print("Enter your choice (1-6): ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        checkBalance();
                        break;
                    case 2:
                        depositMoney();
                        break;
                    case 3:
                        withdrawMoney();
                        break;
                    case 4:
                        showAccountInfo();
                        break;
                    case 5:
                        changePin();
                        break;
                    case 6:
                        logout();
                        break;
                    default:
                        System.out.println("✗ Invalid option! Please choose 1-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("✗ Please enter a valid number!");
                choice = 0;
            }
        } while (choice != 6);
    }
    
    private void checkBalance() {
        System.out.println("\n--- BALANCE INQUIRY ---");
        System.out.printf("Current Balance: $%.2f\n", loggedInAccount.getBalance());
    }
    
    private void depositMoney() {
        System.out.println("\n--- DEPOSIT MONEY ---");
        System.out.print("Enter deposit amount: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                loggedInAccount.deposit(amount);
                System.out.printf("New balance: $%.2f\n", loggedInAccount.getBalance());
            } else {
                System.out.println("✗ Amount must be positive!");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid input! Please enter a valid amount.");
        }
    }
    
    private void withdrawMoney() {
        System.out.println("\n--- WITHDRAW MONEY ---");
        System.out.print("Enter withdrawal amount: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                if (loggedInAccount.withdraw(amount)) {
                    System.out.printf("✓ Withdrew $%.2f successfully!\n", amount);
                    System.out.printf("New balance: $%.2f\n", loggedInAccount.getBalance());
                } else {
                    System.out.println("✗ Insufficient funds!");
                    System.out.printf("Your current balance is: $%.2f\n", loggedInAccount.getBalance());
                }
            } else {
                System.out.println("✗ Amount must be positive!");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid input! Please enter a valid amount.");
        }
    }
    
    private void showAccountInfo() {
        System.out.println("\n--- ACCOUNT INFORMATION ---");
        loggedInAccount.displayInfo();
    }
    
    private void changePin() {
        System.out.println("\n--- CHANGE PIN ---");
        System.out.print("Enter current PIN: ");
        String currentPin = scanner.nextLine();
        
        if (loggedInAccount.getPin().equals(currentPin)) {
            System.out.print("Enter new PIN (4 digits): ");
            String newPin = scanner.nextLine();
            
            if (newPin.matches("\\d{4}")) {
                System.out.print("Confirm new PIN: ");
                String confirmPin = scanner.nextLine();
                
                if (newPin.equals(confirmPin)) {
                    System.out.println("✓ PIN changed successfully!");
                    System.out.println("Please login again with your new PIN.");
                    logout();
                } else {
                    System.out.println("✗ PINs do not match!");
                }
            } else {
                System.out.println("✗ PIN must be exactly 4 digits!");
            }
        } else {
            System.out.println("✗ Incorrect current PIN!");
        }
    }
    
    private void logout() {
        System.out.println("\n--- LOGOUT ---");
        System.out.println("Thank you for using our ATM, " + loggedInAccount.getAccountHolderName() + "!");
        System.out.println("Please take your card.");
        loggedInAccount = null;
    }

public boolean validateCredentialsGUI(String accountNumber, String pin) {
    Account account = accounts.get(accountNumber);
    if (account != null && account.getPin().equals(pin)) {
        loggedInAccount = account;
        return true;
    }
    return false;
}

public Account getLoggedInAccount() {
    return loggedInAccount;
}

public void updateAccountBalance() {
    saveAccountsToFile("accounts.csv");
}

private void saveAccountsToFile(String filename) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        writer.println("account_number,pin,balance,holder_name");
        for (Account account : accounts.values()) {
            writer.printf("%s,%s,%.2f,%s\n", 
                account.getAccountNumber(), 
                account.getPin(), 
                account.getBalance(), 
                account.getAccountHolderName());
        }
        System.out.println("✓ Accounts saved to file");
    } catch (IOException e) {
        System.out.println("Error saving accounts: " + e.getMessage());
    }
}

public boolean withdrawGUI(double amount) {
    if (loggedInAccount != null) {
        boolean success = loggedInAccount.withdraw(amount);
        if (success) {
            updateAccountBalance();
        }
        return success;
    }
    return false;
}

public void depositGUI(double amount) {
    if (loggedInAccount != null) {
        loggedInAccount.deposit(amount);
        updateAccountBalance();
    }
}
}
