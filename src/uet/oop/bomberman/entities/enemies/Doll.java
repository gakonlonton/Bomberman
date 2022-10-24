package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.collision.CollisionManager;

public class Doll extends Enemy {
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;

    public Doll(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
    }

    @Override
    public void move() {
        goRandom();
    }
}
