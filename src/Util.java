import java.awt.*;
import java.awt.image.BufferedImage;
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
}

