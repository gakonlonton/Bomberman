package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class EntityDestroyable extends Entity {
    public int speed;
    public EntityDestroyable(int x, int y, Image img) {
        super(x, y, img);
    }
    public void update() {

    }
}
