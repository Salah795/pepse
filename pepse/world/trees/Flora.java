package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The {@code Flora} class is responsible for generating trees (flora) in a specified horizontal range
 * within the game world. Trees are randomly placed based on a probability threshold and are positioned
 * on top of the terrain according to the provided ground height function.
 * @author Salah Mahmied, Kais Sora.
 */
public class Flora {
    /** Probability threshold for planting a tree at a given position. */
    private static final float TREE_PROBABILITY = 0.9f;

    private final Function<Float, Float> groundHeight;
    private final Consumer<JumpObserver> jumpObserverConsumer;
    private final Consumer<Float> addEnergy;
    private final GameObjectCollection gameObjects;
    private final Random random;

    /**
     * Constructs a new {@code Flora} instance for generating trees.
     *
     * @param groundHeightFunc A function that returns the terrain height at a given x-coordinate.
     * @param jumpObserverConsumer A consumer for registering {@link JumpObserver} instances (used for fruits).
     * @param addEnergy A consumer to handle energy increases when collecting fruits.
     * @param gameObjects The game's object collection where new trees are added.
     * @param random A random number generator used to control tree placement.
     */
    public Flora(Function<Float, Float> groundHeightFunc,
                 Consumer<JumpObserver> jumpObserverConsumer,
                 Consumer<Float> addEnergy,
                 GameObjectCollection gameObjects,
                 Random random) {
        this.groundHeight = groundHeightFunc;
        this.jumpObserverConsumer = jumpObserverConsumer;
        this.addEnergy = addEnergy;
        this.gameObjects = gameObjects;
        this.random = random;
    }

    /**
     * Generates trees in the specified x-range. Trees are planted with a certain probability,
     * and only if the x-coordinate is not equal to the avatar's starting x-position.
     *
     * @param minX The minimum x-coordinate (inclusive) of the range.
     * @param maxX The maximum x-coordinate (inclusive) of the range.
     */
    public void createInRange(int minX, int maxX) {
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            if (x == PepseGameManager.avatarInitialX) {
                continue;
            }
            if (random.nextFloat() > TREE_PROBABILITY) {
                Vector2 position = new Vector2(x, groundHeight.apply((float) x));
                new Tree(position, jumpObserverConsumer, random, gameObjects, addEnergy);
            }
        }
    }
}
