package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.graphics.sprite.Sprite;

public abstract class Entity {
    public int x;
    public int y;
    public Image img;

    public Entity(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    public void isPressed(KeyCode keys, boolean isPress) {

    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x - GameMaster.xCamera, y - GameMaster.yCamera);
    }

    public abstract void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
