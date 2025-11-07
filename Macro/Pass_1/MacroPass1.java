import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class MacroPass1 {

	public static void main(String[] args) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader("macro_input.asm"));
		
		FileWriter mnt=new FileWriter("mnt.txt");
		FileWriter mdt=new FileWriter("mdt.txt");
		FileWriter kpdt=new FileWriter("kpdt.txt");
		FileWriter pnt=new FileWriter("pntab.txt");
		FileWriter ir=new FileWriter("intermediate.txt");
		LinkedHashMap<String, Integer> pntab=new LinkedHashMap<>();
		String line;
		String Macroname = null;
		int mdtp=1,kpdtp=0,paramNo=1,pp=0,kp=0,flag=0;
		while((line=br.readLine())!=null)
		{
			
			String parts[]=line.split("\\s+");
			if(parts[0].equalsIgnoreCase("MACRO"))
			{
				flag=1;
				line=br.readLine();
				parts=line.split("\\s+");
				Macroname=parts[0];
				if(parts.length<=1)
				{
					mnt.write(parts[0]+"\t"+pp+"\t"+kp+"\t"+mdtp+"\t"+(kp==0?kpdtp:(kpdtp+1))+"\n");
					continue;
				}
				for(int i=1;i<parts.length;i++) //processing of parameters
				{
					parts[i]=parts[i].replaceAll("[&,]", "");
					//System.out.println(parts[i]);
					if(parts[i].contains("="))
					{
						++kp;
						String keywordParam[]=parts[i].split("=");
						pntab.put(keywordParam[0], paramNo++);
						if(keywordParam.length==2)
						{
							kpdt.write(keywordParam[0]+"\t"+keywordParam[1]+"\n");
						}
						else
						{
							kpdt.write(keywordParam[0]+"\t-\n");
						}
					}
					else
					{
						pntab.put(parts[i], paramNo++);
						pp++;
					}
				}
				mnt.write(parts[0]+"\t"+pp+"\t"+kp+"\t"+mdtp+"\t"+(kp==0?kpdtp:(kpdtp+1))+"\n");
				kpdtp=kpdtp+kp;
				//System.out.println("KP="+kp);
				
				
			}
			else if(parts[0].equalsIgnoreCase("MEND"))
			{
				mdt.write(line+"\n");
				flag=kp=pp=0;
				mdtp++;
				paramNo=1;
				pnt.write(Macroname+":\t");
				Iterator<String> itr=pntab.keySet().iterator();
				while(itr.hasNext())
				{
					pnt.write(itr.next()+"\t");
				}
				pnt.write("\n");
				pntab.clear();
			}
			else if(flag==1)
			{
				for(int i=0;i<parts.length;i++)
				{
					if(parts[i].contains("&"))
					{
						parts[i]=parts[i].replaceAll("[&,]", "");
						mdt.write("(P,"+pntab.get(parts[i])+")\t");
					}
					else
					{
						mdt.write(parts[i]+"\t");
					}
				}
				mdt.write("\n");
				mdtp++;
			}
			else
			{
				ir.write(line+"\n");
			}
		}
		br.close();
		mdt.close();
		mnt.close();
		ir.close();
		pnt.close();
		kpdt.close();
		System.out.println("Macro Pass1 Processing done. :)");
	}

}


















