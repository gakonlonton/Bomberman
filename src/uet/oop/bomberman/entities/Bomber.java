package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.controller.KeyboardEvent;
import uet.oop.bomberman.graphics.BombManager;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Bomber extends MoveableEntity {
    private CollisionManager collisionManager;

    private KeyboardEvent keyboardEvent;
    private BombManager bombManager;

    private boolean placeBomb;
    public Bomber(int x, int y, Image img, KeyboardEvent keyboardEvent, CollisionManager collisionManager) {
        super(x, y, img);
        this.keyboardEvent = keyboardEvent;
        this.collisionManager = collisionManager;
        bombManager = new BombManager(collisionManager);
    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    @Override
    public void update() {
        updatePosition();
        updateBomb();
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
        else if (keyboardEvent.isPressed(KeyCode.SPACE)) {
            placeBomb = true;
        }
    }

    public void updateBomb() {
        if (placeBomb) {
            int _x = x / Sprite.SCALED_SIZE;
            int _y = y / Sprite.SCALED_SIZE;
            Bomb bomb = new Bomb(_x, _y, Sprite.bomb.getFxImage(), bombManager.getFlame());
            if (bombManager.valid(_x, _y)) {
                bombManager.addBomb(bomb);
            }
        }
    }

    public BombManager getBombManager() {
        return bombManager;
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(this.img, x, y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        bombManager.renderBomb(gc);
    }
}
