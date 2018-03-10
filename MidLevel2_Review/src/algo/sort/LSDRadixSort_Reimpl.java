package algo.sort;

import java.util.Scanner;

/**
 * <PRE>
5 5
abcde
bdefd
abdef
dfged
gdszd
 * </PRE>
 */

public class LSDRadixSort_Reimpl {

	static final int ALPHA = 'z' - 'a' + 1;

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);

		int N = scan.nextInt(), M = scan.nextInt();
		char txt[][] = new char[N][M];
		for (int n = 0; n < N; n++) {
			txt[n] = scan.next().toCharArray();
		}

		// LSD RADIX SORT
		radix_sort(txt);

		// PRINT OUT SORTING RESULT
		printout(txt);

		scan.close();
	}

	public static void radix_sort(char txt[][]) {
		int R = txt[0].length, N = txt.length;

		for (int r = R - 1; r >= 0; r--) {
			int counts[] = new int[ALPHA];
			char stxt[][] = new char[N][R];

			for (int n = 0; n < N; n++)
				counts[toindex(charat(txt[n], r))]++;

			int accum = 0;
			for (int c = 0; c < ALPHA; c++) {
				accum = accum + counts[c];
				counts[c] = accum - counts[c];
			}

			for (int n = 0; n < N; n++) {
				int idx = counts[toindex(charat(txt[n], r))]++;
				stxt[idx] = txt[n];
			}

			txt = stxt;
		}
	}

	public static void printout(char txt[][]) {
		for (int n = 0; n < txt.length; n++)
			System.out.println(txt[n]);
	}

	public static char charat(char txt[], int idx) {
		if (idx >= txt.length)
			return 0;
		return txt[idx];
	}

	public static int toindex(char ch) {
		return ch - 'a';
	}
}
