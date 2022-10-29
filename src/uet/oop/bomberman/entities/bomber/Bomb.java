package uet.oop.bomberman.entities.bomber;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.audio.Audio;
import uet.oop.bomberman.controller.collision.CollisionManager;
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
    protected List<Explosion> upExplosion = new ArrayList<>();
    protected List<Explosion> downExplosion = new ArrayList<>();
    protected List<Explosion> leftExplosion = new ArrayList<>();
    protected List<Explosion> rightExplosion = new ArrayList<>();
    protected List<Entity> entitiesAfterBrickDestroyed = new ArrayList<>();
    protected Explosion centerExplosion;
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

                for (Explosion explosion : downExplosion) {
                    explosion.setFlameStatus(status.EXPLODED);
                }
                for (Explosion explosion : leftExplosion) {
                    explosion.setFlameStatus(status.EXPLODED);
                }
                for (Explosion explosion : rightExplosion) {
                    explosion.setFlameStatus(status.EXPLODED);
                }
                for (Explosion explosion : upExplosion) {
                    explosion.setFlameStatus(status.EXPLODED);
                }
                centerExplosion.setFlameStatus(status.EXPLODED);

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
        centerExplosion = new Explosion(x, y, Explosion.explosionType.CENTER, map);
        int xPixel = x * Sprite.SCALED_SIZE;
        int yPixel = y * Sprite.SCALED_SIZE;
        for (int i = 1; i <= flameLength; i++) {
            if (i == flameLength) {
                upExplosion.add(new Explosion(x, y - i, Explosion.explosionType.UP, map));
                downExplosion.add(new Explosion(x, y + i, Explosion.explosionType.DOWN, map));
                leftExplosion.add(new Explosion(x - i, y, Explosion.explosionType.LEFT, map));
                rightExplosion.add(new Explosion(x + i, y, Explosion.explosionType.RIGHT, map));
            }
            else {
                upExplosion.add(new Explosion(x, y - i, Explosion.explosionType.VERTICAL, map));
                downExplosion.add(new Explosion(x, y + i, Explosion.explosionType.VERTICAL, map));
                leftExplosion.add(new Explosion(x - i, y, Explosion.explosionType.HORIZON, map));
                rightExplosion.add(new Explosion(x + i, y, Explosion.explosionType.HORIZON, map));
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
            for (Entity i : leftExplosion){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            for (Entity i : upExplosion){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            for (Entity i : rightExplosion){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            for (Entity i : downExplosion){
                xFlame = i.getX() / Sprite.SCALED_SIZE;
                yFlame = i.getY() / Sprite.SCALED_SIZE;
                if (xFlame == xTile && yFlame == yTile) return true;
            }
            xFlame = centerExplosion.getX() / Sprite.SCALED_SIZE;
            yFlame = centerExplosion.getY() / Sprite.SCALED_SIZE;
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
        // Up Explosion Update
        for (int i = 0; i < upExplosion.size(); i++) {
            int xTile = upExplosion.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = upExplosion.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity tempEntity = map.getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) y / Sprite.SCALED_SIZE - (double) upExplosion.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = upExplosion.size() - 1; j >= distance; j--) {
                        upExplosion.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    if (setItems(xTile, yTile)) {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
                    }
                    map.convertToGraph();
                    distance = (double) y / Sprite.SCALED_SIZE - (double) upExplosion.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = upExplosion.size() - 1; j >= distance; j--) {
                        upExplosion.remove(j);
                    }
                    upExplosion.get((int) distance - 1).setFlameType(Explosion.explosionType.BRICK);
                }
                break;
            }
        }

        // Down Explosion Update
        for (int i = 0; i < downExplosion.size(); i++) {
            int xTile = downExplosion.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = downExplosion.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity tempEntity = map.getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) downExplosion.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = downExplosion.size() - 1; j >= distance; j--) {
                        downExplosion.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    if (setItems(xTile, yTile)) {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
                    }
                    map.convertToGraph();
                    distance = (double) downExplosion.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = downExplosion.size() - 1; j >= distance; j--) {
                        downExplosion.remove(j);
                    }
                    downExplosion.get((int) distance - 1).setFlameType(Explosion.explosionType.BRICK);
                }
                break;
            }
        }

        // Left Explosion Update
        for (int i = 0; i < leftExplosion.size(); i++) {
            int xTile = leftExplosion.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = leftExplosion.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity tempEntity = map.getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) x / Sprite.SCALED_SIZE - (double) leftExplosion.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = leftExplosion.size() - 1; j >= distance; j--) {
                        leftExplosion.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    if (setItems(xTile, yTile)) {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
                    }
                    map.convertToGraph();
                    distance = (double) x / Sprite.SCALED_SIZE - (double) leftExplosion.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = leftExplosion.size() - 1; j >= distance; j--) {
                        leftExplosion.remove(j);
                    }
                    leftExplosion.get((int) distance - 1).setFlameType(Explosion.explosionType.BRICK);
                }
                break;
            }
        }

        // Right Explosion Update
        for (int i = 0; i < rightExplosion.size(); i++) {
            int xTile = rightExplosion.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = rightExplosion.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity tempEntity = map.getPosition(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (tempEntity instanceof Wall) {
                if (bombStatus == bombStatus.EXPLODED) {
                    distance = (double) rightExplosion.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = rightExplosion.size() - 1; j >= distance; j--) {
                        rightExplosion.remove(j);
                    }
                }
                break;
            } else if (tempEntity instanceof Brick) {
                if (bombStatus == bombStatus.EXPLODED) {
                    map.replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    if (setItems(xTile, yTile)) {
                        entitiesAfterBrickDestroyed.add(itemAdd(xTile, yTile));
                    }
                    map.convertToGraph();
                    distance = (double) rightExplosion.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = rightExplosion.size() - 1; j >= distance; j--) {
                        rightExplosion.remove(j);
                    }
                    rightExplosion.get((int) distance - 1).setFlameType(Explosion.explosionType.BRICK);
                }
                break;
            }
        }
    }

    @Override
    public void update() {
        Exploded();
        for (List<Explosion> explosions : Arrays.asList(upExplosion, downExplosion, leftExplosion, rightExplosion)) {
            explosions.forEach(Entity::update);
        }
        centerExplosion.update();

        if (bombStatus == status.REMAIN) {
            spriteIndex = (spriteIndex + 1) % 1000;
            pickSprite(Sprite.movingSprite(Sprite.bomb,
                                        Sprite.bomb_1,
                                        Sprite.bomb_2, spriteIndex, 30).getFxImage());
        }
        if (bombStatus == status.EXPLODED) {
            bombStatus = centerExplosion.getStatus();
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
            for (List<Explosion> explosions : Arrays.asList(upExplosion, downExplosion, leftExplosion, rightExplosion)) {
                explosions.forEach(g -> g.render(gc));
            }
            centerExplosion.render(gc);
        }
    }
}
