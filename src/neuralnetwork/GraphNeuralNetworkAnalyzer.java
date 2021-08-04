package neuralnetwork;
import java.util.AbstractList;

import data.DataPoint;
import data.dataset.DataSet;
import neuralnetwork.perceptron.OutputPerceptron;

public class GraphNeuralNetworkAnalyzer {
	GraphNeuralNetwork neuralNetwork;
	
	public GraphNeuralNetworkAnalyzer(GraphNeuralNetwork neuralNetwork){
		this.neuralNetwork = neuralNetwork;
	}
	
	double calculateAccuracyOnTestingData(DataSet dataSet){
		int correct = 0;
		
		for(DataPoint data : dataSet.getTestingData()) {
			AbstractList<Double> prediction = neuralNetwork.makePrediction(data);
			for(int i = 0; i < prediction.size(); i++) {
				if(Math.abs(prediction.get(i) - data.getOutputs().get(i)) <= dataSet.getMargins().get(i)) {
					correct++;
				}
			}
		}
		
		return correct/(double)dataSet.getTestingData().size();
	}
	
	public double calculateAccuracyOnTrainingData(DataSet dataSet){
		int correct = 0;
		
		for(DataPoint data : dataSet.getTrainingData()) {
			AbstractList<Double> prediction = neuralNetwork.makePrediction(data);
			for(int i = 0; i < prediction.size(); i++) {
				if(Math.abs(prediction.get(i) - data.getOutputs().get(i)) <= dataSet.getMargins().get(i)) {
					correct++;
				}
			}
		}
		
		return correct/(double)dataSet.getTrainingData().size();
	}
	
	public void printResultsOfTestingData(DataSet dataSet){
		for(DataPoint data : dataSet.getTestingData()) {
			System.out.println("Data Point: " + data.toString());
			neuralNetwork.makePrediction(data);
			System.out.println("Predicted Value: " + getCurrentOutputAsString());
		}
	}
	
	private String getCurrentOutputAsString(){
		StringBuffer buff = new StringBuffer();
		for(OutputPerceptron output : neuralNetwork.outputPerceptrons) {
			output.updateValue();
			buff.append(output.getValue());
			buff.append(" ");
		}
		return buff.toString();
	}
}
