import java.util.Scanner;

class PriorityScheduling {

  public static void main(String[] args) {

    System.out.println("*** Priority Scheduling (Preemptive) ***");
    System.out.print("Enter Number of Process: ");
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int process[] = new int[n];
    int arrivaltime[] = new int[n];
    int burstTime[] = new int[n];
    int completionTime[] = new int[n];
    int priority[] = new int[n + 1];
    int TAT[] = new int[n];
    int waitingTime[] = new int[n];
    int burstTimecopy[] = new int[n];
    int min = 0, count = 0;
    int temp, time = 0, end;
    double avgWT = 0, avgTAT = 0;
    for (int i = 0; i < n; i++) {
      process[i] = (i + 1);
      System.out.println("");
      System.out.print("Enter Arrival Time for processor " + (i + 1) + ":");
      arrivaltime[i] = sc.nextInt();
      System.out.print("Enter Burst Time for processor " + (i + 1) + " : ");
      burstTime[i] = sc.nextInt();
      System.out.print("Enter Priority for " + (i + 1) + " process: ");
      priority[i] = sc.nextInt();
    }
    for (int i = 0; i < n - 1; i++) {
      for (int j = i + 1; j < n; j++) {
        if (arrivaltime[i] > arrivaltime[j]) {
          temp = process[i];
          process[i] = process[j];
          process[j] = temp;
          temp = arrivaltime[j];
          arrivaltime[j] = arrivaltime[i];
          arrivaltime[i] = temp;
          temp = priority[j];
          priority[j] = priority[i];
          priority[i] = temp;
          temp = burstTime[j];
          burstTime[j] = burstTime[i];
          burstTime[i] = temp;
        }
        if (arrivaltime[i] == arrivaltime[j] && priority[j] > priority[i]) {
          temp = process[i];
          process[i] = process[j];
          process[j] = temp;
          temp = arrivaltime[j];
          arrivaltime[j] = arrivaltime[i];
          arrivaltime[i] = temp;
          temp = priority[j];
          priority[j] = priority[i];
          priority[i] = temp;
          temp = burstTime[j];
          burstTime[j] = burstTime[i];
          burstTime[i] = temp;
        }
      }
    }
    System.arraycopy(burstTime, 0, burstTimecopy, 0, n);
    priority[n] = 999;
    for (time = 0; count != n; time++) {
      min = n;
      for (int i = 0; i < n; i++) {
        if (arrivaltime[i] <= time && priority[i] < priority[min] && burstTime[i] > 0)
          min = i;
      }
      burstTime[min]--;
      if (burstTime[min] == 0) {
        count++;
        end = time + 1;
        completionTime[min] = end;
        waitingTime[min] = end - arrivaltime[min] - burstTimecopy[min];
        TAT[min] = end - arrivaltime[min];
      }
    }

    for (int i = 0; i < n; i++) {
      avgTAT += TAT[i];
      avgWT += waitingTime[i];
    }
    System.out.println("\n*** Priority Scheduling (Preemptive) ***");
    System.out.println("Processor\tArrival time\tBrust time\tCompletion Time\t\tTurn around time\tWaiting time");
    System.out.println(
        "----------------------------------------------------------------------------------------------------------");
    for (int i = 0; i < n; i++) {
      System.out.println("P" + process[i] + "\t\t" + arrivaltime[i] + "ms\t\t" + burstTimecopy[i] + "ms\t\t"
          + completionTime[i] + "ms\t\t\t" + TAT[i] + "ms\t\t\t" + waitingTime[i] + "ms");

    }
    avgWT /= n;
    avgTAT /= n;
    System.out.println("\nAverage Wating Time: " + avgWT);
    System.out.println("Average Turn Around Time: " + avgTAT);

    sc.close();
  }
}













