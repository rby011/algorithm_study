package data.tree.mst;

import java.util.Scanner;

/**
 * <PRE>
5 7 
0	3	0	5	4
3	0	6	1	0
0	6	0	7	0
5	1	7	0	8
4	0	0	8	0

9 14
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
public class Prims {
	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(System.in);

		int N = scan.nextInt();
		int M = scan.nextInt();

		// BUILD ADJANCET WEIGHTED EDGE GRAPH
		Graph graph = new Graph(N + 1);
		for (int u = 1; u <= N; u++) {
			for (int v = 1; v <= N; v++) {
				int weight = scan.nextInt();
				if (weight != 0)
					graph.addEdge(u, v, weight);
			}
		}

		// BUILD NODE LIST
		int snode = 1;// START NODE
		Node nodes[] = new Node[N + 1];
		// Edge edge = graph.edgelist[snode].head;
		// while (edge != null) {
		// int key = edge.weight;
		// int v = edge.v;
		// nodes[v] = new Node(v, key);
		// edge = edge.next;
		// }
		for (int n = 1; n <= N; n++) {
			nodes[n] = new Node(n, Integer.MAX_VALUE);
			nodes[n].id = n;
			nodes[n].key = Integer.MAX_VALUE;
		}
		nodes[snode].key = 0;

		// BUILD MST - PRIMS
		int pi[] = new int[N + 1];
		MinHeap minqueue = new MinHeap(nodes);
		while (!minqueue.isempty()) {
			Node node = minqueue.dequeue();

			System.out.println(node.pi + "," + node.id);

			Edge ehead = graph.edgelist[node.id].head;
			while (ehead != null) {
				int weight = ehead.weight;
				int key = minqueue.findKey(ehead.v);
				if (minqueue.contain(ehead.v) && weight < key) {
					minqueue.updatekey(ehead.v, weight, node.id);
				}
				ehead = ehead.next;
			}
		}
		scan.close();
	}

	static class MinHeap {
		Node ntree[] = null;
		int itable[] = null;

		int size = 0, R = 1;

		public MinHeap(Node nodes[]) {
			this.ntree = nodes;
			this.size = nodes.length - 1;
			this.itable = new int[nodes.length];

			for (int n = 1; n <= nodes.length - 1; n++) {
				itable[ntree[n].id] = n;
			}

			buildHeap();
		}

		public Node dequeue() throws Exception {
			if (isempty())
				throw new Exception("UNDERFLOW");

			Node min = ntree[R];

			itable[ntree[size].id] = R;
			itable[ntree[R].id] = -1;

			ntree[R] = ntree[size];
			size = size - 1;
			minHeapify(R);

			return min;
		}

		public boolean contain(int nid) {
			return itable[nid] != -1;
		}

		public int findKey(int nid) {
			if (contain(nid))
				return ntree[itable[nid]].key;
			return -1;
		}

		public void updatekey(int nid, int key, int pi) {
			int index = itable[nid];
			ntree[index].key = key;
			ntree[index].pi = pi;

			int iparent = index / 2, ilchild = index * 2, irchild = ilchild + 1;
			boolean haslchild = ilchild <= size, hasrchild = irchild <= size;

			if (index > R && ntree[index].key < ntree[iparent].key) {
				// LIFT UP
				while (index > R && ntree[index].key < ntree[index / 2].key) {
					int itmp = itable[ntree[index].id];
					itable[ntree[index].id] = itable[ntree[index / 2].id];
					itable[ntree[index / 2].id] = itmp;

					Node tmp = ntree[index];
					ntree[index] = ntree[index / 2];
					ntree[index / 2] = tmp;

					index = index / 2;
				}
			} else if ((haslchild && ntree[index].key > ntree[ilchild].key)
					|| (hasrchild && ntree[index].key > ntree[irchild].key)) {
				// MIN HEAPIFY
				minHeapify(index);
			}
		}

		private void buildHeap() {
			int start = size / 2;
			for (int s = start; s >= R; s--)
				minHeapify(s);
		}

		private void minHeapify(int index) {
			int lchild = index * 2;
			int rchild = lchild + 1;

			boolean haslchild = lchild <= size;
			boolean hasrchild = rchild <= size;

			if (!haslchild)
				return;

			int smallerchild = lchild;
			if (hasrchild && ntree[rchild].key < ntree[smallerchild].key) {
				smallerchild = rchild;
			}

			if (ntree[index].key <= ntree[smallerchild].key)
				return;

			int itmp = itable[ntree[index].id];
			itable[ntree[index].id] = itable[ntree[smallerchild].id];
			itable[ntree[smallerchild].id] = itmp;

			Node tmp = ntree[index];
			ntree[index] = ntree[smallerchild];
			ntree[smallerchild] = tmp;

			minHeapify(smallerchild);
		}

		private boolean isempty() {
			return size == 0;
		}

	}

	static class Graph {
		EdgeList edgelist[] = null;

		public Graph(int edgecnt) {
			edgelist = new EdgeList[edgecnt];
			for (int n = 0; n < edgecnt; n++) {
				edgelist[n] = new EdgeList();
			}
		}

		public void addEdge(int u, int v, int weight) {
			edgelist[u].addEdgToList(new Edge(u, v, weight));
		}
	}

	static class EdgeList {
		Edge head = null;

		public void addEdgToList(Edge edge) {
			if (head == null) {
				this.head = edge;
				return;
			}
			edge.next = head;
			head = edge;
		}
	}

	static class Edge {
		int weight;
		int u, v;
		Edge next;

		public Edge(int u, int v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}

	static class Node {
		int id;
		int key;
		int pi;

		public Node(int id, int key) {
			this.id = id;
			this.key = key;
		}
	}
}
