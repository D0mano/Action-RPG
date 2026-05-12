package main;

import object.SuperObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;
    boolean debugKeyPressed = false;
    public boolean healPressed, jEquipPressed,parryPressed,interactionPressed, lEquipPressed, kEquipPressed;
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
                    gp.playSoundEffect(23);
                    gp.previousState = gp.gameState;
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.newGameSlotState;});

                }
                if (gp.ui.commandNumber == 1) {
                    gp.playSoundEffect(49);
                    gp.previousState = gp.gameState;
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                        gp.gameState = gp.loadSaveState;
                    });

                }
                if (gp.ui.commandNumber == 2) {
                    gp.playSoundEffect(23);
                    gp.previousState = gp.gameState;
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                        gp.gameState = gp.optionState;
                    });
                }
            }
            if (code == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
        else if (gp.gameState == gp.newGameSlotState) {


            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberNewSlot == 0) {
                    gp.ui.commandNumberNewSlot = 3;
                } else {
                    gp.ui.commandNumberNewSlot--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberNewSlot == 3) {
                    gp.ui.commandNumberNewSlot = 0;
                } else {
                    gp.ui.commandNumberNewSlot++;
                }
            }

            if (code == KeyEvent.VK_ENTER) {

                if (gp.ui.commandNumberNewSlot == 3) {
                    gp.playSoundEffect(24);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.titleState;
                    gp.ui.commandNumberNewSlot = 0;});
                    return;
                }

                int chosenSlot = gp.ui.commandNumberNewSlot; // 0, 1 ou 2


                gp.currentSaveIndex = chosenSlot;

                gp.setupGame();
                gp.stopMusic();
                gp.playSoundEffect(4);
                gp.playMusic(random.nextInt(3) + 19);
                gp.previousState = gp.gameState;
                gp.gameState = gp.playState;
                gp.ui.commandNumberNewSlot = 0;
            }

            if (code == KeyEvent.VK_ESCAPE) {
                gp.playSoundEffect(24);
                gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                gp.gameState = gp.titleState;
                gp.ui.commandNumberNewSlot = 0;});
            }
        }
        else if(gp.gameState == gp.optionState ) {
            if(code == KeyEvent.VK_ESCAPE){
                if (gp.previousState == gp.titleState) {
                    gp.playSoundEffect(24);
                    gp.previousState = gp.gameState;
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.titleState;});

                }else if (gp.previousState == gp.pauseState) {
                    gp.playSoundEffect(25);
                    gp.previousState = gp.gameState;
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.pauseState;});
                }
            }

            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberOption == 0) {
                    gp.ui.commandNumberOption = 3;
                } else {
                    gp.ui.commandNumberOption--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberOption == 3) {
                    gp.ui.commandNumberOption = 0;
                } else {
                    gp.ui.commandNumberOption++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {

                if (gp.ui.commandNumberOption == 0) {
                    gp.playSoundEffect(25);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.audioSettingstate;});

                }
                if (gp.ui.commandNumberOption == 1) {
                    gp.playSoundEffect(25);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.graphicsSettingstate;});

                }
                if (gp.ui.commandNumberOption == 2) {
                    gp.playSoundEffect(25);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.controlSettingState;});
                }
                if (gp.ui.commandNumberOption == 3) {
                    if (gp.previousState == gp.titleState){
                        gp.playSoundEffect(24);
                        gp.previousState = gp.gameState;
                        gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                        gp.gameState = gp.titleState;});

                    }else if (gp.previousState ==gp.pauseState){
                        gp.playSoundEffect(25);
                        gp.previousState = gp.gameState;
                        gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                        gp.gameState = gp.pauseState;});
                    }


                }
            }
        }
        else if(gp.gameState == gp.controlSettingState ) {
            if(code == KeyEvent.VK_ESCAPE){
                gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                gp.gameState = gp.optionState;});
            }
            // Navigation gauche/droite entre catégories
            if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_Q) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberControle == 0) {
                    gp.ui.commandNumberControle = gp.ui.totalCategories - 1;
                } else {
                    gp.ui.commandNumberControle--;
                }
            }

            if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberControle >= gp.ui.totalCategories - 1) {
                    gp.ui.commandNumberControle = 0;
                } else {
                    gp.ui.commandNumberControle++;
                }
            }
        }
        else if (gp.gameState == gp.loadSaveState ) {
            if(code == KeyEvent.VK_ESCAPE){
                gp.previousState = gp.loadSaveState;
                gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                gp.gameState = gp.titleState;});

            }

            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(45);
                if (gp.ui.commandNumberLoad == 0) {

                    gp.ui.commandNumberLoad = gp.ui.loadCommand.size()-1;
                } else {
                    gp.ui.commandNumberLoad--;
                }

            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(45);
                if (gp.ui.commandNumberLoad == gp.ui.loadCommand.size()-1) {
                    gp.ui.commandNumberLoad = 0;
                } else {
                    gp.ui.commandNumberLoad++;
                }

            }
            if (code == KeyEvent.VK_ENTER) {
                gp.playSoundEffect(48);
                gp.playSoundEffect(44);


                if (gp.ui.loadCommand.get(gp.ui.commandNumberLoad).equals("Cancel")){
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.titleState;});
                    gp.previousState = gp.loadSaveState;
                    gp.playSoundEffect(24);

                }else{
                    gp.currentSaveIndex = gp.ui.commandNumberLoad;
                    gp.gameState = gp.loadSaveSelectionState;
                }
            }

        }
        else if (gp.gameState == gp.loadSaveSelectionState ) {
            if(code == KeyEvent.VK_ESCAPE){
                gp.previousState = gp.gameState;
                gp.gameState = gp.loadSaveState;
            }
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberLoadSelection == 0) {
                    gp.ui.commandNumberLoadSelection = 2;
                } else {
                    gp.ui.commandNumberLoadSelection--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberLoadSelection == 2) {
                    gp.ui.commandNumberLoadSelection = 0;
                } else {
                    gp.ui.commandNumberLoadSelection++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNumberLoadSelection == 0) {
                    gp.stopMusic();
                    gp.loadGame();
                    gp.playSoundEffect(46);
                    gp.playMusic(random.nextInt(3)+19);
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.playState;


                }
                if (gp.ui.commandNumberLoadSelection == 1) {
                    gp.removeSave();
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.loadSaveState;
                    ;

                }
                if (gp.ui.commandNumberLoadSelection == 2) {
                    gp.playSoundEffect(47);
                    gp.gameState = gp.loadSaveState;

                }
            }
        }
        else if(gp.gameState == gp.audioSettingstate) {
            if(code == KeyEvent.VK_ESCAPE){
                gp.playSoundEffect(25);
                gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                gp.gameState = gp.optionState;});
            }

            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberAudio == 0) {
                    gp.ui.commandNumberAudio = 2;
                } else {
                    gp.ui.commandNumberAudio--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberAudio == 2) {
                    gp.ui.commandNumberAudio = 0;
                } else {
                    gp.ui.commandNumberAudio++;
                }
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
                if (gp.ui.commandNumberAudio == 0) {
                    if(gp.music.currentVolume < 1){
                        gp.music.currentVolume += 0.1f;
                        gp.music.currentVolume = Math.max(0f, gp.music.currentVolume);
                        gp.updateMusicVolume(gp.music.currentVolume);
                    }
                }
                if (gp.ui.commandNumberAudio == 1) {
                    if(gp.soundEffects.currentVolume < 1){
                        gp.soundEffects.currentVolume += 0.1f;
                        gp.soundEffects.currentVolume = Math.max(0f, gp.soundEffects.currentVolume);
                        gp.updateSoundVolume(gp.soundEffects.currentVolume);
                    }
                }
            }
            if (code == KeyEvent.VK_Q || code == KeyEvent.VK_LEFT){
                if (gp.ui.commandNumberAudio == 0) {
                    if(gp.music.currentVolume > 0){
                        gp.music.currentVolume -= 0.1F;
                        gp.music.currentVolume = Math.max(0f, gp.music.currentVolume);
                        gp.updateMusicVolume(gp.music.currentVolume);
                    }
                }
                if (gp.ui.commandNumberAudio == 1) {
                    if(gp.soundEffects.currentVolume > 0){
                        gp.soundEffects.currentVolume -= 0.1f;
                        gp.soundEffects.currentVolume = Math.max(0f, gp.soundEffects.currentVolume);
                        gp.updateSoundVolume(gp.soundEffects.currentVolume);
                    }
                }
            }
            if (code == KeyEvent.VK_ENTER) {

                if (gp.ui.commandNumberAudio == 2) {
                    gp.playSoundEffect(25);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.optionState;});
                }
            }

        }
        else if(gp.gameState == gp.graphicsSettingstate) {
            if(code == KeyEvent.VK_ESCAPE){
                gp.playSoundEffect(25);
                gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                gp.gameState = gp.optionState;});
            }

            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberGraphic == 0) {
                    gp.ui.commandNumberGraphic = 2;
                } else {
                    gp.ui.commandNumberGraphic--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberGraphic == 2) {
                    gp.ui.commandNumberGraphic = 0;
                } else {
                    gp.ui.commandNumberGraphic++;
                }
            }
            if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
                if(gp.ui.commandNumberGraphic == 0){
                    if(gp.displayMode.equals(gp.windowMode)  ){
                        gp.displayMode = gp.fullScreenMode;

                    }
                    else{gp.displayMode = gp.windowMode;}
                }
                if(gp.ui.commandNumberGraphic == 1){
                    if(gp.scale == 5){
                        gp.scale = 2;
                    }else{
                        gp.scale ++;
                    }
                    gp.updateSetting();
                }
            }
            if(code == KeyEvent.VK_Q || code == KeyEvent.VK_LEFT){
                if(gp.ui.commandNumberGraphic == 0){
                    if(gp.displayMode.equals(gp.windowMode)  ){
                        gp.displayMode = gp.fullScreenMode;

                    }
                    else{gp.displayMode = gp.windowMode;}
                }
                if(gp.ui.commandNumberGraphic == 1){
                    if(gp.scale == 2){
                        gp.scale = 5;
                    }else{
                        gp.scale --;
                    }
                    gp.updateSetting();
                }
            }
            if (code == KeyEvent.VK_ENTER) {

                if (gp.ui.commandNumberGraphic == 0) {
                    gp.playSoundEffect(25);

                }
                if (gp.ui.commandNumberGraphic == 1) {
                    gp.playSoundEffect(25);

                }
                if (gp.ui.commandNumberGraphic == 2) {
                    gp.playSoundEffect(25);
                    gp.updateSetting();
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.gameState = gp.optionState;});





                }
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
                gp.music.setVolume(gp.music.currentVolume/2);
                gp.playSoundEffect(0);
                gp.previousState = gp.gameState;
                gp.gameState = gp.pauseState;

            }
            if (code == KeyEvent.VK_TAB){
                gp.playSoundEffect(34);
                gp.previousState = gp.gameState;
                gp.player.previousScreenX = gp.player.screenX;
                gp.player.previousScreenY = gp.player.screenY;
                gp.gameState = gp.inInventory;
            }

            if (code == KeyEvent.VK_U){
                healPressed = true;
            }
            if (code == KeyEvent.VK_J){
                jEquipPressed = true;
            }
            if(code == KeyEvent.VK_K){
                kEquipPressed = true;
            }
            if (code == KeyEvent.VK_L){
                lEquipPressed = true;
            }
            if (code == KeyEvent.VK_N){
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
                gp.music.setVolume(gp.music.currentVolume);

                gp.previousState = gp.gameState;
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
                    gp.music.setVolume(gp.music.currentVolume);
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.playState;

                }
                if (gp.ui.commandNumberPause == 1) {
                    gp.playSoundEffect(23);
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.optionState;
                }
                if (gp.ui.commandNumberPause == 2) {
                    gp.stopMusic();
                    gp.playSoundEffect(1);
                    gp.music.setVolume(gp.music.currentVolume);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.titleState;});
                    gp.playMusic(18);
                    gp.saveGame();
                    gp.reset();
                }
            }
        }
        else if(gp.gameState == gp.inInventory){
            if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_TAB){
                gp.previousState = gp.gameState;
                gp.gameState = gp.playState;
                gp.player.screenX = gp.player.previousScreenX;
                gp.player.screenY = gp.player.previousScreenY;
                gp.playSoundEffect(35);
            }

            if(code == KeyEvent.VK_UP || code == KeyEvent.VK_Z){
                gp.playSoundEffect(36);
                if (gp.ui.slotRow == 0){
                    gp.ui.slotRow = 3;
                }else{
                    gp.ui.slotRow--;
                }
            }
            if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S){
                gp.playSoundEffect(36);
                if (gp.ui.slotRow == 3){
                    gp.ui.slotRow = 0;
                }else{
                    gp.ui.slotRow++;
                }
            }
            if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_Q){
                gp.playSoundEffect(36);
                if (gp.ui.slotCol == 0){
                    gp.ui.slotCol = 5;
                }else{
                    gp.ui.slotCol--;
                }
            }
            if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D){
                gp.playSoundEffect(36);
                if (gp.ui.slotCol == 5){
                    gp.ui.slotCol = 0;
                }else{
                    gp.ui.slotCol++;
                }
            }

            if (code == KeyEvent.VK_J){
                SuperObject obj = gp.player.getObjInInventory(gp.ui.slotRow, gp.ui.slotCol);
                if (obj != null && obj.objectType != obj.gear) {
                    if (obj == gp.player.kEquip){
                        gp.player.kEquip = null;
                    }else if (obj == gp.player.lEquip){
                        gp.player.lEquip = null;
                    }
                    gp.player.jEquip = obj;
                    gp.playSoundEffect(38);

                }else{
                    gp.playSoundEffect(37);
                }
            }
            if (code == KeyEvent.VK_K){
                SuperObject obj = gp.player.getObjInInventory(gp.ui.slotRow, gp.ui.slotCol);
                if (obj != null && obj.objectType != obj.gear) {
                    if (obj == gp.player.jEquip) {
                        gp.player.jEquip = null;
                    } else if (obj == gp.player.lEquip) {
                        gp.player.lEquip = null;
                    }
                    gp.player.kEquip = obj;
                    gp.playSoundEffect(40);
                }else{
                    gp.playSoundEffect(37);
                }
            }
            if (code == KeyEvent.VK_L){
                SuperObject obj = gp.player.getObjInInventory(gp.ui.slotRow, gp.ui.slotCol);
                if (obj != null && obj.objectType != obj.gear) {
                    if (obj == gp.player.jEquip) {
                        gp.player.jEquip = null;
                    } else if (obj == gp.player.kEquip) {
                        gp.player.kEquip = null;
                    }
                    gp.player.lEquip = obj;
                    gp.playSoundEffect(39);
                }else{
                    gp.playSoundEffect(37);
                }
            }
        }
        else if(gp.gameState== gp.dialogueState){
            if(code == KeyEvent.VK_ENTER){
                if (gp.ui.messageOn){
                    gp.playSoundEffect(22);
                    gp.ui.messageOn = false;
                }else if(gp.ui.itemOn){
                    gp.playSoundEffect(22);
                    gp.ui.itemOn = false;

                }

                gp.previousState = gp.gameState;
                gp.gameState = gp.playState;
            }
        }
        else if(gp.gameState == gp.gameOver){
            if (code == KeyEvent.VK_UP || code == KeyEvent.VK_Z) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberGameOver == 0) {
                    gp.ui.commandNumberGameOver = 1;
                } else {
                    gp.ui.commandNumberGameOver--;
                }
            }
            if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                gp.playSoundEffect(5);
                if (gp.ui.commandNumberGameOver == 1) {
                    gp.ui.commandNumberGameOver = 0;
                } else {
                    gp.ui.commandNumberGameOver++;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNumberGameOver == 0) {
                    System.out.println("Restart");
                    gp.retry();
                    gp.ui.startOpeningTransition(UI.TransitionType.Iris,80);
                    gp.playSoundEffect(46);
                    gp.playMusic(random.nextInt(3)+19);
                }
                if (gp.ui.commandNumberGameOver == 1) {
                    gp.stopMusic();
                    gp.playSoundEffect(1);
                    gp.music.setVolume(gp.music.currentVolume);
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                        gp.previousState = gp.gameOver;
                        gp.gameState = gp.titleState;});
                    gp.playMusic(18);
                    gp.saveGame();
                    gp.reset();
                }


            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.stopMusic();
                gp.playSoundEffect(1);
                gp.music.setVolume(gp.music.currentVolume);
                gp.ui.startTransition(UI.TransitionType.FadeInOut,15,()->{
                    gp.previousState = gp.gameOver;
                    gp.gameState = gp.titleState;});
                gp.playMusic(18);
                gp.saveGame();

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
            jEquipPressed = false;
        }
        if (code == KeyEvent.VK_K){
            kEquipPressed = false;
        }
        if (code == KeyEvent.VK_L){
            lEquipPressed = false;
        }
        if (code == KeyEvent.VK_N){
            parryPressed = false;
            if(gp.player.entityStatus ==gp.player.parrying){
                gp.playSoundEffect(31);
            }

        }
        if(code == KeyEvent.VK_F){
            interactionPressed = false;

        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }



    }
}
