package com.engine.base.core.interfaces;


import com.engine.base.core.Node;

public interface IComponent {
    void start();
    void update(float delta);
    void destroy();
    IComponent setNode(Node node);
}
