package Tanks.MapElements;

import Tanks.GameObject;
import Tanks.MapElements.PowerUPs.*;
import Tanks.MapElements.Walls.BreakableWall;
import Tanks.MapElements.Walls.UnbreakableWall;
import Tanks.Moveables.Tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameMap {
    private BufferedImage floorImg, breakableImg, unbreakableImg, bounceImg, tripleImg, lifeImg, medImg, poisonImg;
    int mapWidth = 1280, mapHeight = 1280, emptyCount = 0;

        //make 2D array?
        ArrayList<GameObject> collideables = new ArrayList<>();
        ArrayList<String> creatorStrings = new ArrayList<>();


    public GameMap(InputStream file){

        try {
            floorImg = ImageIO.read(getClass().getResource("/resources/flowers.bmp"));
            breakableImg = ImageIO.read((getClass().getResource("/resources/crate.png")));
            unbreakableImg = ImageIO.read((getClass().getResource("/resources/brick.png")));
            bounceImg = ImageIO.read(getClass().getResource("/resources/bounce.png"));
            tripleImg = ImageIO.read(getClass().getResource("/resources/tripleshot.png"));
            lifeImg = ImageIO.read(getClass().getResource("/resources/life.png"));
            medImg = ImageIO.read(getClass().getResource("/resources/med.png"));
            poisonImg = ImageIO.read(getClass().getResource("/resources/poison.png"));

            Scanner stringScanner = new Scanner(file);

            while (stringScanner.hasNextLine())
            {
            creatorStrings.add(stringScanner.nextLine());
            }

            System.out.println("creator string size map " + creatorStrings.size());

            for (int i = 0; i < creatorStrings.size(); i++){
                Scanner intScanner = new Scanner(creatorStrings.get(i));
                int currWidth = 0;
                while (intScanner.hasNextInt()) {
                    int currBlock = intScanner.nextInt();
                    switch (currBlock){
                        case 0:
                            currWidth ++;
                            emptyCount++;
                            break;
                        case 2:

                            collideables.add(new BreakableWall(breakableImg, currWidth*32, i*32));
                            currWidth ++;
                            break;
                        case 1:

                            collideables.add(new UnbreakableWall(unbreakableImg, currWidth*32, i*32));
                            currWidth ++;
                            break;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Cannot Scan file");
        }
    }
    public void update () {
        System.out.println("empty count" + emptyCount);
        Random rand = new Random();
        int bounce = rand.nextInt(emptyCount);
        int triple = rand.nextInt(emptyCount);
        int life = rand.nextInt(emptyCount);
        int poison = rand.nextInt(emptyCount);
        int med = rand.nextInt(emptyCount);
        int counter = 0;
        System.out.println(bounce);

        for (int i = 0; i < creatorStrings.size(); i++) {
            int currWidth = 0;
            Scanner intScanner = new Scanner(creatorStrings.get(i));
            while (intScanner.hasNextInt()) {
               if (intScanner.nextInt() == 0)
               {
                   if (counter == bounce){
                       collideables.add(new BouncePU(bounceImg, currWidth*32, i*32));
                   }
                   else if (counter == triple) {
                       collideables.add(new TriplePU(tripleImg, currWidth*32, i*32));
                   }
                   else if (counter == life) {
                       collideables.add(new LifePU(lifeImg, currWidth*32, i*32));
                   }
                   else if (counter == med) {
                       collideables.add(new MedPU(medImg, currWidth*32, i*32));
                   }
                   else if (counter == poison) {
                       collideables.add(new PoisonPU(poisonImg, currWidth*32, i*32));
                   }
                   counter++;
               }


                currWidth++;
            }
        }
    }

    public ArrayList<GameObject> getArray (){
        return collideables;
    }

    public int getWidth (){
        return mapWidth;
    }
    public int getHeight (){
        return mapHeight;
    }
    public void drawMap (Graphics g){
        drawMapFloor(g);
        drawcollideables(g);
    }
    public void drawMapFloor(Graphics g) {
        int TileWidth = floorImg.getWidth(null);
        int TileHeight = floorImg.getHeight(null);
        Graphics2D g2d = (Graphics2D) g;

        int NumberX = (int) (mapWidth / TileWidth);
        int NumberY = (int) (mapHeight/ TileHeight);

        for (int i = 0; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2d.drawImage(floorImg, j * TileWidth,
                        i * TileHeight, TileWidth,
                        TileHeight, null);
            }
        }

    }
    public void drawcollideables(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < collideables.size(); i++){
            collideables.get(i).drawImage(g2d);
        }
    }

    public void clearPU (){
        for (int i = 0; i< collideables.size(); i++){
            if (collideables.get(i) instanceof PowerUps){
                collideables.remove(i);
            }
        }
    }

    public ArrayList<GameObject> checkTankCollision (Tank tank){
        ArrayList<GameObject> ReturnVal = new ArrayList<>();


        for (int i = 0; i< collideables.size(); i++){


            if (tank.getRect().intersects(collideables.get(i).getRect()))
            {

                ReturnVal.add(collideables.get(i));
                if (collideables.get(i) instanceof PowerUps){
                    collideables.remove(i); 
                }
            }

        }
        return ReturnVal;
    }

    public void checkBulletCollision (Tank tank){
        for (int i = 0; i < tank.getBullets().size(); i++){
            for (int j = 0; j < collideables.size(); j++){
                if (collideables.get(j).getRect().intersects(tank.getBullets().get(i).getRect())){
                    Rectangle intersection = collideables.get(j).getRect().intersection(tank.getBullets().get(i).getRect());
                    if ( collideables.get(j) instanceof BreakableWall){
                        collideables.remove(j);
                        tank.getBullets().remove(i);
                    }
                    else if (collideables.get(j) instanceof PowerUps){

                    }
                    else if (intersection.height > intersection.width){
                        tank.getBullets().get(i).collide(tank.getBullets(), "horizontal");
                    }
                    else{
                        tank.getBullets().get(i).collide(tank.getBullets(), "vertical");
                    }

                    break;
                }
            }
        }
    }
}
