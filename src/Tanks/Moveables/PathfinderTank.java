package Tanks.Moveables;

import Tanks.MapElements.Walls.BreakableWall;
import Tanks.MapElements.Walls.UnbreakableWall;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.Scanner;

public class PathfinderTank extends Tank {
    private HashMap<Point, Integer> maplayout = new HashMap<>();
    private ArrayList<String> creatorStrings = new ArrayList<>();
    private Tank enemyTank;
    private ArrayList<Point> path = new ArrayList<>();
    private HashMap<Point, PathNodes> nodeMap = new HashMap<>();
    private PriorityQueue<PathNodes> nodeQueue = new PriorityQueue<>(16, (a, b) -> a.getPathLength()-b.getPathLength());
    private HashMap<Point, Integer> visited = new HashMap<>();
    private int deltax, deltay, distance;
    private double deltaRatio, angleRatio;
    private boolean lastRotationClock = false;
    private ArrayList<Point> lastpath = new ArrayList<>();
    private Point destination;


    public PathfinderTank(int x, int y, int vx, int vy, int angle, BufferedImage img, ArrayList<BufferedImage> bulletImgs, InputStream mapfile, Tank enemyTank) {
        super(x, y, vx, vy, angle, img, bulletImgs);
        this.enemyTank = enemyTank;

        Scanner stringScanner = new Scanner(mapfile);

        while (stringScanner.hasNextLine())
        {
            creatorStrings.add(stringScanner.nextLine());
        }

        for (int i = 0; i < creatorStrings.size(); i++){
            Scanner intScanner = new Scanner(creatorStrings.get(i));
            int currWidth = 0;
            while (intScanner.hasNextInt()) {
                int currBlock = intScanner.nextInt();
                maplayout.put(new Point(currWidth, i), currBlock);
                visited.put(new Point(currWidth, i), 0);
                currWidth ++;
            }
        }

        Iterator mapLayoutIterator = maplayout.entrySet().iterator();
        while (mapLayoutIterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)mapLayoutIterator.next();
            Point key = (Point) mapElement.getKey();
            if ((int) mapElement.getValue() == 1 || (int) mapElement.getValue() == 2){

                Point left = new Point(key.x - 1, key.y);
                Point right = new Point(key.x + 1, key.y);
                Point down = new Point(key.x, key.y + 1 );
                Point up = new Point(key.x, key.y - 1 );
                Point leftup = new Point(key.x - 1, key.y - 1);
                Point rightup = new Point(key.x + 1, key.y - 1);
                Point leftdown = new Point(key.x - 1, key.y + 1 );
                Point rightdown = new Point(key.x + 1, key.y + 1 );
                if (maplayout.containsKey(left) && maplayout.get(left) == 0){
                    maplayout.replace(left, 3);
                }
                if (maplayout.containsKey(right) && maplayout.get(right ) == 0){
                    maplayout.replace(right, 3);
                }
                if (maplayout.containsKey(down) && maplayout.get(down ) == 0){
                    maplayout.replace(down, 3);
                }
                if (maplayout.containsKey(up) && maplayout.get(up) == 0){
                    maplayout.replace(up, 3);
                }
                if (maplayout.containsKey(leftup) && maplayout.get(leftup) == 0){
                    maplayout.replace(leftup, 3);
                }
                if (maplayout.containsKey(rightup) && maplayout.get(rightup) == 0){
                    maplayout.replace(rightup, 3);
                }
                if (maplayout.containsKey(rightdown) && maplayout.get(rightdown) == 0){
                    maplayout.replace(rightdown, 3);
                }
                if (maplayout.containsKey(leftdown) && maplayout.get(leftdown) == 0){
                    maplayout.replace(leftdown, 3);
                }
            }
            else if (key.x == 0 || key.x == 38 || key.y == 0 || key.y == 38){
                maplayout.replace(key, 3);
            }
        }
    }

    private Point findPoint(Tank tank){
        return new Point (tank.getx()/32, tank.gety()/32);

    }

    private int[] findDirection(Point currPath, Point otherTank){
        int directionOrder[] = new int[4];
        int deltay = currPath.y-otherTank.y;
        int deltax = currPath.x-otherTank.x;

        if (Math.abs(deltax) >= Math.abs(deltay)){
            if (deltax <= 0){
                directionOrder[0] = -1;
                directionOrder[3] = 1;
            }
            else {
                directionOrder[0] = 1;
                directionOrder[3] = -1;
            }
            if (deltay <=0){
                directionOrder[1] = -2;
                directionOrder[2] = 2;
            }
            else {
                directionOrder[1] = 2;
                directionOrder[2] = -2;
            }
        }
        else {
            if (deltax <= 0){
                directionOrder[1] = -1;
                directionOrder[2] = 1;
            }
            else {
                directionOrder[1] = 1;
                directionOrder[2] = -1;
            }
            if (deltay <=0){
                directionOrder[0] = -2;
                directionOrder[3] = 2;
            }
            else {
                directionOrder[0] = 2;
                directionOrder[3] = -2;
            }

        }

        return directionOrder;

    }

    public Point addPointToPath(Point curr,int direction, Map<Point, Integer> mapLayout, Map<Point, Integer> visited){

        Point left = new Point (curr.x - 1, curr.y);
        Point right = new Point (curr.x + 1, curr.y);
        Point up = new Point (curr.x, curr.y - 1);
        Point down = new Point (curr.x, curr.y + 1);

        if (curr.equals(destination)){
            return destination;
        }

        if (direction == 1 && mapLayout.containsKey(left) && mapLayout.get( left ) == 0 && visited.get(left) != 1) {
            visited.replace(left, 1);
            return left;

        }
        else if (direction == -1 && mapLayout.containsKey(right) && mapLayout.get(right) == 0 && visited.get(right) != 1) {
            visited.replace(right, 1);
            return right;
        }
        else if (direction == 2 && mapLayout.containsKey(up)  && mapLayout.get(up) == 0 && visited.get(up) != 1) {
            visited.replace(up, 1);
            return up;
        }
        else if (direction == -2 && mapLayout.containsKey(down) && mapLayout.get(down) == 0 && visited.get(down) != 1) {
            visited.replace(down, 1);
            return down;
        }
        else {
            return null;
        }


    }

    public ArrayList<Point> findPath (){

        path.clear();
        nodeMap.clear();
        nodeQueue.clear();
        int pathlength = 0;
        destination = findPoint(enemyTank);

        Point curr = findPoint(this);
        int[] Priority = findDirection(curr, destination);
        Point temp = null;
        PathNodes tempNode = null;
        PathNodes lastNode = new PathNodes(null, curr, pathlength);
        visited.replaceAll((key, oldValue) -> 0);
        visited.replace(curr, 1);
        nodeQueue.add(new PathNodes(null, curr, pathlength));
        nodeMap.put(curr, new PathNodes(null, curr, pathlength));

        while (!nodeQueue.peek().getCurr().equals(destination)){
            Priority = findDirection(curr, destination);
            pathlength++;
            Point currPoint = nodeQueue.peek().getCurr();
           for (int i = 0; i < 4; i++){
               temp = addPointToPath(currPoint, Priority[i], maplayout, visited );

               if (temp != null ){
                   nodeQueue.add(new PathNodes(currPoint, temp, pathlength));
                   nodeMap.put(temp, new PathNodes(currPoint, temp, pathlength));
                  // lastNode = new PathNodes(currPoint, temp, pathlength);
               }
           }
           nodeQueue.poll();
           if (nodeQueue.size() == 0 ){
               System.out.println("break!");
               break;
           }

        }
        if (nodeQueue.size() != 0 ){
            //lastpath.clear();
            tempNode = nodeQueue.peek();
            path.add(0, tempNode.getCurr());
            while (tempNode.getLast() != null) {
                tempNode = nodeMap.get(tempNode.getLast());
                path.add( tempNode.getCurr());
                //lastpath.add(tempNode.getCurr());
            }
        }
//        else {
//
//            Point estimate = new Point(0,0);
//            Point result = new Point (0,0);
//            while (!estimate.equals(new Point (1280, 1280))){
//
//                System.out.println("looking for estimate x " + estimate.x + "y " + estimate.y );
//                estimate = new Point (estimate.x +1, estimate.y);
//                int estimatedif = Math.abs(estimate.x - destination.x) + Math.abs(estimate.y - destination.y);
//                int resultdif =  Math.abs(result.x - destination.x) + Math.abs(result.y - destination.y);
//                if (nodeMap.containsKey(estimate) && resultdif > estimatedif){
//                    result = estimate;
//                }
//                if (estimate.x > 1280){
//                    estimate.x = 0;
//                    estimate.y = estimate.y + 1;
//                }
//
//            }
//            tempNode = nodeMap.get(result);
//            while (tempNode.getLast() != null) {
//                System.out.println("in loop");
//                tempNode = nodeMap.get(tempNode.getLast());
//                path.add(tempNode.getCurr());
//            }
//            //return lastpath;
//        }
        return path;
    }
    @Override
    public void chase(Tank otherTank) {

        path = findPath();
        if (path.isEmpty()){
            this.setBack();
            return;
        }
        if (path.size() > 4) {
            deltax = this.x - (path.get(path.size() - 5).x) * 32 + 16;
            deltay = this.y - (path.get(path.size() - 5).y) * 32 + 16;
        } else {
            deltax = this.x - (path.get(path.size() - 1).x) * 32 + 16;
            deltay = this.y - (path.get(path.size() - 1).y) * 32 + 16;
        }
        int deltaxtank = this.x - otherTank.getx();
        int deltaytank = this.y - otherTank.gety();
        double deltaTankRatio;
        distance = Math.abs(deltax) + Math.abs(deltay);
        int distanceToTank = Math.abs(deltaxtank) + Math.abs(deltaytank);
        if (deltay != 0 && vy != 0 && deltaytank != 0) {
            deltaRatio = Math.abs((double) deltax / (double) deltay);
            angleRatio = Math.abs((double) vx / (double) vy);
            deltaTankRatio = Math.abs((double) deltaxtank/ (double) deltaytank);
        } else {
            deltaRatio = 0;
            angleRatio = 0;
            deltaTankRatio = 0;
        }

        boolean go = false;
        //enemy to right, moving left and up;
        if (deltax < 0 && vx <= 0 && vy <= 0) {

            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
        //enemy to right, moving left and down ;
        else if (deltax < 0 && vx <= 0 && vy >= 0) {
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
        //enemy to left, moving right and down
        else if (deltax > 0 && vx >= 0 && vy >= 0) {
            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
        //enemy to left, moving right and up
        else if (deltax > 0 && vx >= 0 && vy <= 0) {
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
        //enemy above , moving down and right
        else if (deltay > 0 && vy >= 0 && vx >= 0) {
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        }
        //enemy above , moving down and left
        else if (deltay > 0 && vy >= 0 && vx <= 0) {
            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
        //enemy below , moving up and right
        else if (deltay < 0 && vy <= 0 && vx >= 0) {
            angle += ROTATIONSPEED;
            lastRotationClock = true;
        }
//        enemy below , moving up and left
        else if (deltay < 0 && vy <= 0 && vx <= 0) {
            angle -= ROTATIONSPEED;
            lastRotationClock = false;
        } else if (distanceToTank < 200 && vx * deltaxtank <= 0 && vy * deltaytank <= 0) {

            if (counter % 9 == 0) {
                shoot();

            }


            //facing up and right or left and down
            if ((vy <= 0 && vx >= 0) || (vy >= 0 && vx <= 0)) {
                if (Math.abs(angleRatio - deltaTankRatio) < 0.2) {

                } else if (angleRatio > deltaTankRatio) {
                    angle -= ROTATIONSPEED;
                } else if (angleRatio < deltaTankRatio) {
                    angle += ROTATIONSPEED;
                }

            }
            //facing up and left or right and down
            else if ((vy >= 0 && vx >= 0) || (vy <= 0 && vx <= 0)) {
                if (Math.abs(angleRatio - deltaTankRatio) < 0.2) {

                } else if (angleRatio > deltaTankRatio) {
                    angle += ROTATIONSPEED;
                } else if (angleRatio < deltaTankRatio) {
                    angle -= ROTATIONSPEED;
                }
            }

            moveBackwards();

        } else {
            go = true;
            System.out.println("no angle change");
            if ((vy <= 0 && vx >= 0) || (vy >= 0 && vx <= 0)) {
                if (Math.abs(angleRatio - deltaRatio) < 0.3) {

                } else if (angleRatio > deltaRatio) {
                    angle -= ROTATIONSPEED;
                } else if (angleRatio < deltaRatio) {
                    angle += ROTATIONSPEED;
                }

            }
            //facing up and left or right and down
            else if ((vy >= 0 && vx >= 0) || (vy <= 0 && vx <= 0)) {
                if (Math.abs(angleRatio - deltaRatio) < 0.2) {

                } else if (angleRatio > deltaRatio) {
                    angle += ROTATIONSPEED;
                } else if (angleRatio < deltaRatio) {
                    angle -= ROTATIONSPEED;
                }
            }

        }


        if (this.tankCollisionBool(enemyTank)) {
            moveBackwards();
        }
        if (!stop || (vy == 0 && vx == 0)) {

            moveForwards();
        } else if (stop){
            moveBackwards();
            if (lastRotationClock){
                angle -= ROTATIONSPEED * 3;
            }
            else {
                angle += ROTATIONSPEED * 3;
            }
        }
        if (!go && !stop){
            setBack();
        }

    }

    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
        //System.out.println("path size " + path.size());
        for (int i = 0; i < path.size(); i++) {
            if (i == 5) {
                g.setColor(Color.green);
            }
            g.fillRect(path.get(i).x * 32, path.get(i).y * 32, 20, 20);
        }
        g.setColor(Color.PINK);
    }

}
