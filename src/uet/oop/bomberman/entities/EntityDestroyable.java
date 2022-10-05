package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class EntityDestroyable extends EntityAnimation {
    public boolean death = false;

    public EntityDestroyable(int x, int y, Image img) {
        super(x, y, img);
    }

    public abstract void disapear();

    public void update() {

    }
}
