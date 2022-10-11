package uet.oop.bomberman.scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import uet.oop.bomberman.controller.GameMaster;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.BombermanGame.WINDOW_HEIGHT;
import static uet.oop.bomberman.BombermanGame.WINDOW_WIDTH;


public class LobbyScene extends RootScene {
    Button playButton = new Button("PLAY");
    StackPane layout = new StackPane(playButton);

    public LobbyScene() {
        scene = new Scene(layout, WINDOW_WIDTH * Sprite.SCALED_SIZE, WINDOW_HEIGHT * Sprite.SCALED_SIZE);
        playButton.setOnAction(event -> {
            GameMaster.gameStatus = GameMaster.ingameSatus.INGAME;
        });
    }
}
