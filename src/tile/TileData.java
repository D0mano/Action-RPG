package tile;

import main.Animator;

import java.util.ArrayList;

public class TileData {
    int id;
    public boolean collision;
    public int layer;
    public ArrayList<String> collisionSide;
    boolean animated;

    public TileData(boolean collision, int layer, ArrayList<String> collisionSide,Boolean animated,int id){
        this.collision = collision;
        this.layer = layer;
        this.collisionSide = collisionSide;
        this.animated = animated;
        this.id = id;

    }

}
