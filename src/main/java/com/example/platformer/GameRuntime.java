package com.example.platformer;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

public class GameRuntime extends Application {

    /* root panes for this game's scene graph */
    private Pane sceneRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane userInterfaceRoot = new Pane();

    /* player and platform representations in game */
    private Canvas background;
    private Sprite playerSprite;
    private ArrayList<Sprite> platforms = new ArrayList<Sprite>();
    private ArrayList<Sprite> coins = new ArrayList<Sprite>();
    private ArrayList<Sprite> uiHealth = new ArrayList<>();

    /* dictionary that holds currently pressed keys */
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    /* various game state variables and constants */
    private int levelWidth;
    private int levelHeight;
    private int healthValue = 3;
    private boolean canJump = true;
    private boolean running = true;
    private UI currentUI;

    private void initScene() {
        //Canvas background = new Rectangle(1280, 720);

        //Canvas background = setBackground(new Image("com/example/platformer/background/game-bg-fix.png"));
        background = new Background(new Image("com/example/platformer/background/game-bg-fix.png"));

        sceneRoot.setMaxSize(1280, 720);
        //userInterfaceRoot.setMaxSize(1280, 720);

        levelWidth = Area.LEVEL[0].length() * 60;
        levelHeight = Area.LEVEL.length * 60;

        for (int i = 0; i < Area.LEVEL.length; i++) {
            for (int j = 0; j < Area.LEVEL[i].length(); j++) {
                if (Area.LEVEL[i].charAt(j) == '0') {
                    continue;
                }
                if (Area.LEVEL[i].charAt(j) == '#') {
                    Sprite platformSprite = new Sprite(60, 60, new Image("com/example/platformer/tiles/Tiles/14.png"), j, i);
                    platforms.add(platformSprite);
                    gameRoot.getChildren().add(platformSprite);
                }
                if (Area.LEVEL[i].charAt(j) == '1') {
                    Sprite coinSprite = new Sprite(60,60, new Image("com/example/platformer/ui/coin.png"), j, i);
                    coinSprite.setImage(new Image("com/example/platformer/ui/coin.png"), 5, 5, 40, 40);
                    coins.add(coinSprite);
                    gameRoot.getChildren().add(coinSprite);
                }
            }
        }

        playerSprite = new Sprite(60, 60, new Image("com/example/platformer/girl/Idle (1).png"), 0, 10);
        gameRoot.getChildren().add(playerSprite);

        initScrollScreen(levelWidth, levelHeight, gameRoot);
        //gameRoot.setLayoutX(0);
        gameRoot.setLayoutY(-(levelHeight - 720));

        //UI
        //Sprite health1 = new Sprite(50, 50, new Image("com/example/platformer/ui/3d-heart.png"), 20, 20);
        Image health = new Image("com/example/platformer/ui/3d-heart.png");
        currentUI = new UI(health, healthValue);
        userInterfaceRoot.getChildren().add(currentUI);

        sceneRoot.getChildren().addAll(background, gameRoot, userInterfaceRoot);
    }

    private void initScrollScreen(int levelWidth, int levelHeight, Pane gameRoot) {
        playerSprite.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        playerSprite.translateYProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 360 && offset < levelHeight - 360) {
                gameRoot.setLayoutY(-(offset - 360));
            }
        });
    }

    private void update() {
        if (keys.getOrDefault(KeyCode.W, false) && playerSprite.getTranslateY() >= 5) {
            jumpPlayer();
        }
        if (keys.getOrDefault(KeyCode.A, false) && playerSprite.getTranslateY() >= 5) {
            playerSprite.setImage(new Image("com/example/platformer/girl/Idle (1) - left.png"), 0, 0, 60, 60);
            movePlayerX(-5);
        }
        if (keys.getOrDefault(KeyCode.D, false) && playerSprite.getTranslateY() >= 5) {
            playerSprite.setImage(new Image("com/example/platformer/girl/Idle (1).png"), 0, 0, 60, 60);
            movePlayerX(5);
        }
        if (playerSprite.getPlayerVelocity().getY() < 10) {
            playerSprite.setPlayerVelocity(playerSprite.getPlayerVelocity().add(0,1));
        }

        if (playerSprite.getTranslateY() > levelHeight) {
            if (running) {
                // make new ui with health subtracted
                healthValue--;
                userInterfaceRoot.getChildren().remove(currentUI);
                Image health = new Image("com/example/platformer/ui/3d-heart.png");
                currentUI = new UI(health, healthValue);
                userInterfaceRoot.getChildren().add(currentUI);
                running = false;
            } else {
                //make new character spawned
                gameRoot.getChildren().remove(playerSprite);
                playerSprite = new Sprite(60, 60, new Image("com/example/platformer/girl/Idle (1).png"), 0, 10);
                gameRoot.getChildren().add(playerSprite);
                initScrollScreen(levelWidth, levelHeight, gameRoot);
                gameRoot.setLayoutY(-(levelHeight - 720));
                gameRoot.setLayoutX(0);
                running = true;
            }
        }

/*        if (keys.getOrDefault(KeyCode.SPACE, false)) {
            playerSprite.setImage(new Image("com/example/platformer/boy/Idle (1).png"));
        }*/
        movePlayerY((int) (playerSprite.getPlayerVelocity().getY()));

/*        for (Sprite coin : coins) {
            if (playerSprite.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                coin.setActive(false);
            }
        }*/

        Iterator<Sprite> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Sprite nextCoin = coinIterator.next();
            if (playerSprite.getBoundsInParent().intersects(nextCoin.getBoundsInParent())) {
                nextCoin.setActive(false);
                gameRoot.getChildren().remove(nextCoin);
                coinIterator.remove();
            }
        }
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Sprite platform : platforms) {
                if (playerSprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (playerSprite.getTranslateX() + 60 == platform.getTranslateX()) {
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
        initScene();
        Scene scene = new Scene(sceneRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        stage.setTitle("Adventures In Wonderland");
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
