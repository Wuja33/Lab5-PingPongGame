package appgame.main;

import appgame.tools.ParametrsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {
    //panele
    GamePanel gamePanel;
    JPanel parametersPanel;
    //pola tekstowe
    JTextField field_balls;
    JTextField field_RedPlayers;
    JTextField field_BluePlayers;
    JTextField field_columns;
    JTextField field_rows;
    JTextField field_sleep;
    //etykiety do pól tekstowych
    JLabel label_balls;
    JLabel label_RedPlayers;
    JLabel label_BluePlayers;
    JLabel label_columns;
    JLabel label_rows;
    JLabel label_slepp;
    //liczniki
    public static JLabel counterRed;
    public static JLabel counterBlue;
    public static int countRed;
    public static int countBlue;
    //przyciski
    JButton button_start;
    JButton button_stop;
    //parametry
    int balls;
    int redPlayers;
    int bluePlayers;
    int rows;
    int columns;
    int sleep;
    int gamePanelwidth = 1000;
    int gamePanelheight = 800;
    int parametrsPanelwidth = 200;
    int parametrsPanelheight = 700;

    public MainPanel()
    {
        this.setPreferredSize(new Dimension(gamePanelwidth+parametrsPanelwidth,gamePanelheight));

        //tworzenie panelu parametrów
        parametersPanel = new JPanel();
        parametersPanel.setPreferredSize(new Dimension(parametrsPanelwidth,parametrsPanelheight));
        parametersPanel.setLayout(new GridLayout(9,1,10,40));

        //tworzenie pól do parametrów
        field_balls = new JTextField();
        field_RedPlayers = new JTextField();
        field_BluePlayers = new JTextField();
        field_columns = new JTextField();
        field_rows = new JTextField();
        field_sleep = new JTextField();

        //tworzenie etykiet do pól tekstowych
        label_balls = new JLabel("Ilość piłek", SwingConstants.CENTER);
        label_RedPlayers = new JLabel("Ilość graczy",SwingConstants.CENTER);
        label_BluePlayers = new JLabel("Ilość graczy",SwingConstants.CENTER);
        label_columns = new JLabel("Ilość kolumn",SwingConstants.CENTER);
        label_rows = new JLabel("Ilość wierszy",SwingConstants.CENTER);
        label_slepp = new JLabel("Opóźnienie (ms)",SwingConstants.CENTER);
        refreshLabels();

        //tworzenie przycisków
        button_start = new JButton("START");
        button_stop= new JButton("STOP");

        //tworzenie liczników
        counterRed = new JLabel("0",SwingConstants.CENTER);
        counterRed.setForeground(Color.RED);
        counterRed.setFont(new Font(Font.SERIF,Font.PLAIN,20));
        counterBlue = new JLabel("0",SwingConstants.CENTER);
        counterBlue.setForeground(Color.BLUE);
        counterBlue.setFont(new Font(Font.SERIF,Font.PLAIN,20));


        //przejmowanie wartości parametrów
        field_balls.addActionListener((event)-> {
            balls = ParametrsPanel.getValueFromText(field_balls.getText(),-1,rows+1,balls);
            refreshLabels();
        });
        field_RedPlayers.addActionListener((event)-> {
            redPlayers = ParametrsPanel.getValueFromText(field_RedPlayers.getText(),-1,rows+1,redPlayers);
            refreshLabels();
        });
        field_BluePlayers.addActionListener((event)-> {
            bluePlayers = ParametrsPanel.getValueFromText(field_BluePlayers.getText(),-1,rows+1,bluePlayers);
            refreshLabels();
        });
        field_columns.addActionListener((event)-> {
            columns = ParametrsPanel.getValueFromTextOdd(field_columns.getText(),7-1,71,columns);
            refreshLabels();
        });
        field_rows.addActionListener((event)-> {
            rows = ParametrsPanel.getValueFromText(field_rows.getText(),0,51,rows);
            refreshLabels();
        });
        field_sleep.addActionListener((event)->
        {
            sleep = ParametrsPanel.getValueFromText(field_sleep.getText(),54,sleep);
            refreshLabels();
        });


        //dodawanie pól tekstowych oraz etykiet do panelu z parametrami
        parametersPanel.add(ParametrsPanel.createPanel(label_columns,field_columns));
        parametersPanel.add(ParametrsPanel.createPanel(label_rows,field_rows));
        parametersPanel.add(ParametrsPanel.createPanel(label_balls,field_balls));
        parametersPanel.add(ParametrsPanel.createPanel(label_RedPlayers,field_RedPlayers));
        parametersPanel.add(ParametrsPanel.createPanel(label_BluePlayers,field_BluePlayers));
        parametersPanel.add(ParametrsPanel.createPanel(label_slepp,field_sleep));
        parametersPanel.add(ParametrsPanel.createCounterPanel(counterRed,counterBlue));
        parametersPanel.add(button_start);
        parametersPanel.add(button_stop);

        //tworzenie panelu gry
        gamePanel = new GamePanel();
        //ustawianie głównego panelu
        this.setLayout(new BorderLayout(20,20));
        this.add(parametersPanel,BorderLayout.EAST);
        this.add(gamePanel,BorderLayout.CENTER);

        //działanie przycisków
        button_start.addActionListener(e ->{
            if (checkParameters()) {
                button_start.setEnabled(false);
                //tworzenie nowego panelu gry oraz usuwanie starego, resetowanie liczników
                countRed=0;
                countBlue=0;
                counterRed.setText("0");
                counterBlue.setText("0");
                this.remove(gamePanel);
                gamePanel = new GamePanel(rows,columns,redPlayers,bluePlayers,balls,sleep);
                this.add(gamePanel);
                this.revalidate();
                gamePanel.start();
            }
            else
                JOptionPane.showMessageDialog(null,"Niepoprawne wartości parametrów!","ERROR",JOptionPane.WARNING_MESSAGE);
        });
        button_stop.addActionListener(e -> {
            gamePanel.stop();
            button_start.setEnabled(true);
        });

    }



    public void refreshLabels()
    {
        label_balls.setText("<html><center>Ilość piłek: "+balls+"<br>0&lt;=P&lt;="+rows+"</html>");
        label_RedPlayers.setText("<html><center>Ilość czerwonych graczy: "+redPlayers+"<br>0&lt;RP<="+rows+"</html>");
        label_BluePlayers.setText("<html><center>Ilość niebieskich graczy: "+bluePlayers+"<br>0&lt;BP&lt;="+rows+"</html>");
        label_columns.setText("<html><center>Ilość kolumn: "+columns+"<br>7&lt;=K&lt;=71 && k -- nieparzysta</html>");
        label_rows.setText("<html><center>Ilość wierszy: "+rows+"<br>0&lt;R&lt;50</html>");
        label_slepp.setText("<html><center>Opóźnienie (ms): "+sleep+"<br>54&lt;S</html>");
    }
    public boolean checkParameters()
    {
        if (balls<=rows&&
                redPlayers<=rows&&
                bluePlayers<=rows&&
                columns>=7&&columns%2!=0&&
                rows>0&&sleep>=55&&rows<56)
            return true;
        else
            return false;
    }

    public static void increaseCounterRed()
    {
        countRed++;
        counterRed.setText(String.valueOf(countRed));
    }
    public static void increaseCounterBlue()
    {
        countBlue++;
        counterBlue.setText(String.valueOf(countBlue));
    }
}
