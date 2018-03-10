package data.tree.heap;

public class PriorityQueue {
	public static void main(String args[]) throws Exception {
		int items[] = { 5, 4, 3, 2, 8, 6, 1 };

		MinPQ minq = new MinPQ(10);
		for (int i = 0; i < items.length; i++)
			minq.enqueue(items[i]);

		for (int i = minq.size; i > 0; i--)
			System.out.print(minq.dequeue() + " ");

		System.out.println();

		MaxPQ maxq = new MaxPQ(10);
		for (int i = 0; i < items.length; i++)
			maxq.enqueue(items[i]);

		for (int i = maxq.size; i > 0; i--)
			System.out.print(maxq.dequeue() + " ");

	}

	static class MinPQ {
		int tree[] = null;
		int size = 0;

		MinPQ(int capacity) {
			tree = new int[capacity + 1];
		}

		public void enqueue(int item) throws Exception {
			if (isfull())
				throw new Exception("Overflow Exception");

			int nindex = ++size;
			tree[nindex] = item;

			while (nindex > 1) {
				int pnindex = nindex / 2;
				if (tree[pnindex] > tree[nindex]) {
					// LIFT UP
					int tmp = tree[pnindex];
					tree[pnindex] = tree[nindex];
					tree[nindex] = tmp;

					nindex = pnindex;
				} else {
					break;
				}
			}
		}

		public int dequeue() throws Exception {
			if (isempty())
				throw new Exception("Underflow Exception");

			int min = tree[1];
			tree[1] = tree[size--];
			minHeapify(1);

			return min;
		}

		private void minHeapify(int nindex) {
			int lcindex = nindex * 2, rcindex = lcindex + 1;
			boolean haslchild = lcindex <= size, hasrchild = rcindex <= size;

			if (!haslchild && !hasrchild)
				return;

			int minindex = lcindex;
			if (hasrchild && tree[rcindex] < tree[minindex])
				minindex = rcindex;

			if (tree[nindex] <= tree[minindex])
				return;

			int tmp = tree[minindex];
			tree[minindex] = tree[nindex];
			tree[nindex] = tmp;

			minHeapify(minindex);
		}

		private boolean isempty() {
			return size == 0;
		}

		private boolean isfull() {
			return size == tree.length - 1;
		}

	}

	static class MaxPQ {
		int tree[] = null;
		int size = 0;

		public MaxPQ(int capacity) {
			tree = new int[capacity + 1];
		}

		public void enqueue(int item) throws Exception {
			if (isfull())
				throw new Exception("Overflow Exception");

			int nindex = ++size;
			tree[nindex] = item;

			while (nindex > 1) {
				int pindex = nindex / 2;

				if (tree[pindex] < tree[nindex]) {
					int tmp = tree[pindex];
					tree[pindex] = tree[nindex];
					tree[nindex] = tmp;

					nindex = pindex;
				} else {
					break;
				}
			}
		}

		public int dequeue() throws Exception {
			if (isempty())
				throw new Exception("Underflow Exception");

			int min = tree[1];

			tree[1] = tree[size--];
			maxHeapify(1);

			return min;
		}

		private void maxHeapify(int nindex) {
			int lcindex = nindex * 2, rcindex = lcindex + 1;
			boolean haslchild = lcindex <= size, hasrchild = rcindex <= size;

			if (!haslchild)
				return;

			int maxindex = lcindex;
			if (hasrchild && tree[rcindex] > tree[maxindex])
				maxindex = rcindex;

			if (tree[nindex] >= tree[maxindex])
				return;

			int tmp = tree[nindex];
			tree[nindex] = tree[maxindex];
			tree[maxindex] = tmp;

			maxHeapify(maxindex);
		}

		private boolean isempty() {
			return size == 0;
		}

		private boolean isfull() {
			return size == tree.length - 1;
		}
	}

}
