package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Avatar} class represents the player's controllable character in the game.
 * It supports walking, jumping, and idle animations, with an energy system limiting its actions.
 * The avatar uses input from the user to update its behavior and animations accordingly.
 * <p>
 * The avatar notifies registered {@link JumpObserver}s when it jumps, enabling interaction
 * with environment elements like leaves or fruits.
 * @author Salah Mahmied, Kais Sora.
 */
public class Avatar extends GameObject {
    /** Tag used to identify the avatar GameObject. */
    public static final String AVATAR_TAG = "avatar";

    /** Avatar size (width and height in pixels). */
    public static final int AVATAR_SIZE = 50;

    /** Initial energy value of the avatar. */
    public static final float AVATAR_INITIAL_ENERGY = 100;

    private static final int IDLE_IMAGES_NUMBER = 4;
    private static final int JUMP_IMAGES_NUMBER = 4;
    private static final int RUN_IMAGES_NUMBER = 6;
    private static final float IDLE_STATE_MODIFIER = 1;
    private static final float RUN_STATE_MODIFIER = 0.5f;
    private static final float JUMP_STATE_MODIFIER = 10;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final double TIME_BETWEEN_CLIPS = 0.3;
    private static final String AVATAR_IMAGE_PATH = "assets/idle_0.png";
    private static final String IDLE_IMAGE_PATH_FORMAT = "assets/idle_%d.png";
    private static final String JUMP_IMAGE_PATH_FORMAT = "assets/jump_%d.png";
    private static final String RUN_IMAGE_PATH_FORMAT = "assets/run_%d.png";

    private float avatarEnergy;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private AnimationRenderable idleRenderables;
    private AnimationRenderable jumpRenderables;
    private AnimationRenderable runRenderables;
    private final List<JumpObserver> jumpObservers;

    /**
     * Constructs a new {@code Avatar} GameObject at the given position.
     *
     * @param topLeftCorner The top-left corner of the avatar's position.
     * @param inputListener Object for listening to user key inputs.
     * @param imageReader Utility to load images for animations.
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE),
                imageReader.readImage(AVATAR_IMAGE_PATH, true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.avatarEnergy = AVATAR_INITIAL_ENERGY;
        jumpObservers = new ArrayList<>();
        createIdleRenderable();
        createJumpRenderable();
        createRunRenderable();
        renderer().setRenderable(idleRenderables);
        setTag(AVATAR_TAG);
    }

    /**
     * Increases avatar's energy by a specified amount, without exceeding the maximum.
     *
     * @param energy The amount of energy to add.
     */
    public void addEnergy(float energy) {
        if (this.avatarEnergy + energy <= AVATAR_INITIAL_ENERGY)
            this.avatarEnergy += energy;
    }

    /**
     * Returns the avatar's current energy level as an integer.
     *
     * @return Energy level.
     */
    public int getEnergy() {
        return (int) this.avatarEnergy;
    }

    /**
     * Updates the avatar each frame: handles movement, jumping, and energy logic.
     *
     * @param deltaTime Time elapsed since last update (in seconds).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.avatarEnergy >= RUN_STATE_MODIFIER)
            xVel -= VELOCITY_X;

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && this.avatarEnergy >= RUN_STATE_MODIFIER)
            xVel += VELOCITY_X;

        if (xVel != 0) {
            this.avatarEnergy -= RUN_STATE_MODIFIER;
            renderer().setRenderable(runRenderables);
            renderer().setIsFlippedHorizontally(xVel < 0);
        }

        transform().setVelocityX(xVel);

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 &&
                this.avatarEnergy >= JUMP_STATE_MODIFIER) {
            jumpAvatar();
        }

        if (getVelocity().equals(Vector2.ZERO) && this.avatarEnergy < AVATAR_INITIAL_ENERGY) {
            this.avatarEnergy += IDLE_STATE_MODIFIER;
            renderer().setRenderable(idleRenderables);
        }
    }

    /**
     * Registers a new {@link JumpObserver} to be notified on jump events.
     *
     * @param jumpObserver Observer to register.
     */
    public void registerObserverToJump(JumpObserver jumpObserver) {
        this.jumpObservers.add(jumpObserver);
    }

    /** Triggers the avatar's jump: sets vertical velocity, changes animation,
     *  notifies jump observers, and reduces energy. */
    private void jumpAvatar() {
        transform().setVelocityY(VELOCITY_Y);
        this.avatarEnergy -= JUMP_STATE_MODIFIER;
        renderer().setRenderable(jumpRenderables);
        for (JumpObserver jumpObserver : jumpObservers) {
            jumpObserver.updateForJump();
        }
    }

    /** Loads and creates the avatar's idle animation from idle images. */
    private void createIdleRenderable() {
        Renderable[] renderables = new Renderable[IDLE_IMAGES_NUMBER];
        for (int index = 0; index < IDLE_IMAGES_NUMBER; index++) {
            renderables[index] = imageReader.readImage(
                    String.format(IDLE_IMAGE_PATH_FORMAT, index), true);
        }
        this.idleRenderables = new AnimationRenderable(renderables, TIME_BETWEEN_CLIPS);
    }

    /** Loads and creates the avatar's jump animation from jump images. */
    private void createJumpRenderable() {
        Renderable[] renderables = new Renderable[JUMP_IMAGES_NUMBER];
        for (int index = 0; index < JUMP_IMAGES_NUMBER; index++) {
            renderables[index] = imageReader.readImage(
                    String.format(JUMP_IMAGE_PATH_FORMAT, index), true);
        }
        this.jumpRenderables = new AnimationRenderable(renderables, TIME_BETWEEN_CLIPS);
    }

    /** Loads and creates the avatar's run animation from run images. */
    private void createRunRenderable() {
        Renderable[] renderables = new Renderable[RUN_IMAGES_NUMBER];
        for (int index = 0; index < RUN_IMAGES_NUMBER; index++) {
            renderables[index] = imageReader.readImage(
                    String.format(RUN_IMAGE_PATH_FORMAT, index), true);
        }
        this.runRenderables = new AnimationRenderable(renderables, TIME_BETWEEN_CLIPS);
    }
}
