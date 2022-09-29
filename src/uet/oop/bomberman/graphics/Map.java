package uet.oop.bomberman.graphics;

import uet.oop.bomberman.controller.CollisionManager;
import uet.oop.bomberman.controller.KeyboardEvent;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.Wall;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {
    protected List<List<Entity>> map;
    protected int mapHeight;
    protected int mapWidth;
    public List<Entity> entities = new ArrayList<>();

    public Map(int level, KeyboardEvent keyboardEvent) {
        map = new ArrayList<>();

        Path mapDir = Paths.get("").toAbsolutePath();
        File file = new File(mapDir.normalize().toString() + "/res/levels/Level" + level + ".txt");
        try {
            Scanner sc = new Scanner(file);
            mapHeight = sc.nextInt();
            mapWidth = sc.nextInt();
            sc.nextLine();
            /// Display Bomber
            Entity bomber = new Bomber(1, 1, Sprite.player_right.getFxImage(), keyboardEvent, new CollisionManager(this));
            entities.add(bomber);

            /// Display Map
            for (int i = 0; i < mapHeight; i++) {
                String tempLine = sc.nextLine();
                List<Entity> tempList = new ArrayList<>();
                for (int j = 0; j < mapWidth; j++) {
                    switch (tempLine.charAt(j)) {
                        case '#':
                            tempList.add(new Wall(j, i, Sprite.wall.getFxImage()));
                            break;
                        default:
                            tempList.add(new Grass(j, i, Sprite.grass.getFxImage()));
                            break;
                    }
                }
                map.add(tempList);
            }
            sc.close();
        }
        catch (Exception e) {
            System.out.println("File not found!");
        }
    }

    public List<List<Entity>> getMap() {
        return map;
    }
    public List<Entity> getEntities() {
        return entities;
    }
    public Entity getPosition(int x, int y) {
        int roundedX = Math.round(x / Sprite.SCALED_SIZE);
        int roundedY = Math.round(y / Sprite.SCALED_SIZE);
        return map.get(roundedY).get(roundedX);
    }

    public void entitiesUpdate() {
        entities.forEach(Entity::update);
    }
}
