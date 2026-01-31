package monster;

import entity.Entity;
import main.Animator;
import main.GamePanel;
import main.UtilityTool;
import object.FireBall;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class MON_Hedghog  extends Entity {

    boolean shotTaken = false;
    int shotCounter = 0;
    int shotTimer = 90;

    public MON_Hedghog(GamePanel gp){
        super(gp);
        name = "Hedghog";
        normalSpeed= 1;
        speed = normalSpeed;
        attackPower = 20;
        entityStatus = walking;
        maxHealth = 70;
        health = maxHealth;
        maxMana = 100;
        mana =maxMana;

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

        projectile = new FireBall(gp);



    }

    public BufferedImage setup(String imageName, int scale){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/monsters/Hedghog/"+imageName+".png"));
            image = uTool.scaleImage(image, image.getWidth()*scale,image.getHeight()*scale);

        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }
    public void getImage(){
        up = setup("walking/player_up-Sheet",gp.scale);
        down = setup("walking/player_down-Sheet",gp.scale);
        left = setup("walking/player_left-Sheet",gp.scale);
        right = setup("walking/player_right-Sheet",gp.scale);

        downAttacking = setup("attacking/player_down-slash-Sheet",gp.scale);
        upAttacking = setup("attacking/player_up-slash-Sheet",gp.scale);
        leftAttacking = setup("attacking/player_left-slash-Sheet",gp.scale);
        rightAttacking = setup("attacking/player_right-slash-Sheet",gp.scale);

    }
    public void setAction(){
        System.out.println(shotTaken);

        int xDistance = Math.abs(worldX-gp.player.worldX);
        int yDistance = Math.abs(worldY-gp.player.worldY);
        int playerRow = gp.player.worldY/gp.tileSize;
        int playerCol = gp.player.worldX/gp.tileSize;
        int entityCol = worldX/gp.tileSize;
        int entityRow = worldY/gp.tileSize;
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

            if (playerCol > entityCol){
                direction = "right";
            }
            else if (playerCol< entityCol){
                direction = "left";
            }
            if (playerRow > entityRow){
                direction = "down";
            }else if (gp.player.worldY < worldY){
                direction = "up";
            }
            if(playerCol == entityCol || playerRow == entityRow){
                if (!shotTaken){
                    shotProjectile();
                    shotTaken = true;
                }
            }
            if (shotTaken){
                shotCounter++;
                if(shotCounter>= shotTimer){
                    shotTaken = false;
                    shotCounter = 0;
                }
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
        System.out.println(hitOn);
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
