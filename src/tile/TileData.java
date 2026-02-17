package tile;

import java.util.ArrayList;

/**
 * A Data Transfer Object (DTO) used to temporarily store tile properties
 * parsed from the text configuration file.
 * This data is later combined with the graphical images to create full Tile objects.
 */
public class TileData {
    int id;
    public boolean collision;
    public int layer;
    public ArrayList<String> collisionSide;
    boolean animated;

    /**
     * Constructor for TileData.
     * * @param collision     True if the tile is solid.
     * @param layer         The depth layer of the tile (e.g., background vs foreground).
     * @param collisionSide List of specific solid sides.
     * @param animated      True if the tile requires an Animator.
     * @param id            The unique identifier of the tile.
     */
    public TileData(boolean collision, int layer, ArrayList<String> collisionSide, Boolean animated, int id){
        this.collision = collision;
        this.layer = layer;
        this.collisionSide = collisionSide;
        this.animated = animated;
        this.id = id;
    }
}