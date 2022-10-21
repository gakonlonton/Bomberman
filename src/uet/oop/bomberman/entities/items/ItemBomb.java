package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.bomber.Bomber;

public class ItemBomb extends Item {
    public static final int code = 3;

    public ItemBomb(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        Bomber.bombCount += 1;
    }
}
