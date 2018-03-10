package data.tree.shortestpath;

import java.util.Scanner;

/**
 * <PRE>
4
0 2 0 5
0 0 1 2
0 0 0 3
0 0 0 0

9
0	4	0	0	0	0	0	8	0
4	0	8	0	0	0	0	11	0
0	8	0	7	0	4	0	0	2
0	0	7	0	9	14	0	0	0
0	0	0	9	0	10	0	0	0
0	0	4	14	10	0	2	0	0
0	0	0	0	0	2	0	1	6
8	11	0	0	0	0	1	0	7
0	0	2	0	0	0	6	7	0
 * </PRE>
 */
public class FloydWashall {
	static int dp[][][] = null;
	static int map[][] = null;

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		int N = scan.nextInt();
		map = new int[N][N];
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < N; m++) {
				int weight = scan.nextInt();
				if (weight == 0 && n != m)
					map[n][m] = 10000;
				else
					map[n][m] = weight;
			}
		}
		scan.close();

		/**
		 * USING RECURSION
		 */
		long stime = System.currentTimeMillis();
		dp = new int[N][N][N];
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < N; m++) {
				shortpath_recursion(n, m, N - 1);
			}
		}
		long etime = System.currentTimeMillis();
		System.out.println("# RECURSION : " + (etime - stime));
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < N; m++) {
				System.out.println(n + " -> " + m + " : " + dp[n][m][N - 1]);
			}
		}

		/**
		 * USING LOOP
		 */
		stime = System.currentTimeMillis();
		dp = new int[N][N][N];
		shortpath_topdown(N);
		etime = System.currentTimeMillis();
		System.out.println("# TOP DOWN : " + (etime - stime));
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < N; m++) {
				System.out.println(n + " -> " + m + " : " + dp[n][m][N - 1]);
			}
		}
	}

	public static void shortpath_topdown(int N) {
		for (int k = 0; k < N; k++) {
			for (int n = 0; n < N; n++) {
				for (int m = 0; m < N; m++) {
					if (k == 0) {
						dp[n][m][k] = map[n][m];
						continue;
					}
					if (n == m) {
						dp[n][m][k] = 0;
						continue;
					}
					int sp1 = dp[n][k][k - 1] + dp[k][m][k - 1];
					int sp2 = dp[n][m][k - 1];
					if (sp1 < sp2) {
						dp[n][m][k] = sp1;
					} else {
						dp[n][m][k] = sp2;
					}
				}
			}
		}
	}

	public static int shortpath_recursion(int i, int j, int k) {
		if (i == j)
			return 0;

		if (k == 0) {
			dp[i][j][k] = map[i][j];
			return dp[i][j][k];
		}

		if (dp[i][j][k] != 0)
			return dp[i][j][k];

		int sp1 = dp[i][j][k - 1] = shortpath_recursion(i, j, k - 1);
		int sp2 = (dp[i][k][k - 1] = shortpath_recursion(i, k, k - 1))
				+ (dp[k][j][k - 1] = shortpath_recursion(k, j, k - 1));

		if (sp1 < sp2) {
			dp[i][j][k] = sp1;
			return sp1;
		} else {
			dp[i][j][k] = sp2;
			return sp2;
		}
	}
}
