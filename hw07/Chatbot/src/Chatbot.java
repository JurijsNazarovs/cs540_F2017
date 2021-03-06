
import java.util.*;
import java.io.*;

public class Chatbot{
	static int VOC_SIZE = 4700;
	
    private static String filename = "./WARC201709_wid.txt";
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }
    
    // My functions
    static private Integer countOf(ArrayList<Integer> corpus, ArrayList<Integer> history, int word) {
    		ArrayList <Integer> words = new ArrayList<>();
    		words.addAll(history);
    		words.add(word);
    		int nWords = words.size();
    		if (nWords == 0)
    			return -1;

    		int counter = 0;
		for (int i = 0; i < corpus.size() - nWords + 1; i++) {
			boolean wordsContained = false;
			for (int j = 0; j < nWords; j++) {
				if (corpus.get(i + j).equals(words.get(j))) {
					wordsContained = true;
				} else {
					wordsContained = false;
					break;
				}
			}
			if (wordsContained == true)
				counter++;
		}
		return counter;
    }
    
    static private Integer margCountOf(ArrayList<Integer> corpus, ArrayList<Integer> history) {
    		int nHist = history.size();
    		if (nHist == 0)
    			return corpus.size();
    		
    		ArrayList<Integer> historyTmp = new ArrayList<Integer> ();
    		historyTmp.addAll(history);
    		historyTmp.remove(nHist - 1);
    		int counter = countOf(corpus, historyTmp, history.get(nHist - 1));
    		
    		return counter;
    }
    
    static private Integer sampleFromNGram(ArrayList<Integer> corpus, ArrayList<Integer> history, double r, boolean isPrint) {
    		int margCount = margCountOf(corpus, history);
    		if (margCount == 0) {
			System.out.println("undefined");
			return -1;
		}	
    		
		double rightBound = 0;
		double leftBound = 0;
	    for (int word = 0; word < VOC_SIZE; word++) {
			int count = countOf(corpus, history, word);
	    		if (count > 0) {
	    			leftBound = rightBound;
	    			rightBound += (double)count/(double)margCount;
	    			
	    			if (r >= leftBound && r <= rightBound) {
	    				if (isPrint == true) {
		    				System.out.println(word);
		    				System.out.println(String.format("%.7f", leftBound));
		    				System.out.println(String.format("%.7f", rightBound));
	    				}
	    				return word;
	    			}
	    		}
	    }  
	    return -1;
    }
    
    static public void main(String[] args){
    		//String[] args = {"100", "4699"};
    		//String[] args = {"200", "99997", "100000"};
    		//String[] args = {"300", "4247", "0"};
    		//String[] args = {"400", "15", "100", "4442"};
    		//String[] args = {"500", "2799", "556", "2364"};
    		//String[] args = {"600", "0", "100", "2297", "414"};
    		//String[] args = {"700", "1", "0"};
    		//String[] args = {"700", "1", "1", "523"};
    		ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);
        
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
			int count = countOf(corpus, new ArrayList<Integer>(), w);
			int margCount = margCountOf(corpus, new ArrayList<Integer>());

            System.out.println(count);
            System.out.println(String.format("%.7f",count/(double)margCount));
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            double r = (double)n1/n2;
            
        		sampleFromNGram(corpus, new ArrayList<Integer>(), r, true);
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            
            ArrayList<Integer> history = new ArrayList<Integer>();
			history.add(h);
			
			int count = countOf(corpus, history, w);
			int margCount = margCountOf(corpus, history);
            
            //output 
            System.out.println(count);
            System.out.println(margCount);
            System.out.println(String.format("%.7f",count/(double)margCount));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            
            double r = (double)n1/n2;
            ArrayList<Integer> history = new ArrayList<Integer>();
            history.add(h);

    			sampleFromNGram(corpus, history, r, true);
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            
            ArrayList<Integer> history = new ArrayList<Integer>();
			history.add(h1);
			history.add(h2);
			
			int count = countOf(corpus, history, w);
			int margCount = margCountOf(corpus, history);
			
            //output 
            System.out.println(count);
            System.out.println(margCount);
            if (margCount == 0)
            		System.out.println("undefined");
            else
            		System.out.println(String.format("%.7f",count/(double)margCount));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            
            double r = (double)n1/n2;
            ArrayList<Integer> history = new ArrayList<Integer>();
            history.add(h1);
            history.add(h2);

    			sampleFromNGram(corpus, history, r, true); 
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1 = 0, h2 = 0;
            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                double r = rng.nextDouble();
                h1 = sampleFromNGram(corpus, new ArrayList<Integer>(), r, false);
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }

                // TODO Generate second word using r
                r = rng.nextDouble();
                ArrayList<Integer> history = new ArrayList<Integer>();
                history.add(h1);
                h2 = sampleFromNGram(corpus, history, r, false);
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                // TODO Generate second word using r
                double r = rng.nextDouble();
                ArrayList<Integer> history = new ArrayList<Integer>();
                history.add(h1);
                h2 = sampleFromNGram(corpus, history, r, false);
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12 && h2 != -1){
                ArrayList<Integer> history= new ArrayList<Integer>();
                history.add(h1);
                history.add(h2);
                double r = rng.nextDouble();
                int w  = 0;
                // TODO Generate new word using h1,h2
                w = sampleFromNGram(corpus, history, r, false);
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}
