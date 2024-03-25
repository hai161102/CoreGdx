package com.engine.base.core.interfaces;


import com.engine.base.core.Node;

public interface Component {
    void start();
    void update(float delta);
    void destroy();
    Component setNode(Node node);
}
