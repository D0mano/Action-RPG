package main;

import entity.Entity;

import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

/**
 * The CollisionChecker class is responsible for detecting interactions and overlaps 
 * between the player, monsters, objects, attack hitboxes, and the map environment (tiles).
 */
public class CollisionChecker {
    GamePanel gp;

    /**
     * Constructor for the CollisionChecker.
     * @param gp The GamePanel instance.
     */
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Checks if the given entity is about to collide with a solid tile on the map.
     * It projects the entity's hitbox forward based on its current speed and direction
     * to prevent it from walking into obstacles.
     * @param entity The entity (Player or Monster) attempting to move.
     */
    public void checkTile(Entity entity) {
        // Find the precise pixel coordinates of the entity's solid area (hitbox)
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Convert pixel coordinates into map grid columns and rows
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = (entityRightWorldX - 1) / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = (entityBottomWorldY - 1) / gp.tileSize;

        ArrayList<String> collisionSide1;
        ArrayList<String> collisionSide2;

        int tileNum1, tileNum2,tileNum3; // The third tileNum is relevant only for Entity whose width is superior to the tileSize (Rudeling)
        for (int layer = 0 ;layer < gp.maxWorldLayer ;layer ++) {
            switch (entity.direction) {
                case "up":
                    // Predict the next row the entity will enter
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    if (entityTopRow < 0) {
                        entity.collisionOn = true;
                        break;
                    }

                    // Check the top-left and top-right corners of the hitbox against the tile map
                    tileNum1 = gp.tileM.currentMap.tileMap[entityTopRow][entityLeftCol][layer];
                    tileNum2 = gp.tileM.currentMap.tileMap[entityTopRow][entityRightCol][layer];

                    // For bigger Entity we also check the middle
                    tileNum3 = gp.tileM.currentMap.tileMap[entityTopRow][(entityLeftCol+entityRightCol)/2][layer];

                    if ((tileNum1 != -1 && gp.tileM.tile[tileNum1].collision ) || (tileNum2 != -1 && gp.tileM.tile[tileNum2].collision) || (tileNum3 != -1 && gp.tileM.tile[tileNum3].collision)) {
                        entity.collisionOn = true;
                    }
                    break;

                case "down":

                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    if (entityBottomRow >= gp.maxWorldRow) {
                        entity.collisionOn = true;
                        break;
                    }
                    tileNum1 = gp.tileM.currentMap.tileMap[entityBottomRow][entityLeftCol][layer];
                    tileNum2 = gp.tileM.currentMap.tileMap[entityBottomRow][entityRightCol][layer];
                    tileNum3 = gp.tileM.currentMap.tileMap[entityBottomRow][(entityLeftCol+entityRightCol)/2][layer];


                    if ((tileNum1 != -1 && gp.tileM.tile[tileNum1].collision ) || (tileNum2 != -1 && gp.tileM.tile[tileNum2].collision) || (tileNum3 != -1 && gp.tileM.tile[tileNum3].collision)) {
                        entity.collisionOn = true;
                    }
                    break;

                case "left":
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    if (entityLeftCol < 0) {
                        entity.collisionOn = true;
                        break;
                    }
                    tileNum1 = gp.tileM.currentMap.tileMap[entityTopRow][entityLeftCol][layer];
                    tileNum2 = gp.tileM.currentMap.tileMap[entityBottomRow][entityLeftCol][layer];
                    tileNum3 =gp.tileM.currentMap.tileMap[(entityTopRow+entityBottomRow)/2][entityLeftCol][layer];

                    if ((tileNum1 != -1 && gp.tileM.tile[tileNum1].collision ) || (tileNum2 != -1 && gp.tileM.tile[tileNum2].collision) || (tileNum3 != -1 && gp.tileM.tile[tileNum3].collision)) {
                        entity.collisionOn = true;
                    }
                    break;

                case "right":

                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    if (entityRightCol >= gp.maxWorldCol) {
                        entity.collisionOn = true;
                        break;
                    }
                    tileNum1 = gp.tileM.currentMap.tileMap[entityTopRow][entityRightCol][layer];
                    tileNum2 = gp.tileM.currentMap.tileMap[entityBottomRow][entityRightCol][layer];
                    tileNum3 =gp.tileM.currentMap.tileMap[(entityTopRow+entityBottomRow)/2][entityRightCol][layer];

                    if ((tileNum1 != -1 && gp.tileM.tile[tileNum1].collision ) || (tileNum2 != -1 && gp.tileM.tile[tileNum2].collision) || (tileNum3 != -1 && gp.tileM.tile[tileNum3].collision)) {
                        entity.collisionOn = true;
                    }
                    break;
            }
        }

    }

