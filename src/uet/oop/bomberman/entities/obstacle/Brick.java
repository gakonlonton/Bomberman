package uet.oop.bomberman.entities.obstacle;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.entities.EntityDestroyable;
import uet.oop.bomberman.graphics.sprite.Sprite;

public class Brick extends EntityDestroyable implements Obstacle {
    private int spriteIndex = 0;
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    public void update(int xTile, int yTile) {
        spriteIndex++;
        pickSprite(Sprite.movingSprite(Sprite.brick_exploded,
                Sprite.brick_exploded1,
                Sprite.brick_exploded2, spriteIndex, 15).getFxImage());
        if (spriteIndex == 40) {
            GameMaster.mapList.get(GameMaster.level).replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
            spriteIndex = 0;
        }
    }
}
