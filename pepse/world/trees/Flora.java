package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;


public class Flora {
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
