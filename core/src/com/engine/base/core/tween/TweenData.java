package com.engine.base.core.tween;

public class TweenData<T> {
        private T to;
        private float duration = 0f;
        private float delay = 0f;

        public TweenData(T to, float duration, float delay) {
            this.to = to;
            this.duration = duration;
            this.delay = delay;
        }

        public TweenData(T to, float duration) {
            this.to = to;
            this.duration = duration;
            this.delay = 0;
        }

        public TweenData() {
        }

        public T getTo() {
            return to;
        }

        public void setTo(T to) {
            this.to = to;
        }

        public float getDuration() {
            return duration;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }

        public float getDelay() {
            return delay;
        }

        public void setDelay(float delay) {
            this.delay = delay;
        }
    }