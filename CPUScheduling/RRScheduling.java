import java.util.*;

class Process {
    int processID;
    int arrival, burst, waiting, turnAround, remainingTime;
    int finish, completionTime;
}

public class RRScheduling {

    public static void main(String[] args) {
        int n, sumBurst = 0, quantum, time;
        double avgWAT = 0, avgTAT = 0;
        Scanner sc = new Scanner(System.in);
        Queue<Integer> q = new LinkedList<>();
        System.out.println("*** RR Scheduling (Preemptive) ***");
        System.out.print("Enter Number of Process: ");
        n = sc.nextInt();
        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            p[i].processID = i + 1;
            System.out.print("Enter the arrival time for P" + (i + 1) + ": ");
            p[i].arrival = sc.nextInt();
            System.out.print("Enter the burst time for P" + (i + 1) + ": ");
            p[i].burst = sc.nextInt();
            p[i].remainingTime = p[i].burst;
            p[i].finish = 0;
            sumBurst += p[i].burst;
            System.out.println();
        }
        System.out.print("\nEnter time quantum: ");
        quantum = sc.nextInt();
        Process pTemp;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (p[i].arrival > p[j].arrival) {
                    pTemp = p[i];
                    p[i] = p[j];
                    p[j] = pTemp;
                }
            }
        }
        q.add(0);
        for (time = p[0].arrival; time < sumBurst;) {
            Integer I = q.remove();
            int i = I.intValue();
            if (p[i].remainingTime <= quantum) {
                time += p[i].remainingTime;
                p[i].remainingTime = 0;
                p[i].finish = 1;
                p[i].completionTime = time;
                p[i].waiting = time - p[i].arrival - p[i].burst;
                p[i].turnAround = time - p[i].arrival;
                for (int j = 0; j < n; j++) {
                    Integer J = Integer.valueOf(j);
                    if ((p[j].arrival <= time) && (p[j].finish != 1) && (!q.contains(J)))
                        q.add(j);
                }
            } else {
                time += quantum;
                p[i].remainingTime -= quantum;
                for (int j = 0; j < n; j++) {
                    Integer J = Integer.valueOf(j);
                    if (p[j].arrival <= time && p[j].finish != 1 && i != j && (!q.contains(J)))
                        q.add(j);
                }
                q.add(i);
            }
        }
        System.out.println("\n*** RR Scheduling (Preemptive) ***");
        System.out.println("Processor\tArrival time\tBrust time\tCompletion Time\t\tTurn around time\tWaiting time");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + p[i].processID + "\t\t" + p[i].arrival + "ms\t\t" + p[i].burst + "ms\t\t"
                    + p[i].completionTime + "ms\t\t\t" + p[i].turnAround + "ms\t\t\t" + p[i].waiting + "ms");
            avgWAT += p[i].waiting;
            avgTAT += p[i].turnAround;
        }
        System.out.println("\nAverage turn around time of processor: " + (avgTAT / n)
                + "ms\nAverage waiting time of processor: " + (avgWAT / n) + "ms");
                sc.close();
    }
}

















