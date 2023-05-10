package appgame.main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class InGamePanel extends JPanel {
    int figure=0; //0 - empty  1 - circle  2 - redCircle  3 - blueCircle
    String number;

    InGamePanel()
    {
        Border line = BorderFactory.createLineBorder(Color.BLACK); //dodawanie ramki
        this.setBorder(line);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (figure==1)
            drawCircle(g); //1
        else if (figure==2)
            drawRedCircle(g); //2
        else if (figure==3)
            drawBlueCircle(g); //3
    }

    public void drawCircle(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        if (this.getWidth()/2<this.getHeight()/2) //mechanizm, aby kropka była na środku
            g2d.fill(new Ellipse2D.Double(this.getWidth()/4,this.getHeight()/4+(this.getHeight()/4-this.getWidth()/4),this.getWidth()/2,this.getWidth()/2));
        else
            g2d.fill(new Ellipse2D.Double(this.getWidth()/4+(this.getWidth()/4-this.getHeight()/4),this.getHeight()/4,this.getHeight()/2,this.getHeight()/2));
    }
    public void drawRedCircle(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.RED);
        drawCircle(g);
        //g2d.setPaint(Color.BLACK);
        //g2d.drawString(String.valueOf(number),this.getWidth()/2,this.getHeight()/2);
    }
    public void drawBlueCircle(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.BLUE);
        drawCircle(g);
        //g2d.setPaint(Color.WHITE);
        //g2d.drawString(String.valueOf(number),this.getWidth()/2,this.getHeight()/2);
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
