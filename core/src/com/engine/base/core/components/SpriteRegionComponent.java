package com.engine.base.core.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.engine.base.core.interfaces.Component;
import com.engine.base.core.maths.Quat;
import com.engine.base.core.maths.Vec3;
import com.engine.base.core.Node;

public class SpriteRegionComponent extends TextureRegion implements Component {
    private Node node;
    public SpriteRegionComponent() {
    }

    public SpriteRegionComponent(Texture texture) {
        super(texture);
    }

    public SpriteRegionComponent(Texture texture, int width, int height) {
        super(texture, width, height);
    }

    public SpriteRegionComponent(Texture texture, int x, int y, int width, int height) {
        super(texture, x, y, width, height);
    }

    public SpriteRegionComponent(Texture texture, float u, float v, float u2, float v2) {
        super(texture, u, v, u2, v2);
    }

    public SpriteRegionComponent(TextureRegion region) {
        super(region);
    }

    public SpriteRegionComponent(TextureRegion region, int x, int y, int width, int height) {
        super(region, x, y, width, height);
    }

    public SpriteRegionComponent(SpriteComponent texture) {
        super(texture);
    }

    public SpriteRegionComponent(SpriteComponent texture, int width, int height) {
        super(texture, width, height);
    }

    public SpriteRegionComponent(SpriteComponent texture, int x, int y, int width, int height) {
        super(texture, x, y, width, height);
    }

    public SpriteRegionComponent(SpriteComponent texture, float u, float v, float u2, float v2) {
        super(texture, u, v, u2, v2);
    }

    public SpriteRegionComponent(SpriteRegionComponent region) {
        super(region);
    }

    public SpriteRegionComponent(SpriteRegionComponent region, int x, int y, int width, int height) {
        super(region, x, y, width, height);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {
        if (this.node == null) return;
        Vec3 pos = this.node.globalTransform.getTranslation(new Vec3());
        pos.z = 0;
        Vec3 scale = this.node.globalTransform.getScale(new Vec3());
        Quat rotation = this.node.globalTransform.getRotation(new Quat());
        float angle = rotation.getAxisAngle(Vec3.Z);
        Node.print2d.setColor(
                Node.print2d.getColor().r,
                Node.print2d.getColor().g,
                Node.print2d.getColor().b,
                this.node.opacity
        );
        Vec3 size = this.node.size;
        Vec3 anchor = this.node.anchor;
        Node.print2d.draw(
                this,
                pos.x - size.x * anchor.x,
                pos.y - size.y * anchor.y,
                size.x * anchor.x,
                size.y * anchor.y,
                size.x,
                size.y,
                scale.x,
                scale.y,
                angle
        );
        Node.print2d.setColor(
                Node.print2d.getColor().r,
                Node.print2d.getColor().g,
                Node.print2d.getColor().b,
                1
        );
    }

    @Override
    public void destroy() {
        this.getTexture().dispose();
    }

    @Override
    public Component setNode(Node node) {
        this.node = node;
        return this;
    }
}
