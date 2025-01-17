import javax.swing.*;
import java.awt.*;
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
    boolean[][] generateCollisionMap() {
        boolean[][] cM = new boolean[100][100];
        for(int i = 0; i < 100; i++){
            for(int j = 15; j < 85; j++){
                cM[i][j] = true;
            }
        }
        return cM;
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
           // setVerticalSpeed(0);
            //setY(0);
        }

        if(getY() + getHeight()>parent.getHeight()){
            // setY(parent.getHeight()-getHeight());
            // setVerticalSpeed(0);
            setY(-getHeight());
        }
    }

    public void doesCollide(Sprite r, GamePanel gp) {
        // rough collision detection (treat everything as a square block)
        if (this.intersects(r)) {
            // fine-tuned collision detection
            // get angle to know approximately where the collision takes place, 0 degrees means walkman is directly above
            int direction = Util.angleBetweenSprites(this, r);

            System.out.println(direction);

            if (r instanceof Building_block) {
                Block intersectBlock = ((Building_block) r).block_type;

                // Lava, und Bild ändern
                if(intersectBlock == Block.LAVA){
                    // Lava = Game Over
                    gp.frame.dispose();
                } else  if(intersectBlock == Block.MOUNTAIN){
                    this.pics = gp.loadPics("pics/walkmanBoardF.png",1);
                    this.collisionMap = Util.importCollisionMapPNG("pics/walkmanBoardF.png");
                } else  if(intersectBlock == Block.MOUNTAIN_L){
                this.pics = gp.loadPics("pics/walkmanBoardL2.png",1);
                this.collisionMap = Util.importCollisionMapPNG("pics/walkmanBoardL2.png");
                } else if (intersectBlock == Block.GRASS){
                    this.pics = gp.loadPics("pics/walkManS.png", 9);
                    this.collisionMap = generateCollisionMap();
                }



            // Collision Detection
                if (intersectBlock == Block.MOUNTAIN_L) {
                    Direction richtung = Util.WalkManBlockRichtung(this, r);
                    if (richtung == Direction.DOWN) {
                        setVerticalSpeed(0);
                        setY(r.getY() + 100);
                    }else if (richtung == Direction.LEFT){
                        setHorizontalSpeed(0);
                        setY(r.getX() - 100);
                    } else if(richtung == Direction.UP || richtung == Direction.RIGHT ) {

                    }


                } else {
                    // normaler Fall: quadratischer block
                    Direction richtung = Util.WalkManBlockRichtung(this, r);
                    switch (richtung) {
                        case RIGHT:
                            setHorizontalSpeed(0);
                            setX(r.getX() + 100);
                            break;
                        case DOWN:
                            setVerticalSpeed(0);
                            setY(r.getY() + 100);
                            break;
                        case LEFT:
                            setHorizontalSpeed(0);
                            setX(r.getX() - 100);
                            break;
                        case UP:
                            setVerticalSpeed(0);
                            setY(r.getY() - 100);
                            onfloor = true;
                            break;
                    }
                }



            }
        }
    }

    public void move(long delta) {
        this.setVerticalSpeed(dy +gravity);
        super.move(delta);
    }
}
