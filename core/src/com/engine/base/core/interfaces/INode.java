package com.engine.base.core.interfaces;

public interface INode {
    INode apply();
    void start();
    void update(float delta);
    void destroy();
}
