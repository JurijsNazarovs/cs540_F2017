import java.lang.reflect.Array;
import java.util.*;

class State {
    char[] board;
	public static char MAX_PLAYER = '1';
	public static char MIN_PLAYER = '2';
	public String firstMove = null;
	
    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }

    public int getScore() {
    		// dark(1) is the max player, and light(2) is the min player
    		int score = 0;
    		int scoreMinPlayer = 0;
    		int scoreMaxPlayer = 0;
    		for (int i = 0; i < this.board.length; i++) {
    			if (this.board[i] == State.MIN_PLAYER)
    				scoreMinPlayer++;
    			if(this.board[i] == State.MAX_PLAYER)
    				scoreMaxPlayer++;
    		}
    		
    		if (scoreMaxPlayer > scoreMinPlayer)
    			score = 1;
    		if (scoreMaxPlayer < scoreMinPlayer)
    			score = -1;
    		
        return score;
    }
    
    public boolean isTerminal() {
    		boolean is_terminal = false;
    		if (this.getSuccessors(State.MIN_PLAYER).length == 0 && this.getSuccessors(State.MAX_PLAYER).length == 0)
    			is_terminal = true;
        return is_terminal;
    }

    public State[] getSuccessors(char player) {
        // player - to see for which color we change: 1 or 2
    		char enemy; //symbol corresponding to an enemy
    		if (player == State.MAX_PLAYER) {
    			enemy = State.MIN_PLAYER;	
    		} else {
    			enemy = State.MAX_PLAYER;
    		}
    		
    		List<Integer> inds0 = this.indsOf('0');
    		List<Integer> indsPlayer = this.indsOf(player);
    		List<Integer> indsEnemy = this.indsOf(enemy);
    		Set<String>	 uniqueStates = new LinkedHashSet<String> (); //keep unique states of boards. LinkedHashSet to keep order
    		Set<Integer> indsNext = new TreeSet<Integer>(); //keep sorted indices of successors 
    		
    		// Detect all possible next points to move in
    		for (int indPlayer: indsPlayer) {
    			for (int indEnemy: indsEnemy) {
    				// Make row-column representation. 0 - row, 1 - column
    				int[] coordPlayer = {indPlayer/4, indPlayer%4};
    				int[] coordEnemy = {indEnemy/4, indEnemy%4};

    				// Generate coordinate of next move
    				int[] coordNext = {-2, -2}; //next move
    				// 1. Same row
    				if (coordPlayer[0] == coordEnemy[0]) {
    					coordNext[0] = coordEnemy[0];
    					coordNext[1] = coordEnemy[1] - Integer.signum(coordPlayer[1] - coordEnemy[1]); 
    				}
    				// 2. Same column
    				if (coordPlayer[1] == coordEnemy[1]) {
    					coordNext[0] = coordEnemy[0] - Integer.signum(coordPlayer[0] - coordEnemy[0]); 
    					coordNext[1] = coordEnemy[1];
    				}
    				// 3. Main - diagonal
    				if (coordPlayer[0] - coordEnemy[0] == coordPlayer[1] - coordEnemy[1]) {
    					coordNext[0] = coordEnemy[0] - Integer.signum(coordPlayer[0] - coordEnemy[0]); 
    					coordNext[1] = coordEnemy[1] - Integer.signum(coordPlayer[1] - coordEnemy[1]); 
    				}
    				// 4. Anti - diagonal
    				if (coordPlayer[0] - coordEnemy[0] == -(coordPlayer[1] - coordEnemy[1])) {
    					coordNext[0] = coordEnemy[0] - Integer.signum(coordPlayer[0] - coordEnemy[0]); 
    					coordNext[1] = coordEnemy[1] + Integer.signum(coordPlayer[0] - coordEnemy[0]);
    				}
    				
    				// Validate coordinate of the next move
    				if (coordNext[0] < 0 || coordNext[0] > 3 || coordNext[1] < 0 || coordNext[1] > 3) {
    					continue;
    				}
    				
    				int indNext = transIndBack(coordNext);
    				if(!inds0.contains(indNext)) {
    					continue;
    				}    				
    				indsNext.add(indNext);
    			}
    		}
    		
    		// Generate a combination of points to change and add new state
    		for (int indNext: indsNext) {
    			
    			Set<Integer> indsToChange = new HashSet<Integer>();
    			for (int indPlayer: indsPlayer) {
    				indsToChange.addAll(getIndsOfPointsBetween(indPlayer, indNext, indsEnemy));
    			}

    			// Create a successor by replacing all necessary coordinates with a player character
    			if (indsToChange.size() != 0) {
    				String newBoard = changeBoard(indsToChange, player);
    				uniqueStates.add(newBoard);
    			}
    		}
	
    		// Update successors using unique states of boards
    		State[] 	successors = new State[uniqueStates.size()];
    		int i = 0;
    		for (String state: uniqueStates) {
    			successors[i] = new State(state.toCharArray());
    			i++;
    		}
  
        return successors;
    }
 
    public void printState(int option, char player) {
    		switch(option) {
    		case 1:
    			State[] successors = this.getSuccessors(player);
    			if (successors.length == 0 && !this.isTerminal()) {
    				// Print the board itself
    				System.out.println(this.getBoard());
    			} else {
    				for (State successor: successors) {
    					System.out.println(successor.getBoard());
    				}
    			}
    			break;
        
    		case 2:
    			if (this.isTerminal())
      		  System.out.println(this.getScore());
    			else
      		  System.out.println("non-terminal");
    			break;
        
    		case 3:
    			System.out.println(Minimax.run(this, player));
        		System.out.println(Minimax.MIN_MAX_CALL_ITER);
        		break;
        
    		case 4:
    			Minimax.run(this, player);
        		if (this.firstMove != null)
        			System.out.println(this.firstMove);
        		break;
        
    		case 5:
    			System.out.println(Minimax.run_with_pruning(this, player));
	    		System.out.println(Minimax.MIN_MAX_CALL_ITER);
	    		break;
    
    		case 6:
    			Minimax.run(this, player);
	    		if (this.firstMove != null)
	    			System.out.println(this.firstMove);
	    		break;
    		}
        
		//String[] boardTmp = this.getBoard().split("");
		//for (int i=0; i< boardTmp.length; i++) {
		//	System.out.print(boardTmp[i]);
		//	if ((i + 1) % 4 == 0)
		//		System.out.println("");
		//}
		//System.out.println("");
    }

    public String getBoard() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(this.board[i]);
        }
        return builder.toString().trim();
    }

    public boolean equals(State src) {
        for (int i = 0; i < 16; i++) {
            if (this.board[i] != src.board[i])
                return false;
        }
        return true;
    }
    
    
    // Help functions
 	private List<Integer> indsOf(char symbol) {
 		List<Integer> ind = new ArrayList<>();;
 		for (int i = 0; i < this.board.length; i++) {
 			if (this.board[i] == symbol)
 				ind.add(i);
 		}
 		return ind;
 	}
 	
 	private String changeBoard(Set<Integer> ind, char player) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.board.length; i++) {
        		if (ind.contains(i)) {
        			builder.append(player);
        		} else {
        			builder.append(this.board[i]);
        		}
        		
        }
        return builder.toString().trim();
    }
 	
 	private int transIndBack(int[] coord) {
 		return (coord[1] + coord[0]*4);
 	}
 	
 	
 	private List<Integer> getIndsOfPointsBetween(int indA, int indB, List<Integer> indC) {
 	 	// Generate indices of points between A and B, including A and B
 		// avoiding the path which contains indC
 		int[] coordA = {indA/4, indA%4};
		int[] coordB = {indB/4, indB%4};
		
 		List<Integer> indsBetween = new ArrayList<Integer> (); // transformed indices of points between A and B
 		// 1. Same row
		if (coordA[0] == coordB[0]) {
			int diff = -(coordA[1] - coordB[1]); //direction to move
			for (int i = 0; i <= Math.abs(diff); i++) {
				int val = i*Integer.signum(diff);
				indsBetween.add(transIndBack(new int[] {coordA[0], coordA[1] + val}));
			}
		}
		// 2. Same column
		if (coordA[1] == coordB[1]) {
			int diff = -(coordA[0] - coordB[0]); //direction to move
			for (int i = 0; i <= Math.abs(diff); i++) {
				int val = i*Integer.signum(diff);
				indsBetween.add(transIndBack(new int[] {coordA[0] + val, coordA[1]}));
			}
		}
		// 3. Main - diagonal
		if (coordA[0] - coordB[0] == coordA[1] - coordB[1]) {
			int diff = -(coordA[1] - coordB[1]); //direction to move
			for (int i = 0; i <= Math.abs(diff); i++) {
				int val = i*Integer.signum(diff);
				indsBetween.add(transIndBack(new int[] {coordA[0] + val, coordA[1] + val}));
			}
		}
		// 4. Anti - diagonal
		if (coordA[0] - coordB[0] == -(coordA[1] - coordB[1])) {
			int diff = -(coordA[0] - coordB[0]); //direction to move
			for (int i = 0; i <= Math.abs(diff); i++) {
				int val = i*Integer.signum(diff);
				indsBetween.add(transIndBack(new int[] {coordA[0] + val, coordA[1] - val}));
			}
		}
		
		if (indsBetween.size() <= 2) {
			// If 2 points near each other
			return new ArrayList<Integer>();
		} else {
			for (int i = 1; i < indsBetween.size() - 1; i++) {
				// Check just opponents is between
				if (!indC.contains(indsBetween.get(i))) {
					return new ArrayList<Integer>();
				}
			}
		}
		return indsBetween;
 	}
 	
}


