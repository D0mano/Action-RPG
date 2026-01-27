package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Projectile extends Entity{
    Entity user;
    public Projectile(GamePanel gp) {
        super(gp);
    }
    public BufferedImage setup(String imageName, int scale){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/projectiles/"+imageName+".png"));
            image = uTool.scaleImage(image, image.getWidth()*scale,image.getHeight()*scale);

        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }


    public void set(int worldX, int worldY,String direction,boolean alive ,Entity user){
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.health = this.maxHealth;
        this.entityStatus = walking;
        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 4;
        solidArea.y = gp.tileSize / 4;
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;
        solidArea.width = gp.tileSize / 2;
        solidArea.height = gp.tileSize / 2;
    }

    public void update(){

        if (user == gp.player){
            int monsterIndex = gp.collisionChecker.checkEntity(this,gp.monster);
            if (monsterIndex != 999){
                gp.monster.get(monsterIndex).takeDamage(attackPower);
                knockBack(gp.monster.get(monsterIndex),5);
                alive = false;
            }

        }else{
            collisionOn = false;
            gp.collisionChecker.checkPlayer(this);
            if (collisionOn){
                gp.player.takeDamage(attackPower);
                alive = false;
            }
        }

        switch (direction) {
                case "up":
                    worldY -= speed;
                    upAnimator.update();
                    break;
                case "down":
                    worldY += speed;
                    downAnimator.update();
                    break;
                case "left":
                    worldX -= speed;
                    leftAnimator.update();
                    break;
                case "right":
                    worldX += speed;
                    rightAnimator.update();
                    break;
            }


        health --;
        if (health <= 0) {
            alive = false;
        }
    }


}
