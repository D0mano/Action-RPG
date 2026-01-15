package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Door extends SuperObject{
    GamePanel gp;

    public OBJ_Door(GamePanel gp) {
        name = "door";
        this.gp = gp;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/doors.png"));
            image = uTool.scaleImage(image,gp.tileSize,gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}
        collision = true;
    }

}
