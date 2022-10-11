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

    public CollisionManager(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public boolean touchObstacle(int x, int y, int type) {
        int fixHeight = 0, fixWidth = 0;
        switch (type) {
            case 0:
                fixHeight = 20;
                fixWidth = 29;
                break;
            case 1:
                fixHeight = 30;
                fixWidth = 30;
                break;
            default:
                break;
        }
        Entity topLeft, downLeft, topRight, downRight;
        topLeft = map.getPosition(x, y);
        topRight = map.getPosition(x + fixHeight, y);
        downLeft = map.getPosition(x, y + fixWidth);
        downRight = map.getPosition(x + fixHeight, y + fixWidth);
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
