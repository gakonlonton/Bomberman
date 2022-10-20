package uet.oop.bomberman.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import uet.oop.bomberman.BombermanGame;

public class Lobby extends Root {

    public Lobby() {
        FXMLLoader fxmlLoader = new FXMLLoader(BombermanGame.class.getResource("/ScoreBoard.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (Exception e) {
            System.out.println("vkl");
        }
    }
}
