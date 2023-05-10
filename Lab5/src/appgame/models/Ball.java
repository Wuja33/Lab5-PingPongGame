package appgame.models;

import appgame.main.Counter;
import appgame.main.GamePanel;
import appgame.main.InGamePanel;
import appgame.main.MainPanel;

import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Ball implements Runnable{
    static SpawnerBalls spawner;
    static Counter labels[][];
    static InGamePanel panels[][];
    static AtomicIntegerArray arraySyncRed;
    static AtomicIntegerArray arraySyncBlue;
    int row;
    int col;
    int lastCol;
    int direction;
    int randV; //random czas
    static int delay;
    boolean isNotified=false;
    static final Object lock = new Object();

    Ball(int row, int col, int lastCol)
    {
        this.row = row;
        this.col = col;
        this.lastCol = lastCol;
        Random rand = new Random();
        randV = rand.nextInt(100);
        if (rand.nextInt(2)==0)  //losowanie kierunku ruchu
            direction = 0;
        else
            direction = 1;
    }

    @Override
    public void run()
    {
        printAndErase();
        ballSleep();
        while (!Thread.currentThread().isInterrupted()) { //poruszanie się piłki w lewo   <<----
            move();
        }
    }

    public void printAndErase()  // 0 - left  1 - right
    {
            if(direction==0) {
                panels[row][col].setFigure(1);
                panels[row][col + 1].setFigure(0);
                panels[row][col].repaint();
                panels[row][col + 1].repaint();
            }
            else {
                panels[row][col].setFigure(1);
                panels[row][col - 1].setFigure(0);
                panels[row][col].repaint();
                panels[row][col - 1].repaint();
            }
    }


    public void move()
    {
        if (col != 2 && col != lastCol && col!=1 && col!=(lastCol+1)) {
            moveInDirection(direction);
            printAndErase();
            ballSleep();
        }
        else if (col==2)
        {
            synchronized (arraySyncRed)
            {
                if(arraySyncRed.get(row)!=0) { //jeśli pole jest zajęte przez gracza, odbij piłkę w drugą stronę
                    direction = 1;
                    moveInDirection(direction);
                    printAndErase();
                }
                else
                {
                    arraySyncRed.set(row,2); //jeśli pole nie jest zajęte przez gracza, zajmij miejsce przez piłke
                    moveInDirection(direction);
                    printAndErase();
                }
            }
                ballSleep();
        }
        else if (col==lastCol)
        {
            synchronized (arraySyncBlue)
            {
                if(arraySyncBlue.get(row)!=0) { //jeśli pole jest zajęte przez gracza, odbij piłkę w drugą stronę
                    direction = 0;
                    moveInDirection(direction);
                    printAndErase();
                }
                else
                {
                    arraySyncBlue.set(row,2); //jeśli pole nie jest zajęte przez gracza, zajmij miejsce przez piłk
                    moveInDirection(direction);
                    printAndErase();
                }
            }
                ballSleep();
        }
        else if (col==1)
        {
            erase(0);
        }
        else
        {
            erase(1);
        }
    }
    public synchronized void erase(int side)
    {
        synchronized (lock) {
            if (side == 0) {
                synchronized (arraySyncRed) {
                    panels[row][1].setFigure(0);
                    SwingUtilities.invokeLater(()->{
                        panels[row][1].repaint(); //wyczyść panel
                    });
                    arraySyncRed.set(row, 0); //wyczyść pozycje w liście
                }
            } else {
                synchronized (arraySyncBlue) {
                    panels[row][lastCol+1].setFigure(0);
                    SwingUtilities.invokeLater(()->
                    {
                        panels[row][lastCol+1].repaint(); //wyczyść panel
                    });
                    arraySyncBlue.set(row, 0); //wyczyść pozycje w liście
                }
            }
                labels[row][side].increaseCounter();
                if(side==0)
                    MainPanel.increaseCounterRed();
                else
                    MainPanel.increaseCounterBlue();
            //uruchamianie spawnera
            synchronized (spawner) {
                spawner.unUsedRows.add(row);
                spawner.isWorking = true;
                SpawnerBalls.setB(this);
                spawner.notify(); //powiadom spawner o dodaniu nowej piłki
            }
            while (!isNotified) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    isNotified=true;
                    Thread.currentThread().interrupt();
                    System.out.println("TUTAJ");
                }
            }
            Thread.currentThread().interrupt(); //zakończ żywot piłki
        }
    }
    public void moveInDirection(int direction)
    {
        if (direction==0)
            col--;
        else
            col++;
    }
    private void ballSleep()
    {
        try {
            Thread.sleep(delay+randV);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Ball Interrupted");
        }
    }

    public static void setPanels(InGamePanel[][] panels) {
        Ball.panels = panels;
    }

    public static void setSpawner(SpawnerBalls spawner) {
        Ball.spawner = spawner;
    }

    public static void setArraySyncBlue(AtomicIntegerArray arraySyncBlue) {
        Ball.arraySyncBlue = arraySyncBlue;
    }

    public static void setArraySyncRed(AtomicIntegerArray arraySyncRed) {
        Ball.arraySyncRed = arraySyncRed;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public static void setDelay(int delay) {
        Ball.delay = delay;
    }

    public static void setLabels(Counter[][] labels) {
        Ball.labels = labels;
    }
}
