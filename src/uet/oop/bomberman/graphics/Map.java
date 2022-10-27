package uet.oop.bomberman.graphics;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.controller.collision.Graph;
import uet.oop.bomberman.controller.collision.Vertices;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.bomber.Bomber;
import uet.oop.bomberman.entities.enemies.*;
import uet.oop.bomberman.entities.items.ItemBomb;
import uet.oop.bomberman.entities.items.ItemFlame;
import uet.oop.bomberman.entities.items.ItemPortal;
import uet.oop.bomberman.entities.items.ItemSpeed;
import uet.oop.bomberman.entities.obstacle.Brick;
import uet.oop.bomberman.entities.obstacle.Grass;
import uet.oop.bomberman.entities.obstacle.Wall;
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {
    private int WIDTH;
    private int HEIGHT;

    private int level;
    protected int[][] itemList;
    protected List<List<Entity>> map = new ArrayList<>();
    private Graph graph;

    public Map(int level) {
        this.level = level;
        mapReader();
        convertToGraph();
    }

    /*
        Read map from level files
     */

    public void mapReader() {
        Path mapDir = Paths.get("").toAbsolutePath();
        File file = new File(mapDir.normalize().toString() + "/res/levels/Level" + (level + 1) + ".txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.out.println("Map file not found!");
        }
        String tempLine = "";
        tempLine = sc.nextLine();
        String[] tmp = tempLine.split(" ");
        HEIGHT = Integer.parseInt(tmp[1]);
        WIDTH = Integer.parseInt(tmp[2]);
        itemList = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            tempLine = sc.nextLine();
            map.add(new ArrayList<>());
            for (int j = 0; j < WIDTH; j++) {
                switch (tempLine.charAt(j)) {
                    case 'p':
                        map.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Entity bomber = new Bomber(j, i, Sprite.player_right.getFxImage(),
                                                    new CollisionManager(this, Bomber.HEIGHT, Bomber.WIDTH));
                        GameMaster.entities.get(level).add(bomber);
                        break;
                    case '#':
                        map.get(i).add(new Wall(j, i, Sprite.wall.getFxImage()));
                        break;
                    case '*':
                        map.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case '1':
                        map.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy balloon = new Balloon(j, i, Sprite.balloom_left1.getFxImage(),
                                                        new CollisionManager(this, Balloon.HEIGHT, Balloon.WIDTH));
                        GameMaster.entities.get(level).add(balloon);
                        break;
                    case '2':
                        map.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage(),
                                                    new CollisionManager(this, Oneal.HEIGHT, Oneal.WIDTH), GameMaster.entities.get(level).get(0));
                        GameMaster.entities.get(level).add(oneal);
                        break;
                    case '3':
                        map.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy doll = new Doll(j, i, Sprite.doll_left1.getFxImage(),
                                                    new CollisionManager(this, Doll.HEIGHT, Doll.WIDTH));
                        GameMaster.entities.get(level).add(doll);
                        break;
                    case '4':
                        map.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy duplicate = new Duplicate(j, i, Sprite.minvo_left1.getFxImage(),
                                new CollisionManager(this, Duplicate.HEIGHT, Duplicate.WIDTH), GameMaster.entities.get(level).get(0));
                        GameMaster.entities.get(level).add(duplicate);
                        break;
                    case 'x':
                        map.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        itemList[i][j] = ItemPortal.code;
                        break;
                    case 'b':
                        map.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        itemList[i][j] = ItemBomb.code;
                        break;
                    case 'f':
                        map.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        itemList[i][j] = ItemFlame.code;
                        break;
                    case 's':
                        map.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        itemList[i][j] = ItemSpeed.code;
                        break;
                    default:
                        map.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        break;
                }
            }
        }
        sc.close();
    }

    /*
        Render map on screen
     */

    public void mapRender(GraphicsContext gc) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                map.get(i).get(j).render(gc);
            }
        }
    }

    /*
        Convert map to Graph
     */

    public void convertToGraph() {
        List<Vertices> verticesList = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                verticesList.add(new Vertices(j, i));
            }
        }
        graph = new Graph(verticesList);
        for (int i = 0; i < verticesList.size() - 1; i++) {
            for (int j = i + 1; j < verticesList.size(); j++) {
                boolean isAdj = false;
                int x1 = verticesList.get(i).getXTilePos();
                int x2 = verticesList.get(j).getXTilePos();
                int y1 = verticesList.get(i).getYTilePos();
                int y2 = verticesList.get(j).getYTilePos();
                if (map.get(y1).get(x1) instanceof Grass
                        && map.get(y2).get(x2) instanceof Grass) {
                    if (x1 == x2) {
                        if (y1 == y2 + 1) isAdj = true;
                        else if (y1 == y2 - 1) isAdj = true;
                    } else if (y1 == y2) {
                        if (x1 == x2 + 1) isAdj = true;
                        else if (x1 == x2 - 1) isAdj = true;
                    }
                }
                if (isAdj) graph.addAdjVertices(i, j);
            }
        }
    }

    /*
        Getter functions
     */

    public int getHeightTile() {
        return HEIGHT;
    }

    public int getWidthTile() {
        return WIDTH;
    }

    public int getWidthPixel() {
        return WIDTH * Sprite.SCALED_SIZE;
    }

    public int getHeightPixel() {
        return HEIGHT * Sprite.SCALED_SIZE;
    }
    public List<List<Entity>> getMap() {
        return map;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getItems(int x, int y) {
        return itemList[y][x];
    }

    public Entity getPosition(int x, int y) {
        int xTile = x / Sprite.SCALED_SIZE;
        int yTile = y / Sprite.SCALED_SIZE;
        return map.get(yTile).get(xTile);
    }

    public Bomber getBomber() {
        for (int i = 0; i < GameMaster.entities.get(level).size(); i++) {
            if(GameMaster.entities.get(level).get(i) instanceof Bomber) {
                return (Bomber) GameMaster.entities.get(level).get(i);
            }
        }
        return null;
    }

    public void replace(int x, int y, Entity entity) {
        map.get(y).set(x, entity);
    }

    public void reset() {
        GameMaster.entities.get(level).clear();
        GameMaster.bombsList.clear();
        map.clear();
        mapReader();
        convertToGraph();
    }
}
