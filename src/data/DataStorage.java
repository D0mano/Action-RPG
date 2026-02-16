package data;

import main.Map;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    public ArrayList<String> gearInventory    = new ArrayList<>();
    public ArrayList<String> singleUseInventory = new ArrayList<>();
    public ArrayList<String> equipmentInventory = new ArrayList<>();

    // EQUIPMENT SLOTS
    String jEquip;
    String kEquip;
    String lEquip;


    // OBJECT
    ArrayList<ArrayList<SerialObject>> objList = new ArrayList<>();

    public int timeSpend = 0;





}
