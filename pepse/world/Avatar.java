package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class Avatar extends GameObject {
    public static final int AVATAR_SIZE = 50;
    public static final float AVATAR_INITIAL_ENERGY = 100;
    private static final float IDLE_STATE_MODIFIER = 1;
    private static final float RUN_STATE_MODIFIER = 0.5f;
    private static final float JUMP_STATE_MODIFIER = 10;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final String AVATAR_IMAGE_PATH = "assets/idle_0.png";

    private final Consumer<Integer> energyConsumer;
    private final UserInputListener inputListener;
    private float avatarEnergy;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader,
                  Consumer<Integer> energyUpdater) {
        //TODO check if you could change the signature of this method.
        super(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE), imageReader.readImage(
                AVATAR_IMAGE_PATH, true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.avatarEnergy = AVATAR_INITIAL_ENERGY;
        this.energyConsumer = energyUpdater;
    }

    public void addEnergy(float energy) {
        this.avatarEnergy += energy;
        this.energyConsumer.accept((int) this.avatarEnergy);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.avatarEnergy >= RUN_STATE_MODIFIER) {
            xVel -= VELOCITY_X;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && this.avatarEnergy >= RUN_STATE_MODIFIER) {
            xVel += VELOCITY_X;
        }
        if(xVel != 0) {
            updateEnergy(-RUN_STATE_MODIFIER);
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 &&
                this.avatarEnergy >= JUMP_STATE_MODIFIER) {
            transform().setVelocityY(VELOCITY_Y);
            updateEnergy(-JUMP_STATE_MODIFIER);
        }
        if(getVelocity().equals(Vector2.ZERO) && this.avatarEnergy <= AVATAR_INITIAL_ENERGY - 1) {
            updateEnergy(IDLE_STATE_MODIFIER);
        }
    }

    private void updateEnergy(float value) {
        this.avatarEnergy += value;
        this.energyConsumer.accept((int) this.avatarEnergy);
    }
}
