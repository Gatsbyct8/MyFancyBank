import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * registration interface
 */
public class NewStockWindow extends JFrame
{
    Bank bank;
    UserType userType;

    JFrame father;

    JTextField stockId=new JTextField(10);    // input id
    JTextField stockName=new JTextField(10);   // input name
    JTextField stockNumber=new JTextField(10);     // input number of shares
    JTextField stockPrice=new JTextField(10);    // input price in usd
   
    public NewStockWindow(Bank bank, JFrame father)
    {
        this.bank = bank;
        this.father = father;

        setTitle("Add New Stock");


        //setSize(400,250);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });
        //setLayout(new GridLayout(5,2,5,5));
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = null;

        JPanel panel = new JPanel(gridBag);

        JLabel stockIdLabel = new JLabel("Stock ID", JLabel.LEFT);
        JLabel stockNameLabel = new JLabel("Stock Name", JLabel.LEFT);
        JLabel stockNumberLabel = new JLabel("Number of Shares", JLabel.LEFT);
        JLabel stockPriceLabel = new JLabel("Price in USD", JLabel.LEFT);

        Dimension buttonSize = new Dimension(75,25);
        JButton jbtOK = new JButton("OK");
        jbtOK.setPreferredSize(buttonSize);
        okListener okL = new okListener();
        jbtOK.addActionListener(okL);
        JButton jbtCancel = new JButton("Cancel");
        jbtCancel.setPreferredSize(buttonSize);
        cancelListener cancelL = new cancelListener();
        jbtCancel.addActionListener(cancelL);

        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(stockIdLabel, c);
        gridBag.addLayoutComponent(stockId, c);
        gridBag.addLayoutComponent(stockNameLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(stockName, c);

        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(stockNumberLabel, c);
        gridBag.addLayoutComponent(stockNumber, c);
        gridBag.addLayoutComponent(stockPriceLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(stockPrice, c);

        JPanel p = new JPanel();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(p, c);

        c.gridwidth = 2;
        gridBag.addLayoutComponent(jbtOK, c);
        gridBag.addLayoutComponent(jbtCancel, c);

        panel.add(stockIdLabel);
        panel.add(stockId);
        panel.add(stockNameLabel);
        panel.add(stockName);
        panel.add(stockNumberLabel);
        panel.add(stockNumber);
        panel.add(stockPriceLabel);
        panel.add(stockPrice);
        panel.add(p);
        panel.add(jbtOK);
        panel.add(jbtCancel);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }

    //add a new Stock, returns false if that name is already taken or if the numbers are invalid
    public boolean addStock()
    {
        Stock newStock = new Stock (stockId.getText(), stockName.getText(), Integer.valueOf(stockNumber.getText()), Double.valueOf(stockPrice.getText()));
        return bank.getTotalStock().add(newStock);
    }


    class okListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
        	int numOfShares;
        	double valueOfStock;
        	
            if(stockName.getText().isEmpty() || stockName.getText().equals(""))
            {
                MessageDialog amountError = new MessageDialog("Error", "Your stock must have a name!");
                amountError.setVisible(true);
                return;
            }
            stockId.setText(stockId.getText().toUpperCase());
            if (!isValidID(stockId.getText()))
            {
                MessageDialog amountError = new MessageDialog("Error", "Invalid ID! (must be all caps, 6 letters or less)");
                amountError.setVisible(true);
                return;
            }
            
            try //number of stocks must not be negative
            {
            	numOfShares = Integer.parseInt(stockNumber.getText());
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
            	valueOfStock = Double.parseDouble(stockPrice.getText());
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
            	((ManageStocksWindow)(father)).refreshStockList();//refresh the list of stocks
                closeFrame();
                return;
            }
            
            MessageDialog message = new MessageDialog("Error", "This ID has already been used!");
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
        String reg = "^[A-Z]{1,6}$";
        return id.matches(reg);
    }


}
