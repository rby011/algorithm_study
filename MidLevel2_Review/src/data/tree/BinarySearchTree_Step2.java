package data.tree;

import data.tree.BinarySearchTree_Step1.BST;

public class BinarySearchTree_Step2 {
	public static void main(String args[]) {
		int keys[] = { 15, 8, 28, 18, 45, 41, 48, 30, 50, 38, 33 };

		BST btree = new BST();
		for (int i = 0; i < keys.length; i++) {
			btree.insert(keys[i]);
		}

		btree.printTreeByHeight();
		System.out.println();

		btree.delete(18);
		btree.printTreeByHeight();
		System.out.println();
	}

	static class BST {
		Node root;

		public void insert(int key) {
			Node nnode = new Node(key);

			if (root == null) {
				this.root = nnode;
				return;
			}

			Node node = root, pnode = null;
			while (node != null) {
				pnode = node;
				if (node.key == key) {
					// UPDATE VALUE
					return;
				} else if (key < node.key) {
					node = node.lchild;
				} else {
					node = node.rchild;
				}
			}

			if (key < pnode.key) {
				pnode.lchild = nnode;
			} else {
				pnode.rchild = nnode;
			}
		}

		public boolean delete(int key) {
			Node dnode = this.root, dpnode = null;
			boolean isLeft = false;

			while (dnode != null && dnode.key != key) {
				dpnode = dnode;
				if (key < dnode.key) {
					isLeft = true;
					dnode = dnode.lchild;
				} else if (key > dnode.key) {
					isLeft = false;
					dnode = dnode.rchild;
				}
			}

			if (dnode == null)
				return false;

			if (dnode.lchild == null && dnode.rchild == null) {
				if (dnode == this.root) {
					this.root = null;
				} else {
					if (isLeft) {
						dpnode.lchild = null;
					} else {
						dpnode.rchild = null;
					}
					dnode = null;
				}
			} else if (dnode.lchild != null && dnode.rchild == null) {
				if (dnode == this.root) {
					this.root = dnode.lchild;
					dnode.lchild = null;
				} else {
					if (isLeft) {
						dpnode.lchild = dnode.lchild;
					} else {
						dpnode.rchild = dnode.lchild;
					}
					dnode.lchild = null;
				}
				dnode = null;
			} else if (dnode.lchild == null && dnode.rchild != null) {
				if (dnode == this.root) {
					this.root = dnode.rchild;
					dnode.rchild = null;
				} else {
					if (isLeft) {
						dpnode.lchild = dnode.rchild;
					} else {
						dpnode.rchild = dnode.rchild;
					}
					dnode.rchild = null;
				}
				dnode = null;
			} else {
				Node snode = dnode.rchild, spnode = dnode;
				boolean iscontinuous = false;

				while (snode.lchild != null) {
					spnode = snode;
					snode = snode.lchild;
				}

				if (spnode == dnode) {
					iscontinuous = true;
				} else {
					if (snode.rchild != null) {
						spnode.lchild = snode.rchild;
					} else {
						spnode.lchild = null;
					}
				}

				if (dnode == this.root) {
					if (iscontinuous) {
						snode.lchild = dnode.lchild;
					} else {
						snode.lchild = dnode.lchild;
						snode.rchild = dnode.rchild;
					}
					this.root = snode;
				} else {
					if (isLeft) {
						dpnode.lchild = snode;
					} else {
						dpnode.rchild = snode;
					}
					snode.lchild = dnode.lchild;
					if (!iscontinuous) {
						snode.rchild = dnode.rchild;
					}
				}

				dnode.lchild = null;
				dnode.rchild = null;
				dnode = null;
			}

			return false;
		}

		public Node find(int key) {
			Node node = this.root;

			while (node.key != key) {
				if (key < node.key) {
					node = node.lchild;
				} else if (key > node.key) {
					node = node.rchild;
				} else {
					return node;
				}
			}

			return null;
		}

		public void printTreeByHeight() {
			int height = getheight(this.root, 0);

			NodeList nlist[] = new NodeList[height];
			traverse(this.root, 0, nlist);

			for (int h = 0; h < height; h++) {
				Node node = nlist[h].head;
				while (node != null) {
					int lkey = node.lchild == null ? 0 : node.lchild.key;
					int rkey = node.rchild == null ? 0 : node.rchild.key;
					System.out.print(node.key + "[l:" + lkey + ", r:" + rkey + "] ");
					node = node.next;
				}
				System.out.println();
			}

		}

		private void traverse(Node node, int height, NodeList nlist[]) {
			if (nlist[height] == null)
				nlist[height] = new NodeList();
			nlist[height].addNode(node);

			if (node.lchild != null)
				traverse(node.lchild, height + 1, nlist);
			if (node.rchild != null)
				traverse(node.rchild, height + 1, nlist);
		}

		private int getheight(Node node, int height) {
			if (node == null) {
				return height;
			}

			int h1 = getheight(node.lchild, height + 1);
			int h2 = getheight(node.rchild, height + 1);

			if (h1 > h2) {
				return h1;
			} else {
				return h2;
			}
		}
	}

	static class Node {
		int key;
		Node lchild;
		Node rchild;

		Node next;

		public Node(int key) {
			this.key = key;
		}
	}

	static class NodeList {
		Node head = null, tail = null;
		int size = 0;

		public void addNode(Node node) {
			node.next = null;

			if (size == 0) {
				head = tail = node;
				size++;
				return;
			}

			if (size == 1) {
				tail = node;
				head.next = tail;
				size++;
				return;
			}

			tail.next = node;
			tail = tail.next;
			size++;
		}
	}

}
