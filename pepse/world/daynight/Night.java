package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The {@code Night} class represents a semi-transparent overlay that simulates nighttime
 * by gradually changing its opacity over time to create a day-night cycle effect.
 * @author Salah Mahmied, Kais Sora.
 */
public class Night {
    /** The maximum opacity at midnight (darker). */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /** The minimum opacity at noon (fully transparent). */
    private static final float NOON_OPACITY = 0;

    /** Tag used to identify the night GameObject. */
    private static final String NIGHT_TAG = "Night";

    /** Factor used to split the full day-night cycle into day and night durations. */
    private static final int TRANSITION_FACTOR = 2;

    /**
     * Creates a GameObject representing the night overlay which transitions
     * between transparent (day) and semi-transparent (night) states to simulate
     * a day-night cycle.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The total length of a full day-night cycle (in seconds).
     * @return A {@code GameObject} that overlays the screen and simulates night.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO, new Vector2(windowDimensions),
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<>(night, night.renderer()::setOpaqueness, NOON_OPACITY, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / TRANSITION_FACTOR,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}

