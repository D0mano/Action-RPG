package main;

import javax.swing.JFrame;
import java.awt.*;

public class Main {
    public static JFrame window;
    public static void main(String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");
        Image icon = Toolkit.getDefaultToolkit().getImage(
                Main.class.getResource("/UI/titleScreen/Secret_Legend-icon.png")
        );

        window.setIconImage(icon);
        window.setUndecorated(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}
