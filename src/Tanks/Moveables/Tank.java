package Tanks.Moveables;



import Tanks.Moveables.Bullets.*;
import Tanks.GameObject;
import Tanks.MapElements.Walls.Wall;
import Tanks.MapElements.PowerUPs.PowerUps;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject {

    protected int vx;
    protected int vy;
    protected int lastx;
    protected int lasty;
    protected int lastDirection;
    protected int angle;
    protected int shots= 20;
    protected ArrayList<Bullet> bullets = new ArrayList<>();
    protected ArrayList<BufferedImage> bulletImgs;
    protected int HP = 100;
    protected int lives = 3;
    protected boolean KO = false;
    protected int R = 5;
    protected final int ROTATIONSPEED = 6;
    protected BufferedImage damaged, poisoned, KOimg;
    protected BufferedImage poisonBulletImg;
    protected int counter;
    protected int counter2;
    protected boolean stop = false;


    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean damageTaken = false;
    private boolean isPoisoned = false;
    private boolean lostLife = false;

    protected BulletType BulletTypeName = BulletType.valueOf("Normal");
    enum BulletType {
        Normal, Triple, Bounce, Poison;
    }


    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage img, ArrayList<BufferedImage> bulletImgs) {
        super(img, x, y);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.bulletImgs = bulletImgs;
        try {
            damaged = ImageIO.read(getClass().getResource("/resources/pigdamage.png"));
            poisonBulletImg = ImageIO.read(getClass().getResource("/resources/poison_mushroom.png"));
            poisoned = ImageIO.read(getClass().getResource("/resources/pigpoison.png"));
            KOimg = ImageIO.read(getClass().getResource("/resources/KOpig.png"));
                   }
        catch (Exception e){
            System.out.println("Failed to load image");
        }
    }


    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void ToggleShoot () {this.ShootPressed = true; }


    public void update() {
        counter++;
        lastx = x;
        lasty = y;
        if (this.UpPressed) {
            this.moveForwards();
            lastDirection = 1;
        }
        if (this.DownPressed) {
            this.moveBackwards();
            lastDirection = -1;
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.ShootPressed && bullets.size() < shots) {
            shoot();
        }

        if (isPoisoned && (counter%10 == 0)){
            takeDamage(1);
            damageTaken = false;
        }
        for (int i = 0; i< bullets.size(); i++) {
            bullets.get(i).update();
        }

    }
    public void shoot (){
        int xoffset = x + img.getWidth()/3 + (int) Math.round( (img.getWidth()/2 * Math.cos(Math.toRadians(angle))));
        int yoffset = y + img.getHeight()/3 + (int) Math.round( (img.getHeight()/2 * Math.sin(Math.toRadians(angle))));
        switch (BulletTypeName){
            case Normal:
                Bullet normalshot = new NormalBullet(bulletImgs.get(0), xoffset, yoffset , angle);
                normalshot.shoot(bullets);

                break;
            case Bounce:
                Bullet bounceshot = new BounceBullet(bulletImgs.get(0), xoffset, yoffset , angle);
                bounceshot.shoot(bullets);
                break;

            case Triple:
                Bullet tripleshot = new TripleBullet(bulletImgs.get(0), xoffset, yoffset , angle);
                tripleshot.shoot(bullets);
                break;

            case Poison:
                Bullet poisonshot = new PoisonBullet(poisonBulletImg, xoffset, yoffset, angle);
                poisonshot.shoot(bullets);
                break;

        }
        ShootPressed = false;

    }


    public void setBulletType (String bulletString){
        BulletTypeName = BulletType.valueOf(bulletString);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    protected void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
    }

    protected void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
    }




    public void checkBorder(int width, int height) {
        if (x < 0) {
            x = 0;
        }
        if (x >= width - img.getWidth()) {
            x = width- img.getWidth();
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= height - img.getHeight()) {
            y = height- img.getHeight();
        }
        for (int i = 0; i< bullets.size(); i++){
            //bullets.get(i).update();
            if (bullets.get(i).checkBorder(width, height)){
                bullets.remove(i);
            }
        }
    }


    public void collide (GameObject solid){
        Rectangle lastIntersect;
        Rectangle intersect = solid.getRect().intersection(this.getRect());
        while (solid.getRect().intersects(this.getRect())) {

            //System.out.println(intersect);

            if (intersect.width > intersect.height && lastDirection ==1){
                y -= vy;
            }
            else if (intersect.width < intersect.height && lastDirection ==1){
                x -= vx;
            }
            else if (intersect.width == intersect.height && lastDirection ==1){
                x -= vx;
                y-= vy;
            }
            else if (intersect.width > intersect.height && lastDirection ==-1){
                y += vy;
            }
            else if (intersect.width < intersect.height && lastDirection ==-1){
                x += vx;
            }
            else if (intersect.width == intersect.height && lastDirection ==-1){
                y +=vy;
                x += vx;
            }
            lastIntersect = intersect;
            intersect = solid.getRect().intersection(this.getRect());

            //this is a failsafe so tank never gets stuck.
            if  (lastIntersect.equals(intersect)){
                System.out.println("stuck!!!");
                //angle += ROTATIONSPEED;
                setBack();
                stop = true;
                Timer timer = new Timer();
                TimerTask wait = new TimerTask() {
                    @Override
                    public void run() {
                        stop = false;
                    }
                };
                timer.schedule(wait, 400);
            }
        }

    }



    public void allCollisions (ArrayList<GameObject> solids){
        Rectangle lastIntersection;
        Rectangle currIntersection;
        int currIntersectionPerim, lastIntersectionPerim;

        for (int i = 1; i < solids.size(); i++){
            currIntersection = getRect().intersection(solids.get(i).getRect());
            lastIntersection = getRect().intersection(solids.get(i-1).getRect());
            currIntersectionPerim = currIntersection.height + currIntersection.width;
            lastIntersectionPerim = lastIntersection.height + lastIntersection.width;


            if (getRect().intersects(solids.get(i).getRect())
                    && currIntersectionPerim > lastIntersectionPerim
                        && (currIntersection.x == lastIntersection.x || currIntersection.y == lastIntersection.y)
                            && solids.get(i) instanceof Wall)
            {
                solids.remove(i-1);
            }
        }

        for (int i = 0; i < solids.size(); i++){
            if (solids.get(i) instanceof Wall){
            this.collide(solids.get(i));
            }
            else {
                aquire(solids.get(i));
            }
        }
    }

    public void aquire (GameObject aquiredObject){
        PowerUps PU = (PowerUps) aquiredObject;
        PU.modify(this);

    }

    public void setBack (){
        x= lastx;
        y= lasty;
//        y +=vy;
//        x += vx;
    }

    public boolean tankCollisionBool (Tank otherTank){
        if (otherTank.getRect().intersects(getRect())){
            return true;
        }
        else {return false;
        }
    }
    public void tankCollision (Tank otherTank){
        if (otherTank.getRect().intersects(getRect())){
           otherTank.setBack();
           setBack();
        }
        for (int i = 0; i < otherTank.getBullets().size(); i++){
            if (this.getRect().intersects(otherTank.getBullets().get(i).getRect())){
                ;
                otherTank.getBullets().get(i).damage(this);
                otherTank.getBullets().remove(i);
                System.out.println("damage taken!! " + HP);
            }
        }
    }
    public void takeDamage(int damage){

        HP -= damage;
        if (HP <= 0){
            lives --;
            lostLife = true;
            isPoisoned = false;
            HP = 100;
        }
        if (lives == 0){
            KO = true;
        }
        damageTaken = true;
    }


    public boolean isKO () {
        return KO;
    }

    public int getx(){
        return x;
    }
    public int gety(){
        return y;
    }
    public int getHP(){
        return HP;
    }
    public int getlives(){
        return lives;
    }
    public boolean isLostLife(){return lostLife; }
    public void setLostLife (boolean life) {lostLife = life;}
    public void setPoison(boolean bool){
        isPoisoned = bool;
    }
    public ArrayList<Bullet> getBullets(){
        return bullets;
    }
    public void reset (int x, int y, int angle){
        this.setBulletType("Normal");
        bullets.clear();
        isPoisoned = false;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void extraLife (){
        HP=100;
        if (lives < 3){
            lives++;
        }
    }



    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        if (isPoisoned){
            g2d.drawImage(this.poisoned, rotation, null);
        }
        if (damageTaken){
            g2d.drawImage(this.damaged, rotation, null);
            if (counter2 == 5){
                damageTaken = false;
                counter2 = 0;
            }
            counter2++;
        }
        if (lostLife){
            g2d.drawImage(KOimg, rotation, null);

        }

        for (int i = 0; i< bullets.size(); i++){
            bullets.get(i).drawImage(g2d);
        }
    }
    public void chase (Tank otherTank){

    }



}
