package Tanks.MapElements.PowerUPs;

import Tanks.Moveables.Tank;

import java.awt.image.BufferedImage;

public class TriplePU extends PowerUps {

    public TriplePU(BufferedImage img, int x, int y) {
        super(img, x, y);
    }
    @Override
    public void modify(Tank tank) {
        tank.setBulletType("Triple");
    }
}
