package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;
    boolean debugKeyPressed = false;
    public boolean healPressed,attackPressed,parryPressed,interactionPressed;
    GamePanel gp;
    Random random = new Random();

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {



    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if(gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumber == 0) {
                    gp.ui.commandNumber = 2;
                } else {
                    gp.ui.commandNumber--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumber == 2) {
                    gp.ui.commandNumber = 0;
                } else {
                    gp.ui.commandNumber++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNumber == 0) {
                    gp.stopMusic();
                    gp.playSoundEffect(4);
                    gp.playMusic(random.nextInt(2)+19);
                    gp.gameState = gp.playState;

                }
            }
            if (code == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
        else if(gp.gameState == gp.playState){
            if(code == KeyEvent.VK_Z){
                upPressed = true;
            }
            if(code == KeyEvent.VK_Q){
                leftPressed = true;
            }
            if(code == KeyEvent.VK_S){
                downPressed = true;
            }
            if(code == KeyEvent.VK_D){
                rightPressed = true;
            }
            if(code == KeyEvent.VK_P){
                gp.debugMode = !gp.debugMode;
            }
            if(code == KeyEvent.VK_ESCAPE){
                gp.updateMusicVolume(gp.music.currentVolume/2);
                gp.playSoundEffect(0);
                gp.gameState = gp.pauseState;

            }

            if (code == KeyEvent.VK_U){
                healPressed = true;
            }
            if (code == KeyEvent.VK_J){
                attackPressed = true;
            }
            if (code == KeyEvent.VK_K){
                parryPressed = true;

            }
            if(code == KeyEvent.VK_F){
                interactionPressed = true;

            }
            if(code == KeyEvent.VK_SPACE){
                spacePressed = true;
            }


        }
        else if(gp.gameState == gp.pauseState){
            if(code == KeyEvent.VK_ESCAPE){
                gp.playSoundEffect(7);
                gp.updateMusicVolume(gp.music.originalVolume);

                gp.gameState = gp.playState;
            }
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberPause == 0) {
                    gp.ui.commandNumberPause = 2;
                } else {
                    gp.ui.commandNumberPause--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberPause == 2) {
                    gp.ui.commandNumberPause = 0;
                } else {
                    gp.ui.commandNumberPause++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNumberPause == 0) {
                    gp.playSoundEffect(7);
                    gp.updateMusicVolume(gp.music.originalVolume);
                    gp.gameState = gp.playState;

                }
                if (gp.ui.commandNumberPause == 2) {
                    gp.stopMusic();
                    gp.playSoundEffect(1);
                    gp.updateMusicVolume(gp.music.originalVolume);
                    gp.gameState = gp.titleState;
                    gp.playMusic(18);
                }
            }
        }
        else if(gp.gameState== gp.dialogueState){
            if(code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
            }
        }




        }


    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_Z){
            upPressed = false;

        }
        if(code == KeyEvent.VK_Q){
            leftPressed = false;

        }
        if(code == KeyEvent.VK_S){
            downPressed = false;

        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;

        }
        if (code == KeyEvent.VK_U){
            healPressed = false;
        }
        if (code == KeyEvent.VK_J){
            attackPressed = false;
        }
        if (code == KeyEvent.VK_K){
            parryPressed = false;

        }
        if(code == KeyEvent.VK_F){
            interactionPressed = false;

        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }


    }
}
