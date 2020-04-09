package Tanks;

import Tanks.MapElements.GameMap;
import Tanks.Moveables.Tank;

import java.util.ArrayList;

public class Collisions {
    private GameMap WorldMap;
    private Tank tank1, tank2;

    public Collisions (GameMap w, Tank t1, Tank t2){
        WorldMap = w;
        tank1 = t1;
        tank2 = t2;
    }

    public void checkCollisions (){
        ArrayList<GameObject> check = WorldMap.checkTankCollision(tank1);
        if (!check.isEmpty()){
            System.out.println("world collide t1");
            tank1.allCollisions(check);
        }

        check = WorldMap.checkTankCollision(tank2);
        if (!check.isEmpty()){
            System.out.println("world collide t2");
            tank2.allCollisions(check);
        }
        WorldMap.checkBulletCollision(tank1);
        WorldMap.checkBulletCollision(tank2);
        tank1.checkBorder(WorldMap.getWidth(), WorldMap.getHeight());
        tank2.checkBorder(WorldMap.getWidth(), WorldMap.getHeight());
        tank1.tankCollision(tank2);
        tank2.tankCollision(tank1);

    }


}
