import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class WalkMan extends Sprite {

    private static final long serialVersionUID = 1L;
    long gravity = 10;
    boolean onfloor;

    public WalkMan(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 80);
    }

    @Override
    public void doLogic(long delta){
        super.doLogic(delta);

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
        Rectangle2D.Double smallWalkMan = new Rectangle2D.Double(getX()+15, getY(), 70, 100);
        if(smallWalkMan.intersects(r)){
            Direction richtung = Util.WalkManBlockRichtung(this, r);
            switch (richtung){
                case RIGHT:
                    setHorizontalSpeed(0);
                    setX(r.getX()+100);
                    break;
                case DOWN:
                    setVerticalSpeed(0);
                    setY(r.getY() + 100);
                    break;
                case LEFT:
                    setHorizontalSpeed(0);
                    setX(r.getX()-100);
                    break;
                case UP:
                    setVerticalSpeed(0);
                    setY(r.getY() - 100);
                    onfloor = true;
                    break;

            }
        }
    }

    public void move(long delta) {
        this.setVerticalSpeed(dy +gravity);
        super.move(delta);
    }
}
