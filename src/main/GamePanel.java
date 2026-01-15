package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {


    // SCREEN SETTINGS
    final int originalTileSize = 16;        //16x16 Tiles
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;   // 48x48 Tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;    // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;    // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

     TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this,this.keyH);
    public SuperObject[]  obj = new SuperObject[10];
    public AssetSetter assetSetter = new AssetSetter(this);






    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void setupGame(){
        assetSetter.setObject();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS; // 0.016666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCounter = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                // 1 UPDATE : update information such as character position
                update();

                //2 DRAW : draw the screen with the new update information
                repaint();
                delta--;
                drawCounter++;

            }
//            // Display FPS
//            if (timer >= 1000000000) {
//                System.out.println("FPS :" +drawCounter);
//                drawCounter = 0;
//                timer = 0;
//            }





        }

    }

    public void update(){
        player.update();


    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        long drawStart =0;


        //DEBUG
        if (keyH.debugKeyPressed){
            drawStart = System.nanoTime();
        }


        //TILE
        tileM.draw(g2d);

        // OBJECT
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(g2d, this);
            }
        }

        //PLAYER
        player.draw(g2d);

        //DEBUG
        if (keyH.debugKeyPressed){
            long drawEnd = System.nanoTime();
            long passedTime = drawEnd - drawStart;
            g2d.setColor(Color.white);
            g2d.drawString("Draw Time: " + passedTime , 10, 400);
            System.out.println("Draw Time: " + passedTime );

        }



        g2d.dispose();
    }

}
