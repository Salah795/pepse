package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.JumpObserver;

import java.awt.*;

/**
 * The {@code Trunk} class represents the trunk of a tree in the game world.
 * It is a static, immovable GameObject with a fixed width and height.
 * <p>
 * Implements {@link JumpObserver}, allowing it to respond to jump events
 * (currently with no effect, but can be extended if needed).
 * @author Salah Mahmied, Kais Sora.
 */
public class Trunk extends GameObject implements JumpObserver {

    /** The color of the tree trunk. */
    public static final Color COLOR = new Color(100, 50, 20);

    /** The width of the trunk in pixels. */
    public static final int WIDTH = 20;

    /** The height of the trunk in pixels. */
    public static final int HEIGHT = 100;

    /** The tag used to identify trunk GameObjects. */
    private static final String TRUNK_TAG = "trunk";

    /**
     * Constructs a new {@code Trunk} GameObject at the specified position.
     * The trunk is rendered as a brown rectangle and is physically immovable.
     *
     * @param topLeftCorner The top-left position of the trunk in window coordinates.
     */
    public Trunk(Vector2 topLeftCorner) {
        super(topLeftCorner, new Vector2(WIDTH, HEIGHT), new RectangleRenderable(COLOR));
        physics().preventIntersectionsFromDirection(Vector2.ZERO); // No objects should penetrate the trunk
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);       // Make the trunk immovable
        setTag(TRUNK_TAG);
    }

    /**
     * Responds to a jump event. Currently, this method does nothing,
     * but the interface requires its implementation.
     */
    @Override
    public void updateForJump() { }
}
