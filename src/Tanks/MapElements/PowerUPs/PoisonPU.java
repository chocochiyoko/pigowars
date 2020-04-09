package Tanks.MapElements.PowerUPs;

import Tanks.Moveables.Tank;

import java.awt.image.BufferedImage;

public class PoisonPU extends PowerUps {

    public PoisonPU(BufferedImage img, int x, int y) {
        super(img, x, y);
    }

    @ Override
    public void modify (Tank tank){
        tank.setBulletType("Poison");
    }
}
