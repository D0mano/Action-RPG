package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int[][] mapTileNum;


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];
        getTileImage();
        loadMap("/maps/map01.txt");
    }

    public void loadMap(String mapPath){
        try{
            InputStream is = getClass().getResourceAsStream(mapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            for(int row = 0; row < gp.maxScreenRow; row++){
                String line = br.readLine();
                for( int col = 0; col < gp.maxScreenCol; col++){
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[row][col] = num;

                }
            }
            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getTileImage(){

        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));


        }catch(IOException e){
            e.printStackTrace();

        }

    }

    public void draw(Graphics2D g2d){
       for(int row = 0; row < gp.maxScreenRow; row++){
           for(int col = 0; col < gp.maxScreenCol; col++){
               g2d.drawImage(tile[mapTileNum[row][col]].image, col* gp.tileSize, row* gp.tileSize, gp.tileSize, gp.tileSize , null);

           }
       }
    }
}
