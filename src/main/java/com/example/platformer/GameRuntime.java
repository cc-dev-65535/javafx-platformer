package com.example.platformer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
//import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class GameRuntime extends Application {

    /* root panes for this game's scene graph */
    private Pane sceneRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane userInterfaceRoot = new Pane();

    /* player and platform representations in game */
    private Sprite playerSprite;
    private ArrayList<Sprite> platforms = new ArrayList<Sprite>();

    /* dictionary that holds currently pressed keys */
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    /* various game state variables and constants */
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
//                    Node platform = new Rectangle(60, 60);
//                    platform.setTranslateX(j*60);
//                    platform.setTranslateY(i*60);
//                    ((Rectangle) platform).setFill(Color.BLUE);
//
//                    gameRoot.getChildren().add(platform);
//
//                    platforms.add(platform);
                    Sprite platformSprite = new Sprite(60, 60, new Image("com/example/platformer/tiles/Tiles/14.png"), j, i);
                    platforms.add(platformSprite);
                    gameRoot.getChildren().add(platformSprite);
                }
            }
        }

//        player = new Rectangle(60, 60);
//        player.setTranslateX(0);
//        player.setTranslateY(600);
//        ((Rectangle) player).setFill(Color.RED);
//
//        gameRoot.getChildren().add(player);
        playerSprite = new Sprite(60, 60, new Image("com/example/platformer/girl/Idle (1).png"), 0, 10);
        gameRoot.getChildren().add(playerSprite);

        playerSprite.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        sceneRoot.getChildren().addAll(background, gameRoot, userInterfaceRoot);
    }

    private void update() {
        if (keys.getOrDefault(KeyCode.W, false) && playerSprite.getTranslateY() >= 5) {
            jumpPlayer();
        }
        if (keys.getOrDefault(KeyCode.A, false) && playerSprite.getTranslateY() >= 5) {
            movePlayerX(-5);
        }
        if (keys.getOrDefault(KeyCode.D, false) && playerSprite.getTranslateY() >= 5) {
            movePlayerX(5);
        }
        if (playerSprite.getPlayerVelocity().getY() < 10) {
            playerSprite.setPlayerVelocity(playerSprite.getPlayerVelocity().add(0,1));
        }
        movePlayerY((int) (playerSprite.getPlayerVelocity().getY()));
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Sprite platform : platforms) {
                if (playerSprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (playerSprite.getTranslateX() + 60 == platform.getTranslateX()) {
                            playerSprite.setTranslateX(playerSprite.getTranslateX() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (playerSprite.getTranslateX() == platform.getTranslateX() + 80){
                            return;
                        }
                    }
                }
            }
            playerSprite.setTranslateX(playerSprite.getTranslateX() + (movingRight ? 1 : - 1));
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Sprite platform : platforms) {
                if (playerSprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (playerSprite.getTranslateY() + 60 == platform.getTranslateY()) {
                            playerSprite.setTranslateY(playerSprite.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (playerSprite.getTranslateY() == platform.getTranslateY() + 80){
                            return;
                        }
                    }
                }
            }
            playerSprite.setTranslateY(playerSprite.getTranslateY() + (movingDown ? 1 : - 1));
        }
    }

    private void jumpPlayer() {
        if (canJump) {
            playerSprite.setPlayerVelocity(playerSprite.getPlayerVelocity().add(0,-30));
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
