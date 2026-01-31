package entity;

import main.GamePanel;

import java.awt.*;

public class Particle extends Entity{
    Entity generator;
    Color color;
    int xd,yd;
    int size;
    public Particle(GamePanel gp,Entity generator,Color color,int size,int speed,int maxLife,int xd,int yd) {
        super(gp);
        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.xd = xd;
        this.yd = yd;
        this.health = maxLife;
        worldX = generator.worldX;
        worldY = generator.worldY;
    }
}
