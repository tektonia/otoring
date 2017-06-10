/*
 * Pixel definitions
 * 
 * 2017 - VVS
 */

/**
 * Pixel
 * 
 *   A Pixel is composed by RGBY values
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class Pixel{
	static final int PIXEL_LEN = 4;
	double[] pixel=new double[PIXEL_LEN];

	/**
	 * Pixel
	 * 
	 *   Create a pixel from RGBY values
	 *   
	 * @param p
	 */
	Pixel(double[] p){pixel =p.clone();}
	
	/**
	 * Pixel
	 * 
	 *   Duplicate pixel p
	 * 
	 * @param p
	 */
	Pixel(Pixel p) {pixel=p.pixel.clone();}


	/**
	 * Pixel
	 * 
	 *   Create a pixel from RGBY values
	 * 
	 * @param p
	 */
	Pixel(int[] p){
		pixel =new double[p.length];
		int i=0;
		for(int n:p) pixel[i++]=n;
	}
	
	/**
	 * setCanal
	 * 
	 *	Set channel @param canal with @param intt value
	 *
	 * @param canal
	 * @param intt
	 */
	public void setCanal(int canal, double intt) { pixel[canal]=intt; }	
	
	/**
	 * getCanal
	 * 
	 * 	Get channel @param c value
	 * 
	 * @param c
	 * @return
	 */
	public double getCanal(int c){ return pixel[c];}
	
	/**
	 * mean
	 * 
	 * 	Mean value from all RBGY values
	 * 
	 * @return
	 */
	public double mean(){ return (pixel[0]+pixel[1]+pixel[2]+pixel[3])/4.0;}
	
	/**
	 * value
	 * 
	 *	Returns RGBY values
	 * 
	 * @return
	 */
	public double[] value(){ return pixel;}
	
	/**
	 *  toString()
	 *  
	 *  String containing the description of a Pixel
	 */
	public String toString(){ return "0:"+pixel[0]+" 1:"+pixel[1]+" 2:"+pixel[2]+" 3:"+pixel[3];}
	
}
