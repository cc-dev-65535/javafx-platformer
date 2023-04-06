package com.example.platformer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class describes the Game UI.
 *
 * @author Collin Chan, Ediljohn Joson
 * @version 2023-04-05
 */
public class UI extends Canvas {

    /**
     * The width of the game window.
     */
    public static final int WINDOW_WIDTH = 1280;

    /**
     * The height of the game window.
     */
    public static final int WINDOW_HEIGHT = 720;

    /**
     * The size of the health image.
     */
    public static final int HEALTH_IMG_SIZE = 50;

    /**
     * The Y coordinate of the health images.
     */
    public static final int HEALTH_Y_POS = 20;

    /**
     * The X coordinate of the first health image.
     */
    public static final int HEALTH1_X_POS = 20;

    /**
     * The X coordinate of the second health image.
     */
    public static final int HEALTH2_X_POS = 80;

    /**
     * The X coordinate of the third health image.
     */
    public static final int HEALTH3_X_POS = 140;

    /**
     * The X coordinate of the "Coins Left" text.
     */
    public static final int COINS_LEFT_X_POS = 970;

    /**
     * The Y coordinate of the "Coins Left" text.
     */
    public static final int COINS_LEFT_Y_POS = 60;

    /**
     * The font size of the "Coins Left" text.
     */
    public static final int COINS_LEFT_FONT_SIZE = 50;

    /**
     * The X coordinate of the ending screen image.
     */
    public static final int SCREEN_TYPE_X_POS = 300;

    /**
     * The Y coordinate of the ending screen image.
     */
    public static final int SCREEN_TYPE_Y_POS = 260;

    /**
     * The width of the ending screen image.
     */
    public static final int SCREEN_TYPE_WIDTH = 705;

    /**
     * The height of the ending screen image.
     */
    public static final int SCREEN_TYPE_HEIGHT = 110;


    /**
     * Construct UI with image and coin elements.
     * @param healthImage the image of health
     * @param pos the number of health to display
     * @param coinsLeft the amount of coins left
     */
    public UI(final Image healthImage, final int pos, final int coinsLeft) {
        super(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = this.getGraphicsContext2D();
        switch (pos) {
            case 3 -> {
                gc.drawImage(healthImage, HEALTH1_X_POS, HEALTH_Y_POS, HEALTH_IMG_SIZE, HEALTH_IMG_SIZE);
                gc.drawImage(healthImage, HEALTH2_X_POS, HEALTH_Y_POS, HEALTH_IMG_SIZE, HEALTH_IMG_SIZE);
                gc.drawImage(healthImage, HEALTH3_X_POS, HEALTH_Y_POS, HEALTH_IMG_SIZE, HEALTH_IMG_SIZE);
            }
            case 2 -> {
                gc.drawImage(healthImage, HEALTH1_X_POS, HEALTH_Y_POS, HEALTH_IMG_SIZE, HEALTH_IMG_SIZE);
                gc.drawImage(healthImage, HEALTH2_X_POS, HEALTH_Y_POS, HEALTH_IMG_SIZE, HEALTH_IMG_SIZE);
            }
            case 1 -> gc.drawImage(healthImage, HEALTH1_X_POS, HEALTH_Y_POS, HEALTH_IMG_SIZE, HEALTH_IMG_SIZE);
            default -> {
            }
        }
        gc.setFill(Color.rgb(255, 204, 0));
        gc.setFont(new Font("Impact", COINS_LEFT_FONT_SIZE));
        gc.fillText("Coins Left: " + coinsLeft, COINS_LEFT_X_POS, COINS_LEFT_Y_POS);
    }

    /**
     * Construct UI for an ending screen.
     * @param screenType the type of screen
     */
    public UI(final Image screenType) {
        super(WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.drawImage(screenType, SCREEN_TYPE_X_POS, SCREEN_TYPE_Y_POS, SCREEN_TYPE_WIDTH, SCREEN_TYPE_HEIGHT);
    }
}
