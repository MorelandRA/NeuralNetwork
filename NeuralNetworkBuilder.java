import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

	// Creates a basic neural net with the appropriate number of inputs and outputs
	// for the data set,
	// And "intermediate" number of perceptrons as a middle, hidden layer
	static GraphNeuralNetwork buildSampleNetwork(DataSet data, int intermediate) {
		Long perceptronIDCounter = 0L;
		// Count inputs from data
		int numOfInputs = data.getTestingData().get(0).getInputs().size();
		// Count outputs from data
		int numOfOutputs = data.getTestingData().get(0).getOutputs().size();
		// Create lists for the input and output perceptrons
		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();

		// Create input perceptrons
		for (int i = 0; i < numOfInputs; i++) {
			inputs.add(new InputPerceptron(perceptronIDCounter++));
		}

		// Create output perceptrons
		for (int i = 0; i < numOfOutputs; i++) {
			outputs.add(new OutputPerceptron(perceptronIDCounter++));
		}

		// Connect all inputs as parents to outputs
		for (int i = 0; i < numOfInputs; i++) {
			for (int j = 0; j < numOfOutputs; j++) {
				relate(inputs.get(i), outputs.get(j));
			}
		}

		// Create "intermediate" number of perceptrons
		for (int i = 0; i < intermediate; i++) {
			Perceptron newPerceptron = new Perceptron(perceptronIDCounter++);

			// Connect all inputs as parents to intermediate perceptrons
			for (int j = 0; j < numOfInputs; j++) {
				relate(inputs.get(j), newPerceptron);
			}

			// Connect all outputs as children of intermediate perceptrons
			for (int j = 0; j < numOfOutputs; j++) {
				relate(newPerceptron, outputs.get(j));
			}
		}

		// Generate and return the neural network
		return new GraphNeuralNetwork(inputs, outputs);
	}

	// Builds a network with no hidden layer
	static GraphNeuralNetwork buildSimpleNetwork(DataPoint data) {
		return buildSimpleNetwork(data.getInputs().size(), data.getOutputs().size());
	}

	// Sets parent to be the parent of child with a random weight
	static private void relate(Perceptron parent, Perceptron child) {
		relate(parent, child, Perceptron.getRandomWeight());
	}

	// Sets parent to be the parent of child with a chosen weight
	private static void relate(Perceptron parent, Perceptron child, double weight) {
		parent.assignChild(child);
		child.assignParent(parent, weight);
	}

	// Tries to generate a neural network from a chosen save file
	static GraphNeuralNetwork loadFrom(String file) throws IOException {
		Scanner in = null;
		try {
			in = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (in == null)
			throw new IOException("Generating a scanner for the file failed");

		// Maps PerceptronIDs to perceptrons
		Map<Long, Perceptron> perceptrons = new TreeMap<Long, Perceptron>();
		// A list of the inputs to the neural net
		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		// A list of the outputs of the neural net
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();

		// Determine if the network has already been mutated to train more
		String progressedString = in.nextLine();
		boolean progressed = true;
		if (progressedString.equalsIgnoreCase("False")) {
			progressed = false;
		} else if (!progressedString.equalsIgnoreCase("True")) {
			throw new IOException("Invalid progressed value: \"" + progressedString + "\", expected true or false");
		}

		while (in.hasNextLine()) {
			Perceptron newPerceptron;
			String type = in.nextLine();
			Long newPerceptronID = in.nextLong();

			// set newPerceptron to be the proper type, and..
			if (type.equalsIgnoreCase("Input")) {
				// If it's an input perceptron, add it to the inputs list
				newPerceptron = new InputPerceptron(newPerceptronID);
				inputs.add((InputPerceptron) newPerceptron);
			} else if (type.equalsIgnoreCase("Output")) {
				// If it's an output perceptron, add it to the outputs list
				newPerceptron = new OutputPerceptron(newPerceptronID);
				outputs.add((OutputPerceptron) newPerceptron);
			} else {
				if (!type.equalsIgnoreCase("Hidden")) {
					throw new IOException(
							"Invalid perceptron type: \"" + progressedString + "\", expected input, output, or hidden");
				}
				newPerceptron = new Perceptron(newPerceptronID);
			}

			// If the new perceptron has been seen before
			if (perceptrons.containsKey(newPerceptronID)) {
				// Grab the already existing perceptron, that was seen before
				Perceptron placeholderPerceptron = perceptrons.get(newPerceptronID);
				// And for each of its children, make it the new perceptron's child
				for (Perceptron child : placeholderPerceptron.children) {
					relate(newPerceptron, child, child.parentWeightMap.get(placeholderPerceptron));
				}
				// And delete the one that was seen before; the new perceptron now represents it
				placeholderPerceptron.delete();
			}

			// Add the new perceptron to the map
			perceptrons.put(newPerceptron.getPerceptronID(), newPerceptron);

			// Set its bias
			newPerceptron.setBias(in.nextDouble());
			// And for each parent of the new perceptron
			while (in.hasNextLong()) {
				// Get the parent's id
				Long parentID = in.nextLong();
				// If we don't already have it in memory, add a placeholder to memory. We'll
				// replace it when we find it again later.
				if (!perceptrons.containsKey(parentID)) {
					Perceptron parent = new Perceptron(parentID);
					parent.setPerceptronID(parentID);
					perceptrons.put(parentID, parent);
				}
				// Get the weight between the parent and the child
				double weight = in.nextDouble();

				// Get the parent from the map (We might have just made it, as a placeholder)
				Perceptron parent = perceptrons.get(parentID);

				// And add the relationship
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
					newPerceptron = new InputPerceptron(next.getPerceptronID());
					inputs.add((InputPerceptron) newPerceptron);
				} else if (next.getPerceptronType().equalsIgnoreCase("Output")) {
					newPerceptron = new OutputPerceptron(next.getPerceptronID());
					outputs.add((OutputPerceptron) newPerceptron);
				} else {
					newPerceptron = new Perceptron(next.getPerceptronID());
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
		Long perceptronIDCounter = 0L;
		AbstractList<InputPerceptron> inputs = new ArrayList<InputPerceptron>();
		AbstractList<OutputPerceptron> outputs = new ArrayList<OutputPerceptron>();
		for (int i = 0; i < inputSize; i++) {
			inputs.add(new InputPerceptron(perceptronIDCounter++));
		}
		for (int i = 0; i < outputSize; i++) {
			OutputPerceptron output = new OutputPerceptron(perceptronIDCounter++);
			for (InputPerceptron input : inputs) {
				relate(input, output);
			}

			outputs.add(output);
		}
		return new GraphNeuralNetwork(inputs, outputs);
	}
}
