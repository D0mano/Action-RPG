package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The Animator class handles the slicing of sprite sheets and manages 
 * frame-by-frame animation playback for entities and objects.
 */
public class Animator {

    // ANIMATION DATA
    public ArrayList<BufferedImage> sprites; // Stores all individual frames of the animation
    public BufferedImage currentsprite;      // The exact frame currently being displayed

    // COUNTERS & SETTINGS
    public int index;                        // Current frame index in the list
    public int counter = 0;                  // Frame tick counter
    public int animationSpeed;               // How many game ticks must pass before the next frame
    public boolean repeatAnimation = true;   // True for loops (walking), False for single actions (attacking)

    /**
     * Constructor for the Animator.
     * Automatically slices the provided sprite sheet into individual frames.
     * @param spriteSheet     The full image containing all animation frames.
     * @param spriteWidth     The width of a single frame.
     * @param spriteHeight    The height of a single frame.
     * @param speed           The speed of the animation (higher value = slower animation).
     * @param repeatAnimation Whether the animation should loop endlessly.
     */
    public Animator(BufferedImage spriteSheet, int spriteWidth, int spriteHeight, int speed, boolean repeatAnimation) {
        sprites = new ArrayList<>();
        loadSprites(spriteSheet, spriteWidth, spriteHeight);
        this.repeatAnimation = repeatAnimation;
        this.animationSpeed = speed;
    }

    /**
     * Reloads the animation sprites. Usually called when the screen scale 
     * or resolution changes to recreate properly sized images.
     * @param newSpriteSheet  The newly scaled sprite sheet.
     * @param newSpriteWidth  The newly scaled frame width.
     * @param newSpriteHeight The newly scaled frame height.
     */
    public void reload(BufferedImage newSpriteSheet, int newSpriteWidth, int newSpriteHeight) {
        loadSprites(newSpriteSheet, newSpriteWidth, newSpriteHeight);
    }

    /**
     * Slices a 2D sprite sheet into individual 1D frames based on the specified width and height.
     * @param spriteSheet  The source image to be cut.
     * @param spriteWidth  The width of a single frame cut.
     * @param spriteHeight The height of a single frame cut.
     */
    public void loadSprites(BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        // [FIX] Clear the old list before reloading to prevent memory leaks and infinite appending
        sprites.clear();

        int imageWidth = spriteSheet.getWidth();
        int imageHeight = spriteSheet.getHeight();

        // Calculate how many rows and columns fit in the sprite sheet
        int nbcol = imageWidth / spriteWidth;
        int nbrow = imageHeight / spriteHeight;

        // Extract each sub-image (frame) and add it to the animation list
        for (int i = 0; i < nbrow; i++) {
            for (int j = 0; j < nbcol; j++) {
                BufferedImage image = spriteSheet.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
                sprites.add(image);
            }
        }

        // Set the default displayed frame to the first one
        currentsprite = sprites.get(index);
    }

    /**
     * Updates the animation logic. Should be called every game tick.
     * Advances the frame index based on the animation speed and handles looping.
     */
    public void update() {
        // Safety check to avoid crashes if no sprites are loaded
        if (sprites.isEmpty()) {
            return;
        }

        counter++;

        // If enough ticks have passed, move to the next frame
        if (counter >= animationSpeed) {
            counter = 0;
            index++;
        }

        // If the animation reaches the end of the frames list
        if (index >= sprites.size()) {
            if (repeatAnimation) {
                // Loop back to the first frame
                index = 0;
                counter = 0;
            } else {
                // Stay locked on the final frame (useful for death or attack animations)
                return;
            }
        }

        // Update the image to be drawn this tick
        currentsprite = sprites.get(index);
    }

    /**
     * Renders the current active frame onto the screen.
     * @param g2d    The Graphics2D component drawing the screen.
     * @param x      The X screen coordinate.
     * @param y      The Y screen coordinate.
     * @param width  The final width to draw.
     * @param height The final height to draw.
     */
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        if (currentsprite != null) {
            g2d.drawImage(currentsprite, x, y, width, height, null);
        }
    }

    /**
     * Resets the animation back to the very first frame.
     * Essential for single-play animations (like sword swinging) to ensure
     * they start from the beginning every time the action is triggered.
     */
    public void resetAnimation() {
        index = 0;
        counter = 0; // Ensures the first frame lasts the correct amount of time
        if (!sprites.isEmpty()) {
            currentsprite = sprites.get(index);
        }
    }
}