package data.hash;

public class HashChaining {
	public static void main(String args[]) {
		String keys[] = { "abc", "abcd", "abcde", "abcdef", "abcdefg", "abc" };
		String values[] = { "abc", "abcd", "abcde", "abcdef", "abcdefg", "abc1" };

		Hashtable htable = new Hashtable(keys.length - keys.length / 4);

		for (int i = 0; i < keys.length; i++) {
			htable.put(keys[i], values[i]);
		}

		for (int i = 0; i < keys.length; i++) {
			System.out.println(htable.get(keys[i]));
		}

	}

	static class Hashtable {
		EntryList table[] = null;
		int capacity;

		Hashtable(int capacity) {
			this.capacity = capacity;
			table = new EntryList[this.capacity];
		}

		// NEED TO CONSIDER UPDATE (SAME KEY BUT DIFFERENT VALUE)
		public void put(String key, String value) {

			Entry entry = new Entry(key, value);
			int index = hashcode(key) & 0x7fffffff % capacity;

			if (table[index] == null)
				table[index] = new EntryList();
			else
				System.out.println("Collision");

			table[index].addEntry(entry);
		}

		public String get(String key) {
			int index = hashcode(key) & 0x7fffffff % capacity;

			if (table[index] == null)
				return null;

			Entry entry = table[index].head;
			while (entry != null && !entry.key.equals(key)) {
				entry = entry.next;
			}

			if (entry == null)
				return null;

			return entry.value;
		}

	}

	static class EntryList {
		Entry head;

		public void addEntry(Entry entry) {
			if (head == null) {
				head = entry;
			} else {
				entry.next = head;
				head = entry;
			}
		}

		public boolean removeEntry(String key) {
			Entry entry = head, pentry = null;
			while (entry != null && !entry.key.equals(key)) {
				pentry = entry;
				entry = entry.next;
			}

			if (entry == null)
				return false;

			if (entry == head) {
				head = head.next;
			} else {
				pentry.next = entry.next;
			}
			entry = null;

			return true;
		}
	}

	static class Entry {
		Entry next = null;

		String key = null;
		String value = null;

		Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	public static int hashcode(String key) {
		int hash = 1;
		char ckey[] = key.toCharArray();
		for (int c = 0; c < ckey.length; c++) {
			hash = hash * 41 + ckey[c];
		}
		return hash;
	}
}
