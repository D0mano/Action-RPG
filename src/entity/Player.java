package entity;

import main.*;
import object.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

/**
 * The Player class represents the main character controlled by the user.
 * It handles movement, collision detection, attacks, rolling, parrying,
 * inventory management, and smooth status rendering (health/mana/endurance).
 */
public class Player extends Entity {
    KeyHandler keyH;
    int hasKey = 0;

    // ENDURANCE / STAMINA SYSTEM
    public int maxEndurance;
    public int endurance;
    public int enduranceCost;
    public float displayedEndurance = 0; // Used for smooth UI gauge rendering
    public float displayedMana = 0;      // Used for smooth UI gauge rendering
    public boolean useEndurance = false;
    public int enduranceCounter;
    public int enduranceDuration;

    // ROLLING & MOVEMENT
    int rollCounter;
    int rollDuration;
    int rollSpeed;
    int parryingSpeed;

    // ANIMATORS
    Animator downRollAnimator, upRollAnimator, leftRollAnimator, rightRollAnimator;
    Animator downIdleAnimator, upIdleAnimator, leftIdleAnimator, rightIdleAnimator;
    Animator downParryAnimator, upParryAnimator, leftParryAnimator, rightParryAnimator;

    public float displayedHealth = health; // Used for smooth UI gauge rendering

    // SCREEN POSITION (Camera center)
    public int screenX, screenY;
    public int previousScreenX, previousScreenY;

    // ANIMATION SPRITE SHEETS
    public BufferedImage downRoll, upRoll, leftRoll, rightRoll;
    public BufferedImage downIdle, upIdle, leftIdle, rightIdle;
    public BufferedImage downParry, upParry, leftParry, rightParry;

    // KEY PRESS FLAGS (To prevent rapid-fire triggering)
    public boolean jEquipKeyProcessed = false;
    public boolean kEquipKeyProcessed = false;
    public boolean healKeyProcessed = false;
    public boolean parryKeyProcessed = false;
    public boolean lEquipKeyProcessed = false;
    public boolean interactionKeyProcessed = false;

    // INVENTORY SYSTEM
    // Array of 3 lists: [0] = Gear, [1] = Single Use, [2] = Equipment
    public ArrayList<SuperObject>[] inventory = new ArrayList[3];
    public final int gearSize = 6;
    public final int singleUseSize = 12;
    public final int equipmentSize = 6;

    // EQUIPPED ITEMS SLOTS
    public SuperObject jEquip;
    public SuperObject kEquip;
    public SuperObject lEquip;

