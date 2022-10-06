package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;

public class EnemyBalloon extends Enemy {
    private int speed;

    public EnemyBalloon(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
        this.speed = 1;
    }

    public void update() {
        if (death) return;

        if (direction == DIRECTION.LEFT) {
            if (collisionManager.enemyTouchObstacle(x - this.speed, y)) {
                goRandom();
            }
            else super.update(DIRECTION.LEFT, true, this.speed);
        }

        if (direction == DIRECTION.RIGHT) {
            if (collisionManager.enemyTouchObstacle(x + this.speed, y)) {
                goRandom();
            }
            else super.update(DIRECTION.RIGHT, true, this.speed);
        }

        if (direction == DIRECTION.UP) {
            if (collisionManager.enemyTouchObstacle(x, y - this.speed)) {
                goRandom();
            }
            else super.update(DIRECTION.UP, true, this.speed);
        }

        if (direction == DIRECTION.DOWN) {
            if (collisionManager.enemyTouchObstacle(x, y + this.speed)) {
                goRandom();
            }
            else super.update(DIRECTION.DOWN, true, this.speed);
        }
    }

}
