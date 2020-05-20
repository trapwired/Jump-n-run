import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Metal_block extends Building_block {
    public Metal_block(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 75);
        this.block_type = Block.METALL;
    }

    @Override
    boolean[][] generateCollisionMap() {
        boolean[][] cM = new boolean[100][100];
        boolean[] temp = new boolean[100];
        Arrays.fill(temp, true);
        Arrays.fill(cM, temp);
        return cM;
    }
}
