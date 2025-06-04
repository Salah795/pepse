package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final int SUN_HALO_RADIUS = 150;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(sun.getCenter(), new Vector2(SUN_HALO_RADIUS, SUN_HALO_RADIUS),
                new OvalRenderable(HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sun halo");
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
