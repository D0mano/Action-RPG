package tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public int layer = 1;
    public ArrayList<String>  collisionSide;
}
