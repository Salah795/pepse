package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

public class EnergyCounter extends GameObject {
    private final Supplier<Float> avatarEnergySupplier;
    private final TextRenderable textRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param textRenderable    The textRenderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public EnergyCounter(Vector2 topLeftCorner, Vector2 dimensions, TextRenderable textRenderable,
                         Supplier<Float> avatarEnergySupplier) {
        super(topLeftCorner, dimensions, textRenderable);
        this.avatarEnergySupplier = avatarEnergySupplier;
        this.textRenderable = textRenderable;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.textRenderable.setString(this.avatarEnergySupplier.get().toString());
    }
}
