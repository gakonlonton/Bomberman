package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.bomber.Bomber;

public class ItemFlame extends Item {
    public static final int code = 2;
    public ItemFlame(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        Bomber.flameLength += 1;
    }
}
