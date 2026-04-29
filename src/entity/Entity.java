package entity;

import main.Animator;
import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Entity class is the base/parent class for all moving characters in the game
 * (Player, Monsters, NPCs). It holds common attributes like coordinates, speed,
 * health, hitboxes, and handles the core state machine (walking, attacking, taking damage).
 */
public class Entity {
    public GamePanel gp;
    public UtilityTool uTool = new UtilityTool();

    // ENTITY ATTRIBUTES
    public String name;
    public int worldX, worldY;         // Position on the global map grid
    public int worldCol, worldRow;     // Grid coordinates
    public int speed;                  // Current movement speed
    public int normalSpeed;            // Default movement speed
    public String direction;           // Facing direction (up, down, left, right)

    // STATS
    public int maxHealth;
    public int health;
    public int maxMana;
    public int mana;
    public int attackPower;
    public int useCost;                // Mana/Stamina cost for actions
    public int maxPotion;
    public int potionNotUsed;
    public Projectile projectile;      // Default projectile tied to this entity

    // ANIMATORS (Handling Sprite frames)
    public Animator downAnimator, upAnimator, leftAnimator, rightAnimator;
    public Animator downAttackingAnimator, upAttackingAnimator, leftAttackingAnimator, rightAttackingAnimator;

    // RAW SPRITE SHEETS
    public BufferedImage up, down, left, right;
    public BufferedImage downAttacking, upAttacking, leftAttacking, rightAttacking;

    // HITBOXES (Physical collision & Attack areas)
    public Rectangle solidArea;
    public int solideAreaDefaultX, solideAreaDefaultY;

    public Rectangle attackingArea = new Rectangle(0, 0, 0, 0);
    public int attackingAreaDefaultHX, attackingAreaDefaultHY, attackingAreaDefaultVX, attackingAreaDefaultVY;
    public Rectangle attackingAreaHorizontal = new Rectangle(0, 0, 0, 0);
    public Rectangle attackingAreaVertical = new Rectangle(0, 0, 0, 0);

    public int deathSoundIndex;

    // CHARACTER STATUS (State Machine)
    public int entityStatus;
    final public int idle = 0;
    final public int walking = 1;
    final public int rolling = 2;
    final public int attacking = 3;
    final public int knockBacking = 4;
    final public int parrying = 5;
    final public int freezing = 6;
    final public int grappling = 7;
    final public int grabbed = 8; // When hooked/pulled by the player

    // BOOLEAN STATES (Flags)
    public boolean alive = true;
    public boolean dying = false;
    public boolean invincible = false;
    public boolean damageTaken = false;
    public boolean hitOn = false;        // True if an attack lands
    public boolean collisionOn = false;  // True if hitting a wall/obstacle
    public boolean hpBarOn = false;      // Toggles HP bar visibility
    public boolean onPath = false;       // Used for pathfinding AI
    public boolean canAttack = true;
    public boolean attackHitDealt = false; // Prevents multi-hits in a single swing

    // COUNTERS
    public int dyingCounter = 0;
    public int actionLockCounter = 0;    // AI behavior delay
    public int invisibleCounter = 0;
    public int damageTakenCounter = 0;
    public int hpBarCounter = 0;
    public int attackCounter = 0;
    public int canAttackCounter = 0;
    public int knockBackCounter = 0;
    public int freezingCounter = 0;

    // TIMERS (Durations for specific states)
    public int actionLockTimer = 120;
    public int invisibleTimer;
    public int damageTakenTimer = 15;
    public int hpBarTimer = 600;         // How long the HP bar stays visible (frames)
    public int attackDuration = 40;
    public int attackingHitFrame = 20;   // The exact frame the damage is applied
    public int attackCooldownTimer = 90;
    public int knockBacTimer = 10;
    public int freezingTimer;

    /**
     * Constructor for the generic Entity.
     * @param gp The GamePanel instance.
     */
    public Entity(GamePanel gp) {
        this.gp = gp;
        direction = "down";
        entityStatus = walking;
    }

