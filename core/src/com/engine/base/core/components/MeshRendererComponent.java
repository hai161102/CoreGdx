package com.engine.base.core.components;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.engine.base.core.Node;
import com.engine.base.core.interfaces.Component;
import com.engine.base.core.maths.Mat4;
import com.engine.base.core.maths.Vec3;

public class MeshRendererComponent extends ModelInstance implements Component {
    private Node node;
    public MeshRendererComponent(Model model) {
        super(model);
    }

    public MeshRendererComponent(Model model, String nodeId, boolean mergeTransform) {
        super(model, nodeId, mergeTransform);
    }

    public MeshRendererComponent(Model model, Mat4 transform, String nodeId, boolean mergeTransform) {
        super(model, transform.convert(), nodeId, mergeTransform);
    }

    public MeshRendererComponent(Model model, String nodeId, boolean parentTransform, boolean mergeTransform) {
        super(model, nodeId, parentTransform, mergeTransform);
    }

    public MeshRendererComponent(Model model, Mat4 transform, String nodeId, boolean parentTransform, boolean mergeTransform) {
        super(model, transform.convert(), nodeId, parentTransform, mergeTransform);
    }

    public MeshRendererComponent(Model model, String nodeId, boolean recursive, boolean parentTransform, boolean mergeTransform) {
        super(model, nodeId, recursive, parentTransform, mergeTransform);
    }

    public MeshRendererComponent(Model model, Mat4 transform, String nodeId, boolean recursive, boolean parentTransform, boolean mergeTransform) {
        super(model, transform.convert(), nodeId, recursive, parentTransform, mergeTransform);
    }

    public MeshRendererComponent(Model model, Mat4 transform, String nodeId, boolean recursive, boolean parentTransform, boolean mergeTransform, boolean shareKeyframes) {
        super(model, transform.convert(), nodeId, recursive, parentTransform, mergeTransform, shareKeyframes);
    }

    public MeshRendererComponent(Model model, String... rootNodeIds) {
        super(model, rootNodeIds);
    }

    public MeshRendererComponent(Model model, Mat4 transform, String... rootNodeIds) {
        super(model, transform.convert(), rootNodeIds);
    }

    public MeshRendererComponent(Model model, Array<String> rootNodeIds) {
        super(model, rootNodeIds);
    }

    public MeshRendererComponent(Model model, Mat4 transform, Array<String> rootNodeIds) {
        super(model, transform.convert(), rootNodeIds);
    }

    public MeshRendererComponent(Model model, Mat4 transform, Array<String> rootNodeIds, boolean shareKeyframes) {
        super(model, transform.convert(), rootNodeIds, shareKeyframes);
    }

    public MeshRendererComponent(Model model, Vec3 position) {
        super(model, position.convert());
    }

    public MeshRendererComponent(Model model, float x, float y, float z) {
        super(model, x, y, z);
    }

    public MeshRendererComponent(Model model, Mat4 transform) {
        super(model, transform.convert());
    }

    public MeshRendererComponent(ModelInstance copyFrom) {
        super(copyFrom);
    }

    public MeshRendererComponent(ModelInstance copyFrom, Mat4 transform) {
        super(copyFrom, transform.convert());
    }

    public MeshRendererComponent(ModelInstance copyFrom, Mat4 transform, boolean shareKeyframes) {
        super(copyFrom, transform.convert(), shareKeyframes);
    }

    public MeshRendererComponent(MeshRendererComponent copyFrom) {
        super(copyFrom);
    }

    public MeshRendererComponent(MeshRendererComponent copyFrom, Mat4 transform) {
        super(copyFrom, transform.convert());
    }

    public MeshRendererComponent(MeshRendererComponent copyFrom, Mat4 transform, boolean shareKeyframes) {
        super(copyFrom, transform.convert(), shareKeyframes);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float delta) {
        if (this.node == null) return;
        Node.print3d.render(this, this.node.getEnvironment());
    }

    @Override
    public void destroy() {
        this.model.dispose();
    }

    @Override
    public Component setNode(Node node) {
        this.node = node;
        return this;
    }
}
