package com.engine.base.core.tween;

import com.engine.base.core.maths.Vec2;

public class TweenVec2 extends TweenBase<Vec2> {
    private Vec2 minus;
    @Override
    protected void playEnd() {

    }

    @Override
    protected void logicUpdate(TweenData<Vec2> data, float deltaTime, float percent) {
        this._target.add(minus.cpy().scl(percent).lerp(Vec2.Zero.cpy(), 0.01f));
    }

    @Override
    protected void logicStart(TweenData<Vec2> data) {
        minus = data.getTo().cpy().sub(this._target);
    }
}
