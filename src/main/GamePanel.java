package main;

import data.SaveLoad;
import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.Tile;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {

    public int timeSpend = 0; // Time spend playing (in frames)


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
    public Map currentMap;
    KeyHandler keyH = new KeyHandler(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public EventHandler eventHandler = new EventHandler(this);

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

    // SAVE SETTINGS
    public SaveLoad[] saves = new SaveLoad[3];
    public int currentSaveIndex = 0;

    public UI ui = new UI(this,player);




    // GAME STATE
    public int gameState;
    public int previousState;
    public final int titleState = 0;
    public final int optionState = 1;
    public final int loadSaveState = 2;
    public final int loadSaveSelectionState = 3;
    public final int playState = 4;
    public final int pauseState = 5;
    public final int dialogueState = 6;
    public final int audioSettingstate = 7;
    public final int graphicsSettingstate = 8;
    final public int inInventory = 9;
    final public int gameOver = 10;
    final public int newGameSlotState = 11;
    final public int controlSettingState = 12;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth2, screenHeight2));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        loadMap(new Map(this,"Overworld"));
        loadMap(new Map(this,"EastForest"));

    }

    /**
     * Sets the screen mode to either Windowed or Fullscreen based on settings.
     * It disposes the current frame, adjusts rendering sizes, and re-displays the window.
     */
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

    /**
     * Reloads all assets, map dimensions, and updates screen configurations.
     */
    public void updateSetting(){
        reload();
        setScreenMode();

    }

    /**
     * Recalculates variables based on current scaling and reloads necessary manager data.
     */
    public void reload(){
        tileSize = originalTileSize * scale;
        screenWidth = tileSize * maxScreenCol;
        screenHeight = tileSize * maxScreenRow;
        screenWidth2 = screenWidth ;
        screenHeight2 = screenHeight;
        worldWidth = tileSize * maxWorldCol;
        worldHeight = tileSize * maxWorldRow;


        assetSetter.reload();
        player.reload();
        ui.reload();
        for (Map m : mapsList) {
            m.reload();
        }

    }

    public void retry(){
        resetMonsters();
        player.retry();
        gameState = playState;
        setMap(currentMapIndex);


    }

    public void reset(){
        for (Map map : mapsList) {
            map.monsterList.clear();
            map.objectsList.clear();
        }
        player.resetPlayerValues();
    }

    /**
     * Creates an off-screen BufferedImage buffer used for rendering to prevent flickering.
     */
    public void createTempScreen() {
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    /**
     * Adds a new Map instance to the global map list.
     * @param newMap The map object to be registered.
     */
    public void loadMap(Map newMap){
        mapsList.add(newMap);
    }

    /**
     * Switches the active map of the game using the provided index.
     * Restores map limits, entities, and objects for the newly active map.
     * @param mapIndex The index of the map in the mapsList.
     */
    public void setMap(int mapIndex){
        System.out.println("Before map change: ");
        for (SuperObject obj : this.obj){
            System.out.println(obj.name);
        }
        System.out.println("End for loop");
        currentMapIndex = mapIndex;
        currentMap = mapsList.get(mapIndex);
        maxWorldLayer = currentMap.maxMapLayer;
        maxWorldCol = currentMap.maxMapCol;
        maxWorldRow = currentMap.maxMapRow;
        worldWidth = currentMap.mapWidth;
        worldHeight = currentMap.mapHeight;
        clearCurrentMonsterList();
        monster.addAll(currentMap.monsterList);
        System.out.println("After map change :");
        for (SuperObject obj : this.obj){
            System.out.println(obj.name);
        }
        System.out.println("End for loop");

        obj.clear();
        obj.addAll(currentMap.objectsList);
        System.out.println("After obj.clear: ");
        for (SuperObject obj : this.obj){
            System.out.println(obj.name);
        }
        System.out.println("End for loop");

    }

    /**
     * Basic game initialization. Prepares the menu state and searches for save files.
     */
    public void setup(){
        File saveDir = new File("saves");
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        gameState = titleState;
        assetSetter.setPlayerSpawn();
        setLoadFile();
        createTempScreen();
        updateSetting();
    }

    /**
     * Starts a completely new game by resetting all maps, spawning base items/monsters,
     * and resetting player stats/inventory.
     */
    public void setupGame(){
        for (Map map : mapsList) {
            map.objectsList.clear();
            map.monsterList.clear();
        }
        assetSetter.setObject();
        assetSetter.setMonster();
        setMap(0);
        mapsList.get(currentMapIndex).setPlayerSpawn();
        player.resetPlayerValues();
        player.resetInventory();
    }

    /**
     * Searches the local save directory for existing .dat files to populate the Load Menu.
     */
    public void setLoadFile(){
        for (int i = 0; i < 3; i++) {
            String fileName = "File #" + (i + 1);
            File file = new File("saves/" + fileName + ".dat");
            if (file.exists()) {
                saves[i] = new SaveLoad(this, i + 1);
                if (!ui.loadCommand.contains(fileName)) {
                    ui.loadCommand.remove("Cancel");
                    ui.loadCommand.add(fileName);
                    Collections.sort(ui.loadCommand);
                    ui.loadCommand.add("Cancel");
                }
            }
        }
    }

    /**
     * Saves the game into the currently selected save slot.
     */
    public void saveGame(){
        saves[currentSaveIndex] = new SaveLoad(this,currentSaveIndex+1);
        saves[currentSaveIndex].save();

        String slotName = saves[currentSaveIndex].fileName;
        if (!ui.loadCommand.contains(slotName)) {
            ui.loadCommand.remove("Cancel");
            ui.loadCommand.add(slotName);
            Collections.sort(ui.loadCommand);
            ui.loadCommand.add("Cancel");
        }


    }

    /**
     * Loads the game from the currently selected save slot.
     */
    public void loadGame(){
        if (saves[currentSaveIndex] == null) {
            System.err.println("[GamePanel] No saves on the slot " + (currentSaveIndex + 1));
            return;
        }
        player.resetPlayerValues();
        player.resetInventory();
        saves[currentSaveIndex].load();
    }

    /**
     * Deletes the currently selected save file from the disk and removes it from UI elements.
     */
    public void removeSave(){
        if (saves[currentSaveIndex] == null) return;

        File file = new File("saves/" + saves[currentSaveIndex].fileName + ".dat");

        if (file.exists() && !file.delete()) {
            System.err.println("[GamePanel] Impossible de supprimer : " + file.getPath());
            return;
        }

        ui.loadCommand.remove(saves[currentSaveIndex].fileName);
        saves[currentSaveIndex] = null;
    }

    /**
     * Initializes and starts the main game loop thread.
     * It also triggers the default background music.
     */
    public void startGameThread() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            playMusic(18); // Starts default BGM
        }
    }

    /**
     * Clears the active monster list from the current map.
     * Useful when resetting a map or transitioning between areas.
     */
    public void clearCurrentMonsterList() {
        monster.clear();
    }

    public void resetMonsters(){
        for (Map map : mapsList) {
            map.monsterList.clear();
        }
        assetSetter.setMonster();
    }

    public void resetObjects() {
        for (Map map : mapsList) {
            map.objectsList.clear();
        }
        assetSetter.setObject();
    }

    /**
     * Safely stops the main game loop and waits for the thread to die.
     */
    public void stopGameThread() {
        running = false;
        try {
            gameThread.join(); // Wait for the thread to finish cleanly
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameThread = null;
    }

    /**
     * Core game loop logic. Runs at a fixed rate defined by the FPS variable
     * using a delta-time accumulator to ensure consistent game speed.
     */
    @Override
    public void run() {
        // 1000000000 nanoseconds = 1 second. Divided by FPS (e.g. 60) gives frame duration.
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCounter = 0;

        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // When delta reaches 1, it's time to process the next frame
            if (delta >= 1) {
                // 1. UPDATE: Calculate new positions, physics, and game logic
                update();

                // 2. DRAW: Render the screen with the updated information
                repaint();

                delta--;
                drawCounter++;
            }

            // Display FPS in console once every second
            if (timer >= 1000000000) {
//                System.out.println("FPS :" + drawCounter);
                drawCounter = 0;
                timer = 0;
            }
        }
    }

    /**
     * Updates the logical state of the game (entity positions, animations, interactions).
     * The behavior changes drastically depending on the current gameState.
     */
    public void update() {
        // Increment playtime tracker if the game is active or paused/in inventory
        if ((gameState == playState) || (gameState == pauseState) || (gameState == inInventory)) {
            timeSpend++;
        }

        // --- PLAY STATE LOGIC ---
        if (gameState == playState) {

            // Update animated tiles
            currentMap.upadate();

            // Update main player logic
            player.update();

            // Update monsters
            for (Entity entity : monster) {
                if (entity != null) {
                    // Only update living monsters that aren't currently playing death animations
                    if (entity.alive && !entity.dying) {
                        entity.update();
                    }
                }
            }
            // Safely remove dead monsters from the list
            monster.removeIf(e -> !e.alive);

            // Update projectiles (magic, arrows, hook)
            for (Entity entity : projectileList) {
                if (entity != null) {
                    if (entity.alive) {
                        entity.update();
                    }
                }
            }
            // Safely remove destroyed projectiles from the list
            projectileList.removeIf(e -> !e.alive);
        }

        // --- PAUSE STATE LOGIC ---
        if (gameState == pauseState) {
            // Game logic is frozen, nothing updates here
        }

        // Always update UI elements (menus, dialogs, inventory cursors)
        ui.update();
    }

    /**
     * Renders all graphical elements (tiles, objects, entities, UI) onto a temporary
     * off-screen buffer. This technique (Double Buffering) prevents screen flickering
     * and allows for dynamic resolution scaling.
     */
    public void drawToTempScreen() {
        // Ensure the buffer exists
        if (tempScreen == null || g2 == null) {
            createTempScreen();
        }

        long drawStart = 0;
        if (debugMode) {
            drawStart = System.nanoTime();
        }

        if (gameState == titleState) {
            // Only UI is drawn on title screen (handled below)
        } else if ((gameState == playState) || (gameState == pauseState) || (gameState == inInventory)) {

            // 1. DRAW TILE MAP (Background / Layer 1)
            currentMap.draw(g2, 1);

            // 2. DRAW GROUND OBJECTS (Items, Chests, Doors)
            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2, this);
                }
            }

            // 3. GATHER ALL ENTITIES FOR Y-SORTING
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

            // 4. SORT ENTITIES BY Y-COORDINATE
            // This ensures entities "lower" on the screen are drawn last,
            // creating a fake 3D depth effect (e.g., a player standing in front of a monster).
            entitiesList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            // 5. DRAW SORTED ENTITIES
            for (Entity e : entitiesList) {
                e.draw(g2);
            }

            // Clear the list for the next frame
            entitiesList.clear();

            // 6. DRAW TILE MAP (Foreground / Overlapping Layer 2)
            currentMap.draw(g2, 2);
        }

        // 7. DRAW UI (Health bars, Dialogues, Inventory, Menus)
        ui.draw(g2);

        // 8. DRAW DEBUG OVERLAY
        if (gameState == playState) {
            if (debugMode) {
                long drawEnd = System.nanoTime();
                long passedTime = drawEnd - drawStart;

                g2.setColor(Color.white);
                g2.setFont(new Font("Serif", Font.BOLD, (int) (screenHeight / 19.2f)));

                // Print render time and coordinates
                g2.drawString("Draw Time: " + passedTime, (screenHeight / 57.6f), (screenHeight / 1.92f));
                g2.drawString("Coordinate :" + player.worldX + "," + player.worldY, (screenHeight / 57.6f), (screenHeight / 1.74f));
                g2.drawString("Tile Coordinate :" + player.worldCol + "," + player.worldRow, (screenHeight / 57.6f), (screenHeight / 1.60f));

                // Show player's hitboxes
                player.showHitbox(g2);
            }
        }
    }

    /**
     * Standard Swing method to render graphics. It draws the pre-rendered
     * temporary screen onto the actual JPanel window, automatically stretching
     * it to fit the current window size (screenWidth2, screenHeight2).
     * @param g The Graphics context provided by Java Swing.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Erases previous frame

        // Prepare the off-screen image
        drawToTempScreen();

        // Draw the fully composed image onto the physical screen
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);

        // Clean up graphics resources to avoid memory leaks
        g2d.dispose();
    }

    /**
     * Plays a background music track on a continuous loop.
     * @param i The index of the music file in the Sound array.
     */
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    /**
     * Stops the currently playing background music.
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * Plays a sound effect once.
     * @param i The index of the sound file in the Sound array.
     */
    public void playSoundEffect(int i) {
        soundEffects.setFile(i);
        soundEffects.play();
    }

    /**
     * Adjusts the volume of the background music.
     * @param volume The target volume level (in decibels).
     */
    public void updateMusicVolume(float volume) {
        music.updateVolume(volume);
    }

    /**
     * Adjusts the volume of the sound effects.
     * @param volume The target volume level (in decibels).
     */
    public void updateSoundVolume(float volume) {
        soundEffects.updateVolume(volume);
    }

}
