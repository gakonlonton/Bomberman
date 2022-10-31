package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.obstacle.Wall;
import uet.oop.bomberman.graphics.sprite.Sprite;

public class Doll extends Enemy {
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;
    private int totalStep = 0;

    public Doll(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
    }

    @Override
    public void goRandom() {
        spriteIndex++;
        if (totalStep == 64) {
            totalStep = 0;
            goNext = false;
        }
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
        if (touchWall(x, y, dir, speed) || touchBomb(x, y, dir)) {
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
            totalStep++;
            goNext = true;
        }
    }

    private boolean touchWall(int x, int y, String dir, int speed) {
        int curX = x;
        int curY = y;
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
        Entity topLeft = GameMaster.mapList.get(GameMaster.level).getPosition(curX, curY);
        Entity topRight = GameMaster.mapList.get(GameMaster.level).getPosition(curX + WIDTH, curY);
        Entity downLeft = GameMaster.mapList.get(GameMaster.level).getPosition(curX, curY + HEIGHT);
        Entity downRight = GameMaster.mapList.get(GameMaster.level).getPosition(curX + WIDTH, curY + HEIGHT);
        return topLeft instanceof Wall || topRight instanceof Wall
                || downLeft instanceof Wall || downRight instanceof Wall;
    }

    @Override
    public void move() {
        goRandom();
    }
}
