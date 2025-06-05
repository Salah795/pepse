package pepse.world;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.PepseGameManager;

public class EnergyCounter {
    private static final int ENERGY_COUNTER_SIZE = 50;
    private static final int DISTANCE_FROM_TOP = 10;

    public static GameObject create(Vector2 windowDimensions) {
        GameObject energyCounter = new GameObject(
                new Vector2(windowDimensions.x() * PepseGameManager.DIMENSION_MIDDLE, DISTANCE_FROM_TOP),
                Vector2.ONES.mult(ENERGY_COUNTER_SIZE),
    }
}
