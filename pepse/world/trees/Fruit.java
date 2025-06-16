package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.JumpObserver;

import java.awt.*;
import java.util.function.Consumer;

public class Fruit extends GameObject implements JumpObserver {
    public static final String FRUIT_TAG = "fruit";
    private static final int FRUIT_ENERGY = 10;

    private final Consumer<Float> addEnergyToAvatar;
    private final OvalRenderable renderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Float> addEnergyToAvatar) {
        super(topLeftCorner, Vector2.ONES.mult(Leaf.SIZE), new OvalRenderable(Color.YELLOW));
        this.addEnergyToAvatar = addEnergyToAvatar;
        this.renderable = new OvalRenderable(Color.YELLOW);
        setTag(FRUIT_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Avatar.AVATAR_TAG) && renderer().getRenderable() != null) {
            this.renderer().setRenderable(null);
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH, false,
                    () -> this.renderer().setRenderable(renderable));
            ((Avatar)other).addEnergy(FRUIT_ENERGY);
            this.addEnergyToAvatar.accept((float) FRUIT_ENERGY);
        }
    }

    @Override
    public void updateForJump() {
        if (renderer().getRenderable() != null) {
            this.renderer().setRenderable(renderable);
        }
    }
}
