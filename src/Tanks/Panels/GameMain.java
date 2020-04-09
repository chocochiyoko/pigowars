package Tanks.Panels;


import Tanks.Collisions;
import Tanks.Moveables.Bullets.Bullet;
import Tanks.MapElements.GameMap;
import Tanks.Moveables.ComputerTank;
import Tanks.Moveables.PathfinderTank;
import Tanks.Moveables.Tank;
import Tanks.Moveables.TankControl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class GameMain extends JPanel implements Runnable
{
    private Thread thread;
    private int mapHeight = 1280;
    private int MapWidth = 1280;
    private int winHeight = 700;
    private int winWidth = 1000;
    private Graphics2D g2D;
    private BufferedImage buff;
    private BufferedImage miniBuff;
    private BufferedImage leftBuff;
    private BufferedImage rightBuff;
    private BufferedImage life1;
    private BufferedImage life2;
    private BufferedImage life3;
    protected BufferedImage tankimg;
    protected GameMap gameWorld;
    private Tank tank1;
    private Tank tank2;
    private ComputerTank compTank2;
    private Collisions collisions;
    private ArrayList<Bullet> bullets;
    private BufferedImage bulletImg1;
    private InputStream mapFile, mapFile2;
    protected ArrayList<BufferedImage> bulletImgs = new ArrayList<>();
    private JPanel mainPanel;
    private EndScreen endScreen;
    private boolean newGame = true;
    private int counter;
    private int players;
    private int winner;


    public GameMain (JPanel mainPanel, EndScreen endScreen){

        this.mainPanel = mainPanel;
        this.endScreen = endScreen;

    }

    public void init(int players){
        this.players = players;
        setBackground(Color.RED);
        try {
             tankimg = ImageIO.read(getClass().getResource("/resources/pig.png"));
            life1 = ImageIO.read((getClass().getResource("/resources/life1.png")));
            life2 = ImageIO.read((getClass().getResource("/resources/life2.png")));
            life3 = ImageIO.read((getClass().getResource("/resources/life3.png")));
            bulletImg1 = ImageIO.read((getClass().getResource("/resources/mushroom.png")));
            if (players == 1){
                mapFile = getClass().getResourceAsStream("/resources/map2");
                mapFile2 = getClass().getResourceAsStream("/resources/map2");
            }
            else if (players == 2){
                mapFile = getClass().getResourceAsStream("/resources/map1");
            }

    }
        catch (Exception e){
            System.out.println("Failed to load image");
        }
        bulletImgs.add(bulletImg1);
        System.out.println("before map input: " + mapFile.toString());
        gameWorld = new GameMap(mapFile);
        tank1 = new Tank(200, 200, 0, 0, 30, tankimg, bulletImgs);

        if (players == 1){
            System.out.println("before tank input: " + mapFile.toString());
            tank2 = new PathfinderTank(1000, 1000, 0, 0 , 180, tankimg, bulletImgs, mapFile2, tank1);
        }
        else if (players == 2){
            tank2 = new Tank (1000, 1000, 0, 0 , 180, tankimg, bulletImgs);
            TankControl tankControl2 = new TankControl(tank2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
            addKeyListener(tankControl2);
        }

        TankControl tankControl1 = new TankControl(tank1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        collisions = new Collisions(gameWorld, tank1, tank2);
        addKeyListener(tankControl1);
        setFocusable(true);

    }




    public void drawComponents(){

        tank1.drawImage(g2D);
        tank2.drawImage(g2D);

    }
    public void updateComponents() {
        counter++;
        tank1.update();
        if (players == 1){

            tank2.update();
            tank2.chase(tank1);
        }
        else if (players == 2){
            tank2.update();
        }

        collisions.checkCollisions();
        if (tank1.isKO()){
            System.out.println("game over!");
            newGame = true;
            tank2.reset(1000, 1000, 180);
            if (tank2 instanceof ComputerTank){
                end(3);
            }
            else {
                end(2);
            }

        }
        else if (tank2.isKO()){
            tank1.reset(200, 200, 30);
            System.out.println("game over!");
            newGame = true;
            end(1);
        }
        if (counter % 500 == 0){
            System.out.println("counter");
            gameWorld.update();
        }

    }
    public BufferedImage getCam (Tank tank){
        int camx = tank.getx() - winWidth/4;
        int camy = tank.gety() - winHeight/2;

        if (camx < 0 ){
            camx = 0;
        }
        if (camy < 0){
            camy = 0;
        }
        if (camx > MapWidth - winWidth/2){
            camx = MapWidth - winWidth/2;
        }
        if (camy > mapHeight-winHeight){

            camy = mapHeight-winHeight;
        }
            return buff.getSubimage(camx,camy , winWidth/2, winHeight);


    }
    public void paint(Graphics g) {

        if (newGame){
            this.requestFocusInWindow();
            newGame=false;
        }

        if(buff == null) {

            buff = (BufferedImage) createImage(MapWidth,
                    mapHeight);
            g2D = buff.createGraphics();
            System.out.println("buff null");
            miniBuff = new BufferedImage(213, 213, BufferedImage.TYPE_INT_ARGB);
            leftBuff = (BufferedImage) createImage(winWidth/2,
                    winHeight);
            rightBuff = new BufferedImage( winWidth/2, winHeight, BufferedImage.TYPE_INT_RGB);



        }
        AffineTransform miniscale = new AffineTransform();
        miniscale.scale(.16, .16);
        AffineTransformOp scaleOp = new AffineTransformOp(miniscale, AffineTransformOp.TYPE_BILINEAR);
        leftBuff = getCam(tank1);
        rightBuff = getCam(tank2);

        gameWorld.drawMap(g2D);
        updateComponents();
        drawComponents();
        g.setColor(Color.WHITE);
        miniBuff = scaleOp.filter(buff, miniBuff);
        g.drawImage(leftBuff, 0, 0, this);
        g.drawImage(rightBuff, winWidth/2, 0, this);
        g.fillRect(((winWidth/2)-miniBuff.getWidth()/2)-5, 0, miniBuff.getWidth()+3, miniBuff.getHeight());
        g.fillRect(winWidth/2, miniBuff.getHeight(), 5, winHeight-miniBuff.getHeight());
        g.drawImage(miniBuff, (winWidth/2)-miniBuff.getWidth()/2, 0, this);
        healthBar(g, tank1, 0, 40);
        healthBar(g, tank2, winWidth-200, 40);
        g.setColor(Color.WHITE);
        g.drawRect(0, 40, 200, 30);
        g.drawRect(winWidth-200, 40, 200, 30);
        g.drawImage(lives(tank1), 0, 0, null);
        g.drawImage(lives(tank2), winWidth-200, 0, null);



    }

    public BufferedImage lives (Tank tank){
        if (tank.getlives() == 3) {
            return life3;
        }
        if (tank.getlives() == 2) {
            return life2;
        }
        else return life1;
    }

    public void healthBar (Graphics g, Tank tank, int x, int y){
        if (tank.getHP() > 30){
            g.setColor(Color.GREEN);
        }
        else {
            g.setColor(Color.RED);
        }

        g.fillRect(x, y, tank.getHP()*2, 30);
    }

    public void start() {
        System.out.println();
        thread = new Thread((Runnable) this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {

        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                thread.sleep(10);
                if (tank1.isLostLife() || tank2.isLostLife()){
                    thread.sleep(3000);
                    tank1.setLostLife(false);
                    tank2.setLostLife(false);
                    tank1.reset(200, 200, 0);
                    tank2.reset(1000, 1000, 180);
                    gameWorld.clearPU();

                }
            } catch (InterruptedException e) {
                System.out.println("thread catch");
                break;
            }

        }
    }

    private void end(int winner) {

        endScreen.init(winner);
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.next(mainPanel);
    }

}
