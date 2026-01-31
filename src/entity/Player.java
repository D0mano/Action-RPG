package entity;
import main.Animator;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.FireBall;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
public class Player extends Entity {
    KeyHandler keyH;
    int hasKey = 0;


    public int maxEndurance;
    public int endurance;
    public int enduranceCost;
    public float displayedEndurance = 0;

    public float displayedMana = 0;



    public boolean useEndurance = false;
    public int enduranceCounter;
    public int enduranceDuration;

    int rollCounter ;
    int rollDuration;
    int rollSpeed;

    int parryingSpeed;


    // ANIMATION

    Animator downRollAnimator,upRollAnimator,leftRollAnimator,rightRollAnimator;
    Animator downIdleAnimator,upIdleAnimator,leftIdleAnimator,rightIdleAnimator;
    Animator downParryAnimator,upParryAnimator,leftParryAnimator,rightParryAnimator;
    public float displayedHealth = health;

    public  int screenX,screenY;

    public BufferedImage downRoll,upRoll,leftRoll,rightRoll;
    public BufferedImage downIdle,upIdle,leftIdle,rightIdle;
    public BufferedImage downParry,upParry,leftParry,rightParry;

    // Key Flags
    public boolean attackKeyProcessed=false;
    public boolean healKeyProcessed = false;
    public boolean parryKeyProcessed = false;
    public boolean magicAttackKeyProcessed = false;
    public boolean interactionKeyProcessed= false;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        setDefaultsValues();
        getPlayerImage();
        projectile = new FireBall(gp);



        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 6;
        solidArea.y = gp.tileSize / 3;
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;
        solidArea.width = (2 * gp.tileSize) / 3;
        solidArea.height = (2 * gp.tileSize) / 3;

        attackingAreaVertical.x = gp.tileSize/ 8;
        attackingAreaVertical.y = 0;
        attackingAreaDefaultVX = attackingAreaVertical.x;
        attackingAreaDefaultVY = attackingAreaVertical.y;
        attackingAreaVertical.width = (int)(gp.tileSize * 3/4f);
        attackingAreaVertical.height = gp.tileSize;

