package com.engine.base.core.tween;

import com.badlogic.gdx.graphics.Color;

public class TweenColor extends TweenBase<Color>{
    private Color minusColor;
    @Override
    protected void playEnd() {

    }

    @Override
    protected void logicUpdate(TweenData<Color> data, float deltaTime, float percent) {
        this._target.add(minusColor.cpy().mul(percent));
    }

    @Override
    protected void logicStart(TweenData<Color> data) {
        this.minusColor = data.getTo().cpy().sub(this._target);
    }
}
