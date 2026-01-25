package main;

import entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font lilliput_40,trunic,lilliput_20;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public String currentDialogue = "";
    public boolean gameFinished = false;
    Player player;
    BufferedImage healthTop,healthOverlay, healthMiddle;
    BufferedImage enduranceTop,enduranceOverlay,enduranceMiddle;
    BufferedImage manaTop,manaOverlay,manaMiddle;
    BufferedImage menuSelection ,menuSelectionOrange,menuSelectionOrange2;
    BufferedImage messageWindow;
    BufferedImage optionWindow;
    int commandNumber = 0;

     public String[] pauseCommand = {"Return to Game","Options","Quit"};
     public int commandNumberPause = 0;
     public String[] optionCommand = {"Audio","Graphics","Return"};
     public int commandNumberOption = 0;

     public String[] audioCommand = {"Musics","Sounds Effects","Return"};
     public int commandNumberAudio = 0;

     public String[] graphicCommand = {"Display Mode","Resolution","Return"};
     public int commandNumberGraphic = 0;




    BufferedImage whiteTitle,blackTitle;
    public UI(GamePanel gp,Player player) {
        this.gp = gp;
        this.player = player;

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

        messageWindow = setup("/menu/window1",gp.scale);

        optionWindow = setup("/menu/optionOverlay",gp.scale);


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

        }
        if (gp.gameState == gp.pauseState){
            drawPauseScreen();

        }
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
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
            int buttonY = startY + (i * (buttonHeight + 10));

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
            int textY = buttonY + 38;
            g2.drawString(pauseCommand[i], textX, textY);
        }

    }

    public void drawTitleScreen(){
        Color myOrange = new Color(206, 157, 58);
        int titleX = gp.screenWidth/12;
        int titleY = gp.screenHeight/2 -  whiteTitle.getHeight()/2;
//        g2.setColor(new Color(155,240,253));
//        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
        g2.drawImage(whiteTitle,titleX,titleY,null);
        int x = gp.screenWidth * 3/4;
        if (commandNumber == 0){
            g2.setColor(myOrange);
        }
        g2.drawString("New Game",getXForCenteredTextAroundX("New Game",x),titleY);
        g2.setColor(Color.white);
        if (commandNumber == 1){
            g2.setColor(myOrange);
        }
        g2.drawString("Load Game",getXForCenteredTextAroundX("Load Game",x),titleY+100);
        g2.setColor(Color.white);
        if (commandNumber == 2){
            g2.setColor(myOrange);
        }
        g2.drawString("Options",getXForCenteredTextAroundX("Options",x),titleY+200);

    }

    public void drawDialogueScreen(){

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
            int buttonY = startY + (i * (buttonHeight + 10));

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
            int textY = buttonY + 38;
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
        FontMetrics metrics = g2.getFontMetrics();
        int buttonWidth =(int)(1.3f*menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (audioCommand.length * buttonHeight) / 2;

        for (int i = 0; i < audioCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + 10));

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
            int textY = buttonY + 38;
            g2.drawString(audioCommand[i], textX, textY);
            if (i ==0){
                g2.setColor(new Color(196, 148, 62));
                textX += 7*gp.tileSize;
                g2.drawString(Math.round(gp.music.currentVolume * 100)+"%", textX, textY);
            }
            if (i == 1){
                g2.setColor(new Color(196, 148, 62));
                textX += 7*gp.tileSize;
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

        g2.setFont(lilliput_20);
        int buttonWidth =(int)(1.3f*menuSelection.getWidth());
        int buttonHeight = menuSelection.getHeight();
        int startY = gp.screenHeight / 2 - (graphicCommand.length * buttonHeight) / 2;

        for (int i = 0; i < graphicCommand.length; i++) {
            int buttonX = (gp.screenWidth - buttonWidth) / 2;
            int buttonY = startY + (i * (buttonHeight + 10));

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
            int textY = buttonY + 38;
            g2.drawString(graphicCommand[i], textX, textY);
        }

    }

    public void drawSubWindow(int x,int y,int width,int height,BufferedImage image){
       g2.drawImage(image,x,y,width,height,null);
    }

    public void drawPlayerHealth(){
        int x = gp.screenWidth/10;
        int topY = gp.screenHeight*2/3;
        g2.drawImage(healthOverlay,x,topY,null);

        int healthBarHeight = healthOverlay.getHeight();
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
        int x = gp.screenWidth/10 +50;
        int topY = gp.screenHeight*2/3;
        g2.drawImage(enduranceOverlay,x,topY,null);

        int healthBarHeight = enduranceOverlay.getHeight();
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

    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;

    }
    public int getXForCenteredTextAroundX(String text,int x){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return x - length/2;
    }
}

