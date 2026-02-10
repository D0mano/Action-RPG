package object;

import main.Animator;
import main.GamePanel;


public class OBJ_Shield extends SuperObject
{
	public OBJ_Shield(GamePanel gp) {
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "shield";
        objectType = gear;
        image = setup("shield", gp.scale);
        up = setup("shield-Sheet",gp.scale);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,6,false);
        }
}
