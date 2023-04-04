package com.example.platformer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;

public class UI extends Canvas {

    public UI(Image healthImage, int pos) {
        super(1280,720);
        GraphicsContext gc = this.getGraphicsContext2D();
        switch (pos) {
            case 3:
                gc.drawImage(healthImage, 20, 20, 50, 50);
                gc.drawImage(healthImage, 80, 20, 50, 50);
                gc.drawImage(healthImage, 140, 20, 50, 50);
                break;
            case 2:
                gc.drawImage(healthImage, 20, 20, 50, 50);
                gc.drawImage(healthImage, 80, 20, 50, 50);
                break;
            case 1:
                gc.drawImage(healthImage, 20, 20, 50, 50);
                break;
            case 0:

        }
        gc.setFill(Color.rgb(255, 204, 0));
        gc.setFont(new Font("Impact", 50));
        gc.fillText("Coins Left: ", 1000, 60);
    }

/*    public UI(int coinsLeft) {
        this.getGraphicsContext2D().setFill(Color.rgb(255, 204, 0));
        this.getGraphicsContext2D().setFont(new Font("Impact", 50));
        this.getGraphicsContext2D().fillText("Coins Left " + coinsLeft, 1000, 60);
    }*/


}