    /**
     * Projects the entity's attack hitbox to check if there are destructible tiles (like bushes)
     * in front of it that should be destroyed by the attack.
     * @param entity The entity performing the attack.
     * @return A list of Points representing the column/row coordinates of bushes to remove.
     */
    public List<Point> checkCanCut(Entity entity) {
        List<Point> bushHit = new ArrayList<>();

        // Temporarily adjust the attack area position to the real world coordinates
        entity.attackingAreaVertical.x += entity.worldX;
        entity.attackingAreaHorizontal.x += entity.worldX;
        entity.attackingAreaVertical.y += entity.worldY;
        entity.attackingAreaHorizontal.y += entity.worldY;

        // Position the hitbox correctly according to the direction the entity faces
        switch (entity.direction) {
            case "up":
                entity.attackingArea = entity.attackingAreaVertical;
                entity.attackingArea.y -= gp.tileSize;
                break;
            case "down":
                entity.attackingArea = entity.attackingAreaVertical;
                entity.attackingArea.y += gp.tileSize;
                break;
            case "left":
                entity.attackingArea = entity.attackingAreaHorizontal;
                entity.attackingArea.x -= gp.tileSize;
                break;
            case "right":
                entity.attackingArea = entity.attackingAreaHorizontal;
                entity.attackingArea.x += gp.tileSize;
                break;
        }

        // Get map grid values covering the corners of the attack hitbox
        int entityLeftAttCol = entity.attackingArea.x / gp.tileSize;
        int entityRightAttCol = (entity.attackingArea.x + entity.attackingArea.width) / gp.tileSize;
        int entityTopAttRow = entity.attackingArea.y / gp.tileSize;
        int entityBottomAttRow = (entity.attackingArea.y + entity.attackingArea.height) / gp.tileSize;

        // Check if the specific tile ID matches a bush (ID 61)
        if (gp.tileM.currentMap.tileMap[entityTopAttRow][entityRightAttCol][0] == 61) {
            bushHit.add(new Point(entityRightAttCol, entityTopAttRow));
        }
        if (gp.tileM.currentMap.tileMap[entityTopAttRow][entityLeftAttCol][0] == 61) {
            bushHit.add(new Point(entityLeftAttCol, entityTopAttRow));
        }
        if (gp.tileM.currentMap.tileMap[entityBottomAttRow][entityRightAttCol][0] == 61) {
            bushHit.add(new Point(entityRightAttCol, entityBottomAttRow));
        }
        if (gp.tileM.currentMap.tileMap[entityBottomAttRow][entityLeftAttCol][0] == 61) {
            bushHit.add(new Point(entityLeftAttCol, entityBottomAttRow));
        }

        // Reset the hitbox values back to their relative defaults
        entity.attackingAreaHorizontal.x = entity.attackingAreaDefaultHX;
        entity.attackingAreaHorizontal.y = entity.attackingAreaDefaultHY;
        entity.attackingAreaVertical.x = entity.attackingAreaDefaultVX;
        entity.attackingAreaVertical.y = entity.attackingAreaDefaultVY;

        return bushHit;
    }