class Minimax {
	public static int MIN_MAX_CALL_ITER = 0;
	
	private static int max_value(State curr_state) {
	    	// Returns the max value, which can be achieved for curr_state
		Minimax.MIN_MAX_CALL_ITER++;
		int rememberIter = Minimax.MIN_MAX_CALL_ITER;
	    	if (curr_state.isTerminal()) {
	    		return curr_state.getScore();
	    	}
	    	
	    	int alpha = Integer.MIN_VALUE; //-infty
	    	int alphaBest = Integer.MIN_VALUE; //Part D
	    	State[] succCurrState = curr_state.getSuccessors(State.MAX_PLAYER);
	    	if (succCurrState.length != 0) {
	    	 	for (State s: succCurrState) {
		    		alpha = Math.max(alpha, Minimax.min_value(s));
		    		// Part D
		    		if (rememberIter == 1 && alpha > alphaBest) {
		    			alphaBest = alpha;
		    			curr_state.firstMove = s.getBoard();
		    		}
	    	 	}
	    	} else {
	    		// If we have to skip the step, we have 0 successors
	    		if (rememberIter == 1 ) {
	    			curr_state.firstMove = curr_state.getBoard();
	    		}
	    		return Minimax.min_value(curr_state);
	    	}

	    	return alpha;
	}
	
