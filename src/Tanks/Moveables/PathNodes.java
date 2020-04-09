package Tanks.Moveables;

import java.awt.*;

public class PathNodes {

    int pathLength;
    Point curr;
    Point last;

    public PathNodes (Point last, Point curr, int pathLength){
        this.last = last;
        this.curr = curr;
        this.pathLength = pathLength;
    }

    public int getPathLength() {
        return pathLength;
    }

    public Point getCurr() {
        return curr;
    }

    public Point getLast() {
        return last;
    }
}
