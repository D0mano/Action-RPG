package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        //getTileImage();
        getTileImageFromTileSet("TunicTilesetV1");
        loadMap("/maps/Worldtest.csv");
    }
    public void getTileImage(){
        setup(0,"grass",false);

        setup(1,"wall",true);

        setup(2,"water",true);

        setup(3,"earth",false);

        setup(4,"tree",true);

        setup(5,"sand",false);
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
                    BufferedImage tile = tileset.getSubimage(j*gp.originalTileSize,i*gp.originalTileSize,gp.originalTileSize,gp.originalTileSize);
                    boolean collision = false;
                    if ((17 <= index && index <= 29)|| (32 <= index && index <= 44)){
                        collision = true;
                    }
                    setup2(index,tile,collision);
                    index++;

                }

            }


        }catch(IOException e){e.printStackTrace();}

    }

    public void loadMap(String mapPath){
        try{
            InputStream is = getClass().getResourceAsStream(mapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            for(int row = 0; row < gp.maxWorldRow; row++){
                String line = br.readLine();
                for( int col = 0; col < gp.maxWorldCol; col++){
                    String[] numbers = line.split(",");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[row][col] = num;

                }
            }
            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setup(int index ,String imageName,boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+imageName+".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize,  gp.tileSize);
            tile[index].collision = collision;

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setup2(int index,BufferedImage image,boolean collision){
        UtilityTool uTool = new UtilityTool();
        tile[index] = new Tile();
        tile[index].image = image;
        tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize,  gp.tileSize);
        tile[index].collision = collision;

    }

    public void draw(Graphics2D g2d){
       for(int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++){
           for(int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++){
               int tileNum = mapTileNum[worldRow][worldCol];
               int worldX = worldCol * gp.tileSize;
               int worldY = worldRow * gp.tileSize;
               int screenX = worldX - gp.player.worldX + gp.player.screenX;
               int screenY = worldY - gp.player.worldY + gp.player.screenY;
               if(((-gp.tileSize) <= screenX &&  screenX <= (gp.worldWidth + gp.tileSize)) &&
                       ((- gp.tileSize) <= screenY &&  screenY <= (gp.worldHeight + gp.tileSize))){
                   g2d.drawImage(tile[tileNum].image, screenX, screenY, null);


               }


           }
       }
    }
}
