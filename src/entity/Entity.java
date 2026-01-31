package entity;

import main.Animator;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public GamePanel gp;

    // Entity Attributes
    public String name;
    public int worldX, worldY;
    public int speed;
    public int normalSpeed;
    public String direction;
    public int maxHealth;
    public int health;
    public int maxMana;
    public int mana;
    public int attackPower;
    public Projectile projectile;
    public int useCost;
    public int maxPotion;
    public int potionNotUsed;


    // Animation
    public Animator downAnimator, upAnimator, leftAnimator, rightAnimator;
    public Animator downAttackingAnimator,upAttackingAnimator,leftAttackingAnimator,rightAttackingAnimator;

    // Image Animation
    public BufferedImage up, down, left, right;
    public BufferedImage downAttacking,upAttacking,leftAttacking,rightAttacking;



    // Hit Boxes
    public Rectangle solidArea;
    public int solideAreaDefaultX, solideAreaDefaultY;

    public Rectangle attackingArea = new Rectangle(0,0,0,0);
    public int attackingAreaDefaultHX, attackingAreaDefaultHY,attackingAreaDefaultVX, attackingAreaDefaultVY;
    public Rectangle attackingAreaHorizontal = new Rectangle(0,0,0,0);
    public Rectangle attackingAreaVertical = new Rectangle(0,0,0,0);

    public int deathSoundIndex;

    // CHARACTER STATUS
    public int entityStatus;
    final public int idle = 0;
    final public int walking = 1;
    final public int rolling = 2;
    final public int attacking = 3;
    final public int knockBacking = 4;
    final public int parrying = 5;




    // CHARACTER STATE
    public boolean alive = true;
    public boolean dying = false;
    public boolean invisible = false;
    public boolean damageTaken = false;
    public boolean hitOn = false;
    public boolean collisionOn = false;
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean canAttack = true;



    // COUNTER
    public int dyingCounter = 0;
    public int actionLockCounter = 0;
    public int invisibleCounter = 0;
    public int damageTakenCounter = 0;
    public int hpBarCounter = 0;
    public int attackCounter = 0;
    public int canAttackCounter = 0;
    public int knockBackCounter = 0;



    // TIMER
    public int actionLockTimer = 120;
    public int invisibleTimer;
    public int damageTakenTimer ;
    public int hpBarTimer = 600;
    public int attackDuration = 40;
    public int attackCooldownTimer = 90;
    public int knockBacTimer = 10;

    public Entity(GamePanel gp) {
        this.gp = gp;
        direction = "down";
        entityStatus = walking;


    }

    public void setAction() {
    }

    public void update() {

        if (damageTaken) {
            damageTakenCounter++;
            if (damageTakenCounter > damageTakenTimer) {
                damageTaken = false;
                damageTakenCounter = 0;
            }
        }

        if (entityStatus == knockBacking){
            collisionOn = false;
            gp.collisionChecker.checkEntity(this,gp.monster);
            gp.collisionChecker.checkTile(this);
            gp.collisionChecker.checkPlayer(this);
            if (collisionOn) {
                knockBackCounter = 0;
                if (name.equals("Rudeling")) {
                    entityStatus = parrying;
                }else{
                    entityStatus = walking;
                }
                speed =normalSpeed;
            }else{
                switch (direction) {
                    case "up": worldY -= speed;break;
                    case "down": worldY += speed;break;
                    case "left": worldX -= speed;break;
                    case "right": worldX += speed;break;
                }
            }
            knockBackCounter++;
            if (knockBackCounter > knockBacTimer){
                knockBackCounter = 0;
                if (name.equals("Rudeling")) {
                    entityStatus = parrying;
                }else{
                    entityStatus = walking;
                }
                speed =normalSpeed;
            }

        }
        if (entityStatus == walking || entityStatus == parrying) {
            setAction();
        }


        if (entityStatus == attacking) {
            canAttack = false;

            attackCounter++;

            switch (direction) {
                case "up":
                    upAttackingAnimator.update();
                    break;
                case "down":
                    downAttackingAnimator.update();
                    break;
                case "left":
                    leftAttackingAnimator.update();
                    break;
                case "right":
                    rightAttackingAnimator.update();
                    break;
            }
            if (attackCounter >= attackDuration) {
                attackCounter = 0;
                if (name.equals("Rudeling")) {
                    entityStatus = parrying;
                }else{
                    entityStatus = walking;
                }
            }

            return;
        }
        if (!canAttack){
            canAttackCounter++;
            if (canAttackCounter > attackCooldownTimer){
                canAttack = true;
                canAttackCounter = 0;
            }

        }
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);
        gp.collisionChecker.checkEntity(this ,gp.monster);

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
                upAnimator.resetAnimation();
                downAnimator.resetAnimation();
                leftAnimator.resetAnimation();
                rightAnimator.resetAnimation();
            }

        }

    }


    public void draw(Graphics2D g) {

        if (damageTaken){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        if (dying){
            dyingAnimation(g);
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (((-gp.tileSize) <= screenX && screenX <= (gp.worldWidth + gp.tileSize)) &&
                ((-gp.tileSize) <= screenY && screenY <= (gp.worldHeight + gp.tileSize))) {

            switch (direction) {
                case "up":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        upAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    }else if (entityStatus == attacking) {
                        upAttackingAnimator.draw(g,screenX, screenY-gp.tileSize, gp.tileSize, 2*gp.tileSize);
                    }
                    break;
                case "down":
                    if (entityStatus == walking|| entityStatus == knockBacking) {
                        downAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    }else if (entityStatus == attacking) {
                        downAttackingAnimator.draw(g,screenX, screenY, gp.tileSize, 2*gp.tileSize);
                    }
                    break;
                case "left":
                    if (entityStatus == walking || entityStatus == knockBacking) {
                        leftAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    }else if (entityStatus == attacking) {
                        leftAttackingAnimator.draw(g,screenX-gp.tileSize, screenY, 2*gp.tileSize, gp.tileSize);
                    }
                    break;
                case "right":
                    if (entityStatus == walking || entityStatus == knockBacking)  {
                        rightAnimator.draw(g, screenX, screenY, gp.tileSize, gp.tileSize);
                    }else if (entityStatus == attacking) {
                        rightAttackingAnimator.draw(g,screenX, screenY, 2*gp.tileSize, gp.tileSize);
                    }
                    break;


            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            if(hpBarOn){
                g.setColor(Color.black);
                g.fillRect(screenX-1, screenY-16, gp.tileSize+2, 7);
                g.setColor(new Color(250, 110, 150));
                g.fillRect(screenX, screenY-15, (int)(gp.tileSize* ((float)health/(float)maxHealth)), 5);
                hpBarCounter++;

                if (hpBarCounter > hpBarTimer) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }

            }



            if (gp.debugMode) {
                g.setColor(Color.RED);
                g.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
                if (entityStatus == attacking) {

                    g.setColor(Color.GREEN);

                    switch(direction){
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

                    g.drawRect(screenX+attackingArea.x,screenY+attackingArea.y,attackingArea.width,attackingArea.height);
                    attackingAreaHorizontal.x = attackingAreaDefaultHX;
                    attackingAreaHorizontal.y = attackingAreaDefaultHY;
                    attackingAreaVertical.x = attackingAreaDefaultVX;
                    attackingAreaVertical.y = attackingAreaDefaultVY;


                }
            }


        }

    }

    public void dyingAnimation(Graphics2D g) {
        dyingCounter++;
        if (dyingCounter <= 5){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }else if (dyingCounter <= 10){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }else if (dyingCounter <= 15){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }else if (dyingCounter <= 20){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        }else if (dyingCounter <= 25){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }else if (dyingCounter <= 30){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }else if (dyingCounter <= 35){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }else if (dyingCounter <= 40){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }else{
            dying = false;
            alive = false;
            gp.player.rechargeMana(20);

        }

    }

    public void takeDamage(int damage){
        damageTaken = true;
        hpBarOn = true;
        if(health-damage > 0){
            health -= damage;
        }
        else{
            gp.playSoundEffect(deathSoundIndex);
            health=0;
            dying = true;

        }

    }

    public void heal(int heal){
        if (potionNotUsed > 0){
            if (health < maxHealth){
                gp.playSoundEffect(29);
                potionNotUsed--;
            }
            if(health+heal < maxHealth){
                health += heal;
            }else{
                health = maxHealth;}
        }

    }
    public void shotProjectile(){
        if (!projectile.alive && mana - projectile.useCost > 0){
            gp.playSoundEffect(33);
            projectile.set(worldX,worldY,direction,true,this);
            gp.projectileList.add(projectile);


        }

    }


    public void knockBack(Entity entity,int knockBackPower){
        entity.direction = direction;
        entity.speed += knockBackPower;
        entity.entityStatus = knockBacking;
    }
}



