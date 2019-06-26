import java.awt.*;
import java.awt.image.BufferedImage;

public class WalkMan extends Sprite {

    private static final long serialVersionUID = 1L;
    long gravity = 15;
    boolean onFloor;

    public WalkMan(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 80);
    }

    @Override
    public void doLogic(long delta){
        super.doLogic(delta);
        if (!onFloor){
            movingDirection = Direction.JUMP;
        }
        if(getX()<0){
            setHorizontalSpeed(0);
            setX(0);
        }

        if(getX() + getWidth()>parent.getWidth()){
            setX(parent.getWidth()-getWidth());
            setHorizontalSpeed(0);
        }

        if(getY()<0){
            setVerticalSpeed(0);
            setY(0);
        }

        if(getY() + getHeight()>parent.getHeight()){
            setY(parent.getHeight()-getHeight());
            setVerticalSpeed(0);
        }
    }

    public void doesCollide(Sprite r) {
        if(this.intersects(r)){
            Direction dir = calculateRelativeDirection(r);
            switch (dir){
                case RIGHT:
                    setHorizontalSpeed(0);
                    setX(r.getX()+100);
                    break;
                case LEFT:
                    setHorizontalSpeed(0);
                    setX(r.getX()-100);
                    break;
                case UP:
                    setVerticalSpeed(0);
                    setY(r.getY()-100);
                    onFloor = true;
                    break;
                case DOWN:
                    setVerticalSpeed(0);
                    setY(r.getY()+100);
                    break;
            }
        }
    }

    private Direction calculateRelativeDirection(Sprite r) {
        //get direction of walkman relative to block
        Point centereWM = new Point((int) getCenterX(), (int) getCenterY());
        Point centerB = new Point((int) r.getCenterX(), (int) r.getCenterY());
        double angle = Util.getAngle(centereWM, centerB);
        //we assume to find the man only on the outside
        if(angle < 45){
            return Direction.UP;
        } else if (angle < 135){
            return Direction.RIGHT;
        } else if (angle < 225){
            return Direction.DOWN;
        } else if (angle < 315){
            return Direction.LEFT;
        } else {
            return Direction.UP;
        }
    }



    public void move(long delta) {
        dy += gravity;
        super.move(delta);
    }
}
