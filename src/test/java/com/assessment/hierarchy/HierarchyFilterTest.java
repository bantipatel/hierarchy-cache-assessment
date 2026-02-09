package com.assessment.hierarchy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for HierarchyFilter.
 * 
 * <p>Test coverage includes:
 * <ul>
 *   <li>Edge cases (empty, single node, all pass, none pass)</li>
 *   <li>Ancestor filtering (root fails, middle node fails)</li>
 *   <li>Multiple trees in forest</li>
 *   <li>Sibling nodes with different predicate results</li>
 *   <li>Deep hierarchies</li>
 *   <li>Complex patterns and alternating predicates</li>
 * </ul>
 */
class HierarchyFilterTest {
    
    @Test
    @DisplayName("Original test case: Filter multiples of 3")
    void testFilter() {
        // Arrange
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
            new int[]{0, 1, 2, 3, 1, 0, 1, 0, 1, 1, 2}
        );

        // Act
        Hierarchy filteredActual = HierarchyFilter.filter(unfiltered, nodeId -> nodeId % 3 != 0);

        // Assert
        Hierarchy filteredExpected = new ArrayBasedHierarchy(
            new int[]{1, 2, 5, 8, 10, 11},
            new int[]{0, 1, 1, 0, 1, 2}
        );
        assertEquals(filteredExpected.formatString(), filteredActual.formatString());
    }
    
    @Test
    @DisplayName("Empty hierarchy should return empty result")
    void testFilterEmpty() {
        // Arrange
        Hierarchy empty = new ArrayBasedHierarchy(new int[]{}, new int[]{});

        // Act
        Hierarchy filtered = HierarchyFilter.filter(empty, nodeId -> true);

        // Assert
        assertEquals("[]", filtered.formatString());
        assertEquals(0, filtered.size());
    }
    
    @Test
    @DisplayName("All nodes pass predicate")
    void testFilterAllPass() {
        // Arrange
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3},
            new int[]{0, 1, 2}
        );

        // Act
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> true);

        // Assert
        assertEquals("[1:0, 2:1, 3:2]", filtered.formatString());
    }
    
    @Test
    @DisplayName("No nodes pass predicate")
    void testFilterNonePass() {
        // Arrange
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3},
            new int[]{0, 1, 2}
        );

        // Act
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> false);

        // Assert
        assertEquals("[]", filtered.formatString());
    }
    
    @Test
    @DisplayName("Root fails predicate - entire subtree excluded")
    void testFilterRootFails() {
        // Arrange
        // Tree: 1 - 2 - 3
        // If root (1) fails, all descendants are excluded
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3},
            new int[]{0, 1, 2}
        );

        // Act
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId != 1);

        // Assert
        assertEquals("[]", filtered.formatString());
    }
    
    @Test
    @DisplayName("Middle node fails - its descendants excluded")
    void testFilterMiddleNodeFails() {
        // Arrange
        // Tree: 1 - 2 - 3 - 4
        // If 2 fails, 3 and 4 are excluded (ancestors must pass)
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4},
            new int[]{0, 1, 2, 3}
        );

        // Act
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId != 2);

        // Assert
        assertEquals("[1:0]", filtered.formatString());
    }
    
    @Test
    @DisplayName("Multiple trees with selective filtering")
    void testFilterMultipleTrees() {
        // Arrange
        // Tree 1: 10 - 20
        // Tree 2: 30 - 40 - 50
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{10, 20, 30, 40, 50},
            new int[]{0, 1, 0, 1, 2}
        );

        // Act
        // Filter out 40 (and its child 50)
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId != 40);

        // Assert
        Hierarchy expected = new ArrayBasedHierarchy(
            new int[]{10, 20, 30},
            new int[]{0, 1, 0}
        );
        assertEquals(expected.formatString(), filtered.formatString());
    }
    
    @Test
    @DisplayName("Siblings with mixed predicate results")
    void testFilterSiblings() {
        // Arrange
        // Tree: 1 - 2
        //         - 3
        //         - 4
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4},
            new int[]{0, 1, 1, 1}
        );
        // Filter to keep only even numbers - root 1 fails, so all excluded
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId % 2 == 0);
        assertEquals("[]", filtered.formatString());
        
        // Now test with even root
        Hierarchy unfiltered2 = new ArrayBasedHierarchy(
            new int[]{2, 3, 4, 5},
            new int[]{0, 1, 1, 1}
        );

        // Act
        Hierarchy filtered2 = HierarchyFilter.filter(unfiltered2, nodeId -> nodeId % 2 == 0);

        // Assert
        Hierarchy expected2 = new ArrayBasedHierarchy(
            new int[]{2, 4},
            new int[]{0, 1}
        );
        assertEquals(expected2.formatString(), filtered2.formatString());
    }
    
    @Test
    @DisplayName("Deep hierarchy nesting")
    void testFilterDeepHierarchy() {
        // Arrange
        // Tree: 1 - 2 - 3 - 4 - 5 - 6
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4, 5, 6},
            new int[]{0, 1, 2, 3, 4, 5}
        );

        // Act
        // Keep nodes <= 4
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId <= 4);

        // Assert
        Hierarchy expected = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4},
            new int[]{0, 1, 2, 3}
        );
        assertEquals(expected.formatString(), filtered.formatString());
    }
    
    @Test
    @DisplayName("Filter leaf nodes only")
    void testFilterLeafNodes() {
        // Arrange
        // Tree: 1 - 2 - 3
        //         - 4
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4},
            new int[]{0, 1, 2, 1}
        );

        // Act
        // Filter out leaves (3 and 4)
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId <= 2);

        // Assert
        Hierarchy expected = new ArrayBasedHierarchy(
            new int[]{1, 2},
            new int[]{0, 1}
        );
        assertEquals(expected.formatString(), filtered.formatString());
    }
    
    @Test
    @DisplayName("Single root node")
    void testFilterSingleNode() {
        // Arrange
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{42},
            new int[]{0}
        );

        // Act & Assert
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId == 42);
        assertEquals("[42:0]", filtered.formatString());
        
        Hierarchy filtered2 = HierarchyFilter.filter(unfiltered, nodeId -> nodeId != 42);
        assertEquals("[]", filtered2.formatString());
    }
    
    @Test
    @DisplayName("Alternating pattern - ancestor dependency")
    void testFilterAlternatingPattern() {
        // Arrange
        // Tree: 1 - 2 - 3 - 4 - 5
        Hierarchy unfiltered = new ArrayBasedHierarchy(
            new int[]{1, 2, 3, 4, 5},
            new int[]{0, 1, 2, 3, 4}
        );

        // Act
        // Keep only odd (1, 3, 5), but 3 and 5 require ancestor 2 to pass
        Hierarchy filtered = HierarchyFilter.filter(unfiltered, nodeId -> nodeId % 2 == 1);
        // Only 1 survives because 2 fails (so 3 and 5 are excluded)

        // Assert
        Hierarchy expected = new ArrayBasedHierarchy(
            new int[]{1},
            new int[]{0}
        );
        assertEquals(expected.formatString(), filtered.formatString());
    }
    
    @Test
    @DisplayName("Null hierarchy throws exception")
    void testNullHierarchy() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            HierarchyFilter.filter(null, nodeId -> true);
        });
    }
    
    @Test
    @DisplayName("Null predicate throws exception")
    void testNullPredicate() {
        // Arrange
        Hierarchy hierarchy = new ArrayBasedHierarchy(new int[]{1}, new int[]{0});

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            HierarchyFilter.filter(hierarchy, null);
        });
    }
}
