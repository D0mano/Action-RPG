package object;

import entity.Projectile;
import main.Animator;
import main.GamePanel;

import java.awt.*;

public class Hook extends Projectile {
    GamePanel gp;
    public Hook(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Hook";
        normalSpeed = gp.tileSize/5;
        speed = normalSpeed;
        maxHealth = 80;
        attackPower = 0;
        health = maxHealth;
        useCost = 10;
        alive =  false;
        getImage();
        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,6,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,6,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,6,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,6,true);

    }
    public void reload(){
        worldX = worldCol*gp.tileSize;
        worldY = worldRow*gp.tileSize;
        normalSpeed = gp.tileSize/5;
        speed = normalSpeed;
        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 4;
        solidArea.y = gp.tileSize / 4;
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;
        solidArea.width = gp.tileSize / 2;
        solidArea.height = gp.tileSize / 2;
        getImage();
        downAnimator.reload(down,gp.tileSize,gp.tileSize);
        upAnimator.reload(up,gp.tileSize,gp.tileSize);
        leftAnimator.reload(left,gp.tileSize,gp.tileSize);
        rightAnimator.reload(right,gp.tileSize,gp.tileSize);

    }

    public void getImage(){
        down = setup("hook_down-Sheet",gp.scale);
        up = setup("hook_up-Sheet",gp.scale);
        left = setup("hook_left-Sheet",gp.scale);
        right = setup("hook_right-Sheet",gp.scale);
        tailsV = setup("hook_tail-vertical",gp.scale);
        tailsH = setup("hook_tail-horizontal",gp.scale);

    }

    public void drawTrails(Graphics2D g){
        int worldX,worldY,width,height,screenX,screenY;

        switch (direction){
            case "up":
                worldX = user.worldX;
                worldY = user.worldY;
                width = gp.tileSize;
                height = this.worldY+gp.tileSize - worldY;
                screenX = worldX - gp.player.worldX + gp.player.screenX;
                screenY = worldY - gp.player.worldY + gp.player.screenY;
                g.drawImage(tailsV,screenX,screenY,width,height,null);


                break;
            case "down":
                worldX = user.worldX;
                worldY = user.worldY+gp.tileSize;
                width = gp.tileSize;
                height = this.worldY+gp.tileSize - worldY;
                screenX = worldX - gp.player.worldX + gp.player.screenX;
                screenY = worldY - gp.player.worldY + gp.player.screenY;
                g.drawImage(tailsV,screenX,screenY,width,height,null);
                break;
            case "left":
                worldX = user.worldX;
                worldY = user.worldY;
                width = this.worldX+gp.tileSize - worldX;
                height = gp.tileSize;
                screenX = worldX - gp.player.worldX + gp.player.screenX;
                screenY = worldY - gp.player.worldY + gp.player.screenY;
                g.drawImage(tailsH,screenX,screenY,width,height,null);
                break;
            case "right":
                worldX = user.worldX+gp.tileSize;
                worldY = user.worldY;
                width = this.worldX - worldX;
                height = gp.tileSize;
                screenX = worldX - gp.player.worldX + gp.player.screenX;
                screenY = worldY - gp.player.worldY + gp.player.screenY;
                g.drawImage(tailsH,screenX,screenY,width,height,null);
                break;
        }



    }


}
