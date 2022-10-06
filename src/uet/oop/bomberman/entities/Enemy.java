package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;

public class Enemy extends EntityDestroyable {
    protected CollisionManager collisionManager;

    public Enemy(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
    }

    public boolean goLeft() {
        if (collisionManager.enemyTouchObstacle(x - speed, y)) {
            return false;
        }
        else {
            super.update(DIRECTION.LEFT, true, speed);
            return true;
        }
    }

    public boolean goRight() {
        if (collisionManager.enemyTouchObstacle(x + speed, y)) {
            return false;
        }
        else {
            super.update(DIRECTION.RIGHT, true, speed);
            return true;
        }
    }

    public boolean goUp() {
        if (collisionManager.enemyTouchObstacle(x, y - speed)) {
            return false;
        }
        else {
            super.update(DIRECTION.UP, true, speed);
            return true;
        }
    }

    public boolean goDown() {
        if (collisionManager.enemyTouchObstacle(x, y + speed)) {
            return false;
        }
        else {
            super.update(DIRECTION.DOWN, true, speed);
            return true;
        }
    }

    public void goRandom() {
        int rand = (int) (Math.random() * 4);
        switch (rand) {
            case 0:
                if(goLeft()) return;
                if(goUp()) return;
                if(goRight()) return;
                if(goDown()) return;
                break;
            case 1:
                if(goRight()) return;
                if(goDown()) return;
                if(goLeft()) return;
                if(goUp()) return;
                break;
            case 2:
                if(goUp()) return;
                if(goRight()) return;
                if(goDown()) return;
                if(goLeft()) return;
                break;
            case 3:
                if(goDown()) return;
                if(goRight()) return;
                if(goLeft()) return;
                if(goUp()) return;
                break;
            default:
                break;
        }
    }

    @Override
    public void disapear() {
        death = true;
    }
}
