package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class DestroyableEntity extends Entity {
    public boolean death = false;

    public DestroyableEntity(int x, int y, Image img) {
        super(x, y, img);
    }

    public abstract void disapear();

    public void update() {

    }
}
