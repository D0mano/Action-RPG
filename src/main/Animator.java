package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Animator {
    public BufferedImage spriteSheet;
    public BufferedImage[] sprites;
    public BufferedImage sprite;
    public int index;
    public int counter = 0;
    public int animationSpeed;
    public int x, y;
    public int spriteWidth;
    public int spriteHeight;
    public boolean repeatAnimation = true;

    public Animator(String spritSheetPath ,int x, int y,int spriteWidth,int spriteHeight,boolean repeatAnimation)  {
        sprites = new BufferedImage[4];
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.x = x;
        this.y = y;
        setSprites(spritSheetPath);
        this.repeatAnimation = repeatAnimation;
        animationSpeed = 12;
        System.out.println(sprites.length);

    }

    public void loadSpriteSheet(String path) {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(path));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSprites(String path) {
        loadSpriteSheet(path);

        int imageWidth = spriteSheet.getWidth();
        int imageHeight = spriteSheet.getHeight();
        int index = 0;
        int nbcol = imageWidth / spriteWidth;
        int nbrow = imageHeight / spriteHeight;
        for (int i = 0; i < nbrow; i++) {
            for (int j = 0; j < nbcol; j++) {
                BufferedImage image = spriteSheet.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
                sprites[index] = image;
                index++;


            }
        }
        sprite = sprites[this.index];
    }

    public void update(int x,int y) {

        this.x = x;
        this.y = y;
        if (sprites != null){
            counter++;
            if (counter >= animationSpeed) {
                counter = 0;
                index++;
            }
            if (index >= sprites.length) {
                if(repeatAnimation) {
                    index = 0;
                }else{}

            }
            else{
                sprite = sprites[index];
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(sprite,x,y,spriteWidth*3,spriteHeight*3,null);
    }
}


