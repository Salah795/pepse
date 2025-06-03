package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;

import java.util.List;

public class PepseGameManager extends GameManager {
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
        GameObject night = Night.create(windowController.getWindowDimensions(), 30);
        //TODO check if the layer should really be UI.
        gameObjects().addGameObject(night, Layer.UI);
    }
}
