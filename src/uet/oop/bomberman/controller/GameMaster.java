package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Pair;
import uet.oop.bomberman.controller.audio.Audio;
import uet.oop.bomberman.entities.bomber.Bomb;
import uet.oop.bomberman.entities.enemies.Enemy;
import uet.oop.bomberman.entities.enemies.Oneal;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.scene.Lobby;
import uet.oop.bomberman.scene.Master;

import java.io.IOException;
import java.util.*;

public class GameMaster {
    public static final int MAX_LEVEL = 1;
    public static int level = 0;
    private boolean isReset = false;

    public enum inGameStatus {
        LOBBY, PLAY, PAUSE, WIN, LOSE
    }
    public static inGameStatus gameStatus = inGameStatus.LOBBY;

    /*
        Stage controller
     */

    public static Canvas canvas = new Canvas(Master.SCREEN_WIDTH, Master.SCREEN_HEIGHT - 30);
    public static final GraphicsContext gc = canvas.getGraphicsContext2D();
    private Stage stage;
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

    /*
        Load map
     */

    public void loadMap() {
        for (int i = 0; i <= MAX_LEVEL; i++) {
            entities.add(new ArrayList<>());
            mapList.add(new Map(i));
        }
    }

    /*
        Timer adjust
     */
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            render();
            update();
        }
    };

    /*
        In-game processing
     */

    public void update() {
        if (gameStatus == inGameStatus.PLAY) {
            isReset = true;
            audio.playAlone(Audio.AudioType.PLAYING, -1);
            entities.get(level).forEach(Entity::update);
            updateCamera();
            entitiesUpdate();
        }
        else if (gameStatus == inGameStatus.LOBBY) {
            reset();
        }
        else if (gameStatus == inGameStatus.LOSE) {
            reset();
            gameStatus = inGameStatus.LOBBY;
        }
    }

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

        if (bomber_xPixel < Master.SCREEN_WIDTH / 2) {
            xCamera = 0;
        } else if (bomber_xPixel < mapList.get(level).getWidthPixel() - Master.SCREEN_WIDTH / 2) {
            xCamera = bomber_xPixel - Master.SCREEN_WIDTH / 2;
        } else {
            xCamera = mapList.get(level).getWidthPixel() - Master.SCREEN_WIDTH;
        }
        if (bomber_yPixel < (Master.SCREEN_HEIGHT - 30) / 2) {
            yCamera = 0;
        } else if (bomber_yPixel < mapList.get(level).getHeightPixel() - (Master.SCREEN_HEIGHT - 30) / 2) {
            yCamera = bomber_yPixel - (Master.SCREEN_HEIGHT - 30) / 2;
        } else {
            yCamera = mapList.get(level).getHeightPixel() - Master.SCREEN_HEIGHT + 30;
        }
    }

    public void render() {
        if (gameStatus == inGameStatus.PLAY) {
            Lobby.clearScreen();
            mapList.get(level).mapRender(gc);
            entities.get(level).forEach(g -> g.render(gc));
        }
        else if (gameStatus == inGameStatus.LOBBY) {

        }
    }

    public void reset() {
        if (isReset) {
            try {
                stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Lobby.fxml")))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.show();
            for (int i = 0; i <= level; i++) {
                mapList.get(level).reset();
            }
        }
        isReset = false;
        audio.playAlone(Audio.AudioType.LOBBY, -1);
    }

    public void run() {
        try {
            stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Lobby.fxml")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
        timer.start();
    }
}
