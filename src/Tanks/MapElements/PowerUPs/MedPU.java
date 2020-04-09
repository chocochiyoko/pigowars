package Tanks.MapElements.PowerUPs;

import Tanks.Moveables.Tank;

import java.awt.image.BufferedImage;

public class MedPU extends PowerUps{

    public MedPU(BufferedImage img, int x, int y) {
        super(img, x, y);
    }

    @Override
    public void modify(Tank tank){
        tank.setPoison(false);
    }
}
