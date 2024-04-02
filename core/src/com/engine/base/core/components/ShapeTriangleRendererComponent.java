package com.engine.base.core.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.engine.base.core.Node;
import com.engine.base.core.interfaces.Component;
import com.engine.base.core.maths.Quat;
import com.engine.base.core.maths.Vec3;

public class ShapeTriangleRendererComponent implements Component {

    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Filled;
    public Color bgColor = new Color(0, 0, 0, 1);
    public int segments = 64;
    public Triangle triangle = new Triangle();
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
        Node.shapeRenderer.triangle(
                triangle.point1.x, triangle.point1.y,
                triangle.point2.x, triangle.point2.y,
                triangle.point3.x, triangle.point3.y
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

    public static class Triangle {
        public final Vec3 point1 = new Vec3();
        public final Vec3 point2 = new Vec3();
        public final Vec3 point3 = new Vec3();

        public Triangle() {
        }
        public Triangle(Vec3 p1, Vec3 p2, Vec3 p3) {
            this.point1.set(p1);
            this.point2.set(p2);
            this.point3.set(p3);
        }
    }
}
