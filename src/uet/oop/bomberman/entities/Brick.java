package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends EntityDestroyable implements Obstacle {
    private int spriteIndex = 0;
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        spriteIndex++;
        if (spriteIndex == 15) {
            pickSprite(Sprite.movingSprite(Sprite.brick_exploded,
                    Sprite.brick_exploded1,
                    Sprite.brick_exploded2, spriteIndex, 15).getFxImage());
            spriteIndex = 0;
        }
    }
}
