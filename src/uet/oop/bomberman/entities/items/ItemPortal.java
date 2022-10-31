package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.bomber.Bomber;
import uet.oop.bomberman.graphics.Menu;
import uet.oop.bomberman.graphics.sprite.Sprite;

import static uet.oop.bomberman.controller.GameMaster.*;

public class ItemPortal extends Item {
    public static final int code = 4;

    public ItemPortal(int x, int y, Image img) {
        super(x, y, img);
    }

    public void update(Bomber bomber) {
        if (level == MAX_LEVEL - 1) {
            menu.setMenuState(Menu.MenuState.WIN);
        } else if (entities.get(level).size() == 1) {
            bomber.resetStats();
            bombsList.clear();
            level++;
        }
    }
}
