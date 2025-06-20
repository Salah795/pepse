package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.Random;
import java.util.function.Consumer;

/**
 * The {@code Tree} class represents a tree in the game world, composed of a trunk,
 * leaves, and optionally fruits. Fruits and leaves are placed probabilistically
 * around the top of the trunk. Fruits restore avatar energy and respond to jumps.
 * <p>
 * The class also registers {@link JumpObserver} instances for trunk, leaves, and fruits,
 * enabling animations and interactions triggered by avatar jumps.
 * @author Salah Mahmied, Kais Sora.
 */
public class Tree {
    /** The probability threshold for placing a fruit (lower value = more fruits). */
    private static final double FRUIT_PROBABILITY = 0.9f;

    /** The probability threshold for placing a leaf (lower value = more leaves). */
    private static final double LEAVES_PROBABILITY = 0.2f;

    private final Consumer<JumpObserver> jumpObserverConsumer;
    private final GameObjectCollection gameObjects;
    private final Consumer<Float> addEnergy;
    private final Vector2 position;
    private final Random random;

    /**
     * Constructs a new {@code Tree} object with a trunk, leaves, and optionally fruits,
     * all added to the given game object collection and linked to a jump observer.
     *
     * @param position The bottom-center position of the tree (aligned with the terrain).
     * @param jumpObserverConsumer A consumer to register jump-responsive components (trunk, leaves, fruits).
     * @param random A random number generator used to determine placement of leaves and fruits.
     * @param gameObjects The collection to which the tree components will be added.
     * @param addEnergy A consumer for handling energy gain from collecting fruits.
     */
    public Tree(Vector2 position, Consumer<JumpObserver> jumpObserverConsumer, Random random,
                GameObjectCollection gameObjects, Consumer<Float> addEnergy) {
        this.jumpObserverConsumer = jumpObserverConsumer;
        this.addEnergy = addEnergy;
        this.random = random;
        this.position = position;
        this.gameObjects = gameObjects;
        createTrunk();
        createLeaves();
        createFruits();
    }

    /**
     * Creates the tree's trunk and adds it to the game object collection.
     * Also registers the trunk as a jump observer.
     */
    private void createTrunk() {
        Vector2 trunkTopLeftCorner = position.add(new Vector2(0, -Trunk.HEIGHT));
        Trunk trunk = new Trunk(trunkTopLeftCorner);
        this.jumpObserverConsumer.accept(trunk);
        this.gameObjects.addGameObject(trunk, Layer.STATIC_OBJECTS);
    }

    /**
     * Randomly generates leaves around the top of the trunk and registers
     * them as jump observers. Each potential position has a probability of containing a leaf.
     */
    private void createLeaves() {
        for (int row = (int) (this.position.x() - Block.SIZE);
             row <= this.position.x() + Block.SIZE;
             row += Leaf.SIZE) {
            for (int column = (int) (this.position.y() - Leaf.SIZE);
                 column <= this.position.y() + Leaf.SIZE;
                 column += Leaf.SIZE) {
                if (random.nextFloat() >= LEAVES_PROBABILITY) {
                    Vector2 location = new Vector2(row, position.y() - Trunk.HEIGHT);
                    Leaf leaf = new Leaf(location);
                    this.gameObjects.addGameObject(leaf);
                    jumpObserverConsumer.accept(leaf);
                }
            }
        }
    }

    /**
     * Randomly generates fruits around the top of the trunk. Fruits are added to the game object collection
     * and registered as jump observers. Each potential position has a probability of containing a fruit.
     */
    private void createFruits() {
        for (int row = (int) (this.position.x() - Block.SIZE);
             row <= this.position.x() + Block.SIZE;
             row += Leaf.SIZE) {
            for (int column = (int) (this.position.y() - Leaf.SIZE);
                 column <= this.position.y() + Leaf.SIZE;
                 column += Leaf.SIZE) {
                if (random.nextFloat() >= FRUIT_PROBABILITY) {
                    Vector2 location = new Vector2(row, position.y() - Trunk.HEIGHT);
                    Fruit fruit = new Fruit(location, this.addEnergy);
                    this.gameObjects.addGameObject(fruit);
                    jumpObserverConsumer.accept(fruit);
                }
            }
        }
    }
}

