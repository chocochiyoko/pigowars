package Tanks.Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class StartScreen extends JPanel {

    private boolean startStatus = false;
    private int players = 0;
    BufferedImage background;

    public StartScreen(JPanel mainPanel, GameMain gameMain) {


        try {
            background = ImageIO.read(getClass().getResource("/resources/StartScreen.png"));

        }
        catch (Exception e){
            System.out.println("Failed to load image on start");
        }




        JButton StartButton1 = new JButton("Start 1 Player");
        StartButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMain.init(1);
                gameMain.start();
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.next(mainPanel);

            }
        });

        JButton StartButton2 = new JButton("Start 2 Player");
        StartButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameMain.init(2);
                gameMain.start();
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.next(mainPanel);

            }
        });

        this.setLayout(null);
        this.add(StartButton1, null);
        this.add(StartButton2, null);
        StartButton1.setBounds(300, 450, 150, 40);
        StartButton2.setBounds(550, 450, 150, 40);
        StartButton1.setBackground(Color.white);
        StartButton2.setBackground(Color.white);
        StartButton1.setBorderPainted(false);
        StartButton2.setBorderPainted(false);


    }

    @Override
    public void paintComponent (Graphics g){


        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }

}
