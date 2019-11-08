import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;

public class ManageStocksWindow extends JFrame
{
    private Bank bank;
    private ManagerHomePage father;
    private Stock selectedStock;
    private ManageStocksWindow self = this;

    private JComboBox<String> stockList = new JComboBox<String>();   // choose a customer to check his transaction

    private JLabel stockIdLabel = new JLabel("", JLabel.LEFT);
    private JLabel stockNameLabel = new JLabel("", JLabel.LEFT);
    private JLabel stockNumberLabel = new JLabel("", JLabel.LEFT);
    private JLabel stockPriceLabel = new JLabel("", JLabel.LEFT);
    private JLabel newStockPriceLabel = new JLabel("New Stock Price in USD:", JLabel.LEFT);
    private JTextField stockPrice = new JTextField(5);
    private JButton jbtNewStock = new JButton("Add Stock");
    private JButton jbtRestore = new JButton("Restore Stock");
    private JButton jbtUpdate = new JButton("Update Value");
    private JButton jbtDelete = new JButton("Delete Stock");
    //this panel is the data block for each stock
    private GridBagLayout gridBag = new GridBagLayout();
    private GridBagConstraints c = null;
    private JPanel panel = new JPanel(gridBag);
    
    ManageStocksWindow(Bank bank, ManagerHomePage father)
    {
        this.bank = bank;
        this.father = father;

        setTitle("Manage Stocks");
        setSize(500,550);
        setResizable(false);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        stockList.setEditable(false);
        stockList.setEnabled(true);
        stockListListener stockListL = new stockListListener();
        stockList.addItemListener(stockListL);
        refreshStockList();
        //setDataBlock();
        
        newStocksListener newStocksL = new newStocksListener();
        jbtNewStock.addActionListener(newStocksL);
        
        
        restoreListener restoreL = new restoreListener();
        jbtRestore.addActionListener(restoreL);
        
        
        editPriceListener editPriceL = new editPriceListener();
        jbtUpdate.addActionListener(editPriceL);
        
        
        deleteListener deleteStockL = new deleteListener();
        jbtDelete.addActionListener(deleteStockL);






        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(stockIdLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(stockNameLabel, c);

        c = new GridBagConstraints();
        c.insets = new Insets(10,5,10,5);
        gridBag.addLayoutComponent(stockNumberLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(stockPriceLabel, c);

        panel.add(stockIdLabel);
        panel.add(stockNameLabel);
        panel.add(stockNumberLabel);
        panel.add(stockPriceLabel);
        
        add(stockList);
        add(jbtNewStock);
        add(jbtRestore);
        //how to add on new line?
        add(panel);
        //how to add on new line?
        add(newStockPriceLabel);
        add(stockPrice);
        add(jbtUpdate);
        //how to add on new line?
        add(jbtDelete);

    }

    class stockListListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {
                String selected = e.getItem().toString();
                int start = selected.indexOf("(");
                int end = selected.indexOf(")");

                String id = selected.substring(start+1, end);

                selectedStock = bank.getTotalStock().getStock(id);
                setDataBlock();
            }
        }
    }
    
    class editPriceListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
        	double newPrice;

        	try //price of stocks must not be negative
            {
        		newPrice = Double.parseDouble(stockPrice.getText());
                if (newPrice < 0)
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

            //validation passed, update stock
            if(selectedStock!=null && bank.getTotalStock().update(selectedStock, newPrice))//actually change value of stock
            {

            	setDataBlock();
                return;
            }
            
            MessageDialog message = new MessageDialog("Error", "Update failure! Perhaps you forgot to select a stock?");
            message.setVisible(true);
        }
    }
    
    class newStocksListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            NewStockWindow newStockWindow = new NewStockWindow(bank, self);
            setVisible(false);
            newStockWindow.setVisible(true);
        }
    }
    
    class restoreListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(bank.getTotalStock().restoreStock()) {
                MessageDialog restoreSuccess = new MessageDialog("Success", "Stock restored.");
                restoreSuccess.setVisible(true);
                refreshStockList();
                return;
            }
            MessageDialog restoreError = new MessageDialog("Error", "No stocks have been deleted.");
            restoreError.setVisible(true);
            return;
        }
    }
    
    class deleteListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(selectedStock!=null && bank.getTotalStock().delete(selectedStock)) {
                MessageDialog deleteSuccess = new MessageDialog("Success", "Stock deleted.");
                deleteSuccess.setVisible(true);
                refreshStockList();
                return;
            }
            MessageDialog deleteError = new MessageDialog("Error", "No stock selected to delete.");
            deleteError.setVisible(true);
            return;
        }
    }

    public void refreshStockList() {
    	stockList.removeAllItems(); //reset the list
        selectedStock = null;
    	for(Stock item : bank.getStocks())    // add all stocks to the list
        {
        	//selectedStock = item;
            String name = item.getName();
            String id = item.getId();
            stockList.addItem(name + " (" + id + ")");
        }
        setDataBlock();
    }
    
    public void refreshDataBlock() {
		stockPrice.setText("");
        stockIdLabel.setText("Stock ID: "+selectedStock.getId());
        stockNameLabel.setText("Stock Name: "+selectedStock.getName());
        stockNumberLabel.setText("Number of Shares: "+selectedStock.getNumber());
        stockPriceLabel.setText("Price in USD: "+selectedStock.getPrice());
        panel.setVisible(true);
        newStockPriceLabel.setVisible(true);
        stockPrice.setVisible(true);
        jbtUpdate.setVisible(true);
        jbtDelete.setVisible(true);
    }
    
    public void hideDataBlock() {
        panel.setVisible(false);
        newStockPriceLabel.setVisible(false);
        stockPrice.setVisible(false);
        jbtUpdate.setVisible(false);
        jbtDelete.setVisible(false);
    }
    
    public void setDataBlock() {//this will either hide the data(if there is no stock selected) or update it to the selected stock's info
    	if(selectedStock==null) {
    		hideDataBlock();
    	}
    	else {
    		refreshDataBlock();
    	}
    }

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
