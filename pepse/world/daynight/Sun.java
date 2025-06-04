package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Terrain;

import java.awt.*;

public class Sun {
    private static final int SUN_RADIUS = 80;
    private static final float INITIAL_ANGLE = 0;
    private static final float FINAL_ANGLE = 360;
    private static final String SUN_TAG = "Sun";

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
