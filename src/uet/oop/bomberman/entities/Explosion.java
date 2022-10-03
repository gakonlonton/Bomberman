package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Explosion extends Entity {
    public enum EXPLOSION_STATE {
        BRICK, MIDDLE, END
    }

    private DIRECTION direction;
    private CollisionManager collisionManager;
    private EXPLOSION_STATE explosionState;
    private boolean exploded;
    private int spriteIndex = 0;

    public Explosion(int x, int y, Image img, DIRECTION direction, EXPLOSION_STATE explosionState, CollisionManager collisionManager) {
        super(x, y, img);
        this.direction = direction;
        this.explosionState = explosionState;
        this.collisionManager = collisionManager;
        exploded = false;
    }

    public boolean isExploded() {
        return exploded;
    }

    @Override
    public void update() {
        spriteIndex++;
        if (!(img == Sprite.brick_exploded.getFxImage()
            || img == Sprite.bomb_exploded1.getFxImage()
            || img == Sprite.bomb_exploded2.getFxImage())) {
            Map map = collisionManager.getMap();
            for (int i = 0; i < map.getEntities().size(); i++) {
                Entity e = map.getEntities().get(i);
                if (e instanceof DestroyableEntity) {
                    if (collisionManager.collide(this, e)) {
                        ((DestroyableEntity) e).disapear();
                    }
                }
            }
            List<Bomb> bombs = collisionManager.getBombList();
            for (int i = 0; i < bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                if (x == bomb.x && y == bomb.y) {
                    bomb.setExploded(true);
                }
            }
        }
    }

    private void pickSprite(Image img) {
        this.img = img;
    }
}