    /**
     * Constructor for the Player.
     * Initializes stats, hitboxes, UI values, and all animation frames.
     * * @param gp   The GamePanel instance.
     * @param keyH The Input/Key Handler instance.
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        // Center player on the screen
        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        setDefaultsValues();
        getPlayerImage();

        // Initialize inventory arrays
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ArrayList<>();
        }
        setInventory();
        projectile = new IceBall(gp); // Default projectile (can be modified later)

        // SOLID AREA HITBOX (Collision with world/objects)
        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 6;
        solidArea.y = gp.tileSize / 3;
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;
        solidArea.width = (2 * gp.tileSize) / 3;
        solidArea.height = (2 * gp.tileSize) / 3;

        // ATTACK AREA HITBOXES (Vertical & Horizontal)
        attackingAreaVertical.x = gp.tileSize / 8;
        attackingAreaVertical.y = 0;
        attackingAreaDefaultVX = attackingAreaVertical.x;
        attackingAreaDefaultVY = attackingAreaVertical.y;
        attackingAreaVertical.width = (int) (gp.tileSize * 3 / 4f);
        attackingAreaVertical.height = gp.tileSize;

        attackingAreaHorizontal.x = attackingAreaVertical.y;
        attackingAreaHorizontal.y = attackingAreaVertical.x;
        attackingAreaDefaultHX = attackingAreaHorizontal.x;
        attackingAreaDefaultHY = attackingAreaHorizontal.y;
        attackingAreaHorizontal.width = attackingAreaVertical.height;
        attackingAreaHorizontal.height = attackingAreaVertical.width;

        // INSTANTIATE ALL ANIMATORS
        downAnimator = new Animator(down, gp.tileSize, gp.tileSize, 12, true);
        upAnimator = new Animator(up, gp.tileSize, gp.tileSize, 12, true);
        leftAnimator = new Animator(left, gp.tileSize, gp.tileSize, 12, true);
        rightAnimator = new Animator(right, gp.tileSize, gp.tileSize, 12, true);

        downRollAnimator = new Animator(downRoll, gp.tileSize, gp.tileSize, 6, true);
        upRollAnimator = new Animator(upRoll, gp.tileSize, gp.tileSize, 6, true);
        leftRollAnimator = new Animator(leftRoll, gp.tileSize, gp.tileSize, 6, true);
        rightRollAnimator = new Animator(rightRoll, gp.tileSize, gp.tileSize, 6, true);

        downIdleAnimator = new Animator(downIdle, gp.tileSize, gp.tileSize, 10, true);
        upIdleAnimator = new Animator(upIdle, gp.tileSize, gp.tileSize, 10, true);
        leftIdleAnimator = new Animator(leftIdle, gp.tileSize, gp.tileSize, 10, true);
        rightIdleAnimator = new Animator(rightIdle, gp.tileSize, gp.tileSize, 10, true);

        downAttackingAnimator = new Animator(downAttacking, gp.tileSize * 2, gp.tileSize * 2, 10, false);
        upAttackingAnimator = new Animator(upAttacking, gp.tileSize * 2, gp.tileSize * 2, 10, false);
        leftAttackingAnimator = new Animator(leftAttacking, gp.tileSize * 2, gp.tileSize * 2, 10, false);
        rightAttackingAnimator = new Animator(rightAttacking, gp.tileSize * 2, gp.tileSize * 2, 10, false);

        downParryAnimator = new Animator(downParry, gp.tileSize, gp.tileSize, 12, true);
        upParryAnimator = new Animator(upParry, gp.tileSize, gp.tileSize, 12, true);
        leftParryAnimator = new Animator(leftParry, gp.tileSize, gp.tileSize, 12, true);
        rightParryAnimator = new Animator(rightParry, gp.tileSize, gp.tileSize, 12, true);
    }

    /**
     * Reloads all variables and images tied to screen scaling or resolution changes.
     */
    public void reload() {
        worldX = gp.tileSize * worldCol;
        worldY = gp.tileSize * worldRow;
        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        normalSpeed = gp.tileSize / 10;
        speed = normalSpeed;
        parryingSpeed = normalSpeed / 2;
        rollSpeed = 2 * normalSpeed;

        getPlayerImage();
        projectile.reload();

        // Hitbox resize
        solidArea = new Rectangle();
        solidArea.x = gp.tileSize / 6;
        solidArea.y = gp.tileSize / 3;
        solideAreaDefaultX = solidArea.x;
        solideAreaDefaultY = solidArea.y;
        solidArea.width = (2 * gp.tileSize) / 3;
        solidArea.height = (2 * gp.tileSize) / 3;

        attackingAreaVertical.x = gp.tileSize / 8;
        attackingAreaVertical.y = 0;
        attackingAreaDefaultVX = attackingAreaVertical.x;
        attackingAreaDefaultVY = attackingAreaVertical.y;
        attackingAreaVertical.width = (int) (gp.tileSize * 3 / 4f);
        attackingAreaVertical.height = gp.tileSize;

        attackingAreaHorizontal.x = attackingAreaVertical.y;
        attackingAreaHorizontal.y = attackingAreaVertical.x;
        attackingAreaDefaultHX = attackingAreaHorizontal.x;
        attackingAreaDefaultHY = attackingAreaHorizontal.y;
        attackingAreaHorizontal.width = attackingAreaVertical.height;
        attackingAreaHorizontal.height = attackingAreaVertical.width;

        // Reload Animators to fit the new sprite sheets sizes
        downAnimator.reload(down, gp.tileSize, gp.tileSize);
        upAnimator.reload(up, gp.tileSize, gp.tileSize);
        leftAnimator.reload(left, gp.tileSize, gp.tileSize);
        rightAnimator.reload(right, gp.tileSize, gp.tileSize);

        downRollAnimator.reload(downRoll, gp.tileSize, gp.tileSize);
        upRollAnimator.reload(upRoll, gp.tileSize, gp.tileSize);
        leftRollAnimator.reload(leftRoll, gp.tileSize, gp.tileSize);
        rightRollAnimator.reload(rightRoll, gp.tileSize, gp.tileSize);

        downIdleAnimator.reload(downIdle, gp.tileSize, gp.tileSize);
        upIdleAnimator.reload(upIdle, gp.tileSize, gp.tileSize);
        leftIdleAnimator.reload(leftIdle, gp.tileSize, gp.tileSize);
        rightIdleAnimator.reload(rightIdle, gp.tileSize, gp.tileSize);

        downAttackingAnimator.reload(downAttacking, gp.tileSize * 2, gp.tileSize * 2);
        upAttackingAnimator.reload(upAttacking, gp.tileSize * 2, gp.tileSize * 2);
        leftAttackingAnimator.reload(leftAttacking, gp.tileSize * 2, gp.tileSize * 2);
        rightAttackingAnimator.reload(rightAttacking, gp.tileSize * 2, gp.tileSize * 2);

        downParryAnimator.reload(downParry, gp.tileSize, gp.tileSize);
        upParryAnimator.reload(upParry, gp.tileSize, gp.tileSize);
        leftParryAnimator.reload(leftParry, gp.tileSize, gp.tileSize);
        rightParryAnimator.reload(rightParry, gp.tileSize, gp.tileSize);

        reloadInventory();
    }

