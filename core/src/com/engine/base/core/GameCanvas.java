package com.engine.base.core;

public class GameCanvas {
    private static GameCanvas instance;
    public static GameCanvas getInstance() {
        if (GameCanvas.instance == null) GameCanvas.instance = new GameCanvas();
        return instance;
    }
    public final Resolution resolution = new Resolution();
    public static class Resolution {
        public int width = 1080;
        public int height = 1920;

        public void set(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
