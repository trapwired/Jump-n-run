import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Cloud extends Sprite {

    private static final long serialVersionUID = 1L;
    final int SPEED = (int) (Math.random()*50) + 10;

    public Cloud(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 0);

        if((int) (Math.random()*2)<1){
            setHorizontalSpeed(-SPEED);
            changeDirection(Direction.LEFT);
        } else {
            setHorizontalSpeed(SPEED);
            changeDirection(Direction.RIGHT);
        }
    }

    @Override
    boolean[][] generateCollisionMap() {
        // TODO generate CollisionMap
        boolean[][] cM = new boolean[100][100];
        return cM;
    }

    @Override
    public void doLogic(long delta){
        super.doLogic(delta);

        if (getHorizontalSpeed()>0 && getX()>parent.getWidth()){
            x = -getWidth();
        }

        if(getHorizontalSpeed()<0 && (getX() + getWidth()<0)){
            x = parent.getWidth()+getWidth();
        }
    }
}
