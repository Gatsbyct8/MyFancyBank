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

    private TableModel stocksTableModel = new TableModel();
    private JTable stocksTable = new JTable();
    private JButton jbtAdd = new JButton("Add");
    private JButton jbtDelete = new JButton("Delete");
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
        String[] stocksHeaderName = {"ID", "Name", "Price"};
        stocksTableModel.setColumnIdentifiers(stocksHeaderName);
        stocksTable.setModel(stocksTableModel);
        stocksTable.setEnabled(false);
        refreshStocksTable();

        AddListener addL = new AddListener();
        jbtAdd.addActionListener(addL);
        DeleteListener deleteL = new DeleteListener();
        jbtDelete.addActionListener(deleteL);
        ModifyListener modifyL = new ModifyListener();
        jbtModify.addActionListener(modifyL);

        GridBagLayout jbtGridBag = new GridBagLayout();
        GridBagConstraints jbtC = new GridBagConstraints();
        JPanel jbtPanel = new JPanel(jbtGridBag);
        jbtC.gridwidth = GridBagConstraints.REMAINDER;
        jbtC.insets = new Insets(20,5,20,5);
        jbtC.anchor = GridBagConstraints.CENTER;
        jbtGridBag.addLayoutComponent(jbtAdd, jbtC);
        jbtGridBag.addLayoutComponent(jbtDelete, jbtC);
        jbtGridBag.addLayoutComponent(jbtModify, jbtC);
        jbtPanel.add(jbtAdd);
        jbtPanel.add(jbtDelete);
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
            StocksInfoInterface stocksInfoInterface = new StocksInfoInterface(self, ManageStocksType.ADD);
            setVisible(false);
            stocksInfoInterface.setVisible(true);
        }
    }

    class DeleteListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }

    class ModifyListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            StocksInfoInterface stocksInfoInterface = new StocksInfoInterface(self, ManageStocksType.MODIFY);
            setVisible(false);
            stocksInfoInterface.setVisible(true);
        }
    }

    class TableModel extends DefaultTableModel
    {
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return false;
        }
    }

    private void refreshStocksTable()
    {

    }

    public void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
