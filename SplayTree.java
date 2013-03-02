import java.util.ArrayList;

public class SplayTree {
	private BinaryKey root;
	private int maxHeight;

	public SplayTree() {
		root = null;
	}

	private void zigZigStep(BinaryKey shouldBeRoot, boolean left) {
		if (left) {
			rotateRight(shouldBeRoot.getDad().getDad());
			rotateRight(shouldBeRoot.getDad());
		} else {
			rotateLeft(shouldBeRoot.getDad().getDad());
			rotateLeft(shouldBeRoot.getDad());
		}

	}

	private void zigZagStep(BinaryKey shouldBeRoot, boolean left) {
		System.out.println("left?: " + left);
		if (left) {

			rotateLeft(shouldBeRoot.getDad());
			rotateRight(shouldBeRoot.getDad());
		} else {
			rotateLeft(shouldBeRoot.getDad());
			rotateRight(shouldBeRoot.getDad());
		}
		System.out.println("after zig zag");
		print();
		System.out.println("/////////////");
	}

	private void zigStep(BinaryKey shouldBeRoot, boolean left) {
		if (left) {
			rotateRight(shouldBeRoot.getDad());
		} else {
			rotateLeft(shouldBeRoot.getDad());
		}
	}

	public BinaryKey insertKey(String key) {
		BinaryKey newKey = findPlace(key);
		if (newKey != root) {
			root = splaying(newKey);
		}
		return root;
	}

	private BinaryKey splaying(BinaryKey newKey) {
		// print();
		if (newKey.equals(root)) {
			return newKey;
		}
		boolean left = true;
		if (needsZigZagLeft(newKey)) {
			zigZagStep(newKey, left);
		} else if (needsZigZagRight(newKey)) {
			zigZagStep(newKey, !left);
		} else if (needsZigRight(newKey)) {
			zigStep(newKey, !left);
		} else if (needsZigLeft(newKey)) {
			zigStep(newKey, left);
		} else if (needsZigZigRight(newKey)) {
			zigZigStep(newKey, !left);
		} else if (needsZigZigLeft(newKey)) {
			zigZigStep(newKey, left);
		}
		return splaying(newKey);

	}

	private boolean needsZigZigLeft(BinaryKey newKey) {
		if (newKey.getDad() != null && newKey.getDad().getDad() != null)
			if (newKey.getDad().hasLeftChild(newKey)) {
				if (newKey.getDad().getDad().hasLeftChild(newKey.getDad()))
					return true;
			}
		return false;
	}

	private boolean needsZigZigRight(BinaryKey newKey) {
		if (newKey.getDad() != null && newKey.getDad().getDad() != null)
			if (newKey.getDad().hasRightChild(newKey))
				if (newKey.getDad().getDad().hasRightChild(newKey.getDad()))
					return true;
		return false;
	}

	private boolean needsZigLeft(BinaryKey newKey) {
		if (newKey.getDad() != null)
			if (newKey.getDad() == root && newKey.getDad().hasLeftChild(newKey))
				return true;
		return false;
	}

	private boolean needsZigRight(BinaryKey newKey) {
		if (newKey.getDad() != null)
			if (newKey.getDad() == root
					&& newKey.getDad().hasRightChild(newKey))
				return true;
		return false;
	}

	private boolean needsZigZagRight(BinaryKey newKey) {
		if (newKey.getDad() != null && newKey.getDad().getDad() != null)
			if (newKey.getDad().hasLeftChild(newKey)) {
				if (newKey.getDad().getDad().hasRightChild(newKey.getDad()))
					return true;
			}
		return false;
	}

	private boolean needsZigZagLeft(BinaryKey newKey) {
		if (newKey.getDad() != null && newKey.getDad().getDad() != null)
			if (newKey.getDad().hasRightChild(newKey)) {
				if (newKey.getDad().getDad().hasLeftChild(newKey.getDad())) {
					System.out.println("parent in conditioning: "
							+ newKey.getDad().getKey());
					System.out.println("grandparent in conditioning: "
							+ newKey.getDad().getDad().getKey());
					return true;
				}
			}
		return false;
	}

