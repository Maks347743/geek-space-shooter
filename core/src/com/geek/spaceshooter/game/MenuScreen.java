package com.geek.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class MenuScreen implements Screen {
    private SpaceGame game;
    private SpriteBatch batch;
    private Background background;
    private Vector2 emptyVelocity = new Vector2(0, 0);
    private TextureRegion texStart;
    private TextureRegion texExit;
    private Rectangle rectStart;
    private Rectangle rectExit;
    private Rectangle gameOverRectStart;
    private Rectangle gameOverRectExit;
    private MyInputProcessor mip;
    private Music music;
    private TextureRegion gameOverTexture;
    private boolean gameOver;
    private int score;
    private StringBuilder scoreHelper;
    private BitmapFont fnt;

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
        fnt = Assets.getInstance().assetManager.get("font.fnt");
        texExit = atlas.findRegion("btExit");
        texStart = atlas.findRegion("btPlay");
        rectStart = new Rectangle(256, 232, texStart.getRegionWidth(), texStart.getRegionHeight());
        rectExit = new Rectangle(1280 - 512, 232, texExit.getRegionWidth(), texExit.getRegionHeight());
        gameOverRectStart = new Rectangle(256, 82, texStart.getRegionWidth(), texStart.getRegionHeight());
        gameOverRectExit = new Rectangle(1280 - 512, 82, texExit.getRegionWidth(), texExit.getRegionHeight());
        gameOverTexture = atlas.findRegion("gameOver");
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        scoreHelper = new StringBuilder(50);
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
            batch.draw(texStart, gameOverRectStart.x, gameOverRectStart.y);
            batch.draw(texExit, gameOverRectExit.x, gameOverRectExit.y);
            batch.setColor(1, 1, 1, 0.3f);
            batch.draw(gameOverTexture, rectStart.x + 128, rectStart.y + 128);
            batch.setColor(1, 1, 1, 1);

        } else {
            batch.draw(texStart, rectStart.x, rectStart.y);
            batch.draw(texExit, rectExit.x, rectExit.y);
        }
        renderScore(batch, fnt, 500, 50 + fnt.getLineHeight());
        batch.end();
    }

    public void update(float dt) {
        background.update(dt, emptyVelocity);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (game.getBestScore() < game.getGameScreen().getPlayer().getScore()) {
                game.getMenuScreen().writeScoreInfo(game.getGameScreen().getPlayer().getScore());
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            Assets.getInstance().clear();
        }
        if (gameOver) {
            if (mip.isTouchedInArea(gameOverRectStart) != -1) {
                game.setScreen(game.getGameScreen());
            }
            if (mip.isTouchedInArea(gameOverRectExit) != -1) {
                Assets.getInstance().clear();
                Gdx.app.exit();
            }
        } else {
            if (mip.isTouchedInArea(rectStart) != -1) {
                game.setScreen(game.getGameScreen());
            }
            if (mip.isTouchedInArea(rectExit) != -1) {
                Assets.getInstance().clear();
                Gdx.app.exit();

            }
        }
    }

    public String loadScoreInfo() {
        BufferedReader br = null;
        String strScore = null;
        try {
            if (Gdx.files.local("score.csv").exists()) {
                br = Gdx.files.local("score.csv").reader(8192);
                strScore = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strScore;
    }

    public void writeScoreInfo(int score) {
        FileHandle file = Gdx.files.local("score.csv");
        file.writeString(String.valueOf(score), false);
    }

    public void renderScore(SpriteBatch batch, BitmapFont fnt, float x, float y) {
        if (loadScoreInfo() != null) {
            score = Integer.parseInt(loadScoreInfo());
        }
        scoreHelper.setLength(0);
        scoreHelper.append("BEST SCORE: ").append(score);
        fnt.draw(batch, scoreHelper, x + 4, y - 4);
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
        Assets.getInstance().loadAssets(ScreenType.MENU);
        TextureAtlas atlas = Assets.getInstance().mainAtlas;
        background = new Background(atlas.findRegion("star16"));
        fnt = Assets.getInstance().assetManager.get("font.fnt");
        texExit = atlas.findRegion("btExit");
        texStart = atlas.findRegion("btPlay");
        rectStart = new Rectangle(256, 232, texStart.getRegionWidth(), texStart.getRegionHeight());
        rectExit = new Rectangle(1280 - 512, 232, texExit.getRegionWidth(), texExit.getRegionHeight());
        gameOverRectStart = new Rectangle(256, 82, texStart.getRegionWidth(), texStart.getRegionHeight());
        gameOverRectExit = new Rectangle(1280 - 512, 82, texExit.getRegionWidth(), texExit.getRegionHeight());
        gameOverTexture = atlas.findRegion("gameOver");
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        scoreHelper = new StringBuilder(50);
        music = Assets.getInstance().assetManager.get("menu_music.mp3", Music.class);
        music.setPosition(7.0f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void hide() {
        Assets.getInstance().clear();
        gameOver = false;
    }

    @Override
    public void dispose() {
        Assets.getInstance().clear();
    }
}
