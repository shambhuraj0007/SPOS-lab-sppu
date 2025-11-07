package JNI;

import java.util.Scanner;

public class JNI {
    public native void JniAdd(int no1, int no2);

    public native void JniSub(int no1, int no2);

    public native void JniMult(int no1, int no2);

    public native void JniDiv(double no1, double no2);

    public native void JniPow(int no1, int no2);

    public native void JniSqrt(int no1);

    public native void JniMod(int no1, int no2);

    static {
        // 👇 Update this path to the location of your DLL file
        System.load(System.getProperty("user.dir") + "\\libJNI.dll");
        //System.load(System.getProperty("user.dir") + "/libJNI.so"); linux la


    }

    public static void main(String[] args) {
        int no1, no2;
        Scanner in = new Scanner(System.in);
        JNI MJ = new JNI();

        System.out.println("JNI using C");
        System.out.print("Enter first number: ");
        no1 = in.nextInt();
        System.out.print("Enter second number: ");
        no2 = in.nextInt();

        MJ.JniAdd(no1, no2);
        MJ.JniSub(no1, no2);
        MJ.JniMult(no1, no2);
        MJ.JniDiv((double) no1, (double) no2);
        MJ.JniPow(no1, no2);
        MJ.JniSqrt(no2);
        MJ.JniMod(no1, no2);

        in.close();
    }
}

















