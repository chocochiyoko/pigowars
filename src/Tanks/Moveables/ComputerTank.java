package Tanks.Moveables;

import Tanks.GameObject;
import Tanks.MapElements.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class ComputerTank extends Tank{

    private Tank enemyTank;
    private GameMap WorldMap;
    private boolean lastRotationClock = false;
    private HashMap worldHash = new HashMap <Point, GameObject>();
    private int deltax, deltay, distance;
    private double deltaRatio, angleRatio;

    public ComputerTank(int x, int y, int vx, int vy, int angle, BufferedImage img, ArrayList<BufferedImage> bulletImgs, GameMap WorldMap) {

        super(x, y, vx, vy, angle, img, bulletImgs);
        R= 3;
        this.WorldMap = WorldMap;
    }
    public void chase(Tank enemyTank) {

        deltax = this.x - enemyTank.getx();
        deltay = this.y - enemyTank.gety();
        distance = Math.abs(deltax) + Math.abs (deltay);
        if (deltay !=0  && vy != 0 ) {
            deltaRatio =  Math.abs((double ) deltax / (double )deltay);
            angleRatio =  Math.abs((double)vx / (double) vy);
        }
        else {
            deltaRatio =0;
            angleRatio = 0;
        }



        //enemy to right, moving left and up;
        if (deltax < 0 && vx <= 0 && vy <= 0){

            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
        //enemy to right, moving left and down ;
        else if (deltax < 0 && vx <= 0 && vy >= 0){
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
        //enemy to left, moving right and down
        else if (deltax > 0 && vx >= 0 && vy >= 0){
            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
        //enemy to left, moving right and up
        else if (deltax > 0 && vx >= 0 && vy <= 0){
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
        //enemy above , moving down and right
        else if (deltay > 0 && vy >= 0 && vx >= 0){
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
        //enemy above , moving down and left
        else if (deltay > 0 && vy >= 0 && vx <= 0){
                angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
        //enemy below , moving up and right
        else if (deltay < 0 && vy <= 0 && vx >= 0){
            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
//        enemy below , moving up and left
        else if (deltay < 0 && vy <= 0 && vx <= 0){
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
//

        else if (distance < 200){
                System.out.println( "delta ratio " + deltaRatio);
                System.out.println( "angle ratio " + angleRatio);

             if (counter%9 ==0) {
                shoot();

            }


            //facing up and right or left and down
            if ((vy <= 0 && vx >= 0) || (vy >= 0 && vx <= 0 )){
                if (Math.abs(angleRatio -deltaRatio) < 0.3){

                }
                else if (angleRatio > deltaRatio){
                    angle -= ROTATIONSPEED;
                }
                else if (angleRatio < deltaRatio){
                    angle += ROTATIONSPEED;
                }

            }
            //facing up and left or right and down
            else if ((vy >= 0 && vx >= 0) || (vy <= 0 && vx <= 0 )){
                if (Math.abs(angleRatio -deltaRatio) < 0.2){

                }
                else if (angleRatio > deltaRatio){
                    angle += ROTATIONSPEED;
                }
                else if (angleRatio < deltaRatio){
                    angle -= ROTATIONSPEED;
                }
            }

            moveBackwards();

        }

        if (this.tankCollisionBool(enemyTank)){
            moveBackwards();
        }

            moveForwards();
        }
//
    }


