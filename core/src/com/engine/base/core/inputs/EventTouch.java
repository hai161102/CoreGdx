package com.engine.base.core.inputs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventTouch {
    public final List<Touch> touches = new ArrayList<>();
    private Touch currentTouch;

    @SuppressWarnings("NewApi")
    public void add(Touch touch) {
        this.touches.add(touch);
        this.touches.stream().max(Comparator.comparingInt(Touch::getPointer)).ifPresent(t -> this.currentTouch = t);
    }
    public Touch getCurrentTouch() {
        return currentTouch;
    }
    public int length() {
        return this.touches.size();
    }

    @SuppressWarnings({"NewApi"})
    public void remove(int pointer) {
        this.touches.removeIf(t -> t.getPointer() == pointer);
        if (this.length() > 0)
            this.touches.stream().max(Comparator.comparingInt(Touch::getPointer)).ifPresent(t -> this.currentTouch = t);
        else this.currentTouch = null;
    }
    public void clear() {
        this.touches.clear();
        this.currentTouch = null;
    }
}
