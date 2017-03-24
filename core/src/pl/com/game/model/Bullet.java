package pl.com.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Savek on 2017-03-24.
 */
public class Bullet extends Rectangle {
    float direction_x, direction_y;
    Texture bulletTexture;

    public Bullet(float direction_x, float direction_y, String image) {
        super();

        this.direction_x = direction_x;
        this.direction_y = direction_y;
        this.bulletTexture = new Texture(Gdx.files.internal(image));
    }

    public Texture getBulletTexture() {
        return bulletTexture;
    }

    public void setBulletTexture(Texture bulletTexture) {
        this.bulletTexture = bulletTexture;
    }

    public void update() {
        this.setPosition(this.getX() + direction_x*3,
                        this.getY() + direction_y*3);
    }
}
