package uet.oop.bomberman.controller;

import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Obstacle;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private Map map;
    public static final int FIX_WIDTH = 5;
    public static final int FIX_HEIGHT = 4;

    public CollisionManager(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public boolean touchObstacle(int x, int y) {
        Entity topLeft, downLeft, topRight, downRight;
        topLeft = map.getPosition(x, y);
        topRight = map.getPosition(x + 20, y);
        downLeft = map.getPosition(x, y + 29);
        downRight = map.getPosition(x + 20, y + 29);
        return topLeft instanceof Obstacle || topRight instanceof Obstacle
                || downLeft instanceof Obstacle || downRight instanceof Obstacle;
    }

    public boolean enemyTouchObstacle(int x, int y) {
        Entity topLeft, downLeft, topRight, downRight;
        topLeft = map.getPosition(x, y);
        topRight = map.getPosition(x + 30, y);
        downLeft = map.getPosition(x, y + 30);
        downRight = map.getPosition(x + 30, y + 30);
        return topLeft instanceof Obstacle || topRight instanceof Obstacle
                || downLeft instanceof Obstacle || downRight instanceof Obstacle;
    }

    public boolean touchBomb(int x, int y) {
        return false;
    }

    public Entity getEntityAt(int x, int y) {
        return map.getMap().get(y).get(x);
    }
}