	private BinaryKey findPlace(String key) {
		// if (start.getLeftChild() == null && start.getRightChild() == null) {
		// return start;
		// }
		// if (start.getKey().compareTo(key) < 0)
		// start = start.getRightChild();
		// else
		// start = start.getLeftChild();
		// return findPlace(start, key);
		BinaryKey currentNode = root;
		if (root == null) {
			root = new BinaryKey(key);
			return root;
		}
		while (currentNode != null) {
			if (currentNode.getKey().compareTo(key) < 0) {
				if (currentNode.getRightChild() == null) {
					BinaryKey newKey = new BinaryKey(key);
					currentNode.setRightChild(newKey);
					newKey.setDad(currentNode);
					return newKey;
				} else {
					currentNode = currentNode.getRightChild();
					continue;
				}
			} else if (currentNode.getKey().compareTo(key) > 0) {
				if (currentNode.getLeftChild() == null) {
					BinaryKey newKey = new BinaryKey(key);
					currentNode.setLeftChild(newKey);
					newKey.setDad(currentNode);
					return newKey;
				} else {
					currentNode = currentNode.getLeftChild();
					continue;
				}
			}

		}
		return null;
	}

	public BinaryKey findKey(String key, boolean delete) {
		BinaryKey currentNode = this.root;
		while (currentNode != null) {
			if (currentNode.getKey().compareTo(key) == 0) {
				if (!delete)
					splaying(currentNode);
				return currentNode;
			} else if (key.compareTo(currentNode.getKey()) < 0)
				currentNode = currentNode.getLeftChild();
			else
				currentNode = currentNode.getRightChild();
		}
		return null;
	}

	public void deleteDownKeys(int h) {
		int height = 0;
		this.maxHeight = h;
		BinaryKey tmp = root;
		tmp = keyAtHeight(root, height);
		if (tmp != null) {

			tmp.setRightChild(null);
			tmp.setLeftChild(null);
		}
	}

	private BinaryKey keyAtHeight(BinaryKey start, int height) {
		if (height == maxHeight) {
			return start;
		}
		if (start.getLeftChild() != null) {
			BinaryKey tmp = keyAtHeight(start.getLeftChild(), height + 1);
			if (tmp != null)
				return tmp;
		}
		if (start.getRightChild() != null) {
			BinaryKey tmp = keyAtHeight(start.getRightChild(), height + 1);
			if (tmp != null)
				return tmp;
		}
		return null;
	}

	private void rotateRight(BinaryKey piot) {
		BinaryKey badChild = piot.getLeftChild();
		BinaryKey transferedChild = badChild.getRightChild();
		piot.setLeftChild(transferedChild);
		// System.out.println("piot's left child has changed to bad child's
		// right in right rotate");
		// print();
		if (transferedChild != null)
			transferedChild.setDad(piot);
		badChild.setDad(piot.getDad());
		if (piot.getDad() == null)
			root = badChild;
		else if (piot.getDad().hasLeftChild(piot))
			piot.getDad().setLeftChild(badChild);
		else
			piot.getDad().setRightChild(badChild);
		// System.out.println("bad child is taking over");
		// print();
		badChild.setRightChild(piot);
		piot.setDad(badChild);
	}

	private void rotateLeft(BinaryKey piot) {
		BinaryKey badChild = piot.getRightChild();
		BinaryKey transferedChild = badChild.getLeftChild();
		piot.setRightChild(transferedChild);
		// System.out.println("piot's right child is changed to bad child's left
		// in left rotate");
		// print();
		if (transferedChild != null)
			transferedChild.setDad(piot);
		// System.out.println("bad child is now taking over");
		badChild.setDad(piot.getDad());
		if (piot.getDad() == null)
			root = badChild;
		else if (piot.getDad().hasLeftChild(piot))
			piot.getDad().setLeftChild(badChild);
		else
			piot.getDad().setRightChild(badChild);
		badChild.setLeftChild(piot);
		// print();
		piot.setDad(badChild);
	}

