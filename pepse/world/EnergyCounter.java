package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

/**
 * The {@code EnergyCounter} class displays the current energy level of the avatar
 * as a percentage on the game screen. It updates in real-time using a provided
 * {@link Supplier} that returns the avatar’s energy level.
 *
 * The text is rendered in screen space (camera coordinates), making it behave like a UI element.
 */
public class EnergyCounter extends GameObject {
    /** Symbol appended to the energy value (e.g., "85%"). */
    public static String PERCENT_SIGN = "%";

    private final Supplier<Integer> avatarEnergySupplier;
    private final TextRenderable textRenderable;

    /**
     * Constructs a new {@code EnergyCounter} object.
     *
     * @param topLeftCorner      The top-left position of the counter in window (camera) coordinates.
     * @param dimensions         The width and height of the counter display.
     * @param textRenderable     The {@link TextRenderable} used to render the energy text.
     * @param avatarEnergySupplier A supplier function that provides the current energy value of the avatar.
     */
    public EnergyCounter(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable textRenderable,
                         Supplier<Integer> avatarEnergySupplier) {
        super(topLeftCorner, dimensions, textRenderable);
        this.avatarEnergySupplier = avatarEnergySupplier;
        this.textRenderable = textRenderable;
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // Keep counter fixed on screen
    }

    /**
     * Updates the displayed energy value each frame.
     *
     * @param deltaTime Time elapsed since the last update (in seconds).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.textRenderable.setString(this.avatarEnergySupplier.get().toString() + PERCENT_SIGN);
    }
}