	private static int min_value(State curr_state) {
		// Returns the min value, which can be achieved for curr_state
	    	Minimax.MIN_MAX_CALL_ITER++;
	    	int rememberIter = Minimax.MIN_MAX_CALL_ITER;
	    	if (curr_state.isTerminal()) {
	    		return curr_state.getScore();
	    	}
	    	
	    	int beta = Integer.MAX_VALUE; //+infty
	    	int betaBest = Integer.MAX_VALUE; //Part D
	    	State[] succCurrState = curr_state.getSuccessors(State.MIN_PLAYER);
	    	if (succCurrState.length != 0) {
		    	for (State s: succCurrState) {
		    		beta = Math.min(beta, Minimax.max_value(s));
		    		// Part D
		    		if (rememberIter == 1 && beta < betaBest) {
		    			betaBest = beta;
		    			curr_state.firstMove = s.getBoard();
		    		}
		    	}
	    	} else {
	    		// If we have to skip the step, we have 0 successors
	    		if (rememberIter == 1 ) {
	    			curr_state.firstMove = curr_state.getBoard();
	    		}
	    		return Minimax.max_value(curr_state);
	    	}
	    	
	    	return beta;
	}
	
	
	private static int max_value_with_pruning(State curr_state, int alpha, int beta) {
	    	// Returns the max value, which can be achieved for curr_state
		Minimax.MIN_MAX_CALL_ITER++;
		int rememberIter = Minimax.MIN_MAX_CALL_ITER;
	    	if (curr_state.isTerminal()) {
	    		return curr_state.getScore();
	    	}
	    	
	    	State[] succCurrState = curr_state.getSuccessors(State.MAX_PLAYER);
	    	int alphaBest = Integer.MIN_VALUE;
	    	if (succCurrState.length != 0) {
	    	 	for (State s: succCurrState) {
		    		alpha = Math.max(alpha, Minimax.min_value_with_pruning(s, alpha, beta));
		    		// Part F
		    		if (rememberIter == 1 && alpha > alphaBest) {
		    			alphaBest = alpha;
		    			curr_state.firstMove = s.getBoard();
		    		}
		    		if (alpha >= beta)
		    			return beta;
	    	 	}
	    	} else {
	    		// If we have to skip the step, we have 0 successors
	    		if (rememberIter == 1 ) {
	    			curr_state.firstMove = curr_state.getBoard();
	    		}
	    		return Minimax.min_value_with_pruning(curr_state, alpha, beta);
	    	}

	    	return alpha;
	}
	