	public void deleteKey(String key) {
		boolean delete = true;
		BinaryKey toBeDeleted = this.findKey(key, delete);
		if (toBeDeleted.getLeftChild() != null
				&& toBeDeleted.getRightChild() != null) {
			BinaryKey tmp=toBeDeleted;
			String trick = toBeDeleted.getKey();
			String trickPath=toBeDeleted.getPath();
			if (toBeDeleted.getRightChild().getLeftChild() != null){
				tmp=utmostleft(toBeDeleted.getRightChild());
				trick = tmp.getKey();
				trickPath=tmp.getPath();
			}
			else if (toBeDeleted.getLeftChild().getRightChild() != null){
				tmp = utmostRight(toBeDeleted.getLeftChild());
				trick=tmp.getKey();
				trickPath=tmp.getPath();
			}
			toBeDeleted.setKey(trick);
			toBeDeleted.setPath(trickPath);
			deleteKey(trick);
			splaying(toBeDeleted.getDad());
		} else if (toBeDeleted.getRightChild() != null
				&& toBeDeleted.getLeftChild() == null) {
			if (toBeDeleted.getDad().hasLeftChild(toBeDeleted))
				toBeDeleted.getDad().setLeftChild(toBeDeleted.getRightChild());
			else if (toBeDeleted.getDad().hasRightChild(toBeDeleted))
				toBeDeleted.getDad().setRightChild(toBeDeleted.getRightChild());
			splaying(toBeDeleted.getDad());
		} else if (toBeDeleted.getRightChild() == null
				&& toBeDeleted.getLeftChild() != null) {
			if (toBeDeleted.getDad().hasLeftChild(toBeDeleted))
				toBeDeleted.getDad().setLeftChild(toBeDeleted.getLeftChild());
			else if (toBeDeleted.getDad().hasRightChild(toBeDeleted))
				toBeDeleted.getDad().setRightChild(toBeDeleted.getLeftChild());
			splaying(toBeDeleted.getDad());
		} else if (toBeDeleted.getRightChild() == null
				&& toBeDeleted.getLeftChild() == null) {
			if (toBeDeleted.getDad().hasLeftChild(toBeDeleted))
				toBeDeleted.getDad().setLeftChild(null);
			else if (toBeDeleted.getDad().hasRightChild(toBeDeleted))
				toBeDeleted.getDad().setRightChild(null);
			splaying(toBeDeleted.getDad());
		}
	}

	private BinaryKey utmostRight(BinaryKey leftChild) {
		BinaryKey tmp = leftChild;
		while (tmp.getRightChild() != null) {
			tmp = tmp.getRightChild();
		}

		return tmp;
	}

	private BinaryKey utmostleft(BinaryKey rightChild) {
		BinaryKey tmp = rightChild;
		while (tmp.getLeftChild() != null) {
			tmp = tmp.getLeftChild();
		}
		return tmp;
	}

	public void print() {
		ArrayList<BinaryKey> nodes = new ArrayList<BinaryKey>();
		nodes.add(root);
		System.out.println("root: " + root.getKey());
		while (!nodes.isEmpty()) {
			System.out.println(nodes.get(0).getKey());
			if (nodes.get(0).getLeftChild() != null) {
				nodes.add(nodes.get(0).getLeftChild());
				System.out.println("check left child: "
						+ nodes.get(0).getLeftChild().getKey());
			}
			if (nodes.get(0).getRightChild() != null) {
				nodes.add(nodes.get(0).getRightChild());
				System.out.println("check right child: "
						+ nodes.get(0).getRightChild().getKey());
			}
			nodes.remove(0);
		}
	}
}