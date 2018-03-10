package algo.sort;

public class QuickSort {

	public static void main(String args[]) {
		int items[] = { 1, 2, 3, 4, 5 };

		sort(items);

		for (int i = 0; i < items.length; i++)
			System.out.print(items[i] + " ");
	}

	private static void sort(int items[]) {
		quicksort(0, items.length - 1, items);
	}

	private static void quicksort(int left, int right, int items[]) {
		int l = left - 1, i = 0, p = right;
		// 1) PARTITIONING
		while (i < p) {
			if (items[i] < items[p]) {
				if (l + 1 != i) {
					int tmp = items[l + 1];
					items[l + 1] = items[i];
					items[i] = tmp;
				}
				l = l + 1;
			}
			i = i + 1;
		}
		
		int tmp = items[p];
		items[p] = items[l + 1];
		items[l + 1] = tmp;

		// 2) CALL
		if (left < l)
			quicksort(left, l, items);
		if (l + 2 < right)
			quicksort(l + 2, right, items);
	}

}
