/*
 * Profile functions
 * 
 * 2017 - VVS
 */

import java.util.ArrayList;
import ij.IJ;
import ij.ImagePlus;
import ij.process.FloatPolygon;

/**
 * OtoProfile
 * 
 *  Transforms the otolith profile to enhance the rings
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class OtoProfile{
	Point []ponto;
	double []distancia;
	Point origem;
	double media=0.0, max=Double.NEGATIVE_INFINITY, min=Double.POSITIVE_INFINITY;
	Filter filtro=null;
	ImagePlus image;
	ArrayList<Segment> segments=null;
	
	/**
	 * distance
	 * 
	 * Geometric distance between origin and (x,y)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private double distance(double x, double y){
		return Math.sqrt((origem.x-x)*(origem.x-x)+(origem.y-y)*(origem.y-y));
	}

	/**
	 * OrtoProfile
	 * 
	 * Creates an object with a initial profile
	 * 
	 * @param fp
	 * @param image
	 */
	OtoProfile(FloatPolygon fp,ImagePlus image){
		int n;
		this.image=image;
		ponto=new Point[n=fp.npoints];
		distancia = new double[n];
		double d;
		
		int inicio=0;
		int fim=n;
		int inc=+1;
		
		if(fp.xpoints[0]>fp.xpoints[n-1]){
			inicio=n-1;
			fim=-1;
			inc=-1;
		}
		
		int idx=0;
	
		origem=new Point(fp.xpoints[inicio], fp.ypoints[inicio],0);
		for(int i=inicio; i!=fim; i+=inc, idx++){
			double dist=distance(fp.xpoints[i],fp.ypoints[i]);
			ponto[idx]=new Point(fp.xpoints[i], fp.ypoints[i],dist,
					image.getPixel((int) fp.xpoints[i], (int) fp.ypoints[i]));
			distancia[idx]=dist;
			d=ponto[idx].pixel.mean();
			if(d>max) max=d;
			if(d<min) min=d;
			media+=d;
		}
		media/=n;
	}

	/**
	 * OtoProfile
	 * 
	 * Creates an object with a initial profile
	 * 
	 * @param pontos
	 * @param image
	 */
	public OtoProfile(Point[] pontos, ImagePlus image) {
		if(pontos==null) return;
		this.image=image;
		ponto=pontos;
		double d;
		int n=pontos.length;
		distancia = new double[n];
		int idx=0;
		origem=new Point(pontos[0].x, pontos[0].y,0);
		for(Point p: pontos){
			distancia[idx++]=distance(p.x,p.y);
			d=p.pixel.mean();
			if(d>max) max=d;
			if(d<min) min=d;
			media+=d;
		}
		media/=n;
	}	
	
	/**
	 * points
	 * 
	 * 	Profile points
	 * 
	 * @return
	 */
	public Point [] points(){ return ponto;	}	

	/**
	 * setFilter
	 * 
	 * 	Set filter
	 * 
	 * @param f
	 */
	public void setFilter(Filter f){filtro=f;}
	
	/**
	 * runFilter
	 * 
	 * 	Apply the filer to the profile
	 * 
	 * @return
	 */
	public Point[] runFilter(){
		if(filtro==null) return null;
		return filtro.output(ponto);
	}

	/**
	 * getPointsFromChannel
	 * 
	 * Get profile points from channel @param ch
	 * 
	 * @param ch
	 * @return
	 */
	public double[] getPointsFromChannel(int ch) {
		double []val = new double[ponto.length];
		int i=0;
		for(Point p: ponto){
			val[i++]=p.pixel.getCanal(ch);
		}
		return val;			
	}

	/**
	 * buildSegments
	 * 
	 * Convert the profile into segments
	 * 
	 */
	public void buildSegments(){
		int idx=ponto.length;
		segments = new ArrayList<Segment>();
		Segment anel=new Segment(ponto[0]);
		segments.add(anel);
		for(int i=1; i<idx; i++){
			anel.setEnd(ponto[i]);
			anel=new Segment(ponto[i]);
			segments.add(anel);
		}
		anel.setEnd(anel.start);
		segments=joinSegments(segments);
	}
	
	/**
	 * smoothSegments
	 * 
	 * Remove segments with heights in channel @param ch below @param grain.
	 * 
	 * @param grain
	 * @param ch
	 */
	public void smoothSegments(double grain, int ch){	segments=appyGranularity(segments, grain, ch);	}
	
	/**
	 * joinSegments
	 * 
	 * 	Connect segments forming a continuous connected segment
	 * 
	 */
	public void joinSegments(){	segments=joinSegments(segments);	}
	
	/**
	 * appyGranularity
	 * 
	 * Remove all segments from @param sgmts with height less than @param threshold in channel @param c
	 * 
	 * @param sgmts
	 * @param threshold
	 * @param c
	 * @return
	 */
	private ArrayList<Segment> appyGranularity(ArrayList<Segment> sgmts, double threshold, int c) {
		ArrayList<Segment> res = new ArrayList<Segment>();
		if(sgmts.size()<2)return sgmts;
		Segment ant=sgmts.get(0);
		res.add(ant);
		for(int i=1; i<sgmts.size(); i++){
			if(Math.abs(sgmts.get(i).start.pixel.getCanal(c) - ant.end.pixel.getCanal(c))<threshold){
				continue;
			}
			ant=sgmts.get(i);
			res.add(ant);
		}
		return res;
	}

	/**
	 * segmentsToPoints
	 * 
	 * Convert segments to points
	 * 
	 * @return
	 */
	public Point[] segmentsToPoints(){
		int idx=segments.size();
		Point[] res = new Point[idx];
		int i=0;
		for(Segment a : segments){
			res[i++]=a.start;
		}
		return res;
	}

	/**
	 * joinSegments
	 * 
	 * Return a list of connected segments from a @param inputSeg list of segments 
	 * 
	 * @param inputSeg
	 * @return
	 */
	private ArrayList<Segment> joinSegments(ArrayList<Segment> inputSeg){
		ArrayList<Segment> novo = new ArrayList<Segment>();
		int idx=inputSeg.size();

		Segment ant=inputSeg.get(0);
		novo.add(ant);
		for(int i=1; i<idx; i++){
			Segment a=inputSeg.get(i);
			if(ant.end.dist!=a.start.dist){
				double dist=(a.start.dist+ant.end.dist)/2;
				ant.end.dist=dist;
				a.start.dist=dist;
				double intt=(a.start.pixel.getCanal(Canal.GREEN_CH)+ant.end.pixel.getCanal(Canal.GREEN_CH))/2;
				a.start.pixel.setCanal(Canal.GREEN_CH,intt);
				ant.end.pixel.setCanal(Canal.GREEN_CH,intt);
			}
			novo.add(a); ant=a;
		}
		return novo;
	}	
	
	/**
	 * getSegments
	 * 
	 * Return the segment list
	 * 
	 * @return
	 */
	public ArrayList<Segment> getSegments() {return segments;	}

	/**
	 * setSegments
	 * 
	 * Set segment list to @param an
	 * 
	 * @param an
	 */
	public void setSegments(ArrayList<Segment> an) {segments=an;}
	
	/**
	 * getDistances
	 * 
	 * Return all point distance
	 * 
	 * @return
	 */
	public double[] getDistances() { return distancia; }
	
	/**
	 * countRings
	 * 
	 * Count identified otolith rings from @param an segments using @param op options on @param ch channel
	 * 
	 * @param an
	 * @param op
	 * @param ch
	 * @return
	 */
	public ArrayList <Segment> [] countRings(ArrayList<Segment> an, Option op, int ch) {
		ArrayList <Segment> []montesEVales=new ArrayList [2];
		final int HILL=0; final int VALLEY=1;
		montesEVales[HILL]=new ArrayList<Segment>();
		montesEVales[VALLEY]=new ArrayList<Segment>();
		double elevMin=op.getElevationMin();
		double depthMin=op.getDepthMin();
		boolean plat=op.getPlateau();
		
		int [] res= new int[2];
		int idx=an.size(); 
		if(idx==0){
			IJ.showMessage("an.size: "+an.size());
			return montesEVales;
		}
		Segment a=null,b=null;
		int hill=0, valley=0;
		a=an.get(0);
		for(int i=1; i<idx; i++){
			b=an.get(i);
			if(Segment.isHill(a, b, ch)){
				if(Segment.hillHeight(a, b, ch)>=elevMin){
				  hill++;
				  montesEVales[HILL].add(a);
				}
			}
			if(Segment.isValley(a, b, ch)){
				if(Segment.valleyDepth(a, b, ch)>=depthMin){
					valley++;
					montesEVales[VALLEY].add(a);
				}
			}
			if(plat && Segment.isPlateau(a, b, ch)){
				  hill++;
				  montesEVales[HILL].add(a);				
					valley++;
					montesEVales[VALLEY].add(a);
			}
			a=b;
		}	
		res[0]=hill;
		res[1]=valley;
		return montesEVales;
	}
	
	/**
	 * filterSegments
	 * 
	 * Filter the segments of channel @param ch by connecting segments with same derivative sign
	 * 
	 * @param ch
	 * @return
	 */
	public ArrayList<Segment> filterSegments(int ch) {
		ArrayList<Segment> novo = new ArrayList<Segment>(); 
		Segment a=null;
		int i=0;
		for(; i<segments.size(); i++){
		  a=segments.get(i);
		  if(a.start.dist!=a.end.dist) break;
		}
		i++;
		Segment b=null;
		boolean kept=false;
		for(; i<segments.size(); i++){
			b=segments.get(i);
			//if(b.start.dist==b.end.dist) continue;
			// one contained in the other, that should not happen!!!
			/**/
			if(a.start.pixel.getCanal(ch)<=a.end.pixel.getCanal(ch)
					&& b.start.pixel.getCanal(ch)<=b.end.pixel.getCanal(ch)
					&& a.start.pixel.getCanal(ch)<=b.start.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)>=b.end.pixel.getCanal(ch)
				){
					if(!kept) novo.add(a); kept=true;
			} 
			else if(a.start.pixel.getCanal(ch)>=a.end.pixel.getCanal(ch)
					&& b.start.pixel.getCanal(ch)>=b.end.pixel.getCanal(ch)
					&& a.start.pixel.getCanal(ch)>=b.start.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)<=b.end.pixel.getCanal(ch)
				){
					if(!kept) novo.add(a); kept=true;
			} 
			else
			/**/
			//co-linear /
				if(a.start.pixel.getCanal(ch)<=a.end.pixel.getCanal(ch)
					&& b.start.pixel.getCanal(ch)<=b.end.pixel.getCanal(ch)
					&& a.start.pixel.getCanal(ch)<=b.start.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)<=b.start.pixel.getCanal(ch)
					&& a.start.pixel.getCanal(ch)<=b.end.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)<=b.end.pixel.getCanal(ch)
				){
					a.end=b.end;
					if(!kept) novo.add(a); kept=true;
			} 
			else // up / down \, connect start equal to end
				if(a.start.pixel.getCanal(ch)<a.end.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)>b.start.pixel.getCanal(ch)
					&& b.start.pixel.getCanal(ch)>b.end.pixel.getCanal(ch)
				){
					b.start=a.end;
					if(!kept) novo.add(a);
					kept=false;
					a=b;
			}
			/**/else//co-linear  \
				if(a.start.pixel.getCanal(ch)>=a.end.pixel.getCanal(ch)
					&& b.start.pixel.getCanal(ch)>=b.end.pixel.getCanal(ch)
					&& a.start.pixel.getCanal(ch)>=b.start.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)>=b.start.pixel.getCanal(ch)
					&& a.start.pixel.getCanal(ch)>=b.end.pixel.getCanal(ch)
					&& a.end.pixel.getCanal(ch)>=b.end.pixel.getCanal(ch)
				){
					a.end=b.end;
					if(!kept) novo.add(a); kept=true;
			}/**/
			else // down \ up /, connect start equal to end
					if(a.start.pixel.getCanal(ch)>a.end.pixel.getCanal(ch)
						&& a.end.pixel.getCanal(ch)<b.start.pixel.getCanal(ch)
						&& b.start.pixel.getCanal(ch)<b.end.pixel.getCanal(ch)
					){
						b.start=a.end;
						if(!kept) novo.add(a);
						kept=false;
						a=b;
			}				
			else{
				if(!kept) {novo.add(a); }
				a=b;
				kept=false;
			}
		}
		if(!kept) novo.add(a);
		novo=joinSegments(novo);
		return novo;
	}
		
}
