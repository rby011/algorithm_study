package data.tree.mst;

import java.util.Scanner;

/**
 * <PRE>
6 9
a b 1
a e 8
b e 2
b c 4
c e 3
c f 5
e f 9
d c 6
d f 7
 * </PRE>
 */
public class Kruskal {
	public static void main(String args[]) throws Exception {
		/**
		 * FOR SYMBOL TABLE OF NODES, EDGE ARRAY
		 */
		Scanner scan = new Scanner(System.in);
		int V = scan.nextInt(), E = scan.nextInt();
		Edge edges[] = new Edge[E + 1];
		Hashtable nsymtable = new Hashtable(V);

		for (int e = 1, nid = 0; e <= E; e++) {
			String n1k = scan.next();
			String n2k = scan.next();
			int weight = scan.nextInt();

			Node node1 = null, node2 = null;
			if (!nsymtable.iscontain(n1k)) {
				node1 = new Node(nid++, n1k);
				nsymtable.put(node1);
			} else {
				node1 = nsymtable.get(n1k);
			}
			if (!nsymtable.iscontain(n2k)) {
				node2 = new Node(nid++, n2k);
				nsymtable.put(node2);
			} else {
				node2 = nsymtable.get(n2k);
			}

			Edge edge = new Edge(node1, node2, weight);
			edges[e] = edge;
		}
		scan.close();

		/**
		 * MINIMU SPANNING TREE <KRUSKAL>
		 */
		MinHeap pqueue = new MinHeap(edges);
		Set set = new Set(V);

		while (!pqueue.isempty()) {
			Edge edge = pqueue.dequeue();
			if (!set.isconnected(edge.u.id, edge.v.id)) {
				set.union(edge.u.id, edge.v.id);
				System.out.println(edge.u.key + "," + edge.v.key + "," + edge.weight);
			}
		}
	}

	/**
	 * FOR KRUSKAL
	 */
	static class Set {
		int parent[] = null;// FOR THE 'node id'
		int tsize[] = null;

		public Set(int capacity) {
			parent = new int[capacity];
			tsize = new int[capacity];
			for (int i = 0; i < parent.length; i++)
				parent[i] = i;
		}

		public void union(int nid1, int nid2) {
			int rid1 = find(nid1), rid2 = find(nid2);

			if (rid1 == rid2)
				return;

			if (tsize[rid1] > tsize[rid2]) {
				parent[rid2] = rid1;
				tsize[rid1] = tsize[rid1] + tsize[rid2];
			} else {
				parent[rid1] = rid2;
				tsize[rid2] = tsize[rid1] + tsize[rid2];
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
			int rid1 = find(nid1);
			int rid2 = find(nid2);
			return rid1 == rid2;
		}
	}

	/**
	 * FOR SORTING EDGES BY ASCENDING ORDER - JUST BUILD HEAP WITH GIVEN EDGE ARRAY
	 */
	static class MinHeap {
		Edge tree[] = null;
		int size = 0, R = 1;

		// THE 'edges' MUST START FROM '1' INDEX, NOT USING '0'
		public MinHeap(Edge edges[]) {
			this.tree = edges;
			this.size = edges.length - 1;

			// BUILD HEAP WITH THE 'edges'
			buildMinHeap();
		}

		public Edge dequeue() throws Exception {
			if (isempty())
				throw new Exception("UNDERFLOW");

			Edge redge = tree[R];
			tree[R] = tree[size--];
			this.minHeapify(R);

			return redge;
		}

		public boolean isempty() {
			return size == 0;
		}

		private void buildMinHeap() {
			int start = size / 2;
			for (int s = start; s <= R; s--)
				minHeapify(s);
		}

		private void minHeapify(int index) {
			int lchild = index * 2;
			int rchild = lchild + 1;

			boolean haslchild = lchild <= size;
			boolean hasrchild = rchild <= size;

			if (!haslchild)
				return;

			int smaller = lchild;
			if (hasrchild && tree[rchild].weight < tree[lchild].weight) {
				smaller = rchild;
			}

			if (tree[index].weight <= tree[smaller].weight)
				return;

			Edge tmp = tree[index];
			tree[index] = tree[smaller];
			tree[smaller] = tmp;

			minHeapify(smaller);
		}
	}

	static class Edge {
		Node u;
		Node v;
		int weight;

		public Edge(Node u, Node v, int weight) {
			this.u = u;
			this.v = v;
			this.weight = weight;
		}
	}

	/**
	 * SYMBOL TABLE FOR MATCHING NODE'S KEY AND ITS ID
	 */
	static class Hashtable {
		NodeList table[] = null;
		int capacity;

		public Hashtable(int capacity) {
			table = new NodeList[capacity];
			for (int c = 0; c < capacity; c++) {
				table[c] = new NodeList();
			}
			this.capacity = capacity;
		}

		public void put(Node node) {
			int index = this.hashcode(node) & 0x7fffffff % capacity;
			// NO UPDATE, JUST ADD TO FIRST
			if (!iscontain(node.key))
				table[index].addToList(node);
		}

		public boolean iscontain(String key) {
			int index = this.hashcode(key) & 0x7fffffff % capacity;
			Node inode = table[index].head;
			while (inode != null && !inode.key.equals(key)) {
				inode = inode.next;
			}
			if (inode == null)
				return false;
			return true;
		}

		public Node get(String key) {
			int index = this.hashcode(key) & 0x7fffffff % capacity;
			Node node = table[index].head;
			while (node != null && !node.key.equals(key))
				node = node.next;
			return node;
		}

		private int hashcode(Node node) {
			return hashcode(node.key);
		}

		private int hashcode(String key) {
			int hash = 1;
			char keyarr[] = key.toCharArray();
			for (int l = 0; l < keyarr.length; l++) {
				hash = hash * 31 + keyarr[l];
			}
			return hash;
		}
	}

	/**
	 * FOR HASHTABLE CHAINING
	 */
	static class NodeList {
		Node head;

		public void addToList(Node node) {
			if (head == null) {
				this.head = node;
				return;
			}
			node.next = head;
			head = node;
		}
	}

	static class Node {
		int id;
		String key;
		Node next = null;

		public Node(int id, String key) {
			this.id = id;
			this.key = key;
		}
	}
}