package pl.com.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Savek on 2017-03-19.
 */
public class Character extends Rectangle {

    Texture characterTexture;

    public Character(int width, int height) {
        super();

        this.width = width;
        this.height = height;
    }

    public Character(int width, int height, String image) {
        super();

        this.width = width;
        this.height = height;

        characterTexture = new Texture(Gdx.files.internal(image));
    }

    public Texture getCharacterTexture() {
        return characterTexture;
    }

    public void setCharacterTexture(Texture characterTexture) {
        this.characterTexture = characterTexture;
    }
}
