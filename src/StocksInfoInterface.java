import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StocksInfoInterface extends JFrame
{
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

    StocksInfoInterface(ManageStocksInterface father, ManageStocksType type)
    {
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

    private void closeFrame()
    {
        father.setVisible(true);
        dispose();
    }
}
