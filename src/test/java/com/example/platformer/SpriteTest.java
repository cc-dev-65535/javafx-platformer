package com.example.platformer;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpriteTest {
    @Test
    @DisplayName("Get velocity of a new Sprite object")
    void getPlayerVelocity() {
        Sprite testSprite = new Sprite(60, 60, null, 0, 0);
        assertEquals(testSprite.getPlayerVelocity(), new Point2D(0, 0));
    }

    @Test
    @DisplayName("Set velocity of a new Sprite object")
    void setPlayerVelocity() {
        Sprite testSprite = new Sprite(60, 60, null, 0, 0);
        assertEquals(testSprite.getPlayerVelocity(), new Point2D(0, 0));
        testSprite.setPlayerVelocity(new Point2D(0, 10));
        assertEquals(testSprite.getPlayerVelocity(), new Point2D(0, 10));
    }
}