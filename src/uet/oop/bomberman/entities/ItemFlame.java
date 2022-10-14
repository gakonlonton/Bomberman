package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class ItemFlame extends Item {
    public static final int code = 2;
    public ItemFlame(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void powerUp(Bomber bomber) {

    }
}
