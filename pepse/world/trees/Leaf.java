package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Leaf extends GameObject {
    //TODO return here again and try to change the implementation here because you have been cheated this.
    private static final int SIZE = 20;
    private static final float VELOCITY_ANGLE = 12;
    private static final float TRANSITION_TIME = 2;
    private static final float MIN_PORTION = 0.5f;
    private static final float MAX_PORTION = 1.5f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        createAngleTransition();
        createWidthTransition();
    }

    private void createAngleTransition() {
        new Transition<>(
                this, this.renderer()::setRenderableAngle, VELOCITY_ANGLE,
                -VELOCITY_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null
        );
    }

    private void createWidthTransition() {
        new Transition<>(
                this, this::setDimensions, getDimensions().mult(MIN_PORTION),
                getDimensions().mult(MAX_PORTION), Transition.LINEAR_INTERPOLATOR_VECTOR, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null
        );
    }
}
