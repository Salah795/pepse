package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The {@code SunHalo} class creates a glowing halo effect that follows the sun GameObject.
 * This enhances the visual representation of the sun by surrounding it with a faint yellow glow.
 * @author Salah Mahmied, Kais Sora.
 */
public class SunHalo {
    /** The radius (size) of the sun halo. */
    private static final int SUN_HALO_RADIUS = 150;

    /** The color of the halo: semi-transparent yellow. */
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    /** Tag used to identify the sun halo GameObject. */
    private static final String SUN_HALO_TAG = "Sun Halo";

    /**
     * Creates a sun halo GameObject that follows the center of the provided sun GameObject.
     * The halo appears as a large, transparent yellow circle around the sun, giving it a glowing effect.
     *
     * @param sun The sun GameObject around which the halo should appear and follow.
     * @return A {@code GameObject} representing the sun halo.
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(sun.getCenter(), Vector2.ONES.mult(SUN_HALO_RADIUS),
                new OvalRenderable(HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);

        // Ensure the halo continuously follows the sun's position
        sunHalo.addComponent(_ -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;
    }
}

