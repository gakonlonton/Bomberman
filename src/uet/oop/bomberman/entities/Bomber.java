package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
    private Map map;
    private boolean placedBomb = false, goUp = false, goDown = false, goLeft = false, goRight = false;
    private KeyCode lastKey = KeyCode.D;
    public List<Bomb> bombManager = new ArrayList<>();
    private int spriteIndex = 0;

    public Bomber(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
        this.map = collisionManager.getMap();
        status = bomberStatus.ALIVE;
    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    @Override
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

    @Override
    public void update() {
        if (status == bomberStatus.ALIVE) {
            updatePosition();
            updateBomb();
            updateBombManager();
            updateBomberStatus();
            updateExplosion();
        }
        if (status == bomberStatus.DEAD) {
            spriteIndex++;
            pickSprite(Sprite.movingSprite(Sprite.player_dead1,
                                        Sprite.player_dead2,
                                        Sprite.player_dead3, spriteIndex, 20).getFxImage());
            if (spriteIndex >= 20) {
                GameMaster.gameStatus = GameMaster.ingameSatus.LOBBY;
                return;
            }
        }
    }

    private void updatePosition() {
        isRunning = false;
        boolean pressed = false;
        if (goUp) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y - speed, 0)) {
                super.update(DIRECTION.UP, false, speed);
                spriteIndex = 0;
            }
            else super.update(DIRECTION.UP, true, speed);
            pickSprite(Sprite.movingSprite(Sprite.player_up,
                                        Sprite.player_up_1,
                                        Sprite.player_up_2, spriteIndex, 20).getFxImage());
        }
        if (goDown) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x, y + speed, 0)) {
                super.update(DIRECTION.DOWN, false, speed);
                spriteIndex = 0;
            }
            else super.update(DIRECTION.DOWN, true, speed);
            pickSprite(Sprite.movingSprite(Sprite.player_down,
                                        Sprite.player_down_1,
                                        Sprite.player_down_2, spriteIndex, 20).getFxImage());
        }
        if (goLeft) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x - speed, y, 0)) {
                super.update(DIRECTION.LEFT, false, speed);
                spriteIndex = 0;
            }
            else super.update(DIRECTION.LEFT, true, speed);
            pickSprite(Sprite.movingSprite(Sprite.player_left,
                                        Sprite.player_left_1,
                                        Sprite.player_left_2, spriteIndex, 20).getFxImage());
        }
        if (goRight) {
            pressed = true;
            spriteIndex++;
            if (collisionManager.touchObstacle(x + speed, y, 0)) {
                super.update(DIRECTION.RIGHT, false, speed);
                spriteIndex = 0;
            }
            else super.update(DIRECTION.RIGHT, true, speed);
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
            int _x = (x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int _y = (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            Bomb bomb = new Bomb(_x, _y, Sprite.bomb.getFxImage(), collisionManager);
            bombManager.add(bomb);
            placedBomb = false;
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
    }

    public void updateExplosion() {
        for (int i = 0; i < bombManager.size(); i++) {
            Bomb bomb = bombManager.get(i);
            if (bomb.getBombStatus() == Bomb.status.EXPLODED) {
                int xTile = bomb.x / Sprite.SCALED_SIZE;
                int yTile = bomb.y / Sprite.SCALED_SIZE;
                Entity tmp;
                for (int dir = 0; dir < 4; dir++) {
                    switch (dir) {
                        case 0:
                            tmp = map.getMap().get(yTile - 1).get(xTile);
                            if (tmp instanceof Brick) {
                                map.replace(xTile, yTile - 1, new Grass(xTile, yTile - 1, Sprite.grass.getFxImage()));
                            }
                            break;
                        case 1:
                            tmp = map.getMap().get(yTile + 1).get(xTile);
                            if (tmp instanceof Brick) {
                                map.replace(xTile, yTile + 1, new Grass(xTile, yTile + 1, Sprite.grass.getFxImage()));
                            }
                            break;
                        case 2:
                            tmp = map.getMap().get(yTile).get(xTile - 1);
                            if (tmp instanceof Brick) {
                                map.replace(xTile - 1, yTile, new Grass(xTile - 1, yTile, Sprite.grass.getFxImage()));
                            }
                            break;
                        case 3:
                            tmp = map.getMap().get(yTile).get(xTile + 1);
                            if (tmp instanceof Brick) {
                                map.replace(xTile + 1, yTile, new Grass(xTile + 1, yTile, Sprite.grass.getFxImage()));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
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
