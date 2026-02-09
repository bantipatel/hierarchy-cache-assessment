# Quick Start Guide

## Prerequisites

Ensure you have installed:
- Java 21 (JDK)
- Maven 3.6+

Verify installations:
```bash
java -version   # Should show Java 21
mvn -version    # Should show Maven 3.6+
```

## Clone and Setup

```bash
# Clone the repository
git clone <your-repo-url>
cd hierarchy-cache-assessment

# Verify project structure
tree -L 3  # or use 'find . -type f' on systems without tree
```

## Build and Test

### 1. Compile the project
```bash
mvn clean compile
```

Expected output: `BUILD SUCCESS`

### 2. Run all tests
```bash
mvn test
```

Expected output:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.assessment.hierarchy.HierarchyFilterTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

### 3. Run with verbose output
```bash
mvn test -Dtest=HierarchyFilterTest#testFilter
```

### 4. Generate test report
```bash
mvn surefire-report:report
```

Report location: `target/site/surefire-report.html`

## Project Structure at a Glance

```
hierarchy-cache-assessment/
│
├── src/main/java/com/assessment/hierarchy/
│   ├── Hierarchy.java              ← Interface definition
│   ├── ArrayBasedHierarchy.java    ← Implementation
│   └── HierarchyFilter.java        ← Main algorithm (YOUR FOCUS)
│
├── src/test/java/com/assessment/hierarchy/
│   └── HierarchyFilterTest.java    ← 15 test cases
│
├── docs/
│   └── SimpleCache.md              ← Code review (15 issues identified)
│
├── pom.xml                         ← Maven config
├── README.md                       ← Full documentation
└── .gitignore                      ← Git ignore rules
```

## Key Files to Review

### Task 1: Implementation
📄 **Main Implementation**: `src/main/java/com/assessment/hierarchy/HierarchyFilter.java`
- Contains the `filter()` method
- O(n) time complexity
- Handles ancestor tracking and depth recalculation

📄 **Tests**: `src/test/java/com/assessment/hierarchy/HierarchyFilterTest.java`
- 15 comprehensive test cases
- Covers edge cases, forest structures, deep nesting

### Task 2: Code Review
📄 **Review Document**: `docs/SimpleCache.md`
- 15 production issues identified
- Impact analysis for each issue
- Recommendations for fixes

## Common Commands

| Command | Purpose |
|---------|---------|
| `mvn clean` | Remove build artifacts |
| `mvn compile` | Compile source code |
| `mvn test` | Run all tests |
| `mvn package` | Create JAR file |
| `mvn test -Dtest=ClassName` | Run specific test class |
| `mvn test -Dtest=ClassName#methodName` | Run specific test method |

## IDE Setup

### IntelliJ IDEA
1. File → Open → Select `pom.xml`
2. Wait for Maven import to complete
3. Right-click `HierarchyFilterTest.java` → Run Tests

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Select project directory
3. Right-click `HierarchyFilterTest.java` → Run As → JUnit Test

### VS Code
1. Install "Java Extension Pack"
2. Open project folder
3. Testing sidebar → Run All Tests

## Troubleshooting

### "mvn: command not found"
Install Maven: https://maven.apache.org/install.html

### "Java version not 21"
Set JAVA_HOME to Java 21 installation:
```bash
export JAVA_HOME=/path/to/java-21
export PATH=$JAVA_HOME/bin:$PATH
```

### Tests fail to run
```bash
mvn clean test -X  # Run with debug output
```

### Compilation errors
Ensure you're using Java 21:
```bash
mvn -version  # Check Maven is using correct Java version
```

## Next Steps

1. ✅ Run `mvn test` to verify everything works
2. 📖 Read `README.md` for full documentation
3. 🔍 Review `HierarchyFilter.java` implementation
4. 📝 Check `docs/SimpleCache.md` code review
5. ✨ Explore test cases in `HierarchyFilterTest.java`

## Assessment Deliverables

This repository contains:
- ✅ Working implementation of `HierarchyFilter.filter()`
- ✅ 15 comprehensive test cases
- ✅ Code review with 15 identified issues
- ✅ Professional project structure
- ✅ Complete documentation

Ready for submission! 🚀
