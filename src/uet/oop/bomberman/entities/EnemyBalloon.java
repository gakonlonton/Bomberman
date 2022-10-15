package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.graphics.Sprite;

public class EnemyBalloon extends Enemy {
    private int speed;
    private int spriteIndex = 0;

    public EnemyBalloon(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
        this.speed = 1;
    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    public void update() {
        if (death) return;

        if (direction == DIRECTION.LEFT) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x - this.speed, y, 1)) {
                goRandom();
                spriteIndex = 0;
            }
            else super.update(DIRECTION.LEFT, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.balloom_left1,
                                        Sprite.balloom_left2,
                                        Sprite.balloom_left3, spriteIndex, 20).getFxImage());
        }

        if (direction == DIRECTION.RIGHT) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x + this.speed, y, 1)) {
                goRandom();
                spriteIndex = 0;
            }
            else super.update(DIRECTION.RIGHT, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.balloom_right1,
                                        Sprite.balloom_right2,
                                        Sprite.balloom_right3, spriteIndex, 20).getFxImage());
        }

        if (direction == DIRECTION.UP) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y - this.speed, 1)) {
                goRandom();
                spriteIndex = 0;
            }
            else super.update(DIRECTION.UP, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.balloom_right1,
                                        Sprite.balloom_right2,
                                        Sprite.balloom_right3, spriteIndex, 20).getFxImage());
        }

        if (direction == DIRECTION.DOWN) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y + this.speed, 1)) {
                goRandom();
                spriteIndex = 0;
            }
            else super.update(DIRECTION.DOWN, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.balloom_left1,
                                        Sprite.balloom_left2,
                                        Sprite.balloom_left3, spriteIndex, 20).getFxImage());
        }
    }

}
