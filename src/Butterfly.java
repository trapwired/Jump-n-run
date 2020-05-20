import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Butterfly extends Sprite {

    int verticalspeed = 70;
    Rectangle2D.Double target;
    boolean found = false;
    private static final long serialVersionUID = 1L;

    public Butterfly(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, (int) (Math.random()*100) + 10);
        this.movingDirection = Direction.FREELY;

        if (getY()<parent.getHeight()/2){
            setVerticalSpeed(verticalspeed);
        } else {
            setVerticalSpeed(-verticalspeed);
        }
    }

    @Override
    boolean[][] generateCollisionMap() {
        // TODO generate collisionMap
        boolean[][] cM = new boolean[100][100];
        return cM;
    }


    @Override
    public void doLogic(long delta){
        super.doLogic(delta);

        if(getHorizontalSpeed()>0){
            target = new Rectangle2D.Double(getX()+getWidth(), getY(), parent.getWidth()-getX(), getHeight());
        } else {
            target = new Rectangle2D.Double(0,getY(), getX(), getHeight());
        }
        if(!found && parent.walkMan.intersects(target)){
            setVerticalSpeed(0);
            found = true;
        }

        if(found){
            if(getY() < parent.walkMan.getY()){
                setVerticalSpeed(40);
            }
            if(getY() > parent.walkMan.getY() + parent.walkMan.getHeight()){
                setVerticalSpeed(-40);
            }
        }

    }

    public void setHorizontalSpeed(double d){
        super.setHorizontalSpeed(d);
    }
}
