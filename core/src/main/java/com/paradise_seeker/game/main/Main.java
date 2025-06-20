package com.paradise_seeker.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.paradise_seeker.game.screen.ControlScreen;
import com.paradise_seeker.game.screen.GameScreen;
import com.paradise_seeker.game.screen.InventoryScreen;
import com.paradise_seeker.game.screen.MainMenuScreen;
import com.paradise_seeker.game.screen.SettingScreen;
import com.paradise_seeker.game.screen.WinScreen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Main extends Game {

    public static final float WORLD_WIDTH = 16;
    public static final float WORLD_HEIGHT = 10;

    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public FitViewport viewport;
    
    public float setVolume = 0.5f; // Default music volume
    public GameScreen currentGame = null;//load screen
    public MainMenuScreen mainMenu = null;
    public SettingScreen settingMenu = null;
    public InventoryScreen inventoryScreen = null;
    public ControlScreen controlScreen = null;
    public WinScreen WinScreen = null;
    @Override
    public void create() {
        batch = new SpriteBatch();
        //font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MinecraftStandard.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16; // Set your desired font size
        font = generator.generateFont(parameter);
        generator.dispose();
        // Khởi tạo camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // FitViewport gắn với camera
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        // Font scaling theo WORLD_HEIGHT
        font.setUseIntegerPositions(false);
        font.getData().setScale(WORLD_HEIGHT / Gdx.graphics.getHeight());

        // Khởi tạo màn hình game
        this.settingMenu = new SettingScreen(this);
        this.mainMenu = new MainMenuScreen(this);
        this.setScreen(mainMenu);

    }

    @Override
    public void render() {
        super.render(); // quan trọng để gọi render() của current screen
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}