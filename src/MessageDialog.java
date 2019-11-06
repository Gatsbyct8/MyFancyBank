import javax.swing.*;
import java.awt.*;

/**
 *  a small dialog which shows the warn information
 */
public class MessageDialog extends JDialog
{
    MessageDialog(String title, String message)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel messageLabel = new JLabel(message);
        Font f = new Font("", Font.BOLD, 16);
        messageLabel.setFont(f);

        setTitle(title);
        setLocationRelativeTo(null);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,15,10,15);
        add(messageLabel, c);
        pack();
        setResizable(false);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
