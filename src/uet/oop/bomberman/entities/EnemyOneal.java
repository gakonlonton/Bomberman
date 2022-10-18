package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.controller.Graph;
import uet.oop.bomberman.controller.Vertices;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class EnemyOneal extends Enemy {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    public enum OnealStatus {
        CHASING,
        WALKING,
        INVALID
    }
    OnealStatus onealStatus;

    private Entity bomber;
    private List<List<Entity>> map = new ArrayList<>();
    private List<Vertices> path;

    public EnemyOneal(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
        onealStatus = OnealStatus.WALKING;
        this.speed = 2;
        this.map = collisionManager.getMap().getMap();
        this.bomber = collisionManager.getMap().getBomber();
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
                if (!collisionManager.touchObstacle(x, y, "LEFT")) {
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
                if (!collisionManager.touchObstacle(x, y, "RIGHT")) {
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
                if (!collisionManager.touchObstacle(x, y, "UP")) {
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
                if (!collisionManager.touchObstacle(x, y, "DOWN")) {
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
        int onealIndex = Graph.getVerticesIndex(x + EnemyOneal.WIDTH / 2, y + EnemyOneal.HEIGHT / 2);
        int bomberIndex = Graph.getVerticesIndex(bomber.getX(), bomber.getY());

        if (onealStatus == OnealStatus.WALKING) {
            path = collisionManager.getMap().getGraph().breathFirstSearch(onealIndex, bomberIndex);
            if (path != null) onealStatus = OnealStatus.CHASING;
        }

        if (onealStatus != OnealStatus.CHASING) {
            goRandom();
        } else {
            path = collisionManager.getMap().getGraph().breathFirstSearch(onealIndex, bomberIndex);
            if (path != null) {
                chasing();
            }
        }
    }

    public void setOnealStatus(OnealStatus onealStatus) {
        this.onealStatus = onealStatus;
    }

    public OnealStatus getOnealStatus() {
        return onealStatus;
    }
}
