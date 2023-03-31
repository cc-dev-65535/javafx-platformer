package com.example.platformer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.awt.*;

public class UI extends Canvas {

    public UI(Image healthImage) {
        super(1280,720);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.drawImage(healthImage, 20, 20, 50, 50);
        gc.drawImage(healthImage, 80, 20, 50, 50);
        gc.drawImage(healthImage, 140, 20, 50, 50);
    }

}
