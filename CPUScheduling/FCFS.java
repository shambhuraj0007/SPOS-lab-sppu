import java.util.Scanner;

public class FCFS {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n, temp;
        float avgtat = 0, avgwt = 0;
        System.out.println("*** First Come First Serve Scheduling ***");
        System.out.print("Enter Number of Process: ");
        n = sc.nextInt();
        int process[] = new int[n];
        int arrivaltime[] = new int[n];
        int burstTime[] = new int[n];
        int completionTime[] = new int[n];
        int TAT[] = new int[n];
        int waitingTime[] = new int[n];
        for (int i = 0; i < n; i++) {
            process[i] = (i + 1);
            System.out.print("\nEnter Arrival Time for processor " + (i + 1) + ":");
            arrivaltime[i] = sc.nextInt();
            System.out.print("Enter Burst Time for processor " + (i + 1) + ": ");
            burstTime[i] = sc.nextInt();
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arrivaltime[i] > arrivaltime[j]) {
                    temp = process[j];
                    process[j] = process[i];
                    process[i] = temp;
                    temp = arrivaltime[j];
                    arrivaltime[j] = arrivaltime[i];
                    arrivaltime[i] = temp;
                    temp = burstTime[j];
                    burstTime[j] = burstTime[i];
                    burstTime[i] = temp;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                completionTime[i] = arrivaltime[i] + burstTime[i];
            } else {
                if (arrivaltime[i] > completionTime[i - 1]) {
                    completionTime[i] = arrivaltime[i] + burstTime[i];
                } else {
                    completionTime[i] = completionTime[i - 1] + burstTime[i];
                }
            }
        }

        System.out.println("\n*** First Come First Serve Scheduling ***");
        System.out.println("Processor\tArrival time\tBrust time\tCompletion Time\t\tTurn around time\tWaiting time");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < n; i++) {

            TAT[i] = completionTime[i] - arrivaltime[i];
            waitingTime[i] = TAT[i] - burstTime[i];
            avgtat += TAT[i];
            avgwt += waitingTime[i];
            System.out.println("P" + process[i] + "\t\t" + arrivaltime[i] + "ms\t\t" + burstTime[i] + "ms\t\t"
                    + completionTime[i] + "ms\t\t\t" + TAT[i] + "ms\t\t\t" + waitingTime[i] + "ms");
        }
        System.out.println("\nAverage turn around time of processor: " + (avgtat / n)
                + "ms\nAverage waiting time of processor: " + (avgwt / n) + "ms");
        sc.close();
    }
}















/*
 
# ✅ ✅ **PROBLEM STATEMENT (For Exam)**

Implement **FCFS (First Come First Serve) CPU Scheduling**.

Rules:

* CPU selects the process **that arrives first**.
* Once a process gets the CPU, it **runs till completion**.
* No preemption.
* If CPU becomes idle, it waits until the next process arrives.

Compute:
✅ Completion Time (CT)
✅ Turn Around Time (TAT = CT – AT)
✅ Waiting Time (WT = TAT – BT)
✅ Average TAT and WT

---

# ✅ ✅ **CODE EXPLANATION (INTERMEDIATE LEVEL)**

---

## ✅ Step 1 — Input number of processes

```java
n = sc.nextInt();
```

---

## ✅ Step 2 — Input Arrival Time & Burst Time

```java
arrivaltime[i] = sc.nextInt();
burstTime[i] = sc.nextInt();
process[i] = i + 1;
```

---

## ✅ Step 3 — Sort by Arrival Time

```java
if(arrivaltime[i] > arrivaltime[j]) swap
```

Makes sure FCFS processes are executed in correct sequence.

---

## ✅ Step 4 — Calculating Completion Time

```java
if (i == 0)
    completionTime[i] = arrivaltime[i] + burstTime[i];
```

For first process → CT = AT + BT

Next processes:

```java
if(arrivaltime[i] > completionTime[i - 1])
    completionTime[i] = arrivaltime[i] + burstTime[i];
else
    completionTime[i] = completionTime[i - 1] + burstTime[i];
```

✔ Handles CPU idle time
✔ Handles continuous execution

---

## ✅ Step 5 — Calculating TAT & WT

```java
TAT[i] = completionTime[i] - arrivaltime[i];
waitingTime[i] = TAT[i] - burstTime[i];
```

---

## ✅ Step 6 — Average Times

```java
avgtat += TAT[i];
avgwt += waitingTime[i];
```

---

# ✅ ✅ **SAMPLE INPUT**

```
Enter Number of Process: 4

P1 → AT = 0, BT = 5
P2 → AT = 1, BT = 3
P3 → AT = 2, BT = 8
P4 → AT = 3, BT = 6
```

---

# ✅ ✅ **DRY RUN (Why This Output Happens)**

### ✅ Processes (sorted by arrival):

| Process | AT | BT |
| ------- | -- | -- |
| P1      | 0  | 5  |
| P2      | 1  | 3  |
| P3      | 2  | 8  |
| P4      | 3  | 6  |

---

## ✅ Completion Time Calculation (step by step)

### ✔ P1:

CT = 0 + 5 = **5**

### ✔ P2:

AT = 1 < CT(P1=5)
So P2 starts at 5
CT = 5 + 3 = **8**

### ✔ P3:

AT = 2 < 8
CT = 8 + 8 = **16**

### ✔ P4:

AT = 3 < 16
CT = 16 + 6 = **22**

---

# ✅ ✅ **GANTT CHART**

```
|   P1   | P2 |     P3     |   P4   |
0        5    8           16       22
```

---

# ✅ ✅ **FINAL OUTPUT TABLE**

| P  | AT | BT | CT | TAT (CT-AT) | WT (TAT-BT) |
| -- | -- | -- | -- | ----------- | ----------- |
| P1 | 0  | 5  | 5  | 5           | 0           |
| P2 | 1  | 3  | 8  | 7           | 4           |
| P3 | 2  | 8  | 16 | 14          | 6           |
| P4 | 3  | 6  | 22 | 19          | 13          |

---

# ✅ ✅ **AVERAGE TIMES**

```
Average TAT = (5 + 7 + 14 + 19) / 4 = 11.25
Average WT  = (0 + 4 + 6 + 13) / 4 = 5.75
```

---

# ✅ ✅ **FINAL OUTPUT (Program Format)**

```
*** First Come First Serve Scheduling ***

Processor  Arrival  Burst  Completion  TAT  WT
-------------------------------------------------------
P1         0        5      5           5    0
P2         1        3      8           7    4
P3         2        8      16          14   6
P4         3        6      22          19   13

Average Turn Around Time: 11.25
Average Waiting Time: 5.75



*/