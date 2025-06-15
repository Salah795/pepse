package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The `Flora` class handles the generation of trees, leaves, and fruits in the game world.
 * It uses procedural generation to create these objects within a specified range and adds
 * them to the game.
 */
public class Flora {
    //TODO the implementation of this class need to be fixed because you copied it.
    private static final float ADD_TREE_PROBABILITY = 0.9f;
    private static final double FRUIT_ADD_PROBABILITY = 0.9f;
    private static final double LEAVES_ADD_PROBABILITY = 0.2f;
    private static final int LEAVES_IN_ROW = 2;
    private static final int LEAVES_IN_COL = 1;

    private final Function<Float, Float> groundHeightFunc; // Function to calculate ground height
    private final Consumer<JumpObserver> jumpingOverObjectsConsumer; // Handles jumpable objects
    private final Consumer<Float> addEnergyFunc; // Handles energy addition for the avatar
    private final GameObjectCollection gameObjects; // Game object collection for managing objects
    private final Random random; // Random generator for procedural placement

    /**
     * Constructs a `Flora` instance for generating trees, leaves, and fruits.
     *
     * @param groundHeightFunc Function to calculate the ground height at a given x-coordinate.
     * @param jumpingOverObjectsConsumer Consumer for handling objects that can be interacted
     *                                  with during jumps.
     * @param addEnergyFunc Consumer for adding energy to the avatar when interacting with fruits.
     * @param gameObjects Collection to manage all game objects.
     * @param random Random instance for procedural generation.
     */
    public Flora(Function<Float, Float> groundHeightFunc,
                 Consumer<JumpObserver> jumpingOverObjectsConsumer,
                 Consumer<Float> addEnergyFunc,
                 GameObjectCollection gameObjects,
                 Random random) {
        this.groundHeightFunc = groundHeightFunc;
        this.jumpingOverObjectsConsumer = jumpingOverObjectsConsumer;
        this.addEnergyFunc = addEnergyFunc;
        this.gameObjects = gameObjects;
        this.random = random;
    }

    /**
     * Generates trees, leaves, and fruits within the specified range and adds them to the game.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of all generated game objects.
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        return createInRangeAndReturn(minX, maxX);
    }

    /**
     * Aligns a value to the nearest block boundary.
     *
     * @param value The value to align.
     * @return The aligned value.
     */
    private int alignToBlockSize(int value) {
        return (value / Block.SIZE) * Block.SIZE;
    }

    /**
     * Checks if the given x-coordinate is the avatar's position.
     *
     * @param x The x-coordinate to check.
     * @return True if the x-coordinate matches the avatar's position, false otherwise.
     */
    private boolean isAvatarPosition(int x) {
        return x == PepseGameManager.avatarInitialX;
    }

    /**
     * Determines whether a tree should be added based on a random probability.
     *
     * @return True if a tree should be added, false otherwise.
     */
    private boolean shouldAddTree() {
        return random.nextFloat() > ADD_TREE_PROBABILITY;
    }

    /**
     * Creates a tree trunk at the specified x-coordinate and height.
     *
     * @param x The x-coordinate of the trunk.
     * @param height The height of the trunk.
     * @return The created trunk object.
     */
    private Trunk createTrunk(float x, int height) {
        // Calculate the location and size of the trunk
        Vector2 trunkLocation = new Vector2(x, groundHeightFunc.apply(x));
        Vector2 trunkTopLeftCorner = trunkLocation.add(new Vector2(0, -height));
        Vector2 trunkSize = new Vector2(Trunk.WIDTH, height);

        // Create a renderable with a slightly varied color for the trunk
        Renderable trunkRenderable = new RectangleRenderable(
                ColorSupplier.approximateColor(Trunk.COLOR));

        Trunk trunk = new Trunk(trunkTopLeftCorner, trunkSize, trunkRenderable);
        Vector2 updatedLocation = new Vector2(x, groundHeightFunc.apply(x) - height);
        trunk.setTopLeftCorner(updatedLocation);
        jumpingOverObjectsConsumer.accept(trunk);
        return trunk;
    }

