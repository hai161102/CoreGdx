package com.engine.base.core.tween;

import com.badlogic.gdx.Gdx;
import com.engine.base.core.interfaces.Tween;
import com.engine.base.core.interfaces.TweenListener;

import java.util.Stack;

public abstract class TweenBase<T> implements Tween<T> {
    protected T _target;
    protected final Stack<TweenData<T>> stack = new Stack<>();
    protected TweenListener.OnUpdateListener<T> onUpdateListener;
    protected TweenListener.OnCompleteListener<T> onCompleteListener;
    protected Thread thread;
    protected boolean isPlaying = false;
    protected static float deltaTime = 0f;
    @SuppressWarnings({"BusyWait", "CallToPrintStackTrace"})
    protected final Runnable runnable = () -> {
        Stack<TweenData<T>> playStack = new Stack<>();
        while (!this.stack.empty()) {
            playStack.push(this.stack.pop());
        }
        while (!playStack.empty() && this.isPlaying) {
            TweenData<T> data = playStack.pop();
            float delta = 0f;
            this.logicStart(data);
            while (delta <= data.getDuration() && this.isPlaying) {
                float percent = deltaTime / data.getDuration();
                this.logicUpdate(data, TweenBase.deltaTime, percent);
                delta += TweenBase.deltaTime;
                Gdx.app.postRunnable(() -> {
                    if (this.onUpdateListener != null) {
                        this.onUpdateListener.onUpdate(this._target, TweenBase.deltaTime);
                    }
                });
                try {
                    Thread.sleep((long) (TweenBase.deltaTime * 1000));
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            try {
                Thread.sleep((long) (data.getDelay() * 1000));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        this.playEnd();
        if (!this.isPlaying) return;
        Gdx.app.postRunnable(() -> {
            if (this.onCompleteListener != null) {this.onCompleteListener.onComplete(this._target);}
        });
        this.isPlaying = false;
        this.thread.interrupt();
    };

    protected abstract void playEnd();

    protected abstract void logicUpdate(TweenData<T> data, float deltaTime, float percent);

    protected abstract void logicStart(TweenData<T> data);

    public static void update(float dt) {
        TweenBase.deltaTime = dt;
    }

    @Override
    public Tween<T> target(T target) {
        this._target = target;
        return this;
    }

    @Override
    public Tween<T> to(T to, float duration) {
        this.stack.push(new TweenData<>(to, duration));
        return this;
    }

    @Override
    public Tween<T> delay(float delay) {
        this.stack.get(this.stack.size() - 1).setDelay(delay);
        return this;
    }

    @Override
    public Tween<T> onUpdate(TweenListener.OnUpdateListener<T> onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
        return this;
    }

    @Override
    public Tween<T> onComplete(TweenListener.OnCompleteListener<T> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        return this;
    }

    @Override
    public void start() {
        if (this.isPlaying) return;
        if (this.stack.empty()) return;
        this.thread = new Thread(runnable);
        this.thread.start();
        this.isPlaying = true;
    }

    @Override
    public void stop() {
        this.isPlaying = false;
        if (this.thread != null) {
            this.thread.interrupt();
        }
        this.stack.clear();
    }
}
