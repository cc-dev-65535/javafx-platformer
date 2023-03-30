package com.example.platformer;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Sprite extends Canvas {
    private Image image;
    //private double positionX;
    //private double positionY;
    private Point2D playerVelocity;
    private double width;
    private double height;

    public Sprite(double width, double height, Image image, int positionX, int positionY) {
        super(60, 60);
        GraphicsContext gc = this.getGraphicsContext2D();
        this.setTranslateX(positionX*60);
        this.setTranslateY(positionY*60);
        gc.setFill(Color.BLACK);
        gc.drawImage(image, 0, 0, 60, 60);
        playerVelocity = new Point2D(0,0);
    }

    public Point2D getPlayerVelocity() {
        return playerVelocity;
    }

    public void setPlayerVelocity(Point2D playerVelocity) {
        this.playerVelocity = playerVelocity;
    }

    //    public void update(double time) {
//        positionX += velocityX * time;
//        positionY += velocityY * time;
//    }

//    public void render(GraphicsContext gc) {
//        gc.drawImage( image, positionX, positionY );
//    }
//
//    public Rectangle2D getBoundary() {
//        return new Rectangle2D(positionX,positionY,width,height);
//    }
//
//    public boolean intersects(Sprite s) {
//        return s.getBoundary().intersects( this.getBoundary() );
//    }
}
