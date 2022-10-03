package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.controller.KeyboardEvent;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends MoveableEntity {
    private CollisionManager collisionManager;

    private KeyboardEvent keyboardEvent;
    public Bomber(int x, int y, Image img, KeyboardEvent keyboardEvent, CollisionManager collisionManager) {
        super(x, y, img);
        this.keyboardEvent = keyboardEvent;
        this.collisionManager = collisionManager;
    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    @Override
    public void update() {
        updatePosition();
    }

    private void updatePosition() {
        if (keyboardEvent.isPressed(KeyCode.W)) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y - speed, DIRECTION.UP)) {
                super.update(DIRECTION.UP, false);
            }
            else super.update(DIRECTION.UP, true);
            pickSprite(Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, spriteIndex, 15).getFxImage());
        }
        else if (keyboardEvent.isPressed(KeyCode.S)) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y + speed, DIRECTION.DOWN)) {
                super.update(DIRECTION.DOWN, false);
            }
            else super.update(DIRECTION.DOWN, true);
            pickSprite(Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, spriteIndex, 15).getFxImage());
        }
        else if (keyboardEvent.isPressed(KeyCode.A)) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x - speed, y, DIRECTION.LEFT)) {
                super.update(DIRECTION.LEFT, false);
            }
            else super.update(DIRECTION.LEFT, true);
            pickSprite(Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, spriteIndex, 15).getFxImage());
        }
        else if (keyboardEvent.isPressed(KeyCode.D)) {
            spriteIndex++;
            if (collisionManager.touchObstacle(x + speed, y, DIRECTION.RIGHT)) {
                super.update(DIRECTION.RIGHT, false);
            }
            else super.update(DIRECTION.RIGHT, true);
            pickSprite(Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, spriteIndex, 15).getFxImage());
        }
    }
}
