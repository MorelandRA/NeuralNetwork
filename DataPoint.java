import java.util.AbstractList;
import java.util.ArrayList;

public class DataPoint {
	private AbstractList<Double> inputs = new ArrayList<Double>();
	private AbstractList<Double> outputs = new ArrayList<Double>();

	public AbstractList<Double> getInputs() {
		return inputs;
	}

	public AbstractList<Double> getOutputs() {
		return outputs;
	}
	
	DataPoint(AbstractList<Double> inputs, AbstractList<Double> outputs){
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	DataPoint(AbstractList<Double> inputs, Double output){
		this.inputs = inputs;
		AbstractList<Double> outputs = new ArrayList<Double>();
		outputs.add(output);
		this.outputs = outputs;
	}
	
	DataPoint(){
	}

	public void setInputs(AbstractList<Double> inputs) {
		this.inputs = inputs;
	}

	public void setOutputs(AbstractList<Double> outputs) {
		this.outputs = outputs;
	}
	
	public String toString() {
		StringBuffer buff = new StringBuffer();
		
		buff.append("Inputs: ");
		for(Double input : inputs) {
			buff.append(" " + input);
		}
		
		buff.append("         Outputs: ");
		for(Double output : outputs) {
			buff.append(" " + output);
		}
		return buff.toString();
	}
}
