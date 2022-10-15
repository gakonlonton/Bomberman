package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class EnemyOneal extends Enemy {
    private int spriteIndex = 0;
    private List<List<Entity>> map;
    private Bomber bomber;

    public EnemyOneal(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img, collisionManager);
        this.speed = 2;
        this.map = collisionManager.getMap().getMap();
        this.bomber = collisionManager.getMap().getBomber();
    }

    public void die() {
        if (!death) {
            death = true;
            spriteIndex = 0;
        }
    }

    public void pickSprite(Image img) {
        this.img = img;
    }

    public void update() {
        spriteIndex++;
        if (death) return;
        if (spriteIndex % 2 == 0) return;
        if (spriteIndex % 32 == 1) {
            int moddedX = Math.round(bomber.x / Sprite.SCALED_SIZE);
            int moddedY = Math.round(bomber.y / Sprite.SCALED_SIZE);
            List<List<Integer>> graph = formatGraph(map, moddedX, moddedY);
            direction = getDirectWithDijkstra(graph, map.size(), map.get(0).size(), moddedY, moddedX);
            if (direction == DIRECTION.CENTER) {
                sizeCollision = speed;
                goRandom();
                return;
            }
        }
        sizeCollision = speed;
        if (direction == DIRECTION.LEFT) {
            super.update(DIRECTION.LEFT, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.oneal_left1,
                    Sprite.oneal_left2,
                    Sprite.oneal_left3, spriteIndex, 20).getFxImage());
        }
        else if (direction == DIRECTION.RIGHT) {
            super.update(DIRECTION.RIGHT, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.oneal_right1,
                    Sprite.oneal_right2,
                    Sprite.oneal_right3, spriteIndex, 20).getFxImage());
        }
        else if (direction == DIRECTION.DOWN) {
            super.update(DIRECTION.DOWN, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.oneal_left1,
                    Sprite.oneal_left2,
                    Sprite.oneal_left3, spriteIndex, 20).getFxImage());
        }
        else if (direction == DIRECTION.UP) {
            super.update(DIRECTION.UP, true, this.speed);
            pickSprite(Sprite.movingSprite(Sprite.oneal_right1,
                    Sprite.oneal_right2,
                    Sprite.oneal_right3, spriteIndex, 20).getFxImage());
        }
    }

    protected List<List<Integer>> formatGraph(List<List<Entity>> map, int xBomber, int yBomber) {
        List<List<Integer>> graph = new ArrayList<>();
        int height = map.size();
        int width = map.get(0).size();
        for (int i = 0; i < height; i++) {
            graph.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                if (map.get(i).get(j) instanceof Wall || map.get(i).get(j) instanceof Brick) {
                    graph.get(i).add(OBSTACLE);
                }
                else {
                    graph.get(i).add(GRASS);
                }
            }
        }
        int moddedX = Math.round(x  / Sprite.SCALED_SIZE);
        int moddedY = Math.round(y  / Sprite.SCALED_SIZE);
        graph.get(yBomber).set(xBomber, BOMBER);
        graph.get(moddedY).set(moddedX, ENEMY);
        return graph;
    }


}
