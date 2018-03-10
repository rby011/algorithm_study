package algo.compression;

import java.util.Scanner;

/**
 * <PRE>
aaabbbaaabbbaabbaaa
aaabbcccaaabbccaa
aaaabbaacbbbaaccbbbaaaaaddfffddfffeee

aaaa bb aa cb bb aa cc bbb aaaaa dd fff dd fff eee
 * </PRE>
 */
public class Huffman_RunLength_Reimpl {

	static Hashtable rtable = null;
	static List rlist = null;
	static MinHeap pqueue = null;

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		char txt[] = scan.next().toCharArray();
		scan.close();

		rtable = new Hashtable(txt.length);
		rlist = new List();

		// GENERAED LINKED LIST OF RUN
		generateRunList(txt);
		// DEBUG // rlist.printRunList();

		// BUILD HEAP FOR PREFIX CODE TREE
		pqueue = new MinHeap(Run.INSTANCE_CNT * 2);
		ListItem litem = rlist.head;
		while (litem != null) {
			if (!pqueue.contains(litem.run))
				pqueue.enqueue(litem.run);
			litem = litem.next;
		}

		// BUILD PREFIX CODE TREE
		while (pqueue.size > 1) {
			Run lchild = pqueue.dequeue();
			Run rchild = pqueue.dequeue();
			Run parent = new Run(lchild, rchild);
			pqueue.enqueue(parent);
		}
		Run pctree = pqueue.dequeue();// PREFIX CODE TREE

		// REMAINING IS OMITTED!!
	}

	static void generateRunList(char txt[]) {
		char symbol = 0;
		int symlen = 0, runinstancecnt = 0;
		for (int t = 0; t < txt.length; t++) {
			if (txt[t] != symbol) {
				if (symbol != 0) {
					Run run = rtable.get(symbol, symlen);
					if (run == null) {
						run = new Run(symbol, symlen);
						rtable.insert(run);
					} else {
						run.frequency++;
					}
					ListItem litem = new ListItem(run);
					rlist.addItemToList(litem);
				}
				// FOR NEXT RUN
				symbol = txt[t];
				symlen = 1;
			} else {
				symlen = symlen + 1;
			}

		}

		// FOR THE LAST RUN
		Run run = rtable.get(symbol, symlen);
		if (run == null) {
			run = new Run(symbol, symlen);
			rtable.insert(run);
		} else {
			run.frequency++;
		}
		ListItem litem = new ListItem(run);
		rlist.addItemToList(litem);
	}

	static class MinHeap {
		Run rtree[] = null;
		int itable[] = null;
		int size = 0, R = 1;

		public MinHeap(int capacity) {
			this.rtree = new Run[capacity + 1];
			this.itable = new int[capacity + 1];
		}

		public void enqueue(Run run) {
			size = size + 1;
			rtree[size] = run;

			liftup(size);
		}

		public Run dequeue() {
			Run min = rtree[R];

			swap(R, size);
			itable[min.id] = 0;
			rtree[size] = null;
			size = size - 1;

			minheapify(R);

			return min;
		}

		public boolean contains(Run run) {
			return itable[run.id] != 0;
		}

		private void liftup(int tindex) {
			while (tindex > R && rtree[tindex].frequency < rtree[tindex / 2].frequency) {
				swap(tindex, tindex / 2);
			}
		}

		private void minheapify(int tindex) {
			int lchild = tindex * 2, rchild = lchild + 1;
			boolean haslchild = lchild <= size, hasrchild = rchild <= size;

			if (haslchild)
				return;

			int smchild = lchild;
			if (hasrchild && rtree[rchild].frequency < rtree[smchild].frequency)
				smchild = rchild;

			if (rtree[tindex].frequency <= rtree[smchild].frequency)
				return;

			swap(tindex, smchild);

			minheapify(smchild);
		}

		private void swap(int tindex1, int tindex2) {
			int itmp = itable[rtree[tindex1].id];
			itable[rtree[tindex1].id] = itable[rtree[tindex2].id];
			itable[rtree[tindex2].id] = itmp;

			Run rtmp = rtree[tindex1];
			rtree[tindex1] = rtree[tindex2];
			rtree[tindex2] = rtmp;
		}
	}

	static class Hashtable {
		HashItemList itemtable[] = null;
		int capacity;

		public Hashtable(int capacity) {
			this.capacity = capacity;
			this.itemtable = new HashItemList[this.capacity];
			for (int i = 0; i < this.capacity; i++)
				itemtable[i] = new HashItemList();
		}

		// ASSUME : THE 'run' instance IS A NEW
		public void insert(Run run) {
			int index = (hashcode(run.symbol, run.symlen) & 0x7fffffff) % this.capacity;
			itemtable[index].addItemToList(run);
		}

		public Run get(char symbol, int symlen) {
			int index = (hashcode(symbol, symlen) & 0x7fffffff) % this.capacity;
			HashItem hitem = itemtable[index].head;

			while (hitem != null) {
				if (symbol == hitem.run.symbol && symlen == hitem.run.symlen) {
					return hitem.run;
				}
				hitem = hitem.next;
			}

			return null;
		}

		private int hashcode(char symbol, int symlength) {
			int hash = 1;
			for (int l = 0; l < symlength; l++) {
				hash = hash * 31 + symbol;
			}
			return hash;
		}
	}

	static class HashItemList {
		HashItem head;

		public void addItemToList(Run run) {
			HashItem hitem = new HashItem(run);
			if (head == null) {
				head = hitem;
				return;
			}
			hitem.next = head;
			head = hitem;
		}
	}

	static class HashItem {

		Run run;
		HashItem next;

		public HashItem(Run run) {
			this.run = run;
		}
	}

	static class List {
		ListItem head, tail;
		int size;

		public void addItemToList(ListItem litem) {
			size++;
			if (head == null) {
				this.head = this.tail = litem;
				return;
			}

			if (head == tail) {
				this.head.next = litem;
				this.tail = litem;
				return;
			}

			this.tail.next = litem;
			this.tail = litem;

		}

		public void printRunList() {
			ListItem litem = head;
			while (litem != null) {
				char symbol = litem.run.symbol;
				int symlen = litem.run.symlen;
				System.out.print(symbol + "[l:" + symlen + "] > ");
				litem = litem.next;
			}
			System.out.println("null");
		}
	}

	static class ListItem {
		ListItem next;
		Run run;

		public ListItem(Run run) {
			this.run = run;
		}
	}

	static class Run {
		static int INSTANCE_CNT = 0;
		char symbol;
		int id, symlen, frequency, codeword, codelen;

		Run lchild, rchild;

		public Run(Run lchild, Run rchild) {
			this.frequency = lchild.frequency + rchild.frequency;
			this.lchild = lchild;
			this.rchild = rchild;
			this.id = ++INSTANCE_CNT;
		}

		public Run(char symbol, int symlen) {
			this.symbol = symbol;
			this.symlen = symlen;
			this.frequency = 1;
			this.id = ++INSTANCE_CNT;
		}
	}
}
