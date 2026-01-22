package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum1; // First layer
    public int[][] mapTileNum2; // Seconde layer
    HashMap<Integer, TileData> tileDataMap = new HashMap<>();




    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[80];
        mapTileNum1 = new int[gp.maxWorldRow][gp.maxWorldCol];
        mapTileNum2 = new int[gp.maxWorldRow][gp.maxWorldCol];
        loadTileData("/maps/tile_data.txt");
        getTileImageFromTileSet("TunicTilesetV2");
        loadMap("/maps/world02");
    }

    public void loadTileData(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0].trim());
                boolean collision = Boolean.parseBoolean(parts[1].trim());
                int layer = Integer.parseInt(parts[2].trim());

                String sidesRaw = parts[3].trim(); // ex: "up;right"
                ArrayList<String> collisionSide = new ArrayList<>();

                if (sidesRaw.equals("none")) {
                }
                else if (sidesRaw.equals("all")) {
                    collisionSide.add("up");
                    collisionSide.add("down");
                    collisionSide.add("left");
                    collisionSide.add("right");
                }
                else {
                    String[] sides = sidesRaw.split(";");
                    for (String side : sides) {
                        collisionSide.add(side.trim());
                    }
                }


                tileDataMap.put(id, new TileData(collision, layer, collisionSide));
            }
            br.close();

        } catch (Exception e) {
            System.out.println("Erreur lecture fichier tuiles !");
            e.printStackTrace();
        }
    }

    public void getTileImageFromTileSet(String tileSetName){
        try{
            BufferedImage tileset = ImageIO.read(getClass().getResourceAsStream("/tilesets/"+tileSetName+".png"));
            int width = tileset.getWidth();
            int height = tileset.getHeight();
            int nbcol = width/gp.originalTileSize;
            int nbrow = height/gp.originalTileSize;
            int index = 0;
            for(int i = 0; i < nbrow; i++){
                for(int j = 0; j < nbcol; j++){
                    boolean collision ;
                    int layer;
                    ArrayList<String> collisionSide;
                    BufferedImage tile = tileset.getSubimage(j*gp.originalTileSize,i*gp.originalTileSize,gp.originalTileSize,gp.originalTileSize);
                    if (tileDataMap.containsKey(index)) {
                        TileData data = tileDataMap.get(index);

                        collision = data.collision;

                        layer = data.layer;

                        collisionSide = new ArrayList<>(data.collisionSide);
                        setup(index,tile,collision,layer,collisionSide);
                        index++;
                    }


                }

            }


        }catch(IOException e){e.printStackTrace();}

    }

    public void loadMap(String mapPath){
        // 1ST LAYER LOADING
        try{
            InputStream is = getClass().getResourceAsStream(mapPath+"_layer_1.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            for(int row = 0; row < gp.maxWorldRow; row++){
                String line = br.readLine();
                for( int col = 0; col < gp.maxWorldCol; col++){
                    String[] numbers = line.split(",");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum1[row][col] = num;

                }
            }
            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        // 2ND LAYER LOADING
        try{
            InputStream is = getClass().getResourceAsStream(mapPath+"_layer_2.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            for(int row = 0; row < gp.maxWorldRow; row++){
                String line = br.readLine();
                for( int col = 0; col < gp.maxWorldCol; col++){
                    String[] numbers = line.split(",");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum2[row][col] = num;

                }
            }
            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setup(int index,BufferedImage image,boolean collision,int layer,ArrayList<String>collisionSide){
        UtilityTool uTool = new UtilityTool();
        tile[index] = new Tile();
        tile[index].image = image;
        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize,  gp.tileSize);
        tile[index].collision = collision;
        tile[index].layer = layer;
        tile[index].collisionSide = collisionSide;

    }

    public void draw(Graphics2D g2d,int layer){
       for(int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++){
           for(int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++){
               int tileNum = mapTileNum1[worldRow][worldCol];
               int tileNum2 = mapTileNum2[worldRow][worldCol];

               int worldX = worldCol * gp.tileSize;
               int worldY = worldRow * gp.tileSize;
               int screenX = worldX - gp.player.worldX + gp.player.screenX;
               int screenY = worldY - gp.player.worldY + gp.player.screenY;

               if(((-gp.tileSize) <= screenX &&  screenX <= (gp.worldWidth + gp.tileSize)) &&
                       ((- gp.tileSize) <= screenY &&  screenY <= (gp.worldHeight + gp.tileSize))){
                   if (layer==1){
                       if( tileNum != -1 && tile[tileNum].layer == layer){
                           g2d.drawImage(tile[tileNum].image, screenX, screenY, null);
                       }
                   }
                   else if (layer==2){
                       if(tileNum2 != -1 && tile[tileNum2].layer == layer){
                           g2d.drawImage(tile[tileNum2].image, screenX, screenY, null);

                       }

                   }
               }


           }
       }
    }
}
