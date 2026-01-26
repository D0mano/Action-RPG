package main;

import entity.Entity;
import monster.MON_Blob;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(){
//        gp.obj[0] = new OBJ_Key(gp);
//        gp.obj[0].worldX = 7 * gp.tileSize;
//        gp.obj[0].worldY = 15 * gp.tileSize;
//
//        gp.obj[1] = new OBJ_Key(gp);
//        gp.obj[1].worldX = 24 * gp.tileSize;
//        gp.obj[1].worldY = 38 * gp.tileSize;
//
//        gp.obj[2] = new OBJ_Door(gp);
//        gp.obj[2].worldX = 3 * gp.tileSize;
//        gp.obj[2].worldY = 39 * gp.tileSize;
//
//
//        gp.obj[3] = new OBJ_Door(gp);
//        gp.obj[3].worldX = 5 * gp.tileSize;
//        gp.obj[3].worldY = 34 * gp.tileSize;
//
//        gp.obj[4] = new OBJ_Chest(gp);
//        gp.obj[4].worldX = 4 * gp.tileSize;
//        gp.obj[4].worldY = 31 * gp.tileSize;
    }

    public void setMonster(){

        Entity monster = new MON_Blob(gp);
        monster.worldX = 40 * gp.tileSize;
        monster.worldY = 27 * gp.tileSize;
        gp.monster.add(monster);

        Entity monster2 = new MON_Blob(gp);
        monster2 .worldX = 49 * gp.tileSize;
        monster2 .worldY = 16 * gp.tileSize;
        gp.monster.add(monster2);

        Entity monster3 = new MON_Blob(gp);
        monster3.worldX = 37 * gp.tileSize;
        monster3.worldY = 44 * gp.tileSize;
        gp.monster.add(monster3);

    }
}
