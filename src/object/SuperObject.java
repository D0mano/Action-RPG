package object;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX,worldY;

    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solideAreaDefaultX = 0;
    public int solideAreaDefaultY = 0;
    public String[] dialogues = new String[20];

    UtilityTool uTool = new UtilityTool();

    public void reload(){}


    public void draw(Graphics2D g2d, GamePanel gp){

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if(((-gp.tileSize) <= screenX &&  screenX <= (gp.worldWidth + gp.tileSize)) &&
                ((- gp.tileSize) <= screenY &&  screenY <= (gp.worldHeight + gp.tileSize))){
            g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize , null);

        }

    }

    public void speak(int i) {
    }
}


