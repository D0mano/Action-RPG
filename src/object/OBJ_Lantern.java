package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Lantern extends SuperObject {
    GamePanel gp;

	public OBJ_Lantern(GamePanel gp){
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "lantern";
        objectType = gear;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/lantern.png"));
            image = uTool.scaleImage(image, gp.tileSize,  gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}
    }
}