    /**
     * Checks if the entity overlaps with any map objects (like items on the ground or chests).
     * Stops the entity's movement if the object is solid.
     * @param entity The moving entity to verify.
     * @param player True if the entity is the player (useful for deciding if an item should be picked up).
     * @return The array index of the colliding object (or 999 if no collision).
     */
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.obj.size(); i++) {
            if (gp.obj.get(i) != null) {
                // Get entity's solid area global position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get object's solid area global position
                gp.obj.get(i).solidArea.x = gp.obj.get(i).worldX + gp.obj.get(i).solidArea.x;
                gp.obj.get(i).solidArea.y = gp.obj.get(i).worldY + gp.obj.get(i).solidArea.y;

                // Predict future position based on speed and direction
                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                // If hitboxes intersect
                if (entity.solidArea.intersects(gp.obj.get(i).solidArea)) {
                    if (gp.obj.get(i).collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                // Reset hitboxes
                entity.solidArea.x = entity.solideAreaDefaultX;
                entity.solidArea.y = entity.solideAreaDefaultY;
                gp.obj.get(i).solidArea.x = gp.obj.get(i).solideAreaDefaultX;
                gp.obj.get(i).solidArea.y = gp.obj.get(i).solideAreaDefaultY;
            }
        }
        return index;
    }

    /**
     * Checks if a moving entity is colliding with any other entities from a list
     * (e.g., player colliding with monsters, or monsters colliding with each other).
     * @param entity The moving entity to verify.
     * @param target The target list of entities to check against.
     * @return The index of the entity in the target list that was hit, or 999.
     */
    public int checkEntity(Entity entity, ArrayList<Entity> target) {
        int index = 999;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get other entity's solid area position
                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    // Make sure an entity doesn't collide with itself
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

    /**
     * Specific method for monsters or projectiles to check if they are colliding 
     * with the player character.
     * @param entity The entity to check against the player.
     */
    public void checkPlayer(Entity entity) {
        // Get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        // Get player solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
        }

        // Reset coordinates
        entity.solidArea.x = entity.solideAreaDefaultX;
        entity.solidArea.y = entity.solideAreaDefaultY;
        gp.player.solidArea.x = gp.player.solideAreaDefaultX;
        gp.player.solidArea.y = gp.player.solideAreaDefaultY;
    }

    /**
     * Resolves interactions between an attacker's weapon hitbox and a target's physical hitbox.
     * If they intersect, it sets the `hitOn` flag to true on the attacker.
     * @param attackEntity The entity performing the physical attack.
     * @param targetEntity The entity receiving the attack.
     */
    public void checkAttack(Entity attackEntity, Entity targetEntity) {
        // Get target entity's solid area position
        targetEntity.solidArea.x = targetEntity.worldX + targetEntity.solidArea.x;
        targetEntity.solidArea.y = targetEntity.worldY + targetEntity.solidArea.y;

        // Base location for attack areas
        attackEntity.attackingAreaVertical.x += attackEntity.worldX;
        attackEntity.attackingAreaHorizontal.x += attackEntity.worldX;
        attackEntity.attackingAreaVertical.y += attackEntity.worldY;
        attackEntity.attackingAreaHorizontal.y += attackEntity.worldY;

        // Offset the hitbox based on facing direction
        switch (attackEntity.direction) {
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

        // Validate contact
        if (attackEntity.attackingArea.intersects(targetEntity.solidArea)) {
            attackEntity.hitOn = true;
        }

        // Reset hitboxes
        attackEntity.attackingAreaHorizontal.x = attackEntity.attackingAreaDefaultHX;
        attackEntity.attackingAreaHorizontal.y = attackEntity.attackingAreaDefaultHY;
        attackEntity.attackingAreaVertical.x = attackEntity.attackingAreaDefaultVX;
        attackEntity.attackingAreaVertical.y = attackEntity.attackingAreaDefaultVY;
        targetEntity.solidArea.x = targetEntity.solideAreaDefaultX;
        targetEntity.solidArea.y = targetEntity.solideAreaDefaultY;
    }

    /**
     * Recursively searches for the next available, non-colliding tile 
     * in the opposite trajectory of the given direction.
     * This is frequently used for calculating knockback or safely spawning an entity.
     * @param worldCol Current grid column.
     * @param worldRow Current grid row.
     * @param direction The impact direction.
     * @return A Point containing the grid coordinates (x=Col, y=Row) of a safe, non-solid tile.
     */
    public Point findNextFreeTile(int worldCol, int worldRow, String direction) {
        int tileIndex;
        int worldForwardCol, worldForwardRow;

        // Search backward from the impact direction
        switch (direction) {
            case "up":
                worldForwardCol = worldCol;
                worldForwardRow = worldRow + 1; // Pushes Down
                break;
            case "down":
                worldForwardCol = worldCol;
                worldForwardRow = worldRow - 1; // Pushes Up
                break;
            case "left":
                worldForwardCol = worldCol + 1; // Pushes Right
                worldForwardRow = worldRow;
                break;
            case "right":
                worldForwardCol = worldCol - 1; // Pushes Left
                worldForwardRow = worldRow;
                break;
            default:
                worldForwardCol = worldCol;
                worldForwardRow = worldRow;
        }

        // Evaluate if the newly calculated tile is a solid wall
        tileIndex = gp.tileM.currentMap.tileMap[worldForwardRow][worldForwardCol][0];
        if (gp.tileM.tile[tileIndex].collision) {
            // If it's solid, search one step further away
            return findNextFreeTile(worldForwardCol, worldForwardRow, direction);
        }

        // Return the safe coordinates once a valid spot is found
        return new Point(worldForwardCol, worldForwardRow);
    }
}