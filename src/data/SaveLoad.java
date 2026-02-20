package data;

import main.GamePanel;
import main.Map;
import main.UtilityTool;
import object.SuperObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles the saving and loading mechanics of the game using object serialization.
 */
public class SaveLoad {
    GamePanel gp;
    UtilityTool uTool = new UtilityTool();
    int fileNumber;
    public String fileName;

    /**
     * Constructor for the SaveLoad system.
     * * @param gp The GamePanel instance.
     * @param fileNumber The slot number used to name the save file.
     */
    public SaveLoad(GamePanel gp, int fileNumber) {
        this.gp = gp;
        this.fileNumber = fileNumber;
        this.fileName = "File #" + fileNumber;
    }

    /**
     * Gathers all the current game data (stats, inventory, map objects)
     * and serializes it into a .dat file on the local disk.
     */
    public void save(){
        DataStorage ds = new DataStorage();

        // SAVE PLAYER STATS
        ds.playerHealth = gp.player.health;
        ds.playerHealthMax = gp.player.maxHealth;
        ds.playerMana = gp.player.mana;
        ds.playerManaMax = gp.player.maxMana;
        ds.playerStamina = gp.player.endurance;
        ds.playerStaminaMax = gp.player.maxEndurance;

        // SAVE PLAYER POSITION
        ds.playerCol = gp.player.worldCol;
        ds.playerRow = gp.player.worldRow;
        ds.playerWorldX = gp.player.worldX;
        ds.playerWorldY = gp.player.worldY;
        ds.playerScreenX = gp.player.screenX;
        ds.playerScreenY = gp.player.screenY;
        ds.currentMapIndex = gp.currentMapIndex;

        // SAVE PLAYER INVENTORY
        for (SuperObject item : gp.player.inventory[0]) {  // Gear
            ds.gearInventory.add(item.name);
        }
        for (SuperObject item : gp.player.inventory[1]) {  // Single Use
            ds.singleUseInventory.add(item.name);
        }
        for (SuperObject item : gp.player.inventory[2]) {  // Equipment
            ds.equipmentInventory.add(item.name);
        }
        if(gp.player.jEquip != null) ds.jEquip = gp.player.jEquip.name;
        if(gp.player.kEquip != null) ds.kEquip = gp.player.kEquip.name;
        if(gp.player.lEquip != null) ds.lEquip = gp.player.lEquip.name;

        // SAVE GROUND OBJECTS ON ALL MAPS
        for (int i = 0; i < gp.mapsList.size(); i++) {
            Map map = gp.mapsList.get(i);
            ds.objList.add(uTool.getSerialArrayListFromSuperObjects(map.objectsList));
        }

        // SAVE TIME SPEND ON THE SAVE FILE
        ds.timeSpend = gp.timeSpend;

        // Write the DataStorage object to disk using try-with-resources
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/" + fileName + ".dat"))) {
            oos.writeObject(ds);
        } catch (IOException e) {
            System.err.println("[SaveLoad] Error saving data : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deserializes the .dat save file from the local disk and applies
     * the data back into the current game state.
     */
    public void load(){
        DataStorage ds;

        // Read data from disk using try-with-resources
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + fileName + ".dat"))) {
            ds = (DataStorage) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("[SaveLoad] File not found : saves/" + fileName + ".dat");
            return;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[SaveLoad] Error loading data : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // RESTORE PLAYER STATS
        gp.player.health = ds.playerHealth;
        gp.player.maxHealth = ds.playerHealthMax;
        gp.player.mana = ds.playerMana;
        gp.player.maxMana = ds.playerManaMax;
        gp.player.endurance = ds.playerStamina;
        gp.player.maxEndurance = ds.playerStaminaMax;

        // RESTORE GROUND OBJECTS
        for (int i = 0; i < gp.mapsList.size(); i++) {
            Map map = gp.mapsList.get(i);
            map.objectsList.clear(); // Clear memory reference
            ArrayList<SuperObject> loadedObjects = uTool.getSuperObjectArrayListFromSerialObjects(ds.objList.get(i), gp);
            if (loadedObjects != null) {
                map.objectsList.addAll(loadedObjects); // Append elements properly
            }
        }

        // RESTORE MAP AND POSITION
        gp.setMap(ds.currentMapIndex);
        gp.player.worldCol = ds.playerCol;
        gp.player.worldRow = ds.playerRow;
        gp.player.worldX = ds.playerWorldX;
        gp.player.worldY = ds.playerWorldY;
        gp.player.screenX = ds.playerScreenX;
        gp.player.screenY = ds.playerScreenY;

        // RESTORE INVENTORY
        for (String name : ds.gearInventory) {
            SuperObject obj = uTool.getObject(gp, name);
            if (obj != null) gp.player.inventory[0].add(obj);
        }
        for (String name : ds.singleUseInventory) {
            SuperObject obj = uTool.getObject(gp, name);
            if (obj != null) gp.player.inventory[1].add(obj);
        }
        for (String name : ds.equipmentInventory) {
            SuperObject obj = uTool.getObject(gp, name);
            if (obj != null) gp.player.inventory[2].add(obj);
        }

        // RESTORE EQUIPMENT SLOTS
        if (ds.jEquip != null) gp.player.jEquip = findInEquipmentInventory(ds.jEquip);
        if (ds.kEquip != null) gp.player.kEquip = findInEquipmentInventory(ds.kEquip);
        if (ds.lEquip != null) gp.player.lEquip = findInEquipmentInventory(ds.lEquip);

        // RESET MONSTERS (forces map logic to spawn fresh monsters based on asset setter)
        for (Map map : gp.mapsList) {
            map.monsterList.clear();
        }
        gp.assetSetter.setMonster();
    }

    /**
     * Reads the save file temporarily to retrieve only the player's equipment
     * (used mainly for rendering UI previews without fully loading the game).
     * * @return An ArrayList containing the equipment objects found in the save.
     */
    public ArrayList<SuperObject> getInventoryEquipment(){
        // Try-with-resources prevents file locking
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + fileName + ".dat"))) {
            DataStorage ds = (DataStorage) ois.readObject();
            ArrayList<SuperObject> objs = new ArrayList<>();
            for(String objName : ds.equipmentInventory){
                SuperObject obj = uTool.getObject(gp, objName);
                if (obj != null){
                    objs.add(obj);
                }
            }
            return objs;
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to get the time spend on a file in numbers of frames.
     * @return The numbers of frames.
     */
    public int getTimeSpend(){
        try( ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/" + fileName + ".dat"))) {
            // Read the DataStorage object
            DataStorage ds = (DataStorage)ois.readObject();

            return ds.timeSpend;

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method to find an object by its name within the player's equipment inventory.
     * * @param name The name of the object to find.
     * @return The SuperObject if found, or null.
     */
    private SuperObject findInEquipmentInventory(String name) {
        for (SuperObject obj : gp.player.inventory[2]) {
            if (obj.name.equals(name)) return obj;
        }
        return null;
    }
}