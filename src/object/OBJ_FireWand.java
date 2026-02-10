package object;

import entity.Projectile;
import main.Animator;
import main.GamePanel;


public class OBJ_FireWand  extends SuperObject{
    GamePanel gp;
    public OBJ_FireWand(GamePanel gp) {
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "fire wand";
        objectType = equipment;
        soundEffectIndex = 32;
        image = setup("fire_wand", gp.scale);
        up = setup("fire_wand-Sheet",gp.scale);
        upAnimator = new Animator(up,gp.tileSize,gp.tileSize,6,false);

    }
    public boolean use(){
        if (gp.player.projectile.alive){
            return false;
        }
        Projectile projectile = new FireBall(gp);
        gp.player.projectile = projectile;
        gp.player.shootProjectile();
        gp.player.consumeMana(projectile.useCost);
        return projectile.alive;
    }
}
