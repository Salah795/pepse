package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    public static final float TERRAIN_FACTOR = (float) 2 / 3;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    private float groundHeightAtX0;
    private Vector2 windowDimensions;

    public Terrain(Vector2 windowDimensions, int seed) {
        this.windowDimensions = new Vector2(windowDimensions);
        this.groundHeightAtX0 = windowDimensions.y() * TERRAIN_FACTOR;
    }

    public float groundHeightAt(float x) { return this.groundHeightAtX0; }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        RectangleRenderable blockRenderable = new RectangleRenderable(
                ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        minX = modifyEdge(minX);
        maxX = modifyEdge(maxX);
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            int height = modifyEdge(groundHeightAt(x));
            for (int counter = 0; counter < TERRAIN_DEPTH; counter++) {
                Block block = new Block(new Vector2(x, height), blockRenderable);
                blocks.add(block);
                height += Block.SIZE;
            }
        }
        return blocks;
    }

    private int modifyEdge(double edge) {
        return (int) (Math.floor(edge / Block.SIZE) * Block.SIZE);
    }
}
