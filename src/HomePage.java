import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame
{

    private Bank bank;

    public HomePage(Bank bank)
    {
        this.bank = bank;

        setTitle("Welcome to my bank");
        setSize(400,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton jbtCustomer = new JButton("Customer");
        CustomerListener customerL = new CustomerListener();
        jbtCustomer.addActionListener(customerL);
        JButton jbtManager = new JButton("Manager");
        ManagerListener managerL = new ManagerListener();
        jbtManager.addActionListener(managerL);

        JPanel panel = new JPanel();

        panel.add(jbtCustomer);
        panel.add(jbtManager);

        add(panel, BorderLayout.CENTER);
    }

    class CustomerListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            LoginInterface customerLogin = new LoginInterface(bank, UserType.CUSTOMER);
            setVisible(false);
            customerLogin.setVisible(true);
        }
    }

    class ManagerListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            LoginInterface ManagerLogin = new LoginInterface(bank, UserType.MANAGER);
            setVisible(false);
            ManagerLogin.setVisible(true);
        }
    }
}
