import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    JFrame frame;

    long delta = 0;     //Zeit des letzten Durchlauufs der GameLoop
    long last = 0;      //Speicherung der letzten Systemzeit
    long fps = 0;       //Berechnung FPS


    public  GamePanel(int w, int h){
        this.setPreferredSize(new Dimension(w,h));
        frame = new JFrame("GameFrame");
        frame.setLocation(100,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        doInitializations();

        //Thread stuff
        Thread th = new Thread(this);
        th.start();
    }

    //do one time initializations (load images, ...)
    private void doInitializations() {
        last = System.nanoTime();

    }


    @Override
    public void run() {
        while(frame.isVisible()){

            computeDelta();

            checkKeys();        //abfrage tastatureingaben
            doLogic();          //ausfürung von logik ops
            moveObjects();      //objekte bewegen

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }

    private void moveObjects() {

    }

    private void doLogic() {

    }

    private void checkKeys() {

    }

    private void computeDelta() {
        delta = System.nanoTime() - last;
        last = System.nanoTime();
        fps = ((long) 1e9) / delta;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);
        g.drawString("FPS: " + Long.toString(fps), 400, 100);
    }
}
