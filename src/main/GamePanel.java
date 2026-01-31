package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.Tile;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {

    Random random = new Random();

    public boolean debugMode = false;
    public String windowMode = "Windowed";
    public String fullScreenMode = "FullScreen";


    // SCREEN SETTINGS
    public final int originalTileSize = 16;        //16x16 Tiles
    public final int scale = 2;
    public String displayMode = windowMode ;

    public final int tileSize = originalTileSize * scale;   // 48x48 Tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;    // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;    // 576 pixels

    public int screenWidth2 = screenWidth ;
    public int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // WORLD SETTINGS
    public final int maxWorldCol = 95;
    public final int maxWorldRow = 79;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;


    // SYSTEM
    public TileManager tileM = new TileManager(this);
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
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> entitiesList = new ArrayList<Entity>();
    public ArrayList<Entity> particles = new ArrayList<>();

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
    final public int inInventory = 8;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

    }
    public void toggleScreenMode(){
        //GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (Objects.equals(displayMode, windowMode)){
            gd.setFullScreenWindow(null);


            screenWidth2 = screenWidth;
            screenHeight2 = screenHeight;

            // 3. On redimensionne la fenêtre Windows proprement
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            Main.window.pack(); // Ajuste la fenêtre à la taille du Panel
            Main.window.setLocationRelativeTo(null); // Centre la fenêtre



        }else{
            gd.setFullScreenWindow(Main.window);
            // GET FULLSCREEN WIDTH AND HEIGHT
            screenWidth2 = Main.window.getWidth();
            screenHeight2 = Main.window.getHeight();
        }



    }

    public void setupGame(){
        assetSetter.setObject();
        assetSetter.setMonster();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
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

            for (Tile tile :tileM.tile){
                if (tile != null && tile.animation != null){
                    tile.animation.update();
                }
            }

            player.update();

            for (Entity entity : monster) {
                if (entity != null) {
                    if (entity.alive && !entity.dying){
                        entity.update();
                    }
                }
            }

            monster.removeIf(e -> !e.alive);

            for (Entity entity : projectileList) {
                if (entity != null) {
                    if (entity.alive ){
                        entity.update();
                    }
                }
            }

            projectileList.removeIf(e -> !e.alive);


        }
        if (gameState == pauseState){
            // NOTHING
        }

    }
    public void drawToTempScreen(){

        long drawStart =0;
        //DEBUG
        if (debugMode){
            drawStart = System.nanoTime();
        }

        if (gameState == titleState){


        }else{
            //TILE 1ST LAYER
            tileM.draw(g2,1);

            // OBJECT
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2, this);
                }
            }
            entitiesList.add(player);
            for (Entity e : monster) {
                if (e != null) {
                    entitiesList.add(e);
                }
            }
            for (Entity e : projectileList) {
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
                e.draw(g2);
            }

            entitiesList.clear();

            //TILE 2ND LAYER
            tileM.draw(g2,2);





        }

        ui.draw(g2);

        if(gameState == playState ){
            //DEBUG
            if (debugMode){
                long drawEnd = System.nanoTime();
                long passedTime = drawEnd - drawStart;
                g2.setColor(Color.white);
                g2.setFont(new Font("Serif", Font.BOLD, 30));
                g2.drawString("Draw Time: " + passedTime , (screenHeight /57.6f), (screenHeight /1.44f));
                g2.drawString("Coordinate :" + player.worldX+","+player.worldY , (screenHeight /57.6f), (screenHeight /1.34f));
                player.showHitbox(g2);

            }
        }
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawToTempScreen();

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tempScreen, 0, 0,screenWidth2,screenHeight2, null);
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
