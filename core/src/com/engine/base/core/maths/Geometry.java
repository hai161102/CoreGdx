package com.engine.base.core.maths;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Geometry {

    public static final float EPSILON = 0.000001f;

    /**
     * Calculate the cross product of two points.
     * @param a first point
     * @param b second point
     * @return the value of the cross product
     */
    @Contract(pure = true)
    public static double crossProduct(@NotNull Vec2 a, @NotNull Vec2 b) {
        return a.x * b.y - b.x * a.y;
    }

    /**
     * Check if bounding boxes do intersect. If one bounding box
     * touches the other, they do intersect.
     * @param a first bounding box
     * @param b second bounding box
     * @return <code>true</code> if they intersect,
     *         <code>false</code> otherwise.
     */
    @Contract(pure = true)
    public static boolean doBoundingBoxesIntersect(Vec2 @NotNull [] a, Vec2 @NotNull [] b) {
        return a[0].x <= b[1].x && a[1].x >= b[0].x && a[0].y <= b[1].y
                && a[1].y >= b[0].y;
    }

    /**
     * Checks if a Point is on a line
     * @param a line (interpreted as line, although given as line
     *                segment)
     * @param b point
     * @return <code>true</code> if point is online, otherwise
     *         <code>false</code>
     */
    public static boolean isPointOnLine(@NotNull LineSegment a, @NotNull Vec2 b) {
        // Move the image, so that a.first is on (0|0)
        LineSegment aTmp = new LineSegment(new Vec2(0, 0), new Vec2(
                a.end.x - a.start.x, a.end.y - a.start.y));
        Vec2 bTmp = new Vec2(b.x - a.start.x, b.y - a.start.y);
        double r = crossProduct(aTmp.end, bTmp);
        return Math.abs(r) < EPSILON;
    }

    /**
     * Checks if a point is right of a line. If the point is on the
     * line, it is not right of the line.
     * @param a line segment interpreted as a line
     * @param b the point
     * @return <code>true</code> if the point is right of the line,
     *         <code>false</code> otherwise
     */
    public static boolean isPointRightOfLine(@NotNull LineSegment a, @NotNull Vec2 b) {
        // Move the image, so that a.first is on (0|0)
        LineSegment aTmp = new LineSegment(new Vec2(0, 0), new Vec2(
                a.end.x - a.start.x, a.end.y - a.start.y));
        Vec2 bTmp = new Vec2(b.x - a.start.x, b.y - a.start.y);
        return crossProduct(aTmp.end, bTmp) < 0;
    }

    /**
     * Check if line segment first touches or crosses the line that is
     * defined by line segment second.
     *
     * @return <code>true</code> if line segment first touches or
     *                           crosses line second,
     *         <code>false</code> otherwise.
     */
    public static boolean lineSegmentTouchesOrCrossesLine(LineSegment a,
                                                          @NotNull LineSegment b) {
        return isPointOnLine(a, b.start)
                || isPointOnLine(a, b.end)
                || (isPointRightOfLine(a, b.start) ^ isPointRightOfLine(a,
                        b.end));
    }

    /**
     * Check if line segments intersect
     * @param a first line segment
     * @param b second line segment
     * @return <code>true</code> if lines do intersect,
     *         <code>false</code> otherwise
     */
    public static boolean doLinesIntersect(@NotNull LineSegment a, @NotNull LineSegment b) {
        Vec2[] box1 = a.getBoundingBox();
        Vec2[] box2 = b.getBoundingBox();
        return doBoundingBoxesIntersect(box1, box2)
                && lineSegmentTouchesOrCrossesLine(a, b)
                && lineSegmentTouchesOrCrossesLine(b, a);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Vec2 findIntersection(@NotNull LineSegment l1, @NotNull LineSegment l2) {
        float a1 = l1.end.y - l1.start.y;
        float b1 = l1.start.x - l1.end.x;
        float c1 = a1 * l1.start.x + b1 * l1.start.y;

        float a2 = l2.end.y - l2.start.y;
        float b2 = l2.start.x - l2.end.x;
        float c2 = a2 * l2.start.x + b2 * l2.start.y;

        float delta = a1 * b2 - a2 * b1;
        return new Vec2((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
    }
    /**
     * Given three collinear points p, q, r, the function checks if point q lies online segment 'pr'
     */
    public static boolean onSegment(Vec2 p, Vec2 q, Vec2 r)
    {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }
    /**
     * Check if x is right end of l
     * @param x an x-coordinate of one endpoint
     * @param l a line
     * @return <code>true</code> if p is right end of l
     *         <code>false</code> otherwise
     */
    @Contract(pure = true)
    private static boolean isRightEnd(double x, @NotNull LineSegment l) {
        return x >= l.start.x && x >= l.end.x;
    }

    /**
     * Get all intersectionLines by applying a sweep line algorithm.
     * @param lines all lines you want to check, in no order
     * @return a list that contains all pairs of intersecting lines
     */
    public static @NotNull Set<LineSegment[]> getAllIntersectingLines(LineSegment @NotNull [] lines) {
        class EventPointLine implements Comparable<EventPointLine> {
            final Double sortingKey;
            final LineSegment line;

            public EventPointLine(double sortingKey, LineSegment line) {
                this.sortingKey = sortingKey;
                this.line = line;
            }

            @Contract(pure = true)
            @Override
            public int compareTo(@NotNull EventPointLine o) {
                return sortingKey.compareTo(o.sortingKey);
            }
        }

        class SweepLineComparator implements Comparator<LineSegment> {
            @Contract(pure = true)
            @Override
            public int compare(@NotNull LineSegment o1, @NotNull LineSegment o2) {
                double o1FirstX = o1.start.x < o1.end.x ? o1.start.y
                        : o1.end.y;
                double o2FirstX = o2.start.x < o2.end.x ? o2.start.y
                        : o2.end.y;

                if (Math.abs(o1FirstX - o2FirstX) < EPSILON) {
                    return 0;
                } else if (o1FirstX > o2FirstX) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }

        Set<LineSegment[]> intersections = new HashSet<LineSegment[]>();
        List<EventPointLine> eventPointSchedule = new ArrayList<EventPointLine>();

        for (LineSegment line : lines) {
            eventPointSchedule.add(new EventPointLine(line.start.x, line));
            eventPointSchedule.add(new EventPointLine(line.end.x, line));
        }

        Collections.sort(eventPointSchedule);

        SweepLineComparator comparator = new SweepLineComparator();
        TreeSet<LineSegment> sweepLine = new TreeSet<LineSegment>(comparator);

        for (EventPointLine p : eventPointSchedule) {
            if (isRightEnd(p.sortingKey, p.line)) {
                LineSegment above = sweepLine.higher(p.line);
                LineSegment below = sweepLine.lower(p.line);
                sweepLine.remove(p.line);

                if (below != null && above != null
                        && doLinesIntersect(above, below)) {
                    LineSegment[] tmp = new LineSegment[2];
                    tmp[0] = above;
                    tmp[1] = p.line;
                    intersections.add(tmp);
                }
            } else {
                if (Math.abs(p.line.start.x - p.line.end.x) < EPSILON) {
                    // this is a vertical line

                    for (LineSegment tmpLine : sweepLine) {
                        if (doLinesIntersect(tmpLine, p.line)) {
                            LineSegment[] tmp = new LineSegment[2];
                            tmp[0] = tmpLine;
                            tmp[1] = p.line;
                            intersections.add(tmp);
                        }
                    }
                } else {

                    // Get identical lines
                    NavigableSet<LineSegment> h = sweepLine.subSet(p.line,
                            true, p.line, true);

                    for (LineSegment tmpLine : h) {
                        if (doLinesIntersect(tmpLine, p.line)) {
                            LineSegment[] tmp = new LineSegment[2];
                            tmp[0] = tmpLine;
                            tmp[1] = p.line;
                            intersections.add(tmp);
                        }

                    }

                    sweepLine.add(p.line);

                    // check if it intersects with line above or below
                    LineSegment above = sweepLine.higher(p.line);
                    LineSegment below = sweepLine.lower(p.line);

                    if (above != null && doLinesIntersect(above, p.line)) {
                        LineSegment[] tmp = new LineSegment[2];
                        tmp[0] = above;
                        tmp[1] = p.line;
                        intersections.add(tmp);
                    }

                    if (below != null && doLinesIntersect(below, p.line)) {
                        LineSegment[] tmp = new LineSegment[2];
                        tmp[0] = below;
                        tmp[1] = p.line;
                        intersections.add(tmp);
                    }
                }
            }
        }

        /* Check if endpoints are equal */
        for (int i = 0; i < eventPointSchedule.size(); i++) {
            int j = i + 1;
            while (j < eventPointSchedule.size()
                    && Math.abs(eventPointSchedule.get(i).sortingKey
                            - eventPointSchedule.get(j).sortingKey) < EPSILON) {
                j += 1;

                LineSegment[] tmp = new LineSegment[2];
                tmp[0] = eventPointSchedule.get(i).line;
                tmp[1] = eventPointSchedule.get(j).line;
                if (doLinesIntersect(tmp[0], tmp[1])
                        && !intersections.contains(tmp)) {
                    intersections.add(tmp);
                }
            }
        }

        return intersections;
    }

    /**
     * Get all intersectionLines by applying a brute force algorithm.
     * @param lines all lines you want to check, in no order
     * @return a list that contains all pairs of intersecting lines
     */
    public static @NotNull Set<LineSegment[]> getAllIntersectingLinesByBruteForce(
            LineSegment @NotNull [] lines) {
        Set<LineSegment[]> intersections = new HashSet<LineSegment[]>();

        for (int i = 0; i < lines.length; i++) {
            for (int j = i + 1; j < lines.length; j++) {
                if (doLinesIntersect(lines[i], lines[j])) {
                    LineSegment[] tmp = new LineSegment[2];
                    tmp[0] = lines[i];
                    tmp[1] = lines[j];
                    intersections.add(tmp);
                }
            }
        }

        return intersections;
    }

    public static boolean isLeftBend(@NotNull Vec2 i, @NotNull Vec2 j, @NotNull Vec2 k) {
        Vec2 pi = new Vec2(i.x, i.y);
        Vec2 pj = new Vec2(j.x, j.y);
        Vec2 pk = new Vec2(k.x, k.y);

        // Move pi to (0,0) and pj and pk with it
        pj.x -= pi.x;
        pk.x -= pi.x;
        pj.y -= pi.y;
        pk.y -= pi.y;
        LineSegment s = new LineSegment(pi, pj);

        // Move pj to (0,0) and pk with it
        pk.x -= pj.x;
        pk.y -= pj.y;

        return !(isPointRightOfLine(s, pk) || isPointOnLine(s, pk));
    }

    /**
     * Calculate the convex hull of points with Graham Scan
     * @param points a list of points in any order
     * @return the convex hull (can be rotated)
     */
    @SuppressWarnings("NewApi")
    public static @NotNull List<Vec2> getConvexHull(@NotNull List<Vec2> points) {
        // TODO: Doesn't work by now
        List<Vec2> l = new ArrayList<Vec2>();

        // find the lowest point. If there is more than one lowest point
        // take the one that is left
        Vec2 pLow = new Vec2(0, Float.POSITIVE_INFINITY);
        for (Vec2 point : points) {
            if (point.y < pLow.y || (point.y == pLow.y && point.x < pLow.x)) {
                pLow = point;
            }
        }

        // Order all other points by angle
        class PointComparator implements Comparator<Vec2> {
            final Vec2 pLow;

            public PointComparator(Vec2 pLow) {
                this.pLow = pLow;
            }

            @Contract(pure = true)
            private double getAngle(@NotNull Vec2 p) {
                // TODO: This is buggy
                double deltaX = pLow.x - p.x;
                double deltaY = pLow.y - p.y;
                if (deltaX < EPSILON) {
                    return 0;
                } else {
                    return deltaY / deltaX;
                }
            }

            @Override
            public int compare(Vec2 o1, Vec2 o2) {
                double a1 = getAngle(o1);
                double a2 = getAngle(o2);
                if (Math.abs(a1 - a2) < EPSILON) {
                    return 0;
                } else {
                    return a1 < a2 ? -1 : 1;
                }
            }
        }

        PointComparator comparator = new PointComparator(pLow);

        points.sort(comparator);

        // go through all points
        for (Vec2 tmp : points) {
            boolean loop = true;

            while (loop) {
                if (l.size() < 3) {
                    l.add(tmp);
                    loop = false;
                } else if (!isLeftBend(l.get(l.size() - 2),
                        l.get(l.size() - 1), tmp)) {
                    l.add(tmp);
                    loop = false;
                } else {
                    l.remove(l.size() - 1);
                }
            }
        }

        return l;
    }
}