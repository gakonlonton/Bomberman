package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

public class Flame extends Entity {
    private int spriteIndex = 0;
    enum flameType {
        UP, DOWN, LEFT, RIGHT, CENTER, VERTICAL, HORIZON
    }
    private flameType type;
    private Map map;
    public Bomb.status status;

    public Bomb.status getStatus() {
        return status;
    }

    public Flame(int x, int y, flameType type, Map map) {
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
            default:
                break;
        }
        this.type = type;
        this.map = map;
        status = Bomb.status.EXPLODED;
    }

    @Override
    public void update() {
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
            default:
                break;
        }
        if (spriteIndex == 20) {
            status = Bomb.status.DISAPPEAR;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (map.getPosition(x, y) instanceof Grass) {
            super.render(gc);
        }
    }
}
