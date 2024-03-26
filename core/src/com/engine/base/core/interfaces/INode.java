package com.engine.base.core.interfaces;

import com.engine.base.core.Node;
import com.engine.base.core.maths.Mat4;
import com.engine.base.core.maths.Quat;
import com.engine.base.core.maths.Vec3;
import org.jetbrains.annotations.NotNull;

public interface INode {
    <T extends INode> T apply();
    void start();
    void update(float delta);
    void destroy();
    default Mat4 calculateLocalTransform() {
        if (!this.isAnimated()) {
            this.setLocalTransform(
                    this.getLocalTransform().set(this.getPosition(), this.getRotation(), this.getScale()));
        }
        return this.getLocalTransform();
    }
    default Mat4 calculateWorldTransform() {
        if (this.isInheritTransform() && this.getParent() != null) {
            this.setGlobalTransform(
                    this.getGlobalTransform().set(this.getParent().getGlobalTransform()).mul(this.getLocalTransform())
            );
        } else {
            this.setGlobalTransform(this.getGlobalTransform().set(this.getLocalTransform()));
        }

        return this.getGlobalTransform();
    }
    String getId();
    boolean isInheritTransform();
    boolean isAnimated();
    Vec3 getPosition();
    Quat getRotation();
    Vec3 getScale();
    Vec3 getSize();
    Vec3 getAnchor();
    float getOpacity();
    boolean isEnableShape();
    boolean isFlipX();
    void setFlipX(boolean flipX);
    boolean isFlipY();
    void setFlipY(boolean flipY);
    void setId(String id);
    void setInheritTransform(boolean inheritTransform);
    void setAnimated(boolean animated);
    void setOpacity(float opacity);
    void setEnableShape(boolean enableShape);
    void setPosition(Vec3 position);
    void setRotation(Quat rotation);
    void setScale(Vec3 scale);
    void setSize(Vec3 size);
    void setAnchor(Vec3 anchor);
    void setWorldPosition(Vec3 worldPosition);
    Vec3 getWorldPosition();
    void translate(Vec3 vec3);
    void translate(float x, float y, float z);
    void scale(float x, float y, float z);
    void scale(float x, float y);
    void scale(@NotNull Vec3 scale);
    void rotateTo(Vec3 axis, float rad);
    void rotate(Vec3 axis, float rad);
    INode getParent();
    Mat4 getLocalTransform();
    Mat4 getGlobalTransform();
    Mat4 setLocalTransform(Mat4 mat4);
    Mat4 setGlobalTransform(Mat4 mat4);
}
