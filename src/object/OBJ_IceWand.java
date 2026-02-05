package object;


import entity.Projectile;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_IceWand extends SuperObject{
    GamePanel gp;

    public OBJ_IceWand(GamePanel gp) {
        super(gp);
        this.gp = gp;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        name = "ice_wand";
        objectType = equipment;
        soundEffectIndex = 32;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ice_wand.png"));
            image = uTool.scaleImage(image, gp.tileSize,  gp.tileSize);

        }catch(IOException e){
            e.printStackTrace();}
    }
    public void use(){
        Projectile projectile = new IceBall(gp);
        gp.player.projectile = projectile;
        gp.player.shootProjectile();
        gp.player.consumeMana(projectile.useCost);
    }

}
