import java.util.AbstractList;

public class Demo {

	public static void main(String[] args) throws Exception {

		System.out.println("This is a demo to show some of the current capabilities of this package.\n");
		
		/******************************
		 * 
		 *    CONSTANT DATA SET
		 * 
		 ******************************/
		
		
		double constant = Math.random();
		System.out.println("Training on a constant data set.\nIn this example, the training and testing data are identical.");
		
		System.out.println("The data set will have four possible inputs, (0,0), (0,1), (1,0), and (1,1), but the"
					+   "\noutput for all of them is expected to be the randomly generated value " + constant);
		ConstantDataSet constantDS = new ConstantDataSet(constant);
		
		//Adjusting the acceptable margin of error from the default to 0.0001
		constantDS.margins.set(0, 0.0001);
		System.out.println("We will consider the neural network to be correct if it guesses a value within 0.0001 of the actual answer.");
		
		//No further adjustments to the data set, so now we can finalize it.
		constantDS.getDataSet();
		
		//Create a neural net with the appropriate number of inputs and outputs, all connected, and no perceptrons in the middle.
		GraphNeuralNetwork NN = NeuralNetworkBuilder.buildSampleNetwork(constantDS, 0);
		
		//Create a neural net analyzer that allows some calculations to be performed on it
		GraphNeuralNetworkAnalyzer NNA = new GraphNeuralNetworkAnalyzer(NN);
		
		double accuracy = NNA.calculateAccuracyOnTrainingData(constantDS)*100;
		System.out.println("No training has been done, and the neural network is " + accuracy + "% accurate.");
		
		//Actually get the data
		AbstractList<DataPoint> dataset = constantDS.getTrainingData();		
		int roundsOfTraining = 0;
		while(accuracy < 95) { //Train it until it's 95% accurate
			for(DataPoint dp : dataset) {
				NN.train(dp);
			}
			accuracy = NNA.calculateAccuracyOnTrainingData(constantDS)*100;
			roundsOfTraining++;
		}
		
		System.out.println("After " + roundsOfTraining + " rounds of training, the neural network is " + accuracy + "% accurate.");
		
		System.out.println("Below are the network's guesses for each input: \n");
		
		NNA.printResultsOfTestingData(constantDS);
		
		/******************************
		 * 
		 *    AND DATA SET
		 * 
		 ******************************/
		
		System.out.println("\n\nTraining on the AND data set.\nIn this example, the training and testing data are identical.");
		
		System.out.println("The data set will have four possible inputs, (0,0), (0,1), (1,0), and (1,1), and the expected output is the logical AND of those two inputs");
		ANDDataSet andDS = new ANDDataSet();
		
		//Adjusting the acceptable margin of error from the default to 0.01
		andDS.margins.set(0, 0.01);
		System.out.println("We will consider the neural network to be correct if it guesses a value within 0.01 of the actual answer.");
		
		//No further adjustments to the data set, so now we can finalize it.
		andDS.getDataSet();
		
		//Create a neural net with the appropriate number of inputs and outputs, all connected, and no perceptrons in the middle.
		NN = NeuralNetworkBuilder.buildSampleNetwork(andDS, 0);
		
		//Create a neural net analyzer that allows some calculations to be performed on it
		NNA = new GraphNeuralNetworkAnalyzer(NN);
		
		accuracy = NNA.calculateAccuracyOnTrainingData(andDS)*100;
		System.out.println("No training has been done, and the neural network is " + accuracy + "% accurate.");
		
		//Actually get the data
		dataset = andDS.getTrainingData();		
		roundsOfTraining = 0;
		while(accuracy < 95) { //Train it until it's 95% accurate
			for(DataPoint dp : dataset) {
				NN.train(dp);
			}
			accuracy = NNA.calculateAccuracyOnTrainingData(andDS)*100;
			roundsOfTraining++;
		}
		
		System.out.println("After " + roundsOfTraining + " rounds of training, the neural network is " + accuracy + "% accurate.");
		
		System.out.println("Below are the network's guesses for each input: \n");
		
		NNA.printResultsOfTestingData(andDS);
		
		
		/******************************
		 * 
		 *    OR DATA SET
		 * 
		 ******************************/
		
		System.out.println("\n\nTraining on the OR data set.\nIn this example, the training and testing data are identical.");
		
		System.out.println("The data set will have four possible inputs, (0,0), (0,1), (1,0), and (1,1), and the expected output is the logical OR of those two inputs");
		ORDataSet orDS = new ORDataSet();
		
		//Adjusting the acceptable margin of error from the default to 0.01
		orDS.margins.set(0, 0.01);
		System.out.println("We will consider the neural network to be correct if it guesses a value within 0.01 of the actual answer.");
		
		//No further adjustments to the data set, so now we can finalize it.
		orDS.getDataSet();
		
		//Create a neural net with the appropriate number of inputs and outputs, all connected, and no perceptrons in the middle.
		NN = NeuralNetworkBuilder.buildSampleNetwork(orDS, 0);
		
		//Create a neural net analyzer that allows some calculations to be performed on it
		NNA = new GraphNeuralNetworkAnalyzer(NN);
		
		accuracy = NNA.calculateAccuracyOnTrainingData(orDS)*100;
		System.out.println("No training has been done, and the neural network is " + accuracy + "% accurate.");
		
		//Actually get the data
		dataset = orDS.getTrainingData();		
		roundsOfTraining = 0;
		while(accuracy < 95) { //Train it until it's 95% accurate
			for(DataPoint dp : dataset) {
				NN.train(dp);
			}
			accuracy = NNA.calculateAccuracyOnTrainingData(orDS)*100;
			roundsOfTraining++;
		}
		
		System.out.println("After " + roundsOfTraining + " rounds of training, the neural network is " + accuracy + "% accurate.");
		
		System.out.println("Below are the network's guesses for each input: \n");
		
		NNA.printResultsOfTestingData(orDS);
		
		
		/******************************
		 * 
		 *    XOR DATA SET
		 * 
		 ******************************/
		
		System.out.println("\n\nTraining on the XOR data set.\nIn this example, the training and testing data are identical.");
		
		System.out.println("The data set will have four possible inputs, (0,0), (0,1), (1,0), and (1,1), and the expected output is the logical XOR of those two inputs");
		XORDataSet xorDS = new XORDataSet();
		
		//Adjusting the acceptable margin of error from the default to 0.01
		xorDS.margins.set(0, 0.01);
		System.out.println("We will consider the neural network to be correct if it guesses a value within 0.01 of the actual answer.");
		
		//No further adjustments to the data set, so now we can finalize it.
		xorDS.getDataSet();
		
		//Create a neural net with the appropriate number of inputs and outputs, all connected, and one perceptron in the middle.
		NN = NeuralNetworkBuilder.buildSampleNetwork(xorDS, 1);
		
		//Create a neural net analyzer that allows some calculations to be performed on it
		NNA = new GraphNeuralNetworkAnalyzer(NN);
		
		accuracy = NNA.calculateAccuracyOnTrainingData(xorDS)*100;
		System.out.println("No training has been done, and the neural network is " + accuracy + "% accurate.");
		
		//Actually get the data
		dataset = xorDS.getTrainingData();		
		roundsOfTraining = 0;
		while(accuracy < 95) { //Train it until it's 95% accurate
			for(DataPoint dp : dataset) {
				NN.train(dp);
			}
			accuracy = NNA.calculateAccuracyOnTrainingData(xorDS)*100;
			roundsOfTraining++;
		}
		
		System.out.println("After " + roundsOfTraining + " rounds of training, the neural network is " + accuracy + "% accurate.");
		
		System.out.println("Below are the network's guesses for each input: \n");
		
		NNA.printResultsOfTestingData(xorDS);
		
		/******************************
		 * 
		 *    IRIS DATA SET
		 * 
		 ******************************/
		
		System.out.println("\n\nTraining on the Iris data set.\nIn this example, the training and testing data are different.");
		
		System.out.println("The data set will be split to use 70% for training and 30% for testing.");
		IrisDataSet irisDS = new IrisDataSet();
		
		//Adjusting the acceptable margin of error from the default to 0.2
		irisDS.margins.set(0, 0.3);
		System.out.println("The output can be one of three flowers. Consider flower 1 to have a value of -2/3, flower 2 to have a value of 0, and flower 3 to have a value of 2/3");
		System.out.println("We will consider the neural network to be correct if it guesses a value within 0.3 of the actual answer.");
		
		//No further adjustments to the data set, so now we can finalize it.
		irisDS.getDataSet();
		
		//Create a neural net with the appropriate number of inputs and outputs, all connected, and two perceptrons in the middle.
		NN = NeuralNetworkBuilder.buildSampleNetwork(irisDS, 2);
		
		//Create a neural net analyzer that allows some calculations to be performed on it
		NNA = new GraphNeuralNetworkAnalyzer(NN);
		
		accuracy = NNA.calculateAccuracyOnTrainingData(irisDS)*100;
		System.out.println("No training has been done, and the neural network is " + accuracy + "% accurate.");
		
		
		//Actually get the data
		dataset = irisDS.getTrainingData();		
		roundsOfTraining = 0;
		int roundsSinceImprovement = 0;
		double best = accuracy;
		while(roundsSinceImprovement < 100) { //Train it until it hasn't improved for a bit
			for(DataPoint dp : dataset) {
				NN.train(dp);
			}
			accuracy = NNA.calculateAccuracyOnTrainingData(irisDS)*100;
			roundsSinceImprovement++;
			if(accuracy > best) {
				best = accuracy;
				roundsSinceImprovement = 0;
			}
			roundsOfTraining++;
		}
		
		
		System.out.println("After " + roundsOfTraining + " rounds of training, the neural network is " + accuracy + "% accurate.");
		
		
		/******************************
		 * 
		 *    SAVING AND LOADING
		 * 
		 ******************************/

		System.out.println("\n\nSaving the data and neural network under your current working directory as \"DemoData\" and \"DemoNN\"");
		irisDS.saveTo("DemoData");
		NN.saveTo("DemoNN");
		
		System.out.println("Forgetting the data and neural network from RAM");
		irisDS = null;
		NN = null;
		NNA = null;
		
		System.out.println("Loading the data and neural network to RAM");
		SplitDataSet ds = DataSet.loadFrom("DemoData");
		NN = NeuralNetworkBuilder.loadFrom("DemoNN");
		NNA = new GraphNeuralNetworkAnalyzer(NN);
		
		accuracy = NNA.calculateAccuracyOnTrainingData(ds)*100;
		System.out.println("No training has been done since loading, and the neural network is still " + accuracy + "% accurate.");
		
		
	}
}
