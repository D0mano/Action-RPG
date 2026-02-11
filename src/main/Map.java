package main;

import entity.Entity;
import object.SuperObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Map {
    public GamePanel gp;
    public String mapName;

    // MAP DIMENSION
    public int maxMapCol;
    public int maxMapRow;
    public int maxMapLayer;
    public int mapWidth;
    public int mapHeight;

    //TILEMAP DATA
    public int [][][] tileMap; // [Row][Col][Layer]

    // MONSTER INFO
    public ArrayList<Entity> monsterList = new ArrayList<>();

    //OBJECT INFO
    public ArrayList<SuperObject> objectsList = new ArrayList<>();

    public Map(GamePanel gp,String mapName){
        this.gp = gp;
        this.mapName = mapName;
        loadMap();
    }

    public void getMapMetaData(){
        try{
            // Layer counting
            int layer = 0;
            while(true){
                String path = "/maps/"+mapName+"_layer_"+(layer+1)+".csv";
                InputStream is = getClass().getResourceAsStream(path);
                if(is == null) break;
                layer++;
                is.close();
            }
            this.maxMapLayer = layer;

            // Collums counting

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
            this.maxMapCol = cols;
            this.maxMapRow = rows;
            this.mapWidth = gp.tileSize  * this.maxMapCol;
            this.mapHeight = gp.tileSize * this.maxMapRow;
        }catch(IOException e){e.printStackTrace();}

    }

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
                    for (int col = 0; col < maxMapCol; col++) {
                        String[] numbers = line.split(",");

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
}
