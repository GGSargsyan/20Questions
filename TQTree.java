/**
 * TQTree.java
 * A Java class that supports a Binary Tree that plays the game of twenty questions
 * @date May 11, 2014
 */
import java.io.*;
import java.util.Scanner;
import java.text.ParseException;
import java.util.LinkedList;

public class TQTree 
{
	private TQNode root;
	LinkedList<TQNode> queue;
	LinkedList<TQNode> printer;

	/** Inner class that supports a node for a twenty questions tree.
	 * You should not need to change this class. */
	class TQNode 
	{
		/*  You SHOULD NOT add any instance variables to this class */
		TQNode yesChild;  // The node's right child
		TQNode noChild;   // The node's left child
		String data;      // A question (for non-leaf nodes) 
		// or an item (for leaf nodes)

		int idx;        // index used for printing

		/** Make a new TWNode 
		 * @param data The question or answer to store in the node. 
		 */
		public TQNode( String data )
		{
			this.data = data;
		}    

		/** Setter for the noChild 
		 * @param noChild The new left (no) child
		 */
		public void setNoChild( TQNode noChild )
		{
			this.noChild = noChild;
		}


		/** Setter for the yesChild 
		 * @param yesChild The new right (yes) child
		 */
		public void setYesChild( TQNode yesChild )
		{
			this.yesChild = yesChild;
		}


		/** Getter for the yesChild 
		 * @return The node's yes (right) child
		 */
		public TQNode getYesChild()
		{
			return this.yesChild;
		}

		/** Getter for the noChild 
		 * @return The node's no (left) child
		 */
		public TQNode getNoChild()
		{
			return this.noChild;
		}

		/** Getter for the data
		 * @return The data stored in this node
		 */
		public String getData()
		{
			return this.data;
		}

		/** Setter for the index
		 * @param idx index of this for printing 
		 */
		public void setIndex(int idx) 
		{
			this.idx = idx;
		}

		/** get the index
		 * @return idx index of this for printing 
		 */
		public int getIndex()
		{
			return this.idx;
		}
	}  // End TQNode


	/** Builds a starter TQ tree with 1 question and 2 answers */
	public TQTree()
	{
		// Makes the default Tree
		makeDefaultTree();
	}

	/** Builds a new TQTree by reading from a file 
	 * @param filename The file containing the tree
	 * */
	public TQTree( String filename )
	{
		File f = new File( filename );
		LineNumberReader reader = null;
		try 
		{
			reader = new LineNumberReader( new FileReader( f ));
		} 
		catch ( FileNotFoundException e ) 
		{
			System.err.println( "Error opening file " + filename );
			System.err.println( "Building default Question Tree." );

			//makes default tree
			makeDefaultTree();
		}

		try 
		{
			// Attempts to build a tree
			root = buildSubtree(reader);
		}
		catch ( ParseException e)
		{
			makeDefaultTree();
		}

		try 
		{
			reader.close();
		} 
		catch ( IOException e ) 
		{
			System.err.println( "An error occured while closing file " + filename );
		}

	}

	/** Builds a new TQNode and sets its Yes and No child
	 * @return TQNode root that has two children
	 * */
	private TQNode makeDefaultTree()
	{
		root = new TQNode("Is it bigger than a breadbox");
		root.setYesChild(new TQNode ("a computer scientist"));
		root.setNoChild(new TQNode ("spam"));
		return root;
	}



