package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.Random;
import java.util.function.Consumer;

public class Tree {
    private static final double FRUIT_PROBABILITY = 0.9f;
    private static final double LEAVES_PROBABILITY = 0.2f;

    private final Consumer<JumpObserver> jumpObserverConsumer;
    private final GameObjectCollection gameObjects;
    private final Consumer<Float> addEnergy;
    private final Vector2 position;
    private final Random random;

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

    private void createTrunk() {
        Vector2 trunkTopLeftCorner = position.add(new Vector2(0, -Trunk.HEIGHT));
        Trunk trunk = new Trunk(trunkTopLeftCorner);
        this.jumpObserverConsumer.accept(trunk);
        this.gameObjects.addGameObject(trunk, Layer.STATIC_OBJECTS);
    }

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
}
