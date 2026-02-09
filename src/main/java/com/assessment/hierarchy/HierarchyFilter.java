package com.assessment.hierarchy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * Provides filtering functionality for Hierarchy instances.
 * 
 * <p>A node is present in the filtered hierarchy if and only if:
 * <ul>
 *   <li>Its node ID passes the predicate, AND</li>
 *   <li>All of its ancestors pass the predicate</li>
 * </ul>
 * 
 * <p>The filtering maintains the DFS traversal order and correctly adjusts depths
 * based on which ancestors were retained.
 */
public class HierarchyFilter {
    
    /**
     * Filters a hierarchy based on a predicate applied to node IDs.
     * 
     * <p>Algorithm:
     * <ol>
     *   <li>Traverse the hierarchy once (O(n))</li>
     *   <li>Track which ancestors at each depth level pass the predicate</li>
     *   <li>Include a node only if it and all its ancestors pass</li>
     *   <li>Recalculate depths based on retained ancestors</li>
     * </ol>
     * 
     * <p>Time Complexity: O(n) where n is the number of nodes
     * <p>Space Complexity: O(n) for the output and tracking arrays
     * 
     * @param hierarchy the input hierarchy to filter
     * @param nodeIdPredicate predicate to test each node ID
     * @return a new filtered hierarchy
     * @throws IllegalArgumentException if hierarchy or predicate is null
     */
    public static Hierarchy filter(Hierarchy hierarchy, IntPredicate nodeIdPredicate) {
        if (hierarchy == null) {
            throw new IllegalArgumentException("Hierarchy cannot be null");
        }
        if (nodeIdPredicate == null) {
            throw new IllegalArgumentException("Predicate cannot be null");
        }

        int size = hierarchy.size();
        if (size == 0) {
            return new ArrayBasedHierarchy(new int[0], new int[0]);
        }

        List<Integer> filteredNodeIds = new ArrayList<>();
        List<Integer> filteredDepths = new ArrayList<>();

        boolean[] validAtDepth = new boolean[size];
        int[] newDepthAtDepth = new int[size];

        int previousDepth = -1;

        for (int i = 0; i < size; i++) {
            int nodeId = hierarchy.nodeId(i);
            int depth = hierarchy.depth(i);

            // 🔑 CLEAR stale deeper state when depth decreases
            if (depth <= previousDepth) {
                for (int d = depth; d < size; d++) {
                    validAtDepth[d] = false;
                }
            }

            boolean parentValid = (depth == 0) || validAtDepth[depth - 1];
            boolean selfValid = parentValid && nodeIdPredicate.test(nodeId);

            validAtDepth[depth] = selfValid;

            if (selfValid) {
                int newDepth = (depth == 0) ? 0 : newDepthAtDepth[depth - 1] + 1;
                newDepthAtDepth[depth] = newDepth;

                filteredNodeIds.add(nodeId);
                filteredDepths.add(newDepth);
            }

            previousDepth = depth;
        }

        int[] nodeIdsArray = filteredNodeIds.stream().mapToInt(Integer::intValue).toArray();
        int[] depthsArray = filteredDepths.stream().mapToInt(Integer::intValue).toArray();

        return new ArrayBasedHierarchy(nodeIdsArray, depthsArray);
    }

}
