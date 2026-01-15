package entity;
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

    public  int screenX,screenY;

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
    }

    public void setDefaultsValues(){
        worldX = gp.tileSize*10;
        worldY = gp.tileSize*10;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){

        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
    }

    public BufferedImage setup(String imageName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/player/"+imageName+".png"));
            image = uTool.scaleImage(image,gp.tileSize,gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void update() {

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                direction = "up";
            }
            else if (keyH.downPressed) {
                direction = "down";
            }
            else if (keyH.leftPressed) {
                direction = "left";
            }
            else {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.collisionChecker.checkObject(this,true);
            pickUpObject(objIndex);

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

            spriteCounter++;
            if(spriteCounter > 12){
                if(spriteNumber == 1){
                    spriteNumber = 2;
                }
                else if(spriteNumber == 2){
                    spriteNumber = 1;
                }
                spriteCounter = 0;
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

    public void draw(Graphics g2d) {
        // g2d.setColor(Color.white);
        // g2d.fillRect(x, y,gp.tileSize , gp.tileSize);


        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNumber == 1) {
                    image = up1;
                }
                if (spriteNumber == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNumber == 1) {
                    image = down1;
                }
                if (spriteNumber == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNumber == 1) {
                    image = left1;
                }
                if (spriteNumber == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNumber == 1) {
                    image = right1;
                }
                if (spriteNumber == 2) {
                    image = right2;
                }
                break;

        }
        g2d.drawImage(image, screenX, screenY,gp.tileSize, gp.tileSize,null);


    }
}