/*
Below is a **full, clear, and examiner-friendly explanation** of your Round Robin Scheduling (Preemptive) program:

✅ **Problem Statement** (as required for exams)
✅ **Line-by-line explanation for intermediate Java developer**
✅ **Sample Input**
✅ **Sample Output**
✅ **WHY that output occurs (full dry run + Gantt chart)**

---

# ✅ **PROBLEM STATEMENT (Exam-Friendly Answer)**

Implement CPU Scheduling using **Round Robin (RR)** algorithm.

**Given:**

* A set of processes with Arrival Time (AT) and Burst Time (BT)
* A time quantum (Q)

**Rules of Round Robin:**

✅ Each process gets CPU for fixed time quantum
✅ If remaining burst time > quantum → preemption
✅ Process goes to **end of ready queue**
✅ If remaining burst time ≤ quantum → process completes
✅ Compute Completion Time (CT), Turn-Around Time (TAT), Waiting Time (WT)

RR ensures:

* **Fairness**
* **Low response time**
* **Time-sharing multi-tasking**

---

# ✅ **CODE EXPLANATION (Line-by-Line)**

---

## ✅ Class Process

```java
class Process {
    int processID;
    int arrival, burst, waiting, turnAround, remainingTime;
    int finish, completionTime;
}
```

Stores all process information.

---

## ✅ Creating processes

```java
for (int i = 0; i < n; i++) {
    p[i] = new Process();
    p[i].processID = i + 1;
    p[i].arrival = sc.nextInt();
    p[i].burst = sc.nextInt();
    p[i].remainingTime = p[i].burst;
    p[i].finish = 0;
}
```

---

## ✅ Sorting processes by arrival time

```java
if (p[i].arrival > p[j].arrival) swap
```

RR needs processes sorted by arrival time.

---

## ✅ Initialize Ready Queue

```java
q.add(0);
```

Index `0` means first arriving process enters ready queue.

---

## ✅ Main Round Robin Loop

```java
for (time = p[0].arrival; time < sumBurst;) {
    i = q.remove();
```

### ✅ If process finishes in this quantum:

```java
if (p[i].remainingTime <= quantum) {
    time += p[i].remainingTime;
    p[i].remainingTime = 0;
    p[i].finish = 1;
```

✅ Completion time calculated
✅ Waiting time and TAT also computed

---

### ✅ If process is not finished (preemption)

```java
else {
    time += quantum;
    p[i].remainingTime -= quantum;
    q.add(i);  // send to back of queue
}
```

✅ Classic RR behavior

---

## ✅ Add newly arrived processes into queue

```java
if (p[j].arrival <= time && p[j].finish != 1 && !q.contains(j))
    q.add(j);
```

RR must check new arrivals after every quantum.

---

## ✅ Output Table

Shows:

* Arrival Time
* Burst Time
* Completion Time
* Turn Around Time
* Waiting Time

---

# ✅ ✅ **SAMPLE INPUT**

```
Enter number of process: 4
Enter AT and BT:
P1: AT=0 BT=5
P2: AT=1 BT=4
P3: AT=2 BT=2
P4: AT=4 BT=3

Enter quantum: 2
```

---

# ✅ ✅ **SAMPLE OUTPUT**

```
*** RR Scheduling (Preemptive) ***
Processor    AT   BT   CT    TAT   WT
-----------------------------------------
P1           0    5    14    14    9
P2           1    4    13    12    8
P3           2    2     6     4    2
P4           4    3    15    11    8

Average TAT = 10.25
Average WT  = 6.75
```

---

# ✅ ✅ **WHY THIS OUTPUT? (FULL DRY RUN)**

Quantum = **2 ms**

---

## ✅ Gantt Chart

| Time  | Executing     |
| ----- | ------------- |
| 0–2   | P1            |
| 2–4   | P2            |
| 4–6   | P3 → finishes |
| 6–8   | P1            |
| 8–10  | P2            |
| 10–12 | P4            |
| 12–13 | P1 → finishes |
| 13–14 | P2 → finishes |
| 14–15 | P4 → finishes |

---

## ✅ Completion Times (CT)

| Process | CT |
| ------- | -- |
| P1      | 14 |
| P2      | 13 |
| P3      | 6  |
| P4      | 15 |

---

## ✅ Turn Around Time (TAT = CT − AT)

| P  | AT | CT | TAT |
| -- | -- | -- | --- |
| P1 | 0  | 14 | 14  |
| P2 | 1  | 13 | 12  |
| P3 | 2  | 6  | 4   |
| P4 | 4  | 15 | 11  |

---

## ✅ Waiting Time (WT = TAT − BT)

| P  | TAT | BT | WT |
| -- | --- | -- | -- |
| P1 | 14  | 5  | 9  |
| P2 | 12  | 4  | 8  |
| P3 | 4   | 2  | 2  |
| P4 | 11  | 3  | 8  |

---

# ✅ ✅ **AVERAGES**
```
Average TAT = (14+12+4+11)/4 = 10.25
Average WT  = (9+8+2+8)/4  = 6.75
*/