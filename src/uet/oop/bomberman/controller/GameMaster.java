package uet.oop.bomberman.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.scene.IngameScene;
import uet.oop.bomberman.scene.LobbyScene;

import java.util.ArrayList;
import java.util.List;

public class GameMaster {
    private Stage stage;
    private static final int MAX_LEVEL = 0;
    private LobbyScene lobby = new LobbyScene();
    private IngameScene ingame = new IngameScene();
    public static final List<Map> mapList = new ArrayList<>();
    public static List<List<Entity>> entities = new ArrayList<>();
    public static int level = 0;

    public enum ingameSatus {
        LOBBY,
        INGAME,
        PAUSE,
        WIN,
        LOSE
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
        if (gameStatus == ingameSatus.INGAME) {
            if (!stage.getScene().equals(ingame)) {
                stage.setScene(ingame.getScene());
            }
            entities.get(level).forEach(g -> g.render(ingame.getGc()));
            entities.get(level).forEach(Entity::update);
        }
        else if (gameStatus == ingameSatus.LOBBY) {
            if (!stage.getScene().equals(lobby)) {
                reset();
                stage.setScene(lobby.getScene());
            }
        }
    }

    public void render() {
        if (gameStatus == ingameSatus.INGAME) {
            ingame.clearScreen();
            mapList.get(level).mapRender(ingame.getGc());
        }
        else if (gameStatus == ingameSatus.LOBBY) {

        }
    }

    public void reset() {
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
