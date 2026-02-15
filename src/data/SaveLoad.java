package data;

import main.GamePanel;
import main.Map;
import main.UtilityTool;
import object.SuperObject;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SaveLoad {
    GamePanel gp;
    UtilityTool uTool = new  UtilityTool();
    int fileNumber;
    public String fileName;

    public SaveLoad(GamePanel gp,int fileNumber) {
        this.gp = gp;
        this.fileNumber = fileNumber;
        this.fileName = "File #" + fileNumber;


    }

    public void save(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("saves/" + fileName+".dat")));
            DataStorage ds = new DataStorage();

            // SAVE PLAYER STAT
            ds.playerHealth = gp.player.health;
            ds.playerHealthMax = gp.player.maxHealth;
            ds.playerMana = gp.player.mana;
            ds.playerManaMax = gp.player.maxMana;
            ds.playerStamina = gp.player.endurance;
            ds.playerStaminaMax = gp.player.maxEndurance;

            ds.playerCol = gp.player.worldCol;
            ds.playerRow = gp.player.worldRow;
            ds.playerScreenX = gp.player.screenX;
            ds.playerScreenY = gp.player.screenY;
            ds.currentMapIndex = gp.currentMapIndex;

            // SAVE PLAYER INVENTORY AND EQUIPMENT
            for (int i = 0; i < gp.player.inventory.length; i++) {
                for (SuperObject items : gp.player.inventory[i]) {
                    ds.inventory.add(items.name);
                }
            }
            if(gp.player.jEquip != null){
                ds.jEquip = gp.player.jEquip.name;

            }
            if(gp.player.kEquip != null){
                ds.kEquip = gp.player.kEquip.name;
            }
            if(gp.player.lEquip != null){
                ds.lEquip = gp.player.lEquip.name;
            }
            // SAVE ON GROUND OBJECT ON THE MAPS
            for (int i = 0; i < gp.mapsList.size(); i++) {
                Map map = gp.mapsList.get(i);
                ds.objList.add(uTool.getSerialArrayListFromSuperObjects(map.objectsList));
            }


            // Write the DataStorage object

            oos.writeObject(ds);

        } catch (IOException e) {
            System.err.println("Error saving data");
            e.printStackTrace();
        }

    }

    public void load(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("saves/" + fileName + ".dat")));

            // Read the DataStorage object
            DataStorage ds = (DataStorage)ois.readObject();

            gp.player.health = ds.playerHealth ;
            gp.player.maxHealth = ds.playerHealthMax;
            gp.player.mana = ds.playerMana;
            gp.player.maxMana = ds.playerManaMax ;
            gp.player.endurance =  ds.playerStamina;
            gp.player.maxEndurance = ds.playerStaminaMax;

            gp.currentMapIndex = ds.currentMapIndex;
            gp.setMap(gp.currentMapIndex);
            gp.player.worldCol  = ds.playerCol;
            gp.player.worldRow = ds.playerRow;
            gp.player.worldX = gp.player.worldCol * gp.tileSize;
            gp.player.worldY = gp.player.worldRow * gp.tileSize;
            gp.player.screenX = ds.playerScreenX;
            gp.player.screenY = ds.playerScreenY;


            if (ds.jEquip != null) {
                gp.player.addObjToInventory(uTool.getObject(gp,ds.jEquip));
                ds.inventory.remove(ds.jEquip);
            }
            if (ds.kEquip != null) {
                gp.player.addObjToInventory(uTool.getObject(gp,ds.kEquip));
                ds.inventory.remove(ds.kEquip);
            }
            if (ds.lEquip != null) {
                gp.player.addObjToInventory(uTool.getObject(gp,ds.lEquip));
                ds.inventory.remove(ds.lEquip);
            }
            for(String objName : ds.inventory){
                gp.player.addObjToInventory(uTool.getObject(gp,objName));
            }

            for (int i = 0; i < gp.mapsList.size(); i++) {
                Map map = gp.mapsList.get(i);
                map.objectsList = uTool.getSuperObjectArrayListFromSerialObjects(ds.objList.get(i),gp);
            }






        } catch (IOException e) {
            System.err.println("Error saving data");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
