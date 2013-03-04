
public class FoundFile implements Comparable<FoundFile> {
	private String name;
	private int priority;
	
	public FoundFile(){
		priority=0;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority() {
		this.priority = priority++;
	}
	@Override
	public int compareTo(FoundFile o) {
		return -this.priority+o.getPriority();
	}
	
	public String toString(){
		return this.name;
	}
	
}
