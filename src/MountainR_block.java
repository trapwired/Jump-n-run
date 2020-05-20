import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MountainR_block extends Building_block {
    public MountainR_block(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 75);
        this.block_type = Block.MOUNTAIN_R;
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
