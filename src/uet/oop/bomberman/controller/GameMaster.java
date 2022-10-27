package uet.oop.bomberman.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import uet.oop.bomberman.controller.audio.Audio;
import uet.oop.bomberman.entities.bomber.Bomb;
import uet.oop.bomberman.entities.bomber.Bomber;
import uet.oop.bomberman.entities.enemies.Duplicate;
import uet.oop.bomberman.entities.enemies.Enemy;
import uet.oop.bomberman.entities.enemies.Oneal;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Graphics;
import uet.oop.bomberman.graphics.Menu;

import java.util.*;

public class GameMaster {
    public static final int MAX_LEVEL = 5;
    public static int level = 0;

    /*
        Game controller
     */

    public static Canvas canvas = new Canvas(Graphics.SCREEN_WIDTH, Graphics.SCREEN_HEIGHT);
    public static final GraphicsContext gc = canvas.getGraphicsContext2D();
    private Stage stage;
    private Graphics graphics;
    private Timer timer;
    public static Menu menu;
    private KeyListener keyListener;
    public static Audio audio = new Audio();


    /*
        Entities controller
     */

    public static List<Map> mapList = new ArrayList<>();
    public static List<List<Entity>> entities = new ArrayList<>();
    public static List<Bomb> bombsList = new ArrayList<>();

    /*
        Constructor
     */

    public GameMaster(Stage stage) {
        this.stage = stage;
        loadMap();
    }


    public void update() {
        switch (menu.getMenuState()) {
            case SINGLE_PLAY:
                try {
                    entities.get(level).forEach(Entity::update);
                } catch (ConcurrentModificationException e) {
                    System.out.print("");
                }
                updateCamera();
                entitiesUpdate();
                break;
            case MAP_RELOAD:
                resetCurrentLevel();
                menu.setMenuState(Menu.MenuState.SINGLE_PLAY);
                break;
            case MENU:
            case PAUSE:
            case MULTIPLAYER:
            case END_STATE:
                menu.update();
                break;
            default:
                throw new IllegalArgumentException("Invalid Game State");
        }
    }

    public void render() {
        switch (menu.getMenuState()) {
            case MENU:
            case PAUSE:
            case END_STATE:
                menu.menuRender(gc);
                break;
            case SINGLE_PLAY:
            case MULTIPLAYER:
                graphics.clearScreen(canvas);
                mapList.get(level).mapRender(gc);
                entities.get(level).forEach(g -> g.render(gc));
                break;
            case MAP_RELOAD:
            case OPTION:
            case END:
                break;
            default:
                throw new IllegalArgumentException("Invalid Game State");
        }
    }

    public void run() {
        graphics = new Graphics(canvas);
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        keyListener = new KeyListener(scene);
        menu = new Menu(keyListener);
        timer = new Timer(this);
        // Stage controller
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /*
        Load map
     */

    public void loadMap() {
        for (int i = 0; i < MAX_LEVEL; i++) {
            entities.add(new ArrayList<>());
            mapList.add(new Map(i));
        }
    }

    public void resetCurrentLevel() {
        int lifeCount = ((Bomber) entities.get(level).get(0)).getLifeCount();
        mapList.get(level).reset();
        ((Bomber) entities.get(level).get(0)).setLifeCount(lifeCount);
    }

    /*
        In-game processing
     */

    public void entitiesUpdate() {
        for (int i = entities.get(level).size() - 1; i >= 0; i--) {
            if (entities.get(level).get(i) instanceof Enemy) {
                if (((Enemy) entities.get(level).get(i)).getEnemyStatus() == Enemy.EnemyStatus.DEAD) {
                    if (entities.get(level).get(i) instanceof Oneal) {
                        ((Oneal) entities.get(level).get(i)).setOnealStatus(Oneal.OnealStatus.WALKING);
                    }
                    if (entities.get(level).get(i) instanceof Duplicate) {
                        ((Duplicate) entities.get(level).get(i)).setDuplicateStatus(Duplicate.DuplicateStatus.WALKING);
                    }
                    entities.get(level).remove(i);
                }
            }
        }

        //If existing an enemy chasing bomber, others will move randomly
        Queue<Pair> dis1 = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o.getValue()));
        Queue<Pair> dis2 = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o.getValue()));

        for (int i = 1; i < entities.get(level).size(); i++) {
            if (entities.get(level).get(i) instanceof Oneal) {
                if (((Oneal) entities.get(level).get(i)).getOnealStatus() == Oneal.OnealStatus.CHASING) {
                    dis1.add(new Pair(i, ((Oneal) entities.get(level).get(i)).getDistanceFromBomber()));
                }
            }
            if (entities.get(level).get(i) instanceof Duplicate) {
                if (((Duplicate) entities.get(level).get(i)).getDuplicateStatus() == Duplicate.DuplicateStatus.CHASING) {
                    dis2.add(new Pair(i, ((Duplicate) entities.get(level).get(i)).getDistanceFromBomber()));
                }
            }
        }

        if (!dis1.isEmpty()) {
            for (Entity j : entities.get(level)) {
                if (!j.equals(entities.get(level).get((Integer) dis1.peek().getKey())) && j instanceof Oneal) {
                    ((Oneal) j).setOnealStatus(Oneal.OnealStatus.INVALID);
                }
            }
        }
        if (!dis2.isEmpty()) {
            for (Entity j : entities.get(level)) {
                if (!j.equals(entities.get(level).get((Integer) dis2.peek().getKey())) && j instanceof Duplicate) {
                    ((Duplicate) j).setDuplicateStatus(Duplicate.DuplicateStatus.INVALID);
                }
            }
        }
    }

    public static int xCamera = 0, yCamera = 0;

    public void updateCamera() {
        int bomber_xPixel = entities.get(level).get(0).getX();
        int bomber_yPixel = entities.get(level).get(0).getY();

        if (bomber_xPixel < Graphics.SCREEN_WIDTH / 2) {
            xCamera = 0;
        } else if (bomber_xPixel < mapList.get(level).getWidthPixel() - Graphics.SCREEN_WIDTH / 2) {
            xCamera = bomber_xPixel - Graphics.SCREEN_WIDTH / 2;
        } else {
            xCamera = mapList.get(level).getWidthPixel() - Graphics.SCREEN_WIDTH;
        }
        if (bomber_yPixel < Graphics.SCREEN_HEIGHT / 2) {
            yCamera = 0;
        } else if (bomber_yPixel < mapList.get(level).getHeightPixel() - Graphics.SCREEN_HEIGHT / 2) {
            yCamera = bomber_yPixel - Graphics.SCREEN_HEIGHT / 2;
        } else {
            yCamera = mapList.get(level).getHeightPixel() - Graphics.SCREEN_HEIGHT;
        }
    }
}
