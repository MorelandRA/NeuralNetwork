import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class DataTransformer {

	public static void main(String[] args) throws Exception {

	}

	static Hashtable<String, Double> enumerateStrings(String[] sarr){
		Hashtable<String, Double> enumerations = new Hashtable<String, Double>();
		int index = 0;
		for(String s : sarr){
			if(enumerations.get(s) == null){
				enumerations.putIfAbsent(s, (double) index++);
			}
		}
		
		int keySize = index;
		
		for(String key : enumerations.keySet()){
			enumerations.put(key, ((enumerations.get(key)+0.5)/(double)(keySize))*2 - 1);
		}
		
		return enumerations;
	}
	
	static double linearNormalize(double val, double min, double max){
		return (val-min)/(max-min);
	}
	
	static ArrayList<Double> intToOneHot(int val, int min, int max) throws Exception{
		if(val < min || val > max) throw new Exception("val " + val + " is out of bounds."
				+ "\n Min = " + min + " Max = " + max);
		
		ArrayList<Double> bin = new ArrayList<Double>();
		
		val -= min;
		max -= min;
		
		for(int i = min; i <= max; i++){
			if(i != val) bin.add(1.);
			else bin.add(0.);
		}
		
		return bin;
	}
	
	static ArrayList<Double> intToBinary(int val, int min, int max) throws Exception{
		if(val < min || val > max) throw new Exception("val " + val + " is out of bounds."
				+ "\n Min = " + min + " Max = " + max);
		
		ArrayList<Double> bin = new ArrayList<Double>();
		
		val -= min;
		max -= min;
		
		while(max > 0){
			if((val&0b1) == 0){
				bin.add(0.);
			} else {
				bin.add(1.);
			}
			val = val>>1;
			max = max>>1;
		}
		return bin;
	}
}
