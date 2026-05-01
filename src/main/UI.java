package main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Player;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The UI class handles all on-screen graphical interfaces.
 * This includes HUD (Health, Mana, Endurance, Inventory), menus (Title, Options, Pause),
 * dialogues, screen transitions, and popup animations (like picking up an item).
 */
public class UI {
    GamePanel gp;
    Graphics2D g2;
    Player player;

    // FONTS
    Font lilliput_40, trunic, lilliput_20, lilliput_15;

    // DIALOGUE & MESSAGES
    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;
    public String currentDialogue = "";

    // ITEM PICKUP ANIMATION
    public boolean itemOn = false;
    public SuperObject item = null;
    float itemScale = 0f;

    public boolean gameFinished = false;
    Color myOrange = new Color(206, 157, 58);

    // HUD IMAGES
    BufferedImage healthTop, healthOverlay, healthMiddle;
    BufferedImage enduranceTop, enduranceOverlay, enduranceMiddle;
    BufferedImage manaTop, manaOverlay, manaMiddle;
    BufferedImage potionFull, potionEmpty;

    // MENU IMAGES
    BufferedImage menuSelection, menuSelectionOrange, menuSelectionOrange2;
    BufferedImage messageWindow;
    BufferedImage optionWindow;
    BufferedImage inventoryFrame;
    BufferedImage whiteTitle, blackTitle;

    // MENU COMMAND TRACKERS
    public int commandNumber = 0;
    public String[] pauseCommand = {"Return to Game", "Options", "Quit"};
    public int commandNumberPause = 0;

    public String[] optionCommand = {"Audio", "Graphics","Controls", "Return"};
    public int commandNumberOption = 0;

    public String[] audioCommand = {"Musics", "Sounds Effects", "Return"};
    public int commandNumberAudio = 0;

    public String[] graphicCommand = {"Display Mode", "Resolution", "Return"};
    public int commandNumberGraphic = 0;

    public JsonNode controleRootNode ;
    public int commandNumberControle = 0;
    public int totalCategories = 0;

    public ArrayList<String> loadCommand = new ArrayList<>();
    public int commandNumberLoad = 0;

    public String[] loadSelectionCommand = {"Load", "Delete", "Cancel"};
    public int commandNumberLoadSelection = 0;

    public String[] gameOverCommand = {"Restart","Quit"};
    public int commandNumberGameOver = 0;

    public int commandNumberNewSlot = 0;

    // INVENTORY CURSOR
    public int slotRow = 0;
    public int slotCol = 0;

    public UtilityTool uTool = new UtilityTool();

    // DYNAMIC HUD POSITIONS (Used for inventory transition animations)
    public int potionXPos, potionYPos;
    public int potionSize;
    public int equipmentXPos, equipmentYPos;
    public int equipmentSize;

    // IRIS TRANSITION (Circle screen wipe)
    public boolean transitionOn = false;
    public int transitionState = 0; // 0 = idle, 1 = closing, 2 = opening
    public int transitionSpeed;
    public double transitionSize = 0;
    public double maxTransitionSize;
    private Runnable onTransitionComplete;

    public enum TransitionType{
        Iris,
        FadeInOut,
        SlideInOut,
        Shutters;
    }
    public TransitionType transitionType = TransitionType.Iris;

    /**
     * Constructor for the UI class.
     * Initializes fonts, sizes, and loads all required UI textures.
     * * @param gp     The GamePanel instance.
     * @param player The Player instance (to track stats/inventory).
     */
    public UI(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        maxTransitionSize = gp.screenWidth * 2.0;
        loadCommand.add("Cancel");

        getFont();
        getImage();
        getControle();



    }

    public void getImage(){
        // LOAD HUD TEXTURES
        healthOverlay = setup("player/health_overlay", (int)(gp.scale * (2 / 3f)));
        healthMiddle = setup("player/health_mid", (int)(gp.scale * (2 / 3f)));
        healthTop = setup("player/health_top", (int)(gp.scale * (2 / 3f)));

        enduranceTop = setup("player/endurance_top", (int)(gp.scale * (2 / 3f)));
        enduranceMiddle = setup("player/endurance_mid", (int)(gp.scale * (2 / 3f)));
        enduranceOverlay = healthOverlay;

        manaTop = setup("player/mana_top", (int)(gp.scale * (2 / 3f)));
        manaMiddle = setup("player/mana_mid", (int)(gp.scale * (2 / 3f)));
        manaOverlay = healthOverlay;

        whiteTitle = setup("titleScreen/tunic_logo_white", (int)(gp.scale * (5 / 3f)));
        blackTitle = setup("titleScreen/tunic_logo_black", 1);

        menuSelection = setup("menu/menuOverlayWhite", gp.scale);
        menuSelectionOrange = setup("menu/menuOverlayOrange", gp.scale);
        menuSelectionOrange2 = setup("menu/menuOverlayOrange2", gp.scale);

        inventoryFrame = setup("menu/inventoryFrame", gp.scale);
        messageWindow = setup("menu/window1", gp.scale);
        optionWindow = setup("menu/optionOverlay", gp.scale);

        potionFull = setup("player/potion_full", gp.scale);
        potionEmpty = setup("player/potion_empty", gp.scale);
    }

