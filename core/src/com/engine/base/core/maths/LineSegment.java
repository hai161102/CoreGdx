package com.engine.base.core.maths;

public class LineSegment {
    Vec2 start;
    Vec2 end;
    String name;

    /**
     * @param a the first point of this line
     * @param b the second point of this line
     */
    public LineSegment(Vec2 a, Vec2 b) {
        this.start = a;
        this.end = b;
        this.name = "LineSegment";
    }

    public LineSegment(Vec2 a, Vec2 b, String name) {
        this.start = a;
        this.end = b;
        this.name = name;
    }

    /**
     * Get the bounding box of this line by two points. The first point is in
     * the lower left corner, the second one in the upper right corner.
     *
     * @return the bounding box
     */
    public Vec2[] getBoundingBox() {
        Vec2[] result = new Vec2[2];
        result[0] = new Vec2(Math.min(start.x, end.x), Math.min(start.y,
                end.y));
        result[1] = new Vec2(Math.max(start.x, end.x), Math.max(start.y,
                end.y));
        return result;
    }

    @Override
    public String toString() {
        if (name.equals("LineSegment")) {
            return "LineSegment [" + start + " to " + end + "]";
        } else {
            return name;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LineSegment other = (LineSegment) obj;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (end == null) {
            return other.end == null;
        } else return end.equals(other.end);
    }

    public Line toLine() {
        return new Line(
                this.start,
                this.end
        );
    }

    public void set(float f1, float f2, float s1, float s2) {
        this.start.set(f1, f2);
        this.end.set(s1, s2);
    }

    public boolean containPoint(Vec2 point) {
        return Geometry.onSegment(this.start, point, this.end)
                || Geometry.onSegment(this.end, point, this.start);
    }
    public boolean isContainNode(LineSegment other) {
        if (Geometry.doLinesIntersect(this, other)
        && Geometry.lineSegmentTouchesOrCrossesLine(this, other)
        && Geometry.lineSegmentTouchesOrCrossesLine(other, this)) {
            return this.containPoint(other.start)
                    || this.containPoint(other.end);
        }
        return false;
    }
}