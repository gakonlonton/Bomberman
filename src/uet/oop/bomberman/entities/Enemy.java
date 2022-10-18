package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static uet.oop.bomberman.BombermanGame.*;
import static uet.oop.bomberman.controller.GameMaster.bombsList;

public abstract class Enemy extends EntityDestroyable {
    public int spriteIndex = 0;
    public enum EnemyStatus {
        ALIVE,
        DEAD
    }
    EnemyStatus enemyStatus;
    protected CollisionManager collisionManager;
    protected Sprite[] leftSprites = new Sprite[3];
    protected Sprite[] rightSprites = new Sprite[3];

    public Enemy(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
        enemyStatus = EnemyStatus.ALIVE;
        this.speed = 1;
    }

    private boolean goNext = false;

    public void goRandom() {
        String dir = "";
        if (this instanceof EnemyBalloon) {
            leftSprites[0] = Sprite.balloom_left1;
            leftSprites[1] = Sprite.balloom_left2;
            leftSprites[2] = Sprite.balloom_left3;
            rightSprites[0] = Sprite.balloom_right1;
            rightSprites[1] = Sprite.balloom_right2;
            rightSprites[2] = Sprite.balloom_right3;
        }
        if (this instanceof EnemyOneal) {
            leftSprites[0] = Sprite.oneal_left1;
            leftSprites[1] = Sprite.oneal_left2;
            leftSprites[2] = Sprite.oneal_left3;
            rightSprites[0] = Sprite.oneal_right1;
            rightSprites[1] = Sprite.oneal_right2;
            rightSprites[2] = Sprite.oneal_right3;
        }
        spriteIndex++;
        if (!goNext) {
            int rand = (int) (Math.random() * 4);
            switch (rand) {
                case 0:
                    dir = "LEFT";
                    break;
                case 1:
                    dir = "RIGHT";
                    break;
                case 2:
                    dir = "UP";
                    break;
                case 3:
                    dir = "DOWN";
                    break;
                default:
                    break;
            }
        }
        if (collisionManager.touchObstacle(x, y, dir)) {
            goNext = false;
            spriteIndex = 0;
        } else {
            if (dir == "LEFT") {
                x -= speed;
                pickSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], spriteIndex, 20).getFxImage());
            }
            if (dir == "RIGHT") {
                x += speed;
                pickSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], spriteIndex, 20).getFxImage());
            }
            if (dir == "UP") {
                y -= speed;
                pickSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], spriteIndex, 20).getFxImage());
            }
            if (dir == "DOWN") {
                y += speed;
                pickSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], spriteIndex, 20).getFxImage());
            }
            goNext = true;
        }
    }

    public abstract void move();

    public EnemyStatus getEnemyStatus() {
        return enemyStatus;
    }

    public boolean touchBomber(int xTile, int yTile) {
        boolean check = true;
        if (xTile + Bomber.WIDTH < x || xTile > x + Sprite.SCALED_SIZE) check = false;
        if (yTile + Bomber.HEIGHT < y || yTile > y + Sprite.SCALED_SIZE) check = false;
        return check;
    }

    @Override
    public void update() {
        for (Entity i : bombsList) {
            if (((Bomb) i).inRange(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)
                    && ((Bomb) i).getbombStatus() == Bomb.status.EXPLODED) {
                enemyStatus = EnemyStatus.DEAD;
            }
        }
        if (enemyStatus == EnemyStatus.ALIVE) {
            move();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (enemyStatus == EnemyStatus.ALIVE) {
            super.render(gc);
        }
    }
}
