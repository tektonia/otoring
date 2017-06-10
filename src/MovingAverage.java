/*
 * Moving Average filter
 * 
 *   Uses the Weighted Moving Average Filter as a specific case
 * where all weights are equal to 1.0
 * 
 * 2017 - VVS
 */

/**
 * MovingAverage
 * 
 * A moving average filter
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class MovingAverage extends Filter{
	WeightedMovingAverage mp;
	
	/**
	 * MovingAverage
	 * 
	 * 	Creates a Moving Average filter with @param num filtering points.
	 *  The filter is set with @param init as initial value
	 *  All input filter values less than @param min are replaced by @param min value
	 * 
	 * @param num
	 * @param init
	 * @param min
	 */
	MovingAverage(int num, double init, double min){
		if(init<min) init=min;
		double [] weight= new double[num];
		for(int i=0; i<weight.length; i++) weight[i]=1.0/num;
		mp=new WeightedMovingAverage(weight,num/2,init,min);
	}
	
	/**
	 * output
	 * 
	 * Returns the filtered input (double) @param d value
	 * 
	 * @param d
	 * @return
	 */
	@Override
	public double output(double d) {
		return mp.output(d);
	}
}
