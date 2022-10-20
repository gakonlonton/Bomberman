package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

import static uet.oop.bomberman.entities.Bomber.*;

public class Bomb extends Entity {
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

    public Bomb(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
        this.map = collisionManager.getMap();
        bombStatus = status.REMAIN;
        timer.schedule(task, 0, 1000);
        CenterFlame = new Flame(x, y, Flame.flameType.CENTER, map);
        int xPixel = x * Sprite.SCALED_SIZE;
        int yPixel = y * Sprite.SCALED_SIZE;
        for (int i = 1; i <= flameLength; i++) {
            if (i == flameLength) {
                UpFlame.add(new Flame(x, y - i, Flame.flameType.UP, map));
                DownFlame.add(new Flame(x, y + i, Flame.flameType.DOWN, map));
                LeftFlame.add(new Flame(x - i, y, Flame.flameType.LEFT, map));
                RightFlame.add(new Flame(x + i, y, Flame.flameType.RIGHT, map));
            }
            else {
                UpFlame.add(new Flame(x, y - i, Flame.flameType.VERTICAL, map));
                DownFlame.add(new Flame(x, y + i, Flame.flameType.VERTICAL, map));
                LeftFlame.add(new Flame(x - i, y, Flame.flameType.HORIZON, map));
                RightFlame.add(new Flame(x + i, y, Flame.flameType.HORIZON, map));
            }
        }
    }

    public status getBombStatus() {
        return bombStatus;
    }

    public boolean inRange(int xPixel, int yPixel) {
        int xTile = xPixel / Sprite.SCALED_SIZE;
        int yTile = yPixel / Sprite.SCALED_SIZE;
        int xFlame, yFlame;
        if (bombStatus == bombStatus.EXPLODED){
            for (Entity i : LeftFlame){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            for (Entity i : UpFlame){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            for (Entity i : RightFlame){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            for (Entity i : DownFlame){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
        }
        return false;
    }

    public boolean setItems(int xTile, int yTile) {
        switch (collisionManager.getMap().getItems(xTile, yTile)) {
            case ItemSpeed.code:
                collisionManager.getMap().replace(xTile, yTile,
                        new ItemSpeed(xTile, yTile, Sprite.powerup_speed.getFxImage()));
                return true;
            case ItemFlame.code:
                collisionManager.getMap().replace(xTile, yTile,
                        new ItemFlame(xTile, yTile, Sprite.powerup_flames.getFxImage()));
                return true;
            case ItemBomb.code:
                collisionManager.getMap().replace(xTile, yTile,
                        new ItemBomb(xTile, yTile, Sprite.powerup_bombs.getFxImage()));
                return true;
            default:
                return false;
        }
    }

    public void Exploded() {
        // Up Flame Update
        for (int i = 0; i < UpFlame.size(); i++) {
            int xTile = UpFlame.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = UpFlame.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameMaster.mapList.get(GameMaster.level)
                    .getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) y / Sprite.SCALED_SIZE - (double) UpFlame.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = UpFlame.size() - 1; j >= distance; j--) {
                        UpFlame.remove(j);
                    }
                }
                break;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        // map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertToGraph();
                    distance = (double) y / Sprite.SCALED_SIZE - (double) UpFlame.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = UpFlame.size() - 1; j >= distance; j--) {
                        UpFlame.remove(j);
                    }
                }
                break;
            }
        }
        // Down Flame Update
        for (int i = 0; i < DownFlame.size(); i++) {
            int xTile = DownFlame.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = DownFlame.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameMaster.mapList.get(GameMaster.level)
                    .getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) DownFlame.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = DownFlame.size() - 1; j >= distance; j--) {
                        DownFlame.remove(j);
                    }
                }
                break;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        // map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertToGraph();
                    distance = (double) DownFlame.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = DownFlame.size() - 1; j >= distance; j--) {
                        DownFlame.remove(j);
                    }
                }
                break;
            }
        }
        // Left Flame Update
        for (int i = 0; i < LeftFlame.size(); i++) {
            int xTile = LeftFlame.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = LeftFlame.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameMaster.mapList.get(GameMaster.level)
                    .getPosition(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) x / Sprite.SCALED_SIZE - (double) LeftFlame.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = LeftFlame.size() - 1; j >= distance; j--) {
                        LeftFlame.remove(j);
                    }
                }
                break;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        // map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertToGraph();
                    distance = (double) x / Sprite.SCALED_SIZE - (double) LeftFlame.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = LeftFlame.size() - 1; j >= distance; j--) {
                        LeftFlame.remove(j);
                    }
                }
                break;
            }
        }
        // Right Flame Update
        for (int i = 0; i < RightFlame.size(); i++) {
            int xTile = RightFlame.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = RightFlame.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameMaster.mapList.get(GameMaster.level)
                    .getPosition(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) RightFlame.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = RightFlame.size() - 1; j >= distance; j--) {
                        RightFlame.remove(j);
                    }
                }
                break;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        // map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertToGraph();
                    distance = (double) RightFlame.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = RightFlame.size() - 1; j >= distance; j--) {
                        RightFlame.remove(j);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void update() {
        Exploded();
        if (bombStatus == status.REMAIN) {
            spriteIndex = (spriteIndex + 1) % 1000;
            pickSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, spriteIndex, 30).getFxImage());
        }
        if (bombStatus == status.EXPLODED) {
            bombStatus = CenterFlame.getStatus();
            for (List<Flame> flames : Arrays.asList(UpFlame, DownFlame, LeftFlame, RightFlame)) {
                flames.forEach(Entity::update);
            }
            CenterFlame.update();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        Exploded();
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
