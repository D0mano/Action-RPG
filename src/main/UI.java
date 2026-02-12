package main;

import entity.Player;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font lilliput_40,trunic,lilliput_20,lilliput_15;
    public boolean messageOn = false;
    public String message = "";
    public boolean itemOn = false;
    public SuperObject item = null;
    int messageCounter = 0;
    public String currentDialogue = "";
    public boolean gameFinished = false;
    Color myOrange = new Color(206, 157, 58);
    Player player;
    BufferedImage healthTop,healthOverlay, healthMiddle;
    BufferedImage enduranceTop,enduranceOverlay,enduranceMiddle;
    BufferedImage manaTop,manaOverlay,manaMiddle;
    BufferedImage menuSelection ,menuSelectionOrange,menuSelectionOrange2;
    BufferedImage messageWindow;
    BufferedImage optionWindow;
    BufferedImage potionFull, potionEmpty;
    BufferedImage inventoryFrame;
    int commandNumber = 0;

    public String[] pauseCommand = {"Return to Game","Options","Quit"};
    public int commandNumberPause = 0;
    public String[] optionCommand = {"Audio","Graphics","Return"};
    public int commandNumberOption = 0;

    public String[] audioCommand = {"Musics","Sounds Effects","Return"};
    public int commandNumberAudio = 0;

    public String[] graphicCommand = {"Display Mode","Resolution","Return"};
    public int commandNumberGraphic = 0;

    public int slotRow = 0;
    public int slotCol = 0;

    public UtilityTool uTool = new UtilityTool();

    public int potionXPos,potionYPos;
    public int potionSize;
    public int equipmentXPos,equipmentYPos;
    public int equipmentSize;

    float itemScale = 0f;

    // TRANSITION IRIS
    public boolean transitionOn = false;
    public int transitionState = 0; // 0 = nothing, 1 = closing , 2 = opening
    public int transitionCounter = 0;
    public double transitionSize = 0;
    public double maxTransitionSize;
    private Runnable onTransitionComplete;



    BufferedImage whiteTitle,blackTitle;
    public UI(GamePanel gp,Player player) {
        this.gp = gp;
        this.player = player;
        maxTransitionSize = gp.screenWidth * 2.0;


        try {
            // Tu changes juste l'extension du fichier ici
            InputStream is = getClass().getResourceAsStream("/fonts/Lilliput Steps.otf");
            InputStream is2 = getClass().getResourceAsStream("/fonts/Trunic-Bold.otf");

            // IMPORTANT : Tu gardes "TRUETYPE_FONT", ça marche pour les .otf aussi
            Font lilliputFont = Font.createFont(Font.TRUETYPE_FONT, is);
            Font Trunic = Font.createFont(Font.TRUETYPE_FONT, is2);

            // On redimensionne
            lilliput_40 = lilliputFont.deriveFont(Font.PLAIN, gp.tileSize);
            lilliput_20 = lilliputFont.deriveFont(Font.PLAIN,(gp.tileSize*(2/3f)));
            lilliput_15 = lilliputFont.deriveFont(Font.PLAIN,(gp.tileSize*(1/2f)));
            trunic = Trunic.deriveFont(Font.BOLD, gp.tileSize);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }


        healthOverlay = setup("/player/health_overlay",(int)(gp.scale*(2/3f)));
        healthMiddle = setup("/player/health_mid",(int)(gp.scale*(2/3f)));
        healthTop = setup("/player/health_top",(int)(gp.scale*(2/3f)));

        enduranceTop = setup("/player/endurance_top",(int)(gp.scale*(2/3f)));
        enduranceMiddle = setup("/player/endurance_mid",(int)(gp.scale*(2/3f)));
        enduranceOverlay = healthOverlay;

        manaTop = setup("/player/mana_top",(int)(gp.scale*(2/3f)));
        manaMiddle = setup("/player/mana_mid",(int)(gp.scale*(2/3f)));
        manaOverlay = healthOverlay;

        whiteTitle = setup("/titleScreen/tunic_logo_white",(int)(gp.scale*(5/3f)));
        blackTitle = setup("/titleScreen/tunic_logo_black",1);

        menuSelection = setup("/menu/menuOverlayWhite",gp.scale);
        menuSelectionOrange = setup("/menu/menuOverlayOrange",gp.scale);
        menuSelectionOrange2 = setup("/menu/menuOverlayOrange2",gp.scale);

        inventoryFrame = setup("/menu/inventoryFrame",gp.scale);


        messageWindow = setup("/menu/window1",gp.scale);

        optionWindow = setup("/menu/optionOverlay",gp.scale);

        potionFull = setup("/player/potion_full",gp.scale);
        potionEmpty = setup("/player/potion_empty",gp.scale);





    }
    public void reload(){
        try {
            // Tu changes juste l'extension du fichier ici
            InputStream is = getClass().getResourceAsStream("/fonts/Lilliput Steps.otf");
            InputStream is2 = getClass().getResourceAsStream("/fonts/Trunic-Bold.otf");

            // IMPORTANT : Tu gardes "TRUETYPE_FONT", ça marche pour les .otf aussi
            Font lilliputFont = Font.createFont(Font.TRUETYPE_FONT, is);
            Font Trunic = Font.createFont(Font.TRUETYPE_FONT, is2);

            // On redimensionne
            lilliput_40 = lilliputFont.deriveFont(Font.PLAIN, gp.tileSize);
            lilliput_20 = lilliputFont.deriveFont(Font.PLAIN,(gp.tileSize*(2/3f)));
            lilliput_15 = lilliputFont.deriveFont(Font.PLAIN,(gp.tileSize*(1/2f)));
            trunic = Trunic.deriveFont(Font.BOLD, gp.tileSize);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        healthOverlay = setup("/player/health_overlay",(int)(gp.scale*(2/3f)));
        healthMiddle = setup("/player/health_mid",(int)(gp.scale*(2/3f)));
        healthTop = setup("/player/health_top",(int)(gp.scale*(2/3f)));

        enduranceTop = setup("/player/endurance_top",(int)(gp.scale*(2/3f)));
        enduranceMiddle = setup("/player/endurance_mid",(int)(gp.scale*(2/3f)));
        enduranceOverlay = healthOverlay;

        manaTop = setup("/player/mana_top",(int)(gp.scale*(2/3f)));
        manaMiddle = setup("/player/mana_mid",(int)(gp.scale*(2/3f)));
        manaOverlay = healthOverlay;

        whiteTitle = setup("/titleScreen/tunic_logo_white",(int)(gp.scale*(5/3f)));
        blackTitle = setup("/titleScreen/tunic_logo_black",1);

        menuSelection = setup("/menu/menuOverlayWhite",gp.scale);
        menuSelectionOrange = setup("/menu/menuOverlayOrange",gp.scale);
        menuSelectionOrange2 = setup("/menu/menuOverlayOrange2",gp.scale);

        inventoryFrame = setup("/menu/inventoryFrame",gp.scale);


        messageWindow = setup("/menu/window1",gp.scale);

        optionWindow = setup("/menu/optionOverlay",gp.scale);

        potionFull = setup("/player/potion_full",gp.scale);
        potionEmpty = setup("/player/potion_empty",gp.scale);

    }

    public void update(){
        if (gp.gameState == gp.dialogueState){
            if (item.upAnimator != null){
                item.upAnimator.update();
            }

        }
        if (itemOn) {
            // Vitesse de l'animation (0.1f signifie 10% plus grand par frame)
            if (itemScale < 1f) {
                itemScale += 0.1f;
                if (itemScale > 1f) itemScale = 1f; // On plafonne à 1 (taille normale)
            }
        } else {
            // Reset l'animation si la fenêtre est fermée
            itemScale = 0f;
        }
        if (transitionOn) {
            double speed = maxTransitionSize / 60; //Do the transition in ~60 frames (1 sec)

            // CLOSING
            if (transitionState == 1) {


                transitionSize -= speed;
                if (transitionSize <= 0) {
                    transitionSize = 0;
                    if (onTransitionComplete != null) {
                        onTransitionComplete.run();
                    }
                    transitionState = 2;
                }
            }
            // OPENING
            else if (transitionState == 2) {
                transitionSize += speed;
                if (transitionSize >= maxTransitionSize) {
                    transitionSize = maxTransitionSize;
                    transitionOn = false; // Transition finie
                    transitionState = 0;
                }
            }
        }

    }

    public BufferedImage setup(String imageName, float scale) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/UI/" + imageName + ".png"));
            image = uTool.scaleImage(image, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void showMessage(String message) {
        this.message = message;
        this.messageCounter++;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(lilliput_40);
        g2.setColor(Color.white);
        if(gp.gameState == gp.titleState){
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

            drawTitleScreen();

        }
        if(gp.gameState == gp.optionState){
            if (gp.previousState == gp.titleState){
                g2.setColor(Color.black);
                g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
            }
            drawOptionScreen();
        }

        if (gp.gameState == gp.audioSettingstate){
            if (gp.previousState == gp.titleState){
                g2.setColor(Color.black);
                g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
            }
            drawAudioScreen();
        }

        if (gp.gameState == gp.graphicsSettingstate){
            if (gp.previousState == gp.titleState){
                g2.setColor(Color.black);
                g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
            }
            drawGraphicsScreen();
        }

        if (gp.gameState == gp.playState){
            drawPlayerHealth();
            drawPlayerEndurance();
            drawPlayerMana();
            drawPlayerPotion();
            drawPlayerEquipment();
            drawLuminosity();
            if (transitionOn){
                drawIrisTransition();
            }


        }
        if (gp.gameState == gp.pauseState){
            drawPauseScreen();
            drawLuminosity();

        }
        if(gp.gameState == gp.dialogueState){
            if (messageOn){

                drawMessageScreen();
                drawLuminosity();
            }
            if (itemOn){
                drawItemScreen2();
                drawLuminosity();
            }
        }
        if (gp.gameState == gp.inInventory){

            int tempScreenX = (2*gp.screenWidth)/3;
            if (gp.player.screenX < tempScreenX){
                gp.player.screenX += gp.tileSize;
            }
            if (gp.player.screenX >= tempScreenX){
                gp.player.screenX = tempScreenX ;
                drawInventoryScreen();

            }
            drawPlayerPotion();
            drawPlayerEquipment();
            drawLuminosity();

        }
    }
    public void drawLuminosity(){
         // LUMINOSITY
         if (gp.luminosity <= 1){
             Color light = new Color(0,0,0,(int)((1-gp.luminosity)*255));
             g2.setColor(light);
             g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
         }
         else{
             Color light = new Color(255,255,255,(int)((gp.luminosity-1)*255));
             g2.setColor(light);
             g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
         }
     }
    public void drawInventoryScreen(){
        GradientPaint vignette = new GradientPaint(
                0,0,new Color(0,0,0,240),
                9*gp.tileSize,0,new Color(0,0,0,0)
        );
        g2.setPaint(vignette);
        g2.fillRect(0,0,9*gp.tileSize,gp.screenHeight);

        g2.drawImage(inventoryFrame,0,0,null);
        g2.setFont(trunic);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, gp.tileSize/3f));
        g2.setColor(Color.white);
        g2.drawString("Gear",gp.tileSize/4,(3*gp.tileSize)/2);
        g2.drawString("Single use",gp.tileSize/4,(7*gp.tileSize)/2);
        g2.drawString("Equipment",gp.tileSize/4,(14*gp.tileSize)/2);
        // SLOTS
        int slotStartX = gp.tileSize/2;
        int slotStartY1 = 2*gp.tileSize;
        int slotStartY2 = 4*gp.tileSize;
        int slotStartY3 = (15*gp.tileSize)/2;
        int slotOffset = 3*gp.tileSize/2;
        int slotX ;
        int slotY = slotStartY1;

        // DRAW PLAYER'S ITEMS
        for (int i = 0;i<3 ;i++){
            slotX = slotStartX;
            slotY = switch (i) {
                case 0 -> slotStartY1;
                case 1 -> slotStartY2;
                case 2 -> slotStartY3;
                default -> slotY;
            };
            for (int j = 0; j<gp.player.inventory[i].size();j++){
                if (j >5){slotY +=slotOffset;}
                g2.drawImage(gp.player.inventory[i].get(j).image,slotX,slotY,null);
                slotX += slotOffset;


            }


        }


        // Cursor
        int cursorX = (slotStartX + (slotOffset)* slotCol) - gp.tileSize/16;
        int cursorY;

        int cursorWidth = (9*gp.tileSize)/8;
        int cursorHeight = (9*gp.tileSize)/8;
        if (slotRow == 0){
            cursorY = (slotStartY1 ) - gp.tileSize/16;

        }else if (slotRow == 1){
            cursorY = (slotStartY2 ) - gp.tileSize/16;
        }else if (slotRow == 2){
            cursorY = (slotStartY2 + slotOffset) - gp.tileSize/16;
        }
        else{
            cursorY = (slotStartY3 ) -  gp.tileSize/16;
        }
        // DRAW CURSOR
        g2.setColor(myOrange);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight,5,5);



    }

    public void drawPauseScreen(){
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth/2,gp.screenHeight/2),
                2*gp.screenWidth,
                new float[]{0.0f,1.0f},
                new Color[]{new Color(0,0,0,240),new Color(0,0,0,100)});
        g2.setPaint(vignette);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setFont(trunic);
        g2.setColor(Color.white);
        String texte ="Nap time";
        int x = getXforCenteredText(texte);
        int y = gp.screenHeight/5;
        g2.drawString(texte,x,y);

        g2.setFont(lilliput_20);
        FontMetrics metrics = g2.getFontMetrics();

        int buttonWidth = menuSelection.getWidth();
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (pauseCommand.length * buttonHeight) / 2;

        for (int i = 0; i < pauseCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight /57.6f)));

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
            int textY = buttonY + (int)(gp.screenHeight /15.15f);
            g2.drawString(pauseCommand[i], textX, textY);
        }

    }

    public void drawTitleScreen(){


        int titleX = gp.screenWidth/12;
        int titleY = gp.screenHeight/2 -  whiteTitle.getHeight()/2;
//        g2.setColor(new Color(155,240,253));
//        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
        g2.drawImage(whiteTitle,titleX,titleY,null);
        int x = gp.screenWidth * 3/4;
        g2.setColor(Color.white);
        if (commandNumber == 0){
            g2.setColor(myOrange);
        }
        g2.drawString("New Game",getXForCenteredTextAroundX("New Game",x),titleY);
        g2.setColor(Color.white);
        if (commandNumber == 1){
            g2.setColor(myOrange);
        }
        g2.drawString("Load Game",getXForCenteredTextAroundX("Load Game",x),titleY+gp.screenHeight /5.76f);
        g2.setColor(Color.white);
        if (commandNumber == 2){
            g2.setColor(myOrange);
        }
        g2.drawString("Options",getXForCenteredTextAroundX("Options",x),titleY+ gp.screenHeight /2.8f);

    }

    public void drawMessageScreen(){

        //WINDOW
        int x = gp.tileSize*2;
        int y= gp.tileSize/2;
        int width = gp.screenWidth-(gp.tileSize*4);
        int height= gp.tileSize*5;
        drawSubWindow(x,y,width,height,messageWindow);
        g2.setFont(trunic);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, gp.tileSize/2f));
        x = getXForCenteredTextAroundX(currentDialogue,(2*x+width)/2);
        y = (2*y+height)/2;
        g2.drawString(currentDialogue,x,y);
    }

    public void drawItemScreen(){
        //  ITEM FRAME
        int x = (11*gp.tileSize)/2;
        int y= gp.tileSize;
        g2.setColor(Color.black);
        g2.fillOval(x,y,gp.tileSize*5,gp.tileSize*5);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(x,y,gp.tileSize*5,gp.tileSize*5);

        // DRAW THE ITEM
        if (item.upAnimator != null){
            item.upAnimator.draw(g2,x+gp.tileSize,y+gp.tileSize,gp.tileSize * 3,gp.tileSize * 3);
        }else{
            g2.drawImage(item.image,x+gp.tileSize,y+gp.tileSize,gp.tileSize * 3,gp.tileSize * 3,null);
        }


        //DRAW TEXT MESSAGE
        g2.setFont(trunic);
        int textX = getXforCenteredText(item.name);
        int textY = 7 * gp.tileSize;
        drawSubWindow(x-gp.tileSize,textY-(3*gp.tileSize)/2,7*gp.tileSize,2*gp.tileSize,messageWindow);
        g2.setColor(Color.white);
        g2.drawString(item.name,textX,textY);

    }

    public void drawItemScreen2(){
        if (item == null) return; // Sécurité

        // --- 1. CALCUL DU CERCLE ---
        // Dimensions finales (cibles)
        int finalSize = gp.tileSize * 5;
        int targetX = (11 * gp.tileSize) / 2;
        int targetY = gp.tileSize;

        // Calcul du centre du cercle (point pivot pour l'agrandissement)
        int centerX = targetX + finalSize / 2;
        int centerY = targetY + finalSize / 2;

        // Dimensions actuelles basées sur l'animation
        int currentSize = (int) (finalSize * itemScale);

        // On recalcule X et Y pour que ça reste centré
        int currentX = centerX - currentSize / 2;
        int currentY = centerY - currentSize / 2;

        // Dessin du fond noir et du contour blanc
        g2.setColor(Color.black);
        g2.fillOval(currentX, currentY, currentSize, currentSize);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(currentX, currentY, currentSize, currentSize);

        // --- 2. DESSIN DE L'ITEM ---
        // On veut que l'item grandisse aussi
        int itemFinalSize = gp.tileSize * 3;
        int itemCurrentSize = (int) (itemFinalSize * itemScale);

        // Centre de l'image de l'item (légèrement décalé car l'item est dans le cercle)
        int itemCenterX = targetX + gp.tileSize + itemFinalSize / 2;
        int itemCenterY = targetY + gp.tileSize + itemFinalSize / 2;

        int itemDrawX = itemCenterX - itemCurrentSize / 2;
        int itemDrawY = itemCenterY - itemCurrentSize / 2;

        if (item.upAnimator != null){
            item.upAnimator.draw(g2, itemDrawX, itemDrawY, itemCurrentSize, itemCurrentSize);
        } else {
            g2.drawImage(item.image, itemDrawX, itemDrawY, itemCurrentSize, itemCurrentSize, null);
        }

        // --- 3. DESSIN DU MESSAGE (TEXTE) ---
        // On affiche le texte seulement si l'animation a commencé à apparaître un peu
        if (itemScale > 0.1f) {
            g2.setFont(trunic);

            // On peut aussi animer la taille de la fenêtre de texte si tu veux
            int textWindowWidth = 7 * gp.tileSize;
            int textWindowHeight = 2 * gp.tileSize;

            // Animation simple : on applique aussi l'échelle à la fenêtre de texte
            int currentTextW = (int)(textWindowWidth * itemScale);
            int currentTextH = (int)(textWindowHeight * itemScale);

            // Position du texte
            int textY = 7 * gp.tileSize;
            int textWindowCenterX = (targetX - gp.tileSize) + (textWindowWidth / 2);
            int textWindowCenterY = (textY - (3 * gp.tileSize) / 2) + (textWindowHeight / 2);

            int winX = textWindowCenterX - currentTextW / 2;
            int winY = textWindowCenterY - currentTextH / 2;

            drawSubWindow(winX, winY, currentTextW, currentTextH, messageWindow);

            // Pour le texte lui-même, changer la taille de la police en temps réel est coûteux.
            // On l'affiche uniquement quand l'animation est presque finie pour éviter les glitchs visuels
            if(itemScale > 0.8f){
                g2.setColor(Color.white);
                int textX = getXforCenteredText(item.name);
                g2.drawString(item.name, textX, textY);
            }
        }
    }

    public void drawOptionScreen(){
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth/2,gp.screenHeight/2),
                2*gp.screenWidth,
                new float[]{0.0f,1.0f},
                new Color[]{new Color(0,0,0,240),new Color(0,0,0,100)});
        g2.setPaint(vignette);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setColor(Color.white);
        drawSubWindow(0,0,gp.screenWidth,gp.screenHeight,optionWindow);
        g2.setFont(lilliput_40);
        int x =getXforCenteredText("Option");
        int y = 2*gp.tileSize;
        g2.drawString("Option",x,y);

        g2.setFont(lilliput_20);
        FontMetrics metrics = g2.getFontMetrics();

        int buttonWidth = menuSelection.getWidth();
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (optionCommand.length * buttonHeight) / 2;

        for (int i = 0; i < optionCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight /57.6f)));

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
            int textY = buttonY + (int)(gp.screenHeight /15.15f);
            g2.drawString(optionCommand[i], textX, textY);
        }




    }

    public void drawAudioScreen(){
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth/2,gp.screenHeight/2),
                2*gp.screenWidth,
                new float[]{0.0f,1.0f},
                new Color[]{new Color(0,0,0,240),new Color(0,0,0,100)});
        g2.setPaint(vignette);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setColor(Color.white);
        drawSubWindow(0,0,gp.screenWidth,gp.screenHeight,optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Audio");
        int y = 2*gp.tileSize;
        g2.drawString("Audio",x,y);

        g2.setFont(lilliput_20);
        int buttonWidth =(int)(1.3f*menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (audioCommand.length * buttonHeight) / 2;

        for (int i = 0; i < audioCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight /57.6f)));

            if (i == commandNumberAudio) {
                if (i == 2 && menuSelectionOrange != null){
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }else if ( menuSelectionOrange2 != null) {
                    g2.drawImage(menuSelectionOrange2, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);


            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth ) / 8;
            int textY = buttonY + (int)(gp.screenHeight /15.15f);
            g2.drawString(audioCommand[i], textX, textY);
            if (i ==0){
                g2.setColor(myOrange);
                textX += 7*gp.tileSize + gp.tileSize/2;
                g2.drawString(Math.round(gp.music.currentVolume * 100)+"%", textX, textY);
            }
            if (i == 1){
                g2.setColor(myOrange);
                textX += 7*gp.tileSize + gp.tileSize/2;
                g2.drawString(Math.round(gp.soundEffects.currentVolume * 100)+"%", textX, textY);

            }
        }
    }

    public void drawGraphicsScreen(){
        RadialGradientPaint vignette = new RadialGradientPaint(
                new Point(gp.screenWidth/2,gp.screenHeight/2),
                2*gp.screenWidth,
                new float[]{0.0f,1.0f},
                new Color[]{new Color(0,0,0,240),new Color(0,0,0,100)});
        g2.setPaint(vignette);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setColor(Color.white);
        drawSubWindow(0,0,gp.screenWidth,gp.screenHeight,optionWindow);
        g2.setFont(lilliput_40);
        int x = getXforCenteredText("Graphics");
        int y = 2*gp.tileSize;
        g2.drawString("Graphics",x,y);

        g2.setFont(lilliput_15);
        int buttonWidth =(int)(1.3f*menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (graphicCommand.length * buttonHeight) / 2;

        for (int i = 0; i < graphicCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + (int)(gp.screenHeight /57.6f)));

            if (i == commandNumberGraphic) {
                if (i == 2 && menuSelectionOrange != null){
                    g2.drawImage(menuSelectionOrange, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }else if ( menuSelectionOrange2 != null) {
                    g2.drawImage(menuSelectionOrange2, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.WHITE);


            } else {
                if (menuSelection != null) {
                    g2.drawImage(menuSelection, buttonX, buttonY, buttonWidth, buttonHeight, null);
                }
                g2.setColor(Color.LIGHT_GRAY);
            }

            int textX = buttonX + (buttonWidth ) / 8;
            int textY = buttonY + (int)(gp.screenHeight /15.15f);
            g2.drawString(graphicCommand[i], textX, textY);

            if (i ==0){
                g2.setColor(myOrange);
                textX += 5*gp.tileSize + gp.tileSize/2;
                g2.drawString(gp.displayMode, textX, textY);
            }
            if (i ==1){
                g2.setColor(myOrange);
                textX += 5*gp.tileSize + gp.tileSize/2;
                g2.drawString(gp.screenWidth+"x"+gp.screenHeight, textX, textY);
            }
        }


    }

    public void drawSubWindow(int x,int y,int width,int height,BufferedImage image){
       g2.drawImage(image,x,y,width,height,null);
    }

    public void drawPlayerHealth(){

        int x = gp.screenWidth/10;
        int topY = gp.screenHeight*2/3;
        int healthBarHeight = (int)(gp.screenHeight/3.5f);
        g2.drawImage(healthOverlay,x,topY,healthOverlay.getWidth(),healthBarHeight,null);

        int midHeight = healthMiddle.getHeight();
        float hp = player.displayedHealth/player.maxHealth;

        float stopY = topY +((1-hp) * (healthBarHeight-healthTop.getHeight()));
        int botY = topY + healthBarHeight;
        if (stopY >= botY-healthTop.getHeight()){
            g2.drawImage(healthTop,x,(botY-healthTop.getHeight()),null);
        }
        else{
            g2.drawImage(healthTop,x,(int)(stopY),null);


        }
        int y = botY - midHeight;
        while( y >= (int)stopY){
            g2.drawImage(healthMiddle,x,y,null);
            y--;
       }



    }

    public void drawPlayerEndurance(){
        int x = gp.screenWidth/10 +(int)(gp.screenHeight /11.52f);
        int topY = gp.screenHeight*2/3;
        int healthBarHeight = (int)(gp.screenHeight/3.5f);
        g2.drawImage(enduranceOverlay,x,topY,enduranceOverlay.getWidth(),healthBarHeight,null);

        int midHeight = enduranceMiddle.getHeight();
        float ed = player.displayedEndurance/player.maxEndurance;

        float stopY = topY +((1-ed) * (healthBarHeight-enduranceTop.getHeight()));
        int botY = topY + healthBarHeight;
        if (stopY >= botY-enduranceTop.getHeight()){
            g2.drawImage(enduranceTop,x,(botY-enduranceTop.getHeight()),null);
        }
        else{
            g2.drawImage(enduranceTop,x,(int)(stopY),null);


        }
        int y = botY - midHeight;
        while( y >= (int)stopY){
            g2.drawImage(enduranceMiddle,x,y,null);
            y--;
        }
    }

    public void drawPlayerMana(){
        int x = gp.screenWidth/10 +(int)(gp.screenHeight /5.76f);
        int topY = gp.screenHeight*2/3;
        int healthBarHeight = (int)(gp.screenHeight/3.5f);
        g2.drawImage(manaOverlay,x,topY,manaOverlay.getWidth(),healthBarHeight,null);

        int midHeight = manaMiddle.getHeight();
        float ed = player.displayedMana/player.maxMana;

        float stopY = topY +((1-ed) * (healthBarHeight-manaTop.getHeight()));
        int botY = topY + healthBarHeight;
        if (stopY >= botY-manaTop.getHeight()){
            g2.drawImage(manaTop,x,(botY-manaTop.getHeight()),null);
        }
        else{
            g2.drawImage(manaTop,x,(int)(stopY),null);


        }
        int y = botY - midHeight;
        while( y >= (int)stopY){
            g2.drawImage(manaMiddle,x,y,null);
            y--;
        }
    }

    public void drawPlayerPotion(){

        int tempX,tempY,tempSize;
        if (gp.gameState == gp.inInventory){
            tempX = (gp.maxScreenCol -2)*gp.tileSize - (gp.tileSize/3);
            tempY = 2*gp.tileSize  + (gp.tileSize/3);
            tempSize = gp.tileSize;

                if(tempX > potionXPos){
                    potionXPos += 1;
                }
                else{potionXPos = tempX;}
                if(tempY > potionYPos){
                    potionYPos += 1;
                }else{potionYPos = tempY;}
                if(tempSize > potionSize){
                    potionSize += 1;
                }else{potionSize = tempSize;}

        }
        else{
            tempX = (gp.maxScreenCol -2)*gp.tileSize;
            tempY = 2*gp.tileSize;
            tempSize = (2*gp.tileSize/3);
            potionXPos = tempX;
            potionYPos = tempY;
            potionSize = tempSize;
        }
        for (int i =0; i< gp.player.potionNotUsed ;i++){
            g2.drawImage(potionFull,potionXPos,potionYPos,potionSize,potionSize,null);
            potionXPos -= potionSize;
        }
        for (int i =0; i< gp.player.maxPotion - gp.player.potionNotUsed ;i++){
            g2.drawImage(potionEmpty,potionXPos,potionYPos,potionSize,potionSize,null);
            potionXPos -= potionSize;
        }
        potionXPos = tempX;
        potionYPos = tempY;
        potionSize = tempSize;
    }

    public void drawPlayerEquipment(){
        int tempX,tempY,tempCirSize;
        if (gp.gameState == gp.inInventory){
            tempX = ((gp.maxScreenCol -2)*gp.tileSize);
            tempY = (2*gp.tileSize/3) ;
            tempCirSize = (3*gp.tileSize/2);

            if(tempX > equipmentXPos){
                equipmentXPos += 1;
            }
            else{equipmentXPos = tempX;}
            if(tempY > equipmentYPos){
                equipmentYPos += 1;
            }else{equipmentYPos = tempY;}
            if(tempCirSize > equipmentSize){
                equipmentSize += 1;
            }else{equipmentSize = tempCirSize;}
        }
        else{

            tempX = ((gp.maxScreenCol -2)*gp.tileSize);
            tempY = (2*gp.tileSize/3) ;
            tempCirSize = gp.tileSize;
            equipmentXPos = tempX;
            equipmentYPos = tempY;
            equipmentSize = tempCirSize;
        }
        int jX = equipmentXPos - 2*equipmentSize;
        int kX = equipmentXPos - equipmentSize;
        int lX = equipmentXPos;
        int y = equipmentYPos;
        SuperObject jEquip = gp.player.jEquip;
        SuperObject kEquip = gp.player.kEquip;
        SuperObject lEquip = gp.player.lEquip;
        g2.setColor(new Color(47,47,47,200));
        g2.fillOval(jX,y,equipmentSize,equipmentSize);
        g2.fillOval(kX,y,equipmentSize,equipmentSize);
        g2.fillOval(lX,y,equipmentSize,equipmentSize);

        if (jEquip != null){
            g2.drawImage(jEquip.image,jX + equipmentSize/6,y + equipmentSize/6,(2*equipmentSize/3),(2*equipmentSize/3),null);
        }if (kEquip != null){
            g2.drawImage(kEquip.image,kX + equipmentSize/6,y + equipmentSize/6,(2*equipmentSize/3),(2*equipmentSize/3),null);
        }if (lEquip != null){
            g2.drawImage(lEquip.image,lX + equipmentSize/6,y + equipmentSize/6,(2*equipmentSize/3),(2*equipmentSize/3),null);
        }

        equipmentXPos = tempX;
        equipmentYPos = tempY;
        equipmentSize = tempCirSize;

    }

    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;

    }
    public int getXForCenteredTextAroundX(String text,int x){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return x - length/2;
    }

    public void drawIrisTransition() {

        // --- DESSIN DU MASQUE ---
        // 1. Créer une "Area" qui couvre tout l'écran (le noir)
        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

        // 2. Créer le cercle au centre (le trou)

        double centerX = player.screenX + gp.tileSize/2.0;
        double centerY = player.screenY + gp.tileSize/2.0;

        double x = centerX - (transitionSize / 2.0);
        double y = centerY - (transitionSize / 2.0);

        // Shape du cercle
        Area circleArea = new Area(new Ellipse2D.Double(x, y, transitionSize, transitionSize));

        // 3. Soustraire le cercle à l'écran ( Noir - Cercle = Masque à trou)
        screenArea.subtract(circleArea);

        // 4. Dessiner
        g2.setColor(java.awt.Color.BLACK);
        g2.fill(screenArea);
    }
    public void startTransition(Runnable action) {
        onTransitionComplete = action;
        transitionOn = true;
        transitionState = 1; // Commence par fermer
        transitionSize = maxTransitionSize; // Commence grand ouvert
    }
}

