package uet.oop.bomberman;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import uet.oop.bomberman.controller.GameMaster;

public class BombermanGame extends Application {

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        stage.getIcons().add(new Image("/textures/app_icon.png"));
        stage.setTitle("Bomberman v1.0");
        GameMaster gameMaster = new GameMaster(stage);
        gameMaster.run();
    }
}
