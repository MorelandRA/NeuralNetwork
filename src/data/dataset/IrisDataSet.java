package data.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import data.DataPoint;
import data.DataTransformer;

public class IrisDataSet extends DefinedDataSet {
	AbstractList<DataPoint> irisData;
	
	public IrisDataSet() {
		margins = new ArrayList<Double>();
		margins.add(0.1);
		try {
			irisData = getDataPoints();
		} catch (Exception e) {
			e.printStackTrace();
		}
		size = 150;
	}

	@Override
	public void getDataSet() {
		for(DataPoint dp : irisData) {
			if(Math.random() < trainingPartition) {
				trainingData.add(dp);
			} else {
				testingData.add(dp);
			}
		}
	}
	
	private static ArrayList<DataPoint> getDataPoints() throws Exception{
		String irisLoc = "src/resources/Iris.csv";
		File file = new File(irisLoc);
		Scanner in = null;
		try {
			in = new Scanner(file).useDelimiter(",|\\s+");
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("The Iris dataset was unable to be found. It should be in " + irisLoc);
		}
		ArrayList<DataPoint> dataSet = new ArrayList<DataPoint>();
		
		String[] sarr = {"Iris-setosa",
				"Iris-versicolor", "Iris-virginica"};
		
		Hashtable<String,Double> enums = DataTransformer.enumerateStrings(sarr);
		
		for(int i = 0; i < 150; i++){
			ArrayList<Double> inputs = new ArrayList<Double>();
			inputs.add(DataTransformer.linearNormalize(in.nextDouble(), 4.3, 7.9));
			inputs.add(DataTransformer.linearNormalize(in.nextDouble(), 2.0, 4.4));
			inputs.add(DataTransformer.linearNormalize(in.nextDouble(), 1.0, 6.9));
			inputs.add(DataTransformer.linearNormalize(in.nextDouble(), 0.1, 2.5));
			double output = enums.get(in.next());
			
			dataSet.add(new DataPoint(inputs,output));
		}
		
		in.close();
		
		return dataSet;
	}
}
