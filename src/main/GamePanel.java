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


    public boolean running = false;

    public boolean debugMode = false;
    public String windowMode = "Windowed";
    public String fullScreenMode = "FullScreen";


    // SCREEN SETTINGS
    public final int originalTileSize = 16;        //16x16 Tiles
    public int scale = 3;
    public String displayMode = windowMode ;

    public  int tileSize = originalTileSize * scale;   // 48x48 Tiles
    public  int maxScreenCol = 16;
    public  int maxScreenRow = 10;
    public  int screenWidth = tileSize * maxScreenCol;    // 768 pixels
    public  int screenHeight = tileSize * maxScreenRow;    // 576 pixels

    public int screenWidth2 = screenWidth ;
    public int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // WORLD SETTINGS
    public int maxWorldCol;
    public int maxWorldRow;
    public int maxWorldLayer;
    public int worldWidth;
    public int worldHeight;
    public int currentMapIndex = 0;
    public ArrayList<Map> mapsList = new ArrayList<>();
    public double luminosity = 1 ;

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
    public Config config = new Config(this);

    // ENTITY AND PLAYER
    public Player player = new Player(this,this.keyH);
    public ArrayList<SuperObject>  obj = new ArrayList<>();
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
        this.setPreferredSize(new Dimension(screenWidth2, screenHeight2));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        loadMap(new Map(this,"OverWorld"));

    }
    public void setScreenMode() {

        GraphicsDevice gd = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        if (displayMode.equals(windowMode)) {

            // SORTIE FULLSCREEN
            gd.setFullScreenWindow(null);

            screenWidth2 = screenWidth;
            screenHeight2 = screenHeight;

            this.setPreferredSize(new Dimension(screenWidth2, screenHeight2));

            Main.window.setResizable(true);
            Main.window.pack();
            Main.window.setLocationRelativeTo(null);
            Main.window.setResizable(false);

        } else {

            // FULLSCREEN
            Main.window.dispose(); // IMPORTANT
            Main.window.setUndecorated(true);
            gd.setFullScreenWindow(Main.window);

            screenWidth2 = 1600;
            screenHeight2 = 1000;

            this.setPreferredSize(new Dimension(screenWidth2, screenHeight2));
            Main.window.setVisible(true);
        }

        createTempScreen(); // OBLIGATOIRE
        this.revalidate();
        this.repaint();
    }
    public void updateSetting(){
        reload();
        setScreenMode();

    }
    public void reload(){
        tileSize = originalTileSize * scale;
        screenWidth = tileSize * maxScreenCol;
        screenHeight = tileSize * maxScreenRow;
        screenWidth2 = screenWidth ;
        screenHeight2 = screenHeight;
        worldWidth = tileSize * maxWorldCol;
        worldHeight = tileSize * maxWorldRow;

        tileM.reload();
        assetSetter.reload();
        player.reload();
        ui.reload();

    }

    public void createTempScreen() {
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    public void loadMap(Map newMap){
        mapsList.add(newMap);
    }
    public void setMap(int mapIndex){
        currentMapIndex = mapIndex;
        Map currentMap = mapsList.get(mapIndex);
        tileM.currentMap = currentMap;
        maxWorldLayer = currentMap.maxMapLayer;
        maxWorldCol = currentMap.maxMapCol;
        maxWorldRow = currentMap.maxMapRow;
        worldWidth = currentMap.mapWidth;
        worldHeight = currentMap.mapHeight;
        monster = currentMap.monsterList;
        obj = currentMap.objectsList;

    }


    public void setupGame(){
        setMap(0);
        assetSetter.setObject();
        assetSetter.setMonster();
        gameState = titleState;
        createTempScreen();
        updateSetting();
    }
    public void startGameThread() {
        if (gameThread==null){
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            playMusic(18);
        }

    }

    public void stopGameThread() {
        running = false;
        try {
            gameThread.join(); // attendre la fin proprement
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameThread = null;
    }


    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS; // 0.016666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCounter = 0;

        while(running){

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
            // Display FPS
            if (timer >= 1000000000) {
                System.out.println("FPS :" +drawCounter);
                drawCounter = 0;
                timer = 0;
           }





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
        ui.update();

    }

    public void drawToTempScreen(){
        if (tempScreen == null || g2 == null) {

            createTempScreen();
        }

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

            entitiesList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
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
                g2.setFont(new Font("Serif", Font.BOLD, (int)(screenHeight/19.2f)));
                g2.drawString("Draw Time: " + passedTime , (screenHeight /57.6f), (screenHeight /1.92f));
                g2.drawString("Coordinate :" + player.worldX+","+player.worldY , (screenHeight /57.6f), (screenHeight /1.74f));
                g2.drawString("Tile Coordinate :"+ player.worldCol +","+player.worldRow,(screenHeight /57.6f), (screenHeight /1.60f));
                player.showHitbox(g2);

            }
        }
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawToTempScreen();

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tempScreen, 0, 0,screenWidth2,screenHeight2, null);
//        System.out.println("Screen width: " + screenWidth2+ " Screen height: " + screenHeight2);
//        System.out.println("Panel width: " + screenWidth+ " Panel height: " + screenHeight);

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
