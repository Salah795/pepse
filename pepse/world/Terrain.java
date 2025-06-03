package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    private float groundHeightAtX0;
    private Vector2 windowDimensions;

    public Terrain(Vector2 windowDimensions, int seed) {
        this.windowDimensions = new Vector2(windowDimensions);
        this.groundHeightAtX0 = windowDimensions.y() * ((float) 2 / 3);
    }

    public float groundHeightAt(float x) { return this.groundHeightAtX0; }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        RectangleRenderable renderable = new RectangleRenderable(
                ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        minX = (int) (Math.floor((double) minX / Block.SIZE) * Block.SIZE);
        maxX = (int) (Math.floor((double) maxX / Block.SIZE) * Block.SIZE);
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            int height = (int) Math.floor(groundHeightAt(x) / Block.SIZE)
                    * Block.SIZE;
            for (int counter = 0; counter < TERRAIN_DEPTH; counter++) {
                Block block = new Block(new Vector2(x, height), renderable);
                block.setTag("ground");
                blocks.add(block);
                height += Block.SIZE;
            }
        }
        return blocks;
    }
}
