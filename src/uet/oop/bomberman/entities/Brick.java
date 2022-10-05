package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Brick extends EntityDestroyable implements Obstacle {
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void disapear() {
        death = true;
    }

    @Override
    public void update() {

    }
}
