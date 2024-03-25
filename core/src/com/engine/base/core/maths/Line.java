package com.engine.base.core.maths;

public class Line {
    public final Vec2 point1;
    public final Vec2 point2;

    public Line() {
        this(new Vec2(), new Vec2());
    }

    public Line(Vec2 point1, Vec2 point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public void set(Vec2 p1, Vec2 p2) {
        this.point1.set(p1);
        this.point2.set(p2);
    }
}
