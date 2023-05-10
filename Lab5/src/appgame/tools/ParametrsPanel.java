package appgame.tools;

import javax.swing.*;
import java.awt.*;

public class ParametrsPanel {
    public static JPanel createPanel(JLabel label, JTextField textField)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label,BorderLayout.NORTH);
        panel.add(textField,BorderLayout.CENTER);
        return panel;
    }
    public static JPanel createCounterPanel(JLabel counterRed, JLabel counterBlue)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(counterRed,BorderLayout.WEST);
        panel.add(counterBlue,BorderLayout.EAST);
        return panel;
    }
    public static int getValueFromText(String string, int min, int max, int value)
    {
        int helpInt;
        try {
            helpInt = Integer.parseInt(string);
        } catch (Exception e) {
            helpInt = 0;
        }

        if (helpInt>min && helpInt<max) //sprawdź czy mieści się w zadanym przedziale
            return helpInt;
        else
            return value;
    }
    public static int getValueFromText(String string, int min, int value)
    {
        int helpInt;
        try {
            helpInt = Integer.parseInt(string);
        } catch (Exception e) {
            helpInt = 0;
        }

        if (helpInt>min) //sprawdź czy mieści się w zadanym przedziale
            return helpInt;
        else
            return value;
    }
    public static int getValueFromTextOdd(String string, int min, int value)
    {
        int helpInt;
        try {
            helpInt = Integer.parseInt(string);
        } catch (Exception e) {
            helpInt = 0;
        }

        if (helpInt>min&&helpInt%2!=0) //sprawdź czy mieści się w zadanym przedziale
            return helpInt;
        else
            return value;
    }
    public static int getValueFromTextOdd(String string, int min,int max, int value)
    {
        int helpInt;
        try {
            helpInt = Integer.parseInt(string);
        } catch (Exception e) {
            helpInt = 0;
        }

        if (helpInt>min&&helpInt%2!=0&&helpInt<max) //sprawdź czy mieści się w zadanym przedziale
            return helpInt;
        else
            return value;
    }
}
