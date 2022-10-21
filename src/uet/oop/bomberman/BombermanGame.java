package uet.oop.bomberman;

import javafx.application.Application;
import javafx.stage.Stage;
import uet.oop.bomberman.controller.GameMaster;

public class BombermanGame extends Application {

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        GameMaster gameMaster = new GameMaster(stage);
        gameMaster.run();
    }
}
