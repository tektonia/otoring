/*
 * Segment definitions
 * 
 * 2017 - VVS
 */

/**
 * Segment
 * 
 * A Segment is a line that connects two Point the start and the end points
 * A Segment can be connected to another segment in which the end of one is the start of the other
 * A connected Segment is never closed
 * A closed Segment is deleted
 *  
 * @author Vaz da Silva
 *
 */
public class Segment {
  Point start;
  Point end;
  
  /**
   * Segment
   * 
   * Create a Segment with @param p as the start Point
   * 
   * @param p
   */
  public Segment(Point p){start = new Point(p);}
  
  /**
   * setEnd
   * 
   * Set segment's end as @param p Point
   * 
   * @param p
   */
  public void setEnd(Point p){end = new Point(p);}
  
  /**
   * isPlateau
   * 
   * Return if connected segment @param a , @param b has the same pixel intensity
   * on the selected @param canal channel.
   * 
   * @param a
   * @param b
   * @param canal
   * @return
   */
  public static boolean isPlateau(Segment a, Segment b, int canal) {
	return a.start.pixel.getCanal(canal) == a.end.pixel.getCanal(canal)
		&& a.start.pixel.getCanal(canal) == b.start.pixel.getCanal(canal)
		&& b.start.pixel.getCanal(canal) == b.end.pixel.getCanal(canal);
  }
  
  /**
   * isHill
   * 
   * 	Return if connected segment @param a , @param b are joined by a higher intensity pixel
   * on the selected @param canal channel.
   * 
   * @param a
   * @param b
   * @param canal
   * @return
   */
  public static boolean isHill(Segment a, Segment b, int canal) {
	return a.start.pixel.getCanal(canal) < a.end.pixel.getCanal(canal)
		&& b.start.pixel.getCanal(canal) > b.end.pixel.getCanal(canal);
  }
   
  /**
   * isValley
   * 
   * 	Return if connected segment @param a , @param b are joined by a less intensity pixel
   * on the selected @param canal channel.
   * 
   * @param a
   * @param b
   * @param canal
   * @return
   */
  public static boolean isValley(Segment a, Segment b, int canal) {
	return a.start.pixel.getCanal(canal) > a.end.pixel.getCanal(canal)
		&& b.start.pixel.getCanal(canal) < b.end.pixel.getCanal(canal);
  }
  
  /**
   * hillHigherThan
   * 
   *  True if connected segments @param a, @param b form a Hill on channel @param canal
   * and the Hill relative intensity is above @param val
   * 
   * @param a
   * @param b
   * @param canal
   * @param val
   * @return
   */
  public static boolean hillHigherThan(Segment a, Segment b, int canal, double val){
	  return (isHill(a,b,canal) && hillHeight(a,b,canal)>=val);
  }
  
  /**
   * valleyDeeperThan
   * 
   *  True if connected segments @param a, @param b form a Valley on channel @param canal
   * and the Valley relative intensity is above @param val
   * 
   * @param a
   * @param b
   * @param canal
   * @param val
   * @return
   */
  public static boolean valleyDeeperThan(Segment a, Segment b, int canal, double val){
	  return (isValley(a,b,canal) && valleyDepth(a,b,canal)>=val);
  }
  
  /**
   * distance
   * 
   * Geometric distance between connected segments @param , @param b end points
   * 
   * @param a
   * @param b
   * @return
   */
  public static double distance(Segment a, Segment b) {
	return b.end.dist - a.start.dist;
  }
  
  /**
   * hillHeight
   * 
   *  Assuming that @param a and @param b forms a hill, this function returns the height
   * of its highest slope according to @param canal channel values
   * 
   * @param a
   * @param b
   * @param canal
   * @return
   */
  public static double hillHeight(Segment a, Segment b, int canal) {
	double subida = a.end.pixel.getCanal(canal) - a.start.pixel.getCanal(canal);
	double descida = b.start.pixel.getCanal(canal) - b.end.pixel.getCanal(canal);
	return Math.max(Math.abs(subida),Math.abs(descida));	   
  }
  
  /**
   * valleyDepth
   * 
   *  Assuming that @param a and @param b forms a valley, this function returns the height
   * of its deepest slope according to @param canal channel values
   * 
   * @param a
   * @param b
   * @param canal
   * @return
   */
  public static double valleyDepth(Segment a, Segment b, int canal) {
		double subida = b.end.pixel.getCanal(canal) - b.start.pixel.getCanal(canal);
		double descida =a.start.pixel.getCanal(canal) - a.end.pixel.getCanal(canal);
		return Math.max(Math.abs(subida),Math.abs(descida));
  }  
  
  /**
   * Show distance and value of the start and end Point
   */
  public String toString(){
	  return start.dist+":"+start.y+"-"+end.dist+":"+end.y;
  }
}
