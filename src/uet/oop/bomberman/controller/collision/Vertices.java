package uet.oop.bomberman.controller.collision;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.obstacle.Grass;
import uet.oop.bomberman.entities.obstacle.Obstacle;

import java.util.List;

public class Vertices {
    private final int xTilePos;
    private final int yTilePos;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertices) {
            Vertices vertices = (Vertices) obj;
            if (vertices.getXTilePos() == xTilePos && vertices.getYTilePos() == yTilePos) return true;
        }
        return false;
    }
}
