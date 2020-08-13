
public class InputPerceptron extends Perceptron {
	InputPerceptron(){
		valueUpdated = true;
	}
	
	void setValue(double value) {
		valueUpdated = true;
		this.value = value;
		for(Perceptron child : children) {
			child.setValueUpdated(false);
		}
	}
	
	@Override
	public void delete() {
		throw new UnsupportedOperationException("An input should not be deleted!");
	}
	
	@Override
	public void assignParent(Perceptron parent) {
		throw new UnsupportedOperationException("An input should not have any parents!");
	}

	public void updateValue() {
		
	}

	@Override
	public void forgetParent(Perceptron parent) {
		throw new UnsupportedOperationException("An input should not have any parents!");
	}
	
	@Override
	public String getPerceptronType() {
		return "Input";
	}
	
	@Override
	public void updateWeights(){
		weightsUpdated = true;
		for(Perceptron child : children) {
			child.updateWeights();
		}
	}
	
	@Override
	public String toString() {
		if(!valueUpdated) {
			updateValue();
		}
		
		StringBuffer str = new StringBuffer();
		try {
		str.append("Perceptron ID: " + getPerceptronID());
		str.append("\nPerceptron type: " + getPerceptronType());
		str.append("\nValue: " + getValue());
		str.append("\nOutput IDs: " + getChildDetails());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
	}
	
	@Override
	public boolean remove() {
		
		return false;
	}
}
