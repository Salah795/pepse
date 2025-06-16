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


public class Flora {
    //TODO the implementation of this class need to be fixed because you copied it.
    private static final float ADD_TREE_PROBABILITY = 0.9f;
    private static final double FRUIT_ADD_PROBABILITY = 0.9f;
    private static final double LEAVES_ADD_PROBABILITY = 0.2f;
    private static final int LEAVES_IN_ROW = 2;
    private static final int LEAVES_IN_COL = 1;

    private final Function<Float, Float> groundHeight;
    private final Consumer<JumpObserver> jumpObserverConsumerConsumer;
    private final Consumer<Float> addEnergy;
    private final GameObjectCollection gameObjects;
    private final Random random;

    public Flora(Function<Float, Float> groundHeightFunc,
                 Consumer<JumpObserver> jumpObserverConsumerConsumer,
                 Consumer<Float> addEnergy,
                 GameObjectCollection gameObjects,
                 Random random) {
        this.groundHeight = groundHeightFunc;
        this.jumpObserverConsumerConsumer = jumpObserverConsumerConsumer;
        this.addEnergy = addEnergy;
        this.gameObjects = gameObjects;
        this.random = random;
    }

    public List<GameObject> createInRange(int minX, int maxX) {
        ArrayList<GameObject> treesArray = new ArrayList<>();
        int normalizedMinX = alignToBlockSize(minX);
        int normalizedMaxX = alignToBlockSize(maxX);

        for (int x = normalizedMinX; x <= normalizedMaxX; x += Block.SIZE) {
            if (isAvatarPosition(x)) {
                continue;
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

    private int alignToBlockSize(int value) {
        return (value / Block.SIZE) * Block.SIZE;
    }

    private boolean isAvatarPosition(int x) {
        return x == PepseGameManager.avatarInitialX;
    }

    private boolean shouldAddTree() {
        return random.nextFloat() > ADD_TREE_PROBABILITY;
    }

    private Trunk createTrunk(float x, int height) {
        Vector2 trunkLocation = new Vector2(x, groundHeight.apply(x));
        Vector2 trunkTopLeftCorner = trunkLocation.add(new Vector2(0, -height));

        Trunk trunk = new Trunk(trunkTopLeftCorner);
        Vector2 updatedLocation = new Vector2(x, groundHeight.apply(x) - height);
        trunk.setTopLeftCorner(updatedLocation);
        jumpObserverConsumerConsumer.accept(trunk);
        return trunk;
    }

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

    private void addLeafIfNeeded(List<GameObject> list, int row, int col) {
        if (random.nextFloat() >= LEAVES_ADD_PROBABILITY) {
            Vector2 location = calculateObjectLocation(row, col, Leaf.SIZE);
            Leaf leaf = new Leaf(location);
            list.add(leaf);
            jumpObserverConsumerConsumer.accept(leaf);
        }
    }

    private void addFruitIfNeeded(List<GameObject> list, int row, int col) {
        if (random.nextFloat() >= FRUIT_ADD_PROBABILITY) {
            Vector2 location = calculateObjectLocation(row, col, Leaf.SIZE);
            Fruit fruit = new Fruit(location, this.addEnergy);
            list.add(fruit);
            jumpObserverConsumerConsumer.accept(fruit);
        }
    }

    private Vector2 calculateObjectLocation(int x, int y, float size) {
        float groundHeight = this.groundHeight.apply((float) x);
        return new Vector2(x, groundHeight - (y + size));
    }
}
