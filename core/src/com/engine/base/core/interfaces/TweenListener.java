package com.engine.base.core.interfaces;

public interface TweenListener<T> {
    interface OnUpdateListener<T> {
        void onUpdate(T object, float delta);
    }
    interface OnCompleteListener<T> {
        void onComplete(T object);
    }
}
