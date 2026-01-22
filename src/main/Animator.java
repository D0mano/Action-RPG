package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animator {
    public ArrayList<BufferedImage> sprites;
    public BufferedImage currentsprite;
    public int index;
    public int counter = 0;
    public int animationSpeed;
    public boolean repeatAnimation = true;

    public Animator(BufferedImage spriteSheet ,int spriteWidth,int spriteHeight,int speed,boolean repeatAnimation)  {
        sprites = new ArrayList<>();
        loadSprites(spriteSheet,spriteWidth,spriteHeight);
        this.repeatAnimation = repeatAnimation;
        animationSpeed = speed;

    }

    public void loadSprites(BufferedImage spriteSheet ,int spriteWidth,int spriteHeight) {

        int imageWidth = spriteSheet.getWidth();
        int imageHeight = spriteSheet.getHeight();
        int nbcol = imageWidth / spriteWidth;
        int nbrow = imageHeight / spriteHeight;
        for (int i = 0; i < nbrow; i++) {
            for (int j = 0; j < nbcol; j++) {
                BufferedImage image = spriteSheet.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
                sprites.add(image);


            }
        }
        currentsprite = sprites.get(index);
    }

    public void update() {

        if (sprites.isEmpty()) {return;}

        counter++;
        if (counter >= animationSpeed) {
            counter = 0;
            index++;
        }
        if (index >= sprites.size()) {
            if(repeatAnimation) {
                index = 0;
                counter = 0;
            }else{

                return;
            }
        }
        currentsprite = sprites.get(index);
    }


    public void draw(Graphics2D g2d,int x,int y,int width,int height) {
        if (currentsprite != null){
            g2d.drawImage(currentsprite, x, y, width, height, null);
        }

    }

    public void resetAnimation(){
        index = 0;
    }

}