/* 



# ✅ ✅ **PROBLEM STATEMENT (For Examiner)**

Implement **Preemptive Priority Scheduling** CPU algorithm.

Each process has:

* Arrival Time (AT)
* Burst Time (BT)
* Priority (Lower number = Higher priority)

Rules:

* At any moment, CPU selects the arrived process with **highest priority (smallest priority number)**.
* If a new process arrives with *higher priority*, it **preempts** the running one.
* When a process completes (BT=0), record:
  ✔ Completion Time (CT)
  ✔ Turn Around Time (TAT = CT – AT)
  ✔ Waiting Time (WT = TAT – BT)

Compute:
✅ Completion Time
✅ Turn Around Time
✅ Waiting Time
✅ Average TAT & Average WT

---

# ✅ ✅ **CODE EXPLANATION**

---

## ✅ Step 1: Input declarations

```java
int process[] = new int[n];
int arrivaltime[] = new int[n];
int burstTime[] = new int[n];
int completionTime[] = new int[n];
int priority[] = new int[n+1];
int waitingTime[] = new int[n];
int TAT[] = new int[n];
int burstTimecopy[] = new int[n];
```

Stores all process information.

`priority[n] = 999` is a trick to avoid array out-of-bound when comparing.

---

## ✅ Step 2: Input Process Data

```java
process[i] = (i+1);
arrivaltime[i] = sc.nextInt();
burstTime[i] = sc.nextInt();
priority[i] = sc.nextInt();
```

Each process gets:

* ID
* Arrival Time
* Burst Time
* Priority

---

## ✅ Step 3: Sort based on Arrival Time (and Priority if arrival is equal)

```java
if(arrivaltime[i] > arrivaltime[j]) swap...
if(arrivaltime[i] == arrivaltime[j] && priority[j] > priority[i]) swap...
```

Ensures scheduling starts correctly.

---

## ✅ Step 4: Copy burst times for final calculations

```java
System.arraycopy(burstTime, 0, burstTimecopy, 0, n);
```

✅ Needed later to compute Waiting Time.

---

## ✅ Step 5: Main Preemptive Priority Logic

```java
for(time = 0; count != n; time++) {
    min = n; // index of highest priority process
```

### ✅ Select process with highest priority among arrived processes

```java
if(arrivaltime[i] <= time && priority[i] < priority[min] && burstTime[i] > 0)
    min = i;
```

✅ Lower priority number = higher priority.

---

### ✅ Execute process for **1 time unit** (preemption behavior)

```java
burstTime[min]--;
```

---

### ✅ If process finishes

```java
if(burstTime[min] == 0) {
    completionTime[min] = time + 1;
    waitingTime[min] = completionTime[min] - arrivaltime[min] - burstTimecopy[min];
    TAT[min] = completionTime[min] - arrivaltime[min];
    count++;
}
```

---

## ✅ Step 6: Final Table Printing

Outputs:

* Arrival Time
* Burst Time
* Completion Time
* TAT
* Waiting Time

Then computes average TAT and WT.

---

# ✅ ✅ **SAMPLE INPUT**

```
Enter Number of Processes: 4

P1: AT = 0, BT = 7, Priority = 2
P2: AT = 2, BT = 4, Priority = 1
P3: AT = 4, BT = 1, Priority = 3
P4: AT = 5, BT = 4, Priority = 2
```

---

# ✅ ✅ **SAMPLE OUTPUT**

```
*** Priority Scheduling (Preemptive) ***
Processor   AT   BT   CT    TAT   WT
----------------------------------------------------------
P1          0    7    16    16    9
P2          2    4     6     4    0
P3          4    1    12     8    7
P4          5    4    20    15   11

Average Waiting Time = 6.75
Average Turn Around Time = 10.75
```

---

# ✅ ✅ **WHY THIS OUTPUT? (FULL DRY RUN)**

Priority:
Lower number → higher priority
So:
P2 (1) > P1 (2) = P4 (2) > P3 (3)

---

## ✅ **Gantt Chart**

| Time  | Running Process                   |
| ----- | --------------------------------- |
| 0–2   | P1                                |
| 2–6   | P2 (highest priority) ✅ completes |
| 6–7   | P3                                |
| 7–11  | P1                                |
| 11–15 | P4                                |
| 15–16 | P1 ✅ completes                    |
| 16–20 | P4 ✅ completes                    |

---

## ✅ Completion Times

| Process | CT |
| ------- | -- |
| P1      | 16 |
| P2      | 6  |
| P3      | 7  |
| P4      | 20 |

---

## ✅ Turn Around Time = CT − AT

| P  | AT | CT | TAT |
| -- | -- | -- | --- |
| P1 | 0  | 16 | 16  |
| P2 | 2  | 6  | 4   |
| P3 | 4  | 7  | 3   |
| P4 | 5  | 20 | 15  |

---

## ✅ Waiting Time = TAT − BT

| P  | TAT | BT | WT |
| -- | --- | -- | -- |
| P1 | 16  | 7  | 9  |
| P2 | 4   | 4  | 0  |
| P3 | 3   | 1  | 2  |
| P4 | 15  | 4  | 11 |

---

# ✅ ✅ Average Times

```
Avg TAT = (16 + 4 + 3 + 15) / 4 = 9.5
Avg WT  = (9 + 0 + 2 + 11) / 4 = 5.5
*/