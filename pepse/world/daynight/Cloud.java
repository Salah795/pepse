package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents clouds in the game world. Cloud are composed of multiple blocks that
 * move horizontally across the screen.
 * Cloud also trigger rain drops when the player interacts with them (e.g., jumps).
 */
public class Cloud extends GameObject {
    public static final float CLOUD_VELOCITY = 50f;
    private static final String CLOUD_TAG = "Cloud";
    private static final List<List<Integer>> CLOUD_STRUCTURE = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );

    private final Vector2 windowDimensions;

    /**
     * Constructs a new Cloud instance.
     *
     * @param topLeftCorner The position of the cloud's top-left corner, in window coordinates.
     * @param dimensions    The width and height of the cloud.
     * @param renderable    The renderable used to draw the cloud (can be null).
     * @param gameObjects   The collection of game objects where the cloud will be added.
     * @param layer         The layer to which the cloud blocks will be added.
     * @param windowDimensions The dimensions of the screen.
     */
    public Cloud(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 GameObjectCollection gameObjects, int layer, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.windowDimensions = windowDimensions;
        create(topLeftCorner, gameObjects, layer);
    }

    public List<Block> create(Vector2 topLeftCorner,
                              GameObjectCollection gameObjects,
                              int layer) {
        List<Block> cloudBlocksArray = new ArrayList<>();

        Renderable cloudBlockRenderable =
                new RectangleRenderable(ColorSupplier.approximateMonoColor(
                        PepseGameManager.BASE_CLOUD_COLOR));

        // Generate blocks for the cloud based on the structure
        for (int row = 0; row < CLOUD_STRUCTURE.size(); row++) {
            for (int column = 0; column < CLOUD_STRUCTURE.get(row).size(); column++) {
                if (CLOUD_STRUCTURE.get(row).get(column) == 1) {
                    Vector2 blockPosition = new Vector2(
                            topLeftCorner.x() + column * Block.SIZE,
                            topLeftCorner.y() + row * Block.SIZE
                    );
                    Block cloudBlock = new Block(blockPosition, cloudBlockRenderable);
                    gameObjects.addGameObject(cloudBlock, layer);
                    cloudBlocksArray.add(cloudBlock);
                    cloudBlock.setTag(CLOUD_TAG);
                    cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                }
            }
        }

        Move(cloudBlocksArray);
        return cloudBlocksArray;
    }


    /**
     * Applies horizontal movement to the given list of cloud blocks.
     *
     * @param cloudBlocksArray The list of blocks representing the cloud
     *                         whose movement is to be controlled.
     */
    private void Move(List<Block> cloudBlocksArray){
        for (Block block : cloudBlocksArray) {
            Vector2 topLeftCorner = block.getTopLeftCorner();
            float startX = topLeftCorner.x();
            float distance =
                    windowDimensions.x() +
                            (CLOUD_STRUCTURE.get(0).size() * Block.SIZE);
            float endX = topLeftCorner.x() + distance;
            new Transition<>(
                    block,
                    x -> block.setTopLeftCorner(new Vector2(x, topLeftCorner.y())),
                    startX,
                    endX,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    this.windowDimensions.x() / CLOUD_VELOCITY,
                    Transition.TransitionType.TRANSITION_LOOP,
                    null
            );
        }
    }
}
