package uet.oop.bomberman.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static boolean setAudio = false;

    public void update() {
        switch (menu.getMenuState()) {
            case SINGLE_PLAY:
                try {
                    entities.get(level).forEach(Entity::update);
                } catch (ConcurrentModificationException e) {
                    // Exception catch by Duplicate
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
            case END:
                menu.update();
                break;
            case OPTION:
                if (setAudio) {
                    AudioControls();
                }
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
                newLevel(level);
                break;
            case MAP_RELOAD:
            case OPTION:
            case END:
                break;
            default:
                throw new IllegalArgumentException("Invalid Game State");
        }
    }

    private Group root = new Group();

    public void run() {
        graphics = new Graphics(canvas);
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

    private void AudioControls() {
        setAudio = false;
        audio.setAudioOption(!audio.isMuted());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Path mapDir = Paths.get("").toAbsolutePath();
        alert.setGraphic((new ImageView(mapDir.normalize().toString() + "/res/textures/audio_notify.png")));
        alert.setTitle("Audio Notification");
        alert.setHeaderText(null);
        alert.setContentText("Audio has been turn " + (audio.isMuted() ? "off." : "on."));
        alert.setOnCloseRequest(event -> {
            alert.close();
        });
        alert.show();
        // menu.setMenuState(Menu.MenuState.MENU);
    }

    private int score, lives;
    private TextField ScoreBoard;

    private void ScoreBoardInit() {
        score = 0;
        ScoreBoard = new TextField();
        ScoreBoard.setEditable(false);
        ScoreBoard.setFocusTraversable(false);
        ScoreBoard.setPrefWidth(Graphics.SCREEN_WIDTH);
        ScoreBoard.setPrefHeight(Sprite.SCALED_SIZE);
        ScoreBoard.setFont(Font.font(18));
        ScoreBoard.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
        ScoreBoard.setText("\uD83D\uDCAF Score:  " + score + spaceBetween +
                    "\uD83D\uDC9C Lives: " + lives + spaceBetween +
                    "\uD83D\uDEA9 Level: " + (level + 1));
    }

    private TextField notify;

    private void NotifyInit() {
        notify.setText("Level " + (level + 1));
        notify.setEditable(false);
        notify.setFocusTraversable(false);
        notify.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff");
        notify.setFont(Font.font(50));
        notify.setLayoutX(0);
        notify.setLayoutY(160);
        notify.setPrefWidth(Graphics.SCREEN_WIDTH);
        notify.setAlignment(Pos.CENTER);
    }

    private String spaceBetween = "                                                   ";

    private Button loader;
    private boolean isPlaying = false;
    private long delay = 0;
    public void newLevel(int level) {
        long now = Timer.now();
        if (!isPlaying) {
            if (level != 1) {
                root.getChildren().remove(canvas);
            }
            if (Objects.isNull(notify)) {
                notify = new TextField();
                root.getChildren().add(notify);
                NotifyInit();
            }
            if (now - delay > Timer.INPUT_TIME) {
                delay++;
                MapLoader();
                isPlaying = true;
            }
        } else {
            graphics.clearScreen(canvas);
            mapList.get(level).mapRender(gc);
            entities.get(level).forEach(g -> g.render(gc));
        }
    }

    private void MapLoader() {
        ScoreBoardInit();
        root.getChildren().add(ScoreBoard);
        root.setLayoutX(0);
        ScoreBoard.setLayoutX(0);
        canvas.setLayoutY(32);
        root.getChildren().add(canvas);
        graphics.clearScreen(canvas);
        mapList.get(level).mapRender(gc);
        entities.get(level).forEach(g -> g.render(gc));
    }

    /*
        In-game processing
     */

    public void entitiesUpdate() {
        for (int i = entities.get(level).size() - 1; i >= 0; i--) {
            if (entities.get(level).get(i) instanceof Enemy) {
                if (entities.get(level).get(i) instanceof Oneal) {
                    ((Oneal) entities.get(level).get(i)).setOnealStatus(Oneal.OnealStatus.WALKING);
                }
                if (entities.get(level).get(i) instanceof Duplicate) {
                    ((Duplicate) entities.get(level).get(i)).setDuplicateStatus(Duplicate.DuplicateStatus.WALKING);
                }
                if (((Enemy) entities.get(level).get(i)).getEnemyStatus() == Enemy.EnemyStatus.DEAD) {
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
        if (bomber_yPixel < (Graphics.SCREEN_HEIGHT - 32) / 2) {
            yCamera = 0;
        } else if (bomber_yPixel < mapList.get(level).getHeightPixel() - (Graphics.SCREEN_HEIGHT - 32)/ 2) {
            yCamera = bomber_yPixel - (Graphics.SCREEN_HEIGHT - 32) / 2;
        } else {
            yCamera = mapList.get(level).getHeightPixel() - (Graphics.SCREEN_HEIGHT - 32);
        }
    }
}
