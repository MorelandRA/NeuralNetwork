package data.dataset;

import java.util.AbstractList;

import data.DataPoint;

public class SplitDataSet extends DataSet {

	public SplitDataSet(AbstractList<DataPoint> trainingData, AbstractList<DataPoint> testingData) {
		this.trainingData = trainingData;
		this.testingData = testingData;
	}

	public SplitDataSet(AbstractList<DataPoint> trainingData, AbstractList<DataPoint> testingData,
			AbstractList<Double> margins) {
		this.trainingData = trainingData;
		this.testingData = testingData;
		this.margins = margins;
	}
}
