package neuralnetwork.perceptron;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * 
 *  https://books.google.com/books?id=9bdwtUQLchIC&pg=PA275&lpg=PA275&dq=%22learning+rate++delta%22&source=bl&ots=J73mk8Pqg9&sig=ACfU3U2hfNkKBcMliY5Pmuqx6FZg-zLQMA&hl=en&sa=X&ved=2ahUKEwjfiJjujvbpAhVBQq0KHZGdCuQQ6AEwAXoECAgQAQ#v=onepage&q&f=true
 * 
 */

public class Perceptron implements Comparable<Perceptron> {
	protected double value;
	protected double valuePreActivation;
	private double bias = Math.random() * 2 - 1;
	private Map<Perceptron, Double> parentWeightMap = new TreeMap<Perceptron, Double>();
	private Set<Perceptron> children = new TreeSet<Perceptron>();
	protected boolean weightsUpdated = false;
	protected boolean valueUpdated = false;
	protected double err;

	private long perceptronID;

	protected static double LEARNING_RATE = 0.5;
	protected static double MOMENTUM = 0.3;

	public Perceptron(long id) {
		perceptronID = id;
	}

	@Override
	public String toString() {

		if (!valueUpdated) {
			updateValue();
		}

		StringBuffer str = new StringBuffer();
		try {

			str.append("Perceptron ID: " + getPerceptronID());
			str.append("\nPerceptron type: " + getPerceptronType());
			str.append("\nNumber of inputs: " + getParentWeightMap().size());
			str.append("\nValue Pre-activation: " + getValuePreActivation());

			str.append("\nValue: " + getValue());

			str.append("\nBias: " + getBias());
			str.append("\nInputs: " + getParentDetails());
			str.append("\nOutput IDs: " + getChildDetails());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();
	}

	public void setPerceptronID(long id) {
		perceptronID = id;
	}

	protected double getValuePreActivation() {
		if (!valueUpdated) {
			System.out.println("WARNING: Attempted to get pre-activation value before updating the value.");
			updateValue();
		}
		return valuePreActivation;
	}

	protected String getParentDetails() {
		StringBuffer str = new StringBuffer();
		for (Entry<Perceptron, Double> parentWeightPair : getParentWeightMap().entrySet()) {
			str.append("\n" + parentWeightPair.getKey().getPerceptronID() + " " + parentWeightPair.getValue());
		}
		return str.toString();
	}

	protected String getChildDetails() {
		StringBuffer str = new StringBuffer();
		for (Perceptron child : getChildren()) {
			str.append(child.getPerceptronID() + " ");
		}
		return str.toString();
	}

	public long getPerceptronID() {
		return perceptronID;
	}

	public String getPerceptronType() {
		return "Hidden";
	}

	protected void setValueUpdated(boolean valueUpdated) {
		if (!valueUpdated && this.valueUpdated) {
			for (Perceptron child : getChildren()) {
				child.setValueUpdated(false);
			}
		}
		this.valueUpdated = valueUpdated;
	}

	protected void setWeightsUpdated(boolean weightsUpdated) {
		if (!weightsUpdated && this.weightsUpdated) {
			for (Perceptron parent : getParentWeightMap().keySet()) {
				parent.setWeightsUpdated(false);
			}
		}
		this.weightsUpdated = weightsUpdated;
	}

	protected void updateWeights() { // Back Propagate
		if (!this.weightsUpdated) {
			// Update all descendants first
			for (Perceptron child : getChildren()) {
				child.updateWeights();
			}

			err = 0;
			for (Perceptron child : getChildren()) {
				err += child.err * child.getParentWeightMap().get(this);
			}

			// This is the part where this perceptron's weights are actually updated

			for (Entry<Perceptron, Double> parentWeightPair : getParentWeightMap().entrySet()) {
				double newValue = parentWeightPair.getValue()
						+ LEARNING_RATE * value * (1 - value) * err * parentWeightPair.getKey().getValue();

				parentWeightPair.setValue(newValue);
			}
			setBias(getBias() + LEARNING_RATE * value * (1 - value) * err);

			this.weightsUpdated = true;
		}
	}

	public void updateValue() { // Forward Propagate
		if (!this.valueUpdated) {
			valuePreActivation = getBias();
			for (Perceptron parent : getParentWeightMap().keySet()) {
				// Update the ancestor
				parent.updateValue();
				// Then consider its value
				valuePreActivation += parent.getValue() * getParentWeightMap().get(parent);
			}
			value = activationFunction(valuePreActivation);

			this.valueUpdated = true;
			this.weightsUpdated = false;
		}
	}

	protected double getValue() {
		if (!valueUpdated) {
			System.out.println("WARNING: Attempted to get value before updating the value.");
			updateValue();
		}
		return value;
	}

	private double activationFunction(double x) {
		return sigmoidActivation(x);
	}

	private double sigmoidActivation(double x) {
		if (x > 100)
			return 1.;
		if (x < -100)
			return 0.;
		double ex = Math.pow(Math.E, -x);
		return 1. / (1. + ex);
	}

	public void delete() {
		for (Perceptron child : getChildren()) {
			child.forgetParent(this);
		}
		// No perceptron points to this as its parent anymore

		for (Perceptron parent : getParentWeightMap().keySet()) {
			parent.forgetChild(this);
		}
		// No perceptron points to this as its child anymore

		// Nothing points to this; garbage collector will pick it up
	}

	private void forgetChild(Perceptron child) {
		getChildren().remove(child);
	}

	public void forgetParent(Perceptron parent) {
		if (getParentWeightMap().containsKey(parent)) {
			parent.forgetChild(this);
			getParentWeightMap().remove(parent);
		}
	}

	public void assignChild(Perceptron child) {
		getChildren().add(child);
	}

	static public double getRandomWeight() {
		return Math.random() * 2. - 1;
	}
	
	public void assignParent(Perceptron parent) {
		double newWeight = getRandomWeight();

		getParentWeightMap().put(parent, newWeight);
		setValueUpdated(false);
	}

	public void assignParent(Perceptron parent, double weight) {
		getParentWeightMap().put(parent, weight);
	}

	@Override
	public int compareTo(Perceptron comparing) {
		return Integer.compare(this.hashCode(), comparing.hashCode());
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public boolean remove() {
		for (Entry<Perceptron, Double> parent : getParentWeightMap().entrySet()) {
			parent.getKey().forgetChild(this);
		}
		for (Perceptron child : getChildren()) {
			forgetChild(child);
		}
		return true;
	}
	
	public boolean isAncestorOf(Perceptron node) {
		Set<Perceptron> added = new TreeSet<Perceptron>();
		LinkedList<Perceptron> deque = new LinkedList<Perceptron>();
		
		deque.add(this);

		while (!deque.isEmpty()) {
			Perceptron next = deque.pop();
			if (!added.contains(next)) {
				
				if(next == node) return true;
				
				for (Perceptron child : next.getChildren()) {
					deque.add(child);
				}
				added.add(next);
			}
		}
		
		return false;
	}

	public double getBias() {
		return bias;
	}

	public Map<Perceptron, Double> getParentWeightMap() {
		return parentWeightMap;
	}

	public void setParentWeightMap(Map<Perceptron, Double> parentWeightMap) {
		this.parentWeightMap = parentWeightMap;
	}

	public Set<Perceptron> getChildren() {
		return children;
	}

	public void setChildren(Set<Perceptron> children) {
		this.children = children;
	}
}