    public void getFont(){
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Lilliput Steps.otf");
            InputStream is2 = getClass().getResourceAsStream("/fonts/Trunic-Bold.otf");

            // NOTE: Font.TRUETYPE_FONT works perfectly for .otf files as well.
            Font lilliputFont = Font.createFont(Font.TRUETYPE_FONT, is);
            Font Trunic = Font.createFont(Font.TRUETYPE_FONT, is2);

            // Derive specific sizes based on tile scaling
            lilliput_40 = lilliputFont.deriveFont(Font.PLAIN, gp.tileSize);
            lilliput_20 = lilliputFont.deriveFont(Font.PLAIN, (gp.tileSize * (2 / 3f)));
            lilliput_15 = lilliputFont.deriveFont(Font.PLAIN, (gp.tileSize * (1 / 2f)));
            trunic = Trunic.deriveFont(Font.BOLD, gp.tileSize);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    public void getControle(){
        ObjectMapper mapper = new ObjectMapper();
        String path = "/UI/controle_file.json";

        try {
            InputStream is = getClass().getResourceAsStream(path);
            controleRootNode = mapper.readTree(is);

            // Calcule le nombre de catégories
            JsonNode controlsArray = controleRootNode.get("controls");
            if (controlsArray != null && controlsArray.isArray()) {
                totalCategories = controlsArray.size();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reloads UI components (fonts, scaling).
     * Called when changing the game's resolution or scaling mode to avoid pixelation.
     */
    public void reload() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/Lilliput Steps.otf");
            InputStream is2 = getClass().getResourceAsStream("/fonts/Trunic-Bold.otf");

            Font lilliputFont = Font.createFont(Font.TRUETYPE_FONT, is);
            Font Trunic = Font.createFont(Font.TRUETYPE_FONT, is2);

            lilliput_40 = lilliputFont.deriveFont(Font.PLAIN, gp.tileSize);
            lilliput_20 = lilliputFont.deriveFont(Font.PLAIN, (gp.tileSize * (2 / 3f)));
            lilliput_15 = lilliputFont.deriveFont(Font.PLAIN, (gp.tileSize * (1 / 2f)));
            trunic = Trunic.deriveFont(Font.BOLD, gp.tileSize);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        healthOverlay = setup("player/health_overlay", (int)(gp.scale * (2 / 3f)));
        healthMiddle = setup("player/health_mid", (int)(gp.scale * (2 / 3f)));
        healthTop = setup("player/health_top", (int)(gp.scale * (2 / 3f)));

        enduranceTop = setup("player/endurance_top", (int)(gp.scale * (2 / 3f)));
        enduranceMiddle = setup("player/endurance_mid", (int)(gp.scale * (2 / 3f)));
        enduranceOverlay = healthOverlay;

        manaTop = setup("player/mana_top", (int)(gp.scale * (2 / 3f)));
        manaMiddle = setup("player/mana_mid", (int)(gp.scale * (2 / 3f)));
        manaOverlay = healthOverlay;

        whiteTitle = setup("titleScreen/tunic_logo_white", (int)(gp.scale * (5 / 3f)));
        blackTitle = setup("titleScreen/tunic_logo_black", 1);

        menuSelection = setup("menu/menuOverlayWhite", gp.scale);
        menuSelectionOrange = setup("menu/menuOverlayOrange", gp.scale);
        menuSelectionOrange2 = setup("menu/menuOverlayOrange2", gp.scale);

        inventoryFrame = setup("menu/inventoryFrame", gp.scale);
        messageWindow = setup("menu/window1", gp.scale);
        optionWindow = setup("menu/optionOverlay", gp.scale);

        potionFull = setup("player/potion_full", gp.scale);
        potionEmpty = setup("player/potion_empty", gp.scale);
    }

    /**
     * Updates animations and background UI logic natively each frame.
     * Manages item popups and iris screen transitions.
     */
    public void update() {
        // Update item animations in dialogs
        if (gp.gameState == gp.dialogueState) {
            if (item != null && item.upAnimator != null) {
                item.upAnimator.update();
            }
        }

        // Handle pop-up zoom animation when obtaining an item
        if (itemOn) {
            // Scale up by 10% per frame
            if (itemScale < 1f) {
                itemScale += 0.1f;
                if (itemScale > 1f) itemScale = 1f; // Cap at 100% normal size
            }
        } else {
            // Reset scale when the pop-up is dismissed
            itemScale = 0f;
        }

        // Handle Iris (circle wipe) Transition logic
        if (transitionOn) {
            double speed = maxTransitionSize / transitionSpeed; // Default value 60 : Complete transition in ~60 frames (1 second)

            // STATE 1: CLOSING TRANSITION (Screen goes black)
            if (transitionState == 1) {
                if (transitionType == TransitionType.Iris) {
                    transitionSize -= speed;
                    if (transitionSize <= 0) {
                        transitionSize = 0;
                        // Trigger the action (e.g., loading map) exactly when screen is fully black
                        if (onTransitionComplete != null) {
                            onTransitionComplete.run();
                        }
                        transitionState = 2; // Switch to opening
                    }
                }
                else if (transitionType == TransitionType.FadeInOut || transitionType == TransitionType.Shutters || transitionType == TransitionType.SlideInOut) {
                    transitionSize += speed;
                    if (transitionSize >= maxTransitionSize) {
                        transitionSize = maxTransitionSize;
                        // Trigger the action (e.g., loading map) exactly when screen is fully black
                        if (onTransitionComplete != null) {
                            onTransitionComplete.run();
                        }
                        transitionState = 2; // Switch to opening
                    }

                }

            }
            // STATE 2: OPENING TRANSITION (Screen reveals)
            else if (transitionState == 2) {
                if (transitionType == TransitionType.Iris) {
                    transitionSize += speed;
                    if (transitionSize >= maxTransitionSize) {
                        transitionSize = maxTransitionSize;
                        transitionOn = false; // Transition finished
                        transitionState = 0;
                    }
                }
                else if (transitionType == TransitionType.FadeInOut || transitionType == TransitionType.Shutters || transitionType == TransitionType.SlideInOut) {
                    transitionSize -= speed;
                    if (transitionSize <= 0) {
                        transitionSize = 0;
                        transitionOn = false; // Transition finished
                        transitionState = 0;
                    }
                }

            }
        }
    }

    /**
     * Utility method to load and scale UI images correctly.
     * * @param imageName The path inside the UI folder.
     * @param scale     The target scale factor.
     * @return The correctly scaled BufferedImage.
     */
    public BufferedImage setup(String imageName, float scale) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/UI/" + imageName + ".png")));
            image = uTool.scaleImage(image, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));
        } catch (IOException e) {
            System.out.println("Error loading image " + imageName);
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Displays a temporary popup message on the screen.
     * * @param message The text to display.
     */
    public void showMessage(String message) {
        this.message = message;
        this.messageCounter++;
        messageOn = true;
    }

    /**
     * Main drawing method that redirects the rendering logic based on the current Game State.
     * * @param g2 The Graphics2D component.
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(lilliput_40);
        g2.setColor(Color.white);

        // MENU STATES
        if (gp.gameState == gp.titleState) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            drawTitleScreen();
        }
        if (gp.gameState == gp.newGameSlotState) {
            drawNewGameSlotScreen();
        }
        if (gp.gameState == gp.optionState) {
            if (gp.previousState == gp.titleState) {
                g2.setColor(Color.black);
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            }
            drawOptionScreen();
        }
        if (gp.gameState == gp.controlSettingState) {
            drawControleScreen();
        }
        if (gp.gameState == gp.audioSettingstate) {
            if (gp.previousState == gp.titleState) {
                g2.setColor(Color.black);
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            }
            drawAudioScreen();
        }
        if (gp.gameState == gp.graphicsSettingstate) {
            if (gp.previousState == gp.titleState) {
                g2.setColor(Color.black);
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            }
            drawGraphicsScreen();
        }
        if (gp.gameState == gp.loadSaveState) {
            drawLoadScreen();
        }
        if (gp.gameState == gp.loadSaveSelectionState) {
            drawLoadSelectionScreen();
        }

        // IN-GAME PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerHealth();
            drawPlayerEndurance();
            drawPlayerMana();
            drawPlayerPotion();
            drawPlayerEquipment();
            drawLuminosity();


        }

        // PAUSE & DIALOGUE STATES
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
            drawLuminosity();
        }
        if (gp.gameState == gp.dialogueState) {
            if (messageOn) {
                drawMessageScreen();
                drawLuminosity();
            }
            if (itemOn) {
                drawItemScreen2();
                drawLuminosity();
            }
        }

        // INVENTORY STATE
        if (gp.gameState == gp.inInventory) {
            int tempScreenX = (2 * gp.screenWidth) / 3;
            // Shift the camera slightly to the right to leave space for the inventory menu
            if (gp.player.screenX < tempScreenX) {
                gp.player.screenX += gp.tileSize;
            }
            if (gp.player.screenX >= tempScreenX) {
                gp.player.screenX = tempScreenX;
                drawInventoryScreen();
            }
            drawPlayerPotion();
            drawPlayerEquipment();
            drawLuminosity();
        }


        if (gp.gameState == gp.gameOver){
            drawGameOverScreen();
        }
        if (transitionOn) {
            if (transitionType == TransitionType.Iris){
                drawIrisTransition();
            }
            if (transitionType == TransitionType.FadeInOut){
                drawFadeInOutTransition();
            }
            if (transitionType == TransitionType.SlideInOut){
                drawSlideInOutTransition();
            }
            if (transitionType == TransitionType.Shutters){
                drawShutterTransition();
            }

        }


    }

    /**
     * Draws the global lighting layer over the screen.
     */
    public void drawLuminosity() {
        if (gp.luminosity <= 1) {
            // Darken the screen
            Color light = new Color(0, 0, 0, (int)((1 - gp.luminosity) * 255));
            g2.setColor(light);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        } else {
            // Brighten the screen
            Color light = new Color(255, 255, 255, (int)((gp.luminosity - 1) * 255));
            g2.setColor(light);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }
    }

    /**
     * Draws the player's inventory layout, item slots, and cursor logic.
     */
    public void drawInventoryScreen() {
        // Draw a dark fading gradient behind the inventory UI
        GradientPaint vignette = new GradientPaint(
                0, 0, new Color(0, 0, 0, 240),
                9 * gp.tileSize, 0, new Color(0, 0, 0, 0)
        );
        g2.setPaint(vignette);
        g2.fillRect(0, 0, 9 * gp.tileSize, gp.screenHeight);

        g2.drawImage(inventoryFrame, 0, 0, null);

        g2.setFont(trunic);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, gp.tileSize / 3f));
        g2.setColor(Color.white);
        g2.drawString("Gear", gp.tileSize / 4, (3 * gp.tileSize) / 2);
        g2.drawString("Single use", gp.tileSize / 4, (7 * gp.tileSize) / 2);
        g2.drawString("Equipment", gp.tileSize / 4, (14 * gp.tileSize) / 2);

        // SLOTS CONFIGURATION
        int slotStartX = gp.tileSize / 2;
        int slotStartY1 = 2 * gp.tileSize;
        int slotStartY2 = 4 * gp.tileSize;
        int slotStartY3 = (15 * gp.tileSize) / 2;
        int slotOffset = 3 * gp.tileSize / 2;
        int slotX;
        int slotY = slotStartY1;

        // DRAW PLAYER'S ACTUAL INVENTORY ITEMS
        for (int i = 0; i < 3; i++) {
            slotX = slotStartX;
            slotY = switch (i) {
                case 0 -> slotStartY1;
                case 1 -> slotStartY2;
                case 2 -> slotStartY3;
                default -> slotY;
            };

            for (int j = 0; j < gp.player.inventory[i].size(); j++) {
                if (j > 5) { // Shift to the next row if exceeding 6 items
                    slotY += slotOffset;
                }
                g2.drawImage(gp.player.inventory[i].get(j).image, slotX, slotY, null);
                slotX += slotOffset;
            }
        }

        // CALCULATE AND DRAW CURSOR POSITIONS
        int cursorX = (slotStartX + (slotOffset) * slotCol) - gp.tileSize / 16;
        int cursorY;
        int cursorWidth = (9 * gp.tileSize) / 8;
        int cursorHeight = (9 * gp.tileSize) / 8;

        if (slotRow == 0) {
            cursorY = (slotStartY1) - gp.tileSize / 16;
        } else if (slotRow == 1) {
            cursorY = (slotStartY2) - gp.tileSize / 16;
        } else if (slotRow == 2) {
            cursorY = (slotStartY2 + slotOffset) - gp.tileSize / 16;
        } else {
            cursorY = (slotStartY3) - gp.tileSize / 16;
        }

        // DRAW CURSOR HIGHLIGHT
        g2.setColor(myOrange);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 5, 5);
    }

    /**
     * Renders the Pause menu.
     */
    public void drawPauseScreen() {
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(trunic);
        g2.setColor(Color.white);
        String text = "Nap time";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 5;
        g2.drawString(text, x, y);

        g2.setFont(lilliput_20);
        FontMetrics metrics = g2.getFontMetrics();

        int buttonWidth = menuSelection.getWidth();
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (pauseCommand.length * buttonHeight) / 2;

        for (int i = 0; i < pauseCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight / 57.6f)));

            if (i == commandNumberPause) {
                if (menuSelectionOrange != null) {
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);
            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth - metrics.stringWidth(pauseCommand[i])) / 2;
            int textY = buttonY + (int)(gp.screenHeight / 15.15f);
            g2.drawString(pauseCommand[i], textX, textY);
        }
    }

    /**
     * Renders the Main Title Screen menu.
     */
    public void drawTitleScreen() {
        int titleX = gp.screenWidth / 12;
        int titleY = gp.screenHeight / 2 - whiteTitle.getHeight() / 2;
        g2.drawImage(whiteTitle, titleX, titleY, null);

        int x = gp.screenWidth * 3 / 4;

        g2.setColor(Color.white);
        if (commandNumber == 0) g2.setColor(myOrange);
        g2.drawString("New Game", getXForCenteredTextAroundX("New Game", x), titleY);

        g2.setColor(Color.white);
        if (commandNumber == 1) g2.setColor(myOrange);
        g2.drawString("Load Game", getXForCenteredTextAroundX("Load Game", x), titleY + gp.screenHeight / 5.76f);

        g2.setColor(Color.white);
        if (commandNumber == 2) g2.setColor(myOrange);
        g2.drawString("Options", getXForCenteredTextAroundX("Options", x), titleY + gp.screenHeight / 2.8f);
    }

    /**
     * Renders the classic dialogue box and NPC text.
     */
    public void drawMessageScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 5;
        drawSubWindow(x, y, width, height, messageWindow);

        g2.setFont(trunic);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, gp.tileSize / 2f));
        x = getXForCenteredTextAroundX(currentDialogue, (2 * x + width) / 2);
        y = (2 * y + height) / 2;
        g2.drawString(currentDialogue, x, y);
    }

    /**
     * Draws the dynamic "Item Found" popup animation (similar to Zelda's chest opening).
     */
    public void drawItemScreen2() {
        if (item == null) return; // Safety check

        // --- 1. CALCULATE THE CIRCLE ---
        // Final targeted size and position
        int finalSize = gp.tileSize * 5;
        int targetX = (11 * gp.tileSize) / 2;
        int targetY = gp.tileSize;

        // Calculate center of the circle (pivot point for zooming)
        int centerX = targetX + finalSize / 2;
        int centerY = targetY + finalSize / 2;

        // Apply scale factor to dimensions
        int currentSize = (int) (finalSize * itemScale);

        // Recalculate X and Y to keep it perfectly centered while zooming
        int currentX = centerX - currentSize / 2;
        int currentY = centerY - currentSize / 2;

        // Draw black background and white outline for the popup circle
        g2.setColor(Color.black);
        g2.fillOval(currentX, currentY, currentSize, currentSize);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(currentX, currentY, currentSize, currentSize);

        // --- 2. DRAW THE ITEM ICON ---
        // Ensure the item image scales dynamically as well
        int itemFinalSize = gp.tileSize * 3;
        int itemCurrentSize = (int) (itemFinalSize * itemScale);

        // Center of the item (slightly offset within the circle)
        int itemCenterX = targetX + gp.tileSize + itemFinalSize / 2;
        int itemCenterY = targetY + gp.tileSize + itemFinalSize / 2;

        int itemDrawX = itemCenterX - itemCurrentSize / 2;
        int itemDrawY = itemCenterY - itemCurrentSize / 2;

        if (item.upAnimator != null) {
            item.upAnimator.draw(g2, itemDrawX, itemDrawY, itemCurrentSize, itemCurrentSize);
        } else {
            g2.drawImage(item.image, itemDrawX, itemDrawY, itemCurrentSize, itemCurrentSize, null);
        }

        // --- 3. DRAW THE TEXT/MESSAGE ---
        // Only show text if the animation has expanded significantly
        if (itemScale > 0.1f) {
            g2.setFont(trunic);

            // Dynamically scale the text window background
            int textWindowWidth = 7 * gp.tileSize;
            int textWindowHeight = 2 * gp.tileSize;
            int currentTextW = (int)(textWindowWidth * itemScale);
            int currentTextH = (int)(textWindowHeight * itemScale);

            // Compute text position
            int textY = 7 * gp.tileSize;
            int textWindowCenterX = (targetX - gp.tileSize) + (textWindowWidth / 2);
            int textWindowCenterY = (textY - (3 * gp.tileSize) / 2) + (textWindowHeight / 2);

            int winX = textWindowCenterX - currentTextW / 2;
            int winY = textWindowCenterY - currentTextH / 2;

            drawSubWindow(winX, winY, currentTextW, currentTextH, messageWindow);

            // Wait until animation is mostly finished before drawing the font to prevent visual glitches
            if (itemScale > 0.8f) {
                g2.setColor(Color.white);
                int textX = getXforCenteredText(item.name);
                g2.drawString(item.name, textX, textY);
            }
        }
    }

    /**
     * Main Options menu screen.
     */
    public void drawOptionScreen() {
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        drawSubWindow(0, 0, gp.screenWidth, gp.screenHeight, optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Option");
        int y = (3 * gp.tileSize) / 2;
        g2.drawString("Option", x, y);

        g2.setFont(lilliput_20);
        FontMetrics metrics = g2.getFontMetrics();

        int buttonWidth = menuSelection.getWidth();
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (optionCommand.length * buttonHeight) / 2;

        for (int i = 0; i < optionCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight / 57.6f)));

            if (i == commandNumberOption) {
                if (menuSelectionOrange != null) {
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);
            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth - metrics.stringWidth(optionCommand[i])) / 2;
            int textY = buttonY + (int)(gp.screenHeight / 15.15f);
            g2.drawString(optionCommand[i], textX, textY);
        }
    }

    /**
     * Sub-menu to configure Music and Sound Effects volumes.
     */
    public void drawAudioScreen() {
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        drawSubWindow(0, 0, gp.screenWidth, gp.screenHeight, optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Audio");
        int y = (3 * gp.tileSize) / 2;
        g2.drawString("Audio", x, y);

        g2.setFont(lilliput_20);
        int buttonWidth = (int)(1.3f * menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (audioCommand.length * buttonHeight) / 2;

        for (int i = 0; i < audioCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight / 57.6f)));

            if (i == commandNumberAudio) {
                if (i == 2 && menuSelectionOrange != null) {
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                } else if (menuSelectionOrange2 != null) {
                    g2.drawImage(menuSelectionOrange2, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);
            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth) / 8;
            int textY = buttonY + (int)(gp.screenHeight / 15.15f);
            g2.drawString(audioCommand[i], textX, textY);

            // Draw current volume percentages
            if (i == 0) {
                g2.setColor(myOrange);
                textX += 7 * gp.tileSize + gp.tileSize / 2;
                g2.drawString(Math.round(gp.music.currentVolume * 100) + "%", textX, textY);
            }
            if (i == 1) {
                g2.setColor(myOrange);
                textX += 7 * gp.tileSize + gp.tileSize / 2;
                g2.drawString(Math.round(gp.soundEffects.currentVolume * 100) + "%", textX, textY);
            }
        }
    }

    /**
     * Sub-menu to configure screen resolution and display mode (Fullscreen/Windowed).
     */
    public void drawGraphicsScreen() {
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        drawSubWindow(0, 0, gp.screenWidth, gp.screenHeight, optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Graphics");
        int y = (3 * gp.tileSize) / 2;
        g2.drawString("Graphics", x, y);

        g2.setFont(lilliput_15);
        int buttonWidth = (int)(1.3f * menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (graphicCommand.length * buttonHeight) / 2;

        for (int i = 0; i < graphicCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight / 57.6f)));

            if (i == commandNumberGraphic) {
                if (i == 2 && menuSelectionOrange != null) {
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                } else if (menuSelectionOrange2 != null) {
                    g2.drawImage(menuSelectionOrange2, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);
            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth) / 8;
            int textY = buttonY + (int)(gp.screenHeight / 15.15f);
            g2.drawString(graphicCommand[i], textX, textY);

            // Display current parameters
            if (i == 0) {
                g2.setColor(myOrange);
                textX += 5 * gp.tileSize + gp.tileSize / 2;
                g2.drawString(gp.displayMode, textX, textY);
            }
            if (i == 1) {
                g2.setColor(myOrange);
                textX += 5 * gp.tileSize + gp.tileSize / 2;
                g2.drawString(gp.screenWidth + "x" + gp.screenHeight, textX, textY);
            }
        }
    }

    public void drawControleScreen(){
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        drawSubWindow(0, 0, gp.screenWidth, gp.screenHeight, optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Control");
        int y = (3 * gp.tileSize) / 2;
        g2.drawString("Control", x, y);

        // Safety check: verify that the JSON file is loaded
        if (controleRootNode == null || !controleRootNode.has("controls")) {
            g2.setFont(lilliput_20);
            g2.setColor(Color.RED);
            String error = "Error: controls file not loaded";
            g2.drawString(error, getXforCenteredText(error), gp.screenHeight / 2);
            return;
        }

        JsonNode controlsArray = controleRootNode.get("controls");

        // Safety check: verify that categories exist
        if (controlsArray.size() == 0) {
            g2.setFont(lilliput_20);
            g2.setColor(Color.RED);
            String error = "No controls defined";
            g2.drawString(error, getXforCenteredText(error), gp.screenHeight / 2);
            return;
        }

        // Clamp commandNumberControle within the valid range
        if (commandNumberControle < 0) commandNumberControle = 0;
        if (commandNumberControle >= totalCategories) commandNumberControle = totalCategories - 1;

        // Retrieve the current category
        JsonNode currentCategory = controlsArray.get(commandNumberControle);
        String categoryName = currentCategory.get("category").asText();
        JsonNode actions = currentCategory.get("actions");

        // --- Display the category name ---
        g2.setFont(trunic);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, gp.tileSize /1.5f));
        g2.setColor(myOrange);
        int categoryX = getXforCenteredText(categoryName);
        int categoryY = y + 2*gp.tileSize;
        g2.drawString(categoryName, categoryX, categoryY);

        // --- Display actions (label + key) ---
        g2.setFont(lilliput_20);
        FontMetrics metrics = g2.getFontMetrics();

        int startY = categoryY + gp.tileSize;
        int lineHeight = (int) (gp.tileSize * 0.6f);
        int keyColumnX = gp.screenWidth / 2 + 3*gp.tileSize;  // Key column on the right

        for (int i = 0; i < actions.size(); i++) {
            JsonNode action = actions.get(i);
            String label = action.get("label").asText();
            String key = action.get("key").asText();

            int lineY = startY + (i * lineHeight);

            // Label (aligned to the left of the center)
            g2.setColor(Color.WHITE);
            int labelX = gp.screenWidth / 2 - gp.tileSize * 6;
            g2.drawString(label, labelX, lineY);

            // Key (aligned to the right of the center, with orange background)
            g2.setColor(myOrange);
            int keyWidth = metrics.stringWidth(key) + gp.tileSize / 4;
            int keyHeight = (int) (gp.tileSize * 0.5f);
            int keyX = keyColumnX - gp.tileSize / 8;
            int keyY = lineY - (int) (gp.tileSize * 0.46f);

            // Orange rounded rectangle background
            g2.fillRoundRect(keyX, keyY, keyWidth, keyHeight, 5, 5);

            // Key text in white
            g2.setColor(Color.WHITE);
            int textX = keyX + gp.tileSize / 8;
            int textY = lineY;
            g2.drawString(key, textX, textY);
        }

        // --- Navigation indicators (up/down arrows) ---
        g2.setFont(lilliput_15);
        metrics = g2.getFontMetrics();
        g2.setColor(Color.LIGHT_GRAY);
        int navY = gp.screenHeight - 3*gp.tileSize/2;

        // Left arrow if not the first category
        if (commandNumberControle > 0) {
            String leftArrow = "<- " + controlsArray.get(commandNumberControle - 1).get("category").asText();
            g2.drawString(leftArrow, (3*gp.tileSize)/2, navY);
        }

        // Right arrow if not the last category
        if (commandNumberControle < totalCategories - 1) {
            String rightArrow = controlsArray.get(commandNumberControle + 1).get("category").asText() + " ->";
            int rightX = gp.screenWidth - (3*gp.tileSize)/2 - metrics.stringWidth(rightArrow);
            g2.drawString(rightArrow, rightX, navY);
        }

        // "ESC to return" instruction
        g2.setColor(Color.GRAY);
        String escText = "ESC to return";
        int escX = getXforCenteredText(escText);
        int escY = gp.screenHeight - gp.tileSize;
        g2.drawString(escText, escX, escY);
    }

    /**
     * Menu allowing the player to select which save file to load.
     */
    public void drawLoadScreen() {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        drawSubWindow(0, 0, gp.screenWidth, gp.screenHeight, optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Save Data");
        int y = (3 * gp.tileSize) / 2;
        g2.drawString("Save Data", x, y);

        g2.setFont(lilliput_15);
        int buttonWidth = (int)(1.3f * menuSelection.getWidth());
        int buttonHeight = (3 * menuSelection.getHeight()) / 2;
        int startY = gp.screenHeight / 2 - ((loadCommand.size() - 1) * buttonHeight + buttonHeight / 2) / 2;

        for (int i = 0; i < loadCommand.size(); i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight / 57.6f)));

            // The "Cancel" button has a smaller height
            if (gp.ui.loadCommand.get(i).equals("Cancel")) {
                buttonHeight = (3 * menuSelection.getHeight()) / 4;
            } else {
                buttonHeight = (3 * menuSelection.getHeight()) / 2;
            }

            if (i == commandNumberLoad) {
                if (menuSelectionOrange != null) {
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);
            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            // Draw text
            int textX = buttonX + (buttonWidth) / 8;
            int textY = buttonY + (int)(gp.screenHeight / 15.15f);
            if (gp.ui.loadCommand.get(i).equals("Cancel")) {
                textY -= (int)(gp.tileSize / 9.6f);
            }
            g2.drawString(loadCommand.get(i), textX, textY);

            // Draw visual save info (Inventory icons from save preview)
            int j = 0;
            if (i < 3 && gp.saves[i] != null) {
                for (SuperObject obj : gp.saves[i].getInventoryEquipment()) {
                    g2.drawImage(obj.image, textX + 5 * gp.tileSize + j * (2 * gp.tileSize) / 3, textY - (gp.tileSize / 3), (2 * gp.tileSize) / 3, (2 * gp.tileSize) / 3, null);
                    j++;
                }
                // Draw playtime from save
                g2.drawString(uTool.getTimeFromFrame(gp.saves[i].getTimeSpend(), gp.FPS), textX, textY + gp.tileSize / 2);
            }
        }
    }

    /**
     * Confirmation menu specifically asking whether to load or delete the selected save file.
     */
    public void drawLoadSelectionScreen() {
        String fileName = "File #" + (gp.currentSaveIndex + 1);
        g2.setColor(Color.white);
        drawSubWindow((4 * gp.tileSize), (2 * gp.tileSize), gp.screenWidth / 2, gp.screenHeight / 2, optionWindow);

        g2.setFont(lilliput_20);
        int x = getXforCenteredText(fileName);
        int y = (int)(gp.screenHeight / 3.5f);
        g2.drawString(fileName, x, y);

        FontMetrics metrics = g2.getFontMetrics();
        int buttonWidth = (menuSelection.getWidth()) / 2;
        int buttonHeight = (2 * menuSelection.getHeight()) / 3;
        int startY = (int)(gp.screenHeight / 2.3f) - (loadSelectionCommand.length * buttonHeight) / 2;

        for (int i = 0; i < loadSelectionCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight / 57.6f)));

            if (i == commandNumberLoadSelection) {
                if (menuSelectionOrange != null) {
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);
            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth - metrics.stringWidth(loadSelectionCommand[i])) / 2;
            int textY = buttonY + (int)(gp.screenHeight / 20f);
            g2.drawString(loadSelectionCommand[i], textX, textY);
        }
    }

    /**
     * Menu allowing the user to pick an empty slot to create a brand new game,
     * warning them if they select an already occupied slot.
     */
    public void drawNewGameSlotScreen() {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth / 2, gp.screenHeight / 2),
                2 * gp.screenWidth,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 240), new Color(0, 0, 0, 100)});
        g2.setPaint(vignette);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        drawSubWindow(0, 0, gp.screenWidth, gp.screenHeight, optionWindow);

        g2.setFont(lilliput_40);
        g2.setColor(Color.white);
        int x = getXforCenteredText("New Game");
        int y = (3 * gp.tileSize) / 2;
        g2.drawString("New Game", x, y);

        g2.setFont(lilliput_20);
        FontMetrics metrics = g2.getFontMetrics();

        // Retrieve dynamically parsed slot names (checking for overwrite)
        String[] slotLabels = buildSlotLabels();
        String[] allEntries = {slotLabels[0], slotLabels[1], slotLabels[2], "Cancel"};

        int buttonWidth = (int) (1.3f * menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (allEntries.length * buttonHeight) / 2;

        for (int i = 0; i < allEntries.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + i * (buttonHeight + (int) (gp.screenHeight / 57.6f));

            if (i == commandNumberNewSlot) {
                g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                g2.setColor(Color.WHITE);
            } else {
                g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth - metrics.stringWidth(allEntries[i])) / 2;
            int textY = buttonY + (int) (gp.screenHeight / 15.15f);
            g2.drawString(allEntries[i], textX, textY);
        }
    }

    /**
     * Renders the Game Over screen.
     * Black background, "Game Over" title in Trunic font,
     * options displayed like the title screen menu.
     */
    public void drawGameOverScreen() {

        // Only show menu once the Iris is fully closed
        if (transitionOn && transitionState == 1) return;

        // --- 1. BLACK BACKGROUND ---
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // --- 2. "GAME OVER" TITLE ---
        g2.setFont(trunic);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, gp.tileSize * 1.5f));
        String title = "Game Over";
        int titleX = getXforCenteredText(title);
        int titleY = gp.screenHeight / 3;
        g2.setColor(Color.white);
        g2.drawString(title, titleX, titleY);

        // --- 3. OPTIONS (same style as title screen) ---
        g2.setFont(lilliput_20);
        int optionsX = gp.screenWidth / 2;
        int startY = (int)(gp.screenHeight * 0.65f);
        int gap = (int)(gp.screenHeight / 5.76f);

        for (int i = 0; i < gameOverCommand.length; i++) {
            g2.setColor(commandNumberGameOver == i ? myOrange : Color.white);
            int textX = getXForCenteredTextAroundX(gameOverCommand[i], optionsX);
            g2.drawString(gameOverCommand[i], textX, startY + (i * gap));
        }
        // RESET OPACITY TO NORMAL
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        gp.ui.player.draw(g2);
    }

    /**
     * Constructs labels for the 3 save slots indicating if they are free or occupied.
     * @return String array representing the text of the slots.
     */
    private String[] buildSlotLabels() {
        String[] labels = new String[3];
        for (int i = 0; i < 3; i++) {
            String slotName = "File #" + (i + 1);
            if (gp.saves[i] != null) {
                labels[i] = slotName + " — Overwrite";
            } else {
                labels[i] = slotName + " — Empty";
            }
        }
        return labels;
    }

    /**
     * Helper to draw a given background UI image safely.
     */
    public void drawSubWindow(int x, int y, int width, int height, BufferedImage image) {
        g2.drawImage(image, x, y, width, height, null);
    }

    /**
     * Dynamically renders the player's current health level.
     * It uses a loop to render the middle section to seamlessly resize the bar.
     */
    public void drawPlayerHealth() {
        int x = gp.screenWidth / 10;
        int topY = gp.screenHeight * 2 / 3;
        int healthBarHeight = (int)(gp.screenHeight / 3.5f);

        g2.drawImage(healthOverlay, x, topY, healthOverlay.getWidth(), healthBarHeight, null);

        int midHeight = healthMiddle.getHeight();
        float hp = player.displayedHealth / player.maxHealth;
        float stopY = topY + ((1 - hp) * (healthBarHeight - healthTop.getHeight()));
        int botY = topY + healthBarHeight;

        // Render the cap on top of the health bar
        if (stopY >= botY - healthTop.getHeight()) {
            g2.drawImage(healthTop, x, (botY - healthTop.getHeight()), null);
        } else {
            g2.drawImage(healthTop, x, (int)(stopY), null);
        }

        // Fill the rest with the middle texture piece
        int y = botY - midHeight;
        while (y >= (int)stopY) {
            g2.drawImage(healthMiddle, x, y, null);
            y--;
        }
    }

    /**
     * Dynamically renders the player's current stamina/endurance level using identical logic to health.
     */
    public void drawPlayerEndurance() {
        int x = gp.screenWidth / 10 + (int)(gp.screenHeight / 11.52f);
        int topY = gp.screenHeight * 2 / 3;
        int healthBarHeight = (int)(gp.screenHeight / 3.5f);

        g2.drawImage(enduranceOverlay, x, topY, enduranceOverlay.getWidth(), healthBarHeight, null);

        int midHeight = enduranceMiddle.getHeight();
        float ed = player.displayedEndurance / player.maxEndurance;
        float stopY = topY + ((1 - ed) * (healthBarHeight - enduranceTop.getHeight()));
        int botY = topY + healthBarHeight;

        if (stopY >= botY - enduranceTop.getHeight()) {
            g2.drawImage(enduranceTop, x, (botY - enduranceTop.getHeight()), null);
        } else {
            g2.drawImage(enduranceTop, x, (int)(stopY), null);
        }

        int y = botY - midHeight;
        while (y >= (int)stopY) {
            g2.drawImage(enduranceMiddle, x, y, null);
            y--;
        }
    }

    /**
     * Dynamically renders the player's current mana level.
     */
    public void drawPlayerMana() {
        int x = gp.screenWidth / 10 + (int)(gp.screenHeight / 5.76f);
        int topY = gp.screenHeight * 2 / 3;
        int healthBarHeight = (int)(gp.screenHeight / 3.5f);

        g2.drawImage(manaOverlay, x, topY, manaOverlay.getWidth(), healthBarHeight, null);

        int midHeight = manaMiddle.getHeight();
        float ed = player.displayedMana / player.maxMana;
        float stopY = topY + ((1 - ed) * (healthBarHeight - manaTop.getHeight()));
        int botY = topY + healthBarHeight;

        if (stopY >= botY - manaTop.getHeight()) {
            g2.drawImage(manaTop, x, (botY - manaTop.getHeight()), null);
        } else {
            g2.drawImage(manaTop, x, (int)(stopY), null);
        }

        int y = botY - midHeight;
        while (y >= (int)stopY) {
            g2.drawImage(manaMiddle, x, y, null);
            y--;
        }
    }

    /**
     * Displays the potion stock on the top right.
     * Applies a small transition effect if the inventory is being opened.
     */
    public void drawPlayerPotion() {
        int tempX, tempY, tempSize;

        // Setup transition offsets when inventory opens
        if (gp.gameState == gp.inInventory) {
            tempX = (gp.maxScreenCol - 2) * gp.tileSize - (gp.tileSize / 3);
            tempY = 2 * gp.tileSize + (gp.tileSize / 3);
            tempSize = gp.tileSize;

            if (tempX > potionXPos) potionXPos += 1; else potionXPos = tempX;
            if (tempY > potionYPos) potionYPos += 1; else potionYPos = tempY;
            if (tempSize > potionSize) potionSize += 1; else potionSize = tempSize;
        } else {
            tempX = (gp.maxScreenCol - 2) * gp.tileSize;
            tempY = 2 * gp.tileSize;
            tempSize = (2 * gp.tileSize / 3);
            potionXPos = tempX;
            potionYPos = tempY;
            potionSize = tempSize;
        }

        // Draw Full Potions
        for (int i = 0; i < gp.player.potionNotUsed; i++) {
            g2.drawImage(potionFull, potionXPos, potionYPos, potionSize, potionSize, null);
            potionXPos -= potionSize;
        }
        // Draw Empty Potions
        for (int i = 0; i < gp.player.maxPotion - gp.player.potionNotUsed; i++) {
            g2.drawImage(potionEmpty, potionXPos, potionYPos, potionSize, potionSize, null);
            potionXPos -= potionSize;
        }

        // Reset positional tracking memory
        potionXPos = tempX;
        potionYPos = tempY;
        potionSize = tempSize;
    }

    /**
     * Renders the bottom right HUD showing currently assigned shortcut equipment (J, K, L).
     */
    public void drawPlayerEquipment() {
        int tempX, tempY, tempCirSize;

        if (gp.gameState == gp.inInventory) {
            tempX = ((gp.maxScreenCol - 2) * gp.tileSize);
            tempY = (2 * gp.tileSize / 3);
            tempCirSize = (3 * gp.tileSize / 2);

            if (tempX > equipmentXPos) equipmentXPos += 1; else equipmentXPos = tempX;
            if (tempY > equipmentYPos) equipmentYPos += 1; else equipmentYPos = tempY;
            if (tempCirSize > equipmentSize) equipmentSize += 1; else equipmentSize = tempCirSize;
        } else {
            tempX = ((gp.maxScreenCol - 2) * gp.tileSize);
            tempY = (2 * gp.tileSize / 3);
            tempCirSize = gp.tileSize;
            equipmentXPos = tempX;
            equipmentYPos = tempY;
            equipmentSize = tempCirSize;
        }

        int jX = equipmentXPos - 2 * equipmentSize;
        int kX = equipmentXPos - equipmentSize;
        int lX = equipmentXPos;
        int y = equipmentYPos;

        SuperObject jEquip = gp.player.jEquip;
        SuperObject kEquip = gp.player.kEquip;
        SuperObject lEquip = gp.player.lEquip;

        g2.setColor(new Color(47, 47, 47, 200));
        g2.fillOval(jX, y, equipmentSize, equipmentSize);
        g2.fillOval(kX, y, equipmentSize, equipmentSize);
        g2.fillOval(lX, y, equipmentSize, equipmentSize);

        if (jEquip != null) {
            g2.drawImage(jEquip.image, jX + equipmentSize / 6, y + equipmentSize / 6, (2 * equipmentSize / 3), (2 * equipmentSize / 3), null);
        }
        if (kEquip != null) {
            g2.drawImage(kEquip.image, kX + equipmentSize / 6, y + equipmentSize / 6, (2 * equipmentSize / 3), (2 * equipmentSize / 3), null);
        }
        if (lEquip != null) {
            g2.drawImage(lEquip.image, lX + equipmentSize / 6, y + equipmentSize / 6, (2 * equipmentSize / 3), (2 * equipmentSize / 3), null);
        }

        equipmentXPos = tempX;
        equipmentYPos = tempY;
        equipmentSize = tempCirSize;
    }

    /**
     * Calculates the exact screen X coordinate to perfectly center a string of text.
     */
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    /**
     * Calculates the exact screen X coordinate to center a string of text around a specific X point.
     */
    public int getXForCenteredTextAroundX(String text, int x) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return x - length / 2;
    }

    /**
     * Draws the circular "Iris wipe" transition effect heavily used for scene/map changes.
     * Uses extremely thick borders rendering inward and outward to ensure the screen is fully masked.
     */
    public void drawIrisTransition() {
        int strokeWidth = gp.screenWidth * 2;

        g2.setStroke(new BasicStroke(strokeWidth));
        g2.setColor(Color.BLACK);

        double currentRadius = transitionSize / 2.0;
        double drawingRadius = currentRadius + (strokeWidth / 2.0);

        // Center on the player
        double centerX = gp.player.screenX + gp.tileSize / 2f;
        double centerY = gp.player.screenY + gp.tileSize / 2f;

        double x = centerX - drawingRadius;
        double y = centerY - drawingRadius;
        double size = drawingRadius;

        g2.drawOval((int) x, (int) y, (int) size * 2, (int) size * 2);

        // Revert to default stroke just in case
        g2.setStroke(new BasicStroke(1));
    }

    /**
     * Draws fade in and out transition effect heavily used for scene/map changes.
     * Drawing a fully black rectangle and changing the alpha composite
     */
    public void drawFadeInOutTransition(){
        g2.setColor(new Color(0, 0, 0, (int)(transitionSize)));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

    }

    /**
     * Draws the slide in and out transition effect heavily used for scene/map changes.
     * Draws a black rectangle and changing its width
     */
    public void drawSlideInOutTransition(){
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,(int)(transitionSize),gp.screenHeight);
    }

    /**
     * Draws the Shutter transition effect  used for scene/map changes.
     * Draws two rectangles and changing their widths and positions
     */
    public void drawShutterTransition(){
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,(int)(transitionSize),gp.screenHeight);
        g2.fillRect((int)(gp.screenWidth-transitionSize),0,(int)(transitionSize+gp.tileSize),gp.screenHeight);
    }


    /**
     * Initiates the screen Transition animation. Begins with the closing state
     * @param action The chunk of code (Runnable block) to execute once the screen is fully dark.
     */
    public void startTransition(TransitionType type ,int transitionSpeed,Runnable action) {
        maxTransitionSize = switch (type){
            case TransitionType.Iris -> 2*gp.screenWidth;
            case TransitionType.FadeInOut -> 255;
            case TransitionType.SlideInOut -> gp.screenWidth;
            case  TransitionType.Shutters ->gp.screenWidth/2f;
        };
        transitionType = type;
        this.transitionSpeed =  transitionSpeed;
        onTransitionComplete = action;
        transitionOn = true;
        transitionState = 1; // Begins closing sequence
        transitionSize = switch (type) {
            case TransitionType.Iris -> maxTransitionSize;
            case TransitionType.FadeInOut ,TransitionType.SlideInOut  ,TransitionType.Shutters -> 0;
        };
    }

    public void startOpeningTransition(TransitionType type ,int transitionSpeed) {
        transitionSize = switch (type){
            case TransitionType.Iris -> 0;
            case TransitionType.FadeInOut -> 255;
            case TransitionType.SlideInOut -> gp.screenWidth;
            case  TransitionType.Shutters ->gp.screenWidth/2f;
        };
        transitionType = type;
        this.transitionSpeed =  transitionSpeed;
        transitionOn = true;
        transitionState = 2; // Begins opening sequence
        maxTransitionSize = switch (type) {
            case TransitionType.Iris -> 2*gp.screenWidth;
            case TransitionType.FadeInOut ,TransitionType.SlideInOut  ,TransitionType.Shutters -> 0;
        };
    }
}