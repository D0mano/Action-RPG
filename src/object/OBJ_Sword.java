package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Sword extends SuperObject {
    GamePanel gp;
    public OBJ_Sword(GamePanel gp){
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "sword";
        objectType = equipment;
        soundEffectIndex = 17;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/sword.png"));
            image = uTool.scaleImage(image, gp.tileSize,  gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}
    }

    public void use(){
        gp.player.attack();
    }


}
