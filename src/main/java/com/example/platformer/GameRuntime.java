package com.example.platformer;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class GameRuntime extends Application {

    private Pane sceneRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane userInterfaceRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0,0);
    private ArrayList<Node> platforms = new ArrayList<Node>();

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private int levelWidth;
    private boolean canJump = true;

    private void initScene() {
        Rectangle background = new Rectangle(1280, 720);

        levelWidth = Area.LEVEL[0].length() * 60;

        for (int i = 0; i < Area.LEVEL.length; i++) {
            for (int j = 0; j < Area.LEVEL[i].length(); j++) {
                if (Area.LEVEL[i].charAt(j) == '0') {
                    continue;
                }
                if (Area.LEVEL[i].charAt(j) == '#') {
                    Node platform = new Rectangle(60, 60);
                    platform.setTranslateX(j*60);
                    platform.setTranslateY(i*60);
                    ((Rectangle) platform).setFill(Color.BLUE);

                    gameRoot.getChildren().add(platform);

                    platforms.add(platform);
                }
            }
        }

        player = new Rectangle(60, 60);
        player.setTranslateX(0);
        player.setTranslateY(600);
        ((Rectangle) player).setFill(Color.RED);

        gameRoot.getChildren().add(player);

        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        sceneRoot.getChildren().addAll(background, gameRoot, userInterfaceRoot);
    }

    private void update() {
        if (keys.getOrDefault(KeyCode.W, false) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }
        if (keys.getOrDefault(KeyCode.A, false) && player.getTranslateY() >= 5) {
            movePlayerX(-5);
        }
        if (keys.getOrDefault(KeyCode.D, false) && player.getTranslateY() >= 5) {
            movePlayerX(5);
        }
        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0,1);
        }
        movePlayerY((int)playerVelocity.getY());
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if(player.getTranslateX() + 60 == platform.getTranslateX()) {
                            player.setTranslateX(player.getTranslateX() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateX() == platform.getTranslateX() + 80){
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : - 1));
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if(player.getTranslateY() + 60 == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateY() == platform.getTranslateY() + 80){
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : - 1));
        }
    }

    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0,-30);
            canJump = false;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(GameRuntime.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //stage.setTitle("Hello!");
        //stage.setScene(scene);
        //stage.show();

        initScene();
        Scene scene = new Scene(sceneRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
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