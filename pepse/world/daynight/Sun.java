package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

public class Sun {
    private static final int SUN_RADIUS = 80;

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        //TODO fix the sun height problem.
        float sunHeight = windowDimensions.y() * Terrain.TERRAIN_FACTOR * 0.5f;
        Vector2 sunLocation = new Vector2((float) (windowDimensions.x() * 0.5), sunHeight);
        GameObject sun = new GameObject(sunLocation, new Vector2(SUN_RADIUS, SUN_RADIUS),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        return sun;
    }
}
