package uet.oop.bomberman.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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

    public static boolean setAudio = false, unfinished = false;

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
                updateScoreBoard();
                break;
            case MAP_RELOAD:
                resetCurrentLevel();
                menu.setMenuState(Menu.MenuState.SINGLE_PLAY);
                break;
            case WIN:
            case END_STATE:
            case MENU:
            case MULTIPLAYER:
            case END:
                menu.update();
                break;
            case UNFINISHED:
                if (unfinished) {
                    UnfinishedAlert();
                }
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
            case END_STATE:
                menu.menuRender(gc);
                break;
            case SINGLE_PLAY:
            case MULTIPLAYER:
                MapLoader();
                break;
            case WIN:
                menu.winRender(gc);
                break;
            case UNFINISHED:
            case MAP_RELOAD:
            case OPTION:
            case END:
                break;
            default:
                throw new IllegalArgumentException("Invalid Game State");
        }
    }

    private static Group root = new Group();

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
        int lifeCount = ((Bomber) entities.get(level).get(0)).getLivesCount();
        mapList.get(level).reset();
        ((Bomber) entities.get(level).get(0)).setLifeCount(lifeCount);
    }

    public static void resetAllLevel() {
        for (int i = 0; i <= level; i++) {
            mapList.get(i).reset();
        }
        level = 0;
    }

    private void updateScoreBoard() {
        enemyAlive = entities.get(level).size() - 1;
        lives = ((Bomber) entities.get(level).get(0)).getLivesCount();
        flame = ((Bomber) entities.get(level).get(0)).getFlameLength();
        speed = ((Bomber) entities.get(level).get(0)).getSpeed();
        bomb = ((Bomber) entities.get(level).get(0)).getBombCount();
        ScoreBoard.setText("  \uD83D\uDC9C Lives: " + lives + spaceBetween +
                "\uD83D\uDEA9 Level: " + (level + 1) + spaceBetween +
                "\uD83D\uDC7B Enemy:  " + enemyAlive + spaceBetween +
                "\uD83D\uDD25 Flame: " + flame + spaceBetween +
                "\uD83D\uDCA3 Bomb: " + bomb + spaceBetween +
                "\uD83D\uDC5F Speed: " + speed);
    }

    private void UnfinishedAlert() {
        unfinished = true;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sorry..");
        alert.setHeaderText(null);
        alert.setContentText("This feature is coming soon!");
        alert.setOnCloseRequest(event -> {
            alert.close();
        });
        alert.show();
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
        if (!audio.isMuted()) {
            audio.playAlone(Audio.AudioType.LOBBY, -1);
        }
        alert.setOnCloseRequest(event -> {
            alert.close();
        });
        alert.show();
    }

    public static void returnToMenu() {
        root.getChildren().remove(canvas);
        root.getChildren().remove(ScoreBoard);
        canvas.setLayoutY(0);
        root.getChildren().add(canvas);
        isPlaying = false;
        audio.playAlone(Audio.AudioType.LOBBY, -1);
        menu.setMenuState(Menu.MenuState.MENU);
    }

    public static void winAll() {
        root.getChildren().remove(canvas);
        root.getChildren().remove(ScoreBoard);
        canvas.setLayoutY(0);
        root.getChildren().add(canvas);
        isPlaying = false;
        audio.playAlone(Audio.AudioType.WIN, -1);
        menu.setMenuState(Menu.MenuState.WIN);
    }

    private int enemyAlive, lives, flame, bomb, speed;
    private static TextField ScoreBoard;

    private void ScoreBoardInit() {
        ScoreBoard = new TextField();
        ScoreBoard.setEditable(false);
        ScoreBoard.setFocusTraversable(false);
        ScoreBoard.setPrefWidth(Graphics.SCREEN_WIDTH);
        ScoreBoard.setPrefHeight(Sprite.SCALED_SIZE + Sprite.SCALED_SIZE / 2);
        ScoreBoard.setFont(Font.font("Segoe UI Semibold", 17));
        ScoreBoard.setStyle("-fx-background-color: #DCDCDC; -fx-text-fill: #000000;");
        updateScoreBoard();
    }

    private String spaceBetween = "           ";
    private static boolean isPlaying = false;

    private void MapLoader() {
        if (!isPlaying) {
            ScoreBoardInit();
            root.getChildren().remove(canvas);
            root.getChildren().add(ScoreBoard);
            root.setLayoutX(0);
            ScoreBoard.setLayoutX(0);
            canvas.setLayoutY(48);
            root.getChildren().add(canvas);

            graphics.clearScreen(canvas);
            mapList.get(level).mapRender(gc);
            entities.get(level).forEach(g -> g.render(gc));

            isPlaying = true;
        } else {
            graphics.clearScreen(canvas);
            mapList.get(level).mapRender(gc);
            entities.get(level).forEach(g -> g.render(gc));
        }
    }

    /*
        In-game processing
     */

    public void entitiesUpdate() {
        // Set all enemy to WALKING state
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
        if (bomber_yPixel < (Graphics.SCREEN_HEIGHT - 48) / 2) {
            yCamera = 0;
        } else if (bomber_yPixel < mapList.get(level).getHeightPixel() - (Graphics.SCREEN_HEIGHT - 48)/ 2) {
            yCamera = bomber_yPixel - (Graphics.SCREEN_HEIGHT - 48) / 2;
        } else {
            yCamera = mapList.get(level).getHeightPixel() - (Graphics.SCREEN_HEIGHT - 48);
        }
    }
}
