package tile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Animator;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Tileset {
    public GamePanel gp;
    public BufferedImage tilesetImg;
    public int firstId;
    public ArrayList<Tile> tiles = new ArrayList<>();
    private JsonNode rootNode;

    public Tileset(GamePanel gp,String tilesetName,int firstId) {
        this.gp = gp;
        this.firstId = firstId;
        String tilesetPath = "/tilesets/"+tilesetName;
        loadTilesetData(tilesetPath);
        System.out.println(tilesetPath);

    }
    private void loadTilesetData(String tilesetPath) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Loading tileset data from " + tilesetPath);

        try{
            // We load the JSON file from the res/maps folder
            InputStream is = getClass().getResourceAsStream(tilesetPath);
            rootNode = mapper.readTree(is);
            tilesetImg = ImageIO.read(getClass().getResourceAsStream("/tilesets/"+rootNode.get("name").asText()+".png"));
            int width = rootNode.get("imagewidth").asInt();
            int height = rootNode.get("imageheight").asInt();

            // Calculate grid dimensions of the tileset image
            int tileSize = rootNode.get("tileheight").asInt();
            int nbcol = width / tileSize;
            int nbrow = height / tileSize;
            int index = 0;

            for (int i = 0; i < nbrow; i++) {
                for (int j = 0; j < nbcol; j++) {

                    // Slice out a single frame
                    BufferedImage tileImage = tilesetImg.getSubimage(
                            j * tileSize,
                            i * tileSize,
                            tileSize,
                            tileSize
                    );



                    // If we have metadata for this specific tile index, build the Tile object
                    int id = rootNode.get("tiles").get(index).get("id").asInt();
                    JsonNode tileData = rootNode.get("tiles").get(index).get("properties");
                    boolean animated = false;
                    boolean breakable = false;
                    boolean collision = false;
                    for (JsonNode property : tileData) {
                        if (property.get("name").asText().equals("animated")){ animated = property.get("value").asBoolean();}
                        if (property.get("name").asText().equals("breakable")){ breakable = property.get("value").asBoolean();}
                        if (property.get("name").asText().equals("collision")){ collision = property.get("value").asBoolean();}
                    }


                    setup(index, tileImage, collision, id, animated);
                    index++; // Increment ID for the next slice
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setup(int index, BufferedImage image, boolean collision, int id, boolean animated) {
        UtilityTool uTool = new UtilityTool();
        tiles.add(new Tile());
        tiles.get(index).id = id;

        // Scale image to current game resolution
        tiles.get(index).image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        tiles.get(index).collision = collision;


        // Setup animation if required (expects a sprite sheet named "[id]-Sheet.png" in the tiles folder)
        if (animated) {
            try {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/tiles/" + id + "-Sheet.png"));
                spriteSheet = uTool.scaleImage(spriteSheet, spriteSheet.getWidth() * gp.scale, spriteSheet.getHeight() * gp.scale);
                // The Animator loops infinitely with a speed of 10 ticks per frame
                tiles.get(index).animation = new Animator(spriteSheet, gp.tileSize, gp.tileSize, 10, true);
            } catch (Exception e) {
                System.err.println("[Tileset] Could not load animation sheet for tile ID: " + id);
                e.printStackTrace();
            }
        }
    }
}
