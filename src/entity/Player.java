package entity;
import main.Animator;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    int hasKey = 0;
    int playerStatus;
    final int idle = 0;
    final int walking = 1;
    final int rolling = 2;

    public int maxEndurance;
    public int endurance;
    public float displayedEndurance = 0;

    public boolean useEndurance = false;
    public int enduranceCounter;
    public int enduranceDuration;

    int rollCounter ;
    int rollDuration;
    int rollSpeed;
    int normalSpeed;

    Animator downAnimator,upAnimator,leftAnimator,rightAnimator;
    Animator downRollAnimator,upRollAnimator,leftRollAnimator,rightRollAnimator;
    public float displayedHealth = health;

    public  int screenX,screenY;

    public BufferedImage downRoll,upRoll,leftRoll,rightRoll;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        setDefaultsValues();
        getPlayerImage();




        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 6;
        solidArea.y = gp.tileSize / 3;
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;
        solidArea.width = (2 * gp.tileSize) / 3;
        solidArea.height = (2 * gp.tileSize) / 3;

        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,12,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,12,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,12,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,12,true);

        downRollAnimator = new Animator(downRoll,gp.tileSize,gp.tileSize,6,true);
        upRollAnimator = new Animator(upRoll,gp.tileSize,gp.tileSize,6,true);
        leftRollAnimator = new Animator(leftRoll,gp.tileSize,gp.tileSize,6,true);
        rightRollAnimator = new Animator(rightRoll,gp.tileSize,gp.tileSize,6,true);


    }

    public void setDefaultsValues(){
        worldX = gp.tileSize*19;
        worldY = gp.tileSize*12;
        speed = gp.tileSize/10;
        direction = "down";

        //PLAYER STATUS
        maxHealth = 100;
        health = 100;

        maxEndurance = 100;
        endurance = 100;
        enduranceDuration = 120;
        enduranceCounter = 0;


        playerStatus = walking;
        rollCounter = 0;
        rollDuration = 30;
        normalSpeed = speed;
        rollSpeed = 2*normalSpeed;

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
        // CHECK TILE COLLISION
        collisionOn = false;
        gp.collisionChecker.checkTile(this);

        if (playerStatus == rolling){
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
            if (rollCounter > rollDuration) {
                playerStatus = walking;
                rollCounter = 0;
                speed = normalSpeed; // Restaurer la vitesse
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
            rechargeEndurnace(1);
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

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
            else {
                direction = "right";
                rightAnimator.update();
            }




            //CHECK OBJECT COLLISION
            int objIndex = gp.collisionChecker.checkObject(this,true);
            pickUpObject(objIndex);
            // CHECK TILE COLLISION
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

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

        if (keyH.attackPressed){
            takeDamage(5);
        }
        if(keyH.healPressed){
            heal(5);
        }
        if (keyH.spacePressed){
            if (endurance > 0){
                consumeEndurance(40);
                playerStatus = rolling;
                speed = rollSpeed;
            }

        }

    }

    public void pickUpObject(int index){
        if (index != 999){
            String objName = gp.obj[index].name;
            switch (objName) {
                case "key":
                    gp.obj[index] = null;
                    hasKey++;
                    break;
                case "door":
                    if (hasKey > 0){
                        gp.obj[index] = null;
                        hasKey--;
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
                if (playerStatus == rolling) {
                    upRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);

                }else if (playerStatus == walking) {
                    upAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }
                break;
            case "down":
                if (playerStatus == walking) {
                    downAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (playerStatus == rolling) {
                    downRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }

                break;
            case "left":
                if (playerStatus == rolling) {
                    leftRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }else if (playerStatus == walking) {
                    leftAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }
                break;
            case "right":
                if (playerStatus == rolling) {
                    rightRollAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);

                }else if (playerStatus == walking) {
                    rightAnimator.draw(g2d,screenX,screenY,gp.tileSize,gp.tileSize);
                }
                break;

        }


    }

    public void takeDamage(int damage){
        if(health-damage > 0){
            health -= damage;
        }
        else{health=0;}
        System.out.println("health:"+health);
    }

    public void heal(int heal){
        if(health+heal < maxHealth){
            health += heal;
        }else{
            health = maxHealth;}
    }

    public void consumeEndurance(int amount){
        if(endurance-amount > 0){
            endurance -= amount;
        }
        else{endurance=0;}

    }

    public void rechargeEndurnace(int amount){
        if(endurance+amount < maxEndurance){
            endurance += amount;
        }else{
            endurance = maxEndurance;}
    }

    public void showHitbox(Graphics2D g2){
        g2.setColor(Color.RED);
        g2.drawRect(screenX+solidArea.x,screenY+solidArea.y,solidArea.width,solidArea.height);

    }
}
