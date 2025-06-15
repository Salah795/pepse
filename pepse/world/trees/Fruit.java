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
    private static final String FRUIT_TAG = "fruit";
    private static final int FRUIT_ENERGY = 10;

    private final Consumer<Integer> addEnergyToAvatar;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Integer> addEnergyToAvatar) {
        super(topLeftCorner, Vector2.ONES.mult(Leaf.SIZE), new OvalRenderable(Color.YELLOW));
        this.addEnergyToAvatar = addEnergyToAvatar;
        setTag(FRUIT_TAG);
    }

    /**
     * Handles collision logic when the fruit interacts with another object.
     * If the object is the avatar, the fruit disappears temporarily and adds energy to the avatar.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        //TODO fix this method because you have been copied it.
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Avatar.AVATAR_TAG) && renderer().getRenderable() != null) {
            this.renderer().setRenderable(null);
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH, false,
                    () -> this.renderer().setRenderable(new OvalRenderable(Color.YELLOW)));
            ((Avatar)other).addEnergy(FRUIT_ENERGY);
            this.addEnergyToAvatar.accept(FRUIT_ENERGY);
        }
    }

    @Override
    public void updateForJump() {
        //TODO check this method.
        if (renderer().getRenderable() != null) {
            this.renderer().setRenderable(new OvalRenderable(Color.YELLOW));
        }
    }
}