/*
 * Yes! On Ubuntu/Linux, the code must be changed — because:
✅ Linux does NOT use .dll
✅ Linux uses .so (shared object) files
✅ File paths use forward slashes / instead of \

✅ ✅ Correct Load Command on Ubuntu
Replace:
System.load(System.getProperty("user.dir") + "\\libJNI.dll");

✅ With this Linux-compatible version:
System.load(System.getProperty("user.dir") + "/libJNI.so");

OR (recommended for Linux):
System.loadLibrary("JNI");

…but only if your shared library name is:
libJNI.so

And that file must be in:
/usr/lib/
or
/usr/local/lib/
or
java.library.path


✅ ✅ Linux Folder Structure
JNIProject/
│
├── JNI/JNI.java
├── JNI/JNI.class
├── JNI.h
├── jni.c
├── libJNI.so   ✅ (NOT dll)


✅ ✅ How to Generate .so file on Ubuntu
Run these commands from project folder:
1. Compile Java + generate header
javac -h . JNI/JNI.java

2. Compile C file to .so (IMPORTANT)
gcc -shared -fPIC -o libJNI.so -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" jni.c

✅ -fPIC mandatory for Linux shared libraries
✅ include/linux required for JNI bindings

✅ ✅ Run Java Program on Linux
java -cp . JNI.JNI

✅ libJNI.so must be present in the current directory OR in the library path.
If Java cannot find the .so, run:
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$(pwd)

Then run:
java -cp . JNI.JNI


✅ FINAL ANSWER
✔️ For Ubuntu, use .so not .dll
✔️ Use forward slash /
✔️ Load library like:
System.load(System.getProperty("user.dir") + "/libJNI.so");


Below is a **clean, examiner-friendly and student-friendly explanation** of your **JNI program**:
✅ **Problem Statement**
✅ **Java Code Explanation**
✅ **C Code Explanation**
✅ **How JNI Works**
✅ **Sample Input & Output**
✅ **Why that output**
✅ **Corrections + Improvements**

Everything is explained in a structured way suitable for **SPOS viva/exam answers**.

---

# ✅ **PROBLEM STATEMENT (Exam-Friendly Answer)**

**Task:**
Implement a Java program that performs arithmetic operations using **native functions written in C** through **JNI (Java Native Interface).**

The Java program should:

* Accept two numbers from the user
* Call C functions for:
  ✅ Addition
  ✅ Subtraction
  ✅ Multiplication
  ✅ Division
  ✅ Power
  ✅ Square Root
  ✅ Mod (Remainder)

The C program (inside a DLL) must perform these operations and return/output the results.

This demonstrates **JNI integration**, which allows Java applications to use native libraries for faster performance or hardware-level operations.

---

# ✅ **JAVA CODE EXPLAINED (Line-by-Line)**

```java
package JNI;
```

Java file is inside package `JNI`.

---

### ✅ Declaring Native Methods

```java
public class JNI {
    public native void JniAdd(int no1, int no2);
    public native void JniSub(int no1, int no2);
    public native void JniMult(int no1, int no2);
    public native void JniDiv(double no1, double no2);
    public native void JniPow(int no1, int no2);
    public native void JniSqrt(int no1);
    public native void JniMod(int no1, int no2);
```

✅ These functions have **no Java body**
✅ They are **implemented in C** inside the DLL.

---

### ✅ Loading the DLL

```java
static {
    System.load("D:\\spos\\DynamicLinkLibrary\\libJNI.dll");
}
```

This loads your compiled C library at runtime.

⚠️ The path **must be correct**, or you get:
`UnsatisfiedLinkError`.

---

### ✅ Taking Input and Calling C Functions

```java
public static void main(String[] args) {
    int no1, no2;
    Scanner in = new Scanner(System.in);
    JNI MJ = new JNI();

    System.out.println("JNI using C");
    System.out.print("Enter first number: ");
    no1 = in.nextInt();
    System.out.print("Enter second number: ");
    no2 = in.nextInt();

    MJ.JniAdd(no1, no2);
    MJ.JniSub(no1, no2);
    MJ.JniMult(no1, no2);
    MJ.JniDiv((double) no1, (double) no2);
    MJ.JniPow(no1, no2);
    MJ.JniSqrt(no2);
    MJ.JniMod(no1, no2);

    in.close();
}
```

✅ Creates object
✅ Takes two inputs
✅ Calls each C function
✅ Outputs printed by C code appear in terminal

---

# ✅ **C CODE EXPLANATION (Exam-Friendly)**

Your C program implements all the native functions.

Header:

```c
#include <JNI.h>
#include<math.h>
```

Math library `math.h` used for pow(), sqrt(), fmod()

---

### ✅ Addition

```c
JNIEXPORT void JNICALL Java_JNI_JNI_JniAdd
(JNIEnv *e, jobject obj, jint no1, jint no2) {
    int add = no1 + no2;
    printf("Addition = %d", add);
}
```

---

### ✅ Subtraction

```c
JNIEXPORT void JNICALL Java_JNI_JNI_JniSub
(JNIEnv *e, jobject obj, jint no1, jint no2) {
    printf("\nSubtraction = %d", no1 - no2);
}
```

---

### ✅ Multiplication

```c
JNIEXPORT void JNICALL Java_JNI_JNI_JniMult
(JNIEnv *e, jobject obj, jint no1, jint no2) {
    printf("\nMultiplication = %d", no1 * no2);
}
```

---

### ✅ Division

```c
JNIEXPORT void JNICALL Java_JNI_JNI_JniDiv
(JNIEnv *e, jobject obj, jdouble no1, jdouble no2) {
    printf("\nDivision = %.3f", no1 / no2);
}
```

---

### ✅ Mod

```c
printf("\nRemainder = %.3f", fmod(no1,no2));
```

---

### ✅ Power

```c
printf("\nPower = %.3f", pow(no1,no2));
```

---

### ✅ Square Root

```c
printf("\nSquare root of %d = %.3f", no1, sqrt(no1));
```

---

# ✅ **HOW JNI WORKS (Exam-Friendly Explanation)**

1. **Java declares native methods**
2. **javac** compiles Java to .class
3. **javah** generates C header file
4. You write C code using function signature:

   ```
   Java_packagename_Classname_Methodname
   ```
5. Compile C code to DLL (Windows) or .so (Linux)
6. Java loads the DLL using `System.load()`
7. Java calls C functions like normal functions

✅ JNI enables Java to interact with low-level, optimized C code.

---

# ✅ **SAMPLE INPUT**

```
Enter first number: 10
Enter second number: 4
```

---

# ✅ **EXPECTED OUTPUT**

```
JNI using C
Enter first number: 10
Enter second number: 4

Addition of nos.= 14
Subtraction of nos. is= 6
Multiplication of nos. is= 40
Division of nos. is= 2.500
Power is= 10000.000
Square root 4 is= 2.000
Remainder is= 2.000
```

---

# ✅ **WHY THIS OUTPUT?**

| Operation | Calculation | Result |
| --------- | ----------- | ------ |
| Add       | 10 + 4      | 14     |
| Sub       | 10 – 4      | 6      |
| Mult      | 10 × 4      | 40     |
| Div       | 10 ÷ 4      | 2.5    |
| Pow       | 10^4        | 10000  |
| Sqrt      | √4          | 2      |
| Mod       | 10 % 4      | 2      |

✅ Every calculation is correct
✅ C prints the results
✅ Java only triggers the functions

---

# ✅ **Suggestions / Corrections**

✅ Remove unwanted braces at the bottom
✅ Use `System.loadLibrary("libJNI")` instead of full path
✅ Ensure DLL is inside

```
C:\Program Files\Java\jdk\bin
```

---



 */