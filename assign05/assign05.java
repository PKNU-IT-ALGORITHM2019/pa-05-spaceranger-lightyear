import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class assign05 {
	public static Scanner inFile;
	public static assign05 bTree = new assign05();
	private Node root;

	private class Node {
		String word;
		String type;
		String meaning;

		Node leftChild;
		Node rightChild;

		Node(String word, String type, String meaning) {
			this.word = word;
			this.type = type;
			this.meaning = meaning;
		}

		public String toString() {
			return "word:" + this.word;
		}
	}

	public Node getRoot() {
		return this.root;
	}

	public void addNode(String word, String type, String meaning) {
		if (findNode(word) != null) return;

		Node newNode = new Node(word, type, meaning);

		if (root == null) {
			root = newNode;
		} else {
			Node focusNode = root;
			Node parent;

			while(true) {
				parent = focusNode;

				if (word.compareTo(parent.word) < 0) {
					focusNode = parent.leftChild;

					if (focusNode == null) {
						parent.leftChild = newNode;
						return;
					}
				} else {
					focusNode = parent.rightChild;

					if (focusNode == null) {
						parent.rightChild = newNode;
						return;
					}
				}
			}
		}
	}

	public boolean deleteNode(String str) {
		Node focusNode = root;
		Node parent = root;

		boolean isLeftChild = true;

		while(focusNode.word.compareTo(str) != 0) {
			parent = focusNode;

			if(str.compareTo(focusNode.word) < 0) {
				isLeftChild = true;
				focusNode = parent.leftChild;
			} else {
				isLeftChild = false;
				focusNode = parent.rightChild;
			}

			if(focusNode == null) {
				return false;
			}
		}


		Node replacementNode;
		if(focusNode.leftChild == null && focusNode.rightChild == null) {
			if (focusNode == root)
				root = null;
			else if (isLeftChild)
				parent.leftChild = null;
			else
				parent.rightChild = null;
		}

		else if(focusNode.rightChild == null) {
			replacementNode = focusNode.leftChild;

			if (focusNode == root)
				root = replacementNode;
			else if (isLeftChild)
				parent.leftChild = replacementNode;
			else
				parent.rightChild = replacementNode;
		}
		else if (focusNode.leftChild == null) {
			replacementNode = focusNode.rightChild;
			if (focusNode == root)
				root = replacementNode;
			else if (isLeftChild)
				parent.leftChild = replacementNode;
			else
				parent.rightChild = replacementNode;
		}

		else {
			Node rightSubTree = focusNode.rightChild;  
			replacementNode = getRightMinNode(focusNode.rightChild); 

			if (focusNode == root)
				root = replacementNode;
			else if (isLeftChild)
				parent.leftChild = replacementNode;
			else
				parent.rightChild = replacementNode;

			replacementNode.rightChild = rightSubTree;
			if (replacementNode == rightSubTree)
				replacementNode.rightChild = null;

			replacementNode.leftChild = focusNode.leftChild;
		}

		return true;
	}

	private Node getRightMinNode(Node rightChildRoot) {
		Node parent = rightChildRoot;
		Node focusNode = rightChildRoot;

		while (focusNode.leftChild != null) {
			parent = focusNode;
			focusNode = focusNode.leftChild;
		}

		parent.leftChild = null;
		return focusNode;
	}

	public void inOrderTraverse(Node focusNode) {
		if (focusNode != null) {
			inOrderTraverse(focusNode.leftChild);
			System.out.println("Word: " + focusNode.word);
			System.out.println("Class: " + focusNode.type);
			System.out.println("Meaning: " + focusNode.meaning + "\n");
			inOrderTraverse(focusNode.rightChild);
		}
	}

	public void preOrderTraverse(Node focusNode) {
		if (focusNode != null) {
			System.out.println("Word: " + focusNode.word);
			System.out.println("Class: " + focusNode.type);
			System.out.println("Meaning: " + focusNode.meaning + "\n");
			preOrderTraverse(focusNode.leftChild);
			preOrderTraverse(focusNode.rightChild);
		}
	}

	public void postOrderTraverse(Node focusNode) {
		if (focusNode != null) {
			postOrderTraverse(focusNode.leftChild);
			postOrderTraverse(focusNode.rightChild);
			System.out.println("Word: " + focusNode.word);
			System.out.println("Class: " + focusNode.type);
			System.out.println("Meaning: " + focusNode.meaning + "\n");
		}
	}

	public Node findNode(String word) {
		if (root == null) return null;

		Node focusNode = root;

		while (focusNode.word != word) {
			if (word.compareTo(focusNode.word) < 0) {
				focusNode = focusNode.leftChild;
			} else {
				focusNode = focusNode.rightChild;
			}

			if (focusNode == null)
				return null;
		}

		return focusNode;
	}

	public void BFS()
	{
		Queue<Node> q = new LinkedList<>();
		q.offer(root);
		while (!q.isEmpty()) {
			Node n = q.poll();
			System.out.print(n.word + " ");
			if (n.leftChild !=null)
				q.offer(n.leftChild);
			if (n.rightChild !=null)
				q.offer(n.rightChild);
		}
	}

	public void readFile()
	{
		String str;

		try {
			inFile = new Scanner(new File("/Users/spaceranger.lightyear/eclipse-workspace/Algorithm/assign05/file.txt"));

			while(inFile.hasNext()) {
				str = inFile.nextLine();

				if(str.equals( "" ))
					continue;

				int start = str.indexOf( "(" );
				int end = str.indexOf( ")" );

				if(start<0 || end < 0)
					return;

				String word = str.substring(0, start - 1);
				String type = str.substring(start + 1, end);
				String meaning = str.substring(end + 2);
				bTree.addNode(word, type, meaning);
			}
			inFile.close();
			System.out.println("Read successfully.");

		} catch (FileNotFoundException e) {
			System.out.println( "No such file." );
			System.exit(0);
		}
	}

	public void readDeleteAllFile(String uri) {
		String str;

		try {
			inFile = new Scanner(new File(uri));
			int deletedNode = 0;
			while(inFile.hasNext()) {
				str = inFile.nextLine();

				if (str.equals( "" ))
					continue;

				String word = str.trim();
				System.out.println(word);

				if (deleteNode(word)) {
					deletedNode++;
				}
			}
			inFile.close();
			System.out.println(deletedNode + " words were deleted successfully.");
		} catch (FileNotFoundException e) {
			System.out.println( "No such file. ");
			System.exit(0);
		}
	}

	public int getSize() {
		return getSize(bTree.getRoot());
	}

	private int getSize(Node focusNode) {
		if (focusNode != null) {
			return getSize(focusNode.leftChild) + 1 + getSize(focusNode.rightChild);
		}
		return 0;
	}

	public void findMeaning(String word) {
		System.out.println("Meaning: " + findNode(word).meaning);
	}

	public static void main(String[] args) {
		bTree.readFile();
		Scanner s = new Scanner(System.in);

		while (true) {
			String command = s.next();
			if (command.compareTo("size") == 0)
				System.out.println(bTree.getSize());
			else if (command.equals("find")) {
				command = s.next();
				bTree.findMeaning(command);
			}
			else if (command.equals("add")) {
				System.out.print("Word: ");
				String word = s.next();
				System.out.print("Class: ");
				String type = s.next();
				System.out.print("Meaning: ");
				String meaning = s.next();
				bTree.addNode(word, type, meaning);
			}
			else if (command.equals("delete")) {
				System.out.print("Input word to delete: ");
				String word = s.next();

				if(bTree.deleteNode(word))
					System.out.println("Deleted Successfully.");
				else
					System.out.println("Failed to delete.");
			}

			else if (command.equals("deleteall")) {
				System.out.print("Input file uri: ");
				String uri = s.next();
				bTree.readDeleteAllFile(uri);
			}

			else if (command.equals("bfs"))
				bTree.BFS();

			else if (command.equals("exit"))
				return;

			s.close();
		}
	}
}