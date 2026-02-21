package main;

import com.fasterxml.jackson.databind.JsonNode;
import entity.Entity;
import monster.MON_Blob;
import monster.MON_FoxZombie;
import monster.MON_Rudeling;
import object.*;

import java.sql.Blob;

/**
 * The AssetSetter class is responsible for populating the game's maps with
 * interactive elements such as objects (weapons, items, doors) and entities (monsters).
 * It acts as the level initializer when starting a new game.
 */
public class AssetSetter {
    GamePanel gp;

    /**
     * Constructor for the AssetSetter.
     * @param gp The GamePanel instance.
     */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Reloads all currently active objects and monsters on the map.
     * Usually called when the screen resolution or scaling changes
     * so that assets can adjust their internal sizes/hitboxes.
     */
    public void reload() {
        for (SuperObject obj : gp.obj) {
            if (obj != null) {
                obj.reload();
            }
        }
        for (Entity entity : gp.monster) {
            if (entity != null) {
                entity.reload();
            }
        }
    }

    /**
     * Places the initial layout of objects (weapons, items, etc.) onto the maps.
     * This method is called when starting a brand new game.
     */
    public void setObject() {
        int i = 0;
        for (Map map : gp.mapsList){
            if (map != null) {
                JsonNode layerNode = map.rootNode.get("layers");
                 for (JsonNode layer : layerNode) {
                     if (layer.get("type").asText().equals("objectgroup") && layer.get("name").asText().equals("Obj_layer")) {
                         JsonNode objArray = layer.get("objects");
                         for (JsonNode obj : objArray) {
                             String name;
                             if (obj.get("template") != null) {
                                 String templatePath = obj.get("template").asText();
                                  name = templatePath.substring(templatePath.lastIndexOf("/") + 1).replace(".tx", "");
                             }
                             else{
                                  name = obj.get("name").asText();
                             }

                             placeObject(name,obj.get("x").asInt()/gp.originalTileSize,(obj.get("y").asInt()/gp.originalTileSize)-1,i);
                         }
                     }
                 }

            }

            i++;

        }

//        // Syntax: placeObject("Object Name", Column, Row, Map Index)
//        placeObject("sword", 48, 53, 0);
//        placeObject("shield", 16, 17, 0);
//        placeObject("grappling hook", 56, 11, 0);
//        placeObject("ice wand", 48, 27, 0);
    }

    /**
     * Places the initial layout of monsters onto the maps.
     * This method is called when starting a new game to populate the world with enemies.
     */
    public void setMonster() {

        int i = 0;
        for (Map map : gp.mapsList){
            if (map != null) {
                JsonNode layerNode = map.rootNode.get("layers");
                for (JsonNode layer : layerNode) {
                    if (layer.get("type").asText().equals("objectgroup") && layer.get("name").asText().equals("Mon_layer")) {
                        JsonNode objArray = layer.get("objects");
                        for (JsonNode obj : objArray) {
                            String templatePath = obj.get("template").asText();
                            String name = templatePath.substring(templatePath.lastIndexOf("/") + 1).replace(".tx", "");
                            createMonster(name,obj.get("x").asInt()/gp.originalTileSize,(obj.get("y").asInt()/gp.originalTileSize)-1,i);
                        }
                    }
                }

            }

            i++;

        }

//        // BLOB SPAWNS
//        createMonster("blob", 52, 32, 0);
//        createMonster("blob", 34, 27, 0);
//        createMonster("blob", 75, 32, 0);
//        createMonster("blob", 80, 27, 0);
//
//        // FOX ZOMBIE SPAWNS
//        createMonster("foxZombie", 16, 25, 0);
//        createMonster("foxZombie", 13, 12, 0);
//        createMonster("foxZombie", 73, 16, 0);
//        createMonster("foxZombie", 86, 14, 0);
//
//        // RUDELING SPAWNS
//        createMonster("rudeling", 16, 17, 0);
//        createMonster("rudeling", 56, 11, 0);
    }

    /**
     * Defines the default spawn coordinates for the player on every map.
     * These values are used when entering a new map or starting a new game.
     */
    public void setPlayerSpawn() {
        // Map 0 (Main Overworld)
        gp.mapsList.get(0).playerCol = 49;
        gp.mapsList.get(0).playerRow = 66;

        // Map 1 (EastForest)
        gp.mapsList.get(1).playerCol = 0;
        gp.mapsList.get(1).playerRow = 16;
//
//        // Map 2 (Dungeon/Interior 2)
//        gp.mapsList.get(2).playerCol = 40;
//        gp.mapsList.get(2).playerRow = 44;
    }

    /**
     * Helper method to instantiate a specific monster and add it directly
     * to the designated map's monster list.
     * * @param monsterName The internal string name of the monster type.
     * @param worldCol    The X coordinate on the grid.
     * @param worldRow    The Y coordinate on the grid.
     * @param mapIndex    The index of the map in the GamePanel's mapsList.
     */
    public void createMonster(String monsterName, int worldCol, int worldRow, int mapIndex) {
        Entity monster;

        // Determine which monster object to instantiate
        switch (monsterName) {
            case "Blob": monster = new MON_Blob(gp,worldCol,worldRow);break;
            case "Rudeling": monster = new MON_Rudeling(gp,worldCol,worldRow);break;
            case "Fox_Zombie": monster = new MON_FoxZombie(gp,worldCol,worldRow);break;
            default :{System.err.println("[AssetSetter] Invalid monster name! :"+monsterName); return;}
        }
//        if (monsterName.equals("rudeling")) {
//            monster = new MON_Rudeling(gp, worldCol, worldRow);
//        } else if (monsterName.equals("foxZombie")) {
//            monster = new MON_FoxZombie(gp, worldCol, worldRow);
//        } else {
//            // Default to Blob if name doesn't match above
//            monster = new MON_Blob(gp, worldCol, worldRow);
//        }

        // Add the created monster to the specific map's entity list
        gp.mapsList.get(mapIndex).monsterList.add(monster);
    }

    /**
     * Helper method to instantiate a specific object (item, weapon, etc.)
     * and place it physically on the map.
     * * @param objectName The internal string name of the object.
     * @param worldCol   The X coordinate on the grid.
     * @param worldRow   The Y coordinate on the grid.
     * @param mapIndex   The index of the map in the GamePanel's mapsList.
     */
    public void placeObject(String objectName, int worldCol, int worldRow, int mapIndex) {
        UtilityTool uTool = new UtilityTool();

        // Fetch the correct SuperObject instance using the UtilityTool factory method
        SuperObject obj = uTool.getObject(gp, objectName);

        if (obj != null) {
            // Assign world coordinates
            obj.worldCol = worldCol;
            obj.worldRow = worldRow;
            obj.setWorldCoordinate(); // Convert grid (col/row) to precise pixel coordinates

            // Add the created object to the specific map's object list
            gp.mapsList.get(mapIndex).objectsList.add(obj);
        }
    }
}