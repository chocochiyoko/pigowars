package Tanks.Moveables.Bullets;

import Tanks.Moveables.Tank;

import java.awt.image.BufferedImage;

public class PoisonBullet extends Bullet {
    public PoisonBullet(BufferedImage img, int x, int y, int angle) {

        super(img, x, y, angle);
    }

    @Override
    public void damage(Tank enemyTank){
        enemyTank.takeDamage(damagepts);
        enemyTank.setPoison(true);
    }
}
