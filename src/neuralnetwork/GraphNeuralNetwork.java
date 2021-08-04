package neuralnetwork;

import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import data.DataPoint;
import neuralnetwork.perceptron.*;


public class GraphNeuralNetwork implements Comparable<GraphNeuralNetwork>{
	AbstractList<InputPerceptron> inputPerceptrons = new ArrayList<InputPerceptron>();
	AbstractList<OutputPerceptron> outputPerceptrons = new ArrayList<OutputPerceptron>();
	int saveCount = -1;
	boolean progressed = false;

	public void saveTo(String file) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);

			if (writer != null) {
				LinkedList<Perceptron> deque = new LinkedList<Perceptron>();
				Set<Perceptron> added = new TreeSet<Perceptron>();

				writer.write(progressed + "\n");
				
				deque.addAll(inputPerceptrons);

				while (!deque.isEmpty()) {
					Perceptron next = deque.pop();
					if (!added.contains(next)) {
						writer.write(next.getPerceptronType() + "\n");
						writer.write(next.getPerceptronID() + "\n");
						writer.write(next.getBias() + "\n");
						for (Entry<Perceptron, Double> parentWeightSet : next.getParentWeightMap().entrySet()) {
							writer.write(parentWeightSet.getKey().getPerceptronID() + "\n");
							writer.write(parentWeightSet.getValue() + "\n");
						}

						for (Perceptron child : next.getChildren()) {
							deque.add(child);
						}
						added.add(next);
					}
				}
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	GraphNeuralNetwork(AbstractList<InputPerceptron> inputPerceptrons,
			AbstractList<OutputPerceptron> outputPerceptrons) {
		this.inputPerceptrons = inputPerceptrons;
		this.outputPerceptrons = outputPerceptrons;
	}

	public void train(DataPoint data) {
		forwardPropagate(data.getInputs());
		backPropagate(data.getOutputs());
	}

	private void forwardPropagate(AbstractList<Double> inputs) {
		if (inputs.size() != inputPerceptrons.size())
			throw new IllegalArgumentException("The data point has " + inputs.size()
					+ " inputs but the network expects " + inputPerceptrons.size() + " inputs.");

		for (int i = 0; i < inputPerceptrons.size(); i++) { // Tell the network what the inputs are
			inputPerceptrons.get(i).setValue(inputs.get(i));
		}

		for (int i = 0; i < outputPerceptrons.size(); i++) { // And tell it to update the output values
			outputPerceptrons.get(i).updateValue();
		}
	}

	private void backPropagate(AbstractList<Double> outputs) {
		for (int i = 0; i < outputPerceptrons.size(); i++) { // Tell the network what the expected outputs were
			outputPerceptrons.get(i).setActualValue(outputs.get(i));
		}

		for (int i = 0; i < inputPerceptrons.size(); i++) { // Make the network adjust all of its weights
			inputPerceptrons.get(i).updateWeights();
		}
	}

	public AbstractList<Double> makePrediction(DataPoint data) {
		return makePrediction(data.getInputs());
	}

	public AbstractList<Double> makePrediction(AbstractList<Double> inputs) {
		forwardPropagate(inputs);

		AbstractList<Double> results = new ArrayList<Double>();
		for (OutputPerceptron output : outputPerceptrons) {
			results.add(output.getValue());
		}

		return results;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();
		LinkedList<Perceptron> deque = new LinkedList<Perceptron>();
		Set<Perceptron> added = new TreeSet<Perceptron>();

		deque.addAll(inputPerceptrons);

		while (!deque.isEmpty()) {
			Perceptron next = deque.pop();
			if (!added.contains(next)) {
				buff.append(next);
				buff.append("\n\n");
				for (Perceptron child : next.getChildren()) {
					deque.add(child);
				}
				added.add(next);
			}
		}

		return buff.toString();

	}

	@Override
	public int compareTo(GraphNeuralNetwork arg0) {
		return ((Integer)this.hashCode()).compareTo(arg0.hashCode());
	}

	public Set<Perceptron> getPerceptronSet() {
		Set<Perceptron> perceptronSet = new TreeSet<Perceptron>();
		
		LinkedList<Perceptron> deque = new LinkedList<Perceptron>();
		
		deque.addAll(inputPerceptrons);

		while (!deque.isEmpty()) {
			Perceptron next = deque.pop();
			if (!perceptronSet.contains(next)) {
				for (Perceptron child : next.getChildren()) {
					deque.add(child);
				}
				perceptronSet.add(next);
			}
		}
		
		for(Perceptron output : outputPerceptrons) {
			perceptronSet.add(output);
		}
		
		return perceptronSet;
	}
}
