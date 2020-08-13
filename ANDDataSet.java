import java.util.AbstractList;
import java.util.ArrayList;

public class ANDDataSet extends DefinedDataSet {

	ANDDataSet() {
		margins = new ArrayList<Double>();
		margins.add(0.3);
		size = 4;
	}

	@Override
	public void getDataSet() {
		trainingData = new ArrayList<DataPoint>();

		trainingData.add(generateANDDataPoint(0, 0));
		trainingData.add(generateANDDataPoint(0, 1));
		trainingData.add(generateANDDataPoint(1, 0));
		trainingData.add(generateANDDataPoint(1, 1));

		testingData = trainingData;
	}

	private DataPoint generateANDDataPoint(double in1, double in2) {
		DataPoint data = new DataPoint();

		AbstractList<Double> inputs = new ArrayList<Double>();

		inputs.add(in1);
		inputs.add(in2);

		data.setInputs(inputs);

		AbstractList<Double> outputs = new ArrayList<Double>();
		outputs.add((in1 + in2 == 2. ? 1. : 0.));
		data.setOutputs(outputs);

		return data;
	}
}