	/** Play one round of the game of Twenty Questions using this game tree 
	 * Augments the tree if the computer does not guess the right answer
	 */ 
	public void play()
	{
		// Sets current node
		TQNode curr = root;

		// Uses scanner to read
		Scanner scnr = new Scanner(System.in);
		String input;

		// Keep updating curr with input answers until it reaches a leaf
		while (curr.getNoChild() != null && curr.getYesChild() != null)
		{
			System.out.println(curr.getData());

			input = scnr.nextLine();
			// If no
			if ( input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
			{
				// Make curr the No child
				curr = curr.getNoChild();
			}
			// If yes
			else if ( input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
			{
				// Make curr the Yes child
				curr = curr.getYesChild();
			}
		}
		// Asks if it's right
		System.out.println("Is it " + curr.getData() + " ?");
		input = scnr.nextLine();

		//Check if it is a leaf and if so print out the data of curr
		if ( input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
		{
			System.out.println("I win!");
		}
		else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
		{
			// Save original guess
			TQNode oldAnswer = new TQNode(curr.getData()); 
			System.out.println("What is it?");
			// Contains the new item
			input = scnr.nextLine(); 
			// New item saved in new Node
			TQNode newAnswer = new TQNode(input); 

			System.out.println("Please give me a yes or no question.");
			// Gives it a new question
			input = scnr.nextLine(); 
			curr.data = input;

			System.out.println("And what is the answer for you object?");
			// Gives it a new question
			input = scnr.nextLine(); 
			//Determines the structure of the children
			if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
			{
				curr.setYesChild(newAnswer);
				curr.setNoChild(oldAnswer);
			}
			else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
			{
				curr.setNoChild(newAnswer);
				curr.setYesChild(oldAnswer);
			}
		}
	}

	/** Save this Twenty Questions tree to the file with the given name
	 * @param filename The name of the file to save to
	 * @throws FileNotFoundException If the file cannot be used to save the tree
	 */
	public void save( String filename ) throws FileNotFoundException
	{
		// Exception if the file is empty or not there
		if (filename == null)
			throw new FileNotFoundException();

		PrintStream output = new PrintStream(filename);
		PrintStream consoleOut = new PrintStream(filename);

		System.setOut(output);
		// LinkedList queue
		printer = new LinkedList<TQNode>();
		TQNode adder = root;
		//adds root to the queue
		printer.add(adder);
		// Keeps going while queue isn't empty
		while(printer.size() != 0)
		{
			// removes head of queue and gives value to adder
			adder = printer.remove();
			// So long as it has a child
			if(adder.getNoChild() != null)
			{
				// get adder's two children
				printer.add(adder.getNoChild());
				printer.add(adder.getYesChild());
				//Print out tree
				System.out.println(adder.getIndex() + ":   " + 
						adder.getData() +  "  no:(" + 
						adder.getNoChild().getIndex() + 
						")  yes:(" + adder.getYesChild().getIndex()
						+ ")");
			}
			else
			{
				//Prints out last leaves
				System.out.println(adder.getIndex() + ":   " + 
						adder.getData() +  "  no:(null)  yes:(null)");
			}
		}
		
		System.setOut(consoleOut);
	}


	/** Print a level-order traversal of the tree to standard out (System.out)
	 * */ 
	public void print()
	{
		int idx = 0;
		// set current to root
		TQNode curr = root;
		// checks if empty
		if (curr == null)
		{
			System.out.println("It's empty!");
			return;
		}
		// LinkedList queue
		queue = new LinkedList<TQNode>();
		// adds root to queue
		queue.add(curr);
		// keep going while queue not empty
		while(queue.size() != 0)
		{
			// removes head of queue and gives value to curr
			curr = queue.remove();
			curr.setIndex(idx);
			if (curr.getNoChild() != null )
			{
				queue.add(curr.getNoChild());
				queue.add(curr.getYesChild());
			}

			idx++;
		}

		printer = new LinkedList<TQNode>();
		TQNode adder = root;
		printer.add(adder);

		while(printer.size() != 0)
		{
			adder = printer.remove();
			if(adder.getNoChild() != null)
			{
				// get adder's two children
				printer.add(adder.getNoChild());
				printer.add(adder.getYesChild());
				System.out.println(adder.getIndex() + ":   " + 
						adder.getData() +  "  no:(" + 
						adder.getNoChild().getIndex() + 
						")  yes:(" + adder.getYesChild().getIndex()
						+ ")");
			}
			else
			{
				//Prints out last leaves
				System.out.println(adder.getIndex() + ":   " + 
						adder.getData() +  "  no:(null)  yes:(null)");
			}
		}	
	}

	// PRIVATE HELPER METHODS
	// You will likely want to add a few more private helper methods here.    

	/** Recursive method to build a TQTree by reading from a file.
	 * @param reader A LineNumberReader that reads from the file
	 * @return The TQNode at the root of the created tree
	 * @throws ParseException If the file format is incorrect
	 */
	private TQNode buildSubtree( LineNumberReader reader ) throws ParseException 
	{

		String line;
		try
		{
			line = reader.readLine();
		}
		catch ( IOException e ) 
		{
			throw new ParseException( "Error reading tree from file: " + e.getMessage(),
					reader.getLineNumber() );
		}

		if ( line == null ) 
		{
			// We should never be calling this method if we don't have any more input
			throw new ParseException( "End of file reached unexpectedly", reader.getLineNumber() );
		}

		String[] lineSplit = line.split( ":", 2 );
		String qOrA = lineSplit[0];
		String data = lineSplit[1];

		TQNode subRoot = new TQNode( data );
		if ( qOrA.equals( "Q" ) ) 
		{
			subRoot.setNoChild( buildSubtree( reader ) );
			subRoot.setYesChild( buildSubtree( reader ) );
		}    
		return subRoot;
	}


}
