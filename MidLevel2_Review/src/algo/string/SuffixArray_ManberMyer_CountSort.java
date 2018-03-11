package algo.string;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class SuffixArray_ManberMyer_CountSort {

	// INCLUDING NIL CHARACTER
	static int ALPHA = 257;

	static char txt[] = null;
	static int group[] = null;
	static int suffix[] = null;

	public static void main(String args[]) throws Exception {
		String file = ".\\data\\10000.txt";
		Scanner scan = new Scanner(new FileInputStream(new File(file)));
		//Scanner scan = new Scanner(System.in);
		txt = scan.next().toCharArray();
		ALPHA = Math.max(257, txt.length + 1);
		scan.close();

		long stime = System.currentTimeMillis();

		initialize();
		sort();

		long etime = System.currentTimeMillis();

		System.out.println("# TIME - TOTAL : " + (etime - stime));
		System.out.println("# TEXT LENGTH : " + txt.length);
		//printsuffix();

	}

	private static void printsuffix() {
		for (int i = 0; i < suffix.length; i++) {
			for (int s = suffix[i]; s < txt.length; s++) {
				System.out.print(txt[s]);
			}
			System.out.println();
		}
	}

	private static void sort() {
		int counts[] = null, ordertoid[] = null, ngroup[] = null;
		for (int r = 1; r < txt.length; r = r << 1) {
			ngroup = new int[txt.length];
			counts = new int[ALPHA];
			ordertoid = new int[txt.length];

			for (int sfxid = 0; sfxid < txt.length; sfxid++)
				counts[getgroup(sfxid + r)]++;
			for (int c = 1; c < counts.length; c++)
				counts[c] = counts[c] + counts[c - 1];
			for (int sfxid = txt.length - 1; sfxid >= 0; sfxid--) {
				ordertoid[--counts[getgroup(sfxid + r)]] = sfxid;
			}

			counts = new int[ALPHA];
			for (int sfxid = 0; sfxid < txt.length; sfxid++)
				counts[getgroup(sfxid)]++;
			for (int c = 1; c < counts.length; c++)
				counts[c] = counts[c] + counts[c - 1];
			for (int order = txt.length - 1; order >= 0; order--) {
				suffix[--counts[getgroup(ordertoid[order])]] = ordertoid[order];
			}

			ngroup[suffix[0]] = 1;
			for (int order = 1; order < txt.length; order++) {
				if (comparegroup(suffix[order - 1], suffix[order], r) < 0) {
					ngroup[suffix[order]] = ngroup[suffix[order - 1]] + 1;
				} else {
					ngroup[suffix[order]] = ngroup[suffix[order - 1]];
				}
			}

			group = ngroup;
		}
	}

	private static void initialize() {
		group = new int[txt.length];
		suffix = new int[txt.length];
		for (int s = 0; s < txt.length; s++) {
			suffix[s] = s;
			group[s] = txt[s];
		}
	}

	private static int comparegroup(int sfxid1, int sfxid2, int radix) {
		int grp1 = getgroup(sfxid1), grp2 = getgroup(sfxid2);
		if (grp1 == grp2) {
			grp1 = getgroup(sfxid1 + radix);
			grp2 = getgroup(sfxid2 + radix);
		}
		return grp1 - grp2;
	}

	private static int getgroup(int idx) {
		if (idx < group.length)
			return group[idx];
		return 0;
	}
}
