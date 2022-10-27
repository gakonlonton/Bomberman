package uet.oop.bomberman.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import uet.oop.bomberman.controller.audio.Audio;
import uet.oop.bomberman.entities.bomber.Bomb;
import uet.oop.bomberman.entities.bomber.Bomber;
import uet.oop.bomberman.entities.enemies.Enemy;
import uet.oop.bomberman.entities.enemies.Oneal;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Graphics;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Menu;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
                entities.get(level).forEach(Entity::update);
                updateCamera();
                entitiesUpdate();
                break;
            case MAP_RELOAD:
                System.out.println("here");
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
                        for (Entity k : entities.get(level)) {
                            if (k instanceof Oneal) ((Oneal) k).setOnealStatus(Oneal.OnealStatus.WALKING);
                        }
                    }
                    entities.get(level).remove(i);
                }
            }
        }

        //If existing an oneal chasing bomber, others will move randomly
        Queue<Pair> dis = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o.getValue()));

        for (int i = 1; i < entities.get(level).size(); i++) {
            if (entities.get(level).get(i) instanceof Oneal) {
                if (((Oneal) entities.get(level).get(i)).getOnealStatus() == Oneal.OnealStatus.CHASING) {
                    dis.add(new Pair(i, ((Oneal) entities.get(level).get(i)).getDistanceFromBomber()));
                }
            }
        }

        if (!dis.isEmpty()) {
            for (Entity j : entities.get(level)) {
                if (!j.equals(entities.get(level).get((Integer) dis.peek().getKey())) && j instanceof Oneal) {
                    ((Oneal) j).setOnealStatus(Oneal.OnealStatus.INVALID);
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
