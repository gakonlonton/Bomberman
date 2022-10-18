package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.util.Pair;
import uet.oop.bomberman.entities.Enemy;
import uet.oop.bomberman.entities.EnemyOneal;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.scene.IngameScene;
import uet.oop.bomberman.scene.LobbyScene;

import java.util.*;

public class GameMaster {
    private Stage stage;
    private static final int MAX_level = 0;
    public static int level = 0;
    private LobbyScene lobby = new LobbyScene();
    private IngameScene ingame = new IngameScene();
    private Audio audio;
    public static final List<Map> mapList = new ArrayList<>();
    public static List<List<Entity>> entities = new ArrayList<>();
    public static List<Entity> bombsList = new ArrayList<>();
    public enum ingameSatus {
        LOBBY, INGAME, PAUSE, WIN, LOSE
    }
    public static ingameSatus gameStatus;

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
        for (int i = 0; i <= MAX_level; i++) {
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
        if (gameStatus == ingameSatus.INGAME) {
            if (!stage.getScene().equals(ingame)) {
                stage.setScene(ingame.getScene());
                audio.playAlone(Audio.AudioType.PLAYING, -1);
            }
            entities.get(level).forEach(g -> g.render(ingame.getGc()));
            entitiesUpdate();
        }
        else if (gameStatus == ingameSatus.LOBBY) {
            if (!stage.getScene().equals(lobby)) {
                stage.setScene(lobby.getScene());
            }
            audio.playAlone(Audio.AudioType.LOBBY, -1);
        }
        else if (gameStatus == ingameSatus.LOSE) {
            reset();
            gameStatus = ingameSatus.LOBBY;
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

        if (!dis.isEmpty())
            for (Entity j : entities.get(level)) {
                if (!j.equals(entities.get(level).get((Integer) dis.peek().getKey())) && j instanceof EnemyOneal) {
                    ((EnemyOneal) j).setOnealStatus(EnemyOneal.OnealStatus.INVALID);
                }
            }
    }

    public void render() {
        if (gameStatus == ingameSatus.INGAME) {
            ingame.clearScreen();
            mapList.get(level).mapRender(ingame.getGc());
            entities.get(level).forEach(g -> g.render(ingame.getGc()));
        }
        else if (gameStatus == ingameSatus.LOBBY) {

        }
    }

    public void reset() {
        ingame = new IngameScene();
        lobby = new LobbyScene();
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
