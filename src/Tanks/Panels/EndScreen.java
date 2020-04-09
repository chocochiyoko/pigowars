package Tanks.Panels;

import Tanks.GameExecute;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class EndScreen extends JPanel {

    BufferedImage player1, player2, lose;
    int winner ;

    public EndScreen (JPanel mainPanel, GameExecute frame){

        try {
            player1 = ImageIO.read(getClass().getResource("/resources/end1.png"));
            player2 = ImageIO.read(getClass().getResource("/resources/end2.png"));
            lose = ImageIO.read(getClass().getResource("/resources/endlose.png"));
        }
        catch (Exception e){
            System.out.println("Failed to load image");
        }

        JButton ReplayButton = new JButton("Replay?");
        ReplayButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.next(mainPanel);
                System.out.println(cardLayout);
            }
        });
        this.add(ReplayButton, BorderLayout.CENTER);

        JButton EndButton = new JButton("Quit?");
        EndButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
            }
        });
        this.add(EndButton, BorderLayout.CENTER);
    }
    public void init (int winner){


        this.winner = winner;
    }

    public void paintComponent (Graphics g){


        super.paintComponent(g);
        if (winner == 1){
            g.drawImage(player1, 0, 0, null);
        }
        if (winner == 2){
            g.drawImage(player2, 0, 0, null);
        }
        if (winner == 3){
            g.drawImage(lose, 0, 0, null);
        }
    }
}
