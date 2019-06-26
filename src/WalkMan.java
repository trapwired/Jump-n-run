import java.awt.image.BufferedImage;

public class WalkMan extends Sprite {

    private static final long serialVersionUID = 1L;
    long gravity = 10;

    public WalkMan(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 50);
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
        boolean horizontal = getY() == r.getY();
        boolean vertikal = getX() + 100 > r.getX();
        if(horizontal && vertikal){
            setHorizontalSpeed(0);
        }
    }

    public void move(long delta) {
        dy += gravity;
        super.move(delta);
    }
}
