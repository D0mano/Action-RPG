package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_BlueFruit  extends SuperObject{
    GamePanel gp;
    public OBJ_BlueFruit(GamePanel gp){
        super(gp);
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "blue fruit";
        objectType = singleUse;
        soundEffectIndex = 42;
        this.gp = gp;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/blue_fruit.png"));
            image = uTool.scaleImage(image, gp.tileSize,  gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}
    }

    public boolean use(){
        if (gp.player.mana < gp.player.maxMana){
            gp.player.rechargeMana(20);
            return true;
        }
        return false;

    }

}
