package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Flame extends Entity {
    public Flame(int x, int y, Image img) {
        super(x, y, img);
    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    @Override
    public void update() {

    }
}
