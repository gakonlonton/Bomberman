package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.controller.Audio;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scene.IngameScene;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class Bomber extends EntityAnimation {
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
    public List<Bomb> bombManager = new ArrayList<>();
    private Audio audio;
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
        speed = 3;
        flameLength = 1;
        bombCount = 1;
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
        isRunning = false;
        boolean pressed = false;
        if (goUp) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y - speed, "UP")) {
                super.update(DIRECTION.UP, false);
            }
            else {
                super.update(DIRECTION.UP, true);
            }
            pickSprite(Sprite.movingSprite(Sprite.player_up,
                                        Sprite.player_up_1,
                                        Sprite.player_up_2, spriteIndex, 20).getFxImage());
        }
        if (goDown) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y + speed, "DOWN")) {
                super.update(DIRECTION.DOWN, false);
            }
            else {
                super.update(DIRECTION.DOWN, true);
            }
            pickSprite(Sprite.movingSprite(Sprite.player_down,
                                        Sprite.player_down_1,
                                        Sprite.player_down_2, spriteIndex, 20).getFxImage());
        }
        if (goLeft) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x - speed, y, "LEFT")) {
                super.update(DIRECTION.LEFT, false);
            }
            else {
                super.update(DIRECTION.LEFT, true);
            }
            pickSprite(Sprite.movingSprite(Sprite.player_left,
                                        Sprite.player_left_1,
                                        Sprite.player_left_2, spriteIndex, 20).getFxImage());
        }
        if (goRight) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x + speed, y, "RIGHT")) {
                super.update(DIRECTION.RIGHT, false);
            }
            else {
                super.update(DIRECTION.RIGHT, true);
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
                    bombManager.add(bomb);
                }
                placedBomb = false;
            }
        }
    }

    private void updateBombManager() {
        bombManager.forEach(Entity::update);
        if (!bombManager.isEmpty()) {
            if (((Bomb) bombManager.get(0)).getbombStatus() == Bomb.status.DISAPPEAR) {
                bombManager.remove(0);
            }
        }
    }

    public void updateBomberStatus() {
        for (Bomb e: bombManager) {
            if (e.inRange(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)
                && e.getbombStatus() == Bomb.status.EXPLODED) {
                status = bomberStatus.DEAD;
                spriteIndex = 0;
                return;
            }
        }

        for (int i = 1; i < GameMaster.entities.get(GameMaster.level).size(); i++) {
            if (((Enemy) GameMaster.entities.get(GameMaster.level).get(i)).touchBomber(x, y))
                status = bomberStatus.DEAD;
        }
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

    public void updateItemState() {
        Entity item = collisionManager.getMap().getPosition(x + 16, y + 16);
        if (item instanceof Item) {
            Item tmp = (Item) item;
            tmp.powerUp(this);
            int xTile = (x + 16) / Sprite.SCALED_SIZE;
            int yTile = (y + 16) / Sprite.SCALED_SIZE;
            audio.playParallel(Audio.AudioType.EAT_ITEM, 1);
            collisionManager.getMap().replace(xTile, yTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
        }
    }

    @Override
    public void update() {
        if (status == bomberStatus.ALIVE) {
            updatePosition();
            updateBomb();
            updateBombManager();
            updateItemState();
            updateBomberStatus();
        }
        if (status == bomberStatus.DEAD) {
            spriteIndex++;
            pickSprite(Sprite.movingSprite(Sprite.player_dead1,
                    Sprite.player_dead2,
                    Sprite.player_dead3, spriteIndex, 20).getFxImage());
            if (spriteIndex >= 20) {
                GameMaster.gameStatus = GameMaster.ingameSatus.LOSE;
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
