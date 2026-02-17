package tile;

import main.Animator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Represents a single type of tile in the game (e.g., grass, wall, water).
 * Holds the graphical representation and physical properties (like collisions)
 * used to build the map grid.
 */
public class Tile {

    public int id;
    public BufferedImage image;

    // PHYSICAL PROPERTIES
    public boolean collision = false;
    public int layer = 1;

    /** * Specifies which sides of the tile block movement (e.g., ["up", "left"]).
     * Useful for one-way platforms or sloped walls.
     */
    public ArrayList<String> collisionSide;

    // ANIMATION
    /**
     * If the tile is animated (like moving water), this holds the animation logic.
     * Will be null if the tile is static.
     */
    public Animator animation;
}