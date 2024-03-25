package com.engine.base.core.inputs;

import com.engine.base.core.maths.Vec2;

public class Touch {
    private Vec2 location;
    private int pointer;
    private int button;
    private Vec2 startLocation;
    public Touch() {
    }

    public Touch(Vec2 location, int pointer, int button) {
        this.location = location;
        this.pointer = pointer;
        this.button = button;
        this.startLocation = this.location.cpy();
    }

    public Vec2 getLocation() {
        return location;
    }

    public void setLocation(Vec2 location) {
        this.location = location;
        if (this.startLocation == null) this.startLocation = this.location.cpy();
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public Vec2 getStartLocation() {
        return startLocation;
    }

}
