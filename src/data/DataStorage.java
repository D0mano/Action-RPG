package data;

import main.Map;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    // PLAYER STAT
    int playerHealth;
    int playerHealthMax;
    int playerMana;
    int playerManaMax;
    int playerStamina;
    int playerStaminaMax;

    // COORDINATE
    int playerCol;
    int playerRow;
    int playerScreenX;
    int playerScreenY;
    int currentMapIndex;

    // INVENTORY
    ArrayList<String> inventory = new ArrayList<>();
    String jEquip;
    String kEquip;
    String lEquip;


    // OBJECT
    ArrayList<ArrayList<SerialObject>> objList = new ArrayList<>();





}
