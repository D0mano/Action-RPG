package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    Random random = new Random();

    public boolean debugMode = false;


    // SCREEN SETTINGS
    public final int originalTileSize = 16;        //16x16 Tiles
    public final int scale = 4;

    public final int tileSize = originalTileSize * scale;   // 48x48 Tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;    // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;    // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 79;
    public final int maxWorldRow = 49;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;


    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);

    public Sound music = new Sound();
    public Sound soundEffects = new Sound();

    // ENTITY AND PLAYER
    public Player player = new Player(this,this.keyH);
    public SuperObject[]  obj = new SuperObject[10];
    public ArrayList<Entity> monster = new ArrayList<>();
    public ArrayList<Entity> entitiesList = new ArrayList<Entity>();

    public UI ui = new UI(this,player);




    // GAMESATE
    public int gameState;
    public int previousState;
    public final int titleState = 0;
    public final int optionState = 1;
    public final int playState = 2;
    public final int pauseState = 3;
    public final int dialogueState = 5;
    public final int audioSettingstate = 6;
    public final int graphicsSettingstate = 7;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void setupGame(){
        assetSetter.setObject();
        assetSetter.setMonster();
        gameState = titleState;
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        playMusic(18);

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
        if (gameState == playState){


            player.update();

            for (Entity entity : monster) {
                if (entity != null) {
                    if (entity.alive && !entity.dying){
                        entity.update();
                    }
                }
            }

            monster.removeIf(e -> !e.alive);


        }
        if (gameState == pauseState){
            // NOTHING
        }

    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        long drawStart =0;


        //DEBUG
        if (debugMode){
            drawStart = System.nanoTime();
        }

        if (gameState == titleState){


        }else{
            //TILE 1ST LAYER
            tileM.draw(g2d,1);

            // OBJECT
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2d, this);
                }
            }
            entitiesList.add(player);
            for (Entity e : monster) {
                if (e != null) {
                    entitiesList.add(e);
                }
            }

            Collections.sort(entitiesList,new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY,e2.worldY);
                }
            });
            for (Entity e : entitiesList){
                e.draw(g2d);
            }

            entitiesList.clear();

            //TILE 2ND LAYER
            tileM.draw(g2d,2);





        }

        ui.draw(g2d);

        if(gameState == playState ){
            //DEBUG
            if (debugMode){
                long drawEnd = System.nanoTime();
                long passedTime = drawEnd - drawStart;
                g2d.setColor(Color.white);
                g2d.drawString("Draw Time: " + passedTime , 10, 400);
                g2d.drawString("Coordinate :" + player.worldX+","+player.worldY , 10, 200);
                player.showHitbox(g2d);

            }
        }








        g2d.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSoundEffect(int i){
        soundEffects.setFile(i);
        soundEffects.play();
    }

    public void updateMusicVolume(float volume){
        music.updateVolume(volume);
    }
    public void updateSoundVolume(float volume){
        soundEffects.updateVolume(volume);
    }

}
