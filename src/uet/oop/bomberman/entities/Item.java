package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Item extends Entity {
    public Item(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    public boolean inRange(int x, int y) {
        int xTile = x / Sprite.SCALED_SIZE;
        int yTile = y / Sprite.SCALED_SIZE;
        return (xTile == this.x / Sprite.SCALED_SIZE
                && yTile == this.y / Sprite.SCALED_SIZE);
    }

    public abstract void powerUp(Bomber bomber);
}
