package uet.oop.bomberman.graphics;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.controller.Timer;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Explosion;
import uet.oop.bomberman.entities.Grass;

import java.util.ArrayList;
import java.util.List;

public class BombManager {
    private List<Bomb> bombs = new ArrayList<>();
    private CollisionManager collisionManager;
    public List<Explosion> explosions = new ArrayList<>();
    private Map map;
    private int flame = 1;
    private int bombNum = 1;
    private long bombDelay;


    public BombManager(CollisionManager collisionManager) {
        this.map = collisionManager.getMap();
        this.collisionManager = collisionManager;
        bombDelay = Timer.now();
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        /// add sound here
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void update() {
        int i = 0;
        while(i < explosions.size()) {
            explosions.get(i).update();
            if (explosions.get(i).isExploded()) {
                explosions.remove(i);
                i--;
                continue;
            }
            i++;
        }
        for (int j = 0; j < bombs.size(); j++) {
            bombs.get(j).update();
            if (bombs.get(j).isExploded()) {
                Bomb bomb = bombs.get(j);
                int _x = bomb.getX() / Sprite.SCALED_SIZE;
                int _y = bomb.getY() / Sprite.SCALED_SIZE;
                explosions.add(new Explosion(_x, _y, Sprite.bomb_exploded2.getFxImage(), DIRECTION.CENTER, Explosion.EXPLOSION_STATE.MIDDLE, collisionManager));
            }
        }
    }

    public boolean valid(int _x, int _y) {
        long cur = Timer.now();
        if (cur - bombDelay > Timer.TIME_FOR_DELAY_BOMB) {
            bombDelay = cur;
            if (bombs.size() < bombNum) {
                for (Bomb bomb: bombs) {
                    if (bomb.getX() / Sprite.SCALED_SIZE == _x
                        && bomb.getY() / Sprite.SCALED_SIZE == _y) {
                        return false;
                    }
                }
                if (collisionManager.getEntityAt(_x, _y) instanceof Grass) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else return false;
    }

    public void renderBomb(GraphicsContext gc) {
        for (Bomb bomb: bombs) {
            bomb.render(gc);
        }
        for (Explosion explosion: explosions) {
            explosion.render(gc);
        }
    }

    public int getFlame() {
        return flame;
    }
}
