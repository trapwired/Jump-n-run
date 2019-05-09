import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Vector;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;

    JFrame frame;

    long delta = 0;     //Zeit des letzten Durchlauufs der GameLoop
    long last = 0;      //Speicherung der letzten Systemzeit
    long fps = 0;       //Berechnung FPS


    WalkMan walkMan;
    Vector<Sprite> actors;
    Vector<Sprite> painter;

    //moving params, set when key pressed
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean started;
    int speed = 50;

    public  GamePanel(int w, int h){
        this.setPreferredSize(new Dimension(w,h));
        this.setBackground(Color.orange);
        frame = new JFrame("GameFrame");
        frame.setLocation(100,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);

        //Thread stuff
        Thread th = new Thread(this);
        th.start();
    }

    //do one time initializations (load images, ...)
    private void doInitializations() {
        last = System.nanoTime();

        BufferedImage[] walkManAr = loadPics("pics/WalkMan.png", 9);
        walkManAr = Util.revertArray(walkManAr);

        actors = new Vector<Sprite>();
        painter = new Vector<Sprite>();
        walkMan = new WalkMan(walkManAr, 400, 300, 100, this);
        actors.add(walkMan);

        started = true;
    }


    @Override
    public void run() {
        while(frame.isVisible()){

            computeDelta();

            if(isStarted()){
                checkKeys();        //abfrage tastatureingaben
                doLogic();          //ausfürung von logik ops
                moveObjects();      //objekte bewegen
                cloneVectors();     //kopieren von actors zu painter
            }
            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }

    @SuppressWarnings("unchecked")
    private void cloneVectors() {
        painter = (Vector<Sprite>) actors.clone();
    }

    private void moveObjects() {
        for(ListIterator<Sprite> it = actors.listIterator(); it.hasNext();){
            Sprite r = it.next();
            r.move(delta);
        }
    }

    private void doLogic() {
        for(ListIterator<Sprite> it = actors.listIterator(); it.hasNext();){
            Sprite r = it.next();
            r.doLogic(delta);
        }
    }

    private void checkKeys() {
//        if(up) {
//            walkMan.setVerticalSpeed(-speed);
//        }
//        if(down) {
//            walkMan.setVerticalSpeed(speed);
//        }
        if(right) {
            walkMan.setHorizontalSpeed(speed);
        }
        if(left) {
            walkMan.setHorizontalSpeed(-speed);
        }
        if(!up&&!down){
            walkMan.setVerticalSpeed(0);
        }
        if(!left&&!right){
            walkMan.setHorizontalSpeed(0);
            walkMan.changeDirection(Direction.STILL);
        }
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
        g.drawString("FPS: " + Long.toString(fps), 20, 10);


        if(!started){
            return;
        }

        for(ListIterator<Sprite> it = painter.listIterator(); it.hasNext();){
            Sprite r = it.next();
            r.drawObjects(g);
        }
    }

    private BufferedImage[] loadPics(String path, int pics) {
        BufferedImage[] anim = new BufferedImage[pics];
        BufferedImage source = null;

        //URL pic_url = new URL(getClass().getClassLoader().getResource(path);
        //new URL(getCodeBase(), "examples/strawberry.jpg");
        File file = new File(path);

        try {
            source = ImageIO.read(file);
        } catch (IOException e) {}

        for(int x = 0; x < pics; x++){
            anim[x] = source.getSubimage(x*source.getWidth()/pics, 0, source.getWidth()/pics, source.getHeight());

        }
        return anim;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            up = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            down = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            left = true;
            walkMan.changeDirection(Direction.LEFT);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            right = true;
            walkMan.changeDirection(Direction.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            up = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            down = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            left = false;
            walkMan.changeDirection(Direction.STILL);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            right = false;
            walkMan.changeDirection(Direction.STILL);
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!isStarted()){
                doInitializations();
                setStarted(true);
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if(isStarted()){
                setStarted(false);
            } else {
                frame.dispose();
            }
        }
    }

    public boolean isStarted(){
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
