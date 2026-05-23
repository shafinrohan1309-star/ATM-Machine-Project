import javax.swing.*;

public class ATMGUIStarter {
    private ATM atm;
    
    public ATMGUIStarter() {
        atm = new ATM();
        atm.loadAccountsFromFile("accounts.csv");
    }
    
    public void start() {
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null, atm);
            loginDialog.setVisible(true);
            
            if (loginDialog.isLoginSuccess()) {
                ATMGUI gui = new ATMGUI(atm);
                gui.setVisible(true);
            } else {
                int retry = JOptionPane.showConfirmDialog(null, 
                    "Login failed. Would you like to try again?", 
                    "Login Failed", 
                    JOptionPane.YES_NO_OPTION);
                if (retry == JOptionPane.YES_OPTION) {
                    start();
                } else {
                    System.exit(0);
                }
            }
        });
    }
    
    public static void main(String[] args) {
        ATMGUIStarter starter = new ATMGUIStarter();
        starter.start();
    }
}
