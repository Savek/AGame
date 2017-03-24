package pl.com.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import pl.com.game.model.Bullet;
import pl.com.game.model.Character;

public class Game extends ApplicationAdapter{
    public Model model;
    private SpriteBatch batch;
    private Character character;
    private Stage stage;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    Boolean buttonClicked = false;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture blockTexture;
    private Sprite blockSprite;
    private float blockSpeed;
    private Array<Bullet> bulletList;
    private Bullet bullet;

    private float last_x = 1, last_y = 0;
	@Override
	public void create() {

        //Create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 480);

        tiledMap = new TmxMapLoader().load("desert.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

       // bucket = new Rectangle();
        character = new Character(64, 64, "bucket.png");
        character.setPosition(Gdx.graphics.getWidth()/2 - character.getWidth()/2,
                Gdx.graphics.getHeight()/2 - character.getHeight()/2);

        batch = new SpriteBatch();

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);
        touchpad.setPosition(Gdx.graphics.getWidth() * 0.04f, Gdx.graphics.getHeight() * 0.05f);

        bulletList = new Array<Bullet>();
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("droplet.png"))));
        ImageButton shotButton = new ImageButton(drawable);
        shotButton.setPosition(Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.13f);
        shotButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bullet = new Bullet(last_x, last_y, "droplet.png");
                bullet.setPosition(Gdx.graphics.getWidth()/2 - bullet.getWidth()/2,
                        Gdx.graphics.getHeight()/2 - bullet.getHeight()/2);
                bulletList.add(bullet);
            }
        });
        stage = new Stage(new ExtendViewport(800, 840));
        stage.addActor(shotButton);
        stage.addActor(touchpad);

        Gdx.input.setInputProcessor(stage);

        blockSpeed = 3;
	}

	@Override
	public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();
        batch.draw(character.getCharacterTexture(), character.x, character.y);
        camera.position.x = camera.position.x + touchpad.getKnobPercentX()*blockSpeed;
        camera.position.y = camera.position.y + touchpad.getKnobPercentY()*blockSpeed;
        if (touchpad.isTouched()) {
            last_x = Math.signum(touchpad.getKnobPercentX());
            if (touchpad.getKnobPercentX() < 0.1 && touchpad.getKnobPercentX() > -0.1) last_x = 0;
            last_y = Math.signum(touchpad.getKnobPercentY());
            if (touchpad.getKnobPercentY() < 0.1 && touchpad.getKnobPercentY() > -0.1) last_y = 0;
        }

        Iterator<Bullet> iter = bulletList.iterator();
        while(iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update();
            batch.draw(bullet.getBulletTexture(), bullet.x, bullet.y);
            // wypadalo by kiedys usuwac te pociski
        }
        camera.update();

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}

	@Override
	public void dispose() {
        batch.dispose();
        model.dispose();
        stage.dispose();
	}
}