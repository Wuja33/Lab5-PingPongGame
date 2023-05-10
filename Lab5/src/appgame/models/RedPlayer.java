package appgame.models;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static appgame.models.Ball.delay;
import static appgame.models.Ball.panels;

public class RedPlayer implements Runnable{
    static AtomicIntegerArray arraySyncRed;
    static int rows;
    int row;
    int number;

    public RedPlayer(int row)
    {
        this.row = row;
        this.number = row;
    }

    @Override
    public void run()
    {
        //początkowe rysowanie
        synchronized (panels[row][1])
        {
            panels[row][1].setFigure(2);
            panels[row][1].setNumber(String.valueOf(number));
            SwingUtilities.invokeLater(()->
            {
                panels[row][1].repaint();
            });
        }
        sleepPlayer();

        Random rand = new Random();
        while (!Thread.currentThread().isInterrupted())
        {
            move(rand.nextInt(2));
        }
    }

    public void move(int direction)
    {
        synchronized (arraySyncRed)
        {
            if(direction==0) //poruszanie się do góry
            {
                if (row>0) {  // jeśli nie znajdujesz się na samej górze, możesz ruszyć się do góry
                    if (arraySyncRed.get(row-1) == 0) //jeśli pusto
                    {
                        row--;
                        arraySyncRed.set(row, 1); //zajmij miejsce
                        arraySyncRed.set(row+1, 0); //zwolnij poprzednie miejsce
                        draw(direction);
                    }
                }
            }
            else //poruszanie się w dół
            {
                if (row<(rows-1)) // jeśli nie znajdujesz się na samym dole, możesz ruszyć się na dół
                {
                    if (arraySyncRed.get(row+1) == 0) //jeśli pusto
                    {
                        row++;
                        arraySyncRed.set(row, 1); //zajmij miejsce
                        arraySyncRed.set(row-1, 0); //zwolnij poprzednie
                        draw(direction);
                    }
                }
            }
        }
        sleepPlayer();
    }

    public void draw(int direction)
    {
        synchronized (panels[row][1]) { //narysuj gracza w nowym panelu
            panels[row][1].setFigure(2);
            panels[row][1].setNumber(String.valueOf(number));
            SwingUtilities.invokeLater(()->
            {
                panels[row][1].repaint();
            });
        }
        if (direction==0) //poruszanie się do góry
        {
            synchronized (panels[row+1][1]) //wyczyść poprzedni panel
            {
                panels[row+1][1].setFigure(0);
                panels[row+1][1].setNumber("");
                SwingUtilities.invokeLater(()->
                {
                    panels[row+1][1].repaint();
                });
            }
        }
        else //poruszanie się w dół
        {
            synchronized (panels[row-1][1]) //wyczyść poprzedni panel
            {
                panels[row-1][1].setFigure(0);
                panels[row-1][1].setNumber("");
                SwingUtilities.invokeLater(()->{
                    panels[row-1][1].repaint();
                });
            }
        }
    }
    private void sleepPlayer()
    {
        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            System.out.println("Thread Interrupted");
        }
    }

    public static void setArraySyncRed(AtomicIntegerArray arraySyncRed) {
        RedPlayer.arraySyncRed = arraySyncRed;
    }

    public static void setRows(int rows) {
        RedPlayer.rows = rows;
    }

}
