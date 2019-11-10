import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StocksInfoInterface extends JFrame
{
    Bank bank;
    ManageStocksInterface father;
    StocksInfoInterface self = this;
    ManageStocksType type;

    JLabel idLabel = new JLabel("ID");
    JTextField idField = new JTextField(10);
    JLabel nameLabel = new JLabel("Name");
    JTextField nameField = new JTextField(10);
    JLabel numberLabel = new JLabel("Number");
    JTextField numberField = new JTextField(10);
    JLabel priceLabel = new JLabel("Price");
    JTextField priceField = new JTextField(10);

    StocksInfoInterface(Bank bank, ManageStocksInterface father, ManageStocksType type)
    {
        this.bank = bank;
        this.father = father;
        this.type = type;

        setTitle("Stock Info");
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        JButton jbtOK = new JButton("OK");
        OKListener okL = new OKListener();
        jbtOK.addActionListener(okL);
        JButton jbtCancel = new JButton("Cancel");
        CancelListener CancelL = new CancelListener();
        jbtCancel.addActionListener(CancelL);

        if(type.equals(ManageStocksType.MODIFY))
        {
            idField.setEditable(false);
            nameField.setEditable(false);
            numberField.setEditable(false);
        }

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel(gridBag);

        c.insets = new Insets(20,10,20,10);
        gridBag.addLayoutComponent(idLabel, c);
        gridBag.addLayoutComponent(idField, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(nameLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(nameField, c);
        c = new GridBagConstraints();
        c.insets = new Insets(20,10,20,10);
        gridBag.addLayoutComponent(numberLabel, c);
        gridBag.addLayoutComponent(numberField, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(priceLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(priceField, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(jbtOK, c);
        c.gridwidth = GridBagConstraints.REMAINDER;

        panel.add(idLabel);
        panel.add(idField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(numberLabel);
        panel.add(numberField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(jbtOK);
        panel.add(jbtCancel);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    class OKListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(type.equals(ManageStocksType.ADD))   // Add new stocks
            {
                int numOfShares;
                double valueOfStock;

                if(nameField.getText().isEmpty() || nameField.getText().equals(""))
                {
                    MessageDialog amountError = new MessageDialog("Error", "Your stock must have a name!");
                    amountError.setVisible(true);
                    return;
                }
                idField.setText(idField.getText().toUpperCase());
                if (!isValidID(idField.getText()))
                {
                    MessageDialog amountError = new MessageDialog("Error", "Invalid ID! (must be all caps, 6 letters or less)");
                    amountError.setVisible(true);
                    return;
                }

                try //number of stocks must not be negative
                {
                    numOfShares = Integer.parseInt(numberField.getText());
                    if (numOfShares < 0)
                    {
                        MessageDialog amountError = new MessageDialog("Error", "Number of shares must not be negative!");
                        amountError.setVisible(true);
                        return;
                    }
                }
                catch(NumberFormatException nfe)
                {
                    MessageDialog amountError = new MessageDialog("Error", "Number of shares must be an integer!");
                    amountError.setVisible(true);
                    return;
                }

                try //price of stocks must not be negative
                {
                    valueOfStock = Double.parseDouble(priceField.getText());
                    if (valueOfStock < 0)
                    {
                        MessageDialog amountError = new MessageDialog("Error", "Price of stock must not be negative!");
                        amountError.setVisible(true);
                        return;
                    }
                }
                catch(NumberFormatException nfe)
                {
                    MessageDialog amountError = new MessageDialog("Error", "Price of stock must be a number!");
                    amountError.setVisible(true);
                    return;
                }
                catch(NullPointerException npe) {
                    MessageDialog amountError = new MessageDialog("Error", "Price of stock must be a number!");
                    amountError.setVisible(true);
                    return;
                }

                //validation passed, create stock
                if(addStock())//this will fail if the id is already in use
                {
                    closeFrame();
                    return;
                }

                MessageDialog message = new MessageDialog("Error", "This ID has already been used!");
                message.setVisible(true);
            }
            if(type.equals(ManageStocksType.MODIFY))    // Modify stock information
            {

            }
        }
    }

    class CancelListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            closeFrame();
        }
    }

    public boolean addStock()
    {
        Stock newStock = new Stock (idField.getText(), nameField.getText(), Integer.valueOf(numberField.getText()), Double.valueOf(priceField.getText()));
        return bank.getTotalStock().add(newStock);
    }

    private boolean isValidID(String id)
    {
        String reg = "^[A-Z]{1,6}$";
        return id.matches(reg);
    }

    private void closeFrame()
    {
        father.refreshStocksTable();
        father.setVisible(true);
        dispose();
    }
}
