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
    public Tile[] tile;
    public Map currentMap;

    // Temporarily maps a tile ID to its physical properties parsed from the text file
    HashMap<Integer, TileData> tileDataMap = new HashMap<>();

    /**
     * Constructor for the TileManager.
     * Initializes the tile array and triggers the loading process.
     * @param gp The GamePanel instance.
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;
        // Supports up to 120 unique tile types. Increase this if your tileset grows.
        tile = new Tile[120];

        loadTileData("/maps/tile_data.txt");
        getTileImageFromTileSet("TunicTilesetV2");
    }

    /**
     * Reloads the tileset images. 
     * Necessary when screen scaling or resolution is modified during gameplay.
     */
    public void reload() {
        getTileImageFromTileSet("TunicTilesetV2");
    }

    /**
     * Reads a CSV-style text file containing metadata for each tile ID.
     * Format expected: ID, collision(true/false), layer(int), solidSides(e.g., up;right), animated(true/false)
     * @param filePath The resource path to the tile data text file.
     */
    public void loadTileData(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                // Ignore empty lines or comments starting with '#'
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0].trim());
                boolean collision = Boolean.parseBoolean(parts[1].trim());
                int layer = Integer.parseInt(parts[2].trim());
                boolean animated = Boolean.parseBoolean(parts[4].trim());

                // Parse specific collision sides
                String sidesRaw = parts[3].trim(); // e.g., "up;right"
                ArrayList<String> collisionSide = new ArrayList<>();

                if (sidesRaw.equals("none")) {
                    // Leave empty
                } else if (sidesRaw.equals("all")) {
                    collisionSide.add("up");
                    collisionSide.add("down");
                    collisionSide.add("left");
                    collisionSide.add("right");
                } else {
                    String[] sides = sidesRaw.split(";");
                    for (String side : sides) {
                        collisionSide.add(side.trim());
                    }
                }

                // Store the parsed metadata in the temporary HashMap
                tileDataMap.put(id, new TileData(collision, layer, collisionSide, animated, id));
            }
            br.close();

        } catch (Exception e) {
            System.err.println("[TileManager] Error reading tile data file!");
            e.printStackTrace();
        }
    }

    /**
     * Slices the main tileset image into individual square graphics based on the original tile size.
     * Matches each sliced image with its previously parsed TileData.
     * @param tileSetName The name of the tileset image file (without the .png extension).
     */
    public void getTileImageFromTileSet(String tileSetName) {
        try {
            BufferedImage tileset = ImageIO.read(getClass().getResourceAsStream("/tilesets/" + tileSetName + ".png"));
            int width = tileset.getWidth();
            int height = tileset.getHeight();

            // Calculate grid dimensions of the tileset image
            int nbcol = width / gp.originalTileSize;
            int nbrow = height / gp.originalTileSize;
            int index = 0;

            for (int i = 0; i < nbrow; i++) {
                for (int j = 0; j < nbcol; j++) {

                    // Slice out a single frame
                    BufferedImage tileImage = tileset.getSubimage(
                            j * gp.originalTileSize,
                            i * gp.originalTileSize,
                            gp.originalTileSize,
                            gp.originalTileSize
                    );

                    // If we have metadata for this specific tile index, build the Tile object
                    if (tileDataMap.containsKey(index)) {
                        TileData data = tileDataMap.get(index);

                        boolean collision = data.collision;
                        int layer = data.layer;
                        int id = data.id;
                        boolean animated = data.animated;
                        ArrayList<String> collisionSide = new ArrayList<>(data.collisionSide);

                        setup(index, tileImage, collision, layer, collisionSide, id, animated);
                    }
                    index++; // Increment ID for the next slice
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finalizes the creation of a Tile object. It scales the image to match the game's 
     * current display settings and initializes the Animator if the tile is animated.
     * @param index         The array index / ID of the tile.
     * @param image         The raw sliced image from the tileset.
     * @param collision     Collision flag.
     * @param layer         Map layer.
     * @param collisionSide List of solid sides.
     * @param id            Unique ID.
     * @param animated      Animation flag.
     */
    public void setup(int index, BufferedImage image, boolean collision, int layer, ArrayList<String> collisionSide, int id, Boolean animated) {
        UtilityTool uTool = new UtilityTool();
        tile[index] = new Tile();
        tile[index].id = id;

        // Scale image to current game resolution
        tile[index].image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        tile[index].collision = collision;
        tile[index].layer = layer;
        tile[index].collisionSide = collisionSide;

        // Setup animation if required (expects a sprite sheet named "[id]-Sheet.png" in the tiles folder)
        if (animated) {
            try {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/tiles/" + id + "-Sheet.png"));
                spriteSheet = uTool.scaleImage(spriteSheet, spriteSheet.getWidth() * gp.scale, spriteSheet.getHeight() * gp.scale);
                // The Animator loops infinitely with a speed of 10 ticks per frame
                tile[index].animation = new Animator(spriteSheet, gp.tileSize, gp.tileSize, 10, true);
            } catch (Exception e) {
                System.err.println("[TileManager] Could not load animation sheet for tile ID: " + id);
                e.printStackTrace();
            }
        }
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

                // Calculate real world coordinates
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;

                // Calculate screen coordinates relative to the player's camera position
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // PERFORMANCE OPTIMIZATION: Only draw the tile if it's within the screen bounds
                if (((-gp.tileSize) <= screenX && screenX <= gp.screenWidth) &&
                        ((-gp.tileSize) <= screenY && screenY <= gp.screenHeight)) {

                    if (tileNum != -1) { // -1 represents an empty/transparent tile space
                        if (tile[tileNum].animation == null) {
                            // Draw static image
                            g2d.drawImage(tile[tileNum].image, screenX, screenY, null);
                        } else {
                            // Draw current animation frame
                            tile[tileNum].animation.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                        }
                    }
                }
            }
        }
    }
}