package algo.string;

import java.util.Scanner;

public class SuffixArray_ManberMyer_CountSort_Reimpl {

	static int group[] = null, suffix[] = null;
	static int sfxcnt = 0, radix = 0;
	static char txt[] = null;

	public static void main(String args[]) {
		// GET DATA AND INITIALIZE
		Scanner scan = new Scanner(System.in);
		txt = scan.next().toCharArray();
		radix = sfxcnt = txt.length;
		suffix = new int[radix];
		group = new int[sfxcnt];
		for (int order = 0, sfxid = 0; sfxid < sfxcnt; order++, sfxid++) {
			suffix[order] = sfxid;
			group[sfxid] = txt[sfxid] - 'a';// ASSIGN INITIAL GROUP ID
		}
		scan.close();

		makesfxarray();

		printsfx();
	}

	private static void printsfx() {
		for (int i = 0; i < sfxcnt; i++) {
			for (int j = suffix[i]; j < radix; j++) {
				System.out.print(txt[j]);
			}
			System.out.println();
		}
	}

	private static void makesfxarray() {
		int counts[] = null, ordertosfxid[] = null, ngroup[] = null;
		for (int r = 1; r < radix; r = r << 1) {
			ordertosfxid = new int[radix];
			ngroup = new int[sfxcnt];
			counts = new int[radix * 10];

			System.out.println("# R : " + r);
			System.out.println("# BEFORE");
			printsfx();

			for (int sfxid = 0; sfxid < sfxcnt; sfxid++)
				counts[getgroup(sfxid + r)]++;
			for (int cntid = 1; cntid < counts.length; cntid++)
				counts[cntid] = counts[cntid] + counts[cntid - 1];
			for (int sfxid = ordertosfxid.length - 1; sfxid >= 0; sfxid--) {
				int firstlocofsfx = sfxid;
				int order = --counts[getgroup(firstlocofsfx + r)];
				ordertosfxid[order] = sfxid;
			}

			counts = new int[radix * 10];
			for (int sfxid = 0; sfxid < sfxcnt; sfxid++)
				counts[getgroup(sfxid)]++;
			for (int cntid = 1; cntid < counts.length; cntid++)
				counts[cntid] = counts[cntid] + counts[cntid - 1];
			for (int order = ordertosfxid.length - 1; order >= 0; order--) {
				int sfxid = ordertosfxid[order];
				int index = --counts[getgroup(sfxid)];
				suffix[index] = sfxid;
			}

			System.out.println("# AFTER");
			printsfx();
			System.out.println();

			ngroup[suffix[0]] = 0;
			for (int order = 1; order < sfxcnt; order++) {
				if (comparegroup(suffix[order - 1], suffix[order], r) < 0) {
					ngroup[suffix[order]] = ngroup[suffix[order - 1]] + 1;
				} else {
					ngroup[suffix[order]] = ngroup[suffix[order - 1]];
				}

			}
			group = ngroup;
		}
	}

	private static int comparegroup(int sfxid1, int sfxid2, int radix) {
		int grpid1 = getgroup(sfxid1);
		int grpid2 = getgroup(sfxid2);
		if (grpid1 == grpid2) {
			grpid1 = getgroup(sfxid1 + radix);
			grpid2 = getgroup(sfxid2 + radix);
		}
		return grpid1 - grpid2;
	}

	private static int getgroup(int sfxid) {
		if (sfxid < group.length)
			return group[sfxid];
		return 0;
	}

}
