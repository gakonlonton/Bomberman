package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;

public class EnemyBalloon extends Enemy {
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;

    public EnemyBalloon(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
        this.speed = 1;
    }

    public void move() {
        goRandom();
    }
}
