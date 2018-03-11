package algo.string;

import java.io.FileInputStream;
import java.util.Scanner;

public class SuffixArray_LSDRadixSort {
	static final int ALPHA = 'z' - 'a' + 2;// INCLUDING 0 CHARACTER

	public static void main(String args[]) throws Exception {
		String file = ".\\data\\10000.txt";
		Scanner scan = new Scanner(new FileInputStream(file));
		//Scanner scan = new Scanner(System.in);

		char sequence[] = scan.next().toCharArray();
		scan.close();

		long stime = System.currentTimeMillis();
		// INTINALIZE SUFFIX ARRAY
		int sfxcnt = sequence.length, radix = sequence.length;
		int suffix[] = initialize(sfxcnt);
		long etime1 = System.currentTimeMillis();


		// SORT
		suffix = sort(sequence, suffix, radix);
		long etime2 = System.currentTimeMillis();

		// PRINT SUFFIX ARRAY
		// for (int s = 0; s < suffix.length; s++) {
		// for (int i = suffix[s]; i < sequence.length; i++) {
		// System.out.print(sequence[i]);
		// }
		// System.out.println();
		// }

		System.out.println("# TIME - MAKE SUFFIX ARRAY : " + (etime1 - stime));
		System.out.println("# TIME - SUFFIX ARRAY SORT : " + (etime2 - etime1));
		System.out.println("# STIRNG LENGTH : " + sequence.length);
		System.out.println("# MEMORY : " + ((double) (suffix.length * 4) / (double) (1024 * 1024)));
	}

	public static int[] sort(char sequence[], int suffix[], int radix) {

		for (int r = radix - 1; r >= 0; r--) {
			// INITIALIZE
			int counts[] = new int[ALPHA];
			int nsuffix[] = new int[suffix.length];

			int seqlen = sequence.length;

			for (int i = 0; i < suffix.length; i++) {
				int sfxlen = seqlen - suffix[i];
				int index = 0;
				if (r < sfxlen) {
					char ch = sequence[suffix[i] + r];
					index = toindex(ch);
				}
				counts[index]++;
			}

			int accum = 0;
			for (int c = 0; c < counts.length; c++) {
				accum = accum + counts[c];
				counts[c] = accum - counts[c];
			}

			for (int i = 0; i < suffix.length; i++) {
				int sfxlen = seqlen - suffix[i];
				int index = 0;
				if (r < sfxlen) {
					char ch = sequence[suffix[i] + r];
					index = toindex(ch);
				}
				nsuffix[counts[index]++] = suffix[i];
			}

			suffix = nsuffix;
		}

		return suffix;
	}

	public static int[] initialize(int sfxcnt) {
		int suffix[] = new int[sfxcnt];
		for (int s = 0; s < suffix.length; s++) {
			suffix[s] = s;
		}
		return suffix;
	}

	public static int toindex(char ch) {
		return ch - 'a' + 1;
	}

	public static char charat(char txt[], int idx) {
		if (idx >= txt.length)
			return 0;
		return txt[idx];
	}

}
