package appgame.main;

import appgame.models.Ball;
import appgame.models.BluePlayer;
import appgame.models.RedPlayer;
import appgame.models.SpawnerBalls;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class GamePanel extends JPanel {
    InGamePanel panels[][];
    public Counter labels[][];
    AtomicIntegerArray arraySyncRed;
    AtomicIntegerArray arraySyncBlue;
    public static List<Thread> listOfThreads = new ArrayList<>();
    SpawnerBalls spawnerBalls;
    int redPlayers;
    int bluePlayers;
    int balls;
    int rows;
    int columns;
    GamePanel(){}
    GamePanel(int rows,int columns,int redPlayers, int bluePlayers, int balls, int delay)
    {
        this.setLayout(new GridLayout(rows,columns,2,2));
        this.redPlayers = redPlayers;
        this.bluePlayers = bluePlayers;
        this.balls = balls;
        this.rows = rows;
        this.columns = columns;
        this.panels = new InGamePanel[rows][columns];
        this.labels = new Counter[rows][2];
        this.arraySyncRed = new AtomicIntegerArray(rows);
        this.arraySyncBlue= new AtomicIntegerArray(rows);
        this.spawnerBalls = new SpawnerBalls(rows,columns,balls);
        //dodawanie zmiennych statycznych
        Ball.setSpawner(spawnerBalls);
        Ball.setPanels(panels);
        Ball.setArraySyncRed(arraySyncRed);
        Ball.setArraySyncBlue(arraySyncBlue);
        Ball.setDelay(delay);
        Ball.setLabels(labels);
        RedPlayer.setArraySyncRed(arraySyncRed);
        RedPlayer.setRows(rows);
        BluePlayer.setArraySyncBlue(arraySyncBlue);
        BluePlayer.setColumnSync(columns-2);
        BluePlayer.setRows(rows);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                panels[r][c] = new InGamePanel();
                if (c==0) //tworzenie liczników dla drużyny czerwonej
                {
                    labels[r][0] = new Counter();
                    panels[r][c].setLayout(new GridBagLayout());
                    panels[r][c].add(labels[r][0]);
                }
                else if (c==columns-1) //tworzenie liczników dla drużyny niebieskiej
                {
                    labels[r][1] = new Counter();
                    panels[r][c].setLayout(new GridBagLayout());
                    panels[r][c].add(labels[r][1]);
                }
                this.add(panels[r][c]);
            }
        }

    }

    public void start()
    {
        listOfThreads.add(spawnerBalls);
        spawnerBalls.startRand();
        setRandomPlayers();
        listOfThreads.forEach(thread -> thread.start());
    }

    public void stop()
    {
        spawnerBalls.interrupt();
        try {
            Thread.sleep(50);
        }
        catch (Exception e)
        {
            System.out.println("Exception");
        }
        listOfThreads.forEach((thread -> thread.interrupt()));

        listOfThreads.clear();
    }

    public void setRandomPlayers()
    {
        Random random = new Random();
        int randomValue;
        int amountRedPlayers = redPlayers;
        int amountBluePlayers = bluePlayers;
        List<Integer> listOfIndexAvalibleRed = new ArrayList<>();
        List<Integer> listOfIndexAvalibleBlue= new ArrayList<>(listOfIndexAvalibleRed);
        for (int i = 0; i < rows; i++) {
            listOfIndexAvalibleRed.add(i); //wczytywanie dostępnych indeksów wierszy
            listOfIndexAvalibleBlue.add(i);
        }
        List<Integer> listOfChosenIndexRed = new ArrayList<>();
        List<Integer> listOfChosenIndexBlue = new ArrayList<>();
        //losowanie czerwonych
        while (amountRedPlayers>0)
        {
            randomValue = random.nextInt(listOfIndexAvalibleRed.size()); //losuje dostępne indeksy listy
            listOfChosenIndexRed.add(listOfIndexAvalibleRed.get(randomValue));
            listOfIndexAvalibleRed.remove(randomValue);
            amountRedPlayers--;
        }
        //losowanie niebieskich
        while (amountBluePlayers>0)
        {
            randomValue = random.nextInt(listOfIndexAvalibleBlue.size()); //losuje dostępne indeksy listy
            listOfChosenIndexBlue.add(listOfIndexAvalibleBlue.get(randomValue));
            listOfIndexAvalibleBlue.remove(randomValue);
            amountBluePlayers--;
        }

        for (Integer i :
                listOfChosenIndexRed) {
            listOfThreads.add(new Thread(new RedPlayer(i))); //tworzenie czerwonego gracza
            arraySyncRed.set(i,1); //dodawanie graczy do listy atomowej 2 - piłka 1 - gracz 0 - pusto
        }
        for (Integer i :
                listOfChosenIndexBlue) {
            listOfThreads.add(new Thread(new BluePlayer(i))); //tworzenie niebieskiego gracza
            arraySyncBlue.set(i,1); //dodawanie graczy do listy atomowej 2 - piłka 1 - gracz 0 - pusto
        }
    }

    public Counter[][] getLabels() {
        return labels;
    }

    public static void deleteThread(Thread thread)
    {
        synchronized (listOfThreads)
        {
            listOfThreads.remove(thread);
        }
    }

    public int getColumns() {
        return columns;
    }
}
