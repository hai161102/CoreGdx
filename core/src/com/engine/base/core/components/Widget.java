package com.engine.base.core.components;

import com.badlogic.gdx.Gdx;
import com.engine.base.core.Node;
import com.engine.base.core.interfaces.Component;
import com.engine.base.core.maths.Rect;

public class Widget implements Component {

    private Node node;
    public final Constrain left = new Constrain(Constrain.ConstrainType.LEFT);
    public final Constrain top = new Constrain(Constrain.ConstrainType.TOP);
    public final Constrain right = new Constrain(Constrain.ConstrainType.RIGHT);
    public final Constrain bottom = new Constrain(Constrain.ConstrainType.BOTTOM);

    public Widget() {
    }

    public Widget(Node node) {
        this.setNode(node);
    }


    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void destroy() {

    }

    public void apply() {
        if (this.node == null || this.node.getParent() == null) return;
        Rect parentRect = this.node.getParent().getRect2D(true);
        Gdx.app.log("local rect", parentRect.toString());
        if (this.left.active && this.right.active) {
            this.node.getSize().x = parentRect.width - (this.left.margin + this.right.margin);
        }
        else if (this.left.active || this.right.active){
            if (this.left.active) {
                this.node.getPosition().x = parentRect.left
                        + this.left.margin + this.node.getSize().x * this.node.getAnchor().x;
            }
            else {
                this.node.getPosition().x = parentRect.right
                        - (this.right.margin + this.node.getSize().x * this.node.getAnchor().x);
            }
        }
        if (this.top.active && this.bottom.active) {
            this.node.getSize().y = parentRect.height - (this.top.margin + this.bottom.margin);
        }
        else if (this.top.active || this.bottom.active) {
            if (this.top.active) {
                this.node.getPosition().y = parentRect.top
                        - (this.top.margin + this.node.getSize().y * this.node.getAnchor().y);
            }
            else  {
                this.node.getPosition().y = parentRect.bottom
                        + (this.bottom.margin + this.node.getSize().y * this.node.getAnchor().y);
            }
        }

    }
    @Override
    public Component setNode(Node node) {
        this.node = node;
        if (this.node.getParent() == null) return this;
        if (this.top.active) this.top.margin = Math.abs(
                this.node.getParent().getRect2D().top - this.node.getRect2D().top);
        if (this.left.active) this.left.margin = Math.abs(
                this.node.getParent().getRect2D().left - this.node.getRect2D().left);
        if (this.bottom.active) this.bottom.margin = Math.abs(
                this.node.getParent().getRect2D().bottom - this.node.getRect2D().bottom);
        if (this.right.active) this.right.margin = Math.abs(
                this.node.getParent().getRect2D().right - this.node.getRect2D().right);
        return this;
    }

    public void activeAll() {
        this.top.active = this.left.active = this.right.active = this.bottom.active = true;
    }

    public static class Constrain {

        public ConstrainType constrainType;
        public float margin = 0f;
        public boolean active = false;
        public Constrain(ConstrainType constrainType) {
            this.constrainType = constrainType;
        }
        public Constrain(ConstrainType constrainType, float margin) {
            this.constrainType = constrainType;
            this.margin = margin;
        }
        public static class ConstrainData {


        }
        public enum ConstrainType {
            LEFT,
            RIGHT,
            TOP,
            BOTTOM
        }
    }
}
