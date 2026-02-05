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

public class MON_FoxZombie extends Entity {

    boolean shotTaken = false;
    int shotCounter = 0;
    int shotTimer = 90;

    public MON_FoxZombie(GamePanel gp, int worldCol, int worldRow){
        super(gp);
        name = "FoxZombie";
        normalSpeed = 1;
        speed = normalSpeed;
        attackPower = 20;
        this.worldCol = worldCol;
        this.worldRow = worldRow;
        worldX = worldCol * gp.tileSize;
        worldY = worldRow * gp.tileSize;
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

        downAttackingAnimator = new Animator(downAttacking,2*gp.tileSize,2*gp.tileSize,10,false);
        upAttackingAnimator = new Animator(upAttacking,2*gp.tileSize,2*gp.tileSize,10,true);
        leftAttackingAnimator = new Animator(leftAttacking,gp.tileSize*2,2*gp.tileSize,10,false);
        rightAttackingAnimator = new Animator(rightAttacking,gp.tileSize*2,2*gp.tileSize,10,false);

        projectile = new FireBall(gp);



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

        downAttackingAnimator.reload(downAttacking,2*gp.tileSize,2*gp.tileSize);
        upAttackingAnimator.reload(upAttacking,2*gp.tileSize,2*gp.tileSize);
        leftAttackingAnimator.reload(leftAttacking,gp.tileSize*2,2*gp.tileSize);
        rightAttackingAnimator.reload(rightAttacking,gp.tileSize*2,2*gp.tileSize);

    }

    public BufferedImage setup(String imageName, int scale){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/monsters/FoxZombie/"+imageName+".png"));
            image = uTool.scaleImage(image, image.getWidth()*scale,image.getHeight()*scale);

        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }
    public void getImage(){
        up = setup("walking/foxZombie_up-Sheet",gp.scale);
        down = setup("walking/foxZombie_down-Sheet",gp.scale);
        left = setup("walking/foxZombie_left-Sheet",gp.scale);
        right = setup("walking/foxZombie_right-Sheet",gp.scale);

        downAttacking = setup("attacking/foxZombie_down-slash-Sheet",gp.scale);
        upAttacking = setup("attacking/foxZombie_up-slash-Sheet",gp.scale);
        leftAttacking = setup("attacking/foxZombie_left-slash-Sheet",gp.scale);
        rightAttacking = setup("attacking/foxZombie_right-slash-Sheet",gp.scale);

    }
    public void setAction(){

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
                    shootProjectile();
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

    public void draw(Graphics2D g) {

        if (damageTaken){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        if (dying){
            dyingAnimation(g);
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (((-gp.tileSize) <= screenX && screenX <= (gp.worldWidth + gp.tileSize)) &&
                ((-gp.tileSize) <= screenY && screenY <= (gp.worldHeight + gp.tileSize))) {

            switch (direction) {
                case "up":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        upAnimator.draw(g, screenX, screenY, gp.tileSize,gp.tileSize);
                    }else if (entityStatus == attacking) {
                        upAttackingAnimator.draw(g,screenX-gp.tileSize,screenY-gp.tileSize,gp.tileSize*2,gp.tileSize*2);
                    }else if (entityStatus == freezing) {

                        drawFreezeOverlay(g,upAnimator.currentsprite,screenX,screenY,gp.tileSize,gp.tileSize);
                    }
                    break;
                case "down":
                    if (entityStatus == walking|| entityStatus == knockBacking) {
                        downAnimator.draw(g,screenX,screenY,gp.tileSize,gp.tileSize);
                    }else if (entityStatus == attacking) {
                        downAttackingAnimator.draw(g,screenX,screenY,gp.tileSize*2,gp.tileSize*2);
                    }else if (entityStatus == freezing) {

                        drawFreezeOverlay(g,downAnimator.currentsprite,screenX,screenY,gp.tileSize,gp.tileSize);
                    }
                    break;
                case "left":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        leftAnimator.draw(g,screenX,screenY,gp.tileSize,gp.tileSize);
                    }else if (entityStatus == attacking) {
                        leftAttackingAnimator.draw(g,screenX-gp.tileSize,screenY-gp.tileSize,gp.tileSize*2,gp.tileSize*2);
                    }else if (entityStatus == freezing) {

                        drawFreezeOverlay(g,leftAnimator.currentsprite,screenX,screenY,gp.tileSize,gp.tileSize);
                    }
                    break;
                case "right":
                    if (entityStatus == walking || entityStatus == knockBacking)  {
                        rightAnimator.draw(g,screenX,screenY,gp.tileSize,gp.tileSize);
                    }else if (entityStatus == attacking) {
                        rightAttackingAnimator.draw(g,screenX,screenY-gp.tileSize,gp.tileSize*2,gp.tileSize*2);
                    }else if (entityStatus == freezing) {

                        drawFreezeOverlay(g,rightAnimator.currentsprite,screenX,screenY,gp.tileSize,gp.tileSize);
                    }
                    break;


            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            if(hpBarOn){
                g.setColor(Color.black);
                g.fillRect(screenX-1, screenY-16, gp.tileSize+2, 7);
                g.setColor(new Color(250, 110, 150));
                g.fillRect(screenX, screenY-15, (int)(gp.tileSize* ((float)health/(float)maxHealth)), 5);
                hpBarCounter++;

                if (hpBarCounter > hpBarTimer) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }

            }



            if (gp.debugMode) {
                g.setColor(Color.RED);
                g.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
                if (entityStatus == attacking) {

                    g.setColor(Color.GREEN);

                    switch(direction){
                        case "up":
                            attackingArea = attackingAreaVertical;
                            attackingArea.y -= gp.tileSize;
                            break;
                        case "down":
                            attackingArea = attackingAreaVertical;
                            attackingArea.y += gp.tileSize;
                            break;
                        case "left":
                            attackingArea = attackingAreaHorizontal;
                            attackingArea.x -= gp.tileSize;
                            break;
                        case "right":
                            attackingArea = attackingAreaHorizontal;
                            attackingArea.x += gp.tileSize;
                            break;

                    }

                    g.drawRect(screenX+attackingArea.x,screenY+attackingArea.y,attackingArea.width,attackingArea.height);
                    attackingAreaHorizontal.x = attackingAreaDefaultHX;
                    attackingAreaHorizontal.y = attackingAreaDefaultHY;
                    attackingAreaVertical.x = attackingAreaDefaultVX;
                    attackingAreaVertical.y = attackingAreaDefaultVY;


                }
            }


        }

    }





}
