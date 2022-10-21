import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameGUI extends JFrame
{
    public GameGUI() throws IOException
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1954, 1090);
        setResizable(false);
        setLocationRelativeTo(null);

        setContentPane(new Hexa(getSize()));
        setVisible(true);
    }
    public void paint( Graphics g ) {
        super.paint(g);
    }

}
