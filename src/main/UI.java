package main;

import java.awt.*;

public class UI {
    GamePanel gp;
    Graphics g2;
    Font arial_40,arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }
    public void showMessage(String message) {
        this.message = message;
        this.messageCounter++;
        messageOn = true;
    }
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(arial_80B);
        g2.setColor(Color.white);
        if(gp.gameState == gp.titleState){
            drawTitleScreen();

        }

        if (gp.gameState == gp.playState){

        }
        if (gp.gameState == gp.pauseState){
            drawPauseScreen();

        }
    }
    public void drawPauseScreen(){
        String texte ="PAUSE";
        int x = getXforCenteredText(texte);


        int y = gp.screenHeight/2;
        g2.drawString(texte,x,y);
    }

    public void drawTitleScreen(){
    }

    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;

    }
}
