import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Vector;


class MNTEntry {
	String name;
	int pp,kp,mdtp,kpdtp;
	public MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp) {
		super();
		this.name = name;
		this.pp = pp;
		this.kp = kp;
		this.mdtp = mdtp;
		this.kpdtp = kpdtp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPp() {
		return pp;
	}
	public void setPp(int pp) {
		this.pp = pp;
	}
	public int getKp() {
		return kp;
	}
	public void setKp(int kp) {
		this.kp = kp;
	}
	public int getMdtp() {
		return mdtp;
	}
	public void setMdtp(int mdtp) {
		this.mdtp = mdtp;
	}
	public int getKpdtp() {
		return kpdtp;
	}
	public void setKpdtp(int kpdtp) {
		this.kpdtp = kpdtp;
	}
}

public class MacroPass2 {

	public static void main(String[] args) throws Exception {
		BufferedReader irb=new BufferedReader(new FileReader("intermediate.txt"));
		BufferedReader mdtb=new BufferedReader(new FileReader("mdt.txt"));
		BufferedReader kpdtb=new BufferedReader(new FileReader("kpdt.txt"));
		BufferedReader mntb=new BufferedReader(new FileReader("mnt.txt"));
		
		FileWriter fr=new FileWriter("pass2.txt");
		
		HashMap<String, MNTEntry> mnt=new HashMap<>();
		HashMap<Integer, String> aptab=new HashMap<>();
		HashMap<String,Integer> aptabInverse=new HashMap<>();
		
		Vector<String>mdt=new Vector<String>();
		Vector<String>kpdt=new Vector<String>();
		
		int pp,kp,mdtp,kpdtp,paramNo;
		String line;
		while((line=mdtb.readLine())!=null)
		{
			mdt.addElement(line);
		}
		while((line=kpdtb.readLine())!=null)
		{
			kpdt.addElement(line);
		}
		while((line=mntb.readLine())!=null)
		{
			String parts[]=line.split("\\s+");
			mnt.put(parts[0], new MNTEntry(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
			
		}
		
		while((line=irb.readLine())!=null)
		{
			String []parts=line.split("\\s+");
			if(mnt.containsKey(parts[0]))
			{
				pp=mnt.get(parts[0]).getPp();
				kp=mnt.get(parts[0]).getKp();
				kpdtp=mnt.get(parts[0]).getKpdtp();
				mdtp=mnt.get(parts[0]).getMdtp();
				paramNo=1;
				for(int i=0;i<pp;i++)
				{ 
					parts[paramNo]=parts[paramNo].replace(",", "");
					aptab.put(paramNo, parts[paramNo]);
					aptabInverse.put(parts[paramNo], paramNo);
					paramNo++;
				}
				int j=kpdtp-1;
				for(int i=0;i<kp;i++)
				{
					String temp[]=kpdt.get(j).split("\t");
					aptab.put(paramNo,temp[1]);
					aptabInverse.put(temp[0],paramNo);
					j++;
					paramNo++;
				}
				
				for(int i=pp+1;i<parts.length;i++)
				{
					parts[i]=parts[i].replace(",", "");
					String splits[]=parts[i].split("=");
					String name=splits[0].replaceAll("&", "");
					aptab.put(aptabInverse.get(name),splits[1]);
				}
				int i=mdtp-1;
				while(!mdt.get(i).equalsIgnoreCase("MEND"))
				{
					String splits[]=mdt.get(i).split("\\s+");
					fr.write("+");
					for(int k=0;k<splits.length;k++)
					{
						if(splits[k].contains("(P,"))
						{
							splits[k]=splits[k].replaceAll("[^0-9]", "");
							String value=aptab.get(Integer.parseInt(splits[k]));
							fr.write(value+"\t");
						}
						else
						{
							fr.write(splits[k]+"\t");
						}
					}
					fr.write("\n");	
					i++;
				}
				
				aptab.clear();
				aptabInverse.clear();
			}
			else
			{
				fr.write(line+"\n");
			}
			
	}
	
	fr.close();
	mntb.close();
	mdtb.close();
	kpdtb.close();
	irb.close();
	}
}











































/*
 * # Problem statement (concise — for an examiner)

The program implements **Macro Processor — Pass 2**.
Pass 2 reads the tables produced by Pass 1 (MNT, MDT, KPDTAB) and the intermediate source (intermediate.txt). For every macro call in the intermediate file it:

1. Looks up macro info in **MNT** (number of positional params `pp`, number of keyword params `kp`, the MDT pointer `mdtp`, and KPDTAB pointer `kpdtp`).
2. Builds an **APTAB** (Actual Parameter Table) by:

   * placing actual positional parameters,
   * pulling default keyword parameter values from KPDTAB,
   * overwriting any keyword defaults with explicit keyword arguments from the call.
3. Expands the macro body (MDT) by replacing `(P,n)` tokens with values from APTAB and writes expanded lines to the output file (`pass2.txt`). Non-macro lines are copied unchanged.

This is Pass-2 of a two-pass macro processor used by assemblers; it performs macro **expansion** using the tables generated in Pass-1.

---

# High-level overview (for an intermediate Java developer)

Files read:

* `intermediate.txt` — the source with macro calls (macro definitions removed by Pass1)
* `mdt.txt` — macro definition table (macro bodies using `(P,n)` notation)
* `kpdt.txt` — keyword-parameter default table (lines like `B\t5`)
* `mnt.txt` — macro name table (lines like `INCR\t1\t1\t1\t1` meaning: name, pp, kp, mdtp, kpdtp)

Files written:

* `pass2.txt` — final expanded source (macro calls replaced by expanded bodies)

Key data structures:

* `HashMap<String, MNTEntry> mnt` — stores MNT entries for quick lookup by macro name.
* `Vector<String> mdt` and `Vector<String> kpdt` — in-memory MDT and KPDTAB lines.
* `HashMap<Integer,String> aptab` — maps parameter index → actual value (P1 → "X", P2 → "10", ...).
* `HashMap<String,Integer> aptabInverse` — maps parameter name (or keyword id) → index (e.g. "B" → 2).

Flow for each line of `intermediate.txt`:

* Tokenize the line (`line.split("\\s+")`).
* If the first token is a macro name (found in `mnt`), then:

  * Fill APTAB from positional tokens.
  * Load keyword defaults from KPDTAB into APTAB.
  * Apply any explicit keyword assignments from the call to overwrite APTAB entries.
  * Walk MDT starting at `mdtp` and for each MDT line until `MEND`, output a line replacing `(P,n)` with `aptab.get(n)`. Each expanded MDT line is prefixed with `'+'` in the output.
* Otherwise, copy the line to `pass2.txt` unchanged.

---

# Detailed code walk-through (step-by-step)

1. **Read tables into memory**

   ```java
   while((line=mdtb.readLine())!=null) mdt.addElement(line);
   while((line=kpdtb.readLine())!=null) kpdt.addElement(line);
   while((line=mntb.readLine())!=null) {
       String parts[]=line.split("\\s+");
       mnt.put(parts[0], new MNTEntry(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
   }
   ```

   * MDT and KPDTAB read into vectors for indexed access.
   * MNT read into a `HashMap<String,MNTEntry>` keyed by macro name.

2. **Process intermediate lines**

   ```java
   while((line=irb.readLine())!=null) {
       String []parts=line.split("\\s+");
       if(mnt.containsKey(parts[0])) {
           // macro call — expand
       } else {
           fr.write(line+"\n"); // copy as-is
       }
   }
   ```

   * `parts[0]` is tested against MNT to detect macro calls.

3. **Preparing APTAB for a macro call**

   * Retrieve `pp`, `kp`, `kpdtp`, `mdtp` from the MNT entry.
   * `paramNo = 1`.

   **Positional parameters**:

   ```java
   for(int i=0;i<pp;i++) { 
       parts[paramNo] = parts[paramNo].replace(",", "");
       aptab.put(paramNo, parts[paramNo]);
       aptabInverse.put(parts[paramNo], paramNo);
       paramNo++;
   }
   ```

   * This loop expects the actual positional arguments to be at `parts[1]`, `parts[2]`, ... (it strips commas).
   * It stores the actual argument string into `aptab` at index `paramNo`.
   * It also stores `aptabInverse` mapping from the actual argument *string* to the parameter index (this is later used for keyword assignment by name — see caveat below).

   **Load keyword defaults from KPDTAB**:

   ```java
   int j = kpdtp - 1;
   for(int i=0;i<kp;i++) {
       String temp[] = kpdt.get(j).split("\t");
       aptab.put(paramNo, temp[1]);
       aptabInverse.put(temp[0], paramNo);
       j++;
       paramNo++;
   }
   ```

   * For each keyword parameter entry in KPDTAB, puts the default value into `aptab` and records mapping `temp[0] -> paramNo` in `aptabInverse`. Note `temp[0]` is expected to be the keyword name (e.g. `B`) and `temp[1]` its default (e.g. `5`).

   **Apply explicit keyword arguments in macro call**:

   ```java
   for(int i = pp+1; i < parts.length; i++) {
       parts[i] = parts[i].replace(",", "");
       String splits[] = parts[i].split("=");
       String name = splits[0].replaceAll("&", "");
       aptab.put(aptabInverse.get(name), splits[1]);
   }
   ```

   * This loop expects keywords to appear as tokens like `&B=10` (or `B=10` depending on Pass1's format).
   * It finds the index for the keyword via `aptabInverse.get(name)` and overwrites the value stored previously.

4. **Expand MDT and write expanded lines**

   ```java
   int i = mdtp - 1;
   while(!mdt.get(i).equalsIgnoreCase("MEND")) {
       String splits[] = mdt.get(i).split("\\s+");
       fr.write("+");
       for(int k=0;k<splits.length;k++) {
           if(splits[k].contains("(P,")) {
               splits[k]=splits[k].replaceAll("[^0-9]", "");
               String value = aptab.get(Integer.parseInt(splits[k]));
               fr.write(value + "\t");
           } else {
               fr.write(splits[k] + "\t");
           }
       }
       fr.write("\n");
       i++;
   }
   ```

   * For each MDT line, tokens containing `(P,n)` are converted to numeric `n` and replaced with `aptab.get(n)`. Lines are written prefixed with `+`.

5. **Cleanup**

   * `aptab.clear()` and `aptabInverse.clear()` prepare for next macro call.

---

# Important implementation notes / assumptions / limitations (for an examiner)

1. **Tokenization assumptions:**
   The program uses `line.split("\\s+")` and expects arguments and keywords to appear as distinct whitespace-separated tokens. For correct behaviour:

   * Positional arguments must occupy `parts[1]`, `parts[2]`, ... (no commas stuck to them), or commas must be removed by `replace(",","")`.
   * Keyword arguments are expected as tokens containing `=` (for example `&B=10`) so `split("=")` yields name and value.

   If the intermediate file does not conform to these whitespace/token assumptions (for example if arguments are written as `X,10` as a single token), parsing will break or give incorrect results.

2. **KPDTAB format:**
   The code expects each KPDTAB line to be `keyword \t defaultValue` (for example `B\t5`). The keyword saved in KPDTAB is used directly as key in `aptabInverse`.

3. **Error handling / robustness:**

   * No defensive checks for invalid macro calls (missing arguments, index OOB, absent kpdt entries).
   * If a macro call supplies a keyword name not present in `aptabInverse`, `aptabInverse.get(name)` returns `null` and `aptab.put(null, value)` will throw a `NullPointerException` or fail later.
   * The program assumes `mdt` lines exist starting at `mdtp`. If `mdtp` is out of bound, it will throw.

4. **APTAB inverse mapping choices:**

   * `aptabInverse` is populated with `temp[0]` from KPDTAB (keyword name like `B`) and also with `parts[paramNo]` for positional arguments (mapping actual argument string → paramNo). Mapping a positional *value* to an index is unusual — typical implementations map parameter *names* (like `&A`) to indexes. This code will work for keyword override only when the `name` used to find index matches `temp[0]` in KPDTAB (e.g. `B`) — and the call's explicit keyword argument must be `&B=val` so `name = "B"`. The mapping of `parts[paramNo]` → index may be unnecessary or confusing; it is harmless but could collide if an actual positional value equals a keyword name. The code likely uses `aptabInverse.put(temp[0], paramNo)` as the canonical mapping for keywords.

5. **Output formatting:**

   * Expanded MDT lines are prefixed with `+` (this is a convention; `+` can be used to denote expanded macro lines in the pass2 output).
   * Fields on expanded lines are separated by tabs.

6. **Complexity:**

   * Reading files into memory is linear in file sizes.
   * Each macro call expansion scans its MDT body once, and keyword lookup is constant-time via HashMaps — overall time is proportional to input size and macro body sizes.

---

# Minimal working sample files and exact sample input / output (end-to-end)

Below are small sample files. Use these exact contents to test the program and to demonstrate expected output.

**File: `mnt.txt`**

```
INCR	1	1	1	1
```

(Fields: MacroName  PP  KP  MDTP  KPDTP)

**File: `mdt.txt`**

```
LDA	(P,1)
ADD	(P,2)
MEND
```

**File: `kpdt.txt`**

```
B	5
```

(Keyword parameter table: keyword `B` with default `5`)

**File: `intermediate.txt`**
(Important: macro call tokens must be whitespace-separated and keyword arg should be `&B=10` — i.e. a single token containing `=`. We use `INCR X &B=10`.)

```
START 100
INCR X &B=10
END
```

### Explanation of this input

* Macro `INCR` has `pp=1` (one positional param `&A`) and `kp=1` (one keyword param `&B` with default 5).
* The call `INCR X &B=10` provides positional `&A = X` and explicitly overrides keyword `&B` to `10`.

**Run MacroPass2** (with the four input files above present). The program writes `pass2.txt`.

**File: `pass2.txt` (expected output)**

```
START 100
+LDA	X	
+ADD	10	
END
```

(Each expanded macro MDT line is prefixed by `+`; `(P,1)` → `X` and `(P,2)` → `10`)

**Why this output?**

1. MNT tells us `INCR` body starts at MDT line `1` and has `pp=1`, `kp=1`.
2. Positional arg: `parts[1]` → `"X"` stored as `APTAB[1]`.
3. KPDTAB provides default for `B` as `"5"` stored as `APTAB[2]`.
4. Macro call contains `&B=10`, so the code overwrites `APTAB[2]` with `"10"`.
5. Now expand MDT lines:

   * `LDA (P,1)` → `LDA X`
   * `ADD (P,2)` → `ADD 10`
6. Each expanded line is written with a leading `+`, and other non-macro lines (`START 100`, `END`) are copied unchanged.

---
# Final notes (concise)

* The submitted `MacroPass2` implements macro expansion using the tables produced by Pass1.
* It expands `(P,n)` placeholders in MDT to actual arguments from APTAB and writes the expanded lines to `pass2.txt`.
* Correct functioning depends on input file formats and whitespace/tokenization conventions.
* I provided explicit sample input files and the exact expected output; use them to validate the program.

If you want, I can:

* Provide a **robust tokenizer** that accepts more natural argument formats (commas, spaces, mixed),
* Add **error handling** and helpful error messages,
* Produce a combined small Java test harness that creates the sample files, runs `MacroPass2` and prints `pass2.txt`. Which would you like next?

 */
