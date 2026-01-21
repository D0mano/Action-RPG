package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public int speed;

    public BufferedImage up,down,left,right;
    public String direction;


    public Rectangle solidArea;
    public int solideAreaDefaultX, solideAreaDefaultY;
    public boolean collisionOn = false;

    // CHARACTER SETTINGS
    public int maxHealth;
    public int health;
}
