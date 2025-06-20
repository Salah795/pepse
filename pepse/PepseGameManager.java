package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.*;
import danogl.gui.rendering.*;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.*;
import pepse.world.daynight.*;
import pepse.world.trees.Flora;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

/**
 * The {@code PepseGameManager} class sets up and runs the main game loop and environment
 * for the PEaPSE (Platformer Energy-aware Physics Simulation Environment) game.
 * <p>
 * It initializes all core components, including terrain, avatar, energy system,
 * sky, day/night cycle, sun, sun halo, clouds, and flora (trees, fruits, leaves).
 * @author Salah Mahmied, Kais Sora.
 */
public class PepseGameManager extends GameManager {
    /** Base color of clouds. */
    public static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

    /** Initial position of the cloud structure. */
    public static final Vector2 CLOUD_INITIAL_POSITION = new Vector2(100, 50);

    /** Middle of window (used for positioning). */
    public static final float DIMENSION_MIDDLE = 0.5f;

    /** Duration of day/night cycle in seconds. */
    public static final int CYCLE_LENGTH = 30;

    /** Starting position of avatar along X-axis. */
    public static float avatarInitialX;

    /** Distance from top of screen for energy counter UI. */
    private static final int DISTANCE_FROM_TOP = 10;

    /** Width/height of energy counter UI in pixels. */
    private static final int ENERGY_COUNTER_SIZE = 50;

    /** Minimum X value for terrain generation. */
    private static final int TERRAIN_MIN_RANGE = 0;

    /** Cloud movement speed in pixels/second. */
    private static final float CLOUD_VELOCITY = 50;

    /** Tag used to identify cloud blocks. */
    private static final String CLOUD_TAG = "Cloud";

    /** Shape definition for block-based cloud. */
    private static final List<List<Integer>> CLOUD_STRUCTURE = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );

    private WindowController windowController;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private Terrain terrain;
    private GameObject sun;
    private Avatar avatar;
    private Flora flora;
    private final Random random = new Random();

    /**
     * Launches the game.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes all game components and adds them to the game world.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.imageReader = imageReader;

        avatarInitialX = windowController.getWindowDimensions().x() * DIMENSION_MIDDLE;

        createSky();
        createTerrain();
        createNight();
        createSun();
        createSunHalo();
        createAvatar();
        createEnergyCounter();
        createTrees();
        createClouds();
    }

    /** Adds the sky background. */
    private void createSky() {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    /** Generates terrain and adds static ground blocks. */
    private void createTerrain() {
        this.terrain = new Terrain(windowController.getWindowDimensions(), 0);
        List<Block> blocks = terrain.createInRange(TERRAIN_MIN_RANGE,
                (int) windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    /** Adds a transparent overlay that simulates the day-night cycle. */
    private void createNight() {
        GameObject night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.UI);
    }

    /** Initializes and adds the avatar (player-controlled character). */
    private void createAvatar() {
        Vector2 avatarInitialPosition = new Vector2(avatarInitialX,
                terrain.groundHeightAt(avatarInitialX) - Avatar.AVATAR_SIZE);
        this.avatar = new Avatar(avatarInitialPosition, inputListener, imageReader);
        gameObjects().addGameObject(avatar);
        Vector2 vector = this.windowController.getWindowDimensions().mult(0.5f)
                .add(avatarInitialPosition.mult(-1));
        this.setCamera(new Camera(this.avatar, vector,
                this.windowController.getWindowDimensions(),
                this.windowController.getWindowDimensions()));
    }

    /** Adds a UI element to display the avatar’s energy percentage. */
    private void createEnergyCounter() {
        TextRenderable energyCounterRender = new TextRenderable(
                Avatar.AVATAR_INITIAL_ENERGY + EnergyCounter.PERCENT_SIGN);
        Vector2 energyCounterLeftCorner = new Vector2(
                windowController.getWindowDimensions().x() * DIMENSION_MIDDLE - ENERGY_COUNTER_SIZE,
                DISTANCE_FROM_TOP);
        EnergyCounter energyCounter = new EnergyCounter(energyCounterLeftCorner,
                Vector2.ONES.mult(ENERGY_COUNTER_SIZE), energyCounterRender,
                this.avatar::getEnergy);
        gameObjects().addGameObject(energyCounter, Layer.UI);
    }

    /** Adds the sun object and sets it in motion. */
    private void createSun() {
        this.sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    /** Creates a glowing halo around the sun. */
    private void createSunHalo() {
        GameObject sunHalo = SunHalo.create(this.sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    /** Adds trees, leaves, and fruits using the {@link Flora} class. */
    private void createTrees() {
        Function<Float, Float> groundHeightFunction = x -> (float) Math.floor(
                terrain.groundHeightAt(x) / Block.SIZE) * Block.SIZE;
        this.flora = new Flora(groundHeightFunction, this.avatar::registerObserverToJump,
                this.avatar::addEnergy, this.gameObjects(), random);
        flora.createInRange(0, (int) windowController.getWindowDimensions().x());
    }

    /** Creates clouds using a block matrix and applies continuous motion animation. */
    private void createClouds() {
        Renderable cloudBlockRenderable = new RectangleRenderable(
                ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
        List<Block> cloudBlocksArray = new ArrayList<>();
        for (int row = 0; row < CLOUD_STRUCTURE.size(); row++) {
            for (int column = 0; column < CLOUD_STRUCTURE.get(row).size(); column++) {
                if (CLOUD_STRUCTURE.get(row).get(column) == 1) {
                    Vector2 blockPosition = new Vector2(
                            CLOUD_INITIAL_POSITION.x() + column * Block.SIZE,
                            CLOUD_INITIAL_POSITION.y() + row * Block.SIZE);
                    Block cloudBlock = new Block(blockPosition, cloudBlockRenderable);
                    gameObjects().addGameObject(cloudBlock, Layer.BACKGROUND);
                    cloudBlocksArray.add(cloudBlock);
                    cloudBlock.setTag(CLOUD_TAG);
                    cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                }
            }
        }

        moveCloud(cloudBlocksArray);
    }

    /**
     * Animates the movement of a cloud block array across the screen.
     *
     * @param cloudBlocksArray List of blocks that make up the cloud.
     */
    private void moveCloud(List<Block> cloudBlocksArray) {
        for (Block block : cloudBlocksArray) {
            Vector2 topLeftCorner = block.getTopLeftCorner();
            float startX = topLeftCorner.x();
            float distance = windowController.getWindowDimensions().x() +
                    (CLOUD_STRUCTURE.getFirst().size() * Block.SIZE);
            float endX = topLeftCorner.x() + distance;
            new Transition<>(block,
                    x -> block.setTopLeftCorner(new Vector2(x, topLeftCorner.y())),
                    startX, endX,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    windowController.getWindowDimensions().x() / CLOUD_VELOCITY,
                    Transition.TransitionType.TRANSITION_LOOP, null);
        }
    }
}

