import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Vector;

public class Util {

    public static BufferedImage[] revertArray(BufferedImage[] ar){
        BufferedImage[] result = new BufferedImage[ar.length];
        for(int i = 0; i < ar.length; i++){
            result[ar.length - i -1] = ar[i];
        }
        return result;
    }

    public static Vector<Sprite> add_to_actor(Vector<Sprite> actors, Sprite sprite) {
        if (actors.isEmpty()){
            actors.add(sprite);
            return actors;
        } else {
            Vector<Sprite> res =  new Vector<>();
            boolean added = false;
            for(ListIterator<Sprite> it = actors.listIterator(); it.hasNext();){
                Sprite r = it.next();
                if (r.zLocation >= sprite.zLocation){
                    res.add(sprite);
                    added = true;
                }
                res.add(r);
            }
            if (!added){
                res.add(sprite);
            }
            if (actors.size() != res.size() - 1){
                System.out.println("newly to insert: " + sprite);
                System.out.println("    " + actors);
                System.out.println("    " + res);
                check_zLoc(actors);
            }
            return res;
        }

    }

    private static void check_zLoc(Vector<Sprite> actors) {
        int last_zLoc = -1;
        for(ListIterator<Sprite> it = actors.listIterator(); it.hasNext();) {
            Sprite r = it.next();
            if(last_zLoc > r.zLocation){
                System.out.println("zLocation Error");
            }
            last_zLoc = r.zLocation;
        }

    }

    public static Direction WalkManBlockRichtung(WalkMan walkMan, Sprite r) {
        //Gebe Richtung aus: Wo ist Walkman in bezug auf Block
        Point centereWM = new Point((int) walkMan.getCenterX(), (int) walkMan.getCenterY());
        Point centerB = new Point((int) r.getCenterX(), (int) r.getCenterY());
        double winkel = Util.getAngle(centereWM, centerB);
        if(winkel >= 45 && winkel < 135) {
            return Direction.RIGHT;
        }
        if(winkel >= 135 && winkel < 225) {
            return Direction.DOWN;
        }
        if(winkel >= 225 && winkel < 315) {
            return Direction.LEFT;
        }
        if((winkel >= 315 && winkel <= 360)||(winkel >= 0 && winkel < 45)) {
            return Direction.UP;
        }
            return Direction.FREELY;
    }

    public static int angleBetweenSprites(Sprite s1, Sprite s2){
        // 0: s1 is directly above s2
        // 90: s1 is to the right of s2
        // 180: s1 is directly under s2
        // 270: s1 is to the left of s2
        Point center1 = new Point((int) s1.getCenterX(), (int) s1.getCenterY());
        Point center2 = new Point((int) s2.getCenterX(), (int) s2.getCenterY());
        double angle = Util.getAngle(center1, center2);
        return (int) angle;
    }

    public static double getAngle(Point p1, Point p2) {
        //p1 center walkman
        //p2 center block
        double angle = Math.toDegrees(Math.atan2(p1.x - p2.x, -p1.y + p2.y));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;
        return angle;
    }

    public static void exportCollisionMap(String fileName, boolean[][] collision_map){
        // for testing: neue Datei erstellen mit aktueller collision map
        BufferedImage bufferedImage = new BufferedImage(collision_map[0].length,collision_map.length,BufferedImage.TYPE_BYTE_BINARY);
        for(int i = 0; i < collision_map[0].length; i++){
            for(int j = 0; j < collision_map.length; j++){
                bufferedImage.setRGB(i,j,collision_map[j][i]?Color.BLACK.getRGB():Color.WHITE.getRGB());
            }
        }
        try {
            File outputfile = new File("cM/"+fileName +".png");
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

   public static boolean[][] importCollisionMapPNG(String path){
        BufferedImage img = null;
        boolean[][] res = null;
        try {
            img = ImageIO.read(new File(path));
            res = new boolean[img.getHeight()][img.getWidth()];
            int color = 0;
            for(int i = 0; i < img.getWidth(); i++){
                for(int j = 0; j < img.getHeight(); j++){
                    color = img.getRGB(i, j);
                    // Components will be in the range of 0..255:
                    int blue = color & 0xff;
                    int green = (color & 0xff00) >> 8;
                    int red = (color & 0xff0000) >> 16;
                    int alpha = (color & 0xff000000) >>> 24;
                    int rgbSUM = blue + green + red;
                    // if a pixel is either white or transparent, set CM to false (= no block)
                    res[j][i] = !(alpha == 0 || rgbSUM == 765);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //  e.printStackTrace();
        }


        return res;
   }

    public static void exportCollisionMaps() {
        Util.exportCollisionMap("walkManBoardFGen", Util.importCollisionMapPNG("pics/walkManBoardF.png"));
        Util.exportCollisionMap("butterflyGen", Util.importCollisionMapPNG("pics/butterfly.png"));
        Util.exportCollisionMap("CloudGen", Util.importCollisionMapPNG("pics/cloud.png"));
        Util.exportCollisionMap("mountainL_blockGen", Util.importCollisionMapPNG("pics/mountainL_block.png"));

    }
}

