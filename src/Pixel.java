/*
 * Pixel definitions
 * 
 * 2017 - VVS
 */

public class Pixel{
	static final int PIXEL_LEN = 4;
	double[] pixel=new double[PIXEL_LEN];

	Pixel(double[] p){pixel =p.clone();}
	
	Pixel(Pixel p) {pixel=p.pixel.clone();}
	
	public String toString(){ return "0:"+pixel[0]+" 1:"+pixel[1]+" 2:"+pixel[2]+" 3:"+pixel[3];}
	
	public double canal(int c){ return pixel[c];}
	public double mean(){ return (pixel[0]+pixel[1]+pixel[2]+pixel[3])/4.0;}
	public double[] value(){ return pixel;}
	
	Pixel(int[] p){
		pixel =new double[p.length];
		int i=0;
		for(int n:p) pixel[i++]=n;
	}
	
	public void setCanal(int canal, double intt) { pixel[canal]=intt; }	
	
}
