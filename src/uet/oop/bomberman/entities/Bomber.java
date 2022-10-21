package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;
import uet.oop.bomberman.controller.Audio;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.graphics.Sprite;

import java.util.LinkedList;
import java.util.List;
import static uet.oop.bomberman.controller.GameMaster.*;

public class Bomber extends EntityDestroyable {
    public static final int HEIGHT = 30;
    public static final int WIDTH = 20;
    enum bomberStatus {
        ALIVE, DEAD
    }
    bomberStatus status;
    private CollisionManager collisionManager;
    private boolean placedBomb = false;
    private boolean goUp = false, goDown = false, goLeft = false, goRight = false;
    private KeyCode lastKey = KeyCode.D;
    public List<Bomb> bombManager = new LinkedList<>();
    private Audio audio = new Audio();
    private int spriteIndex = 0;

    /*
        Bomber stats
     */
    public static int speed;
    public static int flameLength;
    public static int bombCount;

    /*
        Constructor
     */

    public Bomber(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
        status = bomberStatus.ALIVE;
        // reset stats
        speed = 2;
        flameLength = 1;
        bombCount = 1;
        pickSprite(Sprite.player_right.getFxImage());
    }

    public void isPressed(KeyCode keyCode, boolean isPress) {
        switch (keyCode) {
            case W:
                goUp = isPress;
                lastKey = keyCode;
                break;
            case S:
                goDown = isPress;
                lastKey = keyCode;
                break;
            case A:
                goLeft = isPress;
                lastKey = keyCode;
                break;
            case D:
                goRight = isPress;
                lastKey = keyCode;
                break;
            case SPACE:
                placedBomb = isPress;
                break;
            default:
                break;
        }
        if (isPress == false) {
            spriteIndex = 0;
        }
    }

    private void updatePosition() {
        boolean pressed = false;
        if (goUp) {
            pressed = true;
            spriteIndex++;
            if (!collisionManager.touchObstacle(x, y, "UP", speed) && !collideWithBomb(x, y, "UP")) {
                y -= speed;
            }
            pickSprite(Sprite.movingSprite(Sprite.player_up,
                                        Sprite.player_up_1,
                                        Sprite.player_up_2, spriteIndex, 20).getFxImage());
        }
        if (goDown) {
            pressed = true;
            spriteIndex++;
            if (!collisionManager.touchObstacle(x, y, "DOWN", speed) && !collideWithBomb(x, y, "DOWN")) {
                y += speed;
            }
            pickSprite(Sprite.movingSprite(Sprite.player_down,
                                        Sprite.player_down_1,
                                        Sprite.player_down_2, spriteIndex, 20).getFxImage());
        }
        if (goLeft) {
            pressed = true;
            spriteIndex++;
            if (!collisionManager.touchObstacle(x, y, "LEFT", speed) && !collideWithBomb(x, y, "LEFT")) {
                x -= speed;
            }
            pickSprite(Sprite.movingSprite(Sprite.player_left,
                                        Sprite.player_left_1,
                                        Sprite.player_left_2, spriteIndex, 20).getFxImage());
        }
        if (goRight) {
            pressed = true;
            spriteIndex++;
            if (!collisionManager.touchObstacle(x, y, "RIGHT", speed) && !collideWithBomb(x, y, "RIGHT")) {
                x += speed;
            }
            pickSprite(Sprite.movingSprite(Sprite.player_right,
                                        Sprite.player_right_1,
                                        Sprite.player_right_2, spriteIndex, 20).getFxImage());
        }
        if (placedBomb) {
            pressed = true;
        }
        if (!pressed) {
            spriteIndex = 0;
            switch (lastKey) {
                case W:
                    pickSprite(Sprite.player_up.getFxImage());
                    break;
                case S:
                    pickSprite(Sprite.player_down.getFxImage());
                    break;
                case A:
                    pickSprite(Sprite.player_left.getFxImage());
                    break;
                case D:
                    pickSprite(Sprite.player_right.getFxImage());
                    break;
                default:
                    break;
            }
        }
    }

    private Pair<Integer, Integer> lastBomb;

