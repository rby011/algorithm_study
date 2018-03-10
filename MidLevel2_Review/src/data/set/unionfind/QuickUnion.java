package data.set.unionfind;

/**
 * @ref https://m.blog.naver.com/osw5144/120207429529
 * @author Changsun Song
 *
 */
public class QuickUnion {
	public static void main(String args[]) {
		Set set = new Set(6);
		set.union(0, 2);
		set.union(2, 4);

		set.union(1, 3);
		set.union(3, 5);

		for (int i = 0; i < set.parent.length; i++) {
			System.out.println("p[" + i + "] = " + set.parent[i]);
		}
		System.out.println();

		set.union(1, 2);
		for (int i = 0; i < set.parent.length; i++) {
			System.out.println("p[" + i + "] = " + set.parent[i]);
		}

	}

	static class Set {
		int parent[] = null;
		int size[] = null;

		public Set(int capacity) {
			this.parent = new int[capacity];
			this.size = new int[capacity];
			for (int n = 0; n < parent.length; n++) {
				parent[n] = n;
				size[n] = 1;
			}
		}

		public int find(int nid) {
			while (nid != parent[nid]) {
				parent[nid] = parent[parent[nid]];// PATH COMPRESSION
				nid = parent[nid];// NEXT NODE TO CHECK
			}
			return nid;
		}

		public void union(int nid1, int nid2) {
			int rid1 = find(nid1), size1 = size[nid1];
			int rid2 = find(nid2), size2 = size[nid2];

			// SAME SET
			if (rid1 == rid2)
				return;

			if (size1 > size2) { // SET2 ---> SET1
				parent[rid2] = rid1;
				size[rid1] = size[rid1] + size[rid2];
			} else { // SET1 --> SET2
				parent[rid1] = rid2;
				size[rid2] = size[rid2] + size[rid1];
			}
		}
	}
}
