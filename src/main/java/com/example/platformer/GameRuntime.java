package com.example.platformer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class describes the Game Runtime.
 *
 * @author Collin Chan, Ediljohn Joson
 * @version 2023-04-05
 */
public class GameRuntime extends Application {

    /**
     * The width of the game screen.
     */
    public static final int SCREEN_WIDTH = 1280;

    /**
     * The height of the game screen.
     */
    public static final int SCREEN_HEIGHT = 720;

    /**
     * Half of the width of the game screen.
     */
    public static final int SCREEN_WIDTH_HALF = 640;

    /**
     * Half of the height of the game screen.
     */
    public static final int SCREEN_HEIGHT_HALF = 360;

    /**
     * The size of each sprite in the game area.
     */
    public static final int AREA_SPRITE_SIZE = 60;

    /**
     * The size of the player sprite.
     */
    public static final int PLAYER_SPRITE_SIZE = 60;

    /**
     * The y-offset of the player sprite.
     */
    public static final int PLAYER_SPRITE_Y_OFFSET = 10;

    /**
     * The size of each coin sprite.
     */
    public static final int COIN_SIZE = 40;

    /**
     * The distance between the coin sprite and the edge of the sprite.
     */
    public static final int COIN_OFFSET = 5;

    /**
     * The amount to offset the player sprite when moving horizontally.
     */
    public static final int MOVE_PLAYER_X_OFFSET = 5;

    /**
     * The amount to offset the player sprite vertically.
     */
    public static final int PLAYER_Y_BOUND_OFFSET = 5;

    /**
     * The amount of gravity to apply to the player sprite.
     */
    public static final int PLAYER_GRAVITY_OFFSET = 10;

    /**
     * The velocity of the player's jump.
     */
    public static final int PLAYER_JUMP_VELOCITY = 30;

    /**
     * The initial health of the player.
     */
    public static final int PLAYER_INITIAL_HEALTH = 3;

    /**
     * The amount of space between the player sprite and a platform before a collision is detected.
     */
    public static final int PLATFORM_COLLISION_OFFSET = 80;

    /**
     * The constant to multiply tile spaces.
     */
    public static final int PLATFORM_MULTIPLIER = 60;

    /* Root panes for this game's scene graph */
    private final Pane sceneRoot = new Pane();
    private final Pane gameRoot = new Pane();
    private final Pane userInterfaceRoot = new Pane();

    private Sprite playerSprite;
    private final ArrayList<Sprite> platforms = new ArrayList<>();
    private final ArrayList<Sprite> coins = new ArrayList<>();

    /* HashMap that holds currently pressed keys */
    private final HashMap<KeyCode, Boolean> keys = new HashMap<>();

    /* Game state variables */
    private int levelWidth;
    private int levelHeight;
    private int healthValue = PLAYER_INITIAL_HEALTH;
    private int coinsLeft;
    private boolean canJump = true;
    private boolean running = true;
    private boolean isAlive = true;
    private boolean endScreen = false;
    private boolean gameReset = false;
    private UI currentUI;
    private Instant gameTimerStart;

    /* Initialize the game scene */
    private void initScene() {
        // Start timer
        gameTimerStart = Instant.now();

        /* Player and Platform representations in game */
        Canvas background = new Background(new Image("com/example/platformer/background/game-bg-fix.png"));
        sceneRoot.setMaxSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        levelWidth = Area.LEVEL[0].length() * PLATFORM_MULTIPLIER;
        levelHeight = Area.LEVEL.length * PLATFORM_MULTIPLIER;

        // Scan the Area class LEVEL String
        for (int i = 0; i < Area.LEVEL.length; i++) {
            for (int j = 0; j < Area.LEVEL[i].length(); j++) {
                if (Area.LEVEL[i].charAt(j) == '0') {
                    continue;
                }
                // Create a platform for each '#'
                if (Area.LEVEL[i].charAt(j) == '#') {
                    Sprite platformSprite = new Sprite(AREA_SPRITE_SIZE, AREA_SPRITE_SIZE, new Image("com/example/platformer/tiles/Tiles/14.png"), j, i);
                    platforms.add(platformSprite);
                    gameRoot.getChildren().add(platformSprite);
                }
                // Create a left platform for each 'L'
                if (Area.LEVEL[i].charAt(j) == 'L') {
                    Sprite platformSprite = new Sprite(AREA_SPRITE_SIZE, AREA_SPRITE_SIZE, new Image("com/example/platformer/tiles/Tiles/13.png"), j, i);
                    platforms.add(platformSprite);
                    gameRoot.getChildren().add(platformSprite);
                }
                // Create a right platform for each 'R'
                if (Area.LEVEL[i].charAt(j) == 'R') {
                    Sprite platformSprite = new Sprite(AREA_SPRITE_SIZE, AREA_SPRITE_SIZE, new Image("com/example/platformer/tiles/Tiles/15.png"), j, i);
                    platforms.add(platformSprite);
                    gameRoot.getChildren().add(platformSprite);
                }
                // Create a coin for each '1'
                if (Area.LEVEL[i].charAt(j) == '1') {
                    Sprite coinSprite = new Sprite(AREA_SPRITE_SIZE, AREA_SPRITE_SIZE, new Image("com/example/platformer/ui/coin.png"), j, i);
                    coinSprite.setImage(new Image("com/example/platformer/ui/coin.png"), COIN_OFFSET, COIN_OFFSET, COIN_SIZE, COIN_SIZE);
                    coins.add(coinSprite);
                    gameRoot.getChildren().add(coinSprite);
                    coinsLeft++;
                }
            }
        }

        // Create the player
        playerSprite = new Sprite(PLAYER_SPRITE_SIZE, PLAYER_SPRITE_SIZE, new Image("com/example/platformer/girl/Idle (1).png"), 0, PLAYER_SPRITE_Y_OFFSET);
        gameRoot.getChildren().add(playerSprite);

        // Set up screen scrolling to follow player
        initScrollScreen(levelWidth, levelHeight, gameRoot);
        gameRoot.setLayoutY(-(levelHeight - SCREEN_HEIGHT));

        // Set up the UI for health and coins
        Image health = new Image("com/example/platformer/ui/3d-heart.png");
        currentUI = new UI(health, healthValue, coinsLeft);
        userInterfaceRoot.getChildren().add(currentUI);

        // Add elements to the scene root
        sceneRoot.getChildren().addAll(background, gameRoot, userInterfaceRoot);
    }

