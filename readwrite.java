import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class readwrite {

    static Semaphore mutex = new Semaphore(1);    //mutex = 1
    static Semaphore wrt = new Semaphore(1);        //wrt = 1
    static int readCount = 0;
    static String message = "Hello";
    static Scanner SC = new Scanner(System.in);
    static class Reader implements Runnable {                    //Reader Thread
    public void run() {
            try {
                //Acquire Section
                mutex.acquire();                                      
                readCount++;
                if (readCount == 1) {
                    wrt.acquire();
                }
                mutex.release();
                //Reading section
                System.out.println("Thread "+Thread.currentThread().getName() + " is READING: " + message);
                Thread.sleep(1500);
                System.out.println("Thread "+Thread.currentThread().getName() + " has FINISHED READING");
                //Releasing section
                mutex.acquire();
                readCount--;
                if(readCount == 0) {
                    wrt.release();
                }
                mutex.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    static class Writer implements Runnable {                       //Writer Thread
        public void run() {
            try {
                wrt.acquire();
                message = "Good Morning";
                System.out.println("Thread "+Thread.currentThread().getName() + " is WRITING: " + message);
                       
                
                
                
                Thread.sleep(1500);
                System.out.println("Thread "+Thread.currentThread().getName() + " has finished WRITING");
                wrt.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
//Main Program
    public static void main(String[] args) throws Exception {
        Reader read = new Reader();
        Writer write = new Writer();
        Thread r1 = new Thread(read);
        r1.setName("Reader1");
        Thread r2 = new Thread(read);
        r2.setName("Reader2");
        Thread r3 = new Thread(read);
        r3.setName("Reader3");
        Thread w1 = new Thread(write);
        w1.setName("Writer1");
        Thread w2 = new Thread(write);
        w2.setName("Writer2");
        Thread w3 = new Thread(write);
        w3.setName("Writer3");
        w1.start();
        r1.start();
        w2.start();
        r2.start();
        w3.start();
        r3.start();
        
       }
}





























/*
 * Below is a **clear explanation** of the **problem statement**, **the full code**, and **sample output with reasons**, specifically written for an **intermediate Java developer**.

---

# ✅ **PROBLEM STATEMENT (Readers–Writers Problem)**

You are solving the **Readers–Writers Synchronization Problem**, which is a classic Operating System problem.

### ✅ What is the problem?

Multiple threads want to access **shared data**.

* **Readers** → only read the data
  ✅ Many readers can read **simultaneously**
  ❌ Readers must **not** read while a writer is writing

* **Writers** → modify the data
  ❌ Only **one writer** can write at a time
  ❌ No reader must read while writing
  ❌ No other writer can write while writing

### ✅ Goal

Allow **maximum concurrency** but **avoid race conditions**.

### ✅ Approach Used in This Program

The code uses **two Semaphores**:

1. `mutex` → controls access to `readCount`
2. `wrt` → ensures **exclusive write access**

This solution is known as the **First Readers–Writers problem (Reader Priority)**.

✅ Readers are allowed to read simultaneously
✅ Writers must wait until all readers finish
✅ Readers get priority over writers

---

# ✅ **CODE EXPLANATION (For Intermediate Java Developer)**

---

## ✅ **Global Variables**

```java
static Semaphore mutex = new Semaphore(1);
static Semaphore wrt = new Semaphore(1);
static int readCount = 0;
static String message = "Hello";
```

### Purpose:

* `mutex` → protects `readCount`
* `wrt` → ensures only **one writer** OR **group of readers** access shared data
* `readCount` → tracks number of active readers
* `message` → the shared resource

---

# ✅ **READER THREAD EXPLAINED**

```java
mutex.acquire();
readCount++;
if (readCount == 1) wrt.acquire();
mutex.release();
```

### ✅ What this does:

* First reader locks `wrt` to block writers
* Other readers can freely join

### ✅ Reading Section

```java
System.out.println("Thread ... is READING");
Thread.sleep(1500);
```

Readers read concurrently.

### ✅ Release Section

```java
mutex.acquire();
readCount--;
if (readCount == 0) wrt.release();
mutex.release();
```

* Last reader releases `wrt`
* Now writers can write

---

# ✅ **WRITER THREAD EXPLAINED**

```java
wrt.acquire();   // lock resource
message = "Good Morning";
System.out.println("Thread ... is WRITING");
Thread.sleep(1500);
wrt.release();
```

### ✅ Writer must:

* Acquire `wrt` → full exclusive access
* Write the shared data
* Release `wrt`

Only **one writer** at a time.

---

# ✅ **MAIN METHOD (Thread Creation)**

```java
Thread r1, r2, r3 → Readers
Thread w1, w2, w3 → Writers
```

Start order:

```
w1.start();
r1.start();
w2.start();
r2.start();
w3.start();
r3.start();
```

⚠️ **Thread scheduling is NOT deterministic**, but semaphores will force correct order of execution.

---

# ✅ **SAMPLE OUTPUT (Example)**

**(Output will vary slightly, but order logic will be similar)**

```
Thread Writer1 is WRITING: Good Morning
Thread Writer1 has finished WRITING

Thread Reader1 is READING: Good Morning
Thread Reader2 is READING: Good Morning
Thread Reader3 is READING: Good Morning
Thread Reader1 has FINISHED READING
Thread Reader2 has FINISHED READING
Thread Reader3 has FINISHED READING

Thread Writer2 is WRITING: Good Morning
Thread Writer2 has finished WRITING

Thread Writer3 is WRITING: Good Morning
Thread Writer3 has finished WRITING
```

---

# ✅ **WHY THIS OUTPUT OCCURS**

### ✅ 1. Writer1 starts first

Because main starts `w1.start()` first
→ It immediately locks `wrt`
→ All readers and writers must wait

### ✅ 2. After Writer1 finishes

Reader1 starts

* `readCount = 1` → locks `wrt`
  Readers can now read in **parallel**

### ✅ 3. Readers execute together

r1, r2, r3 read simultaneously
**Because readers only need `mutex` for readCount, not full lock**

### ✅ 4. After all readers finish

`readCount = 0` → last reader releases `wrt`

Now Writers can continue.

### ✅ 5. Writer2 executes

Locks `wrt`, writes, releases

### ✅ 6. Writer3 executes

Same process

---

# ✅ **KEY CONCURRENCY RULES THAT YOUR OUTPUT PROVES**

✅ **Many readers can read together**
✅ **Writers always have exclusive access**
✅ **Readers get priority** (if a reader comes before writer acquires `wrt`)
✅ **No starvation happens in this version**


 */