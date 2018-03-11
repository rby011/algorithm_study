package algo.string;

import java.io.FileInputStream;
import java.util.Scanner;

public class SuffixArray_QuickSort {
	public static void main(String args[]) throws Exception {
		
		String file = ".\\data\\10000.txt";
		Scanner scan = new Scanner(new FileInputStream(file));
		String string = scan.next();
		scan.close();
		
		char sequence[] = string.toCharArray();


		long stime = System.currentTimeMillis();

		// MAKE SUFFIX SET : O(N*N)
		char suffix[][] = makesuffixset(sequence);
		long etime1 = System.currentTimeMillis();

		// SORT SUFFIX SET : O(NlogN) * O(N)
		sort(suffix);
		long etime2 = System.currentTimeMillis();

		// PRINT OUT SUFFIX ARRAY
		//for (int s = 0; s < suffix.length; s++)
		//	System.out.println(suffix[s]);

		System.out.println("# TIME-MAKE SUFFIX : " + (etime1 - stime));
		System.out.println("# TIME-SORT SUFFIX : " + (etime2 - etime1));
		System.out.println("# STRING LENGTH : " + (string.length()));
		System.out.println("# MEMORY : " + (2 * string.length() * string.length()) / (1024 * 1024) + " MB");
	}

	public static void sort(char suffix[][]) {
		quicksort(suffix, 0, suffix.length - 1);
	}

	// COMPLEXITY O(NlogN) * O(N) : SORING * COMPARE
	public static void quicksort(char suffix[][], int low, int high) {
		int scanidx = low, delimidx = low - 1, pivotidx = high;

		// System.out.println("# low, high : " + low + "," + high);
		// for (int i = low; i <= high; i++)
		// System.out.println("> BEFORE : " + new String(suffix[i]));

		// PARTIONING
		while (scanidx < pivotidx) {
			if (compare(suffix[scanidx], suffix[pivotidx]) <= 0) {
				delimidx = delimidx + 1;
				// SWAP
				char tmp[] = suffix[scanidx];
				suffix[scanidx] = suffix[delimidx];
				suffix[delimidx] = tmp;
			}
			scanidx++;
		}
		char tmp[] = suffix[pivotidx];
		suffix[pivotidx] = suffix[delimidx + 1];
		suffix[delimidx + 1] = tmp;

		// System.out.println("# low, high : " + low + "," + high);
		// for (int i = low; i <= high; i++)
		// System.out.println("> AFTER : " + new String(suffix[i]));
		// System.out.println();

		// SORT AGAIN
		if (low < delimidx)
			quicksort(suffix, low, delimidx);
		if (delimidx + 1 < high)
			quicksort(suffix, delimidx + 1, high);
	}

	// IF txt1 IS LARGER, RETURN 1
	// IF txt1 IS SMALLER, RETURN -1
	// IF EQUAL, RETURN 0
	public static int compare(char txt1[], char txt2[]) {
		int cmplen = txt1.length > txt2.length ? txt1.length : txt2.length;

		for (int cmp = 0; cmp < cmplen; cmp++) {
			char ch1 = charat(txt1, cmp);
			char ch2 = charat(txt2, cmp);
			if (ch1 > ch2) {
				return 1;
			} else if (ch1 < ch2) {
				return -1;
			} else {
				continue;
			}
		}

		return 0;
	}

	public static char charat(char txt[], int index) {
		if (index >= txt.length)
			return 0;
		return txt[index];
	}

	// TIME COMPLEXT O(N*N)
	public static char[][] makesuffixset(char sequence[]) {
		int sfxcnt = sequence.length;
		char suffixes[][] = new char[sfxcnt][];

		for (int sfxid = 0; sfxid < sfxcnt; sfxid++)
			suffixes[sfxid] = substring(sequence, sfxid);

		return suffixes;
	}

	// TIME COMPLEXITY : O(N)
	public static char[] substring(char[] sequence, int sid) {
		int sfxlen = sequence.length - sid;
		char ret[] = new char[sfxlen];

		for (int s = sid, rid = 0; s < sequence.length; s++, rid++)
			ret[rid] = sequence[s];

		return ret;
	}
}