    /**
     * Creates leaves and fruits for a tree within the specified range.
     *
     * @param x The x-coordinate of the tree.
     * @param height The height of the tree.
     * @return A list of leaves and fruits created for the tree.
     */
    private List<GameObject> createFruitsAndLeaves(int x, int height) {
        List<GameObject> fruitsAndLeaves = new ArrayList<>();
        int minX = x - Block.SIZE * LEAVES_IN_ROW;
        int maxX = x + Block.SIZE * LEAVES_IN_ROW;
        int minY = height - Leaf.SIZE * LEAVES_IN_COL;
        int maxY = height + Leaf.SIZE * LEAVES_IN_COL;

        for (int row = minX; row <= maxX; row += Leaf.SIZE) {
            for (int col = minY; col <= maxY; col += Leaf.SIZE) {
                addLeafIfNeeded(fruitsAndLeaves, row, col);
                addFruitIfNeeded(fruitsAndLeaves, row, col);
            }
        }
        return fruitsAndLeaves;
    }

    /**
     * Adds a leaf to the list if the random probability allows it.
     *
     * @param list The list to add the leaf to.
     * @param row The row position of the leaf.
     * @param col The column position of the leaf.
     */
    private void addLeafIfNeeded(List<GameObject> list, int row, int col) {
        if (random.nextFloat() >= LEAVES_ADD_PROBABILITY) {
            Vector2 location = calculateObjectLocation(row, col, Leaf.SIZE);
            Leaf leaf = new Leaf(location);
            list.add(leaf);
            jumpingOverObjectsConsumer.accept(leaf);
        }
    }

    /**
     * Adds a fruit to the list if the random probability allows it.
     *
     * @param list The list to add the fruit to.
     * @param row The row position of the fruit.
     * @param col The column position of the fruit.
     */
    private void addFruitIfNeeded(List<GameObject> list, int row, int col) {
        if (random.nextFloat() >= FRUIT_ADD_PROBABILITY) {
            Vector2 location = calculateObjectLocation(row, col, Leaf.SIZE);
            Fruit fruit = new Fruit(location, this.addEnergyFunc);
            list.add(fruit);
            jumpingOverObjectsConsumer.accept(fruit);
        }
    }

    /**
     * Calculates the location of an object based on the row, column, and size.
     *
     * @param x The x-coordinate of the object.
     * @param y The y-coordinate of the object.
     * @param size The size of the object.
     * @return The calculated location as a Vector2.
     */
    private Vector2 calculateObjectLocation(int x, int y, float size) {
        float groundHeight = groundHeightFunc.apply((float) x);
        return new Vector2(x, groundHeight - (y + size));
    }

    /**
     * Generates trees, leaves, and fruits within the specified range and returns them as a list.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of all generated game objects.
     */
   // @Override
    public List<GameObject> createInRangeAndReturn(int minX, int maxX) {
        ArrayList<GameObject> treesArray = new ArrayList<>();
        int normalizedMinX = alignToBlockSize(minX);
        int normalizedMaxX = alignToBlockSize(maxX);

        for (int x = normalizedMinX; x <= normalizedMaxX; x += Block.SIZE) {
            if (isAvatarPosition(x)) {
                continue; // Skip tree creation at the avatar's position
            }
            if (shouldAddTree()) {
                treesArray.add(createTrunk(x, Trunk.HEIGHT));
                treesArray.addAll(createFruitsAndLeaves(x, Trunk.HEIGHT));
            }
        }

        for (GameObject obj : treesArray) {
            if (obj.getTag().equals(Leaf.LEAF_TAG)) {
                this.gameObjects.addGameObject(obj, Layer.DEFAULT - 1);
            } else if (obj.getTag().equals(Fruit.FRUIT_TAG)) {
                this.gameObjects.addGameObject(obj);
            } else {
                this.gameObjects.addGameObject(obj, Layer.STATIC_OBJECTS);
            }
        }
        return treesArray;
    }
}
