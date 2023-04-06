package com.example.platformer;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class describes a Game Sprite.
 *
 * @author Collin Chan, Ediljohn Joson
 * @version 2023-04-05
 */
public class Sprite extends Canvas {

    /**
     * The width of the sprite.
     */
    public static final int SPRITE_WIDTH = 60;

    /**
     * The height of the sprite.
     */
    public static final int SPRITE_HEIGHT = 60;

    /**
     * The X offset of the sprite image.
     */
    public static final int SPRITE_OFFSET_X = 0;

    /**
     * The Y offset of the sprite image.
     */
    public static final int SPRITE_OFFSET_Y = 0;


    private Image image;
    private Point2D playerVelocity;
    private boolean active;

    /**
     * Constructs a sprite.
     * @param width the width to set
     * @param height the height to set
     * @param image the image to set
     * @param positionX the X coordinate
     * @param positionY the Y coordinate
     */
    public Sprite(final double width, final double height, final Image image, final int positionX, final int positionY) {
        super(SPRITE_WIDTH, SPRITE_HEIGHT);
        GraphicsContext gc = this.getGraphicsContext2D();
        this.setTranslateX(positionX * SPRITE_WIDTH);
        this.setTranslateY(positionY * SPRITE_HEIGHT);
        gc.drawImage(image, SPRITE_OFFSET_X, SPRITE_OFFSET_Y, SPRITE_WIDTH, SPRITE_HEIGHT);
        playerVelocity = new Point2D(0, 0);
        active = true;
    }

    /**
     * Get the player velocity.
     * @return the player velocity
     */
    public Point2D getPlayerVelocity() {
        return playerVelocity;
    }

    /**
     * Set the player velocity.
     * @param playerVelocity the new player velocity
     */
    public void setPlayerVelocity(final Point2D playerVelocity) {
        this.playerVelocity = playerVelocity;
    }

    /**
     * Set a new sprite image specified parameters.
     * @param image the image to set
     * @param offsetX the x-coordinate
     * @param offsetY the y-coordinate
     * @param width the image width
     * @param height the image height
     */
    public void setImage(final Image image, final int offsetX, final int offsetY, final int width, final int height) {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        this.setTranslateX(this.getTranslateX());
        this.setTranslateY(this.getTranslateY());
        gc.drawImage(image, offsetX, offsetY, width, height);
    }

    /**
     * Set active variable.
     * @param active the active boolean to change
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

}
