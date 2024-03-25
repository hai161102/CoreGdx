package com.engine.base.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.base.core.inputs.EventTouch;
import com.engine.base.core.inputs.InputEvent;
import com.engine.base.core.inputs.Touch;
import com.engine.base.core.maths.Vec2;
import com.engine.base.core.maths.Vec3;
import com.engine.base.core.tween.TweenBase;

public class Scene implements Screen, InputProcessor {

    public final Environment environment;
    protected final Game root;
    public final Node node;
    public final PerspectiveCamera perspectiveCamera;
    public final OrthographicCamera orthographicCamera;
    public final Color screenColor = new Color(0.2f, 0.2f, 0.2f, 1);
    protected final Viewport viewport;

    public Scene(Game root) {
        this.root = root;
        this.environment = new Environment();
        this.node = new Node();
        this.perspectiveCamera = new PerspectiveCamera(
                45,
                GameCanvas.getInstance().resolution.width,
                GameCanvas.getInstance().resolution.height
        );
        this.orthographicCamera = new OrthographicCamera();
        this.viewport = new FitViewport(
                GameCanvas.getInstance().resolution.width,
                GameCanvas.getInstance().resolution.height,
                this.orthographicCamera
        );
        this.viewport.apply();
        this.orthographicCamera.position.set(
                this.orthographicCamera.viewportWidth / 2f,
                this.orthographicCamera.viewportHeight / 2f,
                0
        );
        this.node.size.set(
                GameCanvas.getInstance().resolution.width,
                GameCanvas.getInstance().resolution.height,
                0
        );
        this.node.position.set(this.node.size.cpy().scl(this.node.anchor));
//        this.viewport = new ScalingViewport(new )
        Node.shapeRenderer.setAutoShapeType(true);
    }
    protected void start() {
    }
    protected void update(float dt) {

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        this.start();
    }

    @Override
    public void render(float v) {
        TweenBase.update(v);
        this.orthographicCamera.update();
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl20.glClearColor(
                this.screenColor.r,
                this.screenColor.g,
                this.screenColor.b,
                this.screenColor.a
        );
        Node.print2d.setProjectionMatrix(this.orthographicCamera.combined);
        Node.shapeRenderer.setProjectionMatrix(this.orthographicCamera.combined);
        Node.print2d.begin();
        Node.shapeRenderer.begin();
        this.update(v);
        this.node.update(v);
        Node.print2d.end();
        Node.shapeRenderer.end();
    }
    @Override
    public void resize(int width, int height) {
        this.viewport.update(width,height);
        this.orthographicCamera.position.set(
                this.orthographicCamera.viewportWidth/2,
                this.orthographicCamera.viewportHeight/2,
                0
        );
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Node.print2d.dispose();
        Node.print3d.dispose();
        Node.shapeRenderer.dispose();
        this.node.destroy();
        InputEvent.getInstance().clear();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = this.viewport.unproject(
                new Vec3(screenX, screenY,0).convert()
        );
        this.orthographicCamera.update();
        Vec2 location = new Vec2(worldCoordinates.x, worldCoordinates.y);
        Touch touch = new Touch(location, pointer, button);
        InputEvent.getInstance().getEventTouch().add(touch);
        this.onTouchStart(InputEvent.getInstance().getEventTouch());
        return false;
    }

    public void onTouchStart(EventTouch event) {}
    @SuppressWarnings("NewApi")
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = this.viewport.unproject(
                new Vector3(screenX,screenY,0)
        );
        this.orthographicCamera.update();
        Vec2 location = new Vec2(worldCoordinates.x, worldCoordinates.y);
        InputEvent.getInstance().getEventTouch().touches
                .stream().filter(t -> t.getPointer() == pointer).findFirst().ifPresent(t -> t.setLocation(location));
        this.onTouchEnd(InputEvent.getInstance().getEventTouch());
        InputEvent.getInstance().getEventTouch().remove(pointer);
        return false;
    }

    @SuppressWarnings("NewApi")
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = this.viewport.unproject(
                new Vector3(screenX,screenY,0)
        );
        this.orthographicCamera.update();
        Vec2 location = new Vec2(worldCoordinates.x, worldCoordinates.y);
        InputEvent.getInstance().getEventTouch().touches
                .stream().filter(t -> t.getPointer() == pointer).findFirst().ifPresent(t -> t.setLocation(location));
        this.onTouchEnd(InputEvent.getInstance().getEventTouch());
        InputEvent.getInstance().getEventTouch().remove(pointer);
        return false;
    }

    public void onTouchEnd(EventTouch event) {}
    @SuppressWarnings("NewApi")
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldCoordinates = this.viewport.unproject(
                new Vector3(screenX,screenY,0)
        );
        this.orthographicCamera.update();
        Vec2 location = new Vec2(worldCoordinates.x, worldCoordinates.y);
        InputEvent.getInstance().getEventTouch().touches
                .stream().filter(t -> t.getPointer() == pointer).findFirst().ifPresent(t -> t.setLocation(location));
        this.onTouchMove(InputEvent.getInstance().getEventTouch());
        return false;
    }
    public void onTouchMove(EventTouch event) {}
    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
