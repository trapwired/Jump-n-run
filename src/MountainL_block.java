import java.awt.image.BufferedImage;

public class MountainL_block extends Building_block {
    public MountainL_block(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p, 75);
        this.block_type = Block.MOUNTAIN_L;
    }

    boolean[][] generateCollisionMap() {
        boolean[][] cM = new boolean[100][100];
        for(int i = 0; i < 100; i++){
            for(int j = 0; j < 100; j++){
                if(i <= j){
                    cM[i][j] = true;
                }
            }
        }
    return cM;
    }

}
