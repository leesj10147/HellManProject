package core;

import javax.swing.*;
import java.awt.*;

public class Window
{
    public Window(int width, int height, Game game)
    {
        JFrame frame = new JFrame();
        game.setPreferredSize(new Dimension(width, height));
        game.setMaximumSize(new Dimension(width, height));
        game.setMinimumSize(new Dimension(width, height));

        frame.setUndecorated(true);
        /*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);*/
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        game.addMouseListener(new MouseInput(frame, 0, 0));
        frame.setVisible(true);
        game.setFocusable(true);
        game.requestFocus();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

    }
}
