import java.awt.image.BufferedImage;

public class MountainL_block extends Building_block {
    public MountainL_block(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 75);
        this.block_type = Block.MOUNTAIN_L;
    }
}
