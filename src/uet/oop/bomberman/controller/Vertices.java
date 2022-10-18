package uet.oop.bomberman.controller;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.Obstacle;

import java.util.List;

public class Vertices {
    private int xTilePos;
    private int yTilePos;

    public Vertices(int xTilePos, int yTilePos) {
        this.xTilePos = xTilePos;
        this.yTilePos = yTilePos;
    }

    public int getXTilePos() {
        return xTilePos;
    }

    public int getYTilePos() {
        return yTilePos;
    }

    public boolean isAVerticesInGraph(List<List<Entity>> map) {
        if (map.get(yTilePos).get(xTilePos) instanceof Grass) {
            if (!(map.get(yTilePos + 1).get(xTilePos) instanceof Obstacle
                    && map.get(yTilePos - 1).get(xTilePos) instanceof Obstacle
                    && map.get(yTilePos).get(xTilePos + 1) instanceof Grass
                    && map.get(yTilePos).get(xTilePos - 1) instanceof Grass)
            && !(map.get(yTilePos + 1).get(xTilePos) instanceof Grass
                    && map.get(yTilePos - 1).get(xTilePos) instanceof Grass
                    && map.get(yTilePos).get(xTilePos + 1) instanceof Obstacle
                    && map.get(yTilePos).get(xTilePos - 1) instanceof Obstacle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertices) {
            Vertices vertices = (Vertices) obj;
            if (vertices.getXTilePos() == xTilePos && vertices.getYTilePos() == yTilePos) return true;
        }
        return false;
    }
}