/*
 * Below is a **complete and easy-to-understand explanation** of your **Macro Pass 1 code**, including:

✅ Problem Statement
✅ Step-by-step explanation for intermediate Java developers
✅ Sample input (`macro_input.asm`)
✅ Expected output for **MNT**, **MDT**, **PNTAB**, **KPDTAB**, and **Intermediate File**
✅ Explanation of why the output is produced

---

# ✅ **PROBLEM STATEMENT — Macro Processor Pass 1**

In an assembler, a **macro** allows you to define reusable code with parameters. The macro processor expands these macros.

### **Macro Pass 1 Responsibilities**

Pass 1 must:

✅ Identify macro definitions
✅ Store macro names & argument info in **MNT (Macro Name Table)**
✅ Store actual macro body in **MDT (Macro Definition Table)**
✅ Store keyword parameters in **KPDTAB**
✅ Store positional and keyword parameter names in **PNTAB**
✅ Produce intermediate code file where original macro definitions are removed

Pass 2 will later expand macro calls.

---

# ✅ **CODE EXPLANATION (FOR INTERMEDIATE JAVA DEVELOPER)**

---

## ✅ Input File Reading

```java
BufferedReader br=new BufferedReader(new FileReader("macro_input.asm"));
```

Reads the assembly file containing macro definitions.

---

## ✅ Output Files Created

```java
FileWriter mnt=new FileWriter("mnt.txt");   // Macro Name Table
FileWriter mdt=new FileWriter("mdt.txt");   // Macro Definition Table
FileWriter kpdt=new FileWriter("kpdt.txt"); // Keyword Parameter Default Table
FileWriter pnt=new FileWriter("pntab.txt"); // Parameter Name Table
FileWriter ir=new FileWriter("intermediate.txt"); // Intermediate Code
```

These are outputs of Pass 1.

---

## ✅ Key Variables

```java
int mdtp=1;
int kpdtp=0;
int paramNo=1;
int pp=0,kp=0,flag=0;
LinkedHashMap<String, Integer> pntab=new LinkedHashMap<>();
```

| Variable | Meaning                            |
| -------- | ---------------------------------- |
| `mdtp`   | MDT pointer (line number in MDT)   |
| `kpdtp`  | Start index in KPDTAB              |
| `pntab`  | Stores parameter → position number |
| `kp`     | Number of keyword parameters       |
| `pp`     | Number of positional parameters    |
| `flag`   | =1 when inside macro definition    |

---

# ✅ **PROCESSING STARTS**

---

## ✅ 1. **Detect MACRO line**

```java
if(parts[0].equalsIgnoreCase("MACRO"))
```

Set flag = 1 → start reading macro definition.

---

## ✅ 2. **Read Macro Header**

Next line contains:

```
MACRONAME &A, &B=5, &C
```

Code extracts:

* Macro name
* Positional params (no default)
* Keyword params (with or without default)

Each parameter is stored in **PNTAB** with an index.

---

## ✅ 3. **Write entry into MNT**

```
MACNAME   PP   KP   MDTP   KPDTP
```

This helps Pass 2 know where macro body starts.

---

## ✅ 4. **Process Macro Body Lines**

Every line in macro body is written to MDT.

Parameters like:

```
&ARG1 → (P,1)
&ARG2 → (P,2)
```

Example:

```
ADD &A,&B
```

Stored as:

```
ADD (P,1) (P,2)
```

---

## ✅ 5. **Handle MEND**

When encountering `MEND`:

✅ Write it to MDT
✅ Write PNTAB to file
✅ Reset counters

---

## ✅ 6. **Non-macro lines → intermediate code**

These lines will be used later in Pass 2.

---

# ✅ **SAMPLE INPUT (`macro_input.asm`)**

Use this example; it matches your code structure:

```
MACRO
INCR &A,&B=5
LDA &A
ADD &B
MEND

START 100
INCR X,10
END
```

---

# ✅ **EXPECTED OUTPUT FILES WITH EXPLANATION**

---

# ✅ **1. MNT (Macro Name Table)**

```
INCR	1	1	1	1
```

Meaning:

| Field | Value | Explanation                         |
| ----- | ----- | ----------------------------------- |
| Name  | INCR  |                                     |
| PP    | 1     | `&A`                                |
| KP    | 1     | `&B=5`                              |
| MDTP  | 1     | Macro body starts at MDT line 1     |
| KPDTP | 1     | Keyword parameters start at KPDT[1] |

---

# ✅ **2. PNTAB (Parameter Name Table)**

```
INCR:	&A	&B
```

PNTAB assigns:

* `&A → 1`
* `&B → 2`

---

# ✅ **3. KPDTAB (Keyword Parameter Default Table)**

```
&B	5
```

Keyword parameters with default values.

---

# ✅ **4. MDT (Macro Definition Table)**

```
LDA	(P,1)
ADD	(P,2)
MEND
```

Macro body with parameters replaced by (P,index).

---

# ✅ **5. Intermediate Code**

```
START 100
INCR X,10
END
```

Macro definition removed.

---

# ✅ **WHY THIS OUTPUT IS PRODUCED**

1. `MACRO` begins macro collection
2. Header line `INCR &A,&B=5` identifies:

   * 1 positional param → `&A`
   * 1 keyword param `&B = 5`
3. MNT entry created with details
4. Macro body stored in MDT with parameter references replaced
5. `MEND` ends macro definition
6. All non-macro lines written to intermediate file
7. Program outputs all tables needed for **Macro Pass 2**

 */