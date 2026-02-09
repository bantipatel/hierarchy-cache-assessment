# Hierarchy Cache Assessment

Senior Developer Take-Home Assessment - Java 21

## Overview

This repository contains solutions to a two-part senior developer assessment:

1. **Task 1**: Hierarchy Filter Implementation - Implement a filtering algorithm for a tree/forest data structure
2. **Task 2**: Cache Code Review - Identify production issues in a concurrent cache implementation

## Project Structure

```
hierarchy-cache-assessment/
├── src/
│   ├── main/java/com/assessment/
│   │   └── hierarchy/
│   │       ├── Hierarchy.java               # Interface defining the forest data structure
│   │       ├── ArrayBasedHierarchy.java     # Array-based implementation
│   │       └── HierarchyFilter.java         # Filtering logic (main implementation)
│   └── test/java/com/assessment/
│       └── hierarchy/
│           └── HierarchyFilterTest.java     # Comprehensive test suite (15 tests)
├── docs/
│   └── SimpleCache.md                       # Code review with 15 identified issues
├── pom.xml                                  # Maven configuration
└── README.md                                # This file
```

## Requirements

- **Java**: 21
- **Build Tool**: Maven 3.6+
- **Testing Framework**: JUnit 5.10.1

## Building and Running

### Compile the project

```bash
mvn clean compile
```

### Run all tests

```bash
mvn test
```

### Run specific test class

```bash
mvn test -Dtest=HierarchyFilterTest
```

### Package as JAR

```bash
mvn package
```

## Task 1: Hierarchy Filter Implementation

### Problem Description

The `Hierarchy` interface represents a forest (collection of trees) stored as:
- An array of node IDs in DFS traversal order
- A parallel array of node depths

Example visualization:
```
nodeIds: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
depths:  0, 1, 2, 3, 1, 0, 1, 0, 1, 1, 2

Tree structure:
1
├─ 2
│  └─ 3
│     └─ 4
└─ 5
6
└─ 7
8
├─ 9
└─ 10
   └─ 11
```

### Filtering Rules

A node appears in the filtered hierarchy if and only if:
1. The node's ID passes the predicate, **AND**
2. **All** of the node's ancestors pass the predicate

### Implementation Approach

**Algorithm** (`HierarchyFilter.filter()`):
1. Single-pass traversal (O(n) time complexity)
2. Track ancestor validity using a boolean array
3. Include nodes only when both node and all ancestors pass
4. Recalculate depths based on retained ancestors

**Key Design Decisions**:
- Uses boolean array to track which ancestors at each depth level passed
- Avoids recursion for better performance and stack safety
- Correctly handles depth recalculation when ancestors are filtered out
- Validates inputs (null checks)

### Test Coverage

15 comprehensive test cases covering:

| Category | Tests |
|----------|-------|
| **Edge Cases** | Empty hierarchy, single node, all pass, none pass |
| **Ancestor Filtering** | Root fails, middle node fails, depth recalculation |
| **Forest Structure** | Multiple trees, complex forests |
| **Sibling Handling** | Mixed predicate results among siblings |
| **Deep Nesting** | Deep hierarchies, leaf node filtering |
| **Special Cases** | Alternating patterns, null parameter validation |

### Example Usage

```java
Hierarchy forest = new ArrayBasedHierarchy(
    new int[]{1, 2, 3, 4, 5},
    new int[]{0, 1, 2, 1, 0}
);

// Filter to keep only even numbers
Hierarchy filtered = HierarchyFilter.filter(forest, nodeId -> nodeId % 2 == 0);

System.out.println(filtered.formatString());
// Output depends on which ancestors were retained
```

## Task 2: SimpleCache Code Review

### Review Summary

Identified **15 production-critical issues** in the concurrent cache implementation.

**Location**: `docs/SimpleCache.md`

### Critical Issues (Must Fix)

1. **Memory Leak** - Expired entries never removed from cache
2. **Race Condition** - TTL check has thread-safety issues  
3. **No Maximum Size** - Unbounded cache growth
4. **Stale Entries** - Expired entries left in cache

### High Priority Issues

5. Missing monitoring/metrics
6. Non-configurable TTL
7. No null value handling
8. Missing thread-safety documentation
9. `System.currentTimeMillis()` problems

### Additional Issues

10. Missing bulk operations
11. No cache warming capability
12. Misleading `size()` method
13. No invalidation API
14. Lost update semantics
15. Unbounded `ConcurrentHashMap` resizing

### Impact Analysis

For the anticipated load (thousands of reads/sec, hundreds of writes/sec, tens of concurrent threads):

- **Memory**: Unbounded growth → OutOfMemoryError
- **Performance**: Cache pollution degrades over time
- **Correctness**: Race conditions cause stale data returns
- **Monitoring**: No visibility into production behavior

### Recommendations

- Use established libraries (Guava Cache, Caffeine, Ehcache)
- If custom implementation required:
  - Implement size-based eviction (LRU)
  - Add scheduled cleanup for expired entries
  - Use atomic operations throughout
  - Add comprehensive metrics
  - Test under realistic load

## Assumptions & Design Choices

### Task 1 Assumptions

1. **Node IDs are unique** across the entire forest
2. **Depths array is valid** (follows the documented invariants)
3. **Input arrays are immutable** (not modified after creation)
4. **Predicate is deterministic** (same input → same output)
5. **Memory constraints** allow O(n) space complexity

### Task 2 Assumptions

1. **High concurrency** as specified (tens of threads)
2. **Production environment** requires reliability
3. **Cache hit rate** matters for cost/performance
4. **Eventual consistency** is acceptable (not strong consistency)

## Performance Characteristics

### Hierarchy Filter

- **Time Complexity**: O(n) where n = number of nodes
- **Space Complexity**: O(n) for output + O(d) for tracking (d = max depth)
- **Memory**: Minimal overhead beyond output arrays
- **Thread Safety**: Pure function, thread-safe if hierarchy is immutable

## Future Enhancements

### Potential Improvements

1. **Parallel filtering** for large hierarchies (Java Streams)
2. **Lazy evaluation** with streaming API
3. **Memoization** for repeated filter operations
4. **Custom collectors** for different output formats
5. **Visualization tools** for debugging forest structures

---

**Assessment Completed**: February 2026  
**Java Version**: 21  
**Build Tool**: Maven  
**Testing Framework**: JUnit 5
