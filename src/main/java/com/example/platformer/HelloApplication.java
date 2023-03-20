package com.example.platformer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.HashMap;

public class HelloApplication extends Application {
    private HashMap<KeyCode, Boolean> keyPressMap = new HashMap<>();
    private Pane sceneRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane userInterfaceRoot = new Pane();

    private Node player;
    private Node platform;

    private void initScene() {
        Rectangle background = new Rectangle(1280, 720);

        for (int i = 0; i < Area.LEVEL.length; i++) {
            for (int j = 0; j < Area.LEVEL[i].length(); j++) {
                if (Area.LEVEL[i].charAt(j) == '0') {
                    continue;
                }
                if (Area.LEVEL[i].charAt(j) == '#') {
                    platform = new Rectangle(60, 60);
                    platform.setTranslateX(j * 60);
                    platform.setTranslateY(i * 60);
                    ((Rectangle) platform).setFill(Color.BLUE);

                    gameRoot.getChildren().add(platform);
                }
            }
        }

        player = new Rectangle(60, 60);
        player.setTranslateX(0);
        player.setTranslateY(600);
        ((Rectangle) player).setFill(Color.RED);

        gameRoot.getChildren().add(player);

        sceneRoot.getChildren().addAll(background, gameRoot, userInterfaceRoot);
    }

    private void update() {
        // TODO: Handle key presses in key map data structure

    }
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //stage.setTitle("Hello!");
        //stage.setScene(scene);
        //stage.show();
        // TODO: Create static user interface in ui pane
        Canvas canvas = new Canvas( 1280, 720 );
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        Font theFont = Font.font("Times New Roman", 32 );
        gc.setFont(theFont);
        gc.fillText("Game", 60, 50);
        gc.strokeText("Game", 60, 50);


        initScene();
        Scene scene = new Scene(sceneRoot);
        // TODO: set up handlers for key presses and releases
        //scene.setOnKeyPressed();
        //scene.setOnKeyReleased();
        stage.setTitle("Title Here");
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                update();
            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}