package main;

import data.SerialObject;
import object.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A utility class containing various helper methods for image manipulation,
 * directional logic,time management and object serialization.
 */
public class UtilityTool {

    /**
     * Scales a given BufferedImage to the specified width and height.
     * @param original The original BufferedImage to scale.
     * @param width The target width in pixels.
     * @param height The target height in pixels.
     * @return The scaled BufferedImage.
     */
    public BufferedImage scaleImage(BufferedImage original , int width, int height) {
        BufferedImage scaleImage = new BufferedImage(width,height,original.getType());
        Graphics2D g2d = scaleImage.createGraphics();
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();

        return scaleImage;
    }

    /**
     * Returns the opposite of a given direction.
     * @param direction The current direction (e.g., "right").
     * @return The opposite direction (e.g., "left"), or null if unknown.
     */
    public String oppositeDirection(String direction){
        return switch (direction) {
            case "right" -> "left";
            case "left" -> "right";
            case "up" -> "down";
            case "down" -> "up";
            default ->{
                System.err.println("[UtilityTool] Unknown Direction : \"" + direction + "\"");
                yield null;
            }
        };
    }

    /**
     * Instantiates and returns a new SuperObject based on its name.
     * * @param gp The GamePanel instance.
     * @param objectName The name of the object to create.
     * @return A new instance of the requested SuperObject, or null if unknown.
     */
    public SuperObject getObject(GamePanel gp, String objectName){
        return switch (objectName) {
            case "sword" -> new OBJ_Sword(gp);
            case "shield" -> new OBJ_Shield(gp);
            case "fire wand" -> new OBJ_FireWand(gp);
            case "ice wand" -> new OBJ_IceWand(gp);
            case "chest" -> new OBJ_Chest(gp);
            case "door" -> new OBJ_Door(gp);
            case "grappling hook" -> new OBJ_GrapplingHook(gp);
            case "key" -> new OBJ_Key(gp);
            case "lantern" -> new OBJ_Lantern(gp);
            case "blue fruit" -> new OBJ_BlueFruit(gp);
            case "red fruit" -> new OBJ_RedFruit(gp);
            default ->{
                System.err.println("[UtilityTool] Unknown Object : \"" + objectName + "\"");
                yield null;
            }
        };
    }

    /**
     * Converts a SuperObject into a SerialObject for saving purposes.
     * @param superObject The SuperObject to serialize.
     * @return A SerialObject containing the basic data (name, coordinates).
     */
    public SerialObject getSerialFromSuperObject(SuperObject superObject){
        SerialObject serialObject = new SerialObject();
        serialObject.name = superObject.name;
        serialObject.worldRow = superObject.worldRow;
        serialObject.worldCol = superObject.worldCol;
        return serialObject;
    }

    /**
     * Converts an ArrayList of SuperObjects into an ArrayList of SerialObjects.
     * @param superObjects The list of SuperObjects.
     * @return The list of serialized objects.
     */
    public ArrayList<SerialObject> getSerialArrayListFromSuperObjects(ArrayList<SuperObject> superObjects){
        ArrayList<SerialObject> serialObjects = new ArrayList<>();
        for (SuperObject superObject : superObjects) {
            serialObjects.add(getSerialFromSuperObject(superObject));
        }
        return serialObjects;
    }

    /**
     * Recreates a SuperObject from a SerialObject loaded from a save file.
     * @param serialObject The serialized object data.
     * @param gp The GamePanel instance.
     * @return The fully restored SuperObject.
     */
    public SuperObject getSuperObjectFromSerialObject(SerialObject serialObject, GamePanel gp){
        SuperObject superObject = getObject(gp, serialObject.name);
        if(superObject != null) {
            superObject.worldRow = serialObject.worldRow;
            superObject.worldCol = serialObject.worldCol;
            superObject.setWorldCoordinate();
        }
        return superObject;
    }

    /**
     * Recreates an ArrayList of SuperObjects from an ArrayList of SerialObjects.
     * @param serialObjects The list of serialized objects.
     * @param gp The GamePanel instance.
     * @return The list of restored SuperObjects.
     */
    public ArrayList<SuperObject> getSuperObjectArrayListFromSerialObjects(ArrayList<SerialObject> serialObjects, GamePanel gp){
        ArrayList<SuperObject> superObjects = new ArrayList<>();
        for (SerialObject serialObject : serialObjects) {
            SuperObject obj = getSuperObjectFromSerialObject(serialObject, gp);
            if(obj != null) {
                superObjects.add(obj);
            }
        }
        return superObjects;
    }

    /**
     * Convert the nbFrame into a time in the format HH:MM:SS
     * @param nbFrame The number of frame passed
     * @param FPS The number of frame per second
     * @return The string representing the time in HH:MM:SS
     */
    public String getTimeFromFrame(int nbFrame,int FPS){

        int totalSeconds = nbFrame/FPS;

        int hours = totalSeconds/3600;
        int minutes = (totalSeconds%3600)/60;
        int seconds = totalSeconds%60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
}
