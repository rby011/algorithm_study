package data.set.unionfind;

public class QuickFind {
	public static void main(String args[]) {
		
	}

	static class Set {
		int set[] = null;
		int capacity = 0;// EQUALS TO # OF NODES

		public Set(int capacity) {
			set = new int[capacity];
			// EACH NODE IS ASSIGNED TO EACH SET
			for (int n = 0; n < capacity; n++)
				set[n] = n;
			this.capacity = capacity;
		}

		public int find(int nodeid) throws Exception {
			if (nodeid < 0 && nodeid > capacity - 1)
				throw new Exception("UNDEFINED NODE");
			return set[nodeid];
		}

		// SET(nid2) --> SET(nid1) , THE 'nid' EXPRESSES THE 'nodeid'
		public void union(int nid1, int nid2) throws Exception {
			int rid1 = find(nid1);
			int rid2 = find(nid2);
			if (rid1 == rid2)
				return;
			for (int n = 0; n < set.length; n++) {
				if (set[n] == rid2)
					set[n] = rid1;
			}
		}
	}

	static class SymbolTable {
		int table[] = null;
		int groups[] = { 2, 3, 5, 7 };

		public SymbolTable(int capacity) {
			table = new int[capacity];
			initialzieSymTable();
		}

		private void initialzieSymTable() {
			for (int n = 0; n < table.length; n++) {
				for (int g = 0; g < groups.length; g++) {
					if (n % groups[g] == 0)
						table[n] = g;
				}
			}
		}
	}
}
