package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Entity implements Obstacle {
    public enum status {
        REMAIN, EXPLODED, DISAPPEAR
    }

    public status bombStatus;
    protected List<Flame> flameExplode = new ArrayList<>();
    public int flameLength = 1;

    private int delayTime = 3;
    private int spriteIndex = 0;
    private CollisionManager collisionManager;
    private Map map;

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        int cnt = 0;
        @Override
        public void run() {
            cnt++;
            if ((delayTime - cnt) <= 0) {
                bombStatus = status.EXPLODED;
                timer.cancel();
                spriteIndex = 0;
            }
        }
    };

    private void pickSprite(Image img) {
        this.img = img;
    }

    public Bomb(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
        this.map = collisionManager.getMap();
        bombStatus = status.REMAIN;
        timer.schedule(task, 0, 1000);
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    flameExplode.add(new Flame(x, y + 1, Flame.flameType.DOWN, map));
                    break;
                case 1:
                    flameExplode.add(new Flame(x, y - 1, Flame.flameType.UP, map));
                    break;
                case 2:
                    flameExplode.add(new Flame(x - 1, y, Flame.flameType.LEFT, map));
                    break;
                case 3:
                    flameExplode.add(new Flame(x + 1, y, Flame.flameType.RIGHT, map));
                    break;
                case 4:
                    flameExplode.add(new Flame(x, y, Flame.flameType.CENTER, map));
                    break;
                default:
                    break;
            }
        }
    }

    public status getBombStatus() {
        return bombStatus;
    }

    @Override
    public void update() {
        if (bombStatus == status.REMAIN) {
            spriteIndex = (spriteIndex + 1) % 1000;
            pickSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, spriteIndex, 30).getFxImage());
        }
        if (bombStatus == status.EXPLODED) {
            spriteIndex = (spriteIndex + 1) % 1000;
            bombStatus = flameExplode.get(0).getStatus();
            flameExplode.forEach(Entity::update);
            bombStatus = (spriteIndex == 15) ? status.DISAPPEAR : bombStatus;
            if (spriteIndex == 15) {
                bombStatus = status.DISAPPEAR;
                bombExplode();
            }
        }
    }

    public boolean inRange(int _x, int _y) {
        int xTile = _x / Sprite.SCALED_SIZE;
        int yTile = _y / Sprite.SCALED_SIZE;
        int xBomb = x / Sprite.SCALED_SIZE;
        int yBomb = y / Sprite.SCALED_SIZE;
        return (xTile == xBomb && yTile == yBomb)
                || (xTile == xBomb && yTile + flameLength == yBomb)
                || (xTile == xBomb && yTile - flameLength == yBomb)
                || (xTile == xBomb + flameLength && yTile == yBomb)
                || (xTile == xBomb - flameLength && yTile == yBomb);
    }

    public void bombExplode() {
        x = x / Sprite.SCALED_SIZE;
        y = y / Sprite.SCALED_SIZE;
        for (int i = 1; i <= flameLength; i++) {
            // Destroy up side
            if (map.getMap().get(y - 1).get(x) instanceof Brick) {
                map.replace(x, y - 1, new Grass(x, y - 1, Sprite.grass.getFxImage()));
            }

            // Destroy down side
            if (map.getMap().get(y + 1).get(x) instanceof Brick) {
                map.replace(x, y + 1, new Grass(x, y + 1, Sprite.grass.getFxImage()));
            }

            // Destroy left side
            if (map.getMap().get(y).get(x - 1) instanceof Brick) {
                map.replace(x - 1, y, new Grass(x - 1, y, Sprite.grass.getFxImage()));
            }

            // Destroy right side
            if (map.getMap().get(y).get(x + 1) instanceof Brick) {
                map.replace(x + 1, y, new Grass(x + 1, y, Sprite.grass.getFxImage()));
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (bombStatus == status.REMAIN) {
            super.render(gc);
        }
        if (bombStatus == status.EXPLODED) {
            flameExplode.forEach(g-> g.render(gc));
        }
    }
}
