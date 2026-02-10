package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_RedFruit extends SuperObject{
    GamePanel gp;
    public OBJ_RedFruit(GamePanel gp) {
        super(gp);
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "red fruit";
        objectType = singleUse;
        soundEffectIndex = 41;
        this.gp = gp;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/red_fruit.png"));
            image = uTool.scaleImage(image, gp.tileSize,  gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public boolean use(){
        if (gp.player.health < gp.player.maxHealth){
            gp.player.heal(20);
            return true;
        }
        return false;
    }
}
