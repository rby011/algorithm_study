package algo.string;

import java.io.FileInputStream;
import java.util.Scanner;

public class SuffixArray_ManberMyer_QuickSort {
	public static void main(String args[]) throws Exception {
		String file = ".\\data\\10000.txt";
		// Scanner scan = new Scanner(System.in);
		Scanner scan = new Scanner(new FileInputStream(file));
		char txt[] = scan.next().toCharArray();
		scan.close();
		
		long stime = System.currentTimeMillis();
		
		int sfxcnt = txt.length;
		int suffix[] = new int[sfxcnt];
		int group[] = new int[sfxcnt];
		int ngroup[] = new int[sfxcnt];

		// INITIALIZE WITH THE FIRST LETTER OF EACH SUFFIX
		for (int t = 0; t < sfxcnt; t++) {
			suffix[t] = t;
			group[suffix[t]] = txt[t] - 'a';
		}

		// MANBER MYER
		for (int r = 1; r < txt.length; r = r << 1) {
			// SORT SUFFIX BASED ON PREVIOUS GROUP
			sort(suffix, group, txt, 0, sfxcnt - 1, r);

			// UPDATE GROUP
			ngroup[suffix[0]] = 0;
			for (int s = 1; s < suffix.length; s++) {
				if (compare(suffix[s - 1], suffix[s], group, r) == 0) {
					ngroup[suffix[s]] = ngroup[suffix[s - 1]];
				} else {
					ngroup[suffix[s]] = ngroup[suffix[s - 1]] + 1;
				}
			}
			group = ngroup;
		}
		
		long etime = System.currentTimeMillis();
		
		
		// PRINTOUT SUFFIX
		// System.out.println("#RESULT");
		// for (int s = 0; s < suffix.length; s++) {
		// for (int i = suffix[s]; i < txt.length; i++) {
		// System.out.print(txt[i]);
		// }
		// System.out.println();
		// }
		
		System.out.println("# TIME - TOTAL : " + (etime-stime));

	}

	public static void sort(int suffix[], int group[], char txt[], int low, int high, int radix) {
		int scanidx = low, delimidx = low - 1, pivotidx = high;

		while (scanidx < pivotidx) {
			int sfxid_scn = suffix[scanidx], sfxid_pvt = suffix[pivotidx];
			int cmpres = compare(sfxid_scn, sfxid_pvt, group, radix);
			if (cmpres <= 0) {
				delimidx++;
				int stmp = suffix[scanidx];
				suffix[scanidx] = suffix[delimidx];
				suffix[delimidx] = stmp;
			}
			scanidx++;
		}

		int stmp = suffix[delimidx + 1];
		suffix[delimidx + 1] = suffix[pivotidx];
		suffix[pivotidx] = stmp;

		if (low < delimidx)
			sort(suffix, group, txt, low, delimidx, radix);
		if (delimidx + 2 < high)
			sort(suffix, group, txt, delimidx + 2, high, radix);

	}

	// EQUAL THEN RETURN 0, sfxid1 > sfxid2 THEN RETURN +, OTHERWISE RETURN -
	public static int compare(int sfxid1, int sfxid2, int group[], int radix) {
		int grp1 = getgroup(group, sfxid1), grp2 = getgroup(group, sfxid2);
		if (grp1 == grp2) {
			return getgroup(group, sfxid1 + radix) - getgroup(group, sfxid2 + radix);

		}
		return grp1 - grp2;
	}

	public static int getgroup(int group[], int gid) {
		if (gid < group.length)
			return group[gid];
		return -1;
	}
}
