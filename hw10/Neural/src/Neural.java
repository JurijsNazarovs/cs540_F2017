
import java.util.*;
import java.io.*;

public class Neural{
	int nLayers;
	List<Integer> nNodesPerLayer = new ArrayList<Integer>();
	List<String> funPerLayer = new ArrayList<String>();
	List<Double> w = new ArrayList<Double>();
	List<Double> pdW = new ArrayList<Double>(); //update using method
	List<Double> x = new ArrayList<Double>();
	List<Layer> layers = new ArrayList<Layer>();
	Integer y = null;
	
	public Neural(Neural nn, List<Double> x, Integer y) {
		// Constructor to copy a structure with weights
		this.nLayers = nn.nLayers; //input layer
		this.nNodesPerLayer.addAll(nn.nNodesPerLayer);
		this.funPerLayer.addAll(nn.funPerLayer);
		this.w.addAll(nn.w);
		this.x.addAll(x);
		this.y = y;
	}
	public Neural(int nLayers, List<Integer> nNodesPerLayer, List<String> funPerLayer) {
		// This constructor consider input x as 0th layer
		if (nLayers != nNodesPerLayer.size())
			throw new IllegalArgumentException("Number of layers and the size of list with "
											+ "number of nodes per layer is not consistent");
		this.nLayers = nLayers + 1; //input layer
		this.nNodesPerLayer.add(0); //input layer
		this.nNodesPerLayer.addAll(nNodesPerLayer);
		this.funPerLayer.add("same"); //input layer
		this.funPerLayer.addAll(funPerLayer);
	}
	
	public Neural(int nLayers, List<Integer> nNodesPerLayer, List<String> funPerLayer, List<Double> w) {
		this(nLayers, nNodesPerLayer, funPerLayer);
		this.w.addAll(w);
	}
	
	public Neural(int nLayers, List<Integer> nNodesPerLayer, List<String> funPerLayer, List<Double> w, List<Double> x) {
		this(nLayers, nNodesPerLayer, funPerLayer, w);
		this.nNodesPerLayer.set(0, x.size()); //update number of nodes in input layer
		this.x.addAll(x);
	}
	
	static public void main(String[] args){
		// Prior parameters
		int nLayers = 2;
		List<Integer> nNodesPerLayer = new ArrayList<Integer>();
		nNodesPerLayer.add(2); nNodesPerLayer.add(1);
		List<String> funPerLayer = new ArrayList<String>();
		funPerLayer.add("relu"); funPerLayer.add("sigmoid");
		
		//String[] args = {"100", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1", "-1"};
		//String[] args = {"100", "1", "0.9", "0.8", "0.7", "0.6", "0.5", "0.4", "0.3", "0.2", "-0.2", "1.7"};
		//String[] args = {"100", "4", "3", "2", "1", "0", "-1", "-2", "-3", "-4", "-4", "1"};
		//String[] args = {"200", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1", "-1", "1"};
		//String[] args = {"200", "1", "0.9", "0.8", "0.7", "0.6", "0.5", "0.4", "0.3", "0.2", "-0.2", "1.7", "0"};
		//String[] args = {"300", "4", "3", "2", "1", "0", "-1", "-2", "-3", "-4", "-4", "1", "0"};
		//String[] args = {"500", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1", "-1", "1", "0.1"};
		//String[] args = {"500", "4", "3", "2", "1", "0", "-1", "-2", "-3", "-4", "-4", "1", "0", "0.1"};
		//String[] args = {"600", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.1"};
		//String[] args = {"700", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.1", "3"};
		//String[] args = {"800", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.1", "1000"};
		//String[] args = {"800", "-1", "1", "-1", "1", "-1", "1", "-1", "1", "-1", "0.001", "100"};
		int flag = Integer.valueOf(args[0]);
		
		// Fill weights and input
		List<Double> weights = new ArrayList<Double>();
		List<Double> inputData = new ArrayList<Double>();
		
		for (int i = 1; i < 10; i++)
			weights.add(Double.valueOf(args[i]));
		
		if (flag != 600 && flag != 700 && flag != 800) {
			for (int i = 10; i < 12; i++)
				inputData.add(Double.valueOf(args[i]));
		}
		
		// Main part
		if (flag == 100) {
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights, inputData);
			nn.update();
			nn.print();
		}
		
