package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.util.Pair;
import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.Direction.DIRECTION;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static uet.oop.bomberman.BombermanGame.*;

public class Enemy extends EntityDestroyable {
    protected CollisionManager collisionManager;
    public int sizeCollision;

    /*
        Decode Entity
     */
    public final static int OBSTACLE = 0;
    public final static int GRASS = 1;
    public final static int BOMBER = 2;
    public final static int ENEMY = 3;

    /*
        Enemy buff
     */
    protected boolean invinsible = false;

    public Enemy(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
    }

    public boolean goLeft() {
        if (collisionManager.touchObstacle(x - sizeCollision, y, 1)) {
            return false;
        }
        else {
            super.update(DIRECTION.LEFT, true, sizeCollision);
            return true;
        }
    }

    public boolean goRight() {
        if (collisionManager.touchObstacle(x + sizeCollision, y, 1)) {
            return false;
        }
        else {
            super.update(DIRECTION.RIGHT, true, sizeCollision);
            return true;
        }
    }

    public boolean goUp() {
        if (collisionManager.touchObstacle(x, y - sizeCollision, 1)) {
            return false;
        }
        else {
            super.update(DIRECTION.UP, true, sizeCollision);
            return true;
        }
    }

    public boolean goDown() {
        if (collisionManager.touchObstacle(x, y + sizeCollision, 1)) {
            return false;
        }
        else {
            super.update(DIRECTION.DOWN, true, sizeCollision);
            return true;
        }
    }

    public void goRandom() {
        int rand = (int) (Math.random() * 4);
        switch (rand) {
            case 0:
                if(goLeft()) return;
                if(goUp()) return;
                if(goRight()) return;
                if(goDown()) return;
                break;
            case 1:
                if(goRight()) return;
                if(goDown()) return;
                if(goLeft()) return;
                if(goUp()) return;
                break;
            case 2:
                if(goUp()) return;
                if(goRight()) return;
                if(goDown()) return;
                if(goLeft()) return;
                break;
            case 3:
                if(goDown()) return;
                if(goRight()) return;
                if(goLeft()) return;
                if(goUp()) return;
                break;
            default:
                break;
        }
    }

    protected DIRECTION getDirectWithDijkstra(List<List<Integer>> graph, int height, int width, int xBomber, int yBomber) {
        int moddedX = Math.round(x / Sprite.SCALED_SIZE);
        int moddedY = Math.round(y  / Sprite.SCALED_SIZE);
        int dist[][] = new int[height][width];
        List<Integer> parent = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                dist[i][j] = Integer.MAX_VALUE / 2;
                parent.add(0);
            }
        }
        PriorityQueue< Pair<Integer, Pair<Integer, Integer>> > pq = new PriorityQueue<Pair<Integer, Pair<Integer, Integer>>>((x, y) -> x.getKey() - y.getKey());
        pq.add(new Pair<>((Math.abs(xBomber - moddedX) + Math.abs(yBomber - moddedY)), new Pair<>(yBomber, xBomber)));
        dist[yBomber][xBomber] = 0;
        while (!pq.isEmpty()) {
            Pair<Integer, Pair<Integer, Integer>> top = pq.poll();
            int x = top.getValue().getValue();
            int y = top.getValue().getKey();
            int du = -top.getKey();
            if (graph.get(y).get(x) == ENEMY) break;
            if (dist[y][x] != du) continue;
            if (x - 1 >= 0 && graph.get(y).get(x - 1) != OBSTACLE) {
                int w = Math.abs(y - moddedY) + Math.abs(x - 1 - moddedX);
                int dv = dist[y][x - 1] + w;
                if (dv > du) {
                    dist[y][x - 1] = dist[y][x] + 1;
                    dv = dist[y][x - 1] + w;
                    parent.set((x - 1) * height + y, x * height + y);
                    pq.add(new Pair<>(dv, new Pair<>(y, x - 1)));
                }
            }
            if(x + 1 < width && graph.get(y).get(x + 1) != OBSTACLE){
                int w = Math.abs(y - moddedY) + Math.abs(x + 1 - moddedX);
                int dv = dist[y][x + 1] + w;
                if(dv > du){
                    dist[y][x + 1] = dist[y][x] + 1;
                    dv = dist[y][x + 1] + w;
                    parent.set((x + 1) * height + y, x * height + y);
                    pq.add(new Pair<>(dv, new Pair<>(y, x + 1)));
                }
            }
            if(y + 1 < height && graph.get(y + 1).get(x) != OBSTACLE){
                int w = Math.abs(y + 1 - moddedY) + Math.abs(x - moddedX);
                int dv = dist[y + 1][x] + w;
                if(dv > du){
                    dist[y + 1][x] = dist[y][x] + 1;
                    dv = dist[y + 1][x] + w;
                    parent.set(x * height + y + 1, x * height + y);
                    pq.add(new Pair<>(dv, new Pair<>(y + 1, x)));
                }
            }
            if(y - 1 >= 0 && graph.get(y - 1).get(x) != OBSTACLE){
                int w = Math.abs(y - 1 - moddedY) + Math.abs(x - moddedX);
                int dv = dist[y - 1][x] + w;
                if(dv > du){
                    dist[y - 1][x] = dist[y][x] + 1;
                    dv = dist[y - 1][x] + w;
                    parent.set(x * height + y - 1, x * height + y);
                    pq.add(new Pair<>(dv, new Pair<>(y - 1, x)));
                }
            }
        }
        if (dist[moddedY][moddedX] == 0 || dist[moddedY][moddedX] == Integer.MAX_VALUE / 2) {
            return DIRECTION.CENTER;
        }
        int nextStep = parent.get(moddedX * height + moddedY);
        int newX = (int) (nextStep / height);
        int newY = nextStep % height;
        if (newX - moddedX == 1) return DIRECTION.RIGHT;
        if (newX - moddedX == -1) return DIRECTION.LEFT;
        if (newY - moddedY == 1) return DIRECTION.DOWN;
        if (newY - moddedY == -1) return DIRECTION.UP;
        return DIRECTION.CENTER;
    }


    @Override
    public void disapear() {
        death = true;
    }
}
