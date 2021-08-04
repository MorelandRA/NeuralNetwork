import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Map.Entry;

public class NeuralNetworkBuilder {

	static GraphNeuralNetwork buildSampleNetwork(DataSet data, int intermediate) {
		int numOfInputs = data.getTestingData().get(0).getInputs().size();
		int numOfOutputs = data.getTestingData().get(0).getOutputs().size();
		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();

		for (int i = 0; i < numOfInputs; i++) {
			inputs.add(new InputPerceptron());
		}

		for (int i = 0; i < numOfOutputs; i++) {
			outputs.add(new OutputPerceptron());
		}
		
		for(int i = 0; i < numOfInputs; i++) {
			for(int j = 0; j < numOfOutputs; j++) {
				relate(inputs.get(i), outputs.get(j));
			}
		}

		for(int i = 0; i < intermediate; i++) {
			Perceptron newPerceptron = new Perceptron();
			
			for(int j = 0; j < numOfInputs; j++) {
				relate(inputs.get(j), newPerceptron);
			}
			
			for(int j = 0; j < numOfOutputs; j++) {
				relate(newPerceptron, outputs.get(j));
			}
		}

		return new GraphNeuralNetwork(inputs, outputs);
	}

	static GraphNeuralNetwork buildSimpleNetwork(DataPoint data) {
		return buildSimpleNetwork(data.getInputs().size(), data.getOutputs().size());
	}

	static private void relate(Perceptron parent, Perceptron child) {
		parent.assignChild(child);
		child.assignParent(parent);
	}

	private static void relate(Perceptron parent, Perceptron child, double weight) {
		parent.assignChild(child);
		child.assignParent(parent, weight);
	}

	static GraphNeuralNetwork loadFrom(String file) {
		Scanner in = null;
		try {
			in = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (in == null) return null;

		Map<Long, Perceptron> perceptrons = new TreeMap<Long, Perceptron>();
		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();
		
		String progressedString = in.nextLine();
		boolean progressed = true;
		if(progressedString.equalsIgnoreCase("False")) {
			progressed = false;
		} else if(!progressedString.equalsIgnoreCase("True")) {
			System.out.println("Invalid Progress! Assuming True");
		}
		
		
		while (in.hasNextLine()) {
			Perceptron newPerceptron;
			String type = in.nextLine();
			Long newPerceptronID = in.nextLong();
			
			
			if (type.equalsIgnoreCase("Input")) {
				newPerceptron = new InputPerceptron();
				inputs.add((InputPerceptron) newPerceptron);
			} else if (type.equalsIgnoreCase("Output")) {
				newPerceptron = new OutputPerceptron();
				outputs.add((OutputPerceptron) newPerceptron);
			} else {
				if (!type.equalsIgnoreCase("Hidden")){
					System.out.println("Invalid type! Assuming Hidden");
				}
				newPerceptron = new Perceptron();
			}
			
			if(perceptrons.containsKey(newPerceptronID)) {
				Perceptron placeholderPerceptron = perceptrons.get(newPerceptronID);
				//TODO: Do this better
				for(Perceptron child : placeholderPerceptron.children) {
					relate(newPerceptron, child, child.parentWeightMap.get(placeholderPerceptron));
				}
				placeholderPerceptron.delete();
			}
			
			newPerceptron.setPerceptronID(newPerceptronID);
			perceptrons.put(newPerceptron.getPerceptronID(), newPerceptron);
			
			
			newPerceptron.setBias(in.nextDouble());
			while (in.hasNextLong()) {
				Long parentID = in.nextLong();
				if(!perceptrons.containsKey(parentID)) {
					Perceptron parent = new Perceptron();
					parent.setPerceptronID(parentID);
					perceptrons.put(parentID, parent);
				}
				
				double weight = in.nextDouble();
	
				Perceptron parent = perceptrons.get(parentID);
	
				relate(parent, newPerceptron, weight);
			}
			in.nextLine();
		}
		
		GraphNeuralNetwork net = new GraphNeuralNetwork(inputs, outputs);

		net.progressed = progressed;
		
		return net;
	}

	static public GraphNeuralNetwork clone(GraphNeuralNetwork nn) {
		LinkedList<Perceptron> deque = new LinkedList<Perceptron>();
		Set<Perceptron> added = new TreeSet<Perceptron>();

		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();
		Map<Long, Perceptron> perceptrons = new TreeMap<Long, Perceptron>();

		deque.addAll(nn.inputPerceptrons);

		while (!deque.isEmpty()) {
			Perceptron next = deque.pop();
			if (!added.contains(next)) {

				Perceptron newPerceptron;

				if (next.getPerceptronType().equalsIgnoreCase("Input")) {
					newPerceptron = new InputPerceptron();
					inputs.add((InputPerceptron) newPerceptron);
				} else if (next.getPerceptronType().equalsIgnoreCase("Output")) {
					newPerceptron = new OutputPerceptron();
					outputs.add((OutputPerceptron) newPerceptron);
				} else {
					newPerceptron = new Perceptron();
				}

				newPerceptron.setPerceptronID(next.getPerceptronID());
				perceptrons.put(newPerceptron.getPerceptronID(), newPerceptron);
				newPerceptron.setBias(next.bias);
				for (Entry<Perceptron, Double> parentWeightPair : next.parentWeightMap.entrySet()) {
					Long parentID = parentWeightPair.getKey().getPerceptronID();
					double weight = parentWeightPair.getValue();

					Perceptron parent = perceptrons.get(parentID);

					relate(parent, newPerceptron, weight);
				}

				for (Perceptron child : next.children) {
					deque.add(child);
				}
				added.add(next);
			}
		}

		return new GraphNeuralNetwork(inputs, outputs);
	}

	public static GraphNeuralNetwork buildSimpleNetwork(int inputSize, int outputSize) {
		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();
		for (int i = 0; i < inputSize; i++) {
			inputs.add(new InputPerceptron());
		}
		for (int i = 0; i < outputSize; i++) {
			OutputPerceptron output = new OutputPerceptron();
			for (InputPerceptron input : inputs) {
				relate(input, output);
			}

			outputs.add(output);
		}
		return new GraphNeuralNetwork(inputs, outputs);
	}
}
