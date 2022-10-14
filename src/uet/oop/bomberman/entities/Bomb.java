package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

import static uet.oop.bomberman.entities.Bomber.*;

public class Bomb extends Entity implements Obstacle {
    public enum status {
        REMAIN, EXPLODED, DISAPPEAR
    }

    public status bombStatus;
    protected List<Flame> UpFlame = new ArrayList<>();
    protected List<Flame> DownFlame = new ArrayList<>();
    protected List<Flame> LeftFlame = new ArrayList<>();
    protected List<Flame> RightFlame = new ArrayList<>();
    protected Flame CenterFlame;
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
        CenterFlame = new Flame(x, y, Flame.flameType.CENTER, map);
        int _x = x * Sprite.SCALED_SIZE;
        int _y = y * Sprite.SCALED_SIZE;
        boolean leftCheck = true, rightCheck = true, upCheck = true, downCheck = true;
        for (int len = 1; len < flameLength; len++) {
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 0:
                        if (map.getPosition(_x, Math.min(_y + len * Sprite.SCALED_SIZE, 18 * Sprite.SCALED_SIZE)) instanceof Obstacle) {
                            downCheck = false;
                        }
                        if (downCheck) {
                            DownFlame.add(new Flame(x, Math.min(y + len, 18), Flame.flameType.VERTICAL, map));
                        }
                        break;
                    case 1:
                        if (map.getPosition(_x, Math.max(_y - len * Sprite.SCALED_SIZE, 0 * Sprite.SCALED_SIZE)) instanceof Obstacle) {
                            upCheck = false;
                        }
                        if (upCheck) {
                            UpFlame.add(new Flame(x, Math.max(y - len, 0), Flame.flameType.VERTICAL, map));
                        }
                        break;
                    case 2:
                        if (map.getPosition(Math.max(_x - len * Sprite.SCALED_SIZE, 0 * Sprite.SCALED_SIZE), _y) instanceof Obstacle) {
                            leftCheck = false;
                        }
                        if (leftCheck) {
                            LeftFlame.add(new Flame(Math.max(x - len, 0), y, Flame.flameType.HORIZON, map));
                        }
                        break;
                    case 3:
                        if (map.getPosition(Math.min(_x + len * Sprite.SCALED_SIZE, 32 * Sprite.SCALED_SIZE), _y) instanceof Obstacle) {
                            rightCheck = false;
                        }
                        if (rightCheck) {
                            RightFlame.add(new Flame(Math.min(x + len, 32), y, Flame.flameType.HORIZON, map));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    if (map.getPosition(_x, Math.min(_y + flameLength * Sprite.SCALED_SIZE, 18 * Sprite.SCALED_SIZE)) instanceof Obstacle) {
                        downCheck = false;
                    }
                    if (downCheck) {
                        DownFlame.add(new Flame(x, Math.min(y + flameLength, 18), Flame.flameType.DOWN, map));
                    }
                    break;
                case 1:
                    if (map.getPosition(_x, Math.max(_y - flameLength * Sprite.SCALED_SIZE, 0 * Sprite.SCALED_SIZE)) instanceof Obstacle) {
                        upCheck = false;
                    }
                    if (upCheck) {
                        UpFlame.add(new Flame(x, Math.max(y - flameLength, 0), Flame.flameType.UP, map));
                    }
                    break;
                case 2:
                    if (map.getPosition(Math.max(_x - flameLength * Sprite.SCALED_SIZE, 0 * Sprite.SCALED_SIZE), _y) instanceof Obstacle) {
                        leftCheck = false;
                    }
                    if (leftCheck) {
                        LeftFlame.add(new Flame(Math.max(x - flameLength, 0), y, Flame.flameType.LEFT, map));
                    }
                    break;
                case 3:
                    if (map.getPosition(Math.min(_x + flameLength * Sprite.SCALED_SIZE, 32 * Sprite.SCALED_SIZE), _y) instanceof Obstacle) {
                        rightCheck = false;
                    }
                    if (rightCheck) {
                        RightFlame.add(new Flame(Math.min(x + flameLength, 32), y, Flame.flameType.RIGHT, map));
                    }
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
            bombStatus = CenterFlame.getStatus();
            UpFlame.forEach(Entity::update);
            DownFlame.forEach(Entity::update);
            LeftFlame.forEach(Entity::update);
            RightFlame.forEach(Entity::update);
            CenterFlame.update();
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

    public void setBombStatus(status S) {
        this.bombStatus = S;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (bombStatus == status.REMAIN) {
            super.render(gc);
        }
        if (bombStatus == status.EXPLODED) {
            for (List<Flame> flames : Arrays.asList(UpFlame, DownFlame, LeftFlame, RightFlame)) {
                flames.forEach(g -> g.render(gc));
            }
            CenterFlame.render(gc);
        }
    }
}
