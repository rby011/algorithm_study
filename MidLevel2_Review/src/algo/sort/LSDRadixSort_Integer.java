package algo.sort;

import java.util.Scanner;

/**
 * <PRE>
5
138273636
193884744
-999999999
10203022
-83828833
 * 
 * </PRE>
 */
public class LSDRadixSort_Integer {

	static int WORD_SIZE = 8, INT_BIT = 32, RADIX_SIZE = INT_BIT / WORD_SIZE;
	static int KEY_CNT = 0, POSSIBLE_KEY_COUNT = (1 << WORD_SIZE) + 1;

	public static void main(String args[]) {
		System.out.println(0xffffffff);
		System.out.println(0xfffffffe);
		System.out.println(0xfffffffd);

		System.out.println(0xffffffffL);
		System.out.println(0xfffffffeL);
		System.out.println(0xfffffffdL);
		
		System.exit(0);
		
		Scanner scan = new Scanner(System.in);
		KEY_CNT = scan.nextInt();
		int numbers[] = new int[KEY_CNT];
		for (int n = 0; n < KEY_CNT; n++)
			numbers[n] = scan.nextInt();
		scan.close();

		// KEY_CNT = 5;
		// int numbers[] = { 5, 4, 4, 3, 1 };
		// int numbers[] = { 0x80000002, 0x80000001, 0xffffffff, 0xfffffffe, 0xffffffff
		// };

		for (int r = 0; r < RADIX_SIZE; r++) {
			int counts[] = new int[POSSIBLE_KEY_COUNT];
			int sorted[] = new int[KEY_CNT];

			for (int k = 0; k < KEY_CNT; k++) {
				int rkey = (numbers[k] >> (r * WORD_SIZE)) & 0x000000ff;
				counts[rkey]++;
			}

			int accum = 0, tmp = 0;
			if (r != RADIX_SIZE - 1) {
				for (int c = 0; c < POSSIBLE_KEY_COUNT; c++) {
					tmp = counts[c];
					counts[c] = accum;
					accum = accum + tmp;
				}
			} else {
				for (int c = 0x80; c <= 0xff; c++) {
					tmp = counts[c];
					counts[c] = accum;
					accum = accum + tmp;
				}
				for (int c = 0x00; c <= 0x7f; c++) {
					tmp = counts[c];
					counts[c] = accum;
					accum = accum + tmp;
				}

				for (int c = 0; c < counts.length; c++) {
					if (counts[c] != 0) {
						System.out.println(c + ":" + counts[c]);
					}
				}

			}

			for (int k = 0; k < KEY_CNT; k++) {
				int rkey = (numbers[k] >> (r * WORD_SIZE)) & 0x000000ff;
				sorted[counts[rkey]++] = numbers[k];
			}

			numbers = sorted;

			// System.out.println("# R : " + r);
			// for (int k = 0; k < KEY_CNT; k++)
			// System.out.println(numbers[k]);
		}

		System.out.println("# FINAL");
		for (int k = 0; k < KEY_CNT; k++)
			System.out.println(numbers[k]);
	}// main
}
