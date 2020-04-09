package Tanks;

import Tanks.Panels.EndScreen;
import Tanks.Panels.GameMain;
import Tanks.Panels.StartScreen;

import javax.swing.*;
import java.awt.*;

public class GameExecute extends JFrame {


    CardLayout cardLayout;
    JPanel mainPanel;
    StartScreen start;
    GameMain game;
    EndScreen end;

    public GameExecute() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        end = new EndScreen(mainPanel, this);
        game = new GameMain(mainPanel, end);
        start = new StartScreen(mainPanel, game);
        mainPanel.add(end, "end");
        mainPanel.add(start, "start");
        mainPanel.add(game, "main");
        add(mainPanel);
        cardLayout.show(mainPanel, "start");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
        setSize(1000, 732);
        setTitle("PigO Wars");


    }



    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                GameExecute gameFrame = new GameExecute();

            }

             });

        }
    }

//    public static void main(String argv[]) {
//        final GameMain demo = new GameMain();
//        final StartScreen start = new StartScreen();
//        final EndScreen end = new EndScreen();
//        end.init();
//        start.init();
//        demo.init();
//        JFrame frame = new JFrame("Tanks");
////        f.addWindowListener(new WindowAdapter() {
////        });
////        frame.getContentPane().add("Center", demo);
////        frame.getContentPane().add("Center", start);
//
//        frame.pack();
//        frame.setSize(new Dimension(1000, 700));
//        frame.setVisible(true);
//
////        JPanel cardPanel = new JPanel(new CardLayout());
////        cardPanel.add(start);
////        cardPanel.add(demo);
////        cardPanel.add(end);
//        frame.getContentPane().add("Center", start);
//        while (!start.getStatus()){
//            if (start.getStatus()){
//                System.out.println("switch");
//                frame.remove(start);
//                frame.getContentPane().add("Center", demo);
//                demo.start();
//            }
//        }
//
//        //f.setResizable(false);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        demo.start();
//    }

