package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_GrapplingHook extends SuperObject{
    GamePanel gp;
    public OBJ_GrapplingHook(GamePanel gp) {
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "grappling hook";
        objectType = equipment;
        soundEffectIndex = 32;
        image = setup("grappling_hook", gp.scale);
//        up = setup("grappling_hook-Sheet",gp.scale);
//        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,6,false);
    }

    public boolean use(){
        if (gp.player.projectile.alive){
            return false;
        }
        Projectile projectile = new Hook(gp);
        gp.player.projectile = projectile;
        gp.player.shootProjectile();
        gp.player.consumeMana(projectile.useCost);
        return projectile.alive;
    }
}
