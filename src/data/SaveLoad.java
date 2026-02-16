package data;

import main.GamePanel;
import main.Map;
import main.UtilityTool;
import object.SuperObject;

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

        DataStorage ds = new DataStorage();

        // SAVE PLAYER STAT
        ds.playerHealth = gp.player.health;
        ds.playerHealthMax = gp.player.maxHealth;
        ds.playerMana = gp.player.mana;
        ds.playerManaMax = gp.player.maxMana;
        ds.playerStamina = gp.player.endurance;
        ds.playerStaminaMax = gp.player.maxEndurance;

        // SAVE PLAYER POSITION
        ds.playerCol = gp.player.worldCol;
        ds.playerRow = gp.player.worldRow;
        ds.playerScreenX = gp.player.screenX;
        ds.playerScreenY = gp.player.screenY;
        ds.currentMapIndex = gp.currentMapIndex;

        // SAVE PLAYER INVENTORY
        for (SuperObject item : gp.player.inventory[0]) {  // gear
            ds.gearInventory.add(item.name);
        }
        for (SuperObject item : gp.player.inventory[1]) {  // singleUse
            ds.singleUseInventory.add(item.name);
        }
        for (SuperObject item : gp.player.inventory[2]) {  // equipment
            ds.equipmentInventory.add(item.name);
        }
        if(gp.player.jEquip != null) ds.jEquip = gp.player.jEquip.name;
        if(gp.player.kEquip != null) ds.kEquip = gp.player.kEquip.name;
        if(gp.player.lEquip != null) ds.lEquip = gp.player.lEquip.name;

        // SAVE ON GROUND OBJECT ON THE MAPS
        for (int i = 0; i < gp.mapsList.size(); i++) {
            Map map = gp.mapsList.get(i);
            ds.objList.add(uTool.getSerialArrayListFromSuperObjects(map.objectsList));
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/" + fileName+".dat"));
            // Write the DataStorage object
            oos.writeObject(ds);
        } catch (IOException e) {
            System.err.println("[SaveLoad] Erreur saving data : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void load(){

        DataStorage ds;

        // --- Lecture depuis le disque ---
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("saves/" + fileName + ".dat"))) {
            ds = (DataStorage) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("[SaveLoad] Fichier introuvable : saves/" + fileName + ".dat");
            return;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[SaveLoad] Erreur lors du chargement : " + e.getMessage());
            e.printStackTrace();
            return;
        }
        // PLAYER STATS
        gp.player.health = ds.playerHealth ;
        gp.player.maxHealth = ds.playerHealthMax;
        gp.player.mana = ds.playerMana;
        gp.player.maxMana = ds.playerManaMax ;
        gp.player.endurance =  ds.playerStamina;
        gp.player.maxEndurance = ds.playerStaminaMax;



        // OBJECT ON THE GROUND
        for (int i = 0; i < gp.mapsList.size(); i++) {
            Map map = gp.mapsList.get(i);
            map.objectsList.clear();
            map.objectsList = uTool.getSuperObjectArrayListFromSerialObjects(ds.objList.get(i),gp);
        }

        // MAP AND POSITION
        gp.setMap(ds.currentMapIndex);
        gp.player.worldCol  = ds.playerCol;
        gp.player.worldRow = ds.playerRow;
        gp.player.worldX = gp.player.worldCol * gp.tileSize;
        gp.player.worldY = gp.player.worldRow * gp.tileSize;
        gp.player.screenX = ds.playerScreenX;
        gp.player.screenY = ds.playerScreenY;

        // INVENTORY
        // Gear
        for (String name : ds.gearInventory) {
            SuperObject obj = uTool.getObject(gp, name);
            if (obj != null) gp.player.inventory[0].add(obj);
        }
        // Single-use
        for (String name : ds.singleUseInventory) {
            SuperObject obj = uTool.getObject(gp, name);
            if (obj != null) gp.player.inventory[1].add(obj);
        }
        // Equipment
        for (String name : ds.equipmentInventory) {
            SuperObject obj = uTool.getObject(gp, name);
            if (obj != null) gp.player.inventory[2].add(obj);
        }

        // EQUIPMENT SLOTS MANUAL ASSIGNATION
        if (ds.jEquip != null) gp.player.jEquip = findInEquipmentInventory(ds.jEquip);
        if (ds.kEquip != null) gp.player.kEquip = findInEquipmentInventory(ds.kEquip);
        if (ds.lEquip != null) gp.player.lEquip = findInEquipmentInventory(ds.lEquip);

        // MONSTER
        for (Map map : gp.mapsList) {
            map.monsterList.clear();
        }
        gp.assetSetter.setMonster();


    }

    public ArrayList<SuperObject> getInventoryEquipment(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + fileName + ".dat"));

            // Read the DataStorage object
            DataStorage ds = (DataStorage)ois.readObject();

            ArrayList<SuperObject> objs = new ArrayList<>();
            for(String objName : ds.equipmentInventory){
                SuperObject obj = uTool.getObject(gp,objName);
                if (obj != null){
                    objs.add(obj);
                }
            }
            return  objs;

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private SuperObject findInEquipmentInventory(String name) {
        for (SuperObject obj : gp.player.inventory[2]) {
            if (obj.name.equals(name)) return obj;
        }
        return null;
    }
}
