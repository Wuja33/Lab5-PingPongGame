package appgame.main;

import javax.swing.*;

/**
 * Dawid Szeląg 264008
 *
 * Kompilacja (Windows):
 * dir appgame /s /B *.java > sources.txt
 * javac @sources.txt
 *
 * Budowanie .jar:
 * jar cfm lab5_pop.jar ./META-INF/MANIFEST.MF *
 *
 * Uruchamianie:
 * java -jar lab5_pop.jar
 * (lub otworzyć z eksploratora)
 */

public class MainFrame extends JFrame {
    MainPanel mainPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
            }
        });
    }

    MainFrame()
    {
        mainPanel = new MainPanel();
        this.setTitle("Lab5");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}
