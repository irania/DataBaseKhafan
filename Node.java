import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Node {
	private ArrayList<Key> keys;
	private String dad;
	private String fileName;
	private String directory;
	private boolean isLeaf;

	public Node() {
		keys = new ArrayList<Key>();
		isLeaf = true;
	}

	public void deleteKey(String key) throws NotFoundException, IOException {
		int index = findKey(key);
		if (index == -1) {
			throw new NotFoundException();
		}
		keys.remove(index);
		File keyFile = new File(this.directory+"/"+key+".txt");
		keyFile.delete();
		this.updateFile();
	}

	public int findKey(String key) {
		int imax = keys.size() - 1;
		int imin = 0;
		while (imax >= imin) {
			/* calculate the midpoint for roughly equal partition */
			int imid = (imin + imax) / 2;

			// determine which subarray to search
			if (keys.get(imid).getKey().compareTo(key) < 0)
				// change min index to search upper subarray
				imin = imid + 1;
			else if (keys.get(imid).getKey().compareTo(key) > 0)
				// change max index to search lower subarray
				imax = imid - 1;
			else
				// key found at index imid
				return imid;
		}
		return -1;
	}

	public int insertKey(String key, String fileName, String path) throws IOException {
		Key tmp = new Key(key, null, null);
		boolean inIf = false;
		int i = 0;
		for (i = 0; i < keys.size(); i++) {
			if (keys.get(i).compareTo(tmp) >= 0) {
				if (!keys.get(i).getKey().equals(key)) {
					
					keys.add(i, tmp);
					// create file
					tmp.createFile(fileName,path);
				} else {
					tmp.updateFile(fileName,path);
					updateFile();
					return -1;
				}
				inIf = true;
				break;
			}
		}
		if (!inIf) {
			keys.add(tmp);
			tmp.createFile(fileName,path);
		}
		updateFile();
		return i;

	}

	public void updateFile() throws IOException {
		FileWriter fstream = new FileWriter(this.getFileName());
		BufferedWriter out = new BufferedWriter(fstream);

		if(isLeaf)
			out.write("l\r\n");
		else
			out.write("n\r\n");
		for (int i = 0; i < keys.size(); i++) {
		//	System.out.println("line 543:"+ i +" "+ keys.get(i).getKey());
			out.write(keys.get(i).getKey() + "\r\n");
		}
		out.close();

	}

	public int numberOfKeys() {
		if (keys == null)
			return 0;
//		// System.out.println("line 400"+keys.toString());
		return keys.size();
	}

	public boolean isLeaf() {
	//	System.out.println(isLeaf);
		return isLeaf;
	}

	@Override
	public String toString() {
		String s = "Node [keys=";
		for (int i = 0; i < this.numberOfKeys(); i++)
			s += keys.get(i).getKey() + " , ";
		s += "]";
		return s;
	}

	public Node someWhereIBelong(String key, String path) throws FileNotFoundException {
		for (int i = 0; i < keys.size(); i++) {
			if (keys.get(i).getKey().compareTo(key) > 0) {
				if(!this.isLeaf())
					path+="/"+(i);
	//			System.out.println("line 451"+path+keys.get(i).getKey()+" "+this.toString() );
				return createNode(path);
			}

		}
		if (this.numberOfKeys() != 0){
			if(!this.isLeaf())
				path+="/"+numberOfKeys();
			return createNode(path);
		}
		else
			return this;
	}

	public String getDad() {
		return dad;
	}

	public void setDad(String dad) {
		this.dad = dad;
	}

	public List<Key> getKeys() {
		return keys;
	}

	public void setKeys(List<Key> keys) {
		ArrayList<Key> m = new ArrayList<Key>();
		for (int i = 0; i < keys.size(); i++)
			m.add(keys.get(i));
		this.keys.clear();
		this.keys.addAll(m);
	}
/*
	public void fromRightSiblingInsert() throws FileNotFoundException {
		Node rightSibil = createNode(this.rightSibling);
		this.keys.add(rightSibil.getKeys().get(0));
		try {
			rightSibil.deleteKey(rightSibil.getKeys().get(0).getKey());
			rightSibil.getKeys().get(0).setLeft(null);// may need more
														// observation
		} catch (NotFoundException e) {
			System.err.println("something is wrong");

		}
	}
*/
	public void addKeys(List<Key> keys) {
		ArrayList<Key> m = new ArrayList<Key>();
		for (int i = 0; i < keys.size(); i++)
			m.add(keys.get(i));
		this.keys.addAll(m);
	}

	public Key getMom() throws FileNotFoundException {
		Node dady = createNode(this.dad);
		
		int index = -1;
		for (int i = 0; i < dady.numberOfKeys(); i++) {
			if ((dady.getDirectory()+"/"+dady.getKeys().get(i).getRight()) .equals( this.getFileName())) {
				index = i;
//				break;
			}
		}
		if(index!=-1){
			return dady.getKeys().get(index);
		}
		else{
			return null;
		}
			
	}

	public Key getRealDad() throws FileNotFoundException {
		int index = -1;
		
		Node dady = createNode(this.dad);
		
		for (int i = 0; i < dady.getKeys().size(); i++) {
			//System.out.println(dady.getDirectory()+"/"+dady.getKeys().get(i).getLeft()+" "+fileName);
			if ((dady.getDirectory()+"/"+dady.getKeys().get(i).getLeft()) .equals (this.fileName)) {
				index = i;
				//System.out.println("maaaaaaaaan"+i);

				break;
			}
		}
		if (index !=-1)
			return dady.getKeys().get(index );
		else
			return null;
	}

	public Node createNode(String filename) throws FileNotFoundException {
	//	System.out.println("line 904"+filename+"/0.txt");
		File inputi = new File(filename+"/0.txt");
//		if(fileName.indexOf("0.txt")!=-1)
//			input = new File(fileName);
//		else
//			input = new File(fileName+"/0.txt");
		Scanner in = new Scanner(inputi);
		Node current = new Node();
		//current.setrightSiblin(in.next());
		
		String l = in.next();
		if(l.equals("l"))
			current . setIsLeaf (true);
		else
			current . setIsLeaf (false);
		//System.out.println("line 914"+filename);
		//System.out.println("line 948"+current.toString()+ " "+current.isLeaf());
		current.setDad(dadDirectory(filename));
		//System.out.println("line 938"+current.toString()+ " "+filename);
		String leftchildKey = "0/0.txt";
		String rightchildKey;
		Integer  k =1;
		ArrayList<Key> tmpKeys = new ArrayList<Key>();
		while (in.hasNext()) {
			Key tmp = new Key(in.next(), "*", "*");
			tmp.setLeft(leftchildKey);
			rightchildKey = k.toString()+"/0.txt";
			tmp.setRight(rightchildKey);
			leftchildKey = rightchildKey;
			tmpKeys.add(tmp);
			k++;
		}
		current.setKeys(tmpKeys);
		current.setDirectory(filename);
		//System.out.println("line 908"+current.toString()+ " "+filename);
		current.setFileName(filename+"/0.txt");
		in.close();
		return current;
	}

	private String retDirectory(String filename) {
		String a = filename;
		
		a.substring(0,a.lastIndexOf("/0.txt"));
		return a;
	}

	public void setIsLeaf(boolean b) {
		// TODO Auto-generated method stub
		isLeaf = b;
	}

	public String dadDirectory(String fileName) {
		int a =fileName.lastIndexOf("/");
		if(a==-1)
			return fileName;
		String p = fileName.substring(0,a);
		return p;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public int insertIndexKey(String key) {
		
		Key tmp = new Key(key, null, null);
		boolean inIf = false;
		int i = 0;
		for (i = 0; i < keys.size(); i++) {
			if (keys.get(i).compareTo(tmp) > 0)  {
				//System.out.println("line 123"+this.toString()+" "+i+" "+tmp);
					keys.add(i, tmp);
					inIf = true;
					break;
			}
		}
		if (!inIf) 
			keys.add(tmp);
		
		return i;
		
	}

	public void changeDF(String fileName2, int i) {
		// TODO Auto-generated method stub
		
	}

	public void deleteIndexKey(String key) throws NotFoundException, IOException {
		int index = findKey(key);
		if (index == -1) {
			throw new NotFoundException();
		}
		keys.remove(index);
		this.updateFile();
		
	}

	public void decreaseDirectories() {
		// TODO Auto-generated method stub
		for(int i=0 ;i<=this.keys.size()+1;i++){
			File tmp = new File(this.getDirectory()+"/"+i);
			tmp.renameTo(new File(this.getDirectory()+"/"+(i-1)));
		}
			
	}

	public void increaseDirectories() {
		// TODO Auto-generated method stub
		for(int i=this.keys.size()-1 ;i>=0;i--){
			File tmp = new File(this.getDirectory()+"/"+i);
		//	System.out.println("in increase directory"+this.getDirectory()+"/"+i);
			tmp.renameTo(new File(this.getDirectory()+"/"+(i+1)));
		}
		
	}

	public void decreaseFrom(int from, String directory  ) {
		// TODO Auto-generated method stub
	//	System.out.println(""+from+"ff"+numberOfKeys()+toString());
		for(int i=from+1 ; i<=this.numberOfKeys() ; i++){
			File a = new File(directory+"/"+ i);
			System.out.println("dddddddddddd"+directory+"/"+ (i-1));
			a.renameTo(new File(directory+"/"+ (i-1)));
		}
			
	}


}