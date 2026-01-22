package main;

import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 7 * gp.tileSize;
        gp.obj[0].worldY = 15 * gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 24 * gp.tileSize;
        gp.obj[1].worldY = 38 * gp.tileSize;

        gp.obj[2] = new OBJ_Door(gp);
        gp.obj[2].worldX = 3 * gp.tileSize;
        gp.obj[2].worldY = 39 * gp.tileSize;


        gp.obj[3] = new OBJ_Door(gp);
        gp.obj[3].worldX = 5 * gp.tileSize;
        gp.obj[3].worldY = 34 * gp.tileSize;

        gp.obj[4] = new OBJ_Chest(gp);
        gp.obj[4].worldX = 4 * gp.tileSize;
        gp.obj[4].worldY = 31 * gp.tileSize;
    }
}
