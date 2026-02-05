package main;

import entity.Entity;
import monster.MON_Blob;
import monster.MON_FoxZombie;
import monster.MON_Rudeling;
import object.*;

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
        gp.obj[0] = new OBJ_Sword(gp);
        gp.obj[0].worldX = 48  * gp.tileSize;
        gp.obj[0].worldY = 53 * gp.tileSize;

        gp.obj[1] = new OBJ_Shield(gp);
        gp.obj[1].worldX = 16  * gp.tileSize;
        gp.obj[1].worldY = 17 * gp.tileSize;

        gp.obj[2] = new OBJ_FireWand(gp);
        gp.obj[2].worldX = 56  * gp.tileSize;
        gp.obj[2].worldY = 11 * gp.tileSize;

        gp.obj[3] = new OBJ_IceWand(gp);
        gp.obj[3].worldX = 48  * gp.tileSize;
        gp.obj[3].worldY = 27 * gp.tileSize;


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
