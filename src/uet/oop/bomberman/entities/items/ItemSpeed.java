package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.bomber.Bomber;

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
