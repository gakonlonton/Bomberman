package uet.oop.bomberman.controller;

import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Obstacle;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

public class CollisionManager {
    private Map map;
    public static final int FIX_WIDTH = 6;
    public static final int FIX_HEIGHT = 5;

    public CollisionManager(Map map) {
        this.map = map;
    }

    public boolean touchObstacle(int x, int y, DIRECTION direction) {
        Entity object1, object2;
        switch (direction) {
            case UP:
                object1 = map.getPosition(x + FIX_WIDTH, y + FIX_HEIGHT);
                object2 = map.getPosition(x + 24 - FIX_WIDTH, y + FIX_HEIGHT);
                break;
            case DOWN:
                object1 = map.getPosition(x + FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                object2 = map.getPosition(x + 24 - FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                break;
            case LEFT:
                object1 = map.getPosition(x + FIX_WIDTH, y + FIX_HEIGHT);
                object2 = map.getPosition(x + FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                break;
            case RIGHT:
                object1 = map.getPosition(x + 24, y + FIX_HEIGHT);
                object2 = map.getPosition(x + 24, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                break;
            default:
                object1 = map.getPosition(x, y);
                object2 = map.getPosition(x, y);
                break;
        }
        if (object1 instanceof Obstacle || object2 instanceof Obstacle) {
            return true;
        }
        else return false;
    }
}
