package com.engine.base.core.components;

import com.badlogic.gdx.Gdx;
import com.engine.base.core.Node;
import com.engine.base.core.maths.Rect;

public class Widget extends Component {

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
//        if (this.node == null || this.node.getParent() == null) return;
//        Rect parentRect = this.node.getParent().getRect2D(true);
//        Gdx.app.log("local rect", parentRect.toString());
//        if (this.left.active && this.right.active) {
//            this.node.size.x = parentRect.width - (this.left.margin + this.right.margin);
//        }
//        else if (this.left.active || this.right.active){
//            if (this.left.active) {
//                this.node.position.x = parentRect.left
//                        + this.left.margin + this.node.size.x * this.node.anchor.x;
//            }
//            else {
//                this.node.position.x = parentRect.right
//                        - (this.right.margin + this.node.size.x * this.node.anchor.x);
//            }
//        }
//        if (this.top.active && this.bottom.active) {
//            this.node.size.y = parentRect.height - (this.top.margin + this.bottom.margin);
//        }
//        else if (this.top.active || this.bottom.active) {
//            if (this.top.active) {
//                this.node.position.y = parentRect.top
//                        - (this.top.margin + this.node.size.y * this.node.anchor.y);
//            }
//            else  {
//                this.node.position.y = parentRect.bottom
//                        + (this.bottom.margin + this.node.size.y * this.node.anchor.y);
//            }
//        }

    }
    @Override
    public Widget setNode(Node node) {
        super.setNode(node);
//        if (this.node.parent == null) return this;
//        if (this.top.active) this.top.margin = Math.abs(
//                this.node.parent.getRect2D().top - this.node.getRect2D().top);
//        if (this.left.active) this.left.margin = Math.abs(
//                this.node.parent.getRect2D().left - this.node.getRect2D().left);
//        if (this.bottom.active) this.bottom.margin = Math.abs(
//                this.node.parent.getRect2D().bottom - this.node.getRect2D().bottom);
//        if (this.right.active) this.right.margin = Math.abs(
//                this.node.parent.getRect2D().right - this.node.getRect2D().right);
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
