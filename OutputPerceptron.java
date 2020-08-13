import java.util.Map.Entry;

public class OutputPerceptron extends Perceptron {
	private double actualValue;

	@Override
	public void delete() {
		throw new UnsupportedOperationException("An output should not be deleted!");
	}

	@Override
	public void assignChild(Perceptron child) {
		throw new UnsupportedOperationException("An output should not have any children!");
	}

	@Override
	public double getValue() { // forces the value to update just to be safe.
		updateValue();
		return super.getValue();
	}

	@Override
	public String getPerceptronType() {
		return "Output";
	}

	@Override
	protected void updateWeights() {
		if (!this.weightsUpdated) {
			err = actualValue - value;

			// This is the part where this perceptron's weights are actually updated

			for (Entry<Perceptron, Double> parentWeightPair : parentWeightMap.entrySet()) { 
				double newValue = parentWeightPair.getValue()
						+ LEARNING_RATE * value * (1 - value) * err * parentWeightPair.getKey().getValue();

				parentWeightPair.setValue(newValue);
			}
			bias += LEARNING_RATE * value * (1 - value) * err; // Changed to try to un-limit the domain from -1 to 1;

			this.weightsUpdated = true;
		}
	}

	public void setActualValue(Double actualValue) {
		this.actualValue = actualValue;
		setWeightsUpdated(false);
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
			str.append("\nNumber of inputs: " + parentWeightMap.size());

			str.append("\nValue Pre-activation: " + getValuePreActivation());

			str.append("\nValue: " + getValue());
			str.append("\nBias: " + bias);
			str.append("\nInputs: " + getParentDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str.toString();
	}
	
	@Override
	public boolean remove() {
		return false;
	}
}
