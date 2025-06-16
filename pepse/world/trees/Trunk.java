package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.JumpObserver;

import java.awt.*;

public class Trunk extends GameObject implements JumpObserver {

    public static final Color COLOR = new Color(100, 50, 20);
    public static final int WIDTH = 20;
    public static final int HEIGHT = 100;
    private static final String TRUNK_TAG = "trunk";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     */
    public Trunk(Vector2 topLeftCorner) {
        super(topLeftCorner, new Vector2(WIDTH, HEIGHT), new RectangleRenderable(COLOR));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TRUNK_TAG);
    }

    @Override
    public void updateForJump() { }
}
