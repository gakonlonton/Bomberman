package uet.oop.bomberman.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import uet.oop.bomberman.BombermanGame;

public class LobbyScene extends RootScene {

    public LobbyScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(BombermanGame.class.getResource("/ScoreBoard.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (Exception e) {
            System.out.println("vkl");
        }
    }
}
