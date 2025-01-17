import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Vector;

public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener {

    private static final long serialVersionUID = 1L;

    JFrame frame;

    long delta = 0;     //Zeit des letzten Durchlauufs der GameLoop
    long last = 0;      //Speicherung der letzten Systemzeit
    long fps = 0;       //Berechnung FPS


    WalkMan walkMan;
    Vector<Sprite> actors;
    Vector<Sprite> painter;
    Vector<Sprite> possible_collisions;

    //moving params, set when key pressed
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean jump;

    boolean started;
    int speed = 200;

    //set to size of actors when last sorted
    private int actors_size = 0;

    //2D array for building blocks
    Building_block[][] block_grid;
    //2d array für Kolisions Map (true = Block vorhanden, false = Luft)
    boolean[][] collision_map;

    Timer timer;
    BufferedImage[] butterfly;

    public  GamePanel(int w, int h){
        int w_b = w / 100;
        int h_b = h / 100;
        w = w_b * 100;
        h = h_b * 100;
        this.setPreferredSize(new Dimension(w,h));
        this.setBackground(Color.getHSBColor(35, 187, 249));
        frame = new JFrame("GameFrame");
        frame.setLocation(100,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
        //Init Collision Map
        collision_map = new boolean[h][w];
        /* new boolean[2][5]
        1 2 3 4 5
        6 7 8 9 10
         */

        //init background grid
        block_grid = new Building_block[h_b][w_b];
        //Thread stuff
        Thread th = new Thread(this);
        th.start();
    }

    private void draw_level() {
        //init the background blocks / draw the level
        int height = block_grid.length;
        int width  = block_grid[0].length;

        for(int i = 0; i <= 12; i++){
            //make floor snowy
            if(i != 7){
                draw_block_at(i,5,Block.MOUNTAIN);
            } else {
                draw_block_at(i,5,Block.LAVA);
            }

        }
        //create hill
        draw_block_at(0,2,Block.MOUNTAIN);
        draw_block_at(0,3,Block.MOUNTAIN);
        draw_block_at(0,4,Block.MOUNTAIN);
        draw_block_at(1,3,Block.MOUNTAIN);
        draw_block_at(1,4,Block.MOUNTAIN);
        draw_block_at(2,4,Block.MOUNTAIN);
        draw_block_at(3,4,Block.MOUNTAIN);
        //Add Diagonal Mountain Pieces
        draw_block_at(1, 2, Block.MOUNTAIN_L);
        draw_block_at(2, 3, Block.MOUNTAIN_L);
        draw_block_at(4, 4, Block.MOUNTAIN_L);
        //Add End Piece
        draw_block_at(12, 4, Block.GRASS);
        //draw_block_at(12, 4, Block.MOUNTAIN_R);
        //draw_block_at(13, 3, Block.MOUNTAIN_R);
        //draw_block_at(13, 4, Block.MOUNTAIN);
    }

    private void draw_block_at(int x, int y, Block type){
        Building_block bb;
        BufferedImage[] bi;
        switch (type){
            case GRASS:
                bi = loadPics("pics/grass_block.png", 1);
                bb = new Grass_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case WOOD:
                bi = loadPics("pics/wood_block.png", 1);
                bb = new Wood_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case METALL:
                bi = loadPics("pics/metal_block.png", 1);
                bb = new Metal_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case GLASS:
                bi = loadPics("pics/glass_block.png", 1);
                bb = new Metal_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case MOUNTAIN:
                bi = loadPics("pics/mountain2_block.png", 1);
                bb = new Mountain_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case MOUNTAIN_L:
                bi = loadPics("pics/mountainL_block.png", 1);
                bb = new MountainL_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case MOUNTAIN_R:
                bi = loadPics("pics/mountainR_block.png", 1);
                bb = new MountainR_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
            case LAVA:
                bi = loadPics("pics/Lava_block2.png", 1);
                bb = new Lava_block(bi, x * 100, y * 100, 1000, this);
                actors.add(bb);
                possible_collisions.add(bb);
                addBlocktoCollisionMap(bb);
                break;
        }
    }

    private void addBlocktoCollisionMap(Building_block bb){

        for(int i = 0; i < this.getHeight(); i++){
            for(int j = 0; j < this.getWidth(); j++){
                if((i >= bb.y && i < bb.y + 101) && (j >= bb.x && j < bb.x + 101)){
                    collision_map[i][j] = true;
                }
            }

        }
        if (bb.block_type == Block.MOUNTAIN_L) {
            // setze dreicke oben auf false
            for(int i = 0; i < 101; i++){
                for(int j = 0; j < 101; j++){
                    if(i > j){
                        collision_map[(int) bb.y + j][ (int) bb.x + i] = false;
                    }
                }
            }

        }

    }



    //do one time initializations (load images, ...)
    private void doInitializations() {
        last = System.nanoTime();

        BufferedImage[] walkManAr = loadPics("pics/WalkManS.png", 9);
        walkManAr = Util.revertArray(walkManAr);

        actors = new Vector<Sprite>();
        painter = new Vector<Sprite>();
        possible_collisions = new Vector<Sprite>();
        walkMan = new WalkMan(walkManAr, 100, 100, 80, this);
        actors.add(walkMan);

        // init_butterfly();
        draw_level();
        // createGrass();
        createClouds();

        Util.exportCollisionMaps();
         //Util.exportCollisionMap("bli", collision_map);

        timer = new Timer(3000, this);
        timer.start();

        started = false;
    }

    private void init_butterfly() {
        BufferedImage[] bf = loadPics("pics/butterfly.png", 16);
        Butterfly buterfly = new Butterfly(bf, 200, 200, 80, this);
        actors.add(buterfly);
    }

    private void createGrass(){
        BufferedImage[] bi = loadPics("pics/grasscomplete.png", 1);
        Grass_block grassBlock = new Grass_block(bi, 0, 490, 1000, this);
        Grass_block grassBlock2 = new Grass_block(bi, 527, 490, 1000, this);
        actors.add(grassBlock);
        actors.add(grassBlock2);


    }

    private void createClouds(){
        BufferedImage[] bi = loadPics("pics/cloud.png", 1);

        for (int y = 10; y < (getHeight()/2); y += ((int)(Math.random()*150))){
            int x = (int) (Math.random()*getWidth());
            Cloud cloud = new Cloud(bi, x, y, 1000, this);
            actors.add(cloud);
        }
    }


    @Override
    public void run() {
        // game loop
        while(frame.isVisible()){

            computeDelta();         // Zeit seit letztem Update

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
        if (actors_size < actors.size()){
            actors.sort(Comparator.comparingInt(s -> s.zLocation));
            actors_size = actors.size();
        }
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
        //Schauen ob Walkman mit Blcoks Kolidiert
        for(ListIterator<Sprite> it = possible_collisions.listIterator(); it.hasNext();){
            Sprite r = it.next();
            walkMan.doesCollide(r, this);
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
        if(jump && walkMan.onfloor){
            walkMan.setVerticalSpeed(-walkMan.gravity*45);
            double temp = walkMan.getY();
            walkMan.setY(temp-1);
            walkMan.onfloor = false;
            jump = false;
        }
        //if(!up&&!down){
        //    walkMan.setVerticalSpeed(0);
        //}
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

    public BufferedImage[] loadPics(String path, int pics) {
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
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            jump = true;
            // walkMan.changeDirection(Direction.JUMP);
        }
        if(e.getKeyCode() == KeyEvent.VK_G){
            walkMan.gravity *= 0.8;

        }
        if(e.getKeyCode() == KeyEvent.VK_H){
            walkMan.gravity *= 1.2;
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
//        if(e.getKeyCode() == KeyEvent.VK_SPACE){
//            jump = false;
//        }

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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
