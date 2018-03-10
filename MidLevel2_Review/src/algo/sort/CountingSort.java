package algo.sort;

import java.util.Scanner;

/**
 * <PRE>
baffa
bbbabdcdefaazzz
 * </PRE>
 */

public class CountingSort {
	static int POSSIBLE_KEY_CNT = 'z' - 'a' + 1;
	static int KEY_CNT = 0;

	public static void main(String args[]) {
		countingsort(false);
	}

	public static void countingsort(boolean isascending) {
		Scanner scan = new Scanner(System.in);
		char keys[] = scan.next().toCharArray();
		scan.close();

		KEY_CNT = keys.length;
		int counts[] = new int[POSSIBLE_KEY_CNT];
		char sorted[] = new char[KEY_CNT];

		for (int k = 0; k < KEY_CNT; k++) {
			counts[getIndex(keys[k])]++;
		}

		int accum = 0, tmp = 0;
		if (isascending) {
			for (int p = 0; p < POSSIBLE_KEY_CNT; p++) {
				tmp = counts[p];
				counts[p] = accum;
				accum = accum + tmp;
			}
		} else {
			for (int p = POSSIBLE_KEY_CNT - 1; p >= 0; p--) {
				tmp = counts[p];
				counts[p] = accum;
				accum = accum + tmp;
			}
		}

		for (int k = 0; k < KEY_CNT; k++) {
			sorted[counts[getIndex(keys[k])]++] = keys[k];
		}

		for (int k = 0; k < KEY_CNT; k++) {
			System.out.print(sorted[k]);
		}
		System.out.println();
	}

	private static int getIndex(char key) {
		return key - 'a';
	}
}
