package uet.oop.bomberman.entities.obstacle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.entities.EntityDestroyable;
import uet.oop.bomberman.graphics.sprite.Sprite;

public class Brick extends EntityDestroyable implements Obstacle {
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }
}
