package appgame.main;

import javax.swing.*;

public class Counter extends JLabel {
    int counter=0;

    Counter()
    {
        this.setText("0");
    }

    public void increaseCounter()
    {
        counter ++;
        this.setText(String.valueOf(counter));
    }
}
