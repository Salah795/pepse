package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.util.List;

public class PepseGameManager extends GameManager {
    public static final float DIMENSION_MIDDLE = 0.5f;
    private static final int CYCLE_LENGTH = 30;
    private static final int TERRAIN_MIN_RANGE = 0;
    private static final int ENERGY_COUNTER_SIZE = 50;
    private static final int DISTANCE_FROM_TOP = 10;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), 0);
        List<Block> blocks = terrain.createInRange(TERRAIN_MIN_RANGE,
                (int) windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        GameObject night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        //TODO check if the layer should really be UI.
        gameObjects().addGameObject(night, Layer.UI);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(sun);
        float avatarXCoordinate = windowController.getWindowDimensions().x() * DIMENSION_MIDDLE;
        Avatar avatar = new Avatar(new Vector2(avatarXCoordinate,
                terrain.groundHeightAt(avatarXCoordinate) - Avatar.AVATAR_SIZE),
                inputListener, imageReader);
        TextRenderable energyCounterRender = new TextRenderable(
                Avatar.AVATAR_INITIAL_ENERGY + "%");
        Vector2 energyCounterLeftCorner = new Vector2(
                windowController.getWindowDimensions().x() * PepseGameManager.DIMENSION_MIDDLE -
                        ENERGY_COUNTER_SIZE, DISTANCE_FROM_TOP);
        EnergyCounter energyCounter = new EnergyCounter(energyCounterLeftCorner,
                Vector2.ONES.mult(ENERGY_COUNTER_SIZE), energyCounterRender, avatar::getEnergy);
        gameObjects().addGameObject(energyCounter, Layer.UI);
        gameObjects().addGameObject(avatar);
        //TODO check if the layer should really be BACKGROUND.
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
        //TODO check if the layer should really be BACKGROUND.
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }
}
