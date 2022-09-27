package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import uet.oop.bomberman.controller.Direction;
import uet.oop.bomberman.controller.KeyboardEvent;

public class Bomber extends MoveableEntity {

    private KeyboardEvent keyboardEvent;
    public Bomber(int x, int y, Image img, KeyboardEvent keyboardEvent) {
        super(x, y, img);
        this.keyboardEvent = keyboardEvent;
    }

    @Override
    public void update() {
        updatePosition();
    }

    private void updatePosition() {
        if (keyboardEvent.isPressed(KeyCode.W)) {
            super.update(Direction.DIRECTION.UP, true);
        }
        else if (keyboardEvent.isPressed(KeyCode.S)) {
            super.update(Direction.DIRECTION.DOWN, true);
        }
        else if (keyboardEvent.isPressed(KeyCode.A)) {
            super.update(Direction.DIRECTION.LEFT, true);
        }
        else if (keyboardEvent.isPressed(KeyCode.D)) {
            super.update(Direction.DIRECTION.RIGHT, true);
        }
    }
}
