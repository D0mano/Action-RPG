package object;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The SuperObject class serves as the base parent class for all items,
 * interactable environment props (doors, chests), and usable equipment
 * in the game. It extends Entity to share common grid positioning logic.
 */
public class SuperObject extends Entity {
    public GamePanel gp;
    public BufferedImage image;
    public String name;
    public boolean collision = false; // Does this object block movement? (e.g., closed doors, chests)

    // OVERRIDDEN POSITION VARIABLES (Specific to static objects)
    public int worldX, worldY;

    // OBJECT CATEGORY TYPES
    public int objectType;
    public final int gear = 0;        // Permanent upgrades or quest items (e.g., maps, permanent buffs)
    public final int singleUse = 1;   // Consumables (e.g., potions, fruits)
    public final int equipment = 2;   // Usable items that can be equipped to shortcuts J, K, L (e.g., swords, wands)
    public final int props = 3;       // Static environment objects (e.g., doors, chests)

    // COLLISION HITBOX
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solideAreaDefaultX = 0;
    public int solideAreaDefaultY = 0;

    // INTERACTION & AUDIO
    public String[] dialogues = new String[20]; // Stores text if the object can be read/inspected
    public int soundEffectIndex;                // The sound played when the object is used or picked up

    UtilityTool uTool = new UtilityTool();

    /**
     * Constructor for the SuperObject.
     * @param gp The GamePanel instance.
     */
    public SuperObject(GamePanel gp) {
        super(gp); // Calls the parent Entity constructor
        this.gp = gp;
    }

    /**
     * Reloads the object's physical dimensions and images.
     * Called when the screen scaling or resolution is modified during gameplay.
     */
    @Override
    public void reload() {
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        setWorldCoordinate();

        // Rescale the image to match the new tile size
        if (image != null) {
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }
    }

    /**
     * Converts the object's grid column and row positions into precise
     * pixel coordinates on the world map.
     */
    public void setWorldCoordinate() {
        worldX = worldCol * gp.tileSize;
        worldY = worldRow * gp.tileSize;
    }

    /**
     * Utility method to load and scale the object's image from the resources folder.
     * * @param imageName The name of the image file (without .png).
     * @param scale     The game's current scale factor.
     * @return The properly formatted and scaled BufferedImage.
     */
    public BufferedImage setup(String imageName, int scale) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage loadedImage = null;
        try {
            // Load the image from the /objects/ folder
            loadedImage = ImageIO.read(getClass().getResourceAsStream("/objects/" + imageName + ".png"));
            // Scale it according to the requested size
            loadedImage = uTool.scaleImage(loadedImage, loadedImage.getWidth() * scale, loadedImage.getHeight() * scale);
        } catch (IOException e) {
            System.err.println("[SuperObject] Missing image file: /objects/" + imageName + ".png");
            e.printStackTrace();
        }
        return loadedImage;
    }

    /**
     * Renders the object on the screen.
     * Includes a camera check to only draw the object if it is currently visible
     * inside the player's viewport (Performance Optimization).
     * * @param g2d The Graphics2D component.
     * @param gp  The GamePanel instance.
     */
    public void draw(Graphics2D g2d, GamePanel gp) {

        // Calculate screen coordinates relative to the player's current camera position
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // PERFORMANCE OPTIMIZATION: Only draw if the object is within the screen bounds
        if (((-gp.tileSize) <= screenX && screenX <= (gp.worldWidth + gp.tileSize)) &&
                ((-gp.tileSize) <= screenY && screenY <= (gp.worldHeight + gp.tileSize))) {

            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    /**
     * Triggers a dialogue specific to this object.
     * Designed to be overridden by subclasses (like readable signs or NPCs).
     * * @param i The index of the dialogue line to display.
     */
    public void speak(int i) {
        // Base logic can be added here or fully overridden in child classes
    }

    /**
     * Defines the action to execute when the player uses this item from their inventory.
     * Designed to be overridden by subclasses (e.g., swinging a sword, drinking a potion).
     * * @return True if the item was successfully used (triggering durability loss or consumption), False otherwise.
     */
    public boolean use() {
        return false;
    }
}