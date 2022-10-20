package uet.oop.bomberman.scene;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import uet.oop.bomberman.controller.GameMaster;

public class ScoreBoard {
    @FXML
    Text NAME_TEXT;

    @FXML
    public void changeText() {
        //NAME_TEXT.setText("445");
        GameMaster.gameStatus = GameMaster.inGameStatus.PLAY;
    }
}
