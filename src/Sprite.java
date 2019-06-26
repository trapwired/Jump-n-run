import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Sprite extends Rectangle2D.Double implements Drawable, Movable {

    private static final long serialVersionUID = 1L;

    long delay;
    long animation = 0;
    GamePanel parent;           //Referenz auf Game Panel
    BufferedImage[] pics;       //Array zum speichern der Animation in Einzelbildern
    BufferedImage[] picsRevert;
    int currentpic = 0;         //zähler für aktuell anzuzeigeneds bild
    Direction movingDirection;

    protected double dx;        //wie schnell soll bewegt werden horizontal?
    protected double dy;

    int zLocation;              //location in z direction; -1: background, 100: top object

    public Sprite(BufferedImage[] i, double x, double y, long delay, GamePanel p, int zLoc) {
        pics = i;
        this.x = x;
        this.y = y;
        this.delay = delay;
        this.width = pics[0].getWidth();
        this.height = pics[0].getHeight();
        parent = p;
        picsRevert = Util.revertArray(i);
        movingDirection = Direction.STILL;
        zLocation = zLoc;
    }

    public void changeDirection(Direction d){
        movingDirection = d;

    }


    public double getHorizontalSpeed() {
        return dx;
    }

    public double getVerticalSpeed() {
        return dy;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setHorizontalSpeed(double dx) {
        this.dx = dx;
    }

    public void setVerticalSpeed(double dy) {
        this.dy = dy;
    }



    public void drawObjects(Graphics g) {
        g.drawImage(pics[currentpic], (int) x, (int) y, null);
    }


    public void doLogic(long delta) {

        switch (movingDirection) {
            case STILL:
                break;
            case LEFT:
                movingAnim(Direction.LEFT, delta);
                break;
            case RIGHT:
                movingAnim(Direction.RIGHT, delta);
                break;
            case FREELY:
                movingAnim(Direction.RIGHT, delta);
                break;
        }


    }

    private void movingAnim(Direction d, long delta){
        animation += (delta/1000000);
        if (animation > delay) {
            //wenn animation grösser als voreingestellter animationswert:
            animation = 0;
            computeAnimation(d);
        }
    }

    public void computeAnimation(Direction d) {
        if(!d.equals(Direction.JUMP)){
            if(d.equals(Direction.LEFT)){
                currentpic--;

                if (currentpic < 0) {
                    currentpic = pics.length - 1;
                }
            } else if (d.equals(Direction.RIGHT)){
                currentpic++;

                if (currentpic >= pics.length) {
                    currentpic = 0;
                }
            }
        }
    }


    public void move(long delta) {

        if(dx != 0) {
            x += dx*(delta/1e9);
        }

        if(dy != 0) {
            y += dy*(delta/1e9);
        }
    }
}
