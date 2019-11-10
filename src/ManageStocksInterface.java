import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

enum ManageStocksType{ADD, DELETE, MODIFY}

public class ManageStocksInterface extends JFrame
{
    private Bank bank;
    private ManagerHomePage father;
    private ManageStocksInterface self = this;

    private StockTableModel stocksTableModel = new StockTableModel();
    private JTable stocksTable = new JTable();
    private JButton jbtAdd = new JButton("Add");
    private JButton jbtDelete = new JButton("Delete");
    private JButton jbtRestore = new JButton("Restore");
    private JButton jbtModify = new JButton("Modify");

    ManageStocksInterface(Bank bank, ManagerHomePage father)
    {
        this.bank = bank;
        this.father = father;

        setTitle("Manage Stock");
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                closeFrame();
            }
        });

        JTableHeader stocksHeader = stocksTable.getTableHeader();
        stocksHeader.setResizingAllowed(true);
        JScrollPane stocksScroll = new JScrollPane(stocksTable);
        String[] stocksHeaderName = {"ID", "Name", "Number", "Price"};
        stocksTableModel.setColumnIdentifiers(stocksHeaderName);
        stocksTable.setModel(stocksTableModel);
        stocksTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshStocksTable();

        Dimension buttonSize = new Dimension(90,25);
        AddListener addL = new AddListener();
        jbtAdd.addActionListener(addL);
        jbtAdd.setPreferredSize(buttonSize);
        DeleteListener deleteL = new DeleteListener();
        jbtDelete.addActionListener(deleteL);
        jbtDelete.setPreferredSize(buttonSize);
        RestoreListener restoreL = new RestoreListener();
        jbtRestore.addActionListener(restoreL);
        jbtRestore.setPreferredSize(buttonSize);
        ModifyListener modifyL = new ModifyListener();
        jbtModify.addActionListener(modifyL);
        jbtModify.setPreferredSize(buttonSize);

        GridBagLayout jbtGridBag = new GridBagLayout();
        GridBagConstraints jbtC = new GridBagConstraints();
        JPanel jbtPanel = new JPanel(jbtGridBag);
        jbtC.gridwidth = GridBagConstraints.REMAINDER;
        jbtC.insets = new Insets(20,5,20,5);
        jbtC.anchor = GridBagConstraints.CENTER;
        jbtGridBag.addLayoutComponent(jbtAdd, jbtC);
        jbtGridBag.addLayoutComponent(jbtDelete, jbtC);
        jbtGridBag.addLayoutComponent(jbtRestore, jbtC);
        jbtGridBag.addLayoutComponent(jbtModify, jbtC);
        jbtPanel.add(jbtAdd);
        jbtPanel.add(jbtDelete);
        jbtPanel.add(jbtRestore);
        jbtPanel.add(jbtModify);

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        JPanel panel = new JPanel(gridBag);

        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.addLayoutComponent(stocksScroll, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(jbtPanel, c);

        panel.add(stocksScroll);
        panel.add(jbtPanel);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    class AddListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            StocksInfoInterface stocksInfoInterface = new StocksInfoInterface(bank, self, ManageStocksType.ADD);
            refreshStocksTable();
            setVisible(false);
            stocksInfoInterface.setVisible(true);
        }
    }

    class DeleteListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Stock selectedStock = getSelectStock();

            if(selectedStock!=null && bank.getTotalStock().delete(selectedStock)) {
                MessageDialog deleteSuccess = new MessageDialog("Success", "Stock deleted.");
                deleteSuccess.setVisible(true);
                refreshStocksTable();
                return;
            }

            return;
        }
    }

    class RestoreListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(bank.getTotalStock().restoreStock()) {
                MessageDialog restoreSuccess = new MessageDialog("Success", "Stock restored.");
                restoreSuccess.setVisible(true);
                refreshStocksTable();
                return;
            }
            MessageDialog restoreError = new MessageDialog("Error", "No stocks have been deleted.");
            restoreError.setVisible(true);
            return;
        }
    }

    class ModifyListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            StocksInfoInterface stocksInfoInterface = new StocksInfoInterface(bank, self, ManageStocksType.MODIFY);
            setVisible(false);
            stocksInfoInterface.setVisible(true);
        }
    }

    class StockTableModel extends DefaultTableModel
    {
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return false;
        }
    }

    private Stock getSelectStock()
    {
        Stock selectedStock = null;
        int index = stocksTable.getSelectedRow();
        if(index == -1)
        {
            MessageDialog amountError = new MessageDialog("Error", "Please select a stock!");
            amountError.setVisible(true);
            return null;
        }
        String stockID = (String)stocksTable.getValueAt(index, 0);

        for(Stock item : bank.getStocks())
        {
            if(item.getId().equals(stockID))
            {
                selectedStock = item;
                return selectedStock;
            }
        }
        MessageDialog amountError = new MessageDialog("Error", "Cannot find the stock!");
        amountError.setVisible(true);
        return selectedStock;
    }

    public void refreshStocksTable()
    {
        stocksTableModel.setRowCount(0);
        for(Stock item : bank.getStocks())
        {
            String id = item.getId();
            String name = item.getName();
            String number = String.valueOf(item.getNumber());
            String price = String.format(" %.2f", item.getPrice());
            stocksTableModel.addRow(new String[]{id, name, number, price});
        }
    }

    public void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
