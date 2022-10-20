package uet.oop.bomberman.controller;

import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Obstacle;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private Map map;
    private int fixHeight;
    private int fixWidth;

    public CollisionManager(Map map, int height, int width) {
        this.map = map;
        fixHeight = height;
        fixWidth = width;
    }

    public boolean touchObstacle(int x, int y, String dir, int speed) {
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
        Entity topLeft, downLeft, topRight, downRight;
        topLeft = map.getPosition(curX, curY);
        topRight = map.getPosition(curX + fixWidth, curY);
        downLeft = map.getPosition(curX, curY + fixHeight);
        downRight = map.getPosition(curX + fixWidth, curY + fixHeight);
        return topLeft instanceof Obstacle || topRight instanceof Obstacle
                || downLeft instanceof Obstacle || downRight instanceof Obstacle;
    }

    public Map getMap() {
        return map;
    }

    public List<Bomb> getBombList() {
        List<Bomb> result = new ArrayList<>();
        for (Bomb bomb: map.getBomber().bombManager) {
            result.add(bomb);
        }
        return result;
    }
}
