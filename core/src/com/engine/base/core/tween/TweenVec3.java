package com.engine.base.core.tween;

import com.engine.base.core.maths.Vec3;

public class TweenVec3 extends TweenBase<Vec3> {

    private Vec3 minusVectorOnLogicStart;
    public static TweenVec3 set(Vec3 target) {
        TweenVec3 vec3Tween = new TweenVec3();
        vec3Tween.target(target);
        return vec3Tween;
    }

    @Override
    protected void playEnd() {

    }

    @Override
    protected void logicUpdate(TweenData<Vec3> data, float deltaTime, float percent) {
        this._target.add(minusVectorOnLogicStart.cpy().scl(percent).lerp(Vec3.Zero.cpy(), 0.01f));
    }

    @Override
    protected void logicStart(TweenData<Vec3> data) {
        minusVectorOnLogicStart = data.getTo().cpy().sub(this._target);
    }
}
