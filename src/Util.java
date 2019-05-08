import java.awt.image.BufferedImage;

public class Util {

    public static BufferedImage[] revertArray(BufferedImage[] ar){
        BufferedImage[] result = new BufferedImage[ar.length];
        for(int i = 0; i < ar.length; i++){
            result[ar.length - i -1] = ar[i];
        }
        return result;
    }
}
