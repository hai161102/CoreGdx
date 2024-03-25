package com.engine.base.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.engine.base.core.interfaces.IComponent;
import com.engine.base.core.interfaces.IJson;
import com.engine.base.core.interfaces.INode;
import com.engine.base.core.maths.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Node implements INode, IJson {
    public static final SpriteBatch print2d = new SpriteBatch();
    public static final ModelBatch print3d = new ModelBatch();
    public static final ShapeRenderer shapeRenderer = new ShapeRenderer();
    public String id;
    public boolean inheritTransform = true;
    public boolean isAnimated;
    public final Vec3 position = new Vec3();
    public final Quat rotation = new Quat(0.0F, 0.0F, 0.0F, 1.0F);
    public final Vec3 scale = new Vec3(1.0F, 1.0F, 1.0F);
    public final Vec3 size = new Vec3(1, 1, 1);
    public boolean flipX = false;
    public boolean flipY = false;
    public final Vec3 anchor = new Vec3(0.5f, 0.5f, 0.5f);
    public float opacity = 1;
    public final Mat4 localTransform = new Mat4();
    public final Mat4 globalTransform = new Mat4();
    public boolean enableShape = false;
    protected Node parent;
    private final Array<Node> children = new Array<>();
    private final List<IComponent> components = new ArrayList<>();
    private Environment environment;
    public Mat4 calculateLocalTransform() {
        if (!this.isAnimated) {
            this.localTransform.set(this.position, this.rotation, this.scale);
        }

        return this.localTransform;
    }

    public Mat4 calculateWorldTransform() {
        if (this.inheritTransform && this.parent != null) {
            this.globalTransform.set(this.parent.globalTransform).mul(this.localTransform);
        } else {
            this.globalTransform.set(this.localTransform);
        }

        return this.globalTransform;
    }

    public void calculateTransforms(boolean recursive) {
        this.calculateLocalTransform();
        this.calculateWorldTransform();
        if (recursive) {

            for (Node child : this.children) {
                child.calculateTransforms(true);
            }
        }

    }
    

    public <T extends Node> void attachTo(T parent) {
        parent.addChild(this);
    }

    public IComponent addComponent(IComponent component) {
        this.components.add(component);
        return component.setNode(this);
    }
    public void detach() {
        if (this.parent != null) {
            this.parent.removeChild(this);
            this.parent = null;
        }

    }

    public boolean hasChildren() {
        return this.children.size > 0;
    }

    public int getChildCount() {
        return this.children.size;
    }

    public Node getChild(int index) {
        return this.children.get(index);
    }

    public Node getChild(String id, boolean recursive, boolean ignoreCase) {
        return getNode(this.children, id, recursive, ignoreCase);
    }

    public <T extends Node> int addChild(T child) {
        return this.insertChild(-1, child);
    }

    public <T extends Node> int addChildren(Iterable<T> nodes) {
        return this.insertChildren(-1, nodes);
    }

    public <T extends Node> int insertChild(int index, T child) {
        Node p;
        for(p = this; p != null; p = p.getParent()) {
            if (p == child) {
                throw new GdxRuntimeException("Cannot add a parent as a child");
            }
        }

        p = child.getParent();
        if (p != null && !p.removeChild(child)) {
            throw new GdxRuntimeException("Could not remove child from its current parent");
        } else {
            if (index >= 0 && index < this.children.size) {
                this.children.insert(index, child);
            } else {
                index = this.children.size;
                this.children.add(child);
            }

            child.parent = this;
            child.setEnvironment(this .getEnvironment());
            return index;
        }
    }

    public <T extends Node> int insertChildren(int index, Iterable<T> nodes) {
        if (index < 0 || index > this.children.size) {
            index = this.children.size;
        }

        int i = index;

        for (T child : nodes) {
            this.insertChild(i++, child);
        }

        return index;
    }

    public <T extends Node> boolean removeChild(T child) {
        if (!this.children.removeValue(child, true)) {
            return false;
        } else {
            child.parent = null;
            return true;
        }
    }

    public List<Node> getChildren() {
        return StreamSupport.stream(this.children.spliterator(), false)
                .collect(Collectors.toList());
    }

    public Node getParent() {
        return this.parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public Node copy() {
        return (new Node()).set(this);
    }

    protected Node set(Node other) {
        this.detach();
        this.id = other.id;
        this.isAnimated = other.isAnimated;
        this.inheritTransform = other.inheritTransform;
        this.position.set(other.position);
        this.rotation.set(other.rotation);
        this.scale.set(other.scale);
        this.localTransform.set(other.localTransform);
        this.globalTransform.set(other.globalTransform);

        this.children.clear();

        for (Node child : other.getChildren()) {
            this.addChild(child.copy());
        }

        return this;
    }

    public static Node getNode(Array<Node> nodes, String id, boolean recursive, boolean ignoreCase) {
        int n = nodes.size;
        Node node;
        int i;
        if (ignoreCase) {
            for(i = 0; i < n; ++i) {
                if ((node = nodes.get(i)).id.equalsIgnoreCase(id)) {
                    return node;
                }
            }
        } else {
            for(i = 0; i < n; ++i) {
                if ((node = nodes.get(i)).id.equals(id)) {
                    return node;
                }
            }
        }

        if (recursive) {
            for(i = 0; i < n; ++i) {
                if ((node = getNode(nodes.get(i).children, id, true, ignoreCase)) != null) {
                    return node;
                }
            }
        }

        return null;
    }

    public void traverse(@NotNull List<Node> out) {
        for (Node child : this.children) {
            out.add(child);
            child.traverse(out);
        }
    }

    @Override
    public void readJson(JsonValue value) {
        this.id = value.getString("id");
//        this.setTexturePath(value.getString("texture"));
//        this.setModelPath(value.getString("model"));
        this.position.readJson(value.get(KeyJson.POSITION));
        this.rotation.readJson(value.get(KeyJson.ROTATION));
        this.scale.readJson(value.get(KeyJson.SCALE));
        this.size.readJson(value.get(KeyJson.SIZE));
        this.anchor.readJson(value.get(KeyJson.ANCHOR));
        this.flipX = value.get(KeyJson.FLIP).getBoolean("x", false);
        this.flipY = value.get(KeyJson.FLIP).getBoolean("y", false);
        this.calculateTransforms(true);
        JsonValue childValue = value.get(KeyJson.CHILDREN);
        for (int i = 0; i < childValue.size; i++) {
            Node node = new Node();
            this.addChild(node);
            node.readJson(childValue.get(i));
        }
    }

    @Override
    public INode apply() {
        this.start();
        return this;
    }

    @Override
    public void start() {
        for (IComponent component : this.components) {
            component.start();
        }
        for (Node child : this.children) {
            child.start();
        }
    }

    @Override
    public void update(float delta) {
        this.calculateTransforms(true);
        for (IComponent component : this.components) {
            component.update(delta);
        }
        for (Node child : this.children) {
            child.update(delta);
        }
    }

    @Override
    public void destroy() {
        for (IComponent component : this.components) {
            component.destroy();
        }
        for (Node child : this.children) {
            child.destroy();
        }

    }

    public void removeFromParent() {
        if (this.parent == null) return;
        this.destroy();
        this.parent.removeChild(this);
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent> T getComponent(Class<T> tClass) {
        Optional<IComponent> optional = this.components.stream().filter(c -> c.getClass().equals(tClass)).findFirst();
        return (T) optional.orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent> List<T> getComponentsInChildren(Class<T> tClass) {
        List<T> list = new ArrayList<>();
        List<Node> allChild = new ArrayList<>();
        this.traverse(allChild);
        for (Node node : allChild) {
            list.addAll(
                    (Collection<? extends T>) node.getComponents()
                            .stream()
                            .filter(c -> c.getClass().equals(tClass)).collect(Collectors.toList())
            );
        }
        return list;
    }
    public List<IComponent> getComponents() {
        return this.components;
    }

    private void drawShape(
            float dt,
            Vec3 pos,
            Vec3 size,
            Vec3 scale,
            Quat rotation
    ) {
        if (!this.enableShape) return;
        Node.shapeRenderer.setColor(Color.GREEN);
        Node.shapeRenderer.box(
                pos.x - size.x * 0.5f * anchor.x,
                pos.y - size.y * 0.5f * anchor.y,
                pos.z - size.z * 0.5f * anchor.z,
                size.x * anchor.x,
                size.y * anchor.y,
                size.z * anchor.z
        );
        Node.shapeRenderer.setColor(Color.RED);
        Node.shapeRenderer.line(
                pos.convert(),
                pos.cpy().add(new Vec3(50, 0, 0)).convert()
        );
        Node.shapeRenderer.setColor(Color.GREEN);
        Node.shapeRenderer.line(
                pos.convert(),
                pos.cpy().add(new Vec3(0, 50, 0)).convert()
        );

    }

    @Nullable
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(@Nullable Environment environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"id\":")
                .append("\"").append(this.id).append("\"").append(",");
//        stringBuilder.append("\"texture\":\"").append(this.texturePath).append("\"").append(",");
//        stringBuilder.append("\"model\":\"").append(this.modelPath).append("\"").append(",");
        stringBuilder.append(getKeyJson(KeyJson.POSITION))
                .append(this.position).append(",");
        stringBuilder.append(getKeyJson(KeyJson.ROTATION))
                .append(this.rotation).append(",");
        stringBuilder.append(getKeyJson(KeyJson.SCALE))
                .append(this.scale).append(",");
        stringBuilder.append(getKeyJson(KeyJson.SIZE))
                .append(this.size.toString()).append(",");
        stringBuilder.append(getKeyJson(KeyJson.ANCHOR))
                .append(this.anchor.toString()).append(",");
        stringBuilder.append(getKeyJson(KeyJson.FLIP))
                .append("{\"x\":")
                .append("\"").append(this.flipX)
                .append("\"").append(",\"y\":")
                .append("\"").append(this.flipY)
                .append("\"").append("}")
                .append(",");
        stringBuilder.append(getKeyJson(KeyJson.CHILDREN)).append("[");
        for (Node child : children) {
            stringBuilder.append(child.toString());
            if (this.children.indexOf(child, true) < this.children.size - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]").append("}");
        return stringBuilder.toString();
    }
    private String getKeyJson(String key) {
        return "\"" + key + "\":";
    }

    public void translate(Vec3 vec3) {
        this.position.add(vec3);
    }
    public void setPosition(Vec3 position) {
        this.position.set(position);
        this.calculateTransforms(true);
    }
    public void setWorldPosition(Vec3 worldPosition) {
        this.calculateTransforms(true);
        Vec3 minus = worldPosition.sub(this.globalTransform.getTranslation(new Vec3()));
        Vec3 nextPos = this.position.cpy().add(minus);
        this.setPosition(nextPos);
    }

    public Vec3 getWorldPosition() {
        this.calculateTransforms(true);
        return this.globalTransform.getTranslation(new Vec3());
    }
    public void translate(float x, float y, float z) {
        this.translate(new Vec3(x, y, z));
    }

    public void scale(float x, float y, float z) {
        this.scale.set(x, y, z);
        this.calculateTransforms(true);
    }

    public void scale(float x, float y) {
        this.scale(x, y, this.scale.z);
    }

    public void scale(@NotNull Vec3 scale) {
        this.scale(scale.x, scale.y, scale.z);
    }

    public void rotateTo(Vec3 axis, float rad) {
        this.rotation.set(axis, (float) Math.toDegrees(rad));
        this.calculateTransforms(true);
    }

    public void rotate(Vec3 axis, float rad) {
        this.rotation.setFromAxisRad(axis, this.rotation.getAxisAngleRad(axis) + rad);
        this.calculateTransforms(true);
    }

    public Mat4 getLocalTransform() {
        return this.calculateLocalTransform();
    }

    public Mat4 getGlobalTransform() {
        return this.calculateWorldTransform();
    }

    public Rect getRect2D(boolean local) {
        this.calculateTransforms(true);
        Vec3 pos = new Vec3();
        if (local) pos.set(this.localTransform.getTranslation(new Vec3()));
        else pos.set(this.globalTransform.getTranslation(new Vec3()));
        Vec2 realSize = new Vec2(
                this.size.x * this.scale.x,
                this.size.y * this.scale.y
        );
        return new Rect(
                pos.x - realSize.x * this.anchor.x,
                pos.y - realSize.y * this.anchor.y,
                realSize.x,
                realSize.y
        ).update();
    }
    public Rect getRect2D() {
        return getRect2D(false);
    }
    public boolean isFlipX() {
        return flipX;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }
    public boolean isIntersect2D(Vec2 point) {
        return getRect2D().contains(point.x, point.y);
    }
    private static class KeyJson {
        public static final String POSITION = "position";
        public static final String ROTATION = "rotation";
        public static final String SCALE = "scale";
        public static final String SIZE = "size";
        public static final String ANCHOR = "anchor";
        public static final String FLIP = "flip";
        public static final String CHILDREN = "children";
    }
}
