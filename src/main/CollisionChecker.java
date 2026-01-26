package main;

import entity.Entity;

import java.util.ArrayList;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y+entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = (entityRightWorldX - 1 )/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = (entityBottomWorldY - 1 )/gp.tileSize;

        ArrayList<String> collisionSide1;
        ArrayList<String> collisionSide2;

        int tileNum1,tileNum2;

        switch (entity.direction){
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                if (entityTopRow < 0){
                    entity.collisionOn = true;
                }
                tileNum1 = gp.tileM.mapTileNum1[entityTopRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum1[entityTopRow][entityRightCol];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    collisionSide1 = gp.tileM.tile[tileNum1].collisionSide;
                    collisionSide2 = gp.tileM.tile[tileNum2].collisionSide;

                    if(collisionSide1.contains("up")){
                        int topY =entityTopRow*gp.tileSize;
                        if(entityTopWorldY-entity.speed*3 <= topY){
                            entity.collisionOn = true;
                        }

                    }if (collisionSide1.contains("down")) {
                        int topY = entityTopRow * gp.tileSize+ gp.tileSize;
                        if (entityTopWorldY - entity.speed <= topY && entityBottomWorldY - entity.speed >= topY) {
                            entity.collisionOn = true;
                        }

                    }
                    if(collisionSide1.contains("left")){
                        int leftX = entityLeftCol*gp.tileSize;
                        if(entityLeftWorldX <= leftX){
                            entity.collisionOn = true;
                        }

                    }if(collisionSide1.contains("right")){
                        int rightX = entityLeftCol*gp.tileSize+gp.tileSize;
                        if(entityLeftWorldX <= rightX && entityRightWorldX >= rightX){
                            entity.collisionOn = true;
                        }

                    }if(collisionSide2.contains("up")){
                        int topY =entityTopRow * gp.tileSize;
                        if(entityTopWorldY-entity.speed*3 <= topY){
                            entity.collisionOn = true;
                        }

                    }if(collisionSide2.contains("down")) {
                        int topY = entityTopRow * gp.tileSize + gp.tileSize;
                        if (entityTopWorldY - entity.speed <= topY  && entityBottomWorldY - entity.speed >= topY) {
                            entity.collisionOn = true;

                        }
                    }if(collisionSide2.contains("left")){
                        int leftX = entityRightCol*gp.tileSize;
                        if(entityRightWorldX >= leftX && entityLeftWorldX <= leftX){
                            entity.collisionOn = true;
                        }


                    }if(collisionSide2.contains("right")){
                        int rightX = entityRightCol*gp.tileSize+gp.tileSize;
                        if(entityRightWorldX >= rightX){
                            entity.collisionOn = true;
                        }
                    }
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                if(entityBottomRow >= gp.maxWorldRow){
                    entity.collisionOn = true;
                    break;
                }
                tileNum1 = gp.tileM.mapTileNum1[entityBottomRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum1[entityBottomRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {

                    collisionSide1 = gp.tileM.tile[tileNum1].collisionSide;
                    collisionSide2 = gp.tileM.tile[tileNum2].collisionSide;

                    if (collisionSide1.contains("up")) {
                        int topY = entityBottomRow * gp.tileSize;
                        if (entityBottomWorldY + entity.speed >= topY && entityTopWorldY + entity.speed <= topY) {
                            entity.collisionOn = true;
                        }

                    }if (collisionSide1.contains("down")) {
                        int topY = entityBottomRow * gp.tileSize + gp.tileSize;
                        if (entityBottomWorldY + entity.speed*3 >= topY) {
                            entity.collisionOn = true;

                        }

                    }
                    if(collisionSide1.contains("left")){
                        int leftX = entityLeftCol*gp.tileSize;
                        if(entityLeftWorldX <= leftX){
                            entity.collisionOn = true;
                        }

                    }if(collisionSide1.contains("right")){
                        int rightX = entityLeftCol*gp.tileSize+gp.tileSize;
                        if(entityLeftWorldX <= rightX && entityRightWorldX >= rightX){
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("up")) {
                        int topY = entityBottomRow * gp.tileSize;
                        if (entityBottomWorldY + entity.speed >= topY &&  entityTopWorldY + entity.speed <= topY) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("down")) {
                        int topY = entityBottomRow * gp.tileSize + gp.tileSize;
                        if (entityBottomWorldY + entity.speed*3 >= topY) {
                            entity.collisionOn = true;

                        }
                    }
                    if(collisionSide2.contains("left")){
                        int leftX = entityRightCol*gp.tileSize;
                        if(entityRightWorldX >= leftX && entityLeftWorldX <= leftX){
                            entity.collisionOn = true;
                        }


                    }if(collisionSide2.contains("right")){
                        int rightX = entityRightCol*gp.tileSize+gp.tileSize;
                        if(entityRightWorldX >= rightX){
                            entity.collisionOn = true;
                        }
                    }
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                if(entityLeftCol < 0){
                    entity.collisionOn = true;
                    break;
                }
                tileNum1 = gp.tileM.mapTileNum1[entityTopRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum1[entityBottomRow][entityLeftCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    collisionSide1 = gp.tileM.tile[tileNum1].collisionSide;
                    collisionSide2 = gp.tileM.tile[tileNum2].collisionSide;

                    if (collisionSide1.contains("up")) {
                        int topY = entityTopRow * gp.tileSize+3;
                        if (entityTopWorldY <= topY) {
                            entity.collisionOn = true;
                        }

                    }if (collisionSide1.contains("down")) {
                        int topY = entityTopRow * gp.tileSize + gp.tileSize-3;
                        if (entityTopWorldY <= topY && entityBottomWorldY >= topY) {
                            entity.collisionOn = true;

                        }

                    }
                    if (collisionSide1.contains("left")) {
                        int leftX = entityLeftCol * gp.tileSize;
                        if (entityLeftWorldX - entity.speed*3 <= leftX) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide1.contains("right")) {
                        int rightX = entityLeftCol * gp.tileSize + gp.tileSize;
                        if (entityLeftWorldX - entity.speed <= rightX && entityRightWorldX >= rightX) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("up")) {
                        int topY = entityBottomRow * gp.tileSize+3;
                        if (entityBottomWorldY  >= topY && entityTopWorldY <= topY) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("down")) {
                        int topY = entityBottomRow * gp.tileSize + gp.tileSize-3;
                        if (entityBottomWorldY >= topY) {
                            entity.collisionOn = true;

                        }
                    }
                    if (collisionSide2.contains("left")) {
                        int leftX = entityLeftCol * gp.tileSize;
                        if (entityLeftWorldX - entity.speed*3 <= leftX) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("right")) {
                        int rightX = entityLeftCol * gp.tileSize + gp.tileSize;
                        if (entityLeftWorldX - entity.speed <= rightX && entityRightWorldX >= rightX) {
                            entity.collisionOn = true;
                        }

                    }
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum1[entityTopRow][entityRightCol];
                tileNum2 = gp.tileM.mapTileNum1[entityBottomRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    collisionSide1 = gp.tileM.tile[tileNum1].collisionSide;
                    collisionSide2 = gp.tileM.tile[tileNum2].collisionSide;

                    if (collisionSide1.contains("up")) {
                        int topY = entityTopRow * gp.tileSize;
                        if (entityTopWorldY <= topY) {
                            entity.collisionOn = true;
                        }

                    }if (collisionSide1.contains("down")) {
                        int topY = entityTopRow * gp.tileSize + gp.tileSize;
                        if (entityTopWorldY <= topY && entityBottomWorldY >= topY) {
                            entity.collisionOn = true;

                        }

                    }
                    if (collisionSide1.contains("left")) {
                        int leftX = entityRightCol * gp.tileSize;
                        if (entityRightWorldX + entity.speed >= leftX && entityLeftWorldX + entity.speed <= leftX) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide1.contains("right")) {
                        int rightX = entityRightCol * gp.tileSize + gp.tileSize;
                        if (entityRightWorldX+ entity.speed*3 >= rightX) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("up")) {
                        int topY = entityBottomRow * gp.tileSize;
                        if (entityBottomRow  >= topY && entityTopRow <= topY) {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("down")) {
                        int topY = entityBottomRow * gp.tileSize + gp.tileSize;
                        if (entityBottomRow >= topY) {
                            entity.collisionOn = true;

                        }
                    }
                    if (collisionSide2.contains("left")) {
                        int leftX = entityRightCol * gp.tileSize;
                        if (entityRightWorldX + entity.speed >= leftX && entityLeftWorldX + entity.speed <= leftX)  {
                            entity.collisionOn = true;
                        }

                    }
                    if (collisionSide2.contains("right")) {
                        int rightX = entityRightCol * gp.tileSize + gp.tileSize;
                        if (entityRightWorldX + entity.speed*3 >= rightX) {
                            entity.collisionOn = true;
                        }

                    }                }
                break;
        }

    }

    public int checkObject(Entity entity,boolean player){

        int index = 999;

        for(int i =0; i<gp.obj.length; i++){
            if(gp.obj[i]!= null){
                //Get entity's solide area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //Get object's solide area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction){
                    case "up": entity.solidArea.y -= entity.speed;break;
                    case "down": entity.solidArea.y += entity.speed;break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed;break;
                }
                if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                    if (gp.obj[i].collision){
                        entity.collisionOn = true;
                    }
                    if(player){
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solideAreaDefaultX;
                entity.solidArea.y = entity.solideAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solideAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solideAreaDefaultY;
            }

        }


        return index;

    }

    public int checkEntity(Entity entity,ArrayList<Entity> target){
        int index = 999;

        for(int i =0; i<target.size(); i++){
            if(target.get(i)!= null){
                //Get entity's solide area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                //Get other entity's solide area position
                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch(entity.direction){
                    case "up": entity.solidArea.y -= entity.speed;break;
                    case "down": entity.solidArea.y += entity.speed;break;
                    case "left": entity.solidArea.x -= entity.speed;break;
                    case "right": entity.solidArea.x += entity.speed;break;

                }
                if(entity.solidArea.intersects(target.get(i).solidArea)){
                    if (target.get(i) != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }

                }
                entity.solidArea.x = entity.solideAreaDefaultX;
                entity.solidArea.y = entity.solideAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solideAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solideAreaDefaultY;
            }

        }
        return index;
    }

    public void checkPlayer(Entity entity){
        //Get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        //Get player solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch(entity.direction){
            case "up": entity.solidArea.y -= entity.speed;break;
            case "down": entity.solidArea.y += entity.speed;break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed;break;
        }
        if(entity.solidArea.intersects(gp.player.solidArea)){
            entity.collisionOn = true;
        }

        entity.solidArea.x = entity.solideAreaDefaultX;
        entity.solidArea.y = entity.solideAreaDefaultY;
        gp.player.solidArea.x = gp.player.solideAreaDefaultX;
        gp.player.solidArea.y = gp.player.solideAreaDefaultY;
    }

    public void checkAttack(Entity attackEntity,Entity targetEntity){
        //Get  target entity's solid area position
        targetEntity.solidArea.x = targetEntity.worldX + targetEntity.solidArea.x;
        targetEntity.solidArea.y = targetEntity.worldY + targetEntity.solidArea.y;

        //Get attackEntity attackingAreas position
        attackEntity.attackingAreaVertical.x += attackEntity.worldX;
        attackEntity.attackingAreaHorizontal.x += attackEntity.worldX;
        attackEntity.attackingAreaVertical.y += attackEntity.worldY;
        attackEntity.attackingAreaHorizontal.y += attackEntity.worldY;

        switch(attackEntity.direction){
            case "up":
                attackEntity.attackingArea = attackEntity.attackingAreaVertical;
                attackEntity.attackingArea.y -= gp.tileSize;
                break;
            case "down":
                attackEntity.attackingArea = attackEntity.attackingAreaVertical;
                attackEntity.attackingArea.y += gp.tileSize;
                break;
            case "left":
                attackEntity.attackingArea = attackEntity.attackingAreaHorizontal;
                attackEntity.attackingArea.x -= gp.tileSize;
                break;
            case "right":
                attackEntity.attackingArea = attackEntity.attackingAreaHorizontal;
                attackEntity.attackingArea.x += gp.tileSize;
                 break;

        }
        if(attackEntity.attackingArea.intersects(targetEntity.solidArea)){
            attackEntity.hitOn = true;
        }

        attackEntity.attackingAreaHorizontal.x = attackEntity.attackingAreaDefaultHX;
        attackEntity.attackingAreaHorizontal.y = attackEntity.attackingAreaDefaultHY;
        attackEntity.attackingAreaVertical.x = attackEntity.attackingAreaDefaultVX;
        attackEntity.attackingAreaVertical.y = attackEntity.attackingAreaDefaultVY;
        targetEntity.solidArea.x = targetEntity.solideAreaDefaultX;
        targetEntity.solidArea.y = targetEntity.solideAreaDefaultY;

    }

}

