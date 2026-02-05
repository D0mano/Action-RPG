package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Shield extends SuperObject
{
	public OBJ_Shield(GamePanel gp) {
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "shield";
        objectType = gear;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/shield.png"));
            image = uTool.scaleImage(image, gp.tileSize,  gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}}
}