    /* Scroll the screen to follow the player */
    private void initScrollScreen(final int levelWidth, final int levelHeight, final Pane gameRoot) {
        playerSprite.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > SCREEN_WIDTH_HALF && offset < levelWidth - SCREEN_WIDTH_HALF) {
                gameRoot.setLayoutX(-(offset - SCREEN_WIDTH_HALF));
            }
        });

        playerSprite.translateYProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > SCREEN_HEIGHT_HALF && offset < levelHeight - SCREEN_HEIGHT_HALF) {
                gameRoot.setLayoutY(-(offset - SCREEN_HEIGHT_HALF));
            }
        });
    }

    /* Update the game during the main loop */
    private void update() {
        // Move the player based on key presses
        if (keys.getOrDefault(KeyCode.W, false) && playerSprite.getTranslateY() >= PLAYER_Y_BOUND_OFFSET) {
            jumpPlayer();
        }
        if (keys.getOrDefault(KeyCode.A, false) && playerSprite.getTranslateY() >= PLAYER_Y_BOUND_OFFSET) {
            playerSprite.setImage(new Image("com/example/platformer/girl/Idle (1) - left.png"), 0, 0, PLAYER_SPRITE_SIZE, PLAYER_SPRITE_SIZE);
            movePlayerX(-MOVE_PLAYER_X_OFFSET);
        }
        if (keys.getOrDefault(KeyCode.D, false) && playerSprite.getTranslateY() >= PLAYER_Y_BOUND_OFFSET) {
            playerSprite.setImage(new Image("com/example/platformer/girl/Idle (1).png"), 0, 0, PLAYER_SPRITE_SIZE, PLAYER_SPRITE_SIZE);
            movePlayerX(MOVE_PLAYER_X_OFFSET);
        }
        if (playerSprite.getPlayerVelocity().getY() < PLAYER_GRAVITY_OFFSET) {
            playerSprite.setPlayerVelocity(playerSprite.getPlayerVelocity().add(0, 1));
        }

        /* Reset the player when they fall off the platform */
        if (playerSprite.getTranslateY() > levelHeight) {
            if (running) {
                if (healthValue <= 1) {
                    isAlive = false;
                    endScreen = true;
                    gameReset = true;
                }
                // Make new ui with health subtracted
                healthValue--;
                userInterfaceRoot.getChildren().remove(currentUI);
                Image health = new Image("com/example/platformer/ui/3d-heart.png");
                currentUI = new UI(health, healthValue, coinsLeft);
                userInterfaceRoot.getChildren().add(currentUI);
                running = false;
            } else {
                spawnNewCharacter();
                running = true;
            }
        }

        movePlayerY((int) (playerSprite.getPlayerVelocity().getY()));

        // Remove coins when player intersects with them. Decrement the total coins left variable
        Iterator<Sprite> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Sprite nextCoin = coinIterator.next();
            if (playerSprite.getBoundsInParent().intersects(nextCoin.getBoundsInParent())) {
                //nextCoin.setActive(false);
                gameRoot.getChildren().remove(nextCoin);
                coinIterator.remove();

                coinsLeft--;

                // Update the UI to reflect coins collected
                userInterfaceRoot.getChildren().remove(currentUI);
                Image health = new Image("com/example/platformer/ui/3d-heart.png");
                currentUI = new UI(health, healthValue, coinsLeft);
                userInterfaceRoot.getChildren().add(currentUI);

                // End the game when all coins are collected
                if (coinsLeft <= 0) {
                    isAlive = false;
                    endScreen = true;
                    gameReset = true;
                }
            }
        }
    }

    /* spawn a new character in the game at initial position */
    private void spawnNewCharacter() {
        // Make new character spawned
        gameRoot.getChildren().remove(playerSprite);
        playerSprite = new Sprite(PLAYER_SPRITE_SIZE, PLAYER_SPRITE_SIZE, new Image("com/example/platformer/girl/Idle (1).png"), 0, PLAYER_SPRITE_Y_OFFSET);
        gameRoot.getChildren().add(playerSprite);
        initScrollScreen(levelWidth, levelHeight, gameRoot);
        gameRoot.setLayoutY(-(levelHeight - SCREEN_HEIGHT));
        gameRoot.setLayoutX(0);
    }

    /* Move the player across X axis */
    private void movePlayerX(final int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Sprite platform : platforms) {
                if (playerSprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (playerSprite.getTranslateX() + PLAYER_SPRITE_SIZE == platform.getTranslateX()) {
                            return;
                        }
                    } else {
                        if (playerSprite.getTranslateX() == platform.getTranslateX() + PLATFORM_COLLISION_OFFSET) return;
                    }
                }
            }
            playerSprite.setTranslateX(playerSprite.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    /* Move the player across Y axis */
    private void movePlayerY(final int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Sprite platform : platforms) {
                if (playerSprite.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (playerSprite.getTranslateY() + PLAYER_SPRITE_SIZE == platform.getTranslateY()) {
                            playerSprite.setTranslateY(playerSprite.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    } else {
                        if (playerSprite.getTranslateY() != platform.getTranslateY() + PLATFORM_COLLISION_OFFSET) {
                            continue;
                        }
                        return;
                    }
                }
            }
            playerSprite.setTranslateY(playerSprite.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    /* Jump the player */
    private void jumpPlayer() {
        if (canJump) {
            playerSprite.setPlayerVelocity(playerSprite.getPlayerVelocity().add(0, -PLAYER_JUMP_VELOCITY));
            canJump = false;
        }
    }

    /* Reset all the coin positions in the game to initial positions */
    private void resetCoins() {
        Iterator<Sprite> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Sprite nextCoin = coinIterator.next();
            gameRoot.getChildren().remove(nextCoin);
            coinIterator.remove();
        }
        coinsLeft = 0;
        for (int i = 0; i < Area.LEVEL.length; i++) {
            for (int j = 0; j < Area.LEVEL[i].length(); j++) {
                // Create a coin for each '1'
                if (Area.LEVEL[i].charAt(j) == '1') {
                    Sprite coinSprite = new Sprite(AREA_SPRITE_SIZE, AREA_SPRITE_SIZE, new Image("com/example/platformer/ui/coin.png"), j, i);
                    coinSprite.setImage(new Image("com/example/platformer/ui/coin.png"), COIN_OFFSET, COIN_OFFSET, COIN_SIZE, COIN_SIZE);
                    coins.add(coinSprite);
                    gameRoot.getChildren().add(coinSprite);
                    coinsLeft++;
                }
            }
        }
    }

    /* Update and listen to keys on the game when on end game screen */
    private void updateReset() {
        if (keys.getOrDefault(KeyCode.R, false)) {
            resetCoins();
            spawnNewCharacter();

            healthValue = PLAYER_INITIAL_HEALTH;
            userInterfaceRoot.getChildren().remove(currentUI);
            Image health = new Image("com/example/platformer/ui/3d-heart.png");
            currentUI = new UI(health, healthValue, coinsLeft);
            userInterfaceRoot.getChildren().add(currentUI);

            gameTimerStart = Instant.now();
            running = true;
            isAlive = true;
            endScreen = false;
            gameReset = false;
        }
    }

    /* Check for the end screen conditions. Add the screen to the UI root */
    private void showEndScreen() {
        Image endScreen;
        if (healthValue <= 1) {
            endScreen = new Image("com/example/platformer/ui/game-over-text.png");
        } else {
            endScreen = new Image("com/example/platformer/ui/win-text.png");
        }

        // End game timer and calculate time elapsed
        Instant gameTimerFinish = Instant.now();
        long timeElapsed = Duration.between(gameTimerStart, gameTimerFinish).toSeconds();

        userInterfaceRoot.getChildren().remove(currentUI);
        currentUI = new UI(endScreen, timeElapsed);
        userInterfaceRoot.getChildren().add(currentUI);
    }

    /* Set up the program, initialize the main gameplay loop  */
    @Override
    public void start(final Stage stage) {
        initScene();
        Scene scene = new Scene(sceneRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        stage.setTitle("Adventures In Wonderland");
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            public void handle(final long currentNanoTime) {
                if (isAlive) {
                    update();
                }
                if (endScreen) {
                    endScreen = false;
                    keys.keySet().forEach(key -> keys.put(key, false));
                    showEndScreen();
                }
                if (gameReset) {
                    updateReset();
                }
            }
        }.start();
    }

    /** Launch the program. */
    public static void main(final String[] args) {
        launch();
    }
}
