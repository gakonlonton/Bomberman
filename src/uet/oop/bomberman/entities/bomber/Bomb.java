package uet.oop.bomberman.entities.bomber;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.audio.Audio;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.entities.items.*;
import uet.oop.bomberman.entities.obstacle.*;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.controller.Map;
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.util.*;

import static uet.oop.bomberman.entities.bomber.Bomber.*;

public class Bomb extends Entity {
    public enum status {
        REMAIN, EXPLODED, DISAPPEAR
    }

    public status bombStatus;
    protected List<Flame> UpFlame = new ArrayList<>();
    protected List<Flame> DownFlame = new ArrayList<>();
    protected List<Flame> LeftFlame = new ArrayList<>();
    protected List<Flame> RightFlame = new ArrayList<>();
    protected List<Entity> brickDestroyed = new ArrayList<>();
    protected List<Entity> entitiesAfterBrickDestroyed = new ArrayList<>();
    protected Flame CenterFlame;
    private int delayTime = 3;
    private int spriteIndex = 0;
    private CollisionManager collisionManager;
    private Map map;
    Timer timer = new Timer();
    public static Audio audio = new Audio();

    TimerTask task = new TimerTask() {
        int cnt = 0;
        @Override
        public void run() {
            cnt++;
            if ((delayTime - cnt) <= 0) {
                bombStatus = status.EXPLODED;

                for (Entity brick: brickDestroyed) {
                    ((Brick) brick).setBrickStatus(Brick.BrickStatus.DESTROY);
                }
                for (Flame flame : DownFlame) {
                    flame.setFlameStatus(status.EXPLODED);
                }
                for (Flame flame : LeftFlame) {
                    flame.setFlameStatus(status.EXPLODED);
                }
                for (Flame flame : RightFlame) {
                    flame.setFlameStatus(status.EXPLODED);
                }
                for (Flame flame : UpFlame) {
                    flame.setFlameStatus(status.EXPLODED);
                }
                CenterFlame.setFlameStatus(status.EXPLODED);

                audio.playOnBackground(Audio.AudioType.EXPLODING, 1);
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
            xFlame = CenterFlame.getX() / Sprite.SCALED_SIZE;
            yFlame = CenterFlame.getY() / Sprite.SCALED_SIZE;
            return xFlame == xTile && yFlame == yTile;
        }
        return false;
    }

    public boolean setItems(int xTile, int yTile) {
        switch (collisionManager.getMap().getItems(xTile, yTile)) {
            case ItemSpeed.code:
            case ItemFlame.code:
            case ItemBomb.code:
            case ItemPortal.code:
                return true;
            default:
                return false;
        }
    }

    private Entity itemAdd(int xTile, int yTile) {
        switch (collisionManager.getMap().getItems(xTile, yTile)) {
            case ItemSpeed.code:
                return new ItemSpeed(xTile, yTile, Sprite.powerup_speed.getFxImage());
            case ItemFlame.code:
                return new ItemFlame(xTile, yTile, Sprite.powerup_flames.getFxImage());
            case ItemBomb.code:
                return new ItemBomb(xTile, yTile, Sprite.powerup_bombs.getFxImage());
            case ItemPortal.code:
                return new ItemPortal(xTile, yTile, Sprite.portal.getFxImage());
            default:
                return null;
        }
    }

    public void Exploded() {
        // Up Flame Update
        for (int i = 0; i < UpFlame.size(); i++) {
            int xTile = UpFlame.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = UpFlame.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity tempEntity = map.getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) y / Sprite.SCALED_SIZE - (double) UpFlame.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = UpFlame.size() - 1; j >= distance; j--) {
                        UpFlame.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                brickDestroyed.add(tempEntity);
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
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
            Entity tempEntity = map.getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) DownFlame.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = DownFlame.size() - 1; j >= distance; j--) {
                        DownFlame.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                brickDestroyed.add(tempEntity);
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
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
            Entity tempEntity = map.getPosition(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) x / Sprite.SCALED_SIZE - (double) LeftFlame.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = LeftFlame.size() - 1; j >= distance; j--) {
                        LeftFlame.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                brickDestroyed.add(tempEntity);
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
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
            Entity tempEntity = map.getPosition(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) RightFlame.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = RightFlame.size() - 1; j >= distance; j--) {
                        RightFlame.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                brickDestroyed.add(tempEntity);
                if (bombStatus == bombStatus.EXPLODED) {
                    if (!setItems(xTile, yTile)) {
                        map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    } else {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
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
        brickDestroyed.forEach(Entity::update);
        for (List<Flame> flames : Arrays.asList(UpFlame, DownFlame, LeftFlame, RightFlame)) {
            flames.forEach(Entity::update);
        }
        CenterFlame.update();
        Exploded();
        if (bombStatus == status.REMAIN) {
            spriteIndex = (spriteIndex + 1) % 1000;
            pickSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, spriteIndex, 30).getFxImage());
        }
        if (bombStatus == status.EXPLODED) {
            bombStatus = CenterFlame.getStatus();
        }
        if (bombStatus == status.DISAPPEAR) {
            for (Entity entity : entitiesAfterBrickDestroyed) {
                int xTile = entity.getX() / Sprite.SCALED_SIZE;
                int yTile = entity.getY() / Sprite.SCALED_SIZE;
                map.replace(xTile, yTile, entity);
            }
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
            for (Entity brick: brickDestroyed) {
                brick.render(gc);
            }
        }
    }
}
