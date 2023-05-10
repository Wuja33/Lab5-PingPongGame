package appgame.models;

import appgame.main.GamePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnerBalls extends Thread{
    List<Integer> unUsedRows;
    int col;
    int columns;
    int amountOfBalls;
    boolean isWorking = false;
    static public Ball b;

    public SpawnerBalls(int rows, int columns, int balls)
    {
        this.unUsedRows = new ArrayList<>();
        this.col = (columns-1)/2;
        this.columns = columns;
        this.amountOfBalls = balls;
        for (int i = 0; i < rows; i++) { //tworzenie listy nieużywanych wierszy
            unUsedRows.add(i);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
        {
            add();
        }
    }

    public synchronized void add(){
        while (!isWorking) {
            try {
                this.wait(); //jeśli dodasz piłkę, czekaj na notify
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread spawner interrupted!");
                break;
            }
        }
        Random rand = new Random();
        if (unUsedRows.size()>0) {
            int randValue = rand.nextInt(unUsedRows.size());
            Thread ball = new Thread(new Ball(unUsedRows.get(randValue), col, columns - 3));
            unUsedRows.remove(randValue);
            synchronized (GamePanel.listOfThreads) {
                GamePanel.listOfThreads.add(ball);
            }
            ball.start();
        }
            isWorking = false; //zakończono dodawanie
        synchronized (b) {
            b.setNotified(true);
            b.notify();
        }
    }

    public void startRand()
    {
        //tworzenie startowych piłek
        for (int i = 0; i < amountOfBalls; i++) {
            Random rand = new Random();
            int randValue = rand.nextInt(unUsedRows.size());
            Thread ball = new Thread(new Ball(unUsedRows.get(randValue),col,columns-3));
            GamePanel.listOfThreads.add(ball);
            unUsedRows.remove(randValue);
        }
    }

    public static void setB(Ball b) {
        SpawnerBalls.b = b;
    }
}
