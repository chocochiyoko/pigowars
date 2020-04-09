package Tanks.MapElements.Walls;

import Tanks.GameObject;

import java.awt.image.BufferedImage;

public abstract class Wall extends GameObject {

    public Wall(BufferedImage img, int x, int y) {
        super(img, x, y);
    }

}

