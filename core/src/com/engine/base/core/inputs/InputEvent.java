package com.engine.base.core.inputs;

public class InputEvent {
    private static InputEvent instance;
    public static InputEvent getInstance() {
        if (InputEvent.instance == null) InputEvent.instance = new InputEvent();
        return instance;
    }
    private final EventTouch eventTouch;

    public InputEvent() {
        this.eventTouch = new EventTouch();
    }

    public void clear(){
        this.eventTouch.clear();
    }

    public EventTouch getEventTouch() {
        return eventTouch;
    }
}
