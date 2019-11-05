import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import java.awt.*;

public class MessageDialog extends JDialog
{
    MessageDialog(String title, String message)
    {
        setTitle(title);
        setSize(200,150);
        setLocationRelativeTo(null);

        JLabel messageLabel = new JLabel(message);
        add(messageLabel, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
