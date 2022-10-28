package uet.oop.bomberman.entities.bomber;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.controller.Map;
import uet.oop.bomberman.entities.obstacle.Wall;
import uet.oop.bomberman.graphics.sprite.Sprite;

public class Explosion extends Entity {
    private int spriteIndex = 0;
    enum explosionType {
        UP, DOWN, LEFT, RIGHT, CENTER, VERTICAL, HORIZON, BRICK
    }
    private explosionType type;
    private Map map;
    public Bomb.status status;

    public Bomb.status getStatus() {
        return status;
    }

    public Explosion(int x, int y, explosionType type, Map map) {
        super(x, y, null);
        switch (type) {
            case DOWN:
                img = Sprite.explosion_vertical_down_last.getFxImage();
                break;
            case UP:
                img = Sprite.explosion_vertical_top_last.getFxImage();
                break;
            case LEFT:
                img = Sprite.explosion_horizontal_left_last.getFxImage();
                break;
            case RIGHT:
                img = Sprite.explosion_horizontal_right_last.getFxImage();
                break;
            case HORIZON:
                img = Sprite.explosion_horizontal.getFxImage();
                break;
            case VERTICAL:
                img = Sprite.explosion_vertical.getFxImage();
                break;
            case CENTER:
                img = Sprite.bomb_exploded2.getFxImage();
                break;
            case BRICK:
                img = Sprite.brick_exploded.getFxImage();
            default:
                break;
        }
        this.type = type;
        this.map = map;
        status = Bomb.status.REMAIN;
    }

    public void setFlameStatus(Bomb.status status) {
        this.status = status;
    }

    public void setFlameType(explosionType type) {
        this.type = type;
    }

    @Override
    public void update() {
        if (status == Bomb.status.EXPLODED) {
            spriteIndex++;
            switch (type) {
                case LEFT:
                    pickSprite(Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                            Sprite.explosion_horizontal_left_last1,
                            Sprite.explosion_horizontal_left_last2,
                            spriteIndex, 20).getFxImage());
                    break;
                case RIGHT:
                    pickSprite(Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                            Sprite.explosion_horizontal_right_last1,
                            Sprite.explosion_horizontal_right_last2,
                            spriteIndex, 20).getFxImage());
                    break;
                case UP:
                    pickSprite(Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                            Sprite.explosion_vertical_top_last1,
                            Sprite.explosion_vertical_top_last2,
                            spriteIndex, 20).getFxImage());
                    break;
                case DOWN:
                    pickSprite(Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                            Sprite.explosion_vertical_down_last1,
                            Sprite.explosion_vertical_down_last2,
                            spriteIndex, 20).getFxImage());
                    break;
                case CENTER:
                    pickSprite(Sprite.movingSprite(Sprite.bomb_exploded,
                            Sprite.bomb_exploded1,
                            Sprite.bomb_exploded2,
                            spriteIndex, 20).getFxImage());
                    break;
                case VERTICAL:
                    pickSprite(Sprite.movingSprite(Sprite.explosion_vertical,
                            Sprite.explosion_vertical1,
                            Sprite.explosion_vertical2,
                            spriteIndex, 20).getFxImage());
                    break;
                case HORIZON:
                    pickSprite(Sprite.movingSprite(Sprite.explosion_horizontal,
                            Sprite.explosion_horizontal1,
                            Sprite.explosion_horizontal2,
                            spriteIndex, 20).getFxImage());
                    break;
                case BRICK:
                    pickSprite(Sprite.movingSprite(Sprite.brick_exploded,
                            Sprite.brick_exploded1,
                            Sprite.brick_exploded2,
                            spriteIndex, 20).getFxImage());
                default:
                    break;
            }
            if (spriteIndex == 20) {
                status = Bomb.status.DISAPPEAR;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!(map.getPosition(x, y) instanceof Wall)) {
            super.render(gc);
        }
    }
}
