package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The {@code Sky} class provides a simple background for the game world,
 * rendered as a solid-colored rectangle that covers the entire screen.
 * <p>
 * It is placed in camera coordinates, meaning it stays fixed relative to the screen
 * and does not scroll with the game world.
 * @author Salah Mahmied, Kais Sora.
 */
public class Sky {

    /** The default sky color (light blue). */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80c6E5");

    /** The tag used to identify the sky GameObject. */
    private static final String SKY_TAG = "Sky";

    /**
     * Creates a sky background GameObject that fills the screen.
     *
     * @param windowDimensions The full dimensions of the game window.
     * @return A {@link GameObject} representing the sky background.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // Fixed to camera
        sky.setTag(SKY_TAG);
        return sky;
    }
}
