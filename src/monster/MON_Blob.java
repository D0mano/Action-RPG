package monster;

import entity.Entity;
import main.Animator;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class MON_Blob  extends Entity {




    public MON_Blob(GamePanel gp,int worldCol,int worldRow) {
        super(gp);
        name = "Blob";
        normalSpeed = 1;
        speed = normalSpeed;
        attackPower = 10;
        this.worldCol = worldCol;
        this.worldRow = worldRow;
        worldX = worldCol*gp.tileSize;
        worldY = worldRow*gp.tileSize;

        maxHealth = 20;
        health = maxHealth;

        deathSoundIndex = 27;

        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = gp.tileSize / 8;
        solidArea.width = gp.tileSize;
        solidArea.height = (int)(gp.tileSize * (7/8f));
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;

        attackingAreaVertical.x = gp.tileSize/ 8;
        attackingAreaVertical.y = 0;
        attackingAreaDefaultVX = attackingAreaVertical.x;
        attackingAreaDefaultVY = attackingAreaVertical.y;
        attackingAreaVertical.width = (int)(gp.tileSize * 3/4f);
        attackingAreaVertical.height = (3 * gp.tileSize) / 4;

        attackingAreaHorizontal.x =  attackingAreaVertical.y;
        attackingAreaHorizontal.y = attackingAreaVertical.x;
        attackingAreaDefaultHX = attackingAreaHorizontal.x;
        attackingAreaDefaultHY = attackingAreaHorizontal.y;
        attackingAreaHorizontal.width = attackingAreaVertical.height;
        attackingAreaHorizontal.height = attackingAreaVertical.width;

        damageTakenTimer = 15;


        getImage();


        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,12,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,12,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,12,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,12,true);

        downAttackingAnimator = new Animator(downAttacking,gp.tileSize,2*gp.tileSize,10,false);
        upAttackingAnimator = new Animator(upAttacking,gp.tileSize,2*gp.tileSize,10,true);
        leftAttackingAnimator = new Animator(leftAttacking,gp.tileSize*2,gp.tileSize,10,false);
        rightAttackingAnimator = new Animator(rightAttacking,gp.tileSize*2,gp.tileSize,10,false);

    }
    public void reload(){
        worldX = worldCol*gp.tileSize;
        worldY = worldRow*gp.tileSize;
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = gp.tileSize / 8;
        solidArea.width = gp.tileSize;
        solidArea.height = (int)(gp.tileSize * (7/8f));
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;

        attackingAreaVertical.x = gp.tileSize/ 8;
        attackingAreaVertical.y = 0;
        attackingAreaDefaultVX = attackingAreaVertical.x;
        attackingAreaDefaultVY = attackingAreaVertical.y;
        attackingAreaVertical.width = (int)(gp.tileSize * 3/4f);
        attackingAreaVertical.height = (3 * gp.tileSize) / 4;

        attackingAreaHorizontal.x =  attackingAreaVertical.y;
        attackingAreaHorizontal.y = attackingAreaVertical.x;
        attackingAreaDefaultHX = attackingAreaHorizontal.x;
        attackingAreaDefaultHY = attackingAreaHorizontal.y;
        attackingAreaHorizontal.width = attackingAreaVertical.height;
        attackingAreaHorizontal.height = attackingAreaVertical.width;

        getImage();
        downAnimator.reload(down,gp.tileSize,gp.tileSize);
        upAnimator.reload(up,gp.tileSize,gp.tileSize);
        leftAnimator.reload(left,gp.tileSize,gp.tileSize);
        rightAnimator.reload(right,gp.tileSize,gp.tileSize);

        downAttackingAnimator.reload(downAttacking,gp.tileSize,2*gp.tileSize);
        upAttackingAnimator.reload(upAttacking,gp.tileSize,2*gp.tileSize);
        leftAttackingAnimator.reload(leftAttacking,gp.tileSize*2,gp.tileSize);
        rightAttackingAnimator.reload(rightAttacking,gp.tileSize*2,gp.tileSize);


    }

    public BufferedImage setup(String imageName, int scale){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/monsters/blob/"+imageName+".png"));
            image = uTool.scaleImage(image, image.getWidth()*scale,image.getHeight()*scale);

        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void getImage(){
        up = setup("walking/blob_up-Sheet",gp.scale);
        down = setup("walking/blob_down-Sheet",gp.scale);
        left = setup("walking/blob_left-Sheet",gp.scale);
        right = setup("walking/blob_right-Sheet",gp.scale);

        upAttacking = setup("attacking/blob_up-attack-Sheet",gp.scale);
        downAttacking = setup("attacking/blob_down-attack-Sheet",gp.scale);
        leftAttacking = setup("attacking/blob_left-attack-Sheet",gp.scale);
        rightAttacking =  setup("attacking/blob_right-attack-Sheet",gp.scale);

    }
    public void setAction(){
        int xDistance = Math.abs(worldX-gp.player.worldX);
        int yDistance = Math.abs(worldY-gp.player.worldY);
        int tileDistance = (xDistance+yDistance)/gp.tileSize;

        if(!onPath && tileDistance < 5){
            onPath = true;
        }
        if(onPath && tileDistance > 10){
            onPath = false;
            entityStatus = walking;

        }
        if (onPath){
            if (tileDistance <= 1 && canAttack){
               attack();
            }

            if (gp.player.worldX > worldX){
                direction = "right";
            }
            else if (gp.player.worldX < worldX){
                direction = "left";
            }
            if (gp.player.worldY > worldY){
                direction = "down";
            }else if (gp.player.worldY < worldY){
                direction = "up";
            }
        }else {

            actionLockCounter++;
            if (actionLockCounter >= actionLockTimer) {
                Random rand = new Random();
                int i = rand.nextInt(100) + 1;

                if (i <= 25) {
                    direction = "up";
                } else if (i <= 50) {
                    direction = "down";
                } else if (i <= 75) {
                    direction = "left";
                } else {
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }
    public void attack(){
        UtilityTool uTool = new UtilityTool();
        gp.playSoundEffect(26);
        upAttackingAnimator.resetAnimation();
        downAttackingAnimator.resetAnimation();
        leftAttackingAnimator.resetAnimation();
        rightAttackingAnimator.resetAnimation();
        entityStatus = attacking;
        hitOn = false;
        gp.collisionChecker.checkAttack(this , gp.player);
        if(hitOn && !gp.player.invisible){
            if(gp.player.entityStatus == parrying && gp.player.direction.equals(uTool.oppositeDirection(direction))){
                gp.player.takeDamage(attackPower/4);
                gp.playSoundEffect(16);
                gp.player.consumeEndurance(10);

            }else{
                gp.player.takeDamage(attackPower);
                gp.playSoundEffect(15);
            }

        }
        hitOn = false;
    }

}
