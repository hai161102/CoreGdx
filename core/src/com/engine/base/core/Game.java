package com.engine.base.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.engine.base.core.maths.Vec2;

public class Game implements ApplicationListener {
    protected Scene screen;
    public final Vec2 resolution = new Vec2(
            GameCanvas.getInstance().resolution.width,
            GameCanvas.getInstance().resolution.height
    );
    public Game() {
    }

    @Override
    public void create() {
        Gdx.files.local("data/uuids/uuid.txt").writeString("", false);
    }

    @Override
    public void dispose () {
        if (screen != null) screen.hide();
    }

    @Override
    public void pause () {
        if (screen != null) screen.pause();
    }

    @Override
    public void resume () {
        if (screen != null) screen.resume();
    }

    @Override
    public void render () {
        float delta = Gdx.graphics.getDeltaTime();
        if (screen != null) screen.render(delta);
    }

    @Override
    public void resize (int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    /** Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     * @param screen may be {@code null} */
    public void setScreen (Scene screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /** @return the currently active {@link Screen}. */
    public Screen getScreen () {
        return screen;
    }
}
