package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

public class EnergyCounter {
    private static final int ENERGY_COUNTER_SIZE = 50;
    private static final int DISTANCE_FROM_TOP = 10;
    private static final String PERCENT_SIGN = "%";
    private static final String ENERGY_COUNTER_TAG = "EnergyCounter";
    private static final TextRenderable energyCounterRender = new TextRenderable(
            (int) Avatar.AVATAR_INITIAL_ENERGY + PERCENT_SIGN);

    public static GameObject create(Vector2 windowDimensions) {
        Vector2 energyCounterLeftCorner = new Vector2(
                windowDimensions.x() * PepseGameManager.DIMENSION_MIDDLE -
                        ENERGY_COUNTER_SIZE, DISTANCE_FROM_TOP);
        GameObject energyCounter = new GameObject(energyCounterLeftCorner,
                Vector2.ONES.mult(ENERGY_COUNTER_SIZE), energyCounterRender);
        energyCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        energyCounter.setTag(ENERGY_COUNTER_TAG);
        return energyCounter;
    }

    public static void update(int newEnergy) {
        energyCounterRender.setString(newEnergy + PERCENT_SIGN);
    }
}