    /**
     * Reloads graphic and scale-dependent variables.
     * (To be overridden by subclasses if needed).
     */
    public void reload() {
    }

    /**
     * Sets the AI behavior/actions for NPC or monsters.
     * Must be overridden by subclasses (e.g., specific monsters).
     */
    public void setAction() {
    }

    /**
     * The main update loop for the entity. Processes state transitions,
     * movement, attacks, AI calls, and collision logic.
     */
    public void update() {

        // HANDLE DAMAGE INVINCIBILITY FRAMES
        if (damageTaken) {
            damageTakenCounter++;
            if (damageTakenCounter > damageTakenTimer) {
                damageTaken = false;
                damageTakenCounter = 0;
            }
        }

        // STATE: FREEZING (Stunned by Ice Magic)
        if (entityStatus == freezing) {
            freezingCounter++;
            if (freezingCounter > freezingTimer) {
                freezingCounter = 0;
                // Specific behavior recovery
                if (name != null && name.equals("Rudeling")) {
                    entityStatus = parrying;
                } else {
                    entityStatus = walking;
                }
                return;
            }
        }

        // STATE: GRABBED (Pulled by the Grappling Hook)
        if (entityStatus == grabbed) {
            int targetCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int targetRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            Point forwardTile = gp.collisionChecker.findNextFreeTile(targetCol, targetRow, direction);
            int offset = gp.tileSize / 4; // Pull speed/step

            // Drag the entity toward the calculated tile, unless it reaches the destination
            switch (direction) {
                case "up":
                    if (forwardTile.y * gp.tileSize <= worldY - offset) {
                        worldY -= offset;
                    } else if (name != null && name.equals("Rudeling")) {
                        entityStatus = parrying;
                    } else {
                        entityStatus = walking;
                    }
                    break;
                case "down":
                    if (forwardTile.y * gp.tileSize >= (worldY+solidArea.y+solidArea.height) + offset) {
                        worldY += offset;
                    } else if (name != null && name.equals("Rudeling")) {
                        entityStatus = parrying;
                    } else {
                        entityStatus = walking;
                    }
                    break;
                case "left":
                    if (forwardTile.x * gp.tileSize <= worldX - offset) {
                        worldX -= offset;
                    } else if (name != null && name.equals("Rudeling")) {
                        entityStatus = parrying;
                    } else {
                        entityStatus = walking;
                    }
                    break;
                case "right":
                    if (forwardTile.x * gp.tileSize >= (worldX + solidArea.x + solidArea.width) + offset ) {
                        worldX += offset;
                    } else if (name != null && name.equals("Rudeling")) {
                        entityStatus = parrying;
                    } else {
                        entityStatus = walking;
                    }
                    break;
            }
            return;
        }

        // STATE: KNOCKBACK (Pushed away after taking damage)
        if (entityStatus == knockBacking) {
            collisionOn = false;
            gp.collisionChecker.checkEntity(this, gp.monster);
            gp.collisionChecker.checkTile(this);
            gp.collisionChecker.checkPlayer(this);

            // Stop knockback if hitting a wall
            if (collisionOn) {
                knockBackCounter = 0;
                if (name != null && name.equals("Rudeling")) {
                    entityStatus = parrying;
                } else {
                    entityStatus = walking;
                }
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
        }

        // Trigger AI updates if the entity is capable of doing so
        if (entityStatus == walking || entityStatus == parrying) {
            setAction();
        }

        // STATE: ATTACKING
        if (entityStatus == attacking) {
            canAttack = false;
            attackCounter++;

            switch (direction) {
                case "up": upAttackingAnimator.update(); break;
                case "down": downAttackingAnimator.update(); break;
                case "left": leftAttackingAnimator.update(); break;
                case "right": rightAttackingAnimator.update(); break;
            }

            // Deal damage precisely at the "hit frame" of the animation
            if (attackCounter == attackingHitFrame && !attackHitDealt) {
                dealDamage();
                attackHitDealt = true;
            }

            // End attack animation
            if (attackCounter >= attackDuration) {
                attackCounter = 0;
                attackHitDealt = false;
                if (name != null && name.equals("Rudeling")) {
                    entityStatus = parrying;
                } else {
                    entityStatus = walking;
                }
            }
            return; // Skip walking logic while attacking
        }

        // COOLDOWN FOR ATTACKING
        if (!canAttack) {
            canAttackCounter++;
            if (canAttackCounter > attackCooldownTimer) {
                canAttack = true;
                canAttackCounter = 0;
            }
        }

        // GENERAL MOVEMENT & COLLISION
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);
        gp.collisionChecker.checkEntity(this, gp.monster);

        if (entityStatus == walking || entityStatus == parrying) {
            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        upAnimator.update();
                        break;
                    case "down":
                        worldY += speed;
                        downAnimator.update();
                        break;
                    case "left":
                        worldX -= speed;
                        leftAnimator.update();
                        break;
                    case "right":
                        worldX += speed;
                        rightAnimator.update();
                        break;
                }
            } else {
                // Pause animation if walking into a wall
                upAnimator.resetAnimation();
                downAnimator.resetAnimation();
                leftAnimator.resetAnimation();
                rightAnimator.resetAnimation();
            }
        }

        // UPDATE GRID POSITION
        worldCol = worldX / gp.tileSize;
        worldRow = worldY / gp.tileSize;
    }

    /**
     * Renders the entity on the screen, managing camera offsets, status effects
     * (frozen, dying, damage blinking), and UI elements like the HP bar.
     * @param g The Graphics2D component.
     */
    public void draw(Graphics2D g) {

        // Blink effect when taking damage
        if (damageTaken) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        // Flicker effect when dying
        if (dying) {
            dyingAnimation(g);
        }

        // Calculate screen coordinates relative to the player's camera position
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // ONLY DRAW IF VISIBLE ON SCREEN (Performance optimization)
        if (((-gp.tileSize) <= screenX && screenX <= (gp.worldWidth + gp.tileSize)) &&
                ((-gp.tileSize) <= screenY && screenY <= (gp.worldHeight + gp.tileSize))) {

            switch (direction) {
                case "up":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        upAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == attacking) {
                        upAttackingAnimator.draw(g, screenX, screenY - gp.tileSize, gp.tileSize, 2 * gp.tileSize);
                    } else if (entityStatus == freezing) {
                        drawFreezeOverlay(g, upAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == grabbed) {
                        g.drawImage(upAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                    break;
                case "down":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        downAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == attacking) {
                        downAttackingAnimator.draw(g, screenX, screenY, gp.tileSize, 2 * gp.tileSize);
                    } else if (entityStatus == freezing) {
                        drawFreezeOverlay(g, downAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == grabbed) {
                        g.drawImage(downAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                    break;
                case "left":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        leftAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == attacking) {
                        leftAttackingAnimator.draw(g, screenX - gp.tileSize, screenY, 2 * gp.tileSize, gp.tileSize);
                    } else if (entityStatus == freezing) {
                        drawFreezeOverlay(g, leftAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == grabbed) {
                        g.drawImage(leftAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                    break;
                case "right":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        rightAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == attacking) {
                        rightAttackingAnimator.draw(g, screenX, screenY, 2 * gp.tileSize, gp.tileSize);
                    } else if (entityStatus == freezing) {
                        drawFreezeOverlay(g, rightAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize);
                    } else if (entityStatus == grabbed) {
                        g.drawImage(rightAnimator.currentsprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                    break;
            }

            // RESET OPACITY TO NORMAL
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // DRAW HP BAR
            if (hpBarOn) {
                // Background outline
                g.setColor(Color.black);
                g.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 7);

                // Actual health fill
                g.setColor(new Color(250, 110, 150));
                g.fillRect(screenX, screenY - 15, (int) (gp.tileSize * ((float) health / (float) maxHealth)), 5);

                // Hide bar after the timer expires
                hpBarCounter++;
                if (hpBarCounter > hpBarTimer) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            // DEBUG MODE : DRAW HITBOXES
            if (gp.debugMode) {
                g.setColor(Color.RED);
                g.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

                if (entityStatus == attacking) {
                    g.setColor(Color.GREEN);
                    switch (direction) {
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

                    g.drawRect(screenX + attackingArea.x, screenY + attackingArea.y, attackingArea.width, attackingArea.height);

                    // Reset attack offsets
                    attackingAreaHorizontal.x = attackingAreaDefaultHX;
                    attackingAreaHorizontal.y = attackingAreaDefaultHY;
                    attackingAreaVertical.x = attackingAreaDefaultVX;
                    attackingAreaVertical.y = attackingAreaDefaultVY;
                }
            }
        }
    }

    /**
     * Creates a blinking effect by rapidly switching the opacity between 0 and 1.
     * Called when the entity reaches 0 health.
     * @param g The Graphics2D component.
     */
    public void dyingAnimation(Graphics2D g) {
        dyingCounter++;
        if (dyingCounter <= 5) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= 10) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else if (dyingCounter <= 15) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= 20) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else if (dyingCounter <= 25) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= 30) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else if (dyingCounter <= 35) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= 40) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else {
            // End of dying sequence, mark entity as completely dead
            dying = false;
            alive = false;

            // Player reward on kill
            if (gp.player != null) {
                gp.player.rechargeMana(20);
            }
        }
    }

    /**
     * Reduces health, plays a sound if killed, and activates the HP bar.
     * @param damage The amount of health to subtract.
     */
    public void takeDamage(int damage) {
        damageTaken = true;
        hpBarOn = true;
        if (health - damage > 0) {
            health -= damage;
        } else {
            gp.playSoundEffect(deathSoundIndex);
            health = 0;
            dying = true;
            if (this == gp.player) {
                gp.music.stop();
            }

        }

    }

    /**
     * Restores health up to the entity's maximum capacity.
     * @param heal The amount of health to restore.
     */
    public void heal(int heal) {
        if (health + heal < maxHealth) {
            health += heal;
        } else {
            health = maxHealth;
        }
    }

    /**
     * Spawns and fires the entity's assigned projectile weapon.
     */
    public void shootProjectile() {
        if (!projectile.alive && mana - projectile.useCost > 0) {
            projectile.set(worldX, worldY, direction, true, this);
            gp.projectileList.add(projectile);
        }
    }

    /**
     * Pushes another entity backward.
     * @param entity The entity receiving the knockback.
     * @param knockBackPower The strength/speed of the push.
     */
    public void knockBack(Entity entity, int knockBackPower) {
        entity.direction = direction; // Forces the victim to move in the attacker's direction
        entity.speed += knockBackPower;
        entity.entityStatus = knockBacking;
    }

    /**
     * Draws a pulsing ice-block visual effect over the entity when frozen.
     * @param g The Graphics2D component.
     * @param image The current sprite of the entity to draw beneath the freeze effect.
     * @param x Screen X coordinate.
     * @param y Screen Y coordinate.
     * @param width Width of the overlay.
     * @param height Height of the overlay.
     */
    public void drawFreezeOverlay(Graphics2D g, BufferedImage image, int x, int y, int width, int height) {
        // First we draw the original image
        g.drawImage(image, x, y, width, height, null);

        Composite old = g.getComposite();

        // Add a pulsing blue overlay block
        float alpha = 0.35f + 0.1f * (float) Math.sin(System.currentTimeMillis() / 200.0);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setColor(new Color(100, 150, 255));
        g.fillRect(x, y, width, height);

        // Draw a solid icy border around it
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        g.setColor(new Color(180, 220, 255));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, width, height);

        g.setComposite(old); // Restore original graphics composite
    }

    /**
     * Defines attack behavior/logic.
     * Intended to be overridden by subclasses (e.g., monsters checking collision against the player).
     */
    public void dealDamage() {
    }
}