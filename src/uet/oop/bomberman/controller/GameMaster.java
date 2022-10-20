package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.util.Pair;
import uet.oop.bomberman.entities.Enemy;
import uet.oop.bomberman.entities.EnemyOneal;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.scene.InGame;
import uet.oop.bomberman.scene.Lobby;

import java.util.*;

public class GameMaster {
    private static final int MAX_LEVEL = 0;
    public static int level = 0;
    private Stage stage;
    private Lobby lobby = new Lobby();
    private InGame inGame = new InGame();
    private Audio audio = new Audio();
    public static final List<Map> mapList = new ArrayList<>();
    public static List<List<Entity>> entities = new ArrayList<>();
    public static List<Entity> bombsList = new ArrayList<>();
    public enum inGameStatus {
        LOBBY, PLAY, PAUSE, WIN, LOSE
    }
    public static inGameStatus gameStatus = inGameStatus.LOBBY;

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
            if (!stage.getScene().equals(inGame.getScene())) {
                stage.setScene(inGame.getScene());
                audio.playAlone(Audio.AudioType.PLAYING, -1);
            }
            entities.get(level).forEach(Entity::update);
            updateCamera();
            entitiesUpdate();
        }
        else if (gameStatus == inGameStatus.LOBBY) {
            if (!stage.getScene().equals(lobby.getScene())) {
                stage.setScene(lobby.getScene());
            }
            audio.playAlone(Audio.AudioType.LOBBY, -1);
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
                    if (entities.get(level).get(i) instanceof EnemyOneal) {
                        for (Entity k : entities.get(level)) {
                            if (k instanceof EnemyOneal) ((EnemyOneal) k).setOnealStatus(EnemyOneal.OnealStatus.WALKING);
                        }
                    }
                    entities.get(level).remove(i);
                }
            }
        }

        //If existing an oneal chasing bomber, others will move randomly
        Queue<Pair> dis = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o.getValue()));

        for (int i = 1; i < entities.get(level).size(); i++) {
            if (entities.get(level).get(i) instanceof EnemyOneal) {
                if (((EnemyOneal) entities.get(level).get(i)).getOnealStatus() == EnemyOneal.OnealStatus.CHASING) {
                    dis.add(new Pair(i, ((EnemyOneal) entities.get(level).get(i)).getDistanceFromBomber()));
                }
            }
        }

        if (!dis.isEmpty()) {
            for (Entity j : entities.get(level)) {
                if (!j.equals(entities.get(level).get((Integer) dis.peek().getKey())) && j instanceof EnemyOneal) {
                    ((EnemyOneal) j).setOnealStatus(EnemyOneal.OnealStatus.INVALID);
                }
            }
        }
    }

    public static int xCamera = 0, yCamera = 0;

    public void updateCamera() {
        int bomber_xPixel = entities.get(level).get(0).getX();
        int bomber_yPixel = entities.get(level).get(0).getY();

        if (bomber_xPixel < inGame.getWidthPixelPlayingFrame() / 2) {
            xCamera = 0;
        } else if (bomber_xPixel < mapList.get(level).getWidthPixel() - inGame.getWidthPixelPlayingFrame() / 2) {
            xCamera = bomber_xPixel - inGame.getWidthPixelPlayingFrame() / 2;
        } else {
            xCamera = mapList.get(level).getWidthPixel() - inGame.getWidthPixelPlayingFrame();
        }
        if (bomber_yPixel < inGame.getHeightPixelPlayingFrame() / 2) {
            yCamera = 0;
        } else if (bomber_yPixel < mapList.get(level).getHeightPixel() - inGame.getHeightPixelPlayingFrame() / 2) {
            yCamera = bomber_yPixel - inGame.getHeightPixelPlayingFrame()/ 2;
        } else {
            yCamera = mapList.get(level).getHeightPixel() - inGame.getHeightPixelPlayingFrame();
        }
    }

    public void render() {
        if (gameStatus == inGameStatus.PLAY) {
            inGame.clearScreen();
            mapList.get(level).mapRender(inGame.getGc());
            entities.get(level).forEach(g -> g.render(inGame.getGc()));
        }
        else if (gameStatus == inGameStatus.LOBBY) {

        }
    }

    public void reset() {
        inGame = new InGame();
        lobby = new Lobby();
        for (int i = 0; i <= level; i++) {
            mapList.get(level).reset();
        }
    }

    public void run() {
        stage.setScene(lobby.getScene());
        stage.show();
        timer.start();
    }
}
