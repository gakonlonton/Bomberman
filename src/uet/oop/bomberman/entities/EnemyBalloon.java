package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;

public class EnemyBalloon extends Enemy {
    private int speed;

    public EnemyBalloon(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
        this.speed = 2;
    }

    public void update() {
        if (death) return;

        if (direction == DIRECTION.LEFT) {
            if (collisionManager.enemyTouchObstacle(x - speed, y)) {
                goRandom();
            }
            else super.update(DIRECTION.LEFT, true);
        }

        if (direction == DIRECTION.RIGHT) {
            if (collisionManager.enemyTouchObstacle(x + speed, y)) {
                goRandom();
            }
            else super.update(DIRECTION.RIGHT, true);
        }

        if (direction == DIRECTION.UP) {
            if (collisionManager.enemyTouchObstacle(x, y - speed)) {
                goRandom();
            }
            else super.update(DIRECTION.UP, true);
        }

        if (direction == DIRECTION.DOWN) {
            if (collisionManager.enemyTouchObstacle(x, y + speed)) {
                goRandom();
            }
            else super.update(DIRECTION.DOWN, true);
        }
    }

}
