package data.set.unionfind;

import java.util.Scanner;

/**
 * @ref http://algs4.tistory.com/23
 * @author Changsun Song
 * 
 *         <PRE>
* 0 : Blocked , 1: Open
10 10
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 0 1
0 0 0 0 0 0 0 0 0 1 
1 1 1 1 1 1 1 1 0 1
30 30 
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1
 * 
 *         </PRE>
 */
public class Perolcation_DFS {
	static int N = 0, M = 0;
	static int MAP[][] = null;
	static boolean FULL[][] = null;

	public static void main(String args[]) {

		generateTestData(5000, 5000);
		// printMAP();
		long stime = System.currentTimeMillis();
		flow();
		boolean flag = ispercolate();
		long etime = System.currentTimeMillis();
		System.out.println(flag + " [" + (etime - stime) + "]");
		// printFULL();
	}

	public static boolean ispercolate() {
		for (int m = 0; m < M; m++) {
			if (FULL[N - 1][m])
				return true;
		}
		return false;
	}

	public static void flow() {
		for (int m = 0; m < M; m++)
			flow(0, m);
	}

	public static void flow(int n, int m) {
		if (n < 0 || n >= N)
			return;
		if (m < 0 || m >= M)
			return;
		if (MAP[n][m] != 1)
			return;

		if (FULL[n][m])
			return;

		FULL[n][m] = true;

		flow(n + 1, m);// DOWN
		flow(n - 1, m);// UP ---> IS REQUIRED??
		flow(n, m + 1);// RIGHT
		flow(n, m - 1);// LEFT
	}

	public static void printMAP() {
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				System.out.print(MAP[n][m] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void printFULL() {
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				System.out.print((FULL[n][m] == true ? "1" : "0") + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void generateTestData(int n, int m) {
		N = n;
		M = m;
		MAP = new int[N][M];
		FULL = new boolean[N][M];

		int ZERO_N = N - 2, ZERO_M = M - 2;
		for (int nn = 0; nn < N; nn++) {
			for (int mm = 0; mm < M; mm++) {
				MAP[nn][mm] = 1;
				if (ZERO_N == nn && mm < M - 1)
					MAP[nn][mm] = 0;
				if (mm == ZERO_M)
					MAP[nn][mm] = 0;
			}
		}
	}
}
