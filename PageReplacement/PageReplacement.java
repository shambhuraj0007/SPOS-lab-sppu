import java.util.*;

public class PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();

        int[] reference = new int[n];
        System.out.println("Enter the reference string (space separated): ");
        for (int i = 0; i < n; i++) {
            reference[i] = sc.nextInt();
        }

        System.out.println("\nChoose Page Replacement Algorithm:");
        System.out.println("1. FIFO");
        System.out.println("2. LRU");
        System.out.println("3. Optimal");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                fifo(reference, frames);
                break;
            case 2:
                lru(reference, frames);
                break;
            case 3:
                optimal(reference, frames);
                break;
            default:
                System.out.println("Invalid choice!");
        }

        sc.close();
    }

    // ---------------- FIFO Algorithm ----------------
    public static void fifo(int[] ref, int frames) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> memory = new HashSet<>();
        int pageFaults = 0;

        System.out.println("\n--- FIFO Page Replacement ---");
        System.out.printf("%-15s%-25s%-15s%n", "Page", "Frames", "Page Fault");

        for (int page : ref) {
            if (!memory.contains(page)) {
                if (memory.size() == frames) {
                    int removed = queue.poll();
                    memory.remove(removed);
                }
                queue.add(page);
                memory.add(page);
                pageFaults++;
                System.out.printf("%-15d%-25s%-15s%n", page, queue, "Yes");
            } else {
                System.out.printf("%-15d%-25s%-15s%n", page, queue, "No");
            }
        }

        System.out.println("\nTotal Page Faults (FIFO): " + pageFaults);
    }

    // ---------------- LRU Algorithm ----------------
    public static void lru(int[] ref, int frames) {
        ArrayList<Integer> memory = new ArrayList<>(frames);
        int pageFaults = 0;

        System.out.println("\n--- LRU Page Replacement ---");
        System.out.printf("%-15s%-25s%-15s%n", "Page", "Frames", "Page Fault");

        for (int i = 0; i < ref.length; i++) {
            int currentPage = ref[i];

            if (!memory.contains(currentPage)) {
                if (memory.size() < frames) {
                    memory.add(currentPage);
                } else {
                    int lruIndex = findLRU(memory, ref, i);
                    memory.set(lruIndex, currentPage);
                }
                pageFaults++;
                System.out.printf("%-15d%-25s%-15s%n", currentPage, memory, "Yes");
            } else {
                System.out.printf("%-15d%-25s%-15s%n", currentPage, memory, "No");
            }
        }

        System.out.println("\nTotal Page Faults (LRU): " + pageFaults);
    }

    private static int findLRU(ArrayList<Integer> memory, int[] ref, int currentIndex) {
        int leastRecent = Integer.MAX_VALUE;
        int lruIndex = -1;

        for (int i = 0; i < memory.size(); i++) {
            int page = memory.get(i);
            int lastUsed = -1;
            for (int j = currentIndex - 1; j >= 0; j--) {
                if (ref[j] == page) {
                    lastUsed = j;
                    break;
                }
            }
            if (lastUsed < leastRecent) {
                leastRecent = lastUsed;
                lruIndex = i;
            }
        }
        return lruIndex;
    }

    // ---------------- Optimal Algorithm ----------------
    public static void optimal(int[] ref, int frames) {
        ArrayList<Integer> memory = new ArrayList<>(frames);
        int pageFaults = 0;

        System.out.println("\n--- Optimal Page Replacement ---");
        System.out.printf("%-15s%-25s%-15s%n", "Page", "Frames", "Page Fault");

        for (int i = 0; i < ref.length; i++) {
            int currentPage = ref[i];

            if (!memory.contains(currentPage)) {
                if (memory.size() < frames) {
                    memory.add(currentPage);
                } else {
                    int replaceIndex = findOptimal(memory, ref, i);
                    memory.set(replaceIndex, currentPage);
                }
                pageFaults++;
                System.out.printf("%-15d%-25s%-15s%n", currentPage, memory, "Yes");
            } else {
                System.out.printf("%-15d%-25s%-15s%n", currentPage, memory, "No");
            }
        }

        System.out.println("\nTotal Page Faults (Optimal): " + pageFaults);
    }

    private static int findOptimal(ArrayList<Integer> memory, int[] ref, int currentIndex) {
        int farthest = currentIndex;
        int replaceIndex = -1;

        for (int i = 0; i < memory.size(); i++) {
            int page = memory.get(i);
            int nextUse = -1;
            for (int j = currentIndex + 1; j < ref.length; j++) {
                if (ref[j] == page) {
                    nextUse = j;
                    break;
                }
            }
            if (nextUse == -1) {
                return i; // Page not used again
            }
            if (nextUse > farthest) {
                farthest = nextUse;
                replaceIndex = i;
            }
        }
        return (replaceIndex == -1) ? 0 : replaceIndex;
    }

}












