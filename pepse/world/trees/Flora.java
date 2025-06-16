package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;


public class Flora {
    //TODO the implementation of this class need to be fixed because you copied it.
    private static final float TREE_PROBABILITY = 0.9f;

    private final Function<Float, Float> groundHeight;
    private final Consumer<JumpObserver> jumpObserverConsumer;
    private final Consumer<Float> addEnergy;
    private final GameObjectCollection gameObjects;
    private final Random random;

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

    public List<GameObject> createInRange(int minX, int maxX) {
        ArrayList<Tree> treesArray = new ArrayList<>();
        int normalizedMinX = alignToBlockSize(minX);
        int normalizedMaxX = alignToBlockSize(maxX);

        for (int x = normalizedMinX; x <= normalizedMaxX; x += Block.SIZE) {
            if (isAvatarPosition(x)) {
                continue;
            }
            if (shouldAddTree()) {
                Vector2 position = new Vector2(x, Trunk.HEIGHT);
                treesArray.add(new Tree(position, jumpObserverConsumer, random, addEnergy));
            }
        }

        for (Tree obj : treesArray) {
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
        return random.nextFloat() > TREE_PROBABILITY;
    }
}
