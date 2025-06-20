package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.JumpObserver;

import java.awt.*;
import java.util.function.Consumer;

/**
 * The {@code Fruit} class represents a collectible fruit object in the game.
 * Fruits can be picked up by the avatar to restore energy. They disappear
 * upon collection and reappear after a fixed time interval.
 * <p>
 * Fruits also implement {@link JumpObserver}, allowing them to reappear
 * after the player jumps.
 * @author Salah Mahmied, Kais Sora.
 */
public class Fruit extends GameObject implements JumpObserver {
    /** Tag used to identify the fruit GameObject. */
    public static final String FRUIT_TAG = "fruit";

    /** Amount of energy granted to the avatar when collecting the fruit. */
    private static final int FRUIT_ENERGY = 10;

    private final Consumer<Float> addEnergyToAvatar;
    private final OvalRenderable renderable;

    /**
     * Constructs a new {@code Fruit} GameObject at the specified location.
     *
     * @param topLeftCorner The top-left corner of the fruit's bounding box.
     * @param addEnergyToAvatar A consumer to handle energy restoration logic externally.
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Float> addEnergyToAvatar) {
        super(topLeftCorner, Vector2.ONES.mult(Leaf.SIZE), new OvalRenderable(Color.YELLOW));
        this.addEnergyToAvatar = addEnergyToAvatar;
        this.renderable = new OvalRenderable(Color.YELLOW);
        setTag(FRUIT_TAG);
    }

    /**
     * Called when this fruit collides with another GameObject. If the other object is
     * the avatar, the fruit disappears, grants energy, and is scheduled to reappear
     * after one full game cycle.
     *
     * @param other The other GameObject involved in the collision.
     * @param collision The details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Avatar.AVATAR_TAG) && renderer().getRenderable() != null) {
            this.renderer().setRenderable(null); // Hide the fruit
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH, false,
                    () -> this.renderer().setRenderable(renderable)); // Reappear after delay
            ((Avatar)other).addEnergy(FRUIT_ENERGY); // Update avatar energy
            this.addEnergyToAvatar.accept((float) FRUIT_ENERGY); // Notify energy gain externally
        }
    }

    /**
     * Re-renders the fruit (if not visible) in response to a jump event.
     * This ensures the fruit can reappear when appropriate.
     */
    @Override
    public void updateForJump() {
        if (renderer().getRenderable() != null) {
            this.renderer().setRenderable(renderable);
        }
    }
}
