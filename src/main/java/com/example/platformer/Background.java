package com.example.platformer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background extends Canvas {

    public Background(Image image) {
        super(1280,720);
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, 1280, 720);
    }

/*
    public Background getBackground(Image image) {
        return new Background(image);
    }
*/

}
