package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class ItemPortal extends Item {
    public static final int code = 4;

    public ItemPortal(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void powerUp(Bomber bomber) {

    }
}
