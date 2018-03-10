package algo.sort;

import java.util.Scanner;

/**
 * <PRE>
3
abc
ab
c

4
 bcc
ddef
acc
ace

6
d
cef
ccdd
acfedd
aac
cdef
 * </PRE>
 */
public class MSDRadixSort_String {

	static final int ALPHA_CNT = 'z' - 'a' + 2;// 1 FOR NIL CHARACTER
	static int N = 0;

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		N = scan.nextInt();

		char txt[][] = new char[N][];
		char stxt[][] = new char[N][];

		for (int n = 0; n < N; n++)
			txt[n] = scan.next().toCharArray();
		scan.close();

		sort(txt, stxt, 0, N - 1, 0);

		for (int n = 0; n < N; n++)
			System.out.println(txt[n]);
	}

	public static void sort(char txt[][], char stxt[][], int start, int end, int r) {
		String indent = "";
		for (int k = 0; k < r; k++)
			indent = indent + "\t";

		int counts[] = new int[ALPHA_CNT + 1];// 1 FOR LAST ACCUM

		for (int n = start; n <= end; n++) {
			char ch = charat(txt[n], r);
			int index = getIndex(ch);
			counts[index]++;
		}

		int accum = 0, tmp = 0;
		for (int c = 0; c < ALPHA_CNT + 1; c++) {
			tmp = counts[c];
			counts[c] = accum;
			accum = accum + tmp;
		}

		for (int n = start; n <= end; n++) {
			char ch = charat(txt[n], r);
			int index = getIndex(ch);
			stxt[counts[index]++] = txt[n];
		}

		for (int n = start; n <= end; n++)
			txt[n] = stxt[n - start];

		for (int c = 0; c < ALPHA_CNT; c++) {
			int s = (c == 0 ? start : start + counts[c - 1]);
			int e = start + counts[c] - 1;
			if (e > s) {
				System.out.println(start + ":" + end + "=" + s + ">" + e);
				sort(txt, stxt, s, e, r + 1);
			}
		}
	}

	private static char charat(char[] seq, int index) {
		if (index < seq.length)
			return seq[index];
		return 0;
	}

	private static int getIndex(char ch) {
		if (ch == 0)
			return 0;
		return ch - 'a' + 1;
	}
}
