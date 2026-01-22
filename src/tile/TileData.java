package tile;

import java.util.ArrayList;

public class TileData {
    public boolean collision;
    public int layer;
    public ArrayList<String> collisionSide;

    public TileData(boolean collision, int layer, ArrayList<String> collisionSide){
        this.collision = collision;
        this.layer = layer;
        this.collisionSide = collisionSide;
    }

}
