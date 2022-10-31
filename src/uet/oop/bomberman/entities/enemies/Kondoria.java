package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.controller.collision.Graph;
import uet.oop.bomberman.controller.collision.Vertices;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.util.List;

import static uet.oop.bomberman.controller.GameMaster.bombsList;
import static uet.oop.bomberman.controller.GameMaster.level;

public class Kondoria extends Enemy {
    public static final int WIDTH = 30;

    public static final int HEIGHT = 30;

    public enum KondoriaStatus {
        CHASING,
        WALKING,
        INVALID,
    }
    KondoriaStatus kondoriaStatus;

    private Entity bomber;
    private List<List<Entity>> map;
    private List<Vertices> path;
    public Kondoria(int x, int y, Image img, CollisionManager collisionManager, Entity bomber) {
        super(x, y, img, collisionManager);
        kondoriaStatus = KondoriaStatus.WALKING;
        this.speed = 1;
        this.map = collisionManager.getMap().getMap();
        this.bomber = bomber;
    }

    public int getDistanceFromBomber() {
        if (path == null) return -1;
        return path.size();
    }

    public void chasing() {
        Vertices src = path.get(0);
        Vertices dst = path.get(1);
        spriteIndex++;

        if (src.getXTilePos() >= dst.getXTilePos()) {
            if (x > dst.getXTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "LEFT", speed) && !touchBomb(x, y, "LEFT")) {
                    pickSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], spriteIndex, 20).getFxImage());
                    x -= speed;
                } else if (touchBomb(x, y, "LEFT")) {
                    kondoriaStatus = KondoriaStatus.WALKING;
                }
            }
        }

        if (src.getXTilePos() <= dst.getXTilePos()) {
            if (x < dst.getXTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "RIGHT", speed) && !touchBomb(x, y, "RIGHT")) {
                    pickSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], spriteIndex, 20).getFxImage());
                    x += speed;
                } else if (touchBomb(x, y, "RIGHT")) {
                    kondoriaStatus = KondoriaStatus.WALKING;
                }
            }
        }

        if (src.getYTilePos() >= dst.getYTilePos()) {
            if (y > dst.getYTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "UP", speed) && !touchBomb(x, y, "UP")) {
                    pickSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], spriteIndex, 20).getFxImage());
                    y -= speed;
                } else if (touchBomb(x, y, "UP")) {
                    kondoriaStatus = KondoriaStatus.WALKING;
                }
            }
        }

        if (src.getYTilePos() <= dst.getYTilePos()) {
            if (y < dst.getYTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "DOWN", speed) && !touchBomb(x, y, "DOWN")) {
                    pickSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], spriteIndex, 20).getFxImage());
                    y += speed;
                } else if (touchBomb(x, y, "DOWN")) {
                    kondoriaStatus = KondoriaStatus.WALKING;
                }
            }
        }
    }

    public void move() {
        int kondoriaIndex = Graph.getVerticesIndex(x + Kondoria.WIDTH / 2, y + Kondoria.HEIGHT / 2);
        int bomberIndex = Graph.getVerticesIndex(bomber.getX(), bomber.getY());

        if (kondoriaStatus == KondoriaStatus.WALKING) {
            path = collisionManager.getMap().getGraph().BFS(kondoriaIndex, bomberIndex);
            if (path != null && bombsList.size() == 0) kondoriaStatus = KondoriaStatus.CHASING;
        }

        if (kondoriaStatus != KondoriaStatus.CHASING) {
            speed = 1;
            goRandom();
        } else {
            path = collisionManager.getMap().getGraph().BFS(kondoriaIndex, bomberIndex);
            if (path != null) {
                speed = 2;
                chasing();
            }
        }
    }

    public void setKondoriaStatus(KondoriaStatus kondoriaStatus) {
        this.kondoriaStatus = kondoriaStatus;
    }

    public KondoriaStatus getKondoriaStatus() {
        return kondoriaStatus;
    }
}