        attackingAreaHorizontal.x =  attackingAreaVertical.y;
        attackingAreaHorizontal.y = attackingAreaVertical.x;
        attackingAreaDefaultHX = attackingAreaHorizontal.x;
        attackingAreaDefaultHY = attackingAreaHorizontal.y;
        attackingAreaHorizontal.width = attackingAreaVertical.height;
        attackingAreaHorizontal.height = attackingAreaVertical.width;

        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,12,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,12,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,12,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,12,true);

        downRollAnimator = new Animator(downRoll,gp.tileSize,gp.tileSize,6,true);
        upRollAnimator = new Animator(upRoll,gp.tileSize,gp.tileSize,6,true);
        leftRollAnimator = new Animator(leftRoll,gp.tileSize,gp.tileSize,6,true);
        rightRollAnimator = new Animator(rightRoll,gp.tileSize,gp.tileSize,6,true);

        downIdleAnimator = new Animator(downIdle,gp.tileSize,gp.tileSize,10,true);
        upIdleAnimator = new Animator(upIdle,gp.tileSize,gp.tileSize,10,true);
        leftIdleAnimator = new Animator(leftIdle,gp.tileSize,gp.tileSize,10,true);
        rightIdleAnimator = new Animator(rightIdle,gp.tileSize,gp.tileSize,10,true);

        downAttackingAnimator = new Animator(downAttacking,gp.tileSize*2,gp.tileSize*2,10,false);
        upAttackingAnimator = new Animator(upAttacking,gp.tileSize*2,gp.tileSize*2,10,false);
        leftAttackingAnimator = new Animator(leftAttacking,gp.tileSize*2,gp.tileSize*2,10,false);
        rightAttackingAnimator = new Animator(rightAttacking,gp.tileSize*2,gp.tileSize*2,10,false);

        downParryAnimator = new Animator(downParry,gp.tileSize,gp.tileSize,12,true);
        upParryAnimator = new Animator(upParry,gp.tileSize,gp.tileSize,12,true);
        leftParryAnimator = new Animator(leftParry,gp.tileSize,gp.tileSize,12,true);
        rightParryAnimator = new Animator(rightParry,gp.tileSize,gp.tileSize,12,true);



    }

    public void setDefaultsValues(){
        worldX = gp.tileSize*49;
        worldY = gp.tileSize*66;
        normalSpeed = gp.tileSize/10;
        speed = normalSpeed;
        parryingSpeed = normalSpeed/2;
        direction = "down";
        attackPower = 30;

        //PLAYER STATUS
        maxHealth = 100;
        health = 100;
        maxMana = 100;
        mana = 100;

        maxPotion = 3;
        potionNotUsed = maxPotion;

        maxEndurance = 100;
        endurance = 100;
        enduranceDuration = 120;
        enduranceCounter = 0;
        enduranceCost = 40;


        entityStatus = idle;
        rollCounter = 0;
        rollDuration = 30;
        rollSpeed = 2*normalSpeed;

        attackCounter = 0;
        attackDuration = 40;
        invisibleTimer = 15;

        deathSoundIndex = 10;



    }

    public void getPlayerImage(){

        up = setup("walking/player_up-Sheet",gp.scale);
        down = setup("walking/player_down-Sheet",gp.scale);
        left = setup("walking/player_left-Sheet",gp.scale);
        right = setup("walking/player_right-Sheet",gp.scale);

        downRoll = setup("rolling/player_down-roll-Sheet",gp.scale);
        upRoll = setup("rolling/player_up-roll-Sheet",gp.scale);
        leftRoll = setup("rolling/player_left-roll-Sheet",gp.scale);
        rightRoll = setup("rolling/player_right-roll-Sheet",gp.scale);

        downIdle = setup("idling/player_down-idle-Sheet",gp.scale);
        upIdle = setup("idling/player_up-idle-Sheet",gp.scale);
        leftIdle = setup("idling/player_left-idle-Sheet",gp.scale);
        rightIdle = setup("idling/player_right-idle-Sheet",gp.scale);

        downAttacking = setup("attacking/player_down-slash-Sheet",gp.scale);
        upAttacking = setup("attacking/player_up-slash-Sheet",gp.scale);
        leftAttacking = setup("attacking/player_left-slash-Sheet",gp.scale);
        rightAttacking = setup("attacking/player_right-slash-Sheet",gp.scale);

        downParry = setup("blocking/player_down-shield-Sheet",gp.scale);
        upParry = setup("blocking/player_up-shield-Sheet",gp.scale);
        leftParry = setup("blocking/player_left-shield-Sheet",gp.scale);
        rightParry = setup("blocking/player_right-shield-Sheet",gp.scale);
    }
    public BufferedImage setup(String imageName,int scale){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/player/"+imageName+".png"));
            image = uTool.scaleImage(image, image.getWidth()*scale,image.getHeight()*scale);

        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }


    public void update() {

        displayedHealth += (health-displayedHealth)*0.15f;
        displayedEndurance += (endurance-displayedEndurance)*0.15f;
        displayedMana += (mana-displayedMana)*0.15f;

        if (entityStatus == attacking){

            attackCounter++;

            switch (direction) {
                case "up": upAttackingAnimator.update();break;
                case "down": downAttackingAnimator.update();break;
                case "left": leftAttackingAnimator.update();break;
                case "right": rightAttackingAnimator.update();break;
            }
            if (attackCounter >= attackDuration){
                attackCounter = 0;
                entityStatus = idle;
            }

            return;
        }

        if (entityStatus == rolling){

            collisionOn = false;
            // CHECK TILE COLLISION
            gp.collisionChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            gp.collisionChecker.checkObject(this,true);

            //CHECK MONSTER COLLISION
            gp.collisionChecker.checkEntity(this,gp.monster);

            useEndurance = true;
            rollCounter ++;
            if (!collisionOn) {

                switch (direction) {
                    case "up":
                        if (worldY - speed > 0) {
                            worldY -= speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2) && worldY < gp.worldHeight)) {
                                screenY -= speed;

                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                            upRollAnimator.update();

                        }
                        break;
                    case "down":
                        if ((worldY + gp.tileSize) + speed < gp.worldHeight) {
                            worldY += speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2))) {
                                screenY += speed;

                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                            downRollAnimator.update();
                        }
                        break;
                    case "left":

                        if (worldX - speed > 0) {
                            worldX -= speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX -= speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                            leftRollAnimator.update();
                        }
                        break;
                    case "right":
                        if (worldX + gp.tileSize + speed < gp.worldWidth) {
                            worldX += speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX += speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                            rightRollAnimator.update();
                        }
                        break;
                }
            }
            else{
                switch (direction) {
                    case "up": upRollAnimator.update();break;
                    case "down": downRollAnimator.update();break;
                    case "left": leftRollAnimator.update();break;
                    case "right": rightRollAnimator.update();break;
                }
            }
            if (invisibleCounter >= invisibleTimer){
                invisible = false;
            }else{
                invisible = true;
                invisibleCounter++;
            }
            if (rollCounter > rollDuration) {
                entityStatus = idle;
                rollCounter = 0;
                speed = normalSpeed; // Restaurer la vitesse
                invisibleCounter = 0;
            }


            return;

        }

        if (useEndurance) {
            enduranceCounter++;
            if (enduranceCounter > enduranceDuration) {
                enduranceCounter = 0;
                useEndurance = false;
            }
        }else{
            rechargeEndurance(1);
        }
        if (entityStatus == walking){

            if (keyH.upPressed) {
                direction = "up";
                upAnimator.update();
            }
            else if (keyH.downPressed) {
                direction = "down";
                downAnimator.update();
            }
            else if (keyH.leftPressed) {
                direction = "left";
                leftAnimator.update();
            }
            else if (keyH.rightPressed) {
                direction = "right";
                rightAnimator.update();
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            gp.collisionChecker.checkObject(this,true);

            // CHECK MONSTER COLLISION
            gp.collisionChecker.checkEntity(this,gp.monster);


            //IF COLLISION IS FALSE PLAYER CAN MOVE
            if (!collisionOn) {

                switch (direction) {
                    case "up":
                        if (worldY - speed > 0) {
                            worldY -= speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2) && worldY < gp.worldHeight)) {
                                screenY -= speed;

                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }

                        }
                        break;
                    case "down":
                        if ((worldY + gp.tileSize) + speed < gp.worldHeight) {
                            worldY += speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2))) {
                                screenY += speed;

                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                    case "left":
                        if (worldX - speed > 0) {
                            worldX -= speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX -= speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                    case "right":
                        if (worldX + gp.tileSize + speed < gp.worldWidth) {
                            worldX += speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX += speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                }
            }
        }

        if (entityStatus == idle) {
            switch (direction) {
                case "up": upIdleAnimator.update();break;
                case "down": downIdleAnimator.update();break;
                case "left": leftIdleAnimator.update();break;
                case "right": rightIdleAnimator.update();break;
            }
        }

        if (entityStatus == parrying){
            useEndurance = true;
            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

                if (keyH.upPressed) {
                    direction = "up";
                    upParryAnimator.update();
                } else if (keyH.downPressed) {
                    direction = "down";
                    downParryAnimator.update();
                } else if (keyH.leftPressed) {
                    direction = "left";
                    leftParryAnimator.update();
                } else if (keyH.rightPressed) {
                    direction = "right";
                    rightParryAnimator.update();
                }

                // CHECK TILE COLLISION
                collisionOn = false;
                gp.collisionChecker.checkTile(this);

                //CHECK OBJECT COLLISION
                gp.collisionChecker.checkObject(this, true);

                // CHECK MONSTER COLLISION
                gp.collisionChecker.checkEntity(this, gp.monster);


                //IF COLLISION IS FALSE PLAYER CAN MOVE
                if (!collisionOn) {

                    switch (direction) {
                        case "up":
                            if (worldY - speed > 0) {
                                worldY -= speed;
                                if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2) && worldY < gp.worldHeight)) {
                                    screenY -= speed;

                                } else {
                                    screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                        case "down":
                            if ((worldY + gp.tileSize) + speed < gp.worldHeight) {
                                worldY += speed;
                                if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2))) {
                                    screenY += speed;

                                } else {
                                    screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                        case "left":
                            if (worldX - speed > 0) {
                                worldX -= speed;
                                if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                    screenX -= speed;
                                } else {
                                    screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                        case "right":
                            if (worldX + gp.tileSize + speed < gp.worldWidth) {
                                worldX += speed;
                                if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                    screenX += speed;
                                } else {
                                    screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                    }
                }
            }



        }



        //CHECK OBJECT COLLISION
        int objIndex = gp.collisionChecker.checkObject(this,true);

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            entityStatus = walking;
        } else{entityStatus = idle;}

        if (keyH.parryPressed){
            if (endurance > 20) {

                entityStatus = parrying;
                speed = parryingSpeed;
                if (!parryKeyProcessed){gp.playSoundEffect(30);parryKeyProcessed=true;}
            }


        } else if (entityStatus != rolling){speed = normalSpeed; parryKeyProcessed=false;}
        else{parryKeyProcessed=false;}

        if (keyH.attackPressed){
            if (!attackKeyProcessed){
                gp.playSoundEffect(17);
                attack();
                attackKeyProcessed = true;
            }
        }else{attackKeyProcessed = false;}

        if(keyH.healPressed){
            if (!healKeyProcessed){
                heal(20);
                healKeyProcessed = true;
            }

        }else{healKeyProcessed = false;}

        if(keyH.interactionPressed){
            if (!interactionKeyProcessed){
                pickUpObject(objIndex);
                interactionKeyProcessed=true;
            }
        }else{interactionKeyProcessed = false;}

        if (keyH.spacePressed){
            if (endurance > 0){
                switch (direction) {
                    case "up":gp.playSoundEffect(12); break;
                    case "down": gp.playSoundEffect(11);break;
                    case "left":gp.playSoundEffect(13);break;
                    case "right":gp.playSoundEffect(14);break;
                }
                consumeEndurance(enduranceCost);
                entityStatus = rolling;
                speed = rollSpeed;
            }

        }

        if (keyH.magicAttackPressed){
            if (!magicAttackKeyProcessed){
                shotProjectile();
                consumeMana(projectile.useCost);
                magicAttackKeyProcessed = true;
            }
        }else{magicAttackKeyProcessed = false;}



    }

    public void pickUpObject(int index){
        if (index != 999){


            String objName = gp.obj[index].name;
            switch (objName) {
                case "key":
                    gp.playSoundEffect(2);
                    gp.ui.currentDialogue = "You pick up a key !";
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.dialogueState;
                    gp.obj[index] = null;
                    hasKey++;
                    break;
                case "door":
                    if (hasKey > 0){
                        gp.obj[index] = null;
                        hasKey--;
                    }else{
                        gp.playSoundEffect(2);
                        gp.ui.currentDialogue = "Doors is locked !";
                        gp.previousState = gp.gameState;
                        gp.gameState = gp.dialogueState;

                    }
                    break;

            }
        }
    }

    public void draw(Graphics2D g2d) {
        // g2d.setColor(Color.white);
        // g2d.fillRect(x, y,gp.tileSize , gp.tileSize);

        switch (direction) {
            case "up":
                if (entityStatus == rolling) {
                    upRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);

                }else if (entityStatus == walking) {
                    upAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == idle) {
                    upIdleAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if(entityStatus == attacking) {
                    upAttackingAnimator.draw(g2d,screenX-gp.tileSize,screenY-gp.tileSize,gp.tileSize*2,gp.tileSize*2);
                }else if(entityStatus == parrying) {
                    upParryAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }
                break;
            case "down":
                if (entityStatus == walking) {
                    downAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == rolling) {
                    downRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == idle) {
                    downIdleAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == attacking) {
                    downAttackingAnimator.draw(g2d,screenX,screenY,gp.tileSize*2,gp.tileSize*2);
                }else if(entityStatus == parrying) {
                    downParryAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }

                break;
            case "left":
                if (entityStatus == rolling) {
                    leftRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == walking) {
                    leftAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == idle) {
                    leftIdleAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == attacking) {
                    leftAttackingAnimator.draw(g2d,screenX-gp.tileSize,screenY-gp.tileSize,gp.tileSize*2,gp.tileSize*2);
                }else if (entityStatus == parrying) {
                    leftParryAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }
                break;
            case "right":
                if (entityStatus == rolling) {
                    rightRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);

                }else if (entityStatus == walking) {
                    rightAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == idle) {
                    rightIdleAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (entityStatus == attacking) {
                    rightAttackingAnimator.draw(g2d,screenX,screenY-gp.tileSize,gp.tileSize*2,gp.tileSize*2);
                }else if (entityStatus == parrying) {
                    rightParryAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }
                break;

        }


    }


    public void consumeEndurance(int amount){
        if(endurance-amount > 0){
            endurance -= amount;
        }
        else{endurance=0;}

    }

    public void rechargeEndurance(int amount){
        if(endurance+amount < maxEndurance){
            endurance += amount;
        }else{
            endurance = maxEndurance;}
    }

    public void consumeMana(int amount){
        if(mana-amount > 0){
            mana -= amount;
        }
        else{mana=0;}

    }

    public void rechargeMana(int amount){
        if(mana+amount < maxMana){
            mana += amount;
        }else{
            mana = maxMana;}
    }

    public void showHitbox(Graphics2D g2){

        g2.setColor(Color.RED);
        g2.drawRect(screenX+solidArea.x,screenY+solidArea.y,solidArea.width,solidArea.height);
        if (invisible) {
            g2.setColor(Color.blue);
            g2.drawRect(screenX+solidArea.x,screenY+solidArea.y,solidArea.width,solidArea.height);

        }
        if (entityStatus == attacking) {

            g2.setColor(Color.GREEN);

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

            g2.drawRect(screenX+attackingArea.x,screenY+attackingArea.y,attackingArea.width,attackingArea.height);
            attackingAreaHorizontal.x = attackingAreaDefaultHX;
            attackingAreaHorizontal.y = attackingAreaDefaultHY;
            attackingAreaVertical.x = attackingAreaDefaultVX;
            attackingAreaVertical.y = attackingAreaDefaultVY;


        }


    }

    public void attack(){
        cutBush(gp.collisionChecker.checkCanCut(this));
        UtilityTool uTool = new UtilityTool();
        upAttackingAnimator.resetAnimation();
        downAttackingAnimator.resetAnimation();
        leftAttackingAnimator.resetAnimation();
        rightAttackingAnimator.resetAnimation();
        entityStatus = attacking;
        int monsterHit = 0;
        for(Entity e : gp.monster){
            if (e != null){
                hitOn = false;
                gp.collisionChecker.checkAttack(this ,e);
                if(hitOn){
                    if(e.entityStatus == parrying && e.direction.equals(uTool.oppositeDirection(direction))){
                        e.takeDamage(0);
                        gp.playSoundEffect(16);
                    }else{
                        e.takeDamage(attackPower);
                        monsterHit++;
                    }
                    knockBack(e,10);
                }
            }
        }
        if (monsterHit > 0){
            gp.playSoundEffect(8);
        }
        hitOn = false;

    }

    public void cutBush(List<Point> bushes){
        if (!bushes.isEmpty()){
            for (Point p : bushes){
                gp.tileM.mapTileNum1[p.y][p.x] = 16;
            }
        }
    }

}
