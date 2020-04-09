package Tanks;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected BufferedImage img;
    protected int x, y, height, width;

    public GameObject (BufferedImage img, int x, int y){
        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight();
        this.width = img.getWidth();
    }
    public Rectangle getRect (){
        return new Rectangle(x, y, width, height);
    }

    public void drawImage (Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, x, y, null);
    }

}
