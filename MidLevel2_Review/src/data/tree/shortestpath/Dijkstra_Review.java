package data.tree.shortestpath;

import java.util.Scanner;

public class Dijkstra_Review {
	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(System.in);
		int N = scan.nextInt(), M = scan.nextInt(), S = scan.nextInt();

		Node nodes[] = new Node[N + 1];
		for (int n = 1; n <= N; n++) {
			nodes[n] = new Node(n);
			nodes[n].pi = -1;
			nodes[n].distance = Integer.MAX_VALUE;
		}

		Graph graph = new Graph(M + 1);
		for (int m = 0; m < M; m++) {
			int u = scan.nextInt(), v = scan.nextInt(), weight = scan.nextInt();
			graph.addEdgeToGraph(u, v, weight);
		}

		MinHeap minqueue = new MinHeap(nodes);

		nodes[S].distance = 0;
		while (!minqueue.isempty()) {
			Node node = minqueue.dequeue();
			int u = node.nid;
			Edge adjedge = graph.adjlist[u].head;
			while (adjedge != null) {
				int v = adjedge.v, weight = adjedge.weight;
				int vdist = minqueue.findNode(v).distance;
				if(minqueue.contain(v) && node.distance + weight < vdist) {
					minqueue.update(v, node.distance + weight , u);
				}
				adjedge = adjedge.next;
			}
		}
		scan.close();
	}

	public static class MinHeap {
		Node tree[] = null;
		int itable[] = null;// NODE ID TO TREE INDEX
		int size = 0, R = 1;

		public MinHeap(int capacity) {
			tree = new Node[capacity + 1];
			itable = new int[capacity + 1];
		}

		public MinHeap(Node node[]) {
			this.tree = node;// THE 'node' STARS FROM 1
			this.size = tree.length - 1;
			// INITIALIZE INDEX TABLE
			for (int i = 1; i < itable.length; i++)
				itable[tree[i].nid] = i;
			// BUILD HEAP
			buildheap();
		}

		public Node dequeue() throws Exception {
			Node min = tree[R];

			swap(R, size);

			itable[min.nid] = -1;
			tree[size] = null;
			size = size - 1;

			minheapify(R);

			return min;
		}

		public void update(int nodeid, int distance, int pi) {
			int tindex = itable[nodeid];
			tree[tindex].distance = distance;
			tree[tindex].pi = pi;

			// ONLY ONE OF TWO OPERATION WILL BE EXECUTED
			liftup(tindex);
			minheapify(tindex);
		}

		public boolean contain(int nodeid) {
			return itable[nodeid] != -1;
		}

		public boolean isempty() {
			return size == 0;
		}

		public Node findNode(int nid) {
			if (!contain(nid))
				return null;
			return tree[itable[nid]];
		}

		private void buildheap() {
			int start = size / 2;
			for (int s = start; s >= R; s--)
				minheapify(s);
		}

		private void liftup(int treeindex) {
			Node node = tree[treeindex];
			while (treeindex > R && node.distance < tree[treeindex / 2].distance)
				swap(treeindex, treeindex / 2);
		}

		private void minheapify(int treeindex) {
			int lchild = treeindex * 2;
			int rchild = lchild + 1;
			boolean haslchild = lchild <= size;
			boolean hasrchild = rchild <= size;

			if (!haslchild)
				return;

			int smchild = lchild;
			if (hasrchild && tree[smchild].distance > tree[rchild].distance)
				smchild = rchild;

			if (tree[treeindex].distance <= tree[smchild].distance)
				return;

			swap(treeindex, smchild);

			minheapify(smchild);
		}

		private void swap(int tindex1, int tindex2) {
			int itmp = itable[tree[tindex1].nid];
			itable[tree[tindex1].nid] = itable[tree[tindex2].nid];
			itable[tree[tindex2].nid] = itmp;

			Node ntmp = tree[tindex1];
			tree[tindex1] = tree[tindex2];
			tree[tindex2] = ntmp;
		}
	}

	public static class Node {
		int nid;// FROM 1 TO N
		int distance, pi;

		public Node(int nodeid) {
			this.nid = nodeid;
		}
	}

	// ADJACENT WEIGHTED EDGE GRAPH
	// THE NODE ID STARTS FROM 1, NOT USING 0
	// INDEX OF 'adjlist' IS THE NODE ID
	public static class Graph {
		EdgeList adjlist[] = null;

		public Graph(int nodecnt) {
			this.adjlist = new EdgeList[nodecnt];
			for (int a = 0; a < nodecnt; a++) {
				adjlist[a] = new EdgeList();
			}
		}

		// EDGE FROM u NODE TO v NODE WITH weight
		public void addEdgeToGraph(int u, int v, int weight) {
			adjlist[u].addEdgeToList(new Edge(u, v, weight));
		}
	}

	public static class EdgeList {
		Edge head;

		public void addEdgeToList(Edge edge) {
			if (head == null) {
				this.head = edge;
				return;
			}
			edge.next = head;
			head = edge;
		}
	}

	public static class Edge {
		int u, v, weight;
		Edge next;

		public Edge(int u, int v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}
}
