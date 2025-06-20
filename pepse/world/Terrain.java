package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Terrain} class generates and manages the ground layer of the game world.
 * It creates terrain blocks at a constant height across the screen and populates
 * vertical columns of blocks to simulate depth.
 * @author Salah Mahmied, Kais Sora.
 */
public class Terrain {
    /** The vertical fraction of the screen height at which the terrain surface begins. */
    public static final float TERRAIN_FACTOR = (float) 2 / 3;

    /** Base color of the ground blocks. */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    /** Number of vertical blocks to create per column for terrain depth. */
    private static final int TERRAIN_DEPTH = 20;

    private final float groundHeightAtX0;

    /**
     * Constructs a {@code Terrain} generator for a given window size.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed A random seed for terrain variability (currently unused).
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = windowDimensions.y() * TERRAIN_FACTOR;
    }

    /**
     * Returns the terrain surface height (Y position) at a given X-coordinate.
     * Currently, returns a constant height for all X values.
     *
     * @param x The X-coordinate.
     * @return The Y-coordinate of the terrain surface at the given X.
     */
    public float groundHeightAt(float x) {
        return this.groundHeightAtX0;
    }

    /**
     * Creates ground blocks between the specified X-coordinate range.
     * For each X value, a vertical column of blocks is created downward.
     *
     * @param minX The minimum X-coordinate (inclusive).
     * @param maxX The maximum X-coordinate (inclusive).
     * @return A list of {@link Block} objects representing the terrain.
     */
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

    /**
     * Aligns a floating-point edge to the nearest lower multiple of block size.
     *
     * @param edge The value to modify.
     * @return The edge value aligned to the block grid.
     */
    private int modifyEdge(double edge) {
        return (int) (Math.floor(edge / Block.SIZE) * Block.SIZE);
    }
}
