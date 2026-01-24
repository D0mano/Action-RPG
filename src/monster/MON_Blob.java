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




    public MON_Blob(GamePanel gp) {
        super(gp);
        name = "Blob";
        speed = 1;

        maxHealth = 20;
        health = maxHealth;

        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = gp.tileSize / 8;
        solidArea.width = gp.tileSize;
        solidArea.height = (int)(gp.tileSize * (7/8f));
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;


        getImage();


        downAnimator = new Animator(down,gp.tileSize,gp.tileSize,12,true);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,12,true);
        leftAnimator = new Animator(left,gp.tileSize,gp.tileSize,12,true);
        rightAnimator = new Animator(right,gp.tileSize,gp.tileSize,12,true);

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
        up = setup("blob_up-Sheet",gp.scale);
        down = setup("blob_down-Sheet",gp.scale);
        left = setup("blob_left-Sheet",gp.scale);
        right = setup("blob_right-Sheet",gp.scale);

    }
    public void setAction(){
        actionLockCounter++;
        if (actionLockCounter == 120){
            Random rand = new Random();
            int i = rand.nextInt(100)+1;

            if ( i <=25){
                direction = "up";
            }else if (i <= 50){
                direction = "down";
            }else if (i <= 75){
                direction = "left";
            }else{
                direction = "right";
           }
            actionLockCounter = 0;
        }
    }

}
