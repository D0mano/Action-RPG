package tile;

import main.Animator;
import main.GamePanel;
import main.Map;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The TileManager class is responsible for loading the tileset image, slicing it into individual tiles,
 * parsing tile physical properties from a text file, and rendering the visible portion of the map to the screen.
 */
public class TileManager {
    GamePanel gp;
    public Map currentMap;
    public Tileset currentTileset;


    public ArrayList<Tileset> tilesets = new ArrayList<>();

    /**
     * Constructor for the TileManager.
     * Initializes the tile array and triggers the loading process.
     * @param gp The GamePanel instance.
     */
    public TileManager(GamePanel gp,Map map) {
        this.gp = gp;
        this.currentMap = map;

    }


    /**
     * Renders the specified layer of the current map onto the screen.
     * Incorporates camera tracking to only draw tiles that are currently visible 
     * inside the player's viewport, highly optimizing rendering performance.
     * @param g2d         The Graphics2D component.
     * @param layerToDraw The requested layer (1-indexed, converted to 0-indexed internally).
     */
    public void draw(Graphics2D g2d, int layerToDraw) {
        int layer = layerToDraw - 1; // Convert 1-indexed to 0-indexed array layer

        // Reset opacity
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        for (int worldRow = 0; worldRow < currentMap.maxMapRow; worldRow++) {
            for (int worldCol = 0; worldCol < currentMap.maxMapCol; worldCol++) {

                // Get the specific tile ID for this coordinate
                int tileNum = currentMap.tileMap[worldRow][worldCol][layer];

                boolean tileSetFound = false;
                for (int i = tilesets.size()-1;i>=0;i--){
                    if (tileNum >= tilesets.get(i).firstId){
                        currentTileset = tilesets.get(i);
                        tileSetFound =true;
                    }
                }
                int tileId = tileSetFound ? tileNum - currentTileset.firstId  : -1;

                // Calculate real world coordinates
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;

                // Calculate screen coordinates relative to the player's camera position
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // PERFORMANCE OPTIMIZATION: Only draw the tile if it's within the screen bounds
                if (((-gp.tileSize) <= screenX && screenX <= gp.screenWidth) &&
                        ((-gp.tileSize) <= screenY && screenY <= gp.screenHeight)) {

                    if (tileId != -1) { // -1 represents an empty/transparent tile space
                        if (currentTileset.tiles.get(tileId).animation == null) {
                            // Draw static image
                            g2d.drawImage(currentTileset.tiles.get(tileId).image, screenX, screenY, null);
                        } else {
                            // Draw current animation frame
                            currentTileset.tiles.get(tileId).animation.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                        }
                    }
                }
            }
        }
    }

    public void update(){
        for (Tileset tileset: tilesets){tileset.update();}
    }

    public Tile getTile(int tileGid){
        Tileset tileset = null;
        boolean tileSetFound = false;
        for (int i = tilesets.size()-1;i>=0;i--){
            if (tileGid >= tilesets.get(i).firstId){
                tileset = tilesets.get(i);
                tileSetFound =true;
            }
        }
        int tileId = tileSetFound ? tileGid - tileset.firstId   : -1;
        Tile tile =tileSetFound ? tileset.tiles.get(tileId) : null;
        if (tile == null){
            System.out.println("test");
        }
        return tile;

    }
}