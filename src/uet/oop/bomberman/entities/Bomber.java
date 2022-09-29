package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.controller.KeyboardEvent;

public class Bomber extends MoveableEntity {
    private CollisionManager collisionManager;

    private KeyboardEvent keyboardEvent;
    public Bomber(int x, int y, Image img, KeyboardEvent keyboardEvent, CollisionManager collisionManager) {
        super(x, y, img);
        this.keyboardEvent = keyboardEvent;
        this.collisionManager = collisionManager;
    }

    @Override
    public void update() {
        updatePosition();
    }

    private void updatePosition() {
        if (keyboardEvent.isPressed(KeyCode.W)) {
            if (collisionManager.touchObstacle(x, y - 1, DIRECTION.UP)) {
                super.update(DIRECTION.UP, false);
            }
            else super.update(DIRECTION.UP, true);
        }
        else if (keyboardEvent.isPressed(KeyCode.S)) {
            if (collisionManager.touchObstacle(x, y + 1, DIRECTION.DOWN)) {
                super.update(DIRECTION.DOWN, false);
            }
            else super.update(DIRECTION.DOWN, true);
        }
        else if (keyboardEvent.isPressed(KeyCode.A)) {
            if (collisionManager.touchObstacle(x - 1, y, DIRECTION.LEFT)) {
                super.update(DIRECTION.LEFT, false);
            }
            else super.update(DIRECTION.LEFT, true);
        }
        else if (keyboardEvent.isPressed(KeyCode.D)) {
            if (collisionManager.touchObstacle(x + 1, y, DIRECTION.RIGHT)) {
                super.update(DIRECTION.RIGHT, false);
            }
            else super.update(DIRECTION.RIGHT, true);
        }
    }
}
