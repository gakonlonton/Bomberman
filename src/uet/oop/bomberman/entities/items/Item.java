package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.sprite.Sprite;

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
}
