package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The {@code Block} class represents a basic terrain unit (e.g., ground or platform)
 * in the game world. It is a static, immovable object that other objects can collide with.
 * <p>
 * Blocks are square and share a uniform size defined by {@code SIZE}.
 * @author Salah Mahmied, Kais Sora.
 */
public class Block extends GameObject {
    /** The side length of the block in pixels. */
    public static final int SIZE = 30;

    /** The tag used to identify block GameObjects. */
    public static final String BLOCK_TAG = "Block";

    /**
     * Constructs a new {@code Block} GameObject with a specified top-left corner and appearance.
     * The block is static (non-movable) and cannot be intersected by other objects.
     *
     * @param topLeftCorner The top-left position of the block in window coordinates.
     * @param renderable The visual appearance of the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO); // Prevent penetration from any direction
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);       // Make the block immovable
        this.setTag(BLOCK_TAG);
    }
}

