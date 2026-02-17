package main;

import entity.Entity;
import object.SuperObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Represents a playable area (Map) in the game, holding map dimensions,
 * tile arrays, and lists of entities/objects present on it.
 */
public class Map {
    public GamePanel gp;
    public String mapName;

    // MAP DIMENSIONS
    public int maxMapCol;
    public int maxMapRow;
    public int maxMapLayer;
    public int mapWidth;
    public int mapHeight;

    // TILEMAP DATA
    public int [][][] tileMap; // Array structure: [Row][Col][Layer]

    // ENTITIES & OBJECTS
    public ArrayList<Entity> monsterList = new ArrayList<>();
    public ArrayList<SuperObject> objectsList = new ArrayList<>();

    // PLAYER SPAWN COORDINATES
    public int playerCol;
    public int playerRow;

    /**
     * Constructor to initialize and load a map.
     * * @param gp The GamePanel instance.
     * @param mapName The base name of the map files (e.g., "Overworld").
     */
    public Map(GamePanel gp, String mapName){
        this.gp = gp;
        this.mapName = mapName;
        loadMap();
    }

    /**
     * Analyzes the map CSV files to determine the total number of layers, columns, and rows.
     * Calculates the total width and height of the map based on the tile size.
     */
    public void getMapMetaData(){
        try{
            // Count the number of layers by checking existing files
            int layer = 0;
            while(true){
                String path = "/maps/"+mapName+"_layer_"+(layer+1)+".csv";
                InputStream is = getClass().getResourceAsStream(path);
                if(is == null) break; // Exit loop when no more layers are found
                layer++;
                is.close();
            }
            this.maxMapLayer = layer;

            // Count rows and columns based on the first layer
            InputStream is = getClass().getResourceAsStream("/maps/"+mapName+"_layer_1.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            int cols = 0;
            int rows = 0;
            if((line = br.readLine()) != null){
                String[] numbers = line.split(",");
                cols = numbers.length;
                rows++;
            }
            while(br.readLine() != null){
                rows++;
            }
            br.close();

            // Assign dimensions
            this.maxMapCol = cols;
            this.maxMapRow = rows;
            this.mapWidth = gp.tileSize  * this.maxMapCol;
            this.mapHeight = gp.tileSize * this.maxMapRow;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads the tile ID map data from the CSV files into the 3D tileMap array.
     */
    public void loadMap(){
        getMapMetaData();
        tileMap = new int[this.maxMapRow][this.maxMapCol][this.maxMapLayer];
        try{
            for (int layer = 0; layer < maxMapLayer; layer++) {
                String layerPath = "/maps/"+mapName+"_layer_"+(layer+1)+".csv";
                InputStream is = getClass().getResourceAsStream(layerPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                for (int row = 0; row < maxMapRow; row++) {
                    String line = br.readLine();
                    String[] numbers = line.split(",");
                    for (int col = 0; col < maxMapCol; col++) {
                        int num = Integer.parseInt(numbers[col]);
                        tileMap[row][col][layer] = num;
                    }
                }
                br.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Places the player at the predefined spawn coordinates for this specific map.
     */
    public void setPlayerSpawn(){
        gp.player.worldCol = playerCol;
        gp.player.worldRow = playerRow;
        gp.player.worldX = playerCol * gp.tileSize;
        gp.player.worldY = playerRow * gp.tileSize;
    }

    /**
     * Reloads the map dimensions and data (useful when resolution/tile size changes).
     */
    public void reload(){
        mapWidth  = gp.tileSize * this.maxMapCol;
        mapHeight = gp.tileSize * this.maxMapRow;
        loadMap();
    }
}