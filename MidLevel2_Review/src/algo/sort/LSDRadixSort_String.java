package algo.sort;

import java.util.Scanner;

/**
 * <PRE>
 * 
7 3
aab
aac
abe
aba
baa
baf
zaa

5 3
ade
cdf
baa
zag
ab

1
10 4
adef
dasf
qwte
dgfg
pokv
fdja
bjkf
fisd
kdhg
uewf


2
5
138273636
193884744
999999999
10203022
83828833

2
5
138273636
193884744
-999999999
10203022
-83828833
 * 
 * </PRE>
 */
public class LSDRadixSort_String {
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);

		int KEY_CNT = scan.nextInt(), KEY_LEN = scan.nextInt();
		int POSSIBLE_KEY_CNT = 'z' - 'a' + 1;

		char txt[][] = new char[KEY_CNT][KEY_LEN];
		for (int c = 0; c < KEY_CNT; c++)
			txt[c] = scan.next().toCharArray();

		scan.close();

		for (int l = KEY_LEN - 1; l >= 0; l--) {
			char sorted[][] = new char[KEY_CNT][KEY_LEN];
			int counts[] = new int[POSSIBLE_KEY_CNT];

			for (int c = 0; c < KEY_CNT; c++) {
				counts[getIndex(txt[c][l])]++;
			}

			int accum = 0, tmp = 0;
			for (int p = 0; p < POSSIBLE_KEY_CNT; p++) {
				tmp = counts[p];
				counts[p] = accum;
				accum = accum + tmp;
			}

			for (int c = 0; c < KEY_CNT; c++) {
				int id = getIndex(txt[c][l]);
				int target_index = counts[id]++;
				sorted[target_index] = txt[c];
			}
			
			txt = sorted;
		}
		
		for(int c=0;c<KEY_CNT;c++)
			System.out.println(txt[c]);
		
		
	}

	public static int getIndex(char key) {
		return key - 'a';
	}
}
