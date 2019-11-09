import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Choose use this system as manager or customer
 */
public class HomePage extends JFrame
{
    private Bank bank;
    private HomePage self = this;

    public HomePage(Bank bank)
    {
        this.bank = bank;

        setTitle("Welcome");
        setSize(300,150);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setResizable(false);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });
        setLocationRelativeTo(null);

        Dimension buttonSize = new Dimension(150,35);
        JButton jbtCustomer = new JButton("Customer");
        jbtCustomer.setPreferredSize(buttonSize);
        CustomerListener customerL = new CustomerListener();
        jbtCustomer.addActionListener(customerL);
        JButton jbtManager = new JButton("Manager");
        jbtManager.setPreferredSize(buttonSize);
        ManagerListener managerL = new ManagerListener();
        jbtManager.addActionListener(managerL);
        Font f = new Font("", Font.BOLD, 16);
        jbtCustomer.setFont(f);
        jbtManager.setFont(f);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,5,10,5);
        add(jbtCustomer, c);
        c.gridx = 0;
        c.gridy = 1;
        add(jbtManager, c);
    }

    class CustomerListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            LoginInterface customerLogin = new LoginInterface(bank, UserType.CUSTOMER, self);
            setVisible(false);
            customerLogin.setVisible(true);
        }
    }

    class ManagerListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            LoginInterface ManagerLogin = new LoginInterface(bank, UserType.MANAGER, self);
            setVisible(false);
            ManagerLogin.setVisible(true);
        }
    }

    private void closeFrame()
    {
        //add your code here
        dispose(); //dispose the frame and exit the program
    }
}
