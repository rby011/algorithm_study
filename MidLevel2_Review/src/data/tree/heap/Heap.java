package data.tree.heap;

public class Heap {
	public static void main(String args[]) {
		int items[] = { 0, 4, 3, 2, 5, 7, 4, 3, 1, 3, 8 };
		
		MaxHeap maxheap = new MaxHeap(items);
		maxheap.sortByAscending();
		for (int i = 1; i < items.length; i++)
			System.out.print(items[i] + " ");
		
		System.out.println();
		
		MinHeap minheap = new MinHeap(items);
		minheap.sortByDescending();
		for (int i = 1; i < items.length; i++)
			System.out.print(items[i] + " ");
		
		
	}

	static class MinHeap {
		private int tree[] = null;

		public MinHeap(int items[]) {
			tree = items;
		}

		public void sortByDescending() {
			buildHeap();

			for (int idx = tree.length - 1, lidx = tree.length - 1; idx >= 2; idx--) {
				int min = tree[1];
				tree[1] = tree[lidx];
				tree[lidx] = min;

				lidx = lidx - 1;

				minHeapify(1, lidx);
			}
		}

		private void buildHeap() {
			for (int index = tree.length / 2; index > 0; index--)
				minHeapify(index, tree.length - 1);
		}

		private void minHeapify(int nindex, int size) {
			int lcindex = nindex * 2, rcindex = lcindex + 1;
			boolean haslchild = lcindex <= size;
			boolean hasrchild = rcindex <= size;

			if (!haslchild && !hasrchild)
				return;

			int mncindex = lcindex;
			if (hasrchild && tree[rcindex] < tree[lcindex])
				mncindex = rcindex;

			if (tree[nindex] <= tree[mncindex])
				return;

			int tmp = tree[mncindex];
			tree[mncindex] = tree[nindex];
			tree[nindex] = tmp;

			minHeapify(mncindex, size);

		}
	}

	static class MaxHeap {
		private int tree[] = null;

		// ASSUME : NOT USE ZERO INDEX
		// - size = tree.length-1;
		public MaxHeap(int items[]) {
			this.tree = items;
		}

		public void sortByAscending() {
			buildHeap();
			for (int idx = tree.length - 1, lidx = tree.length - 1; idx >= 2; idx--) {
				// SWAP ROOT WITH LAST NODE
				int tmp = tree[1];
				tree[1] = tree[lidx];
				tree[lidx] = tmp;

				// REDUCE HEAP AREA
				lidx = lidx - 1;

				// HEAPIFY FROM ROOT
				maxHeapify(1, lidx);
			}
		}

		private void buildHeap() {
			for (int idx = tree.length / 2; idx > 0; idx--)
				maxHeapify(idx, tree.length - 1);
		}

		private void maxHeapify(int node_idx, int size) {
			// CHILDREN
			int lcnode_idx = node_idx * 2;
			int rcnode_idx = lcnode_idx + 1;

			// IF NO CHILDREN
			if (lcnode_idx > size)
				return;

			// BIGGER CHILD
			int mxcnode_idx = lcnode_idx;
			if (rcnode_idx <= size && tree[rcnode_idx] > tree[lcnode_idx])
				mxcnode_idx = rcnode_idx;

			// COMPARE CHILDREN WITH PARENT
			if (tree[node_idx] >= tree[mxcnode_idx])
				return;

			// SWAP
			int tmp = tree[mxcnode_idx];
			tree[mxcnode_idx] = tree[node_idx];
			tree[node_idx] = tmp;

			maxHeapify(mxcnode_idx, size);
		}
	}
}
