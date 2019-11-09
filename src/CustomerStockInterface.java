import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerStockInterface extends JFrame
{
    private Customer customer;
    private Bank bank;
    private CustomerStockInterface self = this;
    private CustomerHomePage father;
    private JComboBox<SecureAccount> secureAccountJComboBox = new JComboBox<SecureAccount>();
    private JComboBox<SavingAccount> savingAccountJComboBox = new JComboBox<SavingAccount>();
    private JComboBox<Balance> balanceJComboBox = new JComboBox<Balance>();
    private DefaultTableModel stockTableModel = new DefaultTableModel();
    private JTable stockTable = new JTable();
    private DefaultTableModel selfStockTableModel = new DefaultTableModel();
    private JTable selfStockTable = new JTable();
    private JButton jbtBuy = new JButton("Buy");
    private JButton jbtSell = new JButton("Sell");

    CustomerStockInterface(Customer customer, Bank bank, CustomerHomePage father)
    {
        this.customer = customer;
        this.bank = bank;
        this.father = father;

        setTitle("Stocks");
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        Dimension JComboBoxSize = new Dimension(250,20);
        secureAccountJComboBox.setEditable(false);
        secureAccountJComboBox.setEnabled(true);
        secureAccountJComboBox.setPreferredSize(JComboBoxSize);
        secureAccountJComboBoxListener secureAccountJComboBoxL = new secureAccountJComboBoxListener();
        secureAccountJComboBox.addItemListener(secureAccountJComboBoxL);
        savingAccountJComboBox.setEditable(false);
        savingAccountJComboBox.setEnabled(true);
        savingAccountJComboBox.setPreferredSize(JComboBoxSize);
        savingAccountJComBoxListener savingAccountJComBoxL = new savingAccountJComBoxListener();
        savingAccountJComboBox.addItemListener(savingAccountJComBoxL);

        List<Account> Accounts = customer.getAccounts();
        for(Account item : Accounts)
        {
            if(item instanceof SavingAccount)
            {
                SavingAccount savingAccount = (SavingAccount)item;
                savingAccountJComboBox.addItem(savingAccount);
            }
            if(item instanceof SecureAccount)
            {
                SecureAccount secureAccount = (SecureAccount)item;
                secureAccountJComboBox.addItem(secureAccount);
            }
        }

        JTableHeader stockHeader = stockTable.getTableHeader();
        stockHeader.setResizingAllowed(true);
        JScrollPane stockScroll = new JScrollPane(stockTable);
        String[] stockHeaderName = {"ID", "Name", "Price"};
        stockTableModel.setColumnIdentifiers(stockHeaderName);
        stockTable.setModel(stockTableModel);
        stockTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshStockTable();

        JTableHeader selfStockHeader = selfStockTable.getTableHeader();
        selfStockHeader.setResizingAllowed(true);
        JScrollPane selfStockScroll = new JScrollPane(selfStockTable);
        String[] selfStockHeaderName = {"Stock ID", "Name", "Buy In", "Amount", "Current Price"};
        selfStockTableModel.setColumnIdentifiers(selfStockHeaderName);
        selfStockTable.setModel(selfStockTableModel);
        selfStockTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshSelfStockTable();

        Dimension buttonSize = new Dimension(75,25);
        jbtBuy.setPreferredSize(buttonSize);
        BuyListener buyL = new BuyListener();
        jbtBuy.addActionListener(buyL);
        jbtSell.setPreferredSize(buttonSize);
        SellListener sellL = new SellListener();
        jbtSell.addActionListener(sellL);


        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = null;
        JPanel panel = new JPanel(gridBag);

        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(12,5,12,5);
        gridBag.addLayoutComponent(secureAccountJComboBox, c);
        gridBag.addLayoutComponent(savingAccountJComboBox, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(stockScroll, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(selfStockScroll, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10,5,20,5);
        gridBag.addLayoutComponent(jbtBuy, c);
        gridBag.addLayoutComponent(jbtSell, c);

        panel.add(secureAccountJComboBox);
        panel.add(savingAccountJComboBox);
        panel.add(stockScroll);
        panel.add(selfStockScroll);
        panel.add(jbtBuy);
        panel.add(jbtSell);

        setContentPane(panel);
        pack();
        //setResizable(false);
        setLocationRelativeTo(null);
    }

    class secureAccountJComboBoxListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if(ItemEvent.SELECTED == e.getStateChange())
            {

            }
        }
    }

    class savingAccountJComBoxListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {

        }
    }

    class BuyListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }

    class SellListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }

    public void refreshStockTable()
    {

    }

    public void refreshSelfStockTable()
    {}

    private void closeFrame()
    {
        dispose();
        father.setVisible(true);
        toFront();
    }
}
