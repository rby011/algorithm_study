package algo.compression;

import java.util.Scanner;

/**
 * <PRE>
aaabbcccaaabbccaa
aaaabbaacbbbaaccbbbaaaaaddfffddfffeee

aaaa bb aa cb bb aa cc bbb aaaaa dd fff dd fff eee
 * </PRE>
 */
public class Huffman_RunLength {
	static Hashtable rtable = new Hashtable(1000);

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		char txt[] = scan.next().toCharArray();
		scan.close();

		// IDENTIFY RUN IN GIVEN TEXT
		RunIDList ridlist = generateRunChain(txt);
		printRunTxtList(ridlist);

		// BUILD HEAP
		int runcnt = ridlist.rsize;
		MinHeap pqueue = new MinHeap(runcnt * 2);

		RunID runid = ridlist.head;
		while (runid != null) {
			if (!pqueue.contain(runid.runid)) {
				Run run = rtable.get(runid.symbol, runid.symlen);
				pqueue.enqueue(run);
			}
			runid = runid.next;
		}

		// BUILD PREFIX CODE TREE
		while (pqueue.size > 1) {
			Run run1 = pqueue.dequeue();
			Run run2 = pqueue.dequeue();

			Run nrun = new Run(run1.frequency + run2.frequency);
			nrun.lchild = run1;
			nrun.rchild = run2;

			pqueue.enqueue(nrun);
		}
		Run prefixCodeTree = pqueue.dequeue();

		// ASSING CODE WORD TO EACH RUN
		int codelen = assignCodeWord(prefixCodeTree, 0, 0);

		// ENCODING TEXT WITH CODEWORD
		Encoded encode = encodeText(ridlist, codelen);

