package uet.oop.bomberman;

import javafx.application.Application;
import javafx.stage.Stage;
import uet.oop.bomberman.controller.GameMaster;

public class BombermanGame extends Application {
    public static final int WINDOW_WIDTH = 31;
    public static final int WINDOW_HEIGHT = 13;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        GameMaster gameMaster = new GameMaster(stage);
        gameMaster.run();
    }
}
