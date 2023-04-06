package com.example.platformer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class describes the Game Background.
 *
 * @author Collin Chan, Ediljohn Joson
 * @version 2023-04-05
 */
public class Background extends Canvas {

    /**
     * The width of the background canvas.
     */
    public static final int BACKGROUND_WIDTH = 1280;

    /**
     * The height of the background canvas.
     */
    public static final int BACKGROUND_HEIGHT = 720;

    /**
     * Constructs the background with an image.
     * @param image the background image to draw
     */
    public Background(final Image image) {
        super(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }
}
