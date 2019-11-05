import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegistrationInterface extends JFrame
{
    Bank bank;
    UserType userType;

    JFrame father;

    JTextField lastNameField=new JTextField(10);
    JTextField firstNameField=new JTextField(10);
    JTextField streetField=new JTextField(10);
    JTextField cityField=new JTextField(10);
    JTextField stateField=new JTextField(10);
    JTextField zipCodeField=new JTextField(10);
    JTextField phoneField=new JTextField(10);
    JTextField userIDField=new JTextField(10);
    JPasswordField passcodeField=new JPasswordField(10);
    public RegistrationInterface(Bank bank, UserType userType, JFrame father)
    {
        this.bank = bank;
        this.userType = userType;
        this.father = father;

        if(userType == UserType.CUSTOMER)
        {
            setTitle("Customer Registration");
        }
        if(userType == UserType.MANAGER)
        {
            setTitle("Manager Registration");
        }

        setSize(400,250);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });
        setLayout(new FlowLayout());

        JPanel lastNamePanel = new JPanel();
        lastNameField.setText("LastName");
        lastNamePanel.add(lastNameField);

        JPanel firstNamePanel = new JPanel();
        firstNameField.setText("FirstName");
        firstNamePanel.add(firstNameField);

        JPanel streetPanel = new JPanel();
        streetField.setText("Street");
        streetPanel.add(streetField);

        JPanel cityPanel = new JPanel();
        cityField.setText("City");
        cityPanel.add(cityField);

        JPanel statePanel = new JPanel();
        stateField.setText("State");
        statePanel.add(stateField);

        JPanel zipCodePanel = new JPanel();
        zipCodeField.setText("ZipCode");
        zipCodePanel.add(zipCodeField);

        JPanel phonePanel = new JPanel();
        phoneField.setText("Phone");
        phonePanel.add(phoneField);

        JPanel userIDPanel = new JPanel();
        userIDField.setText("UserID");
        phonePanel.add(userIDField);

        JPanel passcodePanel = new JPanel();
        passcodeField.setText("Passcode");
        passcodePanel.add(passcodeField);

        JPanel buttonPanel = new JPanel();
        JButton jbtOK = new JButton("OK");
        okListener okL = new okListener();
        jbtOK.addActionListener(okL);
        JButton jbtCancel = new JButton("Cancel");
        cancelListener cancelL = new cancelListener();
        jbtCancel.addActionListener(cancelL);
        buttonPanel.add(jbtOK);
        buttonPanel.add(jbtCancel);

        add(lastNamePanel);
        add(firstNamePanel);
        add(streetPanel);
        add(cityPanel);
        add(statePanel);
        add(zipCodePanel);
        add(phonePanel);
        add(userIDPanel);
        add(passcodePanel);
        add(buttonPanel);
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }

    public boolean regCustomer()
    {
        //Find whether the userID has been used
        for(Customer item : bank.getCustomers())
        {
            if(item.getUserID().equals(userIDField.getText()))
            {
                return false;
            }
        }

        Name newName = new Name(lastNameField.getText(), firstNameField.getText());
        Address newAddress = new Address(streetField.getText(), cityField.getText(), stateField.getText(), zipCodeField.getText());
        Customer newCustomer = new Customer(newName, newAddress, phoneField.getText(), userIDField.getText(), passcodeField.getText());
        bank.getCustomers().add(newCustomer);

        //System.out.println(bank.getCustomers().size());
        return true;
    }

    public boolean regManager()
    {
        //Find whether the userID has been used
        for(Manager item : bank.getManagers())
        {
            if(item.getManagerID().equals(userIDField.getText()))
            {
                return false;
            }
        }

        Name newName = new Name(lastNameField.getText(), firstNameField.getText());
        Address newAddress = new Address(streetField.getText(), cityField.getText(), stateField.getText(), zipCodeField.getText());
        Manager newManager = new Manager(newName, newAddress, phoneField.getText(), userIDField.getText(), passcodeField.getText());
        bank.getManagers().add(newManager);

        //System.out.println(bank.getCustomers().size());
        return true;
    }

    class okListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(!isValidName(lastNameField.getText()) || !isValidName(firstNameField.getText()))
            {
                MessageDialog amountError = new MessageDialog("Error", "Invalid Name!");
                amountError.setVisible(true);
                return;
            }
            if (!isValidID(userIDField.getText()))
            {
                MessageDialog amountError = new MessageDialog("Error", "Invalid ID!");
                amountError.setVisible(true);
                return;
            }
            if(userType == UserType.CUSTOMER)
            {
                if(regCustomer())
                {
                    closeFrame();
                    return;
                }
            }
            if(userType == UserType.MANAGER)
            {
                if(regManager())
                {
                    closeFrame();
                    return;
                }
            }
            MessageDialog message = new MessageDialog("Error", "This ID has existed!");
            message.setVisible(true);
        }
    }

    class cancelListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            closeFrame();
        }
    }

    private boolean isValidID(String id)
    {
        String reg = "^[a-z0-9A-Z]+$";
        return id.matches(reg);
    }

    private boolean isValidName(String name)
    {
        String reg = "^[a-zA-Z]+$";
        return name.matches(reg);
    }
}
