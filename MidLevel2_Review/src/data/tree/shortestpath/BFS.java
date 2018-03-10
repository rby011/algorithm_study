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
public class BFS {

	static final String file = "G10000EWG.txt";
	static final String path = "C:\\Users\\Changsun Song\\Desktop\\알고리즘자격준비\\연습문제\\MidLevel2_Review\\data";

	public static void main(String args[]) throws Exception {
		FileInputStream fin = new FileInputStream(new File(path + "\\" + file));
		Scanner scan = new Scanner(fin);
		// Scanner scan = new Scanner(System.in);

		int N = scan.nextInt(), M = scan.nextInt(), S = scan.nextInt();

		// BUILD GRAPH
		Graph graph = new Graph(N);
		for (int m = 0; m < M; m++) {
			int u = scan.nextInt(), v = scan.nextInt(), weight = scan.nextInt();
			graph.addEdgeToGraph(u, v, weight);
		}

		// INITIALIZE
		long de[] = new long[N + 1];
		int pi[] = new int[N + 1];
		Queue queue = new Queue(M * 2);
		// FOR START NODE
		queue.enqueue(S);
		for (int n = 2; n <= N; n++) {
			de[n] = Integer.MAX_VALUE;
			pi[n] = -1;
		}

		// BFS - SHORTEST PATH FINDING
		long stime = System.currentTimeMillis();
		boolean visited[] = new boolean[N + 1];
		de[S] = 0;
		queue.enqueue(S);

		while (!queue.isempty()) {
			int nodeid = queue.dequeue();
			visited[nodeid] = true;

			Edge edge = graph.adjlist[nodeid].head;
			while (edge != null) {
				int u = nodeid, v = edge.v, w = edge.weight;
				if (!visited[v] && de[v] > de[u] + w) {
					de[v] = de[u] + w;
					pi[v] = u;
					if (!queue.contain(v)) {
						queue.enqueue(v);
					}
				}
				edge = edge.next;
			}
		}
		long etime = System.currentTimeMillis();
		System.out.println("TIME : " + (etime - stime));

		// SHORTEST DISTANCE FROM NODE 1
		for (int n = 1; n <= N; n++)
			System.out.println(n + " : " + de[n]);

		scan.close();
	}

	public static class Queue {
		int nodes[] = null;
		int capacity, head, tail;
		boolean contains[] = null;

		public Queue(int nodecnt) {
			nodes = new int[nodecnt + 1];
			contains = new boolean[nodecnt + 1];
			this.capacity = nodecnt + 1;
			head = tail = 0;
		}

		public void enqueue(int nodeid) throws Exception {
			if (isfull())
				throw new Exception("OVERFLOW");
			contains[nodeid] = true;
			nodes[++tail] = nodeid;
		}

		public int dequeue() throws Exception {
			if (isempty())
				throw new Exception("UNDERFLOW");
			contains[head + 1] = false;
			return nodes[++head];
		}

		public boolean contain(int nid) {
			return contains[nid];
		}

		public boolean isempty() {
			return head == tail;
		}

		public boolean isfull() {
			return tail == capacity - 1;
		}
	}

	static class Graph {
		EdgeList adjlist[] = null;

		public Graph(int nodecnt) {
			this.adjlist = new EdgeList[nodecnt + 1];
			for (int n = 1; n <= nodecnt; n++)
				adjlist[n] = new EdgeList();
		}

		public void addEdgeToGraph(int u, int v, int weight) {
			Edge edge = new Edge(u, v, weight);
			adjlist[u].addEdgeToList(edge);
		}
	}

	static class EdgeList {
		Edge head = null;

		public void addEdgeToList(Edge edge) {
			if (head == null) {
				this.head = edge;
				return;
			}
			edge.next = head;
			head = edge;
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
