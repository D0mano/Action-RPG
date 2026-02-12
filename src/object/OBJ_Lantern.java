package object;

import main.Animator;
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
        image = setup("lantern", gp.scale);
        up = setup("lantern-Sheet",gp.scale);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,6,false);

    }
}