		if (flag == 200) {
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights, inputData);
			nn.update();
			nn.y = Integer.valueOf(args[12]);
			int layerId = nn.nLayers - 1;
			Layer currLayer = nn.layers.get(layerId);
			nn.updatePDOf(currLayer, layerId);
			System.out.printf("%.5f ", nn.predictionError());
			for (int i = 0; i < currLayer.pdNodes.size(); i++)
				System.out.printf("%.5f %.5f ", currLayer.pdNodes.get(i).v, currLayer.pdNodes.get(i).u);
			System.out.println("");
		}
		
		if (flag == 300) {
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights, inputData);
			nn.update();
			
			nn.y = Integer.valueOf(args[12]);
			int layerId = 1;
			Layer currLayer = nn.layers.get(layerId);
			nn.updatePDOf(currLayer, layerId);
			for (int i = 0; i < currLayer.pdNodes.size(); i++)
				System.out.printf("%.5f %.5f ", currLayer.pdNodes.get(i).v, currLayer.pdNodes.get(i).u);
			System.out.println("");
		}
		
		if (flag == 400) {
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights, inputData);
			nn.update();
			
			nn.y = Integer.valueOf(args[12]);
			nn.updatePDWs();
			print(nn.pdW);
		}
		
		if (flag == 500) {
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights, inputData);
			nn.update();
			
			nn.y = Integer.valueOf(args[12]);
			double eta = Double.valueOf(args[13]);
			print(nn.w);
			System.out.printf("%.5f\n", nn.predictionError());
			
			nn.updatePDWs();
			nn.updateWs(eta);
			nn.update();
			print(nn.w);
			System.out.printf("%.5f\n", nn.predictionError());
		}
		
		if (flag == 600) {
			double eta = Double.valueOf(args[10]);
			List<Data> dataTrain = new ArrayList<Data>(importDataFrom("hw2_midterm_A_train.txt", " "));
			List<Data> dataEval = new ArrayList<Data>(importDataFrom("hw2_midterm_A_eval.txt", " "));
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights);
			
			nn.runEpoch(dataTrain, dataEval, 1, eta, 1, false);
		}
		
		if (flag == 700) {
			double eta = Double.valueOf(args[10]);
			int T = Integer.valueOf(args[11]);
			List<Data> dataTrain = new ArrayList<Data>(importDataFrom("hw2_midterm_A_train.txt", " "));
			List<Data> dataEval = new ArrayList<Data>(importDataFrom("hw2_midterm_A_eval.txt", " "));
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights);
			
			nn.runEpoch(dataTrain, dataEval, T, eta, 2, false);
		}
		
		if (flag == 800) {
			double eta = Double.valueOf(args[10]);
			int T = Integer.valueOf(args[11]);
			List<Data> dataTrain = new ArrayList<Data>(importDataFrom("hw2_midterm_A_train.txt", " "));
			List<Data> dataEval = new ArrayList<Data>(importDataFrom("hw2_midterm_A_eval.txt", " "));
			List<Data> dataTest = new ArrayList<Data>(importDataFrom("hw2_midterm_A_test.txt", " "));
			Neural nn = new Neural(nLayers, nNodesPerLayer, funPerLayer, weights);
			
			nn.runEpoch(dataTrain, dataEval, T, eta, 3, true);
			System.out.printf("%.5f\n", classificationAccuracy(nn, dataTest));
		}
			
	}
	
	public void update(){
		// Calculate u and v for every layer according to input weights
		// and update results in layers
		this.layers.clear();
		List<Double> inpTmp = new ArrayList<Double>(this.x); //u in nodes
		int prevStep = 0; //starting position to separate weights
		for (int i = 0; i < this.nLayers; i++) { //Go throw all layers. -1 - for input layer
			int nextStep;
			if (i == this.nLayers - 1)
				nextStep = this.w.size();
			else
				nextStep = prevStep + this.nNodesPerLayer.get(i + 1)*(inpTmp.size() + 1);
			
			// Weights with bias, input without bias
			Neural.Layer currLayer = new Neural.Layer(inpTmp.size(),
												     this.funPerLayer.get(i),
													 w.subList(prevStep, nextStep),
													 inpTmp);
			this.layers.add(currLayer);
			
			if (i != this.nLayers - 1) { //not the output layer
				prevStep = nextStep;
				// Update input
				inpTmp.clear();
				List<Double> allV = new ArrayList<Double>();
				for (int j = 0; j < currLayer.nNodes; j++) {
					allV.add(currLayer.nodes.get(j).v);
				}
				int nInput = allV.size();
				for (int j = 0; j < this.nNodesPerLayer.get(i + 1); j++) { // consider number of nodes for next layer
					// Have to update weight for new input
					// v should be provided WITH bias
					List<Double> productTmp = elWiseProd(currLayer.w.subList(j*nInput, nInput + j*nInput), allV);
					Double u = sumOf(productTmp);
					inpTmp.add(u);
				}
			}
		}
	}
	
	public Double predictionError() {
		// NN has to be update to this point by using 
		// method:  Neural.update
		if (this.y == null)
			throw new IllegalArgumentException("To use method: predictionError, y has to be defined");
		
		Neural.Layer lastLayer = this.layers.get(this.nLayers - 1);
		return 1./2*Math.pow(lastLayer.nodes.get(lastLayer.nNodes - 1).v - this.y, 2);
	}
	
	public int classification() {
		// NN has to be update to this point by using 
		// method:  Neural.update
		if (this.y == null)
			throw new IllegalArgumentException("To use method: classification, y has to be defined");
		
		Neural.Layer lastLayer = this.layers.get(this.nLayers - 1);
		int classId = 0;
		if (lastLayer.nodes.get(lastLayer.nNodes - 1).v >= 0.5)
			classId = 1;
		
		return classId;
	}
	
	public void updatePDOf(Layer layer, int layerId){
		// Returns a vector with partial derivatives of U and V for the layer
		if (this.y == null)
			throw new IllegalArgumentException("To use method: updatePartDerOf, y has to be defined");
		
		List<Neural.Layer.Node> partDer = new ArrayList<Neural.Layer.Node>();
		
		if (layerId == this.nLayers - 1) {
			// Output layer
			for (int i = 1; i < layer.nNodes; i++) { //to skip bias node
				Double u = null;
				Double v = null;
				v = layer.nodes.get(i).v - this.y; // According to a loss function
				u = v*layer.nodes.get(i).v*(1 - layer.nodes.get(i).v);
				partDer.add(new Neural.Layer.Node(u, v));
			}
		} else {
			// Hidden layer
			int nextLayerId = layerId + 1;
			Layer nextLayer = this.layers.get(nextLayerId);
			this.updatePDOf(nextLayer, nextLayerId);
			for (int i = 1; i < layer.nNodes; i++) { //to skip bias node
				Double u = 0.;
				Double v = 0.;
				
				for (int j = 0; j < nextLayer.pdNodes.size(); j++)
					v += layer.w.get(i)*nextLayer.pdNodes.get(j).u;
				
				if (layer.nodes.get(i).u >= 0)
					u = v;
				
				partDer.add(new Neural.Layer.Node(u, v));
			}
		}

		layer.updatePDNodes(partDer);
	}
	
	public void updatePDWs(){
		List<Double> partDerW = new ArrayList<Double>();
		this.updatePDOf(this.layers.get(1), 1); //update partial derivatives of all layers after input
		for (int layerIter = 0; layerIter < this.nLayers - 1; layerIter++) {	//skip output layer
			Layer layer = this.layers.get(layerIter);
			Layer nextLayer = this.layers.get(layerIter + 1);
			
			for (int j = 0; j < nextLayer.pdNodes.size(); j++) { //pdNodes iter
				for (int i = 0; i < layer.nNodes; i++) //including bias
					partDerW.add(layer.nodes.get(i).v*nextLayer.pdNodes.get(j).u);
			}
		}
		
		if (partDerW.size() != this.w.size())
			throw new IllegalArgumentException("Error in updating of pdW in Neural. Inconsistent size");
		this.pdW.clear();
		this.pdW.addAll(partDerW);
	}
	
	public void updateWs(double eta){
		// Partial derivatives of weights have to be updated prior this function
		List<Double> newW = new ArrayList<Double>();
		for (int i = 0; i < this.w.size(); i++)
			newW.add(this.w.get(i) - eta*this.pdW.get(i));
		this.w.clear();
		this.w.addAll(newW);
	}
	
	public void runEpoch(List<Data> dataTrain, List<Data> dataTest, 
						int nEpoch, double eta, int printOpt,
						boolean smartStop){
		// printData - 1 - for every iteration + input data, 2 - for every epoch
		int stopEpoch = 0;
		double evalErrOld = Double.MAX_VALUE;
		for (int i = 1; i <= nEpoch; i++) {
			double evalErr = 0;
			for (int j = 0; j < dataTrain.size(); j ++) {
				Data currTrainData = dataTrain.get(j);
				evalErr = 0;
				// Initialize network
				this.x.clear();
				this.x.addAll(currTrainData.x);
				this.y = currTrainData.y;
				// Update network
				this.update();
				this.updatePDWs();
				this.updateWs(eta);
				for (int k = 0; k < dataTest.size(); k++) {
					Neural evalNN = new Neural(this, dataTest.get(k).x, dataTest.get(k).y);
					evalNN.update();
					evalErr += evalNN.predictionError();
				}
				
				if (printOpt == 1) {
					currTrainData.print();
					print(this.w);
					System.out.printf("%.5f\n", evalErr);
				}
			}
			
			if (printOpt == 2) {
				print(this.w);
				System.out.printf("%.5f\n", evalErr);
			}
			
			if (smartStop == true && evalErrOld < evalErr) {
				stopEpoch = i;
				evalErrOld = evalErr;
				break;
			}
			evalErrOld = evalErr;
			stopEpoch = i;
		}
		
		if (printOpt == 3) {
			System.out.printf("%d\n", stopEpoch);
			print(this.w);
			System.out.printf("%.5f\n", evalErrOld);
		}
	}
	
	public static Double classificationAccuracy(Neural trainNN, List<Data> dataTest) {
		int nCorrectPredictions = 0;
		int n = dataTest.size();
		for (int i = 0; i < n; i++) {
			Data currData = dataTest.get(i);
			Neural testNN = new Neural(trainNN, currData.x, currData.y);
			testNN.update();
			int classId = testNN.classification();
			if (classId == testNN.y)
				nCorrectPredictions += 1;
		}
		return ((double) nCorrectPredictions/n);
	}
	
	public void print() {
		// Function print nodes inside every layer
		// except input layer
		for (int i = 1; i < this.nLayers; i++) {
			this.layers.get(i).print();
			if (i != this.nLayers - 1) {
				System.out.print(" ");
			} else
				System.out.println("");
		}
	}
	
	
	private static class Layer{
		// Class describes a single layer of NN
		int nNodes = 0;
		String fun; //function used for a layer
		List<Neural.Layer.Node> nodes = new ArrayList<Neural.Layer.Node>();
		List<Neural.Layer.Node> pdNodes = new ArrayList<Neural.Layer.Node>(); //nodes with partial derivatives - update using method
		List<Double> w = new ArrayList<Double>(); //weights
		
		private Layer(int nNodes, String fun, List<Double> w, List<Double> listOfU) {
			// listOfU does not include bias. But bias term is add as a node
			if (nNodes != listOfU.size())
				throw new IllegalArgumentException("To construct a layer nNodes has to be equal to a size of listOfU");
			
			this.nNodes = nNodes + 1; //for bias
			this.fun = fun;
			this.w.addAll(w);
			
			for (int i = 0; i < this.nNodes; i++) {
				if (i == 0) {
					// Bias term
					this.nodes.add(new Neural.Layer.Node(1., 1.));
				} else {
					Double u = listOfU.get(i - 1);
					Double v;
					switch (this.fun.toLowerCase()) {
						case "relu":
							v = Math.max(u, 0);
							break;
						case "sigmoid":
							v = Sigmoid(u);
							break;
						case "same":
							v = u;
							break;
						default:
							throw new IllegalArgumentException("Unknow function for a node");
					}

					this.nodes.add(new Neural.Layer.Node(u, v));
				}
			}
		}
		
		private void updatePDNodes(List<Neural.Layer.Node> pdNodes) {
			// Functioin update nodes of partial derivatives
			if (pdNodes.size() != this.nNodes - 1)
				throw new IllegalArgumentException("Number of partial derivatives and number of nodes"
												+ "excluding bias is not consistent");
			this.pdNodes.clear();	
			this.pdNodes.addAll(pdNodes);
		}
		
		private void print() {
			for (int i = 1; i < this.nNodes; i++) { //skip the bias
				this.nodes.get(i).print();
				if (i != this.nNodes - 1)
					System.out.print(" ");
			}
		}
		
		private static class Node{
			// Class is created just to describe a node
			// before and after process
			Double u; //before function applied
			Double v; //after function applied
			
			private Node(Double u, Double v) {
				this.u = u;
				this.v = v;
			}
			
			private void print() {
				System.out.printf("%.5f %.5f", this.u, this.v);
			}
		}
	}
	
	public static class Data {
		List<Double> x = new ArrayList<Double> ();
		Integer y;
		private Data(String str, String sep){
			String[] data = str.split(sep);
			for (int i = 0; i < data.length - 1; i++) {
				x.add(Double.valueOf(data[i]));
			}
			y = Integer.valueOf(data[data.length - 1]);
		}
		
		public void print() {
			for (int i = 0; i < x.size(); i ++)
				System.out.printf("%.5f ", x.get(i));
			System.out.printf("%.5f\n", (double) y);
		}
	}
	
	// My functions of Neural class
	public static List<Double> elWiseProd(List<Double> w, List<Double> x){
		// Element wise product, so that nW = nX * k, then
		// product repeats for every x k times.
		int n = x.size();
		if (w.size() % n != 0) {
			//That is, nW = n * k
			throw new IllegalArgumentException("Size of input in elWiseMult() is not consistent");
		}
		
		int nIter = w.size()/n;
		List<Double> product = new ArrayList<Double>();
		for (int i = 0; i < nIter; i++) {
			for (int j = 0; j < n; j++) {
				product.add(w.get(j + i*n) * x.get(j));
			}
		}
		return product;
	}
	
	public static Double sumOf(List<Double> x){
		Double product = 0.;
		for (int i = 0; i < x.size(); i++) {
			product += x.get(i);
		}
		return product;
	}
	
	public static Double Sigmoid(Double x) {
		Double res = 1./(1 + Math.exp(-x));
		return res;
	}
	
	private static List<Data> importDataFrom(String fileName, String sep){
        List<Data> data = new ArrayList<Data>();
        try{
            File f = new File(fileName);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                data.add(new Data(sc.nextLine(), sep));
            }
            sc.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return data;
    }
	
	public static void print(List<Double> list) {
		for (int i = 0; i < list.size(); i ++) {
			System.out.printf("%.5f", list.get(i));
			if (i == list.size() - 1)
				System.out.printf("\n");
			else
				System.out.printf(" ");
		}
	}
	
}

