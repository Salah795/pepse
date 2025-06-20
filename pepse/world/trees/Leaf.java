package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.JumpObserver;

import java.awt.*;
import java.util.Random;

/**
 * The {@code Leaf} class represents a single leaf object on a tree.
 * Leaves gently oscillate in size and rotation to simulate natural motion,
 * and respond to avatar jumps with a falling animation.
 * <p>
 * Implements {@link JumpObserver} to animate the leaf in response to jump events.
 * @author Salah Mahmied, Kais Sora.
 */
public class Leaf extends GameObject implements JumpObserver {
    /** The size (width and height) of a leaf block in pixels. */
    public static final int SIZE = 20;

    /** The starting angle for the leaf falling animation triggered by a jump. */
    public static final float TRANSITION_START = 90;

    /** The ending angle for the falling animation (flat). */
    public static final float TRANSITION_END = 0;

    /** The default color of a leaf. */
    public static final Color COLOR = new Color(50, 200, 30);

    /** The tag used to identify leaf GameObjects. */
    public static final String LEAF_TAG = "leaf";

    /** Max rotation angle for idle oscillation. */
    private static final float VELOCITY_ANGLE = 12;

    /** Duration (in seconds) of each transition animation. */
    private static final float TRANSITION_TIME = 2;

    /** Minimum scale multiplier for the leaf pulsing animation. */
    private static final float MIN_PORTION = 0.5f;

    /** Maximum scale multiplier for the leaf pulsing animation. */
    private static final float MAX_PORTION = 1.5f;

    /** Maximum random delay before starting the leaf's animation, in seconds. */
    private static final int DELAY_TIME = 30;

    /**
     * Constructs a new {@code Leaf} GameObject at the specified position.
     * The leaf begins an idle animation after a random delay.
     *
     * @param topLeftCorner The top-left position of the leaf in window coordinates.
     */
    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), new RectangleRenderable(COLOR));
        Random random = new Random();
        new ScheduledTask(this, random.nextInt(DELAY_TIME),
                false, this::createTransition);
        setTag(LEAF_TAG);
    }

    /**
     * Initializes the idle animations for the leaf:
     * - A swinging rotation back and forth.
     * - A pulsing size transition to simulate a fluttering effect.
     */
    private void createTransition() {
        new Transition<>(
                this, this.renderer()::setRenderableAngle, VELOCITY_ANGLE,
                -VELOCITY_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null
        );

        new Transition<>(
                this, this::setDimensions, getDimensions().mult(MIN_PORTION),
                getDimensions().mult(MAX_PORTION), Transition.LINEAR_INTERPOLATOR_VECTOR, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null
        );
    }

    /**
     * Triggered when a jump occurs. The leaf performs a falling animation
     * rotating from a vertical to flat angle.
     */
    @Override
    public void updateForJump() {
        new Transition<>(this,
                this.renderer()::setRenderableAngle,
                TRANSITION_START,
                TRANSITION_END,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }
}

