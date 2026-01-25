package entity;

import main.Animator;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public GamePanel gp;
    public String name;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up, down, left, right;
    public Animator downAnimator, upAnimator, leftAnimator, rightAnimator;
    public String direction;


    public Rectangle solidArea;
    public int solideAreaDefaultX, solideAreaDefaultY;
    public boolean collisionOn = false;

    public Rectangle attackingArea = new Rectangle(0,0,0,0);
    public int attackingAreaDefaultHX, attackingAreaDefaultHY,attackingAreaDefaultVX, attackingAreaDefaultVY;
    public Rectangle attackingAreaHorizontal = new Rectangle(0,0,0,0);
    public Rectangle attackingAreaVertical = new Rectangle(0,0,0,0);

    public boolean hitOn = false;

    // CHARACTER SETTINGS
    public int maxHealth;
    public int health;

    public int actionLockCounter = 0;
    public boolean invisible = false;
    public int invisibleCounter = 0;
    public int invisibleTimer;

    public Entity(GamePanel gp) {
        this.gp = gp;
        direction = "down";
    }

    public void setAction() {
    }

    public void update() {
        setAction();
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);
        gp.collisionChecker.checkEntity(this ,gp.monster);
        if (!collisionOn) {
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
        } else {
            upAnimator.resetAnimation();
            downAnimator.resetAnimation();
            leftAnimator.resetAnimation();
            rightAnimator.resetAnimation();

        }
    }

    public void draw(Graphics2D g) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (((-gp.tileSize) <= screenX && screenX <= (gp.worldWidth + gp.tileSize)) &&
                ((-gp.tileSize) <= screenY && screenY <= (gp.worldHeight + gp.tileSize))) {
            switch (direction) {
                case "up":
                    upAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    break;
                case "down":
                    downAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    break;
                case "left":
                    leftAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    break;
                case "right":
                    rightAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    break;


            }

            if (gp.debugMode) {
                g.setColor(Color.RED);
                g.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }


        }

    }

    public int getY(){return worldY;}
}



