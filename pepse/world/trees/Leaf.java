package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.JumpObserver;

import java.awt.*;
import java.util.Random;

public class Leaf extends GameObject implements JumpObserver {
    //TODO return here again and try to change the implementation here because you have been cheated this.
    public static final int SIZE = 20;
    private static final float VELOCITY_ANGLE = 12;
    private static final float TRANSITION_TIME = 2;
    private static final float MIN_PORTION = 0.5f;
    private static final float MAX_PORTION = 1.5f;
    private static final float DELAY_FACTOR = 0.1f;
    private static final int DELAY_TIME = 30;
    private static final String LEAF_TAG = "leaf";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     */
    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), new RectangleRenderable(Color.GREEN));
        Random random = new Random();
        float delayTime = random.nextInt(DELAY_TIME) * DELAY_FACTOR;
        new ScheduledTask(this, delayTime, false, this::createTransition);
        setTag(LEAF_TAG);
    }

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

    @Override
    public void updateForJump() {
        new Transition<>(this,
                );
    }
}
