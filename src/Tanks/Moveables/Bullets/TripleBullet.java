package Tanks.Moveables.Bullets;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TripleBullet extends Bullet {
    public TripleBullet(BufferedImage img, int x, int y, int angle) {
        super(img, x, y, angle);

    }

    @Override
    public void shoot (ArrayList<Bullet> tankBullets){
        tankBullets.add(this);
        TripleBullet tb2 = new TripleBullet(img, x, y, angle+10);
        TripleBullet tb3 = new TripleBullet(img, x, y, angle-10);
        tankBullets.add(tb2);
        tankBullets.add(tb3);
    }
}
