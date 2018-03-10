package data.tree;

import java.util.Scanner;

public class PrefixCodeTree {
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);

		int L = scan.nextInt();

		MinHeap minqueue = new MinHeap(L * 2);
		Hashtable ctable = new Hashtable(L * 2);

		int rid = 1;
		char chs[] = scan.next().toCharArray();
		for (int l = 0; l < L; l++) {
			char ch = chs[l];
			Run run = ctable.get(ch);
			if (run == null) {
				run = new Run(rid++, ch);
				ctable.put(ch, run);
				minqueue.addRun(run);
			} else {
				run.freq++;
			}
		}

		minqueue.buildHeap();

		while (minqueue.size != 1) {
			Run run1 = minqueue.dequeue();
			Run run2 = minqueue.dequeue();
			
			Run rrun = new Run(run1.freq + run2.freq);

			rrun.lchild = run1;
			rrun.rchild = run2;
			rrun.id = rid++;

			minqueue.enqueue(rrun);
		}

		Run prefixtree = minqueue.dequeue();
		printoutCode(ctable, prefixtree, 0, 0);

		scan.close();
	}

	public static void printoutCode(Hashtable ctable, Run run, int code, int codelength) {
		if (run.lchild == null && run.rchild == null) {

			System.out.print(run.ch + " : ");
			for (int c = codelength; c > 0; c--)
				System.out.print(code >> (c - 1) & 0x00000001);
			System.out.println();
			
			ctable.get(run.ch).code = code;;
			
			return;
		}

		if (run.lchild != null)
			printoutCode(ctable, run.lchild, (code << 1) + 0, codelength + 1);
		if (run.rchild != null)
			printoutCode(ctable, run.rchild, (code << 1) + 1, codelength + 1);
	}

	static class Hashtable {
		RunList rlist[] = null;
		int capacity;

		public Hashtable(int capacity) {
			this.capacity = capacity;
			rlist = new RunList[capacity];
			for (int c = 0; c < capacity; c++)
				rlist[c] = new RunList();
		}

		public void put(char key, Run run) {
			int index = (hashcode(key) & 0x7fffffff) % this.capacity;
			rlist[index].addRunToList(run);
		}

		public Run get(char key) {
			int index = (hashcode(key) & 0x7fffffff) % this.capacity;
			Run run = rlist[index].head;
			while (run != null) {
				if (run.ch == key)
					return run;
			}
			return null;
		}

		private int hashcode(char key) {
			int code = (int) key * 31 % capacity;
			return code;
		}
	}

	static class RunList {
		Run head;

		public void addRunToList(Run item) {
			if (head == null) {
				head = item;
				return;
			}
			item.next = head;
			head = item;
		}
	}

	static class Item {
		int runid;
		char ch;
		Item next;

		public Item(char ch, int rid) {
			this.ch = ch;
			this.runid = rid;
		}
	}

	static class MinHeap {
		Run tree[] = null;
		int itable[] = null;
		int size = 0, R = 1;

		public MinHeap(int capacity) {
			tree = new Run[capacity];
			itable = new int[capacity];
		}

		public void buildHeap() {
			int start = size / 2;
			for (int s = start; s >= R; s--)
				minHeapify(s);
		}

		public void enqueue(Run run) {
			itable[run.id] = size + 1;
			tree[size + 1] = run;
			size = size + 1;

			liftup(size);
		}

		public Run dequeue() {
			Run min = tree[R];

			swap(size, R);

			//tree[size] = null;
			itable[min.id] = 0;

			size--;
			
			minHeapify(R);
			
			return min;
		}

		public boolean isempty() {
			return size == 0;
		}

		private void liftup(int tindex) {
			while (tindex > R && tree[tindex].freq < tree[tindex / 2].freq)
				swap(tindex, tindex / 2);
		}

		private void minHeapify(int tindex) {
			int lchild = tindex * 2;
			int rchild = lchild + 1;
			boolean haslchild = lchild <= size;
			boolean hasrchild = rchild <= size;

			if (!haslchild)
				return;

			int schild = lchild;
			if (hasrchild && tree[rchild].freq < tree[schild].freq) {
				schild = rchild;
			}

			if (tree[tindex].freq <= tree[schild].freq)
				return;

			swap(tindex, schild);

			minHeapify(schild);
		}

		private void swap(int tindex1, int tindex2) {
			int itmp = itable[tree[tindex1].id];
			itable[tree[tindex1].id] = itable[tree[tindex2].id];
			itable[tree[tindex2].id] = itmp;

			Run rtmp = tree[tindex1];
			tree[tindex1] = tree[tindex2];
			tree[tindex2] = rtmp;
		}

		// JUST FOR BUILDING RUN-TABLE, NOT FOR HEAP
		public void addRun(Run run) {
			// FROM INDEX 1
			tree[size + 1] = run;
			itable[run.id] = size + 1;
			size = size + 1;
		}

		public Run getRun(int rid) {
			return tree[itable[rid]];
		}

		// FOR BOTH OF THEM, RUN-TABLE AND HEAP
		public boolean contain(int rid) {
			return itable[rid] != 0;
		}
	}

	static class Run {
		int id;
		char ch;
		int freq;
		
		int code;
		int codelength;
		
		Run lchild;
		Run rchild;

		Run next;

		public Run(int frequency) {
			this.id = -1;
			this.ch = (char) 0;
			this.freq = frequency;
		}

		public Run(int id, char character) {
			this.ch = character;
			this.id = id;
			this.freq = 1;
		}

	}
}