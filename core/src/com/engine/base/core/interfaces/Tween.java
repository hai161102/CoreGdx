package com.engine.base.core.interfaces;

public interface Tween<T> {

    Tween<T> target(T target);

    /**
     *
     * @param to goal object
     * @param duration total time from target to goal in seconds
     * @return this
     */
    Tween<T> to(T to, float duration);
    /**
     *
     * @param delay sleep time in seconds
     * @return this
     */
    Tween<T> delay(float delay);
    Tween<T> onUpdate(TweenListener.OnUpdateListener<T> onUpdateListener);
    Tween<T> onComplete(TweenListener.OnCompleteListener<T> onCompleteListener);
    void start();
    void stop();
}
