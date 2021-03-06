import java.util.*;

public class successor {
    public static class JugState {
        int[] Capacity = new int[]{0,0,0};
        int[] Content = new int[]{0,0,0};
        
        // Constructors
        public JugState(JugState copyFrom)
        {
            this.Capacity[0] = copyFrom.Capacity[0];
            this.Capacity[1] = copyFrom.Capacity[1];
            this.Capacity[2] = copyFrom.Capacity[2];
            this.Content[0] = copyFrom.Content[0];
            this.Content[1] = copyFrom.Content[1];
            this.Content[2] = copyFrom.Content[2];
        }
        public JugState()
        {
        }
        public JugState(int A,int B, int C)
        {
            this.Capacity[0] = A;
            this.Capacity[1] = B;
            this.Capacity[2] = C;
        }
        public JugState(int A,int B, int C, int a, int b, int c)
        {
            this.Capacity[0] = A;
            this.Capacity[1] = B;
            this.Capacity[2] = C;
            this.Content[0] = a;
            this.Content[1] = b;
            this.Content[2] = c;
        }
 
        // Actions
        public JugState empty(int ind) {
        		JugState tmpState = new JugState(this);
    			tmpState.Content[ind] = 0;
    			//System.out.println(ind + "-th jug - Empty of " + Content[ind]);
        		
    			return tmpState;
        }
        
        public JugState fill(int ind) {
	    		JugState tmpState = new JugState(this);
    			tmpState.Content[ind] = tmpState.Capacity[ind];
    			//System.out.println(ind + "-th jug - Filled up to " + tmpState.Capacity[ind]);
    		
	    		return tmpState;
        }
        
        public JugState pour(int fromInd, int toInd) {
	        	JugState tmpState = new JugState(this);
	        	int possibleAmountToPour = this.Capacity[toInd] - this.Content[toInd];
	        	
	        	if (possibleAmountToPour > this.Content[fromInd]) {
	        		tmpState.Content[toInd] += tmpState.Content[fromInd];
	        		tmpState.Content[fromInd] = 0;
	        		//System.out.println(fromInd + "-th jug - Poure " + tmpState.Content[fromInd] + " to " + toInd);
	        	} else {
	        		tmpState.Content[toInd] = tmpState.Capacity[toInd];
	        		tmpState.Content[fromInd] -= possibleAmountToPour;
	        		//System.out.println(fromInd + "-th jug - Poure " + possibleAmountToPoure + " to " + toInd);
	        	}
	        	
	    		return tmpState;
        }
        
        // Other functions
        public boolean isSame(JugState a) {
        		if (Arrays.equals(this.Content, a.Content) && Arrays.equals(this.Capacity, a.Capacity)) {
        			return true;
        		} else {
        			return false;
        		}
        }
        
        public void printContent()
        {
            System.out.println(this.Content[0] + " " + this.Content[1] + " " + this.Content[2]);
        }
        
        // Homework function
        public ArrayList<JugState> getNextStates(){
            ArrayList<JugState> successors = new ArrayList<>();
            
            // Empty jugs
            for (int i = 0; i < this.Content.length; i++) {
            		JugState tmpState = this.empty(i);
            		
            		if (tmpState == null) {
            			System.out.println("NULL");
            		}
	            	if (!this.isSame(tmpState)) {
	    				successors.add(tmpState);
	            	}
            }
            
            // Fill jugs
            for (int i = 0; i < this.Content.length; i++) {
	            JugState tmpState = this.fill(i);
	            	if (!this.isSame(tmpState)) {
        				successors.add(tmpState);
	            	}
            }
           
            // Pure from one jug to another
            for (int toInd = 0; toInd < this.Content.length; toInd++) {
            		for (int fromInd = 0; fromInd < this.Content.length; fromInd++) {
	            		if (fromInd != toInd) {
	            			JugState tmpState = this.pour(fromInd, toInd);
	            			if (!this.isSame(tmpState)) {
	            				successors.add(tmpState);
	            			}
	            		}
            		}
            }

            return successors;
        }
    }

    public static void main(String[] args) {
    		//String[] args = new String[] {"11", "5", "2", "6", "3", "0"};
    		//String[] args = new String[] {"3", "2", "1", "3", "2", "0"};
        if( args.length != 6 )
        {
            System.out.println("Usage: java successor [A] [B] [C] [a] [b] [c]");
            return;
        }

        // parse command line arguments
        JugState a = new JugState();
        a.Capacity[0] = Integer.parseInt(args[0]);
        a.Capacity[1] = Integer.parseInt(args[1]);
        a.Capacity[2] = Integer.parseInt(args[2]);
        a.Content[0] = Integer.parseInt(args[3]);
        a.Content[1] = Integer.parseInt(args[4]);
        a.Content[2] = Integer.parseInt(args[5]);

        // Implement this function
        ArrayList<JugState> asist = a.getNextStates();

        // Print out generated successors
        for(int i=0; i< asist.size(); i++)
        {
            asist.get(i).printContent();
        }

        return;
    }
}

