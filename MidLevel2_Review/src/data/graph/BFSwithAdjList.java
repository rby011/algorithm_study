package data.graph;

import java.util.Scanner;

/**
 * <PRE>
5 14
1 2 1 5 2 1 2 3 2 4 2 5 3 2 3 4 4 2 4 3 4 5 5 1 5 2 5 4
 * </PRE>
 */
public class BFSwithAdjList {
	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(System.in);

		int N = scan.nextInt();
		int M = scan.nextInt();
		Graph graph = new Graph(N);

		for (int m = 0; m < M; m++) {
			int u = scan.nextInt();
			int v = scan.nextInt();
			graph.nodelist[u].addNode(new GNode(v));
		}
		scan.close();

		System.out.println("# NODES ADJACNET TO i ");
		for (int i = 1; i < graph.nodelist.length; i++) {
			GNode node = graph.nodelist[i].head;
			System.out.print(i + " : ");
			while (node != null) {
				System.out.print(node.node_id + " ");
				node = node.next;
			}
			System.out.println();
		}

		GNode snode = graph.nodelist[1].head;
		GNode enode = graph.nodelist[4].head.next;
		System.out.println("# TRAVERSE IN BFS STARTING WITH " + snode.node_id);
		graph.traverse(snode);

		System.out.println("# SHORTEST PATH DISTANCE (EDGE COUNT) FROM u TO v ");
		for (int v = 1; v < graph.nodelist.length; v++) {
			System.out.println(snode.node_id + " > " + v + " : " + graph.d[v]);
		}

		System.out.println("# ALL THE SHORTEST PATH FROM " + snode.node_id + " TO " + enode.node_id);
		for (int p = 1; p < graph.nodelist.length; p++) {
			System.out.println("[" + p + "] = " + (graph.pi[p] == null ? "null" : graph.pi[p].node_id));
		}
		graph.printpath(snode, enode);
	}

	static class Graph {
		GNodeList nodelist[] = null;
		int d[] = null;// DISTANE BETWEEN START NODE AND NODE V
		GNode pi[] = null;// PREDECESSOR OF V IN THE SHORTEST PATH TO V FROM STAR NODE

		Graph(int node_size) {
			this.nodelist = new GNodeList[node_size + 1];
			for (int n = 0; n <= node_size; n++)
				nodelist[n] = new GNodeList();

			this.d = new int[node_size + 1];
			for (int n = 0; n <= node_size; n++)
				d[n] = -1;

			this.pi = new GNode[node_size + 1];
		}

		public void traverse(GNode snode) throws Exception {
			Queue queue = new Queue(nodelist.length * 2);

			System.out.print(snode.node_id + " ");
			queue.enqueue(snode);
			d[snode.node_id] = 0;

			while (!queue.isempty()) {
				// NODE LIST ADJACENT TO THE 'node'
				GNode node = queue.dequeue();
				GNode adjnode = nodelist[node.node_id].getAdjacentNodes();
				while (adjnode != null) {
					if (d[adjnode.node_id] == -1) {
						d[adjnode.node_id] = d[node.node_id] + 1;
						pi[adjnode.node_id] = node;
						System.out.print(adjnode.node_id + " ");
						queue.enqueue(adjnode);
					}
					adjnode = adjnode.next;
				}
			}
			System.out.println();
		}

		public void printpath(GNode snode, GNode enode) {
			if (snode == enode) {
				System.out.print(snode.node_id + " ");
			} else if (enode == null) {
				System.out.print("no path");
			} else {
				printpath(snode, pi[enode.node_id]);
				System.out.print(enode.node_id +" ");
			}
		}
	}

	static class GNodeList {
		GNode head;

		public void addNode(GNode node) {
			if (head == null) {
				head = node;
			} else {
				node.next = head;
				head = node;
			}
		}

		public boolean isAdjacent(int node_id) {
			GNode node = head;
			while (node != null) {
				if (node.node_id == node_id)
					return true;
				node = node.next;
			}
			return false;
		}

		public GNode getAdjacentNodes() {
			return this.head;
		}
	}

	static class GNode {
		GNode next;
		int node_id;

		GNode(int node_id) {
			this.node_id = node_id;
		}
	}

	static class Queue {
		GNode nodes[] = null;
		int front = 0, rear = 0;

		Queue(int capacity) {
			this.nodes = new GNode[capacity];
		}

		public void enqueue(GNode node) throws Exception {
			if (isfull())
				throw new Exception("Overflow");
			nodes[rear++] = node;
		}

		public GNode dequeue() throws Exception {
			if (isempty())
				throw new Exception("Underflow");
			return nodes[front++];
		}

		public boolean isempty() {
			return rear == front;
		}

		public boolean isfull() {
			return rear == nodes.length - 1;
		}

	}
}
