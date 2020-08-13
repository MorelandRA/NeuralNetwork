import java.util.AbstractList;
import java.util.ArrayList;

public class ConstantDataSet extends DefinedDataSet {

	double constant;
	
	ConstantDataSet(double val) {
		margins = new ArrayList<Double>();
		margins.add(0.1);
		constant = val;
		size = 4;
	}

	@Override
	public void getDataSet() {
		trainingData = new ArrayList<DataPoint>();

		trainingData.add(generateConstantDataPoint(0, 0));
		trainingData.add(generateConstantDataPoint(0, 1));
		trainingData.add(generateConstantDataPoint(1, 0));
		trainingData.add(generateConstantDataPoint(1, 1));

		testingData = trainingData;
	}

	private DataPoint generateConstantDataPoint(double in1, double in2) {
		DataPoint data = new DataPoint();

		AbstractList<Double> inputs = new ArrayList<Double>();

		inputs.add(in1);
		inputs.add(in2);

		data.setInputs(inputs);

		AbstractList<Double> outputs = new ArrayList<Double>();
		outputs.add(constant);
		data.setOutputs(outputs);

		return data;
	}
}