    public void updateBomb() {
        if (placedBomb) {
            if (bombManager.size() < bombCount) {
                int _x = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
                int _y = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
                Bomb bomb = new Bomb(_x, _y, Sprite.bomb.getFxImage(), collisionManager);
                boolean hasBomb = false;
                for (Entity i : bombManager) {
                    if (i.x == bomb.x && i.y == bomb.y) {
                        hasBomb = true;
                        break;
                    }
                }
                if (!hasBomb) {
                    lastBomb = new Pair<>(_x, _y);
                    bombManager.add(bomb);
                }
                placedBomb = false;
            }
        }
    }

    private void updateBombManager() {
        bombManager.forEach(Entity::update);
        if (!bombManager.isEmpty()) {
            if (((Bomb) bombManager.get(0)).getBombStatus() == Bomb.status.DISAPPEAR) {
                bombManager.remove(0);
            }
        }
    }

    public void updateBomberStatus() {
        for (Bomb e: bombManager) {
            if (e.inRange(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)
                && e.getBombStatus() == Bomb.status.EXPLODED) {
                status = bomberStatus.DEAD;
                spriteIndex = 0;
                return;
            }
        }

        for (int i = 1; i < GameMaster.entities.get(GameMaster.level).size(); i++) {
            if (((Enemy) GameMaster.entities.get(GameMaster.level).get(i)).touchBomber(x, y)) {
                status = bomberStatus.DEAD;
                spriteIndex = 0;
                break;
            }
        }
    }

    public void updateItemState() {
        Entity item = collisionManager.getMap().getPosition(x + 16, y + 16);
        int Bomber_xPixel = entities.get(level).get(0).getX();
        int Bomber_yPixel = entities.get(level).get(0).getY();
        if (item instanceof Item) {
            Item tmp = (Item) item;
            tmp.update();
            int xTile = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int yTile = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            audio.playParallel(Audio.AudioType.EAT_ITEM, 1);
            collisionManager.getMap().replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
        }
    }

    public boolean collideWithBomb(int x, int y, String dir) {
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
        int xTile = curX / Sprite.SCALED_SIZE;
        int yTile = curY / Sprite.SCALED_SIZE;
        int xWidth = (curX + WIDTH) / Sprite.SCALED_SIZE;
        int yHeight = (curY + HEIGHT) / Sprite.SCALED_SIZE;
        boolean result = false, touchLastBomb = false;
        for (int i = 0; i < bombManager.size(); i++) {
            Bomb bomb = (Bomb) bombManager.get(i);
            int xBomb = (bomb.x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int yBomb = (bomb.y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            if ((xTile == xBomb && yTile == yBomb)
                || (xWidth == xBomb && yTile == yBomb)
                || (xTile == xBomb && yHeight == yBomb)
                || (xWidth == xBomb && yHeight == yBomb)) {
                if (lastBomb != null) {
                    if (xBomb == lastBomb.getKey() && yBomb == lastBomb.getValue()) {
                        result = false;
                        touchLastBomb = true;
                    } else {
                        if (i == bombManager.size() - 2) {
                            return false;
                        }
                        result = true;
                        break;
                    }
                }
                else {
                    result = true;
                    break;
                }
            }
        }
        if (!touchLastBomb) {
            lastBomb = null;
        }
        return result;
    }

    @Override
    public void update() {
        if (status == bomberStatus.ALIVE) {
            updatePosition();
            updateBomb();
            updateBomberStatus();
            updateBombManager();
            updateItemState();
        }
        if (status == bomberStatus.DEAD) {
            spriteIndex++;
            pickSprite(Sprite.movingSprite(Sprite.player_dead1,
                    Sprite.player_dead2,
                    Sprite.player_dead3, spriteIndex, 20).getFxImage());
            if (spriteIndex >= 20) {
                GameMaster.gameStatus = GameMaster.inGameStatus.LOSE;
            }
            speed = 2;
            bombCount = 1;
            flameLength = 1;
        }
    }
    @Override
    public void render(GraphicsContext gc) {
        if (status == bomberStatus.ALIVE) {
            for (Entity e: bombManager) {
                e.render(gc);
            }
            super.render(gc);
        }
        if (status == bomberStatus.DEAD) {
            super.render(gc);
        }
    }
}
