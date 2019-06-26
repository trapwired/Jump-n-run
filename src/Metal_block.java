import java.awt.image.BufferedImage;

public class Metal_block extends Building_block {
    public Metal_block(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 75);
        this.block_type = Block.METALL;
    }
}
