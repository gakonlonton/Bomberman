package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.Direction.DIRECTION;

public abstract class MoveableEntity extends Entity {
    public DIRECTION direction = DIRECTION.RIGHT;
    protected boolean isRunning = false;
    /// public DIRECTION backward = DIRECTION.RIGHT;

    public MoveableEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    public void update(DIRECTION dir, boolean success) {
        updateDirection(dir, success);
    }

    public void updateDirection(DIRECTION dir, boolean success) {
        if (success) {
            isRunning = true;
            direction = dir;
            if (dir == DIRECTION.DOWN) {
                y += 1;
            }
            if (dir == DIRECTION.UP) {
                y -= 1;
            }
            if (dir == DIRECTION.LEFT) {
                x -= 1;
            }
            if (dir == DIRECTION.RIGHT) {
                x += 1;
            }
        }
        else {
            isRunning = false;
        }
    }
}
