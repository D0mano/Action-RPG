package tile;

import main.Animator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tile {
    public int id;
    public BufferedImage image;
    public boolean collision = false;
    public int layer = 1;
    public ArrayList<String>  collisionSide;
    public Animator animation;
}
