package object;

import main.Animator;
import main.GamePanel;


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
        image = setup("sword", gp.scale);
        up = setup("sword-Sheet",gp.scale);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,6,false);
    }

    public boolean use(){
        gp.player.attack();
        return true;
    }


}
