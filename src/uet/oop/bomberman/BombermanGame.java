package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import uet.oop.bomberman.controller.KeyboardEvent;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.GraphicManager;
import uet.oop.bomberman.graphics.Map;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {
    private GraphicManager graphics;
    private Canvas canvas;
    public static Map map;
    private List<Entity> entities = new ArrayList<>();
    private KeyboardEvent keyboardEvent;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * GraphicManager.WIDTH, Sprite.SCALED_SIZE * GraphicManager.HEIGHT);
        graphics = new GraphicManager(canvas);

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);
        keyboardEvent = new KeyboardEvent(scene);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
            }
        };
        timer.start();

        map = new Map(1, keyboardEvent);

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage(), keyboardEvent);
        entities.add(bomberman);
    }

    public void update() {
        map.update();
    }

    public void render() {
        graphics.clearScreen(canvas);
        graphics.mapRenderer(map);
    }
}