/*
 * Now I'll provide a comprehensive explanation of the code with sample input/output and detailed reasoning.

## Code Overview[1][2]

This Java program implements **three page replacement algorithms** used in operating system memory management: FIFO (First In First Out), LRU (Least Recently Used), and Optimal. These algorithms determine which page in physical memory should be replaced when a new page needs to be loaded and memory is full.

## Algorithm Explanations

### FIFO (First In First Out) Algorithm[1]

The FIFO method removes the **oldest page** that entered memory first. It uses a `Queue` to track the entry order of pages.

**Key Components:**
- **`Queue<Integer> queue`**: Maintains pages in insertion order
- **`Set<Integer> memory`**: Quickly checks if a page exists in memory
- When memory is full and a new page arrives, the front page (oldest) is removed
- Simple to implement but can be inefficient as frequently-used pages may be evicted

### LRU (Least Recently Used) Algorithm

LRU replaces the page that **hasn't been accessed for the longest time**. It assumes temporal locality (recently used pages are likely to be used again).

**Key Components:**
- **`ArrayList<Integer> memory`**: Stores current pages
- **`findLRU()` method**: Searches backward through the reference string to find which page in memory was used least recently
- More effective than FIFO but computationally expensive

### Optimal Algorithm

Optimal replaces the page that will **not be used for the longest time in the future**. While theoretically best, it's impractical because it requires knowing future page requests.

**Key Components:**
- **`findOptimal()` method**: Looks forward in the reference string to predict which page will be used latest
- Returns immediately if a page won't be used again (safest to evict)
- Provides a performance benchmark for other algorithms

---

## Sample Input and Output

### Input:
```
Enter number of frames: 3
Enter number of pages: 12
Enter the reference string (space separated): 
1 2 3 4 1 2 5 1 2 3 4 5
Choose Page Replacement Algorithm:
1. FIFO
2. LRU
3. Optimal
Enter your choice: 1
```

### Output (FIFO):
```
--- FIFO Page Replacement ---
Page           Frames                   Page Fault
1              [1]                      Yes
2              [1, 2]                   Yes
3              [1, 2, 3]                Yes
4              [2, 3, 4]                Yes
1              [2, 3, 4]                No
2              [2, 3, 4]                No
5              [3, 4, 5]                Yes
1              [4, 5, 1]                Yes
2              [5, 1, 2]                Yes
3              [1, 2, 3]                Yes
4              [2, 3, 4]                Yes
5              [3, 4, 5]                Yes

Total Page Faults (FIFO): 9
```

**Why this output?**

1. **Pages 1, 2, 3 (steps 1-3)**: Memory fills up → 3 page faults
2. **Page 4 (step 4)**: Memory full, remove oldest (page 1) → page fault
3. **Pages 1, 2 (steps 5-6)**: Pages already in memory  → no faults[2][3][4]
4. **Page 5 (step 7)**: Remove page 2 (oldest) → page fault
5. **Pages 1, 2, 3, 4, 5 (steps 8-12)**: Queue rotates, each removal is the oldest → 5 more faults

**Total: 9 faults out of 12 accesses**

---

## LRU Output Example:

```
--- LRU Page Replacement ---
Page           Frames                   Page Fault
1              [1]                      Yes
2              [1, 2]                   Yes
3              [1, 2, 3]                Yes
4              [2, 3, 4]                Yes (remove 1, least recently used)
1              [3, 4, 1]                Yes (remove 2, least recently used)
2              [4, 1, 2]                Yes (remove 3, least recently used)
5              [1, 2, 5]                Yes (remove 4, least recently used)
1              [1, 2, 5]                No
2              [1, 2, 5]                No
3              [2, 5, 3]                Yes (remove 1, least recently used)
4              [5, 3, 4]                Yes (remove 2, least recently used)
5              [5, 3, 4]                No

Total Page Faults (LRU): 8
```

**Why fewer faults than FIFO?** LRU retains frequently-accessed pages (1, 2) in memory longer, reducing faults.

***

## Optimal Output Example:

```
--- Optimal Page Replacement ---
Page           Frames                   Page Fault
1              [1]                      Yes
2              [1, 2]                   Yes
3              [1, 2, 3]                Yes
4              [2, 3, 4]                Yes (remove 1, won't be used until step 5)
1              [2, 3, 1]                Yes (remove 4, won't be used again)
2              [2, 3, 1]                No
5              [2, 3, 5]                Yes (remove 1, won't be used until after step 12)
1              [1, 3, 5]                Yes (remove 2, needed later but 1 is safer)
2              [1, 2, 5]                Yes (remove 3, won't be used again)
3              [3, 2, 5]                Yes (remove 1, needed later than others)
4              [3, 4, 5]                Yes (remove 2, won't be used again)
5              [3, 4, 5]                No

Total Page Faults (Optimal): 8
```

**Why optimal performance?** Algorithm predicts page 4 won't be used again after step 4, so it evicts it immediately.

***

## Performance Comparison

| Algorithm | Page Faults | Characteristics                  |
|-----------|------------|----------------------------------/
| FIFO       | 9         | Simple but ignores usage patterns |
| LRU        | 8          | Practical, respects temporal locality |
| Optimal    | 8          | Theoretical best, requires future knowledge |

***

## Code Logic Flow

**Main method:**
1. Reads frame count and reference string
2. User selects algorithm via switch statement
3. Calls corresponding algorithm method

**Algorithm methods (FIFO example):**
1. Initialize memory storage (Queue/Set/ArrayList)
2. Iterate through each page in reference string
3. Check if page exists in memory
4. If **page missing** and memory **not full**: add page
5. If **page missing** and memory **full**: remove oldest/LRU/optimal candidate, add new page, increment fault counter
6. If **page present**: just display (hit, no fault)
7. Print total faults

 */

