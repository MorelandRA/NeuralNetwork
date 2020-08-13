import java.util.AbstractList;
import java.util.ArrayList;

public class XORDataSet extends DefinedDataSet {

	XORDataSet() {
		margins = new ArrayList<Double>();
		margins.add(0.5);
		size = 4;
		
	}

	@Override
	public void getDataSet() {
		trainingData = new ArrayList<DataPoint>();

		trainingData.add(generateXORDataPoint(0, 0));
		trainingData.add(generateXORDataPoint(0, 1));
		trainingData.add(generateXORDataPoint(1, 0));
		trainingData.add(generateXORDataPoint(1, 1));

		testingData = trainingData;
	}

	private DataPoint generateXORDataPoint(double in1, double in2) {
		DataPoint data = new DataPoint();

		AbstractList<Double> inputs = new ArrayList<Double>();

		inputs.add(in1);
		inputs.add(in2);

		data.setInputs(inputs);

		AbstractList<Double> outputs = new ArrayList<Double>();
		outputs.add((in1 + in2 == 1. ? 1. : 0.));
		data.setOutputs(outputs);

		return data;
	}
}
