import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

enum UserType {CUSTOMER, MANAGER}

public class LoginInterface extends JFrame
{

    private Bank bank;
    private UserType userType;
    private JTextField idField=new JTextField(10);
    private JTextField passcodeField=new JTextField(10);
    private LoginInterface self = this;

    LoginInterface(Bank bank, UserType userType)
    {
        this.bank = bank;
        this.userType = userType;
        if(userType == UserType.CUSTOMER)
        {
            setTitle("Customer Login");
        }
        if(userType == UserType.MANAGER)
        {
            setTitle("Manager Login");
        }


        setSize(400,150);
        setLocationRelativeTo(null);

        JPanel idPanel = new JPanel();
        //JLabel idLabel = new JLabel("ID");
        idField.setText("ID");
        //idPanel.add(idLabel);
        idPanel.add(idField);

        JPanel passcodePanel = new JPanel();
        //JLabel passcodeLabel = new JLabel("PASSCODE");
        passcodeField.setText("PASSCODE");
        //passcodePanel.add(passcodeLabel);
        passcodePanel.add(passcodeField);

        JPanel buttonPanel = new JPanel();
        JButton jbtLogin = new JButton("Login");
        loginListener loginL = new loginListener();
        jbtLogin.addActionListener(loginL);
        JButton jbtReg = new JButton("Registration");
        regListener regL = new regListener();
        jbtReg.addActionListener(regL);
        buttonPanel.add(jbtLogin);
        buttonPanel.add(jbtReg);

        add(idPanel, BorderLayout.NORTH);
        add(passcodePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }

    class loginListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            boolean isAccountExist = false;
            if(userType == UserType.CUSTOMER)
            {
                for(Customer item :bank.getCustomers())
                {
                    if(item.getUserID().equals(idField.getText()))
                    {
                        CustomerHomePage customerHomePage = new CustomerHomePage(bank, item);
                        setVisible(false);
                        customerHomePage.setVisible(true);
                        isAccountExist = true;
                        break;
                    }
                }
                if(!isAccountExist)
                {
                    MessageDialog noCustomer = new MessageDialog("Error!", "No UserId: " + idField.getText());
                    noCustomer.setVisible(true);
                }
            }

            if(userType == UserType.MANAGER)
            {
                for(Manager item :bank.getManagers())
                {
                    if(item.getManagerID().equals(idField.getText()))
                    {
                        ManagerHomePage managerHomePage = new ManagerHomePage(bank, self);
                        setVisible(false);
                        managerHomePage.setVisible(true);
                        isAccountExist = true;
                        break;
                    }
                }
                if(!isAccountExist)
                {
                    MessageDialog noCustomer = new MessageDialog("Error!", "Wrong manager ID: " + idField.getText());
                    noCustomer.setVisible(true);
                }
            }
        }
    }

    class regListener implements  ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            RegistrationInterface registrationInterface = new RegistrationInterface(bank, userType, self);
            setVisible(false);
            registrationInterface.setVisible(true);
        }
    }

}
