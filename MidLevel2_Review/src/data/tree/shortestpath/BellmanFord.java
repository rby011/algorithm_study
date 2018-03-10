package data.tree.shortestpath;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * <PRE>
6 9 1
1 2 1
2 3 2
2 1 3
2 4 4
2 5 2
3 5 3
3 6 1
5 6 2
4 6 5
 * </PRE>
 */
public class BellmanFord {

	static final long LARGE = Integer.MAX_VALUE;
	static final String file = "G10000EWG.txt";
	static final String path = "C:\\Users\\Changsun Song\\Desktop\\알고리즘자격준비\\연습문제\\MidLevel2_Review\\data";

	public static void main(String args[]) throws Exception {
		FileInputStream fin = new FileInputStream(new File(path + "\\" + file));
		Scanner scan = new Scanner(fin);
		// Scanner scan = new Scanner(System.in);

		int N = scan.nextInt(), M = scan.nextInt(), S = scan.nextInt();
		// NODE ID STARS FROM 0 AND ENDS WITH N
		long de[] = new long[N + 1];
		int pi[] = new int[N + 1];

		// NO NEEDS TO KEEP IDS OF EDGES
		Edge edges[] = new Edge[M];
		for (int m = 0; m < M; m++) {
			int u = scan.nextInt(), v = scan.nextInt(), w = scan.nextInt();
			edges[m] = new Edge(u, v, w);
		}

		// INITLIZE DISTANCE ESITMATOR AND PREDECESSOR
		for (int n = 1; n <= N; n++) {
			de[n] = LARGE;
			pi[n] = -1;
		}
		de[S] = 0; // START NODE IS S

		long stime = System.currentTimeMillis();
		// BELLMAN-FORD : TIME COMPLEXITY IS O(NM)
		for (int n = 1; n < N; n++) {
			for (int e = 0; e < edges.length; e++) {
				int u = edges[e].u, v = edges[e].v;
				int w = edges[e].weight;
				// RELAXATION
				if (de[v] > de[u] + w) {
					de[v] = de[u] + w;
					pi[v] = u;
				}
			}
		}
		long etime = System.currentTimeMillis();
		System.out.println("TIME : " + (etime - stime));
		// SHORTEST DISTANCE FROM NODE 1
		for (int n = 1; n <= N; n++)
			System.out.println(n + " : " + de[n]);
		scan.close();
	}

	static class Edge {
		int u, v, weight;

		public Edge(int u, int v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}
}
