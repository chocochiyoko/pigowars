package Tanks.Moveables.Bullets;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BounceBullet extends Bullet{

    private int bounces = 5;

    public BounceBullet(BufferedImage img, int x, int y, int angle) {
        super(img, x, y, angle);

    }
    public void vertical() {
        vy = -vy;
        angle += 180;
        bounces--;
    }
    public void horizontal() {
        vx = -vx;
        angle += 180;
        bounces--;
    }
    @Override

    public boolean checkBorder(int width, int height){
        if (x < 0) {
            horizontal();

            if (bounces > 0){
                return false;
            }
            else return true;
        }
        if (x >= width - img.getWidth()) {
            horizontal();
            if (bounces > 0){
                return false;
            }
            else return true;
        }
        if (y < 0) {
           vertical();
            if (bounces > 0){
                return false;
            }
            else return true;
        }
        if (y >= height - img.getHeight()) {
            vertical();

            if (bounces > 0){
                return false;
            }
            else return true;
        }
        return false;
    }

    @Override
    public void collide (ArrayList<Bullet> tankBullets, String Type){
        if (bounces > 0 && Type == "horizontal"){
            horizontal();
        }
        else if (bounces > 0 && Type == "vertical"){
            vertical();
        }
        else tankBullets.remove(this);
    }
}
