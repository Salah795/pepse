package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Terrain;

import java.awt.*;

/**
 * The {@code Sun} class creates a sun GameObject that moves in a circular path
 * across the sky to simulate the sun's movement during a day-night cycle.
 * @author Salah Mahmied, Kais Sora.
 */
public class Sun {
    /** The radius (size) of the sun. */
    private static final int SUN_RADIUS = 80;

    /** The initial angle (in degrees) for the sun's circular motion. */
    private static final float INITIAL_ANGLE = 0;

    /** The final angle (in degrees) for a full rotation of the sun. */
    private static final float FINAL_ANGLE = 360;

    /** Tag used to identify the sun GameObject. */
    private static final String SUN_TAG = "Sun";

    /**
     * Creates a sun GameObject that moves in a circular path across the screen to simulate
     * the passage of time during a day.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The duration (in seconds) of a full day-night cycle.
     * @return A {@code GameObject} representing the sun, with animated circular movement.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        float sunHeight = windowDimensions.y() * Terrain.TERRAIN_FACTOR * PepseGameManager.DIMENSION_MIDDLE;
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() * PepseGameManager.DIMENSION_MIDDLE,
                sunHeight);
        GameObject sun = new GameObject(initialSunCenter, Vector2.ONES.mult(SUN_RADIUS),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        Vector2 cycleCenter = new Vector2(windowDimensions.x() * PepseGameManager.DIMENSION_MIDDLE,
                windowDimensions.y() * Terrain.TERRAIN_FACTOR);

        new Transition<>(sun, (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter).
                rotated(angle).add(cycleCenter)), INITIAL_ANGLE, FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);

        return sun;
    }
}

