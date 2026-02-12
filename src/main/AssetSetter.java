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
        placeObject("lantern",48,53,0);
        placeObject("shield",16,17,0);
        placeObject("grappling_hook",56,11,0);
        placeObject("ice_wand",48,27,0);




    }

    public void setMonster(){
        // Blob spawn
        createMonster("blob",52,32,0);
        createMonster("blob",34,27,0);
        createMonster("blob",75,32,0);
        createMonster("blob",80,27,0);

        //Hedghog spawn
        createMonster("foxZombie",16,25,0);
        createMonster("foxZombie",13,12,0);
        createMonster("foxZombie",73,16,0);
        createMonster("foxZombie",86,14,0);

        // Rudeling spawn
        createMonster("rudeling",16,17,0);
        createMonster("rudeling",56,11,0);


    }

    public void setPlayerSpawn(){
        gp.mapsList.get(0).playerCol = 49;
        gp.mapsList.get(0).playerRow = 66;

        gp.mapsList.get(1).playerCol = 24;
        gp.mapsList.get(1).playerRow = 39;

        gp.mapsList.get(2).playerCol = 40;
        gp.mapsList.get(2).playerRow = 44;

    }

    public void createMonster(String monsterName,int worldCol,int worldRow,int mapIndex){
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

        gp.mapsList.get(mapIndex).monsterList.add(monster);
    }

    public void placeObject(String objectName,int worldCol,int worldRow,int mapIndex){
        SuperObject obj;
        switch (objectName){
            case "sword": obj = new OBJ_Sword(gp); break;
            case "shield": obj = new OBJ_Shield(gp); break;
            case "fire_wand" : obj = new OBJ_FireWand(gp); break;
            case "ice_wand": obj = new OBJ_IceWand(gp); break;
            case "chest": obj = new OBJ_Chest(gp); break;
            case "door": obj = new OBJ_Door(gp); break;
            case "grappling_hook": obj = new OBJ_GrapplingHook(gp); break;
            case "key": obj = new OBJ_Key(gp); break;
            case "lantern": obj = new OBJ_Lantern(gp); break;
            case "blue_fruit": obj = new OBJ_BlueFruit(gp); break;
            case "red_fruit": obj = new OBJ_RedFruit(gp); break;
            default:obj=null;  break;
        }
        if (obj != null){
            obj.worldCol = worldCol;
            obj.worldRow = worldRow;
            obj.setWorldCoordinate();
            gp.mapsList.get(mapIndex).objectsList.add(obj);
        }
    }
}
