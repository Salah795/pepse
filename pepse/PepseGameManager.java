package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class PepseGameManager extends GameManager {
    public static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    public static final Vector2 CLOUD_INITIAL_POSITION = new Vector2(100, 50);
    public static final float DIMENSION_MIDDLE = 0.5f;
    public static final int CYCLE_LENGTH = 30;
    private static final int TERRAIN_MIN_RANGE = 0;
    private static final int ENERGY_COUNTER_SIZE = 50;
    private static final int DISTANCE_FROM_TOP = 10;

    public static float avatarInitialX;
    private WindowController windowController;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private Terrain terrain;
    private GameObject sun;
    private Avatar avatar;
    private Flora flora;
    private final Random random = new Random();
    private static final float CLOUD_VELOCITY = 50;
    private static final String CLOUD_TAG = "Cloud";
    private static final List<List<Integer>> CLOUD_STRUCTURE = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

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

    private void createSky() {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    private void createTerrain() {
        this.terrain = new Terrain(windowController.getWindowDimensions(), 0);
        List<Block> blocks = terrain.createInRange(TERRAIN_MIN_RANGE,
                (int) windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createNight() {
        GameObject night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.UI);
    }

    private void createAvatar() {
        Vector2 avatarInitialPosition = new Vector2(avatarInitialX,
                terrain.groundHeightAt(avatarInitialX) - Avatar.AVATAR_SIZE);
        this.avatar = new Avatar(avatarInitialPosition,
                inputListener, imageReader);
        gameObjects().addGameObject(avatar);
        Vector2 vector = this.windowController.getWindowDimensions().mult(0.5f).add(
                avatarInitialPosition.mult(-1));
        this.setCamera(new Camera(this.avatar, vector,
                this.windowController.getWindowDimensions(),
                this.windowController.getWindowDimensions()));
    }

    private void createEnergyCounter() {
        TextRenderable energyCounterRender = new TextRenderable(
                Avatar.AVATAR_INITIAL_ENERGY + EnergyCounter.PERCENT_SIGN);
        Vector2 energyCounterLeftCorner = new Vector2(
                windowController.getWindowDimensions().x() * PepseGameManager.DIMENSION_MIDDLE -
                        ENERGY_COUNTER_SIZE, DISTANCE_FROM_TOP);
        EnergyCounter energyCounter = new EnergyCounter(energyCounterLeftCorner,
                Vector2.ONES.mult(ENERGY_COUNTER_SIZE), energyCounterRender, this.avatar::getEnergy);
        gameObjects().addGameObject(energyCounter, Layer.UI);
    }

    private void createSun() {
        this.sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    private void createSunHalo() {
        GameObject sunHalo = SunHalo.create(this.sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    private void createTrees() {
        Function<Float, Float> groundHeightFunction =
                x -> (float) Math.floor(terrain.groundHeightAt(x) / Block.SIZE) * Block.SIZE;
        this.flora = new Flora(groundHeightFunction, this.avatar::registerObserverToJump,
                this.avatar::addEnergy, this.gameObjects(), random);
        flora.createInRange(0, (int) windowController.getWindowDimensions().x());
    }

    private void createClouds() {
        Renderable cloudBlockRenderable = new RectangleRenderable(
                ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
        List<Block> cloudBlocksArray = new ArrayList<>();
        for (int row = 0; row < CLOUD_STRUCTURE.size(); row++) {
            for (int column = 0; column < CLOUD_STRUCTURE.get(row).size(); column++) {
                if (CLOUD_STRUCTURE.get(row).get(column) == 1) {
                    Vector2 blockPosition = new Vector2(
                            CLOUD_INITIAL_POSITION.x() + column * Block.SIZE,
                            CLOUD_INITIAL_POSITION.y() + row * Block.SIZE
                    );
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

    private void moveCloud(List<Block> cloudBlocksArray){
        for (Block block : cloudBlocksArray) {
            Vector2 topLeftCorner = block.getTopLeftCorner();
            float startX = topLeftCorner.x();
            float distance = windowController.getWindowDimensions().x() +
                    (CLOUD_STRUCTURE.getFirst().size() * Block.SIZE);
            float endX = topLeftCorner.x() + distance;
            new Transition<>(block, x -> block.setTopLeftCorner(new Vector2(x, topLeftCorner.y())),
                    startX, endX, Transition.LINEAR_INTERPOLATOR_FLOAT,
                    this.windowController.getWindowDimensions().x() / CLOUD_VELOCITY,
                    Transition.TransitionType.TRANSITION_LOOP, null);
        }
    }
}
