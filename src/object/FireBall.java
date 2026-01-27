package object;

import entity.Projectile;
import main.Animator;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FireBall extends Projectile {
    GamePanel gp;
    public FireBall(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "FireBall";
        normalSpeed = gp.tileSize/5;
        speed = normalSpeed;
        maxHealth = 80;
        attackPower = 10;
        health = maxHealth;
        useCost = 10;
        alive =  false;
        getImage();
        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,12,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,12,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,12,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,12,true);

    }

    public void getImage(){
        down = setup("fireball_down-Sheet",gp.scale);
        up = setup("fireball_up-Sheet",gp.scale);
        left = setup("fireball_left-Sheet",gp.scale);
        right = setup("fireball_right-Sheet",gp.scale);

    }


}
