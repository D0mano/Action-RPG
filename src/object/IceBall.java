package object;

import entity.Projectile;
import main.Animator;
import main.GamePanel;

import java.awt.*;

public class IceBall extends Projectile {
    GamePanel gp;
    public IceBall(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "IceBall";
        freezeTime = 120;
        normalSpeed = gp.tileSize/5;
        speed = normalSpeed;
        maxHealth = 80;
        attackPower = 5;
        health = maxHealth;
        useCost = 10;
        alive =  false;
        getImage();
        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,12,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,12,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,12,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,12,true);

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
        down = setup("iceball_down-Sheet",gp.scale);
        up = setup("iceball_up-Sheet",gp.scale);
        left = setup("iceball_left-Sheet",gp.scale);
        right = setup("iceball_right-Sheet",gp.scale);

    }


}
