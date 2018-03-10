package data.tree;

/**
 * (*) Node has a parent node in its definition
 * @ref https://blog.naver.com/gngh0101/221180812037
 *
 */
public class BinarySearchTree_Step1 {
	public static void main(String args[]) throws Exception {
		int keys[] = { 15, 8, 28 };// , 18, 45, 41, 48, 30, 50, 38, 33 };

		BST btree = new BST();
		for (int i = 0; i < keys.length; i++) {
			btree.insert(keys[i]);
		}

		btree.printTreeByHeight();
		System.out.println();

		btree.delete(15);
		btree.printTreeByHeight();
		System.out.println();
	}

	static class BST {
		Node root;

		public Node find(int key) {
			Node node = findNode(key, root);
			return node;
		}

		private Node findNode(int key, Node node) {
			if (node == null)
				return null;
			if (node.key == key)
				return node;

			Node rnode = null;
			if (key < node.key)
				rnode = findNode(key, node.lchild);
			else
				rnode = findNode(key, node.rchild);

			return rnode;
		}

		private Node findMinNode(Node node) {
			while (node.lchild != null) {
				node = node.lchild;
			}

			return node;
		}

		public void insert(int key) {
			// IN CASE THAT TREE IS EMPTY
			if (root == null) {
				root = new Node(key);
				return;
			}

			Node node = root, pnode = null;
			while (node != null) {
				if (node.key == key) {
					// UPDATE VALUE
					return;
				}

				pnode = node;
				if (key < node.key) {
					node = node.lchild;
				} else {
					node = node.rchild;
				}
			}

			if (key < pnode.key) {
				pnode.lchild = new Node(key);
				pnode.lchild.parent = pnode;
			} else {
				pnode.rchild = new Node(key);
				pnode.rchild.parent = pnode;
			}
		}

		public boolean delete(int key) {
			Node dnode = this.root;
			boolean isLeft = false;

			while (dnode != null && dnode.key != key) {
				if (key == dnode.key) {
					break;
				} else if (key < dnode.key) {
					dnode = dnode.lchild;
					isLeft = true;
				} else {
					dnode = dnode.rchild;
					isLeft = false;
				}
			}
			if (dnode == null)
				return false;

			if (dnode.lchild == null && dnode.rchild == null) {
				if (dnode == root) {
					this.root = null;
				} else {
					if (isLeft) {
						dnode.parent.lchild = null;
					} else {
						dnode.parent.rchild = null;
					}
					dnode.parent = null;
				}
				dnode = null;
			} else if (dnode.lchild != null && dnode.rchild == null) {
				if (dnode == root) {
					dnode.lchild.parent = null;
					this.root = dnode.lchild;
					dnode.lchild = null;
				} else {
					if (isLeft) {
						dnode.parent.lchild = dnode.lchild;
						dnode.lchild.parent = dnode.parent;
					} else {
						dnode.parent.rchild = dnode.lchild;
						dnode.lchild.parent = dnode.parent;
					}
					dnode.parent = null;
					dnode.lchild = null;
				}
				dnode = null;
			} else if (dnode.lchild == null && dnode.rchild != null) {
				if (dnode == root) {
					dnode.rchild.parent = null;
					this.root = dnode.rchild;
					dnode.rchild = null;
				} else {
					if (isLeft) {
						dnode.parent.lchild = dnode.rchild;
						dnode.rchild.parent = dnode.parent;
					} else {
						dnode.parent.rchild = dnode.rchild;
						dnode.rchild.parent = dnode.parent;
					}
					dnode.parent = null;
					dnode.rchild = null;
				}
				dnode = null;
				dnode = null;
			} else if (dnode.lchild != null && dnode.rchild != null) {
				boolean isContinuous = false;

				Node snode = findMinNode(dnode.rchild);

				if (snode.rchild != null && snode.parent.key != dnode.key) {
					snode.rchild.parent = snode.parent;
					if (snode.parent.lchild != null)
						snode.parent.lchild = snode.rchild;
				} else if (/* snode.rchild != null && */snode.parent.key == dnode.key) {
					isContinuous = true;
				} else if (snode.rchild == null) {
					if (snode.parent.lchild.key == snode.key)
						snode.parent.lchild = null;
					else if (snode.parent.rchild.key == snode.key)
						snode.parent.rchild = null;
					snode.parent = null;
				}

				if (dnode == root) {
					snode.parent = null;
					snode.lchild = dnode.lchild;
					if (!isContinuous) {
						snode.rchild = dnode.rchild;
					} else {
						dnode.lchild.parent = snode;
					}
					this.root = snode;
				} else {
					if (isLeft) {
						dnode.parent.lchild = snode;
					} else {
						dnode.parent.rchild = snode;
					}
					snode.parent = dnode.parent;
					snode.lchild = dnode.lchild;
					if (!isContinuous) {
						snode.rchild = dnode.rchild;
					} else {
						dnode.lchild.parent = snode;
					}
				}
				dnode.parent = null;
				dnode.lchild = null;
				dnode.rchild = null;
				dnode = null;
			}

			return false;
		}

		public void printTreeByHeight() throws Exception {
			int height = getHeight();
			NodeList nlist[] = new NodeList[height];

			traverse(0, this.root, nlist);

			for (int h = 0; h < height; h++) {
				Node node = nlist[h].head;
				while (node != null) {
					System.out.print(node.key + "[" + (node.parent == null ? "r" : node.parent.key) + "] ");
					node = node.next;
				}
				System.out.println();
			}
		}

		private void traverse(int height, Node node, NodeList nlist[]) {

			node.next = null;

			if (nlist[height] == null)
				nlist[height] = new NodeList();
			nlist[height].addNode(node);

			if (node.lchild != null)
				traverse(height + 1, node.lchild, nlist);
			if (node.rchild != null)
				traverse(height + 1, node.rchild, nlist);
		}

		private int getHeight() {
			if (root == null)
				return 0;

			height = Integer.MIN_VALUE;

			getHeight(this.root, 0);

			return height;
		}

		int height = 0;

		private void getHeight(Node node, int h) {
			if (node == null) {
				if (h > height)
					height = h;
				return;
			}

			getHeight(node.lchild, h + 1);
			getHeight(node.rchild, h + 1);
		}

	}

	static class NodeList {
		Node head = null, tail = null;
		int size = 0;

		public void addNode(Node node) {
			if (size == 0) {
				head = tail = node;
				size++;
				return;
			}

			if (size == 1) {
				tail.next = node;
				tail = node;
				head.next = tail;
				size++;
				return;
			}

			tail.next = node;
			tail = node;
			tail.next = null;
			size++;
		}
	}

	static class Node {
		int key;

		// FOR TREE EXPRESSION
		Node lchild;
		Node rchild;
		Node parent;

		// FOR LINKTED LIST EXPRESSION
		Node next;

		public Node(int key) {
			this.key = key;
		}
	}

}
