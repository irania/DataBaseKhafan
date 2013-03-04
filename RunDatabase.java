import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class RunDatabase implements Database {
	public RunDatabase() {
		bptree = new BPlusTree();
		sptree = new SplayTree();
	}
	public void delete(String input) {
		ArrayList<String> finds;
		try {
			finds = bptree.found(input);
			
			for (int i = 0; i < finds.size(); i++) {
				File inputi = new File(finds.get(i));
				Scanner in;
				try {
					in = new Scanner(inputi);
					while (in.hasNext()) {
						try {
							bptree.delete(in.next());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// error dar soorate nasakhtan e btree
	}

	public void printBPlusTree() {
		

		try {
			bptree.print();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printSplayTree() {
		sptree.print();
	}

	private BPlusTree bptree;
	private SplayTree sptree;

	public void insert(String fileName) {
		File input = new File(fileName);
		Scanner in;
		 System.out.println("line 432:"+input.exists());

		try {
			in = new Scanner(input);
			
			while (in.hasNext()){
				System.out.println("ssss");
				bptree.insert(in.next(), fileName);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


		public void find(String input) {
			String[] inputs = input.split(" ");
			ArrayList<FoundFile> foundFilesForEachKey = new ArrayList<FoundFile>();
			ArrayList<FoundFile> sameFileKeys = new ArrayList<FoundFile>();
			for (int i = 0; i < inputs.length; i++) {
				BinaryKey tmpKey = sptree.findKey(inputs[i], false);
				Node tmpRoot = bptree.getRoot();
				if (tmpKey == null) {
					
					String path = treeSearch(input, tmpRoot, "./");
					System.out.println("path"+ path);
					if (path != null) {
						BinaryKey inserted = sptree.insertKey(input);
						inserted.setPath(path);
						Scanner scan;
						try {
							scan = new Scanner(new File(inserted.getPath()));
	
						//System.out.println("dddddd"+inserted.getPath()+scan.next());
						while (scan.hasNext()) {
							FoundFile tmp=new FoundFile();
							tmp.setName(scan.nextLine());
							foundFilesForEachKey.add(tmp);
							// System.out.println(scan.nextLine());
							
						}
						scan.close();
						checkForSameFiles(sameFileKeys,foundFilesForEachKey);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
//					System.out.println("else"+tmpKey.getPath());
					File result = new File(tmpKey.getPath());
					System.out.println("ddd"+result.exists());
					Scanner scan;
					try {
						scan = new Scanner(result);
						System.out.println("DD"+scan.hasNext());
						while (scan.hasNext()) {
							FoundFile tmp=new FoundFile();
							tmp.setName(scan.nextLine());
							foundFilesForEachKey.add(tmp);				
						}
						checkForSameFiles(sameFileKeys, foundFilesForEachKey);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//				sptree.deleteDownKeys(89);// deleting the unwanted and less
											// searched data
				foundFilesForEachKey.removeAll(foundFilesForEachKey);
			}
//			Collections.so
			Collections.sort(sameFileKeys);
			System.out.println(sameFileKeys);
		}

	

	private String treeSearch(String key, Node start, String path) {
		if (start.isLeaf()){
			System.out.println("in leaf");
			
			for (int i = 0; i < start.numberOfKeys(); i++) {
				System.out.println(start.getKeys().get(i).getKey());
				if (key.compareTo(start.getKeys().get(i).getKey()) == 0)
					return start.getDirectory()+"/"+key+".txt";
			}
			return null;
		}
		if (key.compareTo(start.getKeys().get(0).getKey()) < 0) {
			// update start and path
			// start should be the left child and so the path
		
			try {
				return treeSearch(key,bptree.createNode(start.getDirectory()+"/"+0), path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("not found!");
			}
		}
		for (int i = 0; i < start.numberOfKeys() - 1; i++) {
			System.out.println("iii"+ i);
			if (key.compareTo(start.getKeys().get(i).getKey()) >= 0)
				if (key.compareTo(start.getKeys().get(i + 1).getKey()) < 0 || key.compareTo(start.getKeys().get(i).getKey())==0) {
					// update start and path
					// start should be the right children
					try {
						return treeSearch(key,bptree.createNode(start.getDirectory()+"/"+(i+1)), path);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("not found!");
					}
				}
		}
		if (key.compareTo(start.getKeys().get(start.numberOfKeys() - 1)
				.getKey()) > 0) {
			// update start and path
			// start should be the right child
			try {
				System.out.println("cccc"+start.getDirectory());
				return treeSearch(key,bptree.createNode(start.getDirectory()+"/"+(start.numberOfKeys())), path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	private void checkForSameFiles(ArrayList<FoundFile> sameFileKeys,
			ArrayList<FoundFile> foundFilesForEachKey) {
		for (int i = 0; i < foundFilesForEachKey.size(); i++) {
			for (int j = 0; j <sameFileKeys.size(); j++) {
				if(foundFilesForEachKey.get(i).getName().compareTo(sameFileKeys.get(j).getName())==0){
					foundFilesForEachKey.get(i).setPriority();
					sameFileKeys.add(foundFilesForEachKey.get(i));
					foundFilesForEachKey.remove(i);
				}
			}
		}
		sameFileKeys.addAll(foundFilesForEachKey);
	}

}