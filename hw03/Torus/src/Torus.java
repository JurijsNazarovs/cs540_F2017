import java.util.*;

class State {
	int[] board;
	State parentPt;
	int depth;

	public State(int[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
		this.parentPt = null;
		this.depth = 0;
	}

	public State[] getSuccessors() {
		
		int indSpace = this.indOfSpace(); // starts with 0
		int indRow = (int) Math.floor((indSpace)/3.);
		int indCol = indSpace - indRow*3;
		
		int indRowTmp = 0; 
		int indColTmp = 0;
		int indChange = 0; //index of changing position
		
		State[] successors = new State[4]; //result of the function
		String[] sortedStates = new String[successors.length];
		
		for (int i = 0; i <= 3; i++) {
			// Do one of 4 possible actions despite the position
			switch(i) {
				case 0: indRowTmp = indRow - 1;
						indColTmp = indCol;
						break;
				case 1: indRowTmp = indRow + 1;
						indColTmp = indCol;
						break;
				case 2: indRowTmp = indRow;
						indColTmp = indCol + 1;
						break;
				case 3: indRowTmp = indRow;
						indColTmp = indCol - 1;
						break;
			}
			
			// Fix positions to make a ring
			if (indRowTmp < 0)
				indRowTmp = 2;
			if (indRowTmp > 2)
				indRowTmp = 0;
			
			if (indColTmp < 0)
				indColTmp = 2;
			if (indColTmp > 2)
				indColTmp = 0;
				
			// Fill successors
			indChange = indColTmp + indRowTmp*3;
			successors[i] = new State(this.board);
			successors[i].board[indChange] = 0;
			successors[i].board[indSpace] = this.board[indChange];
			
			successors[i].parentPt = this;
			successors[i].depth = this.depth + 1;
			
			// Create a string based on the board, to do sorting
			sortedStates[i] = successors[i].getBoard();
		}
		
		// Update successors using sorted states of boards
		Arrays.sort(sortedStates);
		for (int i = 0; i < sortedStates.length; i++) {
			String[] sortedState = sortedStates[i].split(" ");
			for (int j = 0; j < sortedState.length; j++) {
				successors[i].board[j] = Integer.parseInt(sortedState[j]);
			}
		}
		
		return successors;
	}

	public void printState(int option) {
		// Prints a torus single State based on an option (flag)
		switch(option) {
		case 1:
		case 2: System.out.println(this.getBoard());
				break;
		case 3: System.out.print(this.getBoard() + " parent ");
				if (this.parentPt == null)
					System.out.println("0 0 0 0 0 0 0 0 0");
				else 
					System.out.println(this.parentPt.getBoard());
				break;
		}
	}

	public String getBoard() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			builder.append(this.board[i]).append(" ");
		}
		return builder.toString().trim();
	}

	public boolean isGoalState() {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != (i + 1) % 9)
				return false;
		}
		return true;
	}

	public boolean equals(State src) {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != src.board[i])
				return false;
		}
		return true;
	}
	
	
	// My functions
	public int indOfSpace() {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] == 0)
				return i;
		}
		return -1;
	}
	
	public int indAmong(List<State> states) {
		// Method returns an index of this among states
		// -1 if no this find among states
		
		int ind = 0;
		for (State state: states) {
			if (this.equals(state)) {
				return ind;
			} else {
				ind ++;
			}
		}
		return -1;
	}
}

public class Torus {

	public static void main(String args[]) {
		//String[] args = {"500", "1", "2", "3", "4", "5", "0", "6", "7", "8"}; 
		//String[] args = {"500", "4", "3", "8", "5", "1", "6", "7", "2", "0"};
		//String[] args = {"500", "8", "7", "6", "5", "4", "3", "2", "1", "0"};
		if (args.length < 10) {
			System.out.println("Invalid Input");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		int[] board = new int[9];
		for (int i = 0; i < 9; i++) {
			board[i] = Integer.valueOf(args[i + 1]);
		}
		int option = flag / 100;
		int cutoff = flag % 100;
		if (option == 1) {
			State init = new State(board);
			State[] successors = init.getSuccessors();
			for (State successor: successors) {
				successor.printState(option);
			}
		} else {
			// Variables identification
			State initNode = new State(board);
			Stack<State> stack = new Stack<>();
			List<State> prefix = new ArrayList<>();
			
			int goalChecked = 0;
			int maxStackSize = Integer.MIN_VALUE;
			boolean opt4FirstTime = true;

			// Main part
			while (true) {				
				stack.push(initNode);
				prefix.add(initNode);
				
				while (!stack.isEmpty()) {
					maxStackSize = Math.max(maxStackSize, stack.size());
					State currentNode = stack.pop();
					currentNode.printState(option);
					
					// Prefix path - cycle prevention
					if (currentNode.parentPt != null) {
						int indParent = currentNode.parentPt.indAmong(prefix);
						
						if (indParent > -1 && indParent != (prefix.size() - 1)) {
							// Remove part: init...p... => init...p
							prefix.subList(indParent + 1, prefix.size()).clear(); 
						}
						prefix.add(currentNode);
					}
					
					// Goal testing and successors adding
					if (currentNode.depth <= cutoff) {
						
						if(currentNode.depth == cutoff && option == 4 && opt4FirstTime == true) {
							// Print out prefix part
							opt4FirstTime = false;
							for (State state: prefix) {
								System.out.println(state.getBoard());
							}
						}
						
						goalChecked++; // number of times check the goal
						if (currentNode.isGoalState()) {
							if (option == 5) {
								for (State state: prefix)
									System.out.println(state.getBoard());
									
								System.out.println("Goal-check " + goalChecked);
								System.out.println("Max-stack-size " + maxStackSize);
							}
							return;
						}
						
						// Fill children just if cutoff allows and they are not in path
						if (currentNode.depth < cutoff) { 
							for (State successor: currentNode.getSuccessors()) {
								int indTmp = successor.indAmong(prefix);
								if (indTmp == -1)
									stack.push(successor);
							}
						}
					}
				}
			
				if (option != 5)
					break;
				
				// Update initial parameters	for new iteration
			    cutoff++;
			    stack.clear();
			    prefix.clear();
			}
		}
	}
}