    /**
     * Sets the default base stats for a fresh new game session.
     */
    public void setDefaultsValues() {
        worldCol = 0;
        worldRow = 0;
        worldX = gp.tileSize * worldCol;
        worldY = gp.tileSize * worldRow;

        normalSpeed = gp.tileSize / 10;
        speed = normalSpeed;
        parryingSpeed = normalSpeed / 2;
        direction = "down";
        attackPower = 30;

        // PLAYER STATUS
        maxHealth = 100;
        health = 1;
        maxMana = 100;
        mana = 100;

        maxPotion = 3;
        potionNotUsed = maxPotion;

        maxEndurance = 100;
        endurance = 100;
        enduranceDuration = 120;
        enduranceCounter = 0;
        enduranceCost = 40;

        entityStatus = idle;
        rollCounter = 0;
        rollDuration = 30;
        rollSpeed = 2 * normalSpeed;

        attackCounter = 0;
        attackDuration = 40;
        invisibleTimer = 15;

        deathSoundIndex = 10;
    }

    /**
     * Loads the player's sprite sheets via the setup method.
     */
    public void getPlayerImage() {
        up = setup("walking/player_up-Sheet", gp.scale);
        down = setup("walking/player_down-Sheet", gp.scale);
        left = setup("walking/player_left-Sheet", gp.scale);
        right = setup("walking/player_right-Sheet", gp.scale);

        downRoll = setup("rolling/player_down-roll-Sheet", gp.scale);
        upRoll = setup("rolling/player_up-roll-Sheet", gp.scale);
        leftRoll = setup("rolling/player_left-roll-Sheet", gp.scale);
        rightRoll = setup("rolling/player_right-roll-Sheet", gp.scale);

        downIdle = setup("idling/player_down-idle-Sheet", gp.scale);
        upIdle = setup("idling/player_up-idle-Sheet", gp.scale);
        leftIdle = setup("idling/player_left-idle-Sheet", gp.scale);
        rightIdle = setup("idling/player_right-idle-Sheet", gp.scale);

        downAttacking = setup("attacking/player_down-slash-Sheet", gp.scale);
        upAttacking = setup("attacking/player_up-slash-Sheet", gp.scale);
        leftAttacking = setup("attacking/player_left-slash-Sheet", gp.scale);
        rightAttacking = setup("attacking/player_right-slash-Sheet", gp.scale);

        downParry = setup("blocking/player_down-shield-Sheet", gp.scale);
        upParry = setup("blocking/player_up-shield-Sheet", gp.scale);
        leftParry = setup("blocking/player_left-shield-Sheet", gp.scale);
        rightParry = setup("blocking/player_right-shield-Sheet", gp.scale);
    }

    /**
     * Utility method to load and properly scale sprite sheets.
     * * @param imageName The path string inside the "player" resource folder.
     * @param scale     The scale factor to apply.
     * @return The correctly scaled BufferedImage.
     */
    public BufferedImage setup(String imageName, int scale) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, image.getWidth() * scale, image.getHeight() * scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Resets the player's position, status, and health upon loading a game
     * or starting a new session.
     */
    public void resetPlayerValues() {
        worldCol = gp.mapsList.get(gp.currentMapIndex).playerCol;
        worldRow = gp.mapsList.get(gp.currentMapIndex).playerRow;
        worldX = gp.tileSize * worldCol;
        worldY = gp.tileSize * worldRow;
        speed = normalSpeed;
        health = 1;
        mana = maxMana;
        maxPotion = 3;
        potionNotUsed = maxPotion;
        endurance = maxEndurance;
        entityStatus = idle;
        direction = "down";
        invincible = false;
    }

