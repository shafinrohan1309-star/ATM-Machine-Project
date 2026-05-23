import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATMGUI extends JFrame {
    private ATM atm;
    private Account currentAccount;
    private JLabel balanceLabel;
    private JTextArea transactionArea;
    private JLabel timeLabel;
    private Timer timer;
    
    public ATMGUI(ATM atm) {
        this.atm = atm;
        this.currentAccount = atm.getLoggedInAccount();
        
        setTitle("🏦 ATM Machine - " + currentAccount.getAccountHolderName());
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        
       
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(15, 15, 25));
        
       
        JPanel titleBar = createTitleBar();
        mainContainer.add(titleBar, BorderLayout.NORTH);
        
      
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(15, 15, 25));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel leftPanel = createLeftPanel();
        contentPanel.add(leftPanel, BorderLayout.WEST);
        
        
        JPanel centerPanel = createCenterPanel();
        contentPanel.add(centerPanel, BorderLayout.CENTER);
                
JPanel rightPanel = createRightPanel();
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContainer);
        
       
        startTimeUpdater();
        
       
        addTransaction("🏦 WELCOME TO ATM SYSTEM");
        addTransaction("📅 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        addTransaction("👋 Welcome, " + currentAccount.getAccountHolderName());
        addTransaction("💳 Account: " + maskAccountNumber(currentAccount.getAccountNumber()));
        addSeparator();
    }
    
    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(0, 102, 204));
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));
        titleBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("🏦 ATM MACHINE - SECURE BANKING SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        
        JButton minimizeBtn = createTitleButton("─");
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        JButton closeBtn = createTitleButton("✕");
        closeBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(minimizeBtn);
        buttonPanel.add(closeBtn);
        
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(buttonPanel, BorderLayout.EAST);
        
        
        titleBar.addMouseListener(new MouseAdapter() {
            private Point initialClick;
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
        
        return titleBar;
    }
    
    private JButton createTitleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(35, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 0, 0));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
        
        return button;
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(20, 20, 35));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        leftPanel.setPreferredSize(new Dimension(250, 0));
        
        // Card image area
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(new Color(30, 30, 50));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.YELLOW, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel cardLabel = new JLabel("💳", SwingConstants.CENTER);
        cardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        cardPanel.add(cardLabel, BorderLayout.CENTER);
        
        JLabel cardText = new JLabel("DEBIT CARD", SwingConstants.CENTER);
        cardText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardText.setForeground(Color.YELLOW);
        cardPanel.add(cardText, BorderLayout.SOUTH);
        
        leftPanel.add(cardPanel, BorderLayout.NORTH);
        
        // Balance panel
        JPanel balancePanel = new JPanel(new GridBagLayout());
        balancePanel.setBackground(new Color(20, 20, 35));
        balancePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 0)),
            "CURRENT BALANCE",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(0, 255, 0)
        ));
        
        balanceLabel = new JLabel(String.format("$%.2f", currentAccount.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceLabel.setForeground(new Color(0, 255, 0));
        balancePanel.add(balanceLabel);
        
        leftPanel.add(balancePanel, BorderLayout.CENTER);
        
        // Time panel
        JPanel timePanel = new JPanel();
        timePanel.setBackground(new Color(20, 20, 35));
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(Color.LIGHT_GRAY);
        timePanel.add(timeLabel);
        leftPanel.add(timePanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(15, 15, 25));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        // Transaction history
        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        transactionArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        transactionArea.setBackground(new Color(10, 10, 20));
        transactionArea.setForeground(new Color(0, 255, 255));
        transactionArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        
        JScrollPane scrollPane = new JScrollPane(transactionArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            "TRANSACTION HISTORY",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(0, 102, 204)
        ));
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(20, 20, 35));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        rightPanel.setPreferredSize(new Dimension(250, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Menu title
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel menuLabel = new JLabel("ATM MENU", SwingConstants.CENTER);
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuLabel.setForeground(Color.YELLOW);
        rightPanel.add(menuLabel, gbc);
        
        // Buttons
        String[][] buttons = {
            {"💰", "DEPOSIT", "#00994C"},
            {"💸", "WITHDRAW", "#FF6600"},
            {"📊", "BALANCE", "#0066CC"},
            {"ℹ️", "ACCOUNT INFO", "#6666FF"},
            {"🔄", "CHANGE PIN", "#FFCC00"},
            {"🚪", "LOGOUT", "#CC0000"}
        };
        
        for (int i = 0; i < buttons.length; i++) {
            gbc.gridy = i + 1;
            JButton btn = createMenuButton(buttons[i][0] + " " + buttons[i][1], Color.decode(buttons[i][2]));
            final String action = buttons[i][1];
            btn.addActionListener(e -> handleAction(action));
            rightPanel.add(btn, gbc);
        }
        
        return rightPanel;
    }
    
    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
                button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        });
        
        return button;
    }
    
    private void handleAction(String action) {
        switch(action) {
            case "DEPOSIT":
                depositMoney();
                break;
            case "WITHDRAW":
                withdrawMoney();
                break;
            case "BALANCE":
                showBalance();
                break;
            case "ACCOUNT INFO":
                showAccountInfo();
                break;
            case "CHANGE PIN":
                changePin();
                break;
            case "LOGOUT":
                logout();
                break;
        }
    }
    
    private void depositMoney() {
        AmountDialog dialog = new AmountDialog(this, "Deposit", currentAccount.getAccountHolderName());
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            double amount = dialog.getAmount();
            atm.depositGUI(amount);
            updateBalanceDisplay();
            addTransaction("💰 DEPOSIT: +$" + String.format("%.2f", amount));
            addTransaction("   New Balance: $" + String.format("%.2f", currentAccount.getBalance()));
            addSeparator();
            
            JOptionPane.showMessageDialog(this, 
                "✅ Deposit Successful!\nAmount: $" + String.format("%.2f", amount), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void withdrawMoney() {
        AmountDialog dialog = new AmountDialog(this, "Withdraw", currentAccount.getAccountHolderName());
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            double amount = dialog.getAmount();
            if (atm.withdrawGUI(amount)) {
                updateBalanceDisplay();
                addTransaction("💸 WITHDRAWAL: -$" + String.format("%.2f", amount));
                addTransaction("   New Balance: $" + String.format("%.2f", currentAccount.getBalance()));
                addSeparator();
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Withdrawal Successful!\nAmount: $" + String.format("%.2f", amount), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient funds!\nYour balance: $" + String.format("%.2f", currentAccount.getBalance()), 
                    "Transaction Failed", 
                    JOptionPane.ERROR_MESSAGE);
                addTransaction("❌ WITHDRAWAL FAILED: Insufficient funds");
                addSeparator();
            }
        }
    }
    
    private void showBalance() {
        addSeparator();
        addTransaction("📊 BALANCE INQUIRY");
        addTransaction("   Current Balance: $" + String.format("%.2f", currentAccount.getBalance()));
        addSeparator();
        
        JOptionPane.showMessageDialog(this, 
            "Current Balance: $" + String.format("%.2f", currentAccount.getBalance()), 
            "Balance Inquiry", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAccountInfo() {
        String info = "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                      "         ACCOUNT INFORMATION         \n" +
                      "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                      "👤 Account Holder: " + currentAccount.getAccountHolderName() + "\n" +
                      "💳 Account Number: " + maskAccountNumber(currentAccount.getAccountNumber()) + "\n" +
                      "💰 Current Balance: $" + String.format("%.2f", currentAccount.getBalance()) + "\n" +
                      "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
        
        addTransaction(info);
        addSeparator();
        
        JOptionPane.showMessageDialog(this, info, "Account Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void changePin() {
        String currentPin = JOptionPane.showInputDialog(this, "Enter Current PIN:");
        if (currentPin == null) return;
        
        if (currentAccount.getPin().equals(currentPin)) {
            String newPin = JOptionPane.showInputDialog(this, "Enter New PIN (4 digits):");
            if (newPin != null && newPin.matches("\\d{4}")) {
                String confirmPin = JOptionPane.showInputDialog(this, "Confirm New PIN:");
                if (newPin.equals(confirmPin)) {
                    addTransaction("🔄 PIN changed successfully");
                    addSeparator();
                    JOptionPane.showMessageDialog(this, 
                        "PIN changed successfully!\nPlease login again.", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    logout();
                } else {
                    JOptionPane.showMessageDialog(this, "PINs do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "PIN must be 4 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect current PIN!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            addTransaction("🔒 User logged out");
            addSeparator();
            dispose();
            SwingUtilities.invokeLater(() -> {
                ATMGUIStarter starter = new ATMGUIStarter();
                starter.start();
            });
        }
    }
    
    private void updateBalanceDisplay() {
        currentAccount = atm.getLoggedInAccount();
        balanceLabel.setText(String.format("$%.2f", currentAccount.getBalance()));
    }
    
    private void addTransaction(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(new Date());
        transactionArea.append("[" + timestamp + "] " + message + "\n");
        transactionArea.setCaretPosition(transactionArea.getDocument().getLength());
    }
    
    private void addSeparator() {
        transactionArea.append("\n");
        transactionArea.setCaretPosition(transactionArea.getDocument().getLength());
    }
    
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() <= 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
    
    private void startTimeUpdater() {
        timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - EEE, MMM dd");
            timeLabel.setText(sdf.format(new Date()));
        });
        timer.start();
    }
}
