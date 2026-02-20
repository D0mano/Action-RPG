package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    Rectangle eventRect;
    int eventDefaultRectX;
    int eventDefaultRectY;
    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new Rectangle(0,0,0,0);
        eventRect.x = (gp.tileSize*7)/16;
        eventRect.y = (gp.tileSize*7)/16;
        eventRect.width = (gp.tileSize)/8;
        eventRect.height = (gp.tileSize)/8;
        eventDefaultRectX = eventRect.x;
        eventDefaultRectY = eventRect.y;

    }

    public void checkEvent(){
        if (gp.currentMapIndex == 0){
            //Access to the forest
            if (hit(95,14,"right")){
                if (!gp.ui.transitionOn){
                    gp.ui.startTransition(UI.TransitionType.FadeInOut,60,()->{
                        gp.setMap(1);
                        gp.player.worldX = gp.tileM.currentMap.playerCol * gp.tileSize;
                        gp.player.worldY = gp.tileM.currentMap.playerRow * gp.tileSize;
                        gp.player.screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                        gp.player.screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                    });
                }
            }
        }
    }

    public boolean hit(int eventCol ,int eventRow, String reqDirection){
        boolean hit = false;
        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;
        eventRect.x += eventCol*gp.tileSize;
        eventRect.y += eventRow*gp.tileSize;

        if (gp.player.solidArea.intersects(eventRect)){
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;
            }
        }
        gp.player.solidArea.x = gp.player.solideAreaDefaultX;
        gp.player.solidArea.y = gp.player.solideAreaDefaultY;
        eventRect.x = eventDefaultRectX;
        eventRect.y = eventDefaultRectY;


        return hit;
    }
}
