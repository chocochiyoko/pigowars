package Tanks.Moveables.Bullets;

import Tanks.GameObject;
import Tanks.Moveables.Tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Bullet extends GameObject {
    protected int vx, vy, angle;
    protected int damagepts = 10;
    protected int speed = 15;

    public Bullet (BufferedImage img, int x, int y, int angle){
        super(img, x, y);
        this.angle = angle;
        vx = (int) Math.round(speed * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(speed * Math.sin(Math.toRadians(angle)));

    }

    public void update (){
        x = x + vx;
        y = y + vy;

    }
 public boolean checkBorder (int width, int height){
     if (x < 0) {
         return true;
     }
     if (x >= width - img.getWidth()) {
         return true;
     }
     if (y < 0) {
         return true;
     }
     if (y >= height - img.getHeight()) {
         return true;
     }
     return false;
 }
    public void shoot (ArrayList<Bullet> tankBullets){
        tankBullets.add(this);
    }
    public void collide (ArrayList<Bullet> tankBullets, String type){
        tankBullets.remove(this);
    }
    public void damage (Tank enemyTank){
        enemyTank.takeDamage(damagepts);
    }

    public void drawImage (Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g2d.drawImage(this.img, rotation, null);
    }

}
