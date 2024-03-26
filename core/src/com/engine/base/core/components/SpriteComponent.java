package com.engine.base.core.components;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.engine.base.core.interfaces.Component;
import com.engine.base.core.maths.Quat;
import com.engine.base.core.maths.Vec2;
import com.engine.base.core.maths.Vec3;
import com.engine.base.core.Node;

public class SpriteComponent extends Texture implements Component {
    private Node node;
    public SpriteComponent(String internalPath) {
        super(internalPath);
    }

    public SpriteComponent(FileHandle file) {
        super(file);
    }

    public SpriteComponent(FileHandle file, boolean useMipMaps) {
        super(file, useMipMaps);
    }

    public SpriteComponent(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
        super(file, format, useMipMaps);
    }

    public SpriteComponent(Pixmap pixmap) {
        super(pixmap);
    }

    public SpriteComponent(Pixmap pixmap, boolean useMipMaps) {
        super(pixmap, useMipMaps);
    }

    public SpriteComponent(Pixmap pixmap, Pixmap.Format format, boolean useMipMaps) {
        super(pixmap, format, useMipMaps);
    }

    public SpriteComponent(int width, int height, Pixmap.Format format) {
        super(width, height, format);
    }

    public SpriteComponent(TextureData data) {
        super(data);
    }

    protected SpriteComponent(int glTarget, int glHandle, TextureData data) {
        super(glTarget, glHandle, data);
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float delta) {
        if (this.node == null) return;
        Vec3 pos = this.node.getGlobalTransform().getTranslation(new Vec3());
        pos.z = 0;
        Vec3 scale = this.node.getGlobalTransform().getScale(new Vec3());
        Quat rotation = this.node.getGlobalTransform().getRotation(new Quat());
        float angle = rotation.getAxisAngle(Vec3.Z);
        Vec2 imageSize = new Vec2(
                this.getWidth(),
                this.getHeight()
        );
        Node.print2d.setColor(
                Node.print2d.getColor().r,
                Node.print2d.getColor().g,
                Node.print2d.getColor().b,
                this.node.getOpacity()
        );
        Vec3 size = this.node.getSize();
        Vec3 anchor = this.node.getAnchor();
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
                angle,
                0,
                0,
                (int) imageSize.x,
                (int) imageSize.y,
                this.node.isFlipX(),
                this.node.isFlipY()
        );
        Node.print2d.setColor(
                Node.print2d.getColor().r,
                Node.print2d.getColor().g,
                Node.print2d.getColor().b,
                1
        );
//        drawShape(
//                dt,
//                pos,
//                new Vec3(
//                        size.x,
//                        size.y,
//                        0
//                ),
//                scale,
//                rotation
//        );
    }

    @Override
    public void destroy() {

    }

    @Override
    public Component setNode(Node node) {
        this.node = node;
        return this;
    }
}
