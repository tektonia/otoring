/*
 * Weighted Moving Average filter
 * 
 * 
 * 2017 - VVS
 */

/**
 * WeightedMovingAverage
 * 
 * Moving Average filter in which every point of the filter can be set with a different weight
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class WeightedMovingAverage extends Filter{
	double [] weight;
	double [] value;
	int idxC=0;
	int dim;
	int idxV=0;
	double min;
	
	/**
	 * WeightedMovingAverage 
	 * 
	 * Input values less than @param min are ignored and assumed to equal @param min.
	 * 
	 * @param w weights
	 * @param ic position of the mean value
	 * @param init initial value for the filter
	 * @param min minimum value of the input
	 */
	public WeightedMovingAverage(double []w, int ic, double init, double min){
		weight=w; value=new double[dim=weight.length];
		idxC=ic;idxV=ic;
		for(int i=0; i<w.length; i++) value[i]=init;
		this.min=min;
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
		value[idxV]=d<min ? min:d;
		double soma=0.0;
		for(int i=0; i<dim; i++){
			int idxp=(i+idxC)%dim;
			int act=(i+idxV)%dim;
			soma+=value[act]*weight[idxp];
		}
		++idxV;
		idxV%=dim;
		return soma;
	}
}
