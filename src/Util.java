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
    public static double getAngle(Point p1, Point p2) {
        //p1 center walkman
        //p2 center block
        double angle = Math.toDegrees(Math.atan2(p1.x - p2.x, -p1.y + p2.y));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil( -angle / 360 ) * 360;

        return angle;
    }
}
