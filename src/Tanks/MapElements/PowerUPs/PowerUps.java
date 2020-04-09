package Tanks.MapElements.PowerUPs;

import Tanks.GameObject;
import Tanks.Moveables.Tank;

import java.awt.image.BufferedImage;

public abstract class PowerUps extends GameObject {

    public PowerUps(BufferedImage img, int x, int y) {
        super(img, x, y);
    }

    public abstract void modify (Tank tank);
}
