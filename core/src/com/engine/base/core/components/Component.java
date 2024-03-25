package com.engine.base.core.components;


import com.engine.base.core.Node;
import com.engine.base.core.interfaces.IComponent;

public abstract class Component implements IComponent {
    public Node node;

    @Override
    public Component setNode(Node node) {
        this.node = node;
        return this;
    }

}
