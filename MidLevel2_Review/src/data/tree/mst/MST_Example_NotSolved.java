package data.tree.mst;

import java.util.Scanner;

/**
 * @ref https://www.acmicpc.net/problem/2406
 * 
 *      <PRE>
     DIFFICULT!!!
 *      </PRE>
 */
public class MST_Example_NotSolved {
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);

		int N = scan.nextInt(), M = scan.nextInt();

		// COLLECT EDGES TO BUILD GRAPH
		int eindex = 0;
		Edge edges[] = new Edge[M + N - 1];

		int dedges[][] = new int[M][2];
		for (int m = 0; m < M; m++) {
			dedges[m][0] = scan.nextInt();
			dedges[m][1] = scan.nextInt();
		}

		int NWMAP[][] = new int[N + 1][N + 1];
		// CONNECTION COST MAP
		for (int u = 1; u <= N; u++) {
			for (int v = 1; v <= N; v++) {
				NWMAP[u][v] = scan.nextInt();
			}
		}

		for (int i = 2; i <= N; i++) {
			Edge edge = new Edge(1, i, NWMAP[1][i]);
			edges[eindex++] = edge;
		}

		for (int m = 0; m < M; m++) {
			int u = dedges[m][0], v = dedges[m][1], weight = NWMAP[u][v];
			Edge edge = new Edge(u, v, weight);
			edges[eindex++] = edge;
		}

		// VALIDATE NEWTORK
		Validator validator = new Validator(edges, N);
		if (!validator.isStable()) {

		}

		scan.close();
	}

	static class Validator {
		Edge edges[] = null;
		int N = 0;

		public Validator(Edge edges[], int N) {
			this.edges = edges;
			this.N = N;
		}

		public boolean isStable() {
			boolean ret = isNodeStable();
			if (ret)
				ret = isEdgeStable();
			return ret;
		}

		private boolean isEdgeStable() {
			for (int rmv = 0; rmv < edges.length; rmv++) {
				Set set = new Set(N);
				for (int e = 0; e < edges.length; e++) {
					if (rmv != e) {
						// BUILD SET WITHOUT edges[rmv]
						int u = edges[e].u;
						int v = edges[e].v;
						set.union(u, v);
						// CHECK CONNECTION BETWEEN edges[rmv].u AND edges[rmv].u
						if (!set.isconnected(edges[rmv].u, edges[rmv].u))
							return false;
					}
				}
			}
			return true;
		}

		private boolean isNodeStable() {
			Graph graph = new Graph(edges.length);

			for (int rn = 1; rn <= N; rn++) {
				visited = new boolean[N + 1];
				if (rn == 1) {
					dfs(graph, rn, 2);
				} else {
					dfs(graph, rn, 1);
				}
				for (int v = 1; v <= N; v++) {
					if (!visited[v])
						return false;
				}
			}
			return true;
		}

		boolean visited[] = null;

		private void dfs(Graph graph, int rnid, int nid) {
			visited[nid] = true;
			Edge edge = graph.adjlist[nid].head;
			while (edge != null && edge.u != rnid && edge.v != rnid) {
				if (!visited[edge.v])
					dfs(graph, rnid, edge.v);
				edge = edge.next;
			}
		}
	}

	static class MinHeap {
		Edge tree[] = null;
		int size = 0, R = 1;

		// THE FIRST EDGE MUST '1', NOT USING '0' INDEX
		public MinHeap(Edge edges[]) {
			this.tree = edges;
			this.size = edges.length;
		}

		public Edge dequeue() throws Exception {
			if (this.isempty())
				throw new Exception("UNDERFLOW");

			Edge min = tree[R];
			size--;
			minHeapify(R);

			return min;
		}

		private void buildMinHeap() {
			int start = size / 2;
			for (int s = start; s >= R; s--) {
				minHeapify(s);
			}
		}

		private void minHeapify(int eind) {
			int lchild = eind * 2;
			int rchild = lchild + 1;

			boolean haslchild = lchild <= size;
			boolean hasrchild = rchild <= size;

			if (!haslchild)
				return;

			int smallerchild = lchild;
			if (hasrchild && tree[rchild].weight < tree[smallerchild].weight) {
				smallerchild = rchild;
			}

			if (tree[eind].weight <= tree[smallerchild].weight)
				return;

			Edge tmp = tree[eind];
			tree[eind] = tree[smallerchild];
			tree[smallerchild] = tmp;

			minHeapify(smallerchild);
		}

		private boolean isempty() {
			return this.size == 0;
		}
	}

	static class Set {
		int parent[] = null, size[] = null;
		int capacity = 0;

		public Set(int nodecnt) {
			this.capacity = nodecnt;
			// THE FIRST NODE-ID IS '1', NOT USING '0'
			this.parent = new int[this.capacity + 1];
			this.size = new int[this.capacity + 1];

			this.parent[0] = -1;

			for (int i = 1; i < this.capacity; i++) {
				this.parent[i] = i;
				this.size[i] = 1;
			}
		}

		public void buildSet(Graph graph) {
			for (int g = 0; g < graph.adjlist.length; g++) {
				Edge edge = graph.adjlist[g].head;
				while (edge != null) {
					int u = edge.u, v = edge.v;
					if (!this.isconnected(u, v))
						this.union(u, v);
					edge = edge.next;
				}
			}
		}

		public int find(int nid) {
			while (nid != parent[nid]) {
				parent[nid] = parent[parent[nid]];
				nid = parent[nid];
			}
			return nid;
		}

		public boolean isconnected(int nid1, int nid2) {
			return find(nid1) == find(nid2);
		}

		public void union(int nid1, int nid2) {
			int rid1 = find(nid1);
			int rid2 = find(nid2);

			if (rid1 == rid2)
				return;

			if (size[rid1] > size[rid2]) {// MERGE DIRECTION : rid1 <-- rid2
				parent[rid2] = rid1;
				size[rid1] = size[rid1] + size[rid2];
			} else {
				parent[rid1] = rid2;
				size[rid2] = size[rid1] + size[rid2];
			}
		}
	}

	// ADJACENT LIST
	static class Graph {
		int capacity;
		EdgeList[] adjlist = null;

		Graph(int edgecnt) {
			this.capacity = edgecnt;
			adjlist = new EdgeList[this.capacity];

			for (int i = 0; i < this.capacity; i++)
				adjlist[i] = new EdgeList();
		}

		public void addToGraph(Edge edge) {
			this.adjlist[edge.u].addToList(edge);
		}

	}

	static class EdgeList {
		Edge head;

		public void addToList(Edge edge) {
			if (head == null) {
				head = edge;
				return;
			}

			edge.next = head;
			head = edge;
		}
	}

	static class Edge {
		int weight;
		int u, v;// FROM u TO v
		Edge next;

		Edge(int u, int v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}
}
