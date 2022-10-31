package uet.oop.bomberman.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.entities.bomber.Bomb;
import uet.oop.bomberman.entities.bomber.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.EntityDestroyable;
import uet.oop.bomberman.graphics.sprite.Sprite;

import javax.swing.text.html.HTMLDocument;

import java.util.ConcurrentModificationException;

import static uet.oop.bomberman.controller.GameMaster.bombsList;
import static uet.oop.bomberman.controller.GameMaster.level;

public abstract class Enemy extends EntityDestroyable {
    public int spriteIndex = 0;
    public enum EnemyStatus {
        ALIVE,
        LAST,
        DEAD
    }
    EnemyStatus enemyStatus;
    public String dir = "";
    protected CollisionManager collisionManager;
    protected Sprite[] leftSprites = new Sprite[3];
    protected Sprite[] rightSprites = new Sprite[3];
    protected Sprite[] deadSprites = new Sprite[1];

    public Enemy(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
        enemyStatus = EnemyStatus.ALIVE;
        this.speed = 1;
        loadSprite();
    }

    public boolean goNext = false;

    private void loadSprite() {
        if (this instanceof Balloon) {
            leftSprites[0] = Sprite.balloom_left1;
            leftSprites[1] = Sprite.balloom_left2;
            leftSprites[2] = Sprite.balloom_left3;
            rightSprites[0] = Sprite.balloom_right1;
            rightSprites[1] = Sprite.balloom_right2;
            rightSprites[2] = Sprite.balloom_right3;
            deadSprites[0] = Sprite.balloom_dead;
        }
        if (this instanceof Oneal) {
            leftSprites[0] = Sprite.oneal_left1;
            leftSprites[1] = Sprite.oneal_left2;
            leftSprites[2] = Sprite.oneal_left3;
            rightSprites[0] = Sprite.oneal_right1;
            rightSprites[1] = Sprite.oneal_right2;
            rightSprites[2] = Sprite.oneal_right3;
            deadSprites[0] = Sprite.oneal_dead;
        }
        if (this instanceof Doll) {
            leftSprites[0] = Sprite.doll_left1;
            leftSprites[1] = Sprite.doll_left2;
            leftSprites[2] = Sprite.doll_left3;
            rightSprites[0] = Sprite.doll_right1;
            rightSprites[1] = Sprite.doll_right2;
            rightSprites[2] = Sprite.doll_right3;
            deadSprites[0] = Sprite.doll_dead;
        }
        if (this instanceof Duplicate) {
            leftSprites[0] = Sprite.minvo_left1;
            leftSprites[1] = Sprite.minvo_left2;
            leftSprites[2] = Sprite.minvo_left3;
            rightSprites[0] = Sprite.minvo_right1;
            rightSprites[1] = Sprite.minvo_right2;
            rightSprites[2] = Sprite.minvo_right3;
            deadSprites[0] = Sprite.minvo_dead;
        }
    }

    public void goRandom() {
        spriteIndex++;
        if (!goNext) {
            int rand = (int) (Math.random() * 16);
            switch (rand % 4) {
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
        if (collisionManager.touchObstacle(x, y, dir, speed) || touchBomb(x, y, dir)) {
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

    public boolean touchBomb(int x, int y, String dir) {
        int curX = x, curY = y;
        switch (dir) {
            case "UP":
                curY -= speed;
                break;
            case "DOWN":
                curY += speed;
                break;
            case "LEFT":
                curX -= speed;
                break;
            case "RIGHT":
                curX += speed;
                break;
            default:
                break;
        }
        int xTile = curX / Sprite.SCALED_SIZE;
        int yTile = curY / Sprite.SCALED_SIZE;
        int xWidth = (curX + Bomber.WIDTH) / Sprite.SCALED_SIZE;
        int yHeight = (curY + Bomber.HEIGHT) / Sprite.SCALED_SIZE;
        for (Bomb bomb: bombsList) {
            int xBomb = (bomb.x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int yBomb = (bomb.y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            if ((xTile == xBomb && yTile == yBomb)
                    || (xWidth == xBomb && yTile == yBomb)
                    || (xTile == xBomb && yHeight == yBomb)
                    || (xWidth == xBomb && yHeight == yBomb)) {
                return true;
            }
        }
        return false;
    }

    public void randomSpeed(int L, int R) {
        speed = (int) ((Math.random() * (R - L)) + L);
    }

    public boolean touchBomber(int xTile, int yTile) {
        boolean check = true;
        if (xTile + Bomber.WIDTH < x || xTile > x + Sprite.SCALED_SIZE) check = false;
        if (yTile + Bomber.HEIGHT < y || yTile > y + Sprite.SCALED_SIZE) check = false;
        return check;
    }

    @Override
    public void update() {
        if (enemyStatus == EnemyStatus.ALIVE) {
            for (Entity i : bombsList) {
                if (((Bomb) i).inRange(x + 30 / 2, y + 30 / 2)
                        && ((Bomb) i).getBombStatus() == Bomb.status.EXPLODED) {
                    enemyStatus = EnemyStatus.LAST;
                    spriteIndex = 0;
                }
            }
        }
        if (enemyStatus == EnemyStatus.ALIVE) {
            if (this instanceof Balloon && spriteIndex >= 20) {
                ((Balloon) this).invincible = false;
            }
            move();
        }
        if (enemyStatus == EnemyStatus.LAST) {
            // Die for duplicate
            if (this instanceof Duplicate) {
                if (spriteIndex >= 20) {
                    enemyStatus = EnemyStatus.DEAD;
                    Balloon e1 = new Balloon(x / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE,
                                    Sprite.balloom_left1.getFxImage(),
                                    new CollisionManager(collisionManager.getMap(), Balloon.HEIGHT, Balloon.WIDTH));
                    Balloon e2 = new Balloon(x / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE,
                                    Sprite.balloom_right1.getFxImage(),
                                    new CollisionManager(collisionManager.getMap(), Balloon.HEIGHT, Balloon.WIDTH));
                    GameMaster.entities.get(level).add(e1);
                    GameMaster.entities.get(level).add(e2);
                    e1.invincible = true;
                    e2.invincible = true;
                    spriteIndex = 0;
                } else {
                    pickSprite(deadSprites[spriteIndex % deadSprites.length].getFxImage());
                }
                spriteIndex++;
            } else if (this instanceof Balloon) {
                if (!((Balloon) this).invincible) {
                    if (spriteIndex >= 20) {
                        enemyStatus = EnemyStatus.DEAD;
                        spriteIndex = 0;
                    } else {
                        pickSprite(deadSprites[spriteIndex % deadSprites.length].getFxImage());
                    }
                }
                spriteIndex++;
            }
            else {
                if (spriteIndex >= 20) {
                    enemyStatus = EnemyStatus.DEAD;
                    spriteIndex = 0;
                } else {
                    pickSprite(deadSprites[spriteIndex % deadSprites.length].getFxImage());
                }
                spriteIndex++;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (enemyStatus != EnemyStatus.DEAD) {
            super.render(gc);
        }
    }
}
