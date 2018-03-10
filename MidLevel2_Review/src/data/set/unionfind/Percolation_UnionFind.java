package data.set.unionfind;

/**
 * 
 * @author Changsun Song
 * @ref http://algs4.tistory.com/23
 * 
 *      <PRE>
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
 *      </PRE>
 */
public class Percolation_UnionFind {
	static int M, N;
	static int MAP[][] = null;

	public static void main(String args[]) {
		generateTestData(3, 3);
		/**
		 * System.out.println("# MAP"); printArray(MAP); System.out.println();
		 */
		/**
		 * TEST FOR PERCOLATION TYPE #1
		 */
		long stime = System.currentTimeMillis();
		PercolationWithOneSet percol1 = new PercolationWithOneSet(M, N);
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				if (n % 2 == 1)
					if (MAP[n][m] == 1)
						percol1.open(n, m);
			}
		}

		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				if (n % 2 == 0)
					if (MAP[n][m] == 1)
						percol1.open(n, m);
			}
		}

		boolean flag = percol1.isopercolated();
		long etime = System.currentTimeMillis();

		System.out.println("# PERCULATED WITH ONE SET [ELAPED TIME] ");
		System.out.println(flag + "[" + (etime - stime) + "]");
		/**
		 * <PRE>
		 * System.out.println("# FULL ");
		 * for (int n = 0; n < N; n++) {
		 * 	for (int m = 0; m < M; m++) {
		 * 		System.out.print((percol1.isfull(n, m) ? "1" : "0") + " ");
		 * 	}
		 * 	System.out.println();
		 * }
		 * System.out.println();
		 * </PRE>
		 **/

		/**
		 * TEST FOR PERCOLATION TYPE #2
		 */
		stime = System.currentTimeMillis();
		PercolationWithTwoSet percol2 = new PercolationWithTwoSet(M, N);
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				if (n % 2 == 1)
					if (MAP[n][m] == 1)
						percol2.open(n, m);
			}
		}
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				if (n % 2 == 0)
					if (MAP[n][m] == 1)
						percol2.open(n, m);
			}
		}

		flag = percol2.ispercolate();
		etime = System.currentTimeMillis();

		System.out.println("# PERCULATED WITH TWO SETS [ELAPED TIME] ");
		System.out.println(flag + "[" + (etime - stime) + "]");
		printArray(percol2.virutalset.parent);

		/**
		 * <PRE>
		 * System.out.println("# FULL ");
		 * for (int n = 0; n < N; n++) {
		 * 	for (int m = 0; m < M; m++) {
		 * 		System.out.print((percol2.isfull(n, m) ? "1" : "0") + " ");
		 * 	}
		 * 	System.out.println();
		 * }
		 * System.out.println();
		 * </PRE>
		 **/
	}

	static class PercolationWithOneSet {
		Set set = null;
		int NRAW, NCOL;
		boolean open[], connectedToTop[], connectedToBottom[];
		boolean ispercolated = false;

		int delta[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		public PercolationWithOneSet(int rawcnt, int colcnt) {
			this.set = new Set(rawcnt * colcnt);
			this.connectedToTop = new boolean[rawcnt * colcnt];
			this.connectedToBottom = new boolean[rawcnt * colcnt];
			this.open = new boolean[rawcnt * colcnt];
			this.NRAW = rawcnt;
			this.NCOL = colcnt;
		}

		public void open(int rawind, int colind) {
			int ind = toindex(rawind, colind);
			if (open[ind])
				return;

			open[ind] = true;

			boolean conToTop = false, conToBottom = false;
			for (int d = 0; d < delta.length; d++) {
				int nraw = rawind + delta[d][0];
				int ncol = colind + delta[d][1];
				int nind = toindex(nraw, ncol);
				if (isvalid(nraw, ncol) && isopen(nraw, ncol)) {
					int rnind = set.find(nind);// MAJOR COMPUTATION
					if (connectedToTop[rnind])
						conToTop = true;
					if (connectedToBottom[rnind])
						conToBottom = true;

					set.union(ind, nind);
				}
			}

			int rind = set.find(ind);
			connectedToTop[rind] = conToTop || rawind == 0;
			connectedToBottom[rind] = conToBottom || rawind == NRAW - 1;

			this.ispercolated = connectedToTop[rind] && connectedToBottom[rind];
		}

		public boolean isopercolated() {
			return this.ispercolated;
		}

		public boolean isfull(int rawind, int colind) {
			int ind = toindex(rawind, colind);
			int rind = set.find(ind);
			return open[ind] && connectedToTop[rind];
		}

		public boolean isopen(int rawind, int colind) {
			return open[toindex(rawind, colind)];
		}

		private boolean isvalid(int rawind, int colind) {
			boolean invalid = rawind < 0 || colind < 0 || rawind >= NRAW || colind >= NCOL;
			return !invalid;
		}

		private int toindex(int rawind, int colind) {
			return rawind * NCOL + colind;
		}

	}

	static class PercolationWithTwoSet {
		Set virutalset = null, realset = null;
		int TOP, BOTTOM;
		int NRAW, NCOL;

		boolean open[][] = null;

		int delta[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		public PercolationWithTwoSet(int rawcnt, int colcnt) {
			virutalset = new Set(rawcnt * colcnt + 2);
			realset = new Set(rawcnt * colcnt + 2);// DUE TO BACKWASH PROBLEM, NOT CONNECTING TO BOTTOM

			open = new boolean[rawcnt][colcnt];
			NRAW = rawcnt;
			NCOL = colcnt;
			TOP = 0;
			BOTTOM = rawcnt * colcnt + 1;
		}

		public void open(int rawind, int colind) {
			if (isopen(rawind, colind))
				return;

			open[rawind][colind] = true;

			int ind = toIndex(rawind, colind);

			if (rawind == 0) {
				virutalset.union(TOP, ind);
				realset.union(ind, TOP);
			}

			if (rawind == NRAW - 1) {
				virutalset.union(ind, BOTTOM);
			}

			for (int d = 0; d < delta.length; d++) {
				int nrawind = rawind + delta[d][0];
				int ncolind = colind + delta[d][1];
				if (isvalid(nrawind, ncolind) && isopen(nrawind, ncolind)) {
					int nind = toIndex(nrawind, ncolind);
					virutalset.union(nind, ind);
					realset.union(nind, ind);
				}
			}
		}

		public boolean ispercolate() {
			return virutalset.isconnected(TOP, BOTTOM);
		}

		public boolean isfull(int rawind, int colind) {
			return realset.isconnected(TOP, toIndex(rawind, colind));
		}

		public boolean isopen(int rawind, int colind) {
			return open[rawind][colind];
		}

		private boolean isvalid(int rawind, int colind) {
			boolean invalid = (rawind < 0) || (rawind > NRAW - 1) || (colind < 0) || (colind > NCOL - 1);
			return !invalid;
		}

		private int toIndex(int rawind, int colind) {
			return 1 + (NCOL * rawind + colind);
		}
	}

	static class Set {
		int parent[] = null;
		int size[] = null;

		public Set(int capacity) {
			this.parent = new int[capacity];
			this.size = new int[capacity];

			for (int n = 0; n < capacity; n++) {
				parent[n] = n;
				size[n] = 1;
			}
		}

		public int find(int nid) {
			while (nid != parent[nid]) {
				parent[nid] = parent[parent[nid]];// PATH COMPRESSION
				nid = parent[nid];
			}
			return nid;
		}

		public void union(int nid1, int nid2) {
			int rid1 = find(nid1);
			int rid2 = find(nid2);

			if (rid1 == rid2)
				return;

			if (size[rid1] > size[rid2]) {
				parent[rid2] = rid1;
				size[rid1] = size[rid1] + size[rid2];
			} else {
				parent[rid1] = rid2;
				size[rid2] = size[rid1] + size[rid2];
			}
		}

		public boolean isconnected(int nid1, int nid2) {
			return find(nid1) == find(nid2);
		}

	}

	public static void printArray(int arr[]) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(i + "\t");
		}
		System.out.println();
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + "\t");
		}
		System.out.println();
	}

	public static void printArray(int arr[][]) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void printArray(boolean arr[][]) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				System.out.print((arr[i][j] == true ? "1" : "0") + " ");
			}
			System.out.println();
		}
	}

	public static void generateTestData(int n, int m) {
		N = n;
		M = m;
		MAP = new int[N][M];

		int ZERO_N = N - 2, ZERO_M = M - 2;
		for (int nn = 0; nn < N; nn++) {
			for (int mm = 0; mm < M; mm++) {
				MAP[nn][mm] = 1;
				if (nn == ZERO_N && mm < M - 1)
					MAP[nn][mm] = 0;
				if (mm == ZERO_M)
					MAP[nn][mm] = 0;
			}
		}
	}
}
