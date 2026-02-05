package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Projectile extends Entity{
    Entity user;
    public int freezeTime;
    public Projectile(GamePanel gp) {
        super(gp);
    }
    public void reload(){

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
        UtilityTool uTool = new UtilityTool();

        if (user == gp.player){
            int monsterIndex = gp.collisionChecker.checkEntity(this,gp.monster);
            if (monsterIndex != 999){
                if(gp.monster.get(monsterIndex).entityStatus == parrying && gp.monster.get(monsterIndex).direction.equals(uTool.oppositeDirection(direction))){
                    gp.monster.get(monsterIndex).takeDamage(0);
                    gp.playSoundEffect(16);

                }else if (Objects.equals(name, "FireBall")){
                    gp.monster.get(monsterIndex).takeDamage(attackPower);
                    gp.playSoundEffect(32);
                    knockBack(gp.monster.get(monsterIndex),5);
                }
                else if (Objects.equals(name, "IceBall")){
                    gp.monster.get(monsterIndex).freezingTimer = freezeTime;
                    gp.monster.get(monsterIndex).entityStatus = freezing;

                }

                alive = false;
            }

        }else{
            collisionOn = false;
            gp.collisionChecker.checkPlayer(this);
            if (collisionOn){
                if (gp.player.entityStatus == parrying && gp.player.direction.equals(uTool.oppositeDirection(direction))){
                    gp.player.takeDamage(attackPower/4);
                    gp.playSoundEffect(16);
                }else{
                    gp.player.takeDamage(attackPower);
                    gp.playSoundEffect(15);
                }
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

        worldCol = worldX/gp.tileSize;
        worldRow = worldY/gp.tileSize;
    }


}
