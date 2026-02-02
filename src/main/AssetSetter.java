package main;

import entity.Entity;
import monster.MON_Blob;
import monster.MON_FoxZombie;
import monster.MON_Rudeling;
import object.SuperObject;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void reload(){
        for (SuperObject obj : gp.obj){
            if (obj != null){
                obj.reload();
            }
        }
        for (Entity entity : gp.monster){
            if (entity != null){
                entity.reload();
            }
        }
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
        // Blob spawn
        createMonster("blob",52,32);
        createMonster("blob",34,27);
        createMonster("blob",75,32);
        createMonster("blob",80,27);

        //Hedghog spawn
        createMonster("foxZombie",16,25);
        createMonster("foxZombie",13,12);
        createMonster("foxZombie",73,16);
        createMonster("foxZombie",86,14);

        // Rudeling spawn
        createMonster("rudeling",16,17);
        createMonster("rudeling",56,11);


    }

    public void createMonster(String monsterName,int worldCol,int worldRow){
        Entity monster;
        if (monsterName.equals("rudeling")){
            monster = new MON_Rudeling(gp,worldCol,worldRow);
        }
        else if (monsterName.equals("foxZombie")){
            monster = new MON_FoxZombie(gp,worldCol,worldRow);
        }
        else{
            monster = new MON_Blob(gp,worldCol,worldRow);
        }

        gp.monster.add(monster);
    }
}
