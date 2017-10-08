package com.geek.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen implements Screen {
    private SpaceGame game;
    private SpriteBatch batch;
    private Background background;
    private Vector2 emptyVelocity = new Vector2(0, 0);
    private TextureRegion texStart;
    private TextureRegion texExit;
    private Rectangle rectStart;
    private Rectangle rectExit;
    private MyInputProcessor mip;
    private Music music;
    private TextureRegion gameOverTexture;
    private boolean gameOver;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public MenuScreen(SpaceGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.gameOver = false;
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenType.MENU);
        TextureAtlas atlas = Assets.getInstance().mainAtlas;
        background = new Background(atlas.findRegion("star16"));
        texExit = atlas.findRegion("btExit");
        texStart = atlas.findRegion("btPlay");
        rectStart = new Rectangle(256, 232, texStart.getRegionWidth(), texStart.getRegionHeight());
        rectExit = new Rectangle(1280 - 512, 232, texExit.getRegionWidth(), texExit.getRegionHeight());
        gameOverTexture = atlas.findRegion("gameOver");
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        music = Assets.getInstance().assetManager.get("menu_music.mp3", Music.class);
        music.setPosition(7.0f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(game.getCamera().combined);
        batch.begin();
        background.render(batch);
        if (gameOver) {
            batch.draw(texStart, rectStart.x, rectStart.y - 150);
            batch.draw(texExit, rectExit.x, rectExit.y - 150);
            batch.setColor(1, 1, 1, 0.3f);
            batch.draw(gameOverTexture, rectStart.x + 128, rectStart.y + 128);
            batch.setColor(1, 1, 1, 1);
        } else {
            batch.draw(texStart, rectStart.x, rectStart.y);
            batch.draw(texExit, rectExit.x, rectExit.y);
        }
        batch.end();
    }

    public void update(float dt) {
        background.update(dt, emptyVelocity);
        if (mip.isTouchedInArea(rectStart) != -1) {
            game.setScreen(game.getGameScreen());
        }
        if (mip.isTouchedInArea(rectExit) != -1) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Assets.getInstance().clear();
    }

    @Override
    public void dispose() {
        Assets.getInstance().clear();
    }
}