	private static int min_value_with_pruning(State curr_state, int alpha, int beta) {   
		// Returns the min value, which can be achieved for curr_state
	    	Minimax.MIN_MAX_CALL_ITER++;
	    	int rememberIter = Minimax.MIN_MAX_CALL_ITER;
	    	if (curr_state.isTerminal()) {
	    		return curr_state.getScore();
	    	}
	    	
	    	State[] succCurrState = curr_state.getSuccessors(State.MIN_PLAYER);
	    	int betaBest = Integer.MAX_VALUE;
	    	if (succCurrState.length != 0) {
		    	for (State s: succCurrState) {
		    		beta = Math.min(beta, Minimax.max_value_with_pruning(s, alpha, beta));
		    		// Part F
		    		if (rememberIter == 1 && beta < betaBest) {
		    			betaBest = beta;
		    			curr_state.firstMove = s.getBoard();
		    		}
		    		if (alpha >= beta)
		    			return alpha;
		    	}
	    	} else {
	    		// If we have to skip the step, we have 0 successors
	    		if (rememberIter == 1 ) {
	    			curr_state.firstMove = curr_state.getBoard();
	    		}
	    		return Minimax.max_value_with_pruning(curr_state, alpha, beta);
	    	}
	    	
	    	return beta;
	}
	
	public static int run(State curr_state, char player) {
        // Runs the Minimax algorithm and return the game theoretic value
		if (player == State.MAX_PLAYER)
			return Minimax.max_value(curr_state);
		else
			return Minimax.min_value(curr_state);
	}
	
	public static int run_with_pruning(State curr_state, char player) {
		 // Runs alpha-beta algorithm and return the game theoretic value
		int alpha = Integer.MIN_VALUE; //-infty
		int beta = Integer.MAX_VALUE; //+infty
		if (player == State.MAX_PLAYER)
			return Minimax.max_value_with_pruning(curr_state, alpha, beta);
		else
			return Minimax.min_value_with_pruning(curr_state, alpha, beta);
	}
}

public class Reversi {
    public static void main(String args[]) {
    		//String[] args = {"400", "1", "0000021001200000"}; 
        if (args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        char[] board = new char[16];
        for (int i = 0; i < 16; i++) {
            board[i] = args[2].charAt(i);
        }
        int option = flag / 100;
        char player = args[1].charAt(0);
        if ((player != '1' && player != '2') || args[1].length() != 1) {
            System.out.println("Invalid Player Input");
            return;
        }
        State init = new State(board);
        
        init.printState(option, player);
    }
}
