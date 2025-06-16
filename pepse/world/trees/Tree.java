package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Tree {
    private static final double FRUIT_PROBABILITY = 0.9f;
    private static final double LEAVES_PROBABILITY = 0.2f;

    private final Consumer<JumpObserver> jumpObserverConsumer;
    private final Consumer<Float> addEnergy;
    private final Vector2 position;
    private final List<Fruit> fruitsList;
    private final List<Leaf> leavesList;
    private final Random random;
    private Trunk trunk;

    public Tree(Vector2 position, Consumer<JumpObserver> jumpObserverConsumer, Random random,
                Consumer<Float> addEnergy) {
        this.jumpObserverConsumer = jumpObserverConsumer;
        this.fruitsList = new ArrayList<>();
        this.leavesList = new ArrayList<>();
        this.addEnergy = addEnergy;
        this.random = random;
        this.position = position;
        createTrunk();
        createFruits();
        createLeaves();
    }

    private void createTrunk() {
        Vector2 trunkTopLeftCorner = position.add(new Vector2(0, -Trunk.HEIGHT));
        this.trunk = new Trunk(trunkTopLeftCorner);
        this.jumpObserverConsumer.accept(this.trunk);
    }

    private void createFruits() {
        for (int row = (int) (this.position.x() - Block.SIZE);
             row <= this.position.x() + Block.SIZE;
             row += Leaf.SIZE) {
            for (int column = (int) (this.position.y() - Leaf.SIZE);
                 column <= this.position.y() + Leaf.SIZE;
                 column += Leaf.SIZE) {
                if (random.nextFloat() >= FRUIT_PROBABILITY) {
                    Vector2 location = new Vector2(row, position.y() - (column + Leaf.SIZE));
                    Fruit fruit = new Fruit(location, this.addEnergy);
                    fruitsList.add(fruit);
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
                    Vector2 location = new Vector2(row, position.y() - (column + Leaf.SIZE));
                    Leaf leaf = new Leaf(location);
                    leavesList.add(leaf);
                    jumpObserverConsumer.accept(leaf);
                }
            }
        }
    }
}
