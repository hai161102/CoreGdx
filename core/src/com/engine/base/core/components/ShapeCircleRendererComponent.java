package com.engine.base.core.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.engine.base.core.Node;
import com.engine.base.core.interfaces.Component;
import com.engine.base.core.maths.Quat;
import com.engine.base.core.maths.Vec3;

public class ShapeCircleRendererComponent implements Component {

    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Filled;
    public Color bgColor = new Color(0, 0, 0, 1);
    public int segments = 64;
    private Node node;
    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {
        if (this.node == null) return;
        Vec3 pos = this.node.getGlobalTransform().getTranslation(new Vec3());
        pos.z = 0;
        Vec3 scale = this.node.getGlobalTransform().getScale(new Vec3());
        Quat rotation = this.node.getGlobalTransform().getRotation(new Quat());
        float angle = rotation.getAxisAngle(Vec3.Z);
        Vec3 size = this.node.getSize();
        Vec3 anchor = this.node.getAnchor();
        Node.shapeRenderer.set(this.shapeType);
        Node.shapeRenderer.setColor(bgColor);
        Node.shapeRenderer.circle(
                pos.x - size.x * anchor.x,
                pos.y - size.y * anchor.y,
                size.x / 2f,
                segments
        );
    }

    @Override
    public void destroy() {

    }

    @Override
    public Component setNode(Node node) {
        this.node = node;
        return null;
    }
}
