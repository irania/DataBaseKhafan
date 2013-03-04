import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Key implements Comparable<Key>{
	
	private String key;
	private String left;
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String righte) {
		//System.out.println("line 813:"+this.key + " "+righte.toString());
		this.right = righte;
		//System.out.println("line 812:"+righte.isLeaf());
		
	}
	private String right;
	public Key(String key,String left,String right){
		this.key=key;
		this.left=left;
		this.right=right;
	}
	public int compareTo(Key o) {
		return this.key.compareTo(o.key);
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void createFile(String fileName,String directory) throws IOException {
		// TODO Auto-generated method stub
		String s = directory+"/"+this.key+".txt";
		//System.out.println("linux"+s);
	/*	final File file = new File(s);
		final File parent_directory = file.getParentFile();

		if (null != parent_directory)
		{
		    parent_directory.mkdirs();
		}*/

		FileWriter fstream = new FileWriter(s.toString());
		
		BufferedWriter out = new BufferedWriter(fstream);
		//System.out.println("salam");
		out.write(fileName+"\r\n");
		out.close();
		
	}
	public void updateFile(String fileName, String directory) throws IOException {
		File input = new File(directory+"/"+this.key+".txt");
		Scanner in = new Scanner (input);
		while(in.hasNext()){
			if(in.next().equals(fileName)){
				in.close();
				return;
			}
		}
		in.close();
		FileWriter fstream = new FileWriter(directory+"/"+this.key+".txt",true);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(fileName+"\r\n");
		out.close();
	}

}