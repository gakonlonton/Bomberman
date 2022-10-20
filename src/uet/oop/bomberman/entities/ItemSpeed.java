package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class ItemSpeed extends Item {
    public static final int code = 1;

    public ItemSpeed(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        Bomber.speed += 1;
    }
}