		int n = encode.encoded.length;
	}
	
	static void printRunTxtList(RunIDList ridlist) {
		RunID runid = ridlist.head;
		while (runid != null) {
			System.out.print(runid.symbol + "[" + runid.symlen + "] > ");
			runid = runid.next;
		}
		System.out.println("null");
	}

	static class Encoded {
		final static int BIT = 32;
		int encoded[] = null;
		int padsize = 0;

		public Encoded(int codelength) {
			int n = codelength / BIT + 1;
			int padsize = BIT - codelength % BIT;
			encoded = new int[n];
		}
	}

	public static Encoded encodeText(RunIDList runidlist, int codelength) {
		RunID runid = runidlist.head;

		Encoded encode = new Encoded(codelength);

		int n = 0, size = Encoded.BIT;
		while (runid != null) {
			Run run = rtable.get(runid.symbol, runid.symlen);
			int codeword = run.codeword, codelen = run.codelen;
			if (size > codelen) {
				size = size - codelen;
				encode.encoded[n] = encode.encoded[n] & (codeword << size);
			} else if (size == codelen) {
				encode.encoded[n] = encode.encoded[n] & codeword;
				size = Encoded.BIT;
				n = n + 1;
			} else {
				int rcodelen = codelen - size;
				encode.encoded[n] = encode.encoded[n] & (codeword >> rcodelen);

				n = n + 1;
				size = Encoded.BIT - rcodelen;
				encode.encoded[n] = encode.encoded[n] & (codeword << size);
			}

			runid = runid.next;
		}

		return encode;
	}
	
	public static int assignCodeWord(Run run, int codeword, int codelen) {
		if (run.lchild == null && run.rchild == null) {
			run.codeword = codeword;
			run.codelen = codelen;

			System.out.print(
					"[symbol, length, frequency, codeword] " + run.symbol + "," + run.symlen + "," + run.frequency + ",");
			for (int cl = codelen; cl > 0; cl--) {
				System.out.print((codeword >> (cl - 1)) & 1);
			}
			System.out.println();

			return codelen;
		}

		int len1 = 0, len2 = 0;
		if (run.lchild != null)
			len1 = assignCodeWord(run.lchild, (codeword << 1) + 0, codelen + 1);
		if (run.rchild != null)
			len2 = assignCodeWord(run.rchild, (codeword << 1) + 1, codelen + 1);

		return len1 + len2;

	}

	public static RunIDList generateRunChain(char[] txt) {
		int symlen = 0;// SYMBOL LENGTH
		char symbol = 0;

		RunIDList ridlist = new RunIDList();

		for (int cursor = 0; cursor < txt.length; cursor++) {
			// STAR WITH NEW CHARACTER
			if (symbol != txt[cursor]) {
				// IF THERE IS EXISTING RUN
				if (symlen != 0) {
					Run run = rtable.get(symbol, symlen);
					// IF NEW RUN INSTANCE
					if (run == null) {
						run = new Run(symbol, symlen);
						rtable.insert(symbol, symlen, run);// ADD TO HASHTABLE
					} else {
						run.frequency++;
					}
					ridlist.addRunIDToList(run.id, symlen, symbol);
				}
				symbol = txt[cursor];
				symlen = 1;
			} else {
				symlen = symlen + 1;
			}
		}

		// FOR THE LAST ELEMENT
		if (symlen != 0) {
			Run run = rtable.get(symbol, symlen);
			// IF NEW RUN INSTANCE
			if (run == null) {
				run = new Run(symbol, symlen);
				rtable.insert(symbol, symlen, run);// ADD TO HASHTABLE
			} else {
				run.frequency++;
			}
			ridlist.addRunIDToList(run.id, symlen, symbol);
		}
		return ridlist;
	}

	static class MinHeap {
		Run rtree[] = null;
		int itable[] = null;
		int size = 0, R = 1;

		public MinHeap(int runcnt) {
			this.rtree = new Run[runcnt + 1];
			this.itable = new int[runcnt + 1];
		}

		public void enqueue(Run run) {
			size = size + 1;
			rtree[size] = run;
			itable[run.id] = size;

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

		public boolean contain(int runid) {
			return itable[runid] != 0;
		}

		private void minheapify(int tindex) {
			int lchild = tindex * 2, rchild = lchild + 1;
			boolean haslchild = lchild <= size, hasrchild = rchild <= size;

			if (!haslchild)
				return;

			int smchild = lchild;
			if (hasrchild && rtree[smchild].frequency > rtree[rchild].frequency) {
				smchild = rchild;
			}

			if (rtree[tindex].frequency <= rtree[smchild].frequency)
				return;

			swap(smchild, tindex);

			minheapify(smchild);
		}

		private void liftup(int tindex) {
			while (tindex > R && rtree[tindex / 2].frequency > rtree[tindex].frequency)
				swap(tindex, tindex / 2);
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
		RunList rlist[] = null;
		int capacity;

		public Hashtable(int capacity) {
			this.capacity = capacity;
			rlist = new RunList[this.capacity];
			for (int i = 0; i < this.capacity; i++) {
				rlist[i] = new RunList();
			}
		}

		// PUT NEW RUN INSTANCE, KEY = symbol + length
		public void insert(char symbol, int length, Run run) {
			int index = (hashcode(symbol, length) & 0x7fffffff) % this.capacity;
			rlist[index].addRunToListInHash(run);// NO UPDATE
		}

		// GET EXSITING INSTANCE BY KEY (symbol + length)
		public Run get(char symbol, int symlen) {
			int index = (hashcode(symbol, symlen) & 0x7fffffff) % this.capacity;
			Run run = rlist[index].headinhash;
			while (run != null) {
				if (run.symbol == symbol && run.symlen == symlen)
					return run;
				run = run.nextinhash;
			}
			return run;
		}

		private int hashcode(char symbol, int len) {
			int hash = 1;
			for (int l = 1; l <= len; l++) {
				hash = hash * 31 + symbol;
			}
			return hash;
		}
	}

	// THISI IS TEXT LIST
	static class RunIDList {
		RunID head;
		RunID tail;

		int rsize = 0;

		public void addRunIDToList(int runid, int symlen, char symbol) {
			this.rsize++;

			RunID ridinst = new RunID(runid, symlen, symbol);
			if (head == null) {
				head = tail = ridinst;
				return;
			}
			if (head == tail) {
				head.next = ridinst;
				tail = ridinst;
				return;
			}
			tail.next = ridinst;
			tail = ridinst;
		}
	}

	// THISI IS TEXT LIST
	static class RunID {
		int runid;

		char symbol;
		int symlen;

		RunID next;

		public RunID(int runid, int symlen, char symbol) {
			this.runid = runid;
			this.symbol = symbol;
			this.symlen = symlen;
		}
	}

	// THIS IS FOR HASHTABLE
	static class RunList {
		Run headinhash;

		public void addRunToListInHash(Run run) {
			if (headinhash == null) {
				headinhash = run;
				return;
			}
			run.nextinhash = headinhash;
			headinhash = run;
		}

	}

	// THIS IS FOR HASHTABLE AND MINIMUM HEAP
	static class Run {
		static int IDMGR = 0;

		int id;
		int symlen, frequency;
		int codeword, codelen;
		char symbol;

		// FOR HASHTABLE
		Run nextinhash;

		// FOR PREFIX CODE TREE
		Run lchild;
		Run rchild;

		public Run(int frequency) {
			this.symlen = 0;
			this.symbol = 0;
			this.frequency = frequency;
			this.id = ++IDMGR;
		}

		public Run(char symbol, int length) {
			this.symbol = symbol;
			this.symlen = length;
			this.frequency = 1;

			this.id = ++IDMGR;
		}
	}
}
