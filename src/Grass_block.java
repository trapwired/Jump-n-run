import java.awt.image.BufferedImage;

public class Grass_block extends Building_block {
    public Grass_block(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 75);
        this.block_type = Block.GRASS;
    }
}
