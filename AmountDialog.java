import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AmountDialog extends JDialog {
    private JTextField amountField;
    private JButton confirmButton;
    private JButton cancelButton;
    private double amount = 0;
    private boolean confirmed = false;
    private String transactionType;
    
    public AmountDialog(JFrame parent, String type, String accountName) {
        super(parent, type + " Money", true);
        this.transactionType = type;
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 320, 20, 20));
        
        setSize(400, 320);
        setLocationRelativeTo(parent);
        
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, getHeight(), new Color(0, 0, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        String icon = type.equals("Deposit") ? "💰" : "💸";
        JLabel titleLabel = new JLabel(icon + " " + type + " Money");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(type.equals("Deposit") ? new Color(0, 255, 0) : new Color(255, 100, 0));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel accLabel = new JLabel("Account: " + accountName);
        accLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accLabel.setForeground(Color.LIGHT_GRAY);
        centerPanel.add(accLabel, gbc);
        
       
        gbc.gridy = 1;
        JLabel amountLabel = new JLabel("Enter Amount ($)");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountLabel.setForeground(Color.WHITE);
        centerPanel.add(amountLabel, gbc);
        
        
        gbc.gridy = 2;
        amountField = new JTextField(15);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setBackground(new Color(50, 50, 70));
        amountField.setForeground(Color.WHITE);
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(type.equals("Deposit") ? new Color(0, 255, 0) : new Color(255, 100, 0)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        centerPanel.add(amountField, gbc);
        
        
        gbc.gridy = 3;
        JPanel quickPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        quickPanel.setOpaque(false);
        String[] quickAmounts = type.equals("Deposit") ? 
            new String[]{"$20", "$50", "$100", "$500"} : 
            new String[]{"$20", "$50", "$100", "$200"};
        
        for (String amt : quickAmounts) {
            JButton quickBtn = new JButton(amt);
            quickBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            quickBtn.setBackground(new Color(70, 70, 90));
            quickBtn.setForeground(Color.WHITE);
            quickBtn.setFocusPainted(false);
            quickBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            quickBtn.addActionListener(e -> {
                String value = amt.replace("$", "");
                amountField.setText(value);
            });
            quickPanel.add(quickBtn);
        }
        centerPanel.add(quickPanel);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        confirmButton = createStyledButton("CONFIRM", type.equals("Deposit") ? new Color(0, 200, 0) : new Color(255, 100, 0));
        cancelButton = createStyledButton("CANCEL", new Color(100, 100, 100));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
      
        confirmButton.addActionListener(e -> confirmAmount());
        cancelButton.addActionListener(e -> dispose());
        
        
        getRootPane().setDefaultButton(confirmButton);
        
       
        SwingUtilities.invokeLater(() -> amountField.requestFocus());
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void confirmAmount() {
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount > 0) {
                confirmed = true;
                dispose();
            } else {
                showMessage("Please enter an amount greater than $0!", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showMessage(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
    
    public double getAmount() {
        return amount;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}