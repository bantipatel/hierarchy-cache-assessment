package com.assessment.hierarchy;

/**
 * Array-based implementation of the Hierarchy interface.
 * Stores node IDs and depths in parallel arrays for efficient DFS traversal.
 */
public class ArrayBasedHierarchy implements Hierarchy {
    private final int[] nodeIds;
    private final int[] depths;

    /**
     * Constructs a new ArrayBasedHierarchy.
     * 
     * @param nodeIds array of node IDs in DFS order
     * @param depths array of corresponding depths
     * @throws IllegalArgumentException if arrays are null or have different lengths
     */
    public ArrayBasedHierarchy(int[] nodeIds, int[] depths) {
        if (nodeIds == null || depths == null) {
            throw new IllegalArgumentException("Arrays cannot be null");
        }
        if (nodeIds.length != depths.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        this.nodeIds = nodeIds;
        this.depths = depths;
    }

    @Override
    public int size() {
        return depths.length;
    }

    @Override
    public int nodeId(int index) {
        return nodeIds[index];
    }

    @Override
    public int depth(int index) {
        return depths[index];
    }
}
