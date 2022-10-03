package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.Timer;
import uet.oop.bomberman.graphics.Sprite;

public class Bomb extends Entity implements Obstacle {
    private boolean exploded;
    private long timer;
    private int flame;
    public Bomb(int x, int y, Image img, int flame) {
        super(x, y, img);
        this.flame = flame;
        timer = Timer.now();
        spriteIndex = 0;
        exploded = false;
    }
    @Override
    public void update() {
        if (exploded == false) {
            spriteIndex++;
            switch (spriteIndex) {
                case 0:
                    pickSprite(Sprite.bomb.getFxImage());
                    break;
                case 1:
                    pickSprite(Sprite.bomb_1.getFxImage());
                    break;
                case 2:
                    pickSprite(Sprite.bomb_2.getFxImage());
                    break;
                default:
                    spriteIndex = 0;
                    break;
            }
            exploded = (Timer.now() - timer > Timer.TIME_FOR_BOMB_EXPLODE);
        }
    }

    public int getFlame() {
        return flame;
    }

    public void pickSprite(Image img) {
        this.img = img;
    }
    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public boolean isExploded() {
        return exploded;
    }
}
