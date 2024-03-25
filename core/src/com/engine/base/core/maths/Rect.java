package com.engine.base.core.maths;

import com.badlogic.gdx.math.Rectangle;

public class Rect extends Rectangle {
    public float left = 0f;
    public float top = 0f;
    public float right = 0f;
    public float bottom = 0f;

    private final LineSegment[] lineSegments = new LineSegment[4];
    /**
     * Constructs a new rectangle with all values set to zero
     */
    public Rect() {
        super();
        update();
    }

    /**
     * Constructs a new rectangle with the given corner point in the bottom left and dimensions.
     *
     * @param x      The corner point x-coordinate
     * @param y      The corner point y-coordinate
     * @param width  The width
     * @param height The height
     */
    public Rect(float x, float y, float width, float height) {
        super(x, y, width, height);
        update();
    }

    /**
     * Constructs a rectangle based on the given rectangle
     *
     * @param rect The rectangle
     */
    public Rect(Rectangle rect) {
        super(rect);
        update();
    }
    public Rect(Rect rect) {
        super(rect);
        update();
    }

    public Rect update() {
        this.left = this.x;
        this.top = this.y + this.height;
        this.right = this.x + this.width;
        this.bottom = this.y;
        for (int i = 0; i < lineSegments.length; i++) {
            if (lineSegments[i] == null)
                lineSegments[i] = new LineSegment(Vec2.Zero.cpy(), Vec2.Zero.cpy());
        }
        lineSegments[0].set(this.left, this.top, this.right, this.top);
        lineSegments[1].set(this.left, this.bottom, this.right, this.bottom);
        lineSegments[2].set(this.left, this.top, this.left, this.bottom);
        lineSegments[3].set(this.right, this.top, this.right, this.bottom);
        return this;
    }

    public LineSegment[] getLineSegments() {
        return lineSegments;
    }
}
