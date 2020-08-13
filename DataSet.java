import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public abstract class DataSet {
	protected AbstractList<DataPoint> trainingData = new ArrayList<DataPoint>();
	protected AbstractList<DataPoint> testingData = new ArrayList<DataPoint>();
	protected AbstractList<Double> margins = new ArrayList<Double>();
	long size = 0;

	AbstractList<DataPoint> getTrainingData(){
		return trainingData;
	}
	
	AbstractList<DataPoint> getTestingData(){
		return testingData;
	}
	
	AbstractList<Double> getMargins(){
		return margins;
	}
	
	long getSize() {
		return size;
	}
	
	
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("Training Data:\n");
		for(DataPoint data : trainingData) {
			buff.append(data.toString() + "\n");
		}
		buff.append("Testing Data:\n");
		for(DataPoint data : testingData) {
			buff.append(data.toString() + "\n");
		}
		return buff.toString();
	}
	
	void saveTo(String file) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);

			if (writer != null) {
				writer.write(trainingData.size() + "\n");
				writer.write(testingData.size() + "\n");
				writer.write(trainingData.get(0).getInputs().size() + "\n");
				writer.write(trainingData.get(0).getOutputs().size() + "\n");
				
				for(Double margin : margins) {
					writer.write(margin + "\n");
				}
				
				for(DataPoint dp : trainingData) {
					for(Double input : dp.getInputs()) {
						writer.write(input + "\n");
					}
					for(Double output : dp.getOutputs()) {
						writer.write(output + "\n");
					}
				}
				
				for(DataPoint dp : testingData) {
					for(Double input : dp.getInputs()) {
						writer.write(input + "\n");
					}
					for(Double output : dp.getOutputs()) {
						writer.write(output + "\n");
					}
				}
			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static SplitDataSet loadFrom(String file) {
		Scanner in = null;
		try {
			in = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (in == null)
			return null;

		int trainSize = in.nextInt();
		int testSize = in.nextInt();
		int inputSize = in.nextInt();
		int outputSize = in.nextInt();
		
		AbstractList<DataPoint> trainingData = new ArrayList<DataPoint>();
		AbstractList<DataPoint> testingData = new ArrayList<DataPoint>();
		
		AbstractList<Double> margins = new ArrayList<Double>();
		for(int i = 0; i < outputSize; i++) {
			margins.add(in.nextDouble());
		}
		
		
		for(int i = 0; i < trainSize; i++) {
			AbstractList<Double> inputs = new ArrayList<Double>();
			AbstractList<Double> outputs = new ArrayList<Double>();
			for(int j = 0; j < inputSize; j++) {
				inputs.add(in.nextDouble());
			}
			for(int j = 0; j < outputSize; j++) {
				outputs.add(in.nextDouble());
			}
			DataPoint dp = new DataPoint(inputs, outputs);
			trainingData.add(dp);
		}
		for(int i = 0; i < testSize; i++) {
			AbstractList<Double> inputs = new ArrayList<Double>();
			AbstractList<Double> outputs = new ArrayList<Double>();
			for(int j = 0; j < inputSize; j++) {
				inputs.add(in.nextDouble());
			}
			for(int j = 0; j < outputSize; j++) {
				outputs.add(in.nextDouble());
			}
			DataPoint dp = new DataPoint(inputs, outputs);
			testingData.add(dp);
		}

		return new SplitDataSet(trainingData, testingData, margins);
	}
}
