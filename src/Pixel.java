/*
 * Point definitions
 * 
 * 2017 - VVS
 */

/**
 * 
 * Point
 * 
 *   A Point is defined as a Pixel at (x,y).
 *   The distance of that pixel to the origin is dist
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class Point{
	double x;
	double y;
	double dist;
	Pixel pixel;
	
	/**
	 * Point
	 * 
	 * Create a new point x,y at distance d away
	 * 
	 * @param x
	 * @param y
	 * @param d
	 */
	Point(double x, double y, double d){ this.x=x; this.y=y; dist=d;}
	
	/**
	 * Point
	 * 
	 * Create a new point x,y at distance d away with pixel px
	 * 
	 * @param x
	 * @param y
	 * @param d
	 * @param px
	 */
	Point(double x, double y, double d, int[] px){ this(x,y,d); pixel=new Pixel(px);}
	
	/**
	 * Point
	 * 
	 * Create a new Point equal to p (clone)
	 * 
	 * @param p
	 */
	Point (Point p) {this(p.x, p.y, p.dist); pixel=new Pixel(p.pixel);}
	
	/**
	 *  toString()
	 *  
	 *  String containing the description of a Point
	 */
	public String toString(){ return "x:"+x+" y:"+y+" d:"+dist+" "+pixel.toString(); }
			
}
