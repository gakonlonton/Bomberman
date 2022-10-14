package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class ItemBomb extends Item {
    public static final int code = 3;

    public ItemBomb(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void powerUp(Bomber bomber) {

    }
}
