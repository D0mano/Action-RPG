package main;

import data.SerialObject;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage original , int width, int height) {
        BufferedImage scaleImage = new BufferedImage(width,height,original.getType());
        Graphics2D g2d = scaleImage.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();

        return scaleImage;

    }

    public String oppositeDirection(String direction){
        return switch (direction) {
            case "right" -> "left";
            case "left" -> "right";
            case "up" -> "down";
            case "down" -> "up";
            default -> null;
        };
    }

    public SuperObject getObject(GamePanel gp,String objectName){
        return switch (objectName) {
            case "sword" -> new OBJ_Sword(gp);
            case "shield" -> new OBJ_Shield(gp);
            case "fir wand" -> new OBJ_FireWand(gp);
            case "ice wand" -> new OBJ_IceWand(gp);
            case "chest" -> new OBJ_Chest(gp);
            case "door" -> new OBJ_Door(gp);
            case "grappling hook" -> new OBJ_GrapplingHook(gp);
            case "key" -> new OBJ_Key(gp);
            case "lantern" -> new OBJ_Lantern(gp);
            case "blue fruit" -> new OBJ_BlueFruit(gp);
            case "red fruit" -> new OBJ_RedFruit(gp);
            default -> null;
        };
    }

    public SerialObject getSerialFromSuperObject(SuperObject superObject){
        SerialObject serialObject = new SerialObject();
        serialObject.name = superObject.name;
        serialObject.worldRow = superObject.worldRow;
        serialObject.worldCol = superObject.worldCol;
        return serialObject;
    }

    public ArrayList<SerialObject> getSerialArrayListFromSuperObjects(ArrayList<SuperObject> superObjects){
        ArrayList<SerialObject> serialObjects = new ArrayList<>();
        for (SuperObject superObject : superObjects) {
            serialObjects.add(getSerialFromSuperObject(superObject));
        }
        return serialObjects;
    }

    public SuperObject getSuperObjectFromSerialObject(SerialObject serialObject,GamePanel gp){
        SuperObject  superObject = new SuperObject(gp);
        superObject = getObject(gp,serialObject.name);
        superObject.worldRow = serialObject.worldRow;
        superObject.worldCol = serialObject.worldCol;
        superObject.setWorldCoordinate();
        return superObject;
    }

    public ArrayList<SuperObject> getSuperObjectArrayListFromSerialObjects(ArrayList<SerialObject> serialObjects,GamePanel gp){
        ArrayList<SuperObject> superObjects = new ArrayList<>();
        for (SerialObject serialObject : serialObjects) {
            superObjects.add(getSuperObjectFromSerialObject(serialObject,gp));
        }
        return superObjects;
    }
}
