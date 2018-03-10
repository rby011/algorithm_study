package data.graph;

import java.util.Scanner;

/**
 * Queue, Graph, Topological Sort
 * 
 * <PRE>
7 8
plan tom
requirement-gathering jhon
coding tom
design jenny
test jennifer
workshop jenny
team-build lora
plan team-build
plan workshop
plan requirement-gathering
team-build design
requirement-gathering workshop
workshop design
design coding
coding test
 * </PRE>
 **/
public class TopologicalSort {
	public static void main(String args[]) throws Exception {
		Scanner scan = new Scanner(System.in);
		
		int V = scan.nextInt(), E = scan.nextInt();

		Hashtable keytable = new Hashtable(V);
		Hashtable idtable = new Hashtable(V);

		Graph graph = new Graph(V);
		for (int v = 0; v < V; v++) {
			int id = v;
			char key[] = scan.next().toCharArray(), value[] = scan.next().toCharArray();
			Node node = new Node(id, key, value);
			keytable.put(key, node);
			idtable.put(id, node);
		}

		for (int e = 0; e < E; e++) {
			char[] key1 = scan.next().toCharArray();
			char[] key2 = scan.next().toCharArray();

			int u = keytable.get(key1).id;
			int v = keytable.get(key2).id;

			graph.indgr[v]++;
			graph.addVertext(u, v);
		}

		int sid = 0, t = 0;
		int topology[] = graph.sort_bfs();

		System.out.println("# TASK ORDER : FROM " + sid + " NODE");
		for (t = 0; t < topology.length; t++) {
			Node node = idtable.get(topology[t]);
			System.out
					.println((t + 1) + " : " + node.id + " , " + new String(node.key) + " , " + new String(node.value));
		}
		System.out.println();
		
		System.out.println("# TASK ORDER");
		t = 0;
		VertexList list = graph.sort_dfs();
		Vertex vtx = list.head;
		while (vtx != null) {
			Node node = idtable.get(vtx.id);
			System.out.println((++t) + " : " + node.id + " , " + new String(node.key) + " , " + new String(node.value));
			vtx = vtx.next;
		}

		scan.close();
	}

	static class Graph {
		VertexList adjlist[] = null;
		int indgr[] = null;

		public Graph(int vtxCnt) {
			this.adjlist = new VertexList[vtxCnt];
			indgr = new int[vtxCnt];

			for (int v = 0; v < vtxCnt; v++)
				adjlist[v] = new VertexList();
		}

		public void addVertext(int uid, int vid) {
			adjlist[uid].addVertext(new Vertex(vid));
		}

		boolean visited[] = null;

		public VertexList sort_dfs() throws Exception {
			VertexList list = new VertexList();
			visited = new boolean[this.adjlist.length];
			for (int v = 0; v < this.adjlist.length; v++) {
				if (!visited[v])
					traverse(v, list);
			}
			return list;
		}

		private void traverse(int v, VertexList list) {
			visited[v] = true;

			Vertex vertex = this.adjlist[v].head;
			while (vertex != null) {
				if (!visited[vertex.id]) {
					traverse(vertex.id, list);
				}
				vertex = vertex.next;
			}
			list.addVertext(new Vertex(v));
		}

		public int[] sort_bfs() throws Exception {
			int vcnt = this.adjlist.length;
			int sorted[] = new int[vcnt];
			int idx = 0;

			Queue queue = new Queue(vcnt);

			for (int i = 0; i < indgr.length; i++) {
				if (indgr[i] == 0) {
					queue.enqueue(i);
					sorted[idx++] = i;
				}
			}

			while (!queue.isempty()) {
				int uid = queue.dequeue();
				Vertex vtx = adjlist[uid].head;
				while (vtx != null) {
					indgr[vtx.id]--;
					if (indgr[vtx.id] == 0) {
						queue.enqueue(vtx.id);
						sorted[idx++] = vtx.id;
					}
					vtx = vtx.next;
				}
			}

			return sorted;
		}
	}

	static class VertexList {
		Vertex head;

		public void addVertext(Vertex vtx) {
			if (head == null) {
				head = vtx;
			} else {
				vtx.next = head;
				head = vtx;
			}
		}
	}

	static class Vertex {
		int id;
		Vertex next;

		Vertex(int id) {
			this.id = id;
		}
	}

	static class Hashtable {
		NodeList[] table = null;
		int capacity;

		public Hashtable(int capacity) {
			this.table = new NodeList[capacity];

			for (int c = 0; c < capacity; c++) {
				table[c] = new NodeList();
			}

			this.capacity = capacity;
		}

		public void put(char key[], Node node) {
			int index = (node.hashcodewithkey() & 0x7fffffff) % capacity;
			// NO UPDATE WHEN A NEW NODE WITH REGISTERED KEY IS COMMING,
			// BUT JUST ADD TO FIRST
			table[index].addNode(node);
		}

		public void put(int id, Node node) {
			int index = (node.hashcodewithid() & 0x7fffffff) % capacity;
			table[index].addNode(node);
		}

		public Node get(char key[]) {
			int index = (this.hashcodewithkey(key) & 0x7fffffff) % this.capacity;
			Node node = table[index].head;

			while (node != null) {
				if (table[index].isequal(key, node.key))
					return node;
				node = node.next;
			}

			return node;
		}

		public Node get(int id) {
			int index = (this.hashcodewithid(id) & 0x7fffffff) % this.capacity;
			Node node = table[index].head;

			while (node != null) {
				if (node.id == id)
					return node;
				node = node.next;
			}

			return node;
		}

		private int hashcodewithkey(char key[]) {
			int hash = 1;
			for (int k = 0; k < key.length; k++)
				hash = hash * 31 + key[k];
			return hash;
		}

		private int hashcodewithid(int id) {
			int hash = 47529 * id;
			return hash;
		}

	}

	static class NodeList {
		Node head;

		public void addNode(Node node) {
			if (head == null) {
				this.head = node;
			} else {
				node.next = head;
				head = node;
			}
		}

		public Node findNode(char key[]) {
			Node node = head;

			while (node != null) {
				if (isequal(node.key, key))
					return node;
				node = node.next;
			}

			return node;
		}

		private boolean isequal(char key1[], char key2[]) {
			if (key1.length != key2.length)
				return false;

			for (int i = 0; i < key1.length; i++) {
				if (key1[i] != key2[i])
					return false;
			}

			return true;
		}
	}

	static class Node {
		int id;
		char key[];
		char value[];

		Node next;// FOR HASH CHAINNING

		public Node(int id, char key[], char value[]) {
			this.id = id;
			this.key = key;
			this.value = value;
		}

		public int hashcodewithkey() {
			int hash = 1;
			for (int k = 0; k < key.length; k++)
				hash = hash * 31 + key[k];
			return hash;
		}

		public int hashcodewithid() {
			int hash = 47529 * id;
			return hash;
		}
	}

	static class Queue {
		int queue[] = null;
		int front = 0, rear = 0;

		public Queue(int capacity) {
			this.queue = new int[capacity];
		}

		public void enqueue(int vid) throws Exception {
			if (isfull())
				throw new Exception("Overflow");
			queue[rear++] = vid;
		}

		public int dequeue() throws Exception {
			if (isempty())
				throw new Exception("Underflow");
			return queue[front++];
		}

		public boolean isempty() {
			return front == rear;
		}

		public boolean isfull() {
			return rear == queue.length;
		}
	}
}
