package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    boolean debugKeyPressed = false;
    GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if(gp.gameState == gp.titleState){
            if (code == KeyEvent.VK_ENTER){
                gp.gameState = gp.playState;
            }
        }
        else{
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
                debugKeyPressed = !debugKeyPressed;
            }
            if(code == KeyEvent.VK_ESCAPE){
                if(gp.gameState == gp.playState){
                    gp.gameState = gp.pauseState;
                }else if(gp.gameState == gp.pauseState){
                    gp.gameState = gp.playState;
                }
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

    }
}
