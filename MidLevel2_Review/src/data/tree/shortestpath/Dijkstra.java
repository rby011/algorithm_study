package data.tree.shortestpath;

import java.util.Scanner;

/**
 * <PRE>
3 3 1
0 2 3
0 0 2
0 0 0

5 7 1
0	3	0	5	4
3	0	6	1	0
0	6	0	7	0
5	1	7	0	8
4	0	0	8	0

9 14 1
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
public class Dijkstra {
	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(System.in);

		int N = scan.nextInt(), E = scan.nextInt(), S = scan.nextInt();

		// BUILD WEIGHTED EDGE GRAPH
		Graph graph = new Graph(N + 1);
		for (int u = 1; u <= N; u++) {
			for (int v = 1; v <= N; v++) {
				int weight = scan.nextInt();
				if (/* v > u && */ weight != 0)// NO NEED TO KEEP ALL EDGES, THIS IS UNDIRECTED GRAPH
					graph.addEdgeToGraph(u, v, weight);
			}
		}

		for (int n = 1; n <= graph.elist.length - 1; n++) {
			Edge edge = graph.elist[n].head;
			while (edge != null) {
				System.out.print(edge.u + "-" + edge.v + ":" + edge.weight + "\t");
				edge = edge.next;
			}
			System.out.println();
		}

		// MINIMUM SPANNING TREE - dijkstra
		// 1) INITIALIZE NODE
		Node nodes[] = new Node[N + 1];
		for (int n = 1; n <= N; n++) {
			nodes[n] = new Node(n);
			nodes[n].id = n;
			nodes[n].distance = Integer.MAX_VALUE;
			nodes[n].pi = -1;
		}
		nodes[S].distance = 0;

		// 2) BUILD MST
		MinHeap minqueue = new MinHeap(nodes);
		while (!minqueue.isempty()) {

			// for (int q = 1; q <= minqueue.size; q++)
			// System.out.println("[" + q + "]" + minqueue.tree[q].pi + " - " +
			// minqueue.tree[q].id + ", "
			// + minqueue.tree[q].distance);
			// System.out.println();

			Node node = minqueue.dequeue();

			System.out.println(node.pi + " - " + node.id + ":" + node.distance);

			Edge adjedge = graph.elist[node.id].head;
			while (adjedge != null) {
				int vnid = adjedge.v;
				if (minqueue.contains(vnid)) {
					int vdistance = minqueue.findDistance(vnid);// vdistance = nodes[vnid] IS CORRECT?
					if (vdistance > adjedge.weight + node.distance) {
						minqueue.updateNode(vnid, adjedge.weight + node.distance, node.id);
					}
				}
				adjedge = adjedge.next;
			}
		}

		scan.close();
	}

	static class MinHeap {
		Node tree[];
		int itable[];
		int size = 0, R = 1;

		public MinHeap(Node nodes[]) {
			tree = nodes;
			size = nodes.length - 1;
			itable = new int[nodes.length];
			// INITIALIZE INDEX TABLE
			for (int t = 1; t <= size; t++) {
				itable[tree[t].id] = t;
			}
			// BUILD HEAP
			buildHeap();
		}

		public Node dequeue() throws Exception {
			if (isempty())
				throw new Exception("UNDERFLOW");

			Node min = tree[R];

			itable[tree[R].id] = -1;
			itable[tree[size].id] = 1;
			tree[R] = tree[size];

			size = size - 1;

			minHeapify(R);

			return min;
		}

		public int findDistance(int nid) {
			return tree[itable[nid]].distance;
		}

		public void updateNode(int nid, int distance, int pi) {
			// UPDATE DISTANCE VALUE OF THE 'nid'
			int tid = itable[nid];
			tree[tid].distance = distance;
			tree[tid].pi = pi;

			// HEAP RECONSTRUCTION
			if (!liftup(tid))
				minHeapify(tid);

			// int iparent = tid / 2, ilchild = tid * 2, irchild = ilchild + 1;
			// boolean haslchild = ilchild <= size, hasrchild = irchild <= size;
			//
			// if (nid > R && tree[tid].distance < tree[iparent].distance) {
			// liftup(tid);
			// } else {
			// minHeapify(tid);
			// }

		}

		public boolean contains(int nid) {
			return itable[nid] != -1;
		}

		public boolean isempty() {
			return size == 0;
		}

		private void buildHeap() {
			int start = size / 2;
			for (int s = start; s >= R; s--) {
				minHeapify(s);
			}
		}

		private boolean liftup(int tid) {
			boolean flag = false;
			if (tid == R || size == 1)
				return flag;

			while (tid > R && tree[tid].distance < tree[tid / 2].distance) {
				swap(tid, tid / 2);
				tid = tid / 2;
				flag = true;
			}

			return flag;
		}

		private boolean minHeapify(int tid) {
			int lchild = tid * 2, rchild = lchild + 1;
			boolean haslchild = lchild <= size, hasrchild = rchild <= size;

			if (!haslchild)
				return false;

			int smchild = lchild;
			if (hasrchild && tree[rchild].distance < tree[lchild].distance) {
				smchild = rchild;
			}

			if (tree[tid].distance <= tree[smchild].distance)
				return false;

			swap(tid, smchild);

			minHeapify(smchild);

			return true;
		}

		private void swap(int tid1, int tid2) {
			// INDEX TABLE SWAP
			int itmp = itable[tree[tid1].id];
			itable[tree[tid1].id] = itable[tree[tid2].id];
			itable[tree[tid2].id] = itmp;

			// HEAP TREE SWAP
			Node ntmp = tree[tid1];
			tree[tid1] = tree[tid2];
			tree[tid2] = ntmp;
		}
	}

	// ADJACNET NODE GRAPH EXPRESSIOn
	static class Graph {
		EdgeList elist[] = null;// INDEX MEANS NODE ID, SO 0 INDEX IS NOT USED

		public Graph(int nodecnt) {
			elist = new EdgeList[nodecnt];
			for (int e = 0; e < nodecnt; e++) {
				elist[e] = new EdgeList();
			}
		}

		public void addEdgeToGraph(int u, int v, int weight) {
			elist[u].addEdgeToList(new Edge(u, v, weight));
		}
	}

	static class EdgeList {
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

	static class Node {
		int distance, pi;
		int id;

		public Node(int id) {
			this.id = id;
		}
	}

	static class Edge {
		int u, v, weight;
		Edge next;

		public Edge(int u, int v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}
}
