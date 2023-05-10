package appgame.models;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static appgame.models.Ball.delay;
import static appgame.models.Ball.panels;

public class BluePlayer implements Runnable{
    static AtomicIntegerArray arraySyncBlue;
    static int rows;
    static int columnSync;
    int row;
    int number;

    public BluePlayer(int row)
    {
        this.row = row;
        this.number = row;
    }

    @Override
    public void run()
    {
        //początkowe rysowanie
        synchronized (panels[row][columnSync])
        {
            panels[row][columnSync].setFigure(3);
            panels[row][columnSync].setNumber(String.valueOf(number));
            SwingUtilities.invokeLater(()->
            {
                panels[row][columnSync].repaint();
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
        synchronized (arraySyncBlue)
        {
            if(direction==0) //poruszanie się do góry
            {
                if (row>0) {  // jeśli nie znajdujesz się na samej górze, możesz ruszyć się do góry
                    if (arraySyncBlue.get(row-1) == 0) //jeśli pusto
                    {
                        row--;
                        arraySyncBlue.set(row, 1); //zajmij miejsce
                        arraySyncBlue.set(row+1, 0); //zwolnij poprzednie miejsce
                        draw(direction);
                    }
                }
            }
            else //poruszanie się w dół
            {
                if (row<(rows-1)) // jeśli nie znajdujesz się na samym dole, możesz ruszyć się na dół
                {
                    if (arraySyncBlue.get(row+1) == 0) //jeśli pusto
                    {
                        row++;
                        arraySyncBlue.set(row, 1); //zajmij miejsce
                        arraySyncBlue.set(row-1, 0); //zwolnij poprzednie
                        draw(direction);
                    }
                }
            }
        }
        sleepPlayer();
    }

    public void draw(int direction)
    {
        synchronized (panels[row][columnSync]) { //narysuj gracza w nowym panelu
            panels[row][columnSync].setFigure(3);
            panels[row][columnSync].setNumber(String.valueOf(number));
            SwingUtilities.invokeLater(()->
            {
                panels[row][columnSync].repaint();
            });
        }
        if (direction==0) //poruszanie się do góry
        {
            synchronized (panels[row+1][columnSync]) //wyczyść poprzedni panel
            {
                panels[row+1][columnSync].setFigure(0);
                panels[row+1][columnSync].setNumber("");
                SwingUtilities.invokeLater(()->
                {
                    panels[row+1][columnSync].repaint();
                });
            }
        }
        else //poruszanie się w dół
        {
            synchronized (panels[row-1][columnSync]) //wyczyść poprzedni panel
            {
                panels[row-1][columnSync].setFigure(0);
                panels[row-1][columnSync].setNumber("");
                SwingUtilities.invokeLater(()->{
                    panels[row-1][columnSync].repaint();
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

    public static void setArraySyncBlue(AtomicIntegerArray arraySyncBlue) {
        BluePlayer.arraySyncBlue = arraySyncBlue;
    }

    public static void setRows(int rows) {
        BluePlayer.rows = rows;
    }

    public static void setColumnSync(int columnSync) {
        BluePlayer.columnSync = columnSync;
    }
}
