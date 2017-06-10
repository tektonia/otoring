/*
 * Filter class to be used by different filters
 * 
 * 2017 - VVS
 */

/**
 * Filter
 *
 * Abstract class for different filters
 * 
 * @author Vitor Vaz da Silva
 *
 */
public  abstract class Filter{
	
	/**
	 * output
	 * 
	 *   Returns a filtered value related to the (double) @param d input 
	 *   
	 * @param d
	 * @return
	 */
	public abstract double output(double d); 
	
	/**
	 * output
	 * 
	 *   Returns a filtered value related to the (double []) @param val input
	 * 
	 * @param val
	 * @return
	 */
	public double[] output(double [] val){
		double vv[]=new double[val.length];
		int idx=0;
		for(double d: val){
			vv[idx]= output(d);
		}
		return vv;
	}

	/**
	 * output
	 * 
	 *   Returns a filtered value related to the (Pixel []) @param px input
	 * 
	 * @param px
	 * @return
	 */
	public Pixel[] output(Pixel[] px) {
		Pixel []res= new Pixel[px.length];
		int idx=0;
		for(Pixel p:px){
			res[idx++]=output(p);
		}
		return res;
	}
	
	/**
	 * output
	 * 
	 *   Returns a filtered value related to the (Pixel) @param p input
	 * 
	 * @param p
	 * @return
	 */
	public Pixel output(Pixel p) {
		double [] res= new double[4];
		int idx=0;
		for(double v:p.pixel){
			res[idx++]=(int)output(v);
		}
		return new Pixel(res);
	}

	/**
	 * output
	 * 
	 *   Returns a filtered value related to the (Point) @param p input
	 * 
	 * @param p
	 * @return
	 */
	public Point output(Point p) {
		Point res=new Point(p);
		res.pixel=output(p.pixel);
		return res;
	}

	/**
	 * output
	 * 
	 *   Returns a filtered value related to the (Point []) @param pt input
	 * 
	 * @param pt
	 * @return
	 */
	public Point[] output(Point[] pt) {
		Point []res=new Point[pt.length];
		int idx=0;
		for(Point p:pt){
		  res[idx++]=output(p);
		}
		return res;
	}	
	
}
