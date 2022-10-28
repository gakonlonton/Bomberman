package uet.oop.bomberman.entities.obstacle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.entities.EntityDestroyable;
import uet.oop.bomberman.graphics.sprite.Sprite;

public class Brick extends EntityDestroyable implements Obstacle {
    public enum BrickStatus {
        INTACT,
        DESTROY,
        DISAPPEAR
    }
    BrickStatus brickStatus;
    private int spriteIndex = 0;
    public Brick(int x, int y, Image img) {
        super(x, y, img);
        brickStatus = BrickStatus.INTACT;
    }

    public void update(int xTile, int yTile) {
        if (brickStatus == BrickStatus.DESTROY) {
            spriteIndex++;
            pickSprite(Sprite.movingSprite(Sprite.brick_exploded,
                                        Sprite.brick_exploded1,
                                        Sprite.brick_exploded2, spriteIndex, 20).getFxImage());
            if (spriteIndex == 20) {
                brickStatus = BrickStatus.DISAPPEAR;
            }
        }
    }

    public BrickStatus getBrickStatus() {
        return brickStatus;
    }

    public void setBrickStatus(BrickStatus brickStatus) {
        this.brickStatus = brickStatus;
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }
}