    /**
     * Clears all items from the player's inventory and resets equipped slots.
     */
    public void resetInventory() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i].clear();
        }
        jEquip = null;
        kEquip = null;
        lEquip = null;
    }

    /**
     * The main logic loop for the player. Triggers movement, inputs, collisions,
     * and specific action logic depending on the current entity status.
     */
    public void update() {

        // SMOOTH GAUGE ANIMATION CALCULATIONS
        displayedHealth += (health - displayedHealth) * 0.15f;
        displayedEndurance += (endurance - displayedEndurance) * 0.15f;
        displayedMana += (mana - displayedMana) * 0.15f;

        if (damageTaken) {
            damageTakenCounter++;
            if (damageTakenCounter > damageTakenTimer) {
                damageTaken = false;
                damageTakenCounter = 0;
            }
        }

        // STATE: ATTACKING
        if (entityStatus == attacking) {
            attackCounter++;
            switch (direction) {
                case "up": upAttackingAnimator.update(); break;
                case "down": downAttackingAnimator.update(); break;
                case "left": leftAttackingAnimator.update(); break;
                case "right": rightAttackingAnimator.update(); break;
            }
            if (attackCounter >= attackDuration) {
                attackCounter = 0;
                entityStatus = idle;
            }
            return;
        }

        // STATE: KNOCKBACK (Pushed away after taking damage)
        if (entityStatus == knockBacking) {
            collisionOn = false;
            gp.collisionChecker.checkEntity(this, gp.monster);
            gp.collisionChecker.checkTile(this);

            // Stop knockback if hitting a wall
            if (collisionOn) {
                knockBackCounter = 0;
                entityStatus = idle;
                speed = normalSpeed;
            } else {
                // Apply forced movement
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            knockBackCounter++;
            if (knockBackCounter > knockBacTimer) {
                knockBackCounter = 0;
                if (name != null && name.equals("Rudeling")) {
                    entityStatus = parrying;
                } else {
                    entityStatus = walking;
                }
                speed = normalSpeed;
            }

            return;
        }

        // STATE: GRAPPLING (Using Hook)
        if (entityStatus == grappling) {
            switch (direction) {
                case "up": upIdleAnimator.update(); break;
                case "down": downIdleAnimator.update(); break;
                case "left": leftIdleAnimator.update(); break;
                case "right": rightIdleAnimator.update(); break;
            }
            if (!projectile.alive) {
                entityStatus = idle;
            }
            return;
        }

        // STATE: ROLLING (Dodge)
        if (entityStatus == rolling) {
            collisionOn = false;
            // Check collisions to prevent rolling through solid walls or entities
            gp.collisionChecker.checkTile(this);
            gp.collisionChecker.checkObject(this, true);
            gp.collisionChecker.checkEntity(this, gp.monster);

            useEndurance = true;
            rollCounter++;

            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        if (worldY - speed > 0) {
                            worldY -= speed;
                            // Camera panning logic
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2) && worldY < gp.worldHeight)) {
                                screenY -= speed;
                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                            upRollAnimator.update();
                        }
                        break;
                    case "down":
                        if ((worldY + gp.tileSize) + speed < gp.worldHeight) {
                            worldY += speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2))) {
                                screenY += speed;
                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                            downRollAnimator.update();
                        }
                        break;
                    case "left":
                        if (worldX - speed > 0) {
                            worldX -= speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX -= speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                            leftRollAnimator.update();
                        }
                        break;
                    case "right":
                        if (worldX + gp.tileSize + speed < gp.worldWidth) {
                            worldX += speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX += speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                            rightRollAnimator.update();
                        }
                        break;
                }
            } else {
                // Animate rolling even if blocked
                switch (direction) {
                    case "up": upRollAnimator.update(); break;
                    case "down": downRollAnimator.update(); break;
                    case "left": leftRollAnimator.update(); break;
                    case "right": rightRollAnimator.update(); break;
                }
            }

            // Handles I-frames (Invincibility Frames) during rolling
            if (invisibleCounter >= invisibleTimer) {
                invincible = false;
            } else {
                invincible = true;
                invisibleCounter++;
            }

            // Stop rolling
            if (rollCounter > rollDuration) {
                entityStatus = idle;
                rollCounter = 0;
                speed = normalSpeed;
                invisibleCounter = 0;
            }
            return;
        }

        // STAMINA REGENERATION DELAY
        if (useEndurance) {
            enduranceCounter++;
            if (enduranceCounter > enduranceDuration) {
                enduranceCounter = 0;
                useEndurance = false;
            }
        } else {
            rechargeEndurance(1);
        }

        // STATE: WALKING
        if (entityStatus == walking) {
            if (keyH.upPressed) {
                direction = "up";
                upAnimator.update();
            } else if (keyH.downPressed) {
                direction = "down";
                downAnimator.update();
            } else if (keyH.leftPressed) {
                direction = "left";
                leftAnimator.update();
            } else if (keyH.rightPressed) {
                direction = "right";
                rightAnimator.update();
            }

            // CHECK COLLISIONS
            collisionOn = false;
            gp.collisionChecker.checkTile(this);
            gp.collisionChecker.checkObject(this, true);
            gp.collisionChecker.checkEntity(this, gp.monster);
            gp.eventHandler.checkEvent();

            // MOVE IF NO COLLISION
            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        if (worldY - speed > 0) {
                            worldY -= speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2) && worldY < gp.worldHeight)) {
                                screenY -= speed;
                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                    case "down":
                        if ((worldY + gp.tileSize) + speed < gp.worldHeight) {
                            worldY += speed;
                            if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2))) {
                                screenY += speed;
                            } else {
                                screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                    case "left":
                        if (worldX - speed > 0) {
                            worldX -= speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX -= speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                    case "right":
                        if (worldX + gp.tileSize + speed < gp.worldWidth) {
                            worldX += speed;
                            if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                screenX += speed;
                            } else {
                                screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                            }
                        }
                        break;
                }
            }
        }

        // STATE: IDLE
        if (entityStatus == idle) {
            gp.eventHandler.checkEvent();
            switch (direction) {
                case "up": upIdleAnimator.update(); break;
                case "down": downIdleAnimator.update(); break;
                case "left": leftIdleAnimator.update(); break;
                case "right": rightIdleAnimator.update(); break;
            }
        }

        // STATE: PARRYING (Shielding)
        if (entityStatus == parrying) {
            useEndurance = true;

            // Allow slow movement while shielding
            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
                if (keyH.upPressed) {
                    direction = "up";
                    upParryAnimator.update();
                } else if (keyH.downPressed) {
                    direction = "down";
                    downParryAnimator.update();
                } else if (keyH.leftPressed) {
                    direction = "left";
                    leftParryAnimator.update();
                } else if (keyH.rightPressed) {
                    direction = "right";
                    rightParryAnimator.update();
                }

                collisionOn = false;
                gp.collisionChecker.checkTile(this);
                gp.collisionChecker.checkObject(this, true);
                gp.collisionChecker.checkEntity(this, gp.monster);
                gp.eventHandler.checkEvent();

                if (!collisionOn) {
                    switch (direction) {
                        case "up":
                            if (worldY - speed > 0) {
                                worldY -= speed;
                                if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2) && worldY < gp.worldHeight)) {
                                    screenY -= speed;
                                } else {
                                    screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                        case "down":
                            if ((worldY + gp.tileSize) + speed < gp.worldHeight) {
                                worldY += speed;
                                if ((worldY + (gp.tileSize / 2) < gp.screenHeight / 2) || (gp.worldHeight - (gp.screenHeight / 2) < worldY + (gp.tileSize / 2))) {
                                    screenY += speed;
                                } else {
                                    screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                        case "left":
                            if (worldX - speed > 0) {
                                worldX -= speed;
                                if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                    screenX -= speed;
                                } else {
                                    screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                        case "right":
                            if (worldX + gp.tileSize + speed < gp.worldWidth) {
                                worldX += speed;
                                if ((worldX + (gp.tileSize / 2) <= gp.screenWidth / 2) || (gp.worldWidth - (gp.screenWidth / 2) <= worldX + (gp.tileSize / 2))) {
                                    screenX += speed;
                                } else {
                                    screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
                                }
                            }
                            break;
                    }
                }
            }
        }

        // VERIFY OBJECT INTERACTION COLLISION
        int objIndex = gp.collisionChecker.checkObject(this, true);

        // TRANSITION TO WALKING STATE
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            entityStatus = walking;
        } else if (entityStatus != knockBacking) {
            entityStatus = idle;
        }

        // HANDLE SHIELD LOGIC
        if (hasObj("shield") && keyH.parryPressed) {
            if (endurance > 20) {
                entityStatus = parrying;
                speed = parryingSpeed;
                if (!parryKeyProcessed) {
                    gp.playSoundEffect(30);
                    parryKeyProcessed = true;
                }
            }
        } else if (entityStatus != rolling) {
            speed = normalSpeed;
            parryKeyProcessed = false;
        } else {
            parryKeyProcessed = false;
        }

        // HANDLE INVENTORY SHORTCUTS (J, K, L)
        if (jEquip != null && keyH.jEquipPressed) {
            if (!jEquipKeyProcessed) {
                boolean used = jEquip.use();
                if (used) { gp.playSoundEffect(jEquip.soundEffectIndex); }
                jEquipKeyProcessed = true;
                if (used && jEquip.objectType == jEquip.singleUse) {
                    inventory[jEquip.singleUse].remove(jEquip);
                }
            }
        } else { jEquipKeyProcessed = false; }

        if (kEquip != null && keyH.kEquipPressed) {
            if (!kEquipKeyProcessed) {
                boolean used = kEquip.use();
                if (used) { gp.playSoundEffect(kEquip.soundEffectIndex); }
                kEquipKeyProcessed = true;
                if (used && kEquip.objectType == kEquip.singleUse) {
                    inventory[kEquip.singleUse].remove(kEquip);
                }
            }
        } else { kEquipKeyProcessed = false; }

        if (lEquip != null && keyH.lEquipPressed) {
            if (!lEquipKeyProcessed) {
                boolean used = lEquip.use();
                if (used) { gp.playSoundEffect(lEquip.soundEffectIndex); }
                lEquipKeyProcessed = true;
                if (used && lEquip.objectType == lEquip.singleUse) {
                    inventory[lEquip.singleUse].remove(lEquip);
                }
            }
        } else { lEquipKeyProcessed = false; }

        // HANDLE POTION HEALING
        if (keyH.healPressed) {
            if (!healKeyProcessed) {
                usePotion();
                healKeyProcessed = true;
            }
        } else { healKeyProcessed = false; }

        // HANDLE GROUND OBJECT PICKUP
        if (keyH.interactionPressed) {
            if (!interactionKeyProcessed) {
                pickUpObject(objIndex);
                interactionKeyProcessed = true;
            }
        } else { interactionKeyProcessed = false; }

        // HANDLE DODGE ROLL INPUT (Space)
        if (keyH.spacePressed) {
            if (endurance > 0) {
                switch (direction) {
                    case "up": gp.playSoundEffect(12); break;
                    case "down": gp.playSoundEffect(11); break;
                    case "left": gp.playSoundEffect(13); break;
                    case "right": gp.playSoundEffect(14); break;
                }
                consumeEndurance(enduranceCost);
                entityStatus = rolling;
                speed = rollSpeed;
            }
        }

        // UPDATE GRID POSITION
        worldCol = worldX / gp.tileSize;
        worldRow = worldY / gp.tileSize;
        updateEquipment();
    }

    /**
     * Attempts to pick up an item off the ground, handling special
     * behaviors (like doors or keys).
     * * @param index The array list index of the object on the current map.
     */
    public void pickUpObject(int index) {
        if (index != 999) {
            gp.ui.itemOn = true;
            gp.ui.item = gp.obj.get(index);
            String objName = gp.obj.get(index).name;

            addObjToInventory(gp.obj.get(index));

            if (objName.equals("door")) {
                if (hasKey > 0) {
                    gp.obj.remove(index);
                    hasKey--;
                } else {
                    gp.playSoundEffect(2);
                    gp.ui.currentDialogue = "Doors is locked !";
                    gp.previousState = gp.gameState;
                    gp.gameState = gp.dialogueState;
                }
            } else if (objName.equals("chest")) {
                // Chest interaction code logic can go here
            } else {
                gp.playSoundEffect(2);
                gp.ui.currentDialogue = "You pick up a " + objName + " !";
                gp.previousState = gp.gameState;
                gp.gameState = gp.dialogueState;
                gp.obj.remove(index);
                gp.mapsList.get(gp.currentMapIndex).objectsList.remove(index);
                if (objName.equals("key")) {
                    hasKey++;
                }
            }
        }
    }

    /**
     * Render method called every frame to display the correct animation
     * based on the player's status and facing direction.
     * @param g2d The graphics component handler.
     */
    public void draw(Graphics2D g2d) {

//        if (dying){
//            dyingAnimation(g2d);
//        }
        // Blink effect when taking damage
        if (damageTaken) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        switch (direction) {
            case "up":
                if (entityStatus == rolling) {
                    upRollAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == walking || entityStatus == knockBacking) {
                    upAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == idle || entityStatus == grappling) {
                    upIdleAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == attacking) {
                    upAttackingAnimator.draw(g2d, screenX - gp.tileSize, screenY - gp.tileSize, gp.tileSize * 2, gp.tileSize * 2);
                } else if (entityStatus == parrying) {
                    upParryAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                }
                break;
            case "down":
                if (entityStatus == walking || entityStatus == knockBacking) {
                    downAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == rolling) {
                    downRollAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == idle || entityStatus == grappling) {
                    downIdleAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == attacking) {
                    downAttackingAnimator.draw(g2d, screenX, screenY, gp.tileSize * 2, gp.tileSize * 2);
                } else if (entityStatus == parrying) {
                    downParryAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                }
                break;
            case "left":
                if (entityStatus == rolling) {
                    leftRollAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == walking || entityStatus == knockBacking) {
                    leftAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == idle || entityStatus == grappling) {
                    leftIdleAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == attacking) {
                    leftAttackingAnimator.draw(g2d, screenX - gp.tileSize, screenY - gp.tileSize, gp.tileSize * 2, gp.tileSize * 2);
                } else if (entityStatus == parrying) {
                    leftParryAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                }
                break;
            case "right":
                if (entityStatus == rolling) {
                    rightRollAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == walking || entityStatus == knockBacking) {
                    rightAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == idle || entityStatus == grappling) {
                    rightIdleAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                } else if (entityStatus == attacking) {
                    rightAttackingAnimator.draw(g2d, screenX, screenY - gp.tileSize, gp.tileSize * 2, gp.tileSize * 2);
                } else if (entityStatus == parrying) {
                    rightParryAnimator.draw(g2d, screenX, screenY, gp.tileSize, gp.tileSize);
                }
                break;
        }

        // RESET OPACITY TO NORMAL
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    /**
     * Depletes a specific amount of endurance, preventing it from dropping below 0.
     * @param amount the stamina cost.
     */
    public void consumeEndurance(int amount) {
        if (endurance - amount > 0) {
            endurance -= amount;
        } else {
            endurance = 0;
        }
    }

    /**
     * Regenerates endurance gradually until it hits max limits.
     * @param amount the stamina gain.
     */
    public void rechargeEndurance(int amount) {
        if (endurance + amount < maxEndurance) {
            endurance += amount;
        } else {
            endurance = maxEndurance;
        }
    }

    public void consumeMana(int amount) {
        if (mana - amount > 0) {
            mana -= amount;
        } else {
            mana = 0;
        }
    }

    public void rechargeMana(int amount) {
        if (mana + amount < maxMana) {
            mana += amount;
        } else {
            mana = maxMana;
        }
    }

    /**
     * Helper method to visualize the physical hitbox and the weapon hitboxes.
     * Accessible by activating debug mode.
     * * @param g2 The graphics context.
     */
    public void showHitbox(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        if (invincible) {
            g2.setColor(Color.blue);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

        if (entityStatus == attacking) {
            g2.setColor(Color.GREEN);

            switch(direction) {
                case "up":
                    attackingArea = attackingAreaVertical;
                    attackingArea.y -= gp.tileSize;
                    break;
                case "down":
                    attackingArea = attackingAreaVertical;
                    attackingArea.y += gp.tileSize;
                    break;
                case "left":
                    attackingArea = attackingAreaHorizontal;
                    attackingArea.x -= gp.tileSize;
                    break;
                case "right":
                    attackingArea = attackingAreaHorizontal;
                    attackingArea.x += gp.tileSize;
                    break;
            }

            g2.drawRect(screenX + attackingArea.x, screenY + attackingArea.y, attackingArea.width, attackingArea.height);

            // Reset offset logic
            attackingAreaHorizontal.x = attackingAreaDefaultHX;
            attackingAreaHorizontal.y = attackingAreaDefaultHY;
            attackingAreaVertical.x = attackingAreaDefaultVX;
            attackingAreaVertical.y = attackingAreaDefaultVY;
        }
    }

    /**
     * Performs a melee attack, checking collisions with enemies, applying knockback,
     * and cutting grass/bushes if they are in the hitbox.
     */
    public void attack() {
        cutBush(gp.collisionChecker.checkCanCut(this));
        UtilityTool uTool = new UtilityTool();

        upAttackingAnimator.resetAnimation();
        downAttackingAnimator.resetAnimation();
        leftAttackingAnimator.resetAnimation();
        rightAttackingAnimator.resetAnimation();

        entityStatus = attacking;
        int monsterHit = 0;

        for (Entity e : gp.monster) {
            if (e != null) {
                hitOn = false;
                gp.collisionChecker.checkAttack(this, e);

                if (hitOn) {
                    // Check if the enemy parries the player's hit
                    if (e.entityStatus == parrying && e.direction.equals(uTool.oppositeDirection(direction))) {
                        e.takeDamage(0);
                        gp.playSoundEffect(16);
                    } else {
                        e.takeDamage(attackPower);
                        monsterHit++;
                    }
                    knockBack(e, 10);
                }
            }
        }

        if (monsterHit > 0) {
            gp.playSoundEffect(8);
        }
        hitOn = false;
    }

    /**
     * Converts specific breakable tiles (like bushes) into standard ground
     * after being slashed by a weapon.
     * * @param bushes The list of tile coordinates to modify.
     */
    public void cutBush(List<Point> bushes) {
        if (!bushes.isEmpty()) {
            for (Point p : bushes) {
                // Change tile ID at the point to grass (ID 16 in your tile sheet)
                gp.tileM.currentMap.tileMap[p.y][p.x][0] = 16;
            }
        }
    }

    /**
     * Safely pushes a SuperObject inside the correct player inventory array limit.
     * Also auto-equips the item if an empty shortcut slot is available.
     * * @param obj The item to add.
     */
    public void addObjToInventory(SuperObject obj) {
        if (obj != null && obj.objectType != obj.props) {
            switch (obj.objectType) {
                case 0:
                    if (inventory[0].size() < gearSize) {
                        inventory[obj.objectType].add(obj);
                    }
                    break;
                case 1:
                    if (inventory[1].size() < singleUseSize) {
                        inventory[obj.objectType].add(obj);
                    }
                    break;
                case 2:
                    if (inventory[2].size() < equipmentSize) {
                        inventory[obj.objectType].add(obj);
                        // Auto Assign to first free quick-slot (J, K, L)
                        if (jEquip == null) {
                            jEquip = obj;
                        } else if (kEquip == null) {
                            kEquip = obj;
                        } else if (lEquip == null) {
                            lEquip = obj;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Helper to insert starting gears.
     * (Currently commented out; used for debugging).
     */
    public void setInventory() {
        // addObjToInventory(new OBJ_Sword(gp));
        // addObjToInventory(new OBJ_Shield(gp));
        // etc...
    }

    /**
     * Loops through the entire inventory and asks every object to reload
     * its internal images/scalings.
     */
    public void reloadInventory() {
        for (ArrayList<SuperObject> type : inventory) {
            for (SuperObject obj : type) {
                obj.reload();
            }
        }
    }

    /**
     * Scans the 3 arrays of the inventory to check if the player possesses a specific item.
     * * @param objName Name of the item.
     * @return True if found, False otherwise.
     */
    public boolean hasObj(String objName) {
        for (ArrayList<SuperObject> type : inventory) {
            for (SuperObject obj : type) {
                if (obj.name.equals(objName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retrieve a specific object from the UI inventory grid system using row/col map coordinates.
     * * @param slotRow The UI Row number.
     * @param slotCol The UI Col number.
     * @return The target SuperObject, or null if the slot is empty.
     */
    public SuperObject getObjInInventory(int slotRow, int slotCol) {
        SuperObject obj = null;
        if (slotRow == 0) {
            if (!inventory[0].isEmpty() && slotCol < inventory[0].size()) {
                obj = inventory[0].get(slotCol);
            }
        } else if (slotRow == 1) {
            if (!inventory[1].isEmpty() && slotCol < inventory[1].size()) {
                obj = inventory[1].get(slotCol);
            }
        } else if (slotRow == 2) {
            if (!inventory[1].isEmpty() && ((singleUseSize / 2) + slotCol) < inventory[1].size()) {
                obj = inventory[1].get((singleUseSize / 2) + slotCol);
            }
        } else if (slotRow == 3) {
            if (!inventory[2].isEmpty() && slotCol < inventory[2].size()) {
                obj = inventory[2].get(slotCol);
            }
        }
        return obj;
    }

    /**
     * Triggers the healing action, checking potion limits and playing the matching SFX.
     */
    public void usePotion() {
        if (potionNotUsed > 0 && health < maxHealth) {
            gp.playSoundEffect(29);
            potionNotUsed--;
            heal(40);
        }
    }

    /**
     * Clears equipment shortcut memory slots if the actual item is dropped or lost.
     */
    public void updateEquipment() {
        if (jEquip != null && !hasObj(jEquip.name)) {
            jEquip = null;
        }
        if (kEquip != null && !hasObj(kEquip.name)) {
            kEquip = null;
        }
        if (lEquip != null && !hasObj(lEquip.name)) {
            lEquip = null;
        }
    }

    public void dyingAnimation(Graphics2D g) {
        gp.ui.startTransition(UI.TransitionType.Iris,80,()->{
            System.out.println("Transition mid");
        });
    }
}