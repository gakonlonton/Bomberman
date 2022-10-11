package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.Direction.DIRECTION;

public abstract class EntityAnimation extends Entity {
    public DIRECTION direction = DIRECTION.RIGHT;
    protected boolean isRunning = false;
    public int speed = 2;
    public EntityAnimation(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    public void update(DIRECTION dir, boolean success, int realSpeed) {
        updateDirection(dir, success, realSpeed);
    }

    public void updateDirection(DIRECTION dir, boolean success, int realSpeed) {
        if (success) {
            isRunning = true;
            direction = dir;
            if (dir == DIRECTION.DOWN) {
                y += realSpeed;
            }
            if (dir == DIRECTION.UP) {
                y -= realSpeed;
            }
            if (dir == DIRECTION.LEFT) {
                x -= realSpeed;
            }
            if (dir == DIRECTION.RIGHT) {
                x += realSpeed;
            }
        }
        else {
            isRunning = false;
        }
    }
}
