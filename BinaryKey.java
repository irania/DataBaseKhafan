
public class BinaryKey implements Comparable<BinaryKey>{
	private BinaryKey leftChild;
	private BinaryKey rightChild;
	private BinaryKey dad;
	private String key;
	private String path;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public BinaryKey(String key){
		this.key=key;
		leftChild=null;
		rightChild=null;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public BinaryKey getRightChild() {
		return rightChild;
	}
	public void setRightChild(BinaryKey rightChild) {
		this.rightChild = rightChild;
	}
	public BinaryKey getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(BinaryKey leftChild) {
		this.leftChild = leftChild;
	}
	@Override
	public int compareTo(BinaryKey o) {
		// TODO Auto-generated method stub
		return this.key.compareTo(o.getKey());
	}
	public BinaryKey getDad() {
		return dad;
	}
	public void setDad(BinaryKey dad) {
		this.dad = dad;
	}
	
	public boolean hasLeftChild(BinaryKey key){
		if(leftChild !=null)
			return this.leftChild.equals(key);
		else
			return false;
	}
	
	public boolean hasRightChild(BinaryKey key){
		if(rightChild != null)
			return this.rightChild.equals(key);
		else
			return false;
	}
}