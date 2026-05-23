import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginDialog extends JDialog {
    private JTextField accountField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JButton cancelButton;
    private ATM atm;
    private boolean loginSuccess = false;
    
    public LoginDialog(JFrame parent, ATM atm) {
        super(parent, "ATM Login", true);
        this.atm = atm;
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 450, 350, 20, 20));
        
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 0), 0, getHeight(), new Color(30, 30, 50));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
       
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("🏦 SECURE ATM SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel accLabel = new JLabel("Account Number");
        accLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accLabel.setForeground(Color.WHITE);
        formPanel.add(accLabel, gbc);
        
        gbc.gridx = 1;
        accountField = new JTextField(15);
        accountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountField.setBackground(new Color(50, 50, 70));
        accountField.setForeground(Color.WHITE);
        accountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 255)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        formPanel.add(accountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel pinLabel = new JLabel("PIN Code");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pinLabel.setForeground(Color.WHITE);
        formPanel.add(pinLabel, gbc);
        
        gbc.gridx = 1;
        pinField = new JPasswordField(15);
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pinField.setBackground(new Color(50, 50, 70));
        pinField.setForeground(Color.WHITE);
        pinField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 255)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        formPanel.add(pinField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        loginButton = createStyledButton("LOGIN", new Color(0, 200, 0));
        cancelButton = createStyledButton("EXIT", new Color(200, 0, 0));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add action listeners
        loginButton.addActionListener(e -> attemptLogin());
        cancelButton.addActionListener(e -> System.exit(0));
        
        // Enter key presses login
        getRootPane().setDefaultButton(loginButton);
        
        // Make draggable
        addMouseListener(new MouseAdapter() {
            private Point initialClick;
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
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
    
    private void attemptLogin() {
        String accountNumber = accountField.getText().trim();
        String pin = new String(pinField.getPassword());
        
        if (accountNumber.isEmpty() || pin.isEmpty()) {
            showMessage("Please enter both Account Number and PIN!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (atm.validateCredentialsGUI(accountNumber, pin)) {
            loginSuccess = true;
            dispose();
        } else {
            showMessage("Invalid Account Number or PIN!\nPlease try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            accountField.setText("");
            pinField.setText("");
            accountField.requestFocus();
        }
    }
    
    private void showMessage(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
    
    public boolean isLoginSuccess() {
        return loginSuccess;
    }
}
