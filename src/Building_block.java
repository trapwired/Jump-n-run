import java.awt.image.BufferedImage;

public abstract class Building_block extends Sprite {

    Block block_type;

    public Building_block(BufferedImage[] i, double x, double y, long delay, GamePanel p, int zLoc) {
        super(i, x, y, delay, p, 75);
    }
}
