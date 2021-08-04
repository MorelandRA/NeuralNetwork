package data.dataset;

public abstract class DefinedDataSet extends DataSet {
	abstract public void getDataSet();
	
	protected double trainingPartition = 0.7;
	
	public void setTrainingPartition(double trainingPartition) {
		this.trainingPartition = trainingPartition;
	}
}
