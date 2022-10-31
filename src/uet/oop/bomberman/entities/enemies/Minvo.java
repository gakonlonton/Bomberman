package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.controller.collision.Graph;
import uet.oop.bomberman.controller.collision.Vertices;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.util.List;

public class Minvo extends Enemy {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    public enum MinvoStatus {
        CHASING,
        WALKING,
        INVALID
    }
    MinvoStatus minvoStatus;

    private Entity bomber;
    private List<List<Entity>> map;
    private List<Vertices> path;
    public Minvo(int x, int y, Image img, CollisionManager collisionManager, Entity bomber) {
        super(x, y, img, collisionManager);
        minvoStatus = MinvoStatus.WALKING;
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
                }
            }
        }
    }

    public void move() {
        int onealIndex = Graph.getVerticesIndex(x + Oneal.WIDTH / 2, y + Oneal.HEIGHT / 2);
        int bomberIndex = Graph.getVerticesIndex(bomber.getX(), bomber.getY());

        if (minvoStatus == MinvoStatus.WALKING) {
            path = collisionManager.getMap().getGraph().BFS(onealIndex, bomberIndex);
            if (path != null) minvoStatus = MinvoStatus.CHASING;
        }

        if (minvoStatus != MinvoStatus.CHASING) {
            goRandom();
        } else {
            path = collisionManager.getMap().getGraph().BFS(onealIndex, bomberIndex);
            if (path != null) {
                chasing();
            }
        }
    }

    public void setMinvoStatus(MinvoStatus minvoStatus) {
        this.minvoStatus = Minvo.this.minvoStatus;
    }

    public MinvoStatus getMinvoStatus() {
        return minvoStatus;
    }
}
