package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.util.List;

public class PepseGameManager extends GameManager {
    private static final int CYCLE_LENGTH = 30;

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
        List<Block> blocks = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        GameObject night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        //TODO check if the layer should really be UI.
        gameObjects().addGameObject(night, Layer.UI);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(sun);
        float avatarXCoordinate = windowController.getWindowDimensions().x() / 2;
        Avatar avatar = new Avatar(new Vector2(avatarXCoordinate,
                terrain.groundHeightAt(avatarXCoordinate) - Avatar.AVATAR_SIZE),
                inputListener, imageReader);
        gameObjects().addGameObject(avatar);
        //TODO check if the layer should really be BACKGROUND.
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
        //TODO check if the layer should really be BACKGROUND.
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }
}
