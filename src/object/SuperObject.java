package object;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject extends Entity {
    public GamePanel gp;
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX,worldY;
    public int objectType;
    public final int props = 3;
    public final int gear = 0;
    public final int singleUse = 1;
    public final int equipment = 2;

    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solideAreaDefaultX = 0;
    public int solideAreaDefaultY = 0;
    public String[] dialogues = new String[20];

    public int soundEffectIndex;

    UtilityTool uTool = new UtilityTool();

    public SuperObject(GamePanel gp) {
        super(gp);
        this.gp = gp;
    }

    public void reload(){
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        image = uTool.scaleImage(image,gp.tileSize,gp.tileSize);


    }


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
    public void use(){}
}


