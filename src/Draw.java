/*
 * Plot functions
 * 
 * 2017 - VVS
 */

import java.awt.Color;
import java.util.ArrayList;

import ij.gui.OvalRoi;
import ij.gui.Plot;
import ij.gui.Roi;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;

/**
 * Draw
 * 
 * Several methods that draw graphs, lines and points
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class Draw {
	Canal cann;
	
	private class ListOfPoints{
		ArrayList<Double> x= new ArrayList<Double>();
		ArrayList<Double> y= new ArrayList<Double>();
		
		public void add(double dist, double valor){
			x.add(dist);
			y.add(valor);
		}
	}
	
	/**
	 * plot
	 * 
	 * Create a plot with @param p profile, title @param str for each channel in @param cn
	 *  
	 * @param p
	 * @param str
	 * @param can
	 * @return
	 */
	public static Plot plot(OtoProfile p, String str, Canal can){
		Plot plt=null;
		if(can.isRed()) createPlot(p, str,Canal.RED_CH);
		if(can.isGreen()) plt=createPlot(p, str,Canal.GREEN_CH);
		if(can.isBlue()) createPlot(p, str,Canal.BLUE_CH);
		if(can.isYYY()) createPlot(p, str,Canal.YYY_CH);
		return plt;
	}
	
	/**
	 * createPlot
	 * 
	 * Create a plot with @param prfl profile, title @param txt and on channel @param cn
	 * 
	 * @param prfl
	 * @param txt
	 * @param cn
	 * @return
	 */
	private static Plot createPlot(OtoProfile prfl, String txt, int cn){
        String xLabel = "Distance";
        Plot plot = new Plot("Profile "+txt+" "+Canal.name(cn), xLabel, "Intensity", prfl.distances(), prfl.canal(cn));
        //plot.setLimits(perfil.minX(), perfil.maxX(), perfil.min(), perfil.max());
        plot.setColor(Color.black);
        plot.setLimitsToFit(false);
        plot.show();
        return plot;
      }
	
	/**
	 * graph
	 * 
	 * 	Draw the @param p profile with @param sgmnts segments on a graph or append to an existing @param grph graph,
	 *  using colour @param clr. If @param dLine is True a line is drawn with accumulating hills and valleys 
	 * 
	 * @param p
	 * @param titulo
	 * @param sgmnts
	 * @param grph
	 * @param clr
	 * @param dLine
	 * @return
	 */
	public static Plot graph(OtoProfile p, String titulo, ArrayList <Segment> sgmnts, Plot grph, Color clr, boolean dLine) {
		final int COTA=50;
		final int AMPLITUDE=3;
        Plot plot;
        plot= grph!=null?grph:new Plot(titulo, "Distance", "Intensity");
        plot.setColor(clr);
        plot.addPoints(p.distances(), p.canal(Canal.GREEN_CH),2);
        plot.setColor(Color.black);
        plot.setLimitsToFit(false);
        
		plot.setColor(Color.red);
		int cota = COTA;
		ArrayList <Double> x= new ArrayList<Double>();
		ArrayList <Double> y= new ArrayList<Double>();
		plot.setColor(Color.red);
		x.add(sgmnts.get(0).start.dist);
		y.add(sgmnts.get(0).start.pixel.getCanal(Canal.GREEN_CH));		
		for(Segment a: sgmnts){
			int sinal = a.start.pixel.getCanal(Canal.GREEN_CH)<a.end.pixel.getCanal(Canal.GREEN_CH)?+1:
				a.start.pixel.getCanal(Canal.GREEN_CH)==a.end.pixel.getCanal(Canal.GREEN_CH)?0:-1;
			if(dLine) plot.drawDottedLine(a.start.dist, cota,a.end.dist, cota+AMPLITUDE*sinal, 2);
			
			//x.add(a.start.dist);
			//y.add(a.start.pixel.canal(Canal.GREEN_CH));
			x.add(a.end.dist);
			y.add(a.end.pixel.getCanal(Canal.GREEN_CH));
			cota+=AMPLITUDE*sinal;
		}
		plot.addPoints(x,y,1);   // draw the X cross
		/**/
		plot.setColor(Color.blue);
		x= new ArrayList<Double>();
		y= new ArrayList<Double>();
		x.add(sgmnts.get(0).start.dist);
		y.add(sgmnts.get(0).start.pixel.getCanal(Canal.GREEN_CH));
		for(Segment a: sgmnts){
			//plot.setColor(Color.red);
			//plot.drawDottedLine(a.start.dist, cota,a.end.dist, cota, 2);
			
			x.add(a.end.dist);
			y.add(a.end.pixel.getCanal(Canal.GREEN_CH));
		}
		/**/
		plot.addPoints(x,y,2); // draw the lines  
        //plot.setLimits(perfil.minX(), perfil.maxX(), perfil.min(), perfil.max());
		plot.show();
		return plot;
	}
	
	/**
	 * ringMark
	 * 
	 *  Mark rings @param an on the image @param iProc according to @param opcao options
	 * and channels @param canal
	 * 
	 * @param an
	 * @param opcao
	 * @param canal
	 * @param iProc
	 */
	public static void ringMark(ArrayList<Segment> an, Option opcao, int canal, ImageProcessor iProc) {
		double elevMin=opcao.getElevationMin();
		double depthMin=opcao.getDepthMin();
		boolean fill=opcao.isMarkFill();
		int sizeX=opcao.getMarkSizeX();
		int sizeY=opcao.getMarkSizeY();
		Color cH=opcao.getColorH();
		Color cL=opcao.getColorL();
		
		int idx=an.size(); 
		if(idx==0)return;
		Segment a=null,b=null;
		int monte=0, vale=0;
		a=an.get(0);
		for(int i=1; i<idx; i++){
			b=an.get(i);
			if(Segment.hillHigherThan(a, b, canal, elevMin)) oval(a.end.x,a.end.y,fill,cH,sizeX,sizeY,iProc); //monte++;
			else if(Segment.valleyDeeperThan(a, b, canal, depthMin)) oval(a.end.x,a.end.y,fill,cL,sizeX,sizeY,iProc); //vale++;
			a=b;
		}
	}
	
	/**
	 * oval
	 * 
	 * Draw an oval at (@param x,@param y) with size (@param sX, @param sY) colour @param c
	 *  and filled @param fill
	 * 
	 * @param x
	 * @param y
	 * @param fill
	 * @param c
	 * @param sX
	 * @param sY
	 * @param iProc
	 */
	private static void oval(double x, double y, boolean fill, Color c, int sX, int sY, ImageProcessor iProc){
	  Roi oval = new OvalRoi(x-sX/2,y-sY/2, sX, sY);
	  iProc.setColor(c);
	  if(fill) iProc.fill(oval);
	  oval.drawPixels(iProc);
	}

	/**
	 * Mark Hills @param monte and Valleys @param vale on the graph @param plot as H# or L# 
	 * 
	 * @param monte
	 * @param vale
	 * @param plot
	 */
	public static void markHillsAndValleys(ArrayList<Segment> monte, ArrayList<Segment> vale, Plot plot) {
		plot.setColor(Color.RED);
		int n=1;
		ListOfPoints lista=null;
		lista=new Draw().new ListOfPoints();
		for(Segment a:monte){
			//plot.addText("h",a.start.dist,a.start.pixel.canal(Canal.GREEN_CH)+1);
			plot.addText("H"+(n++),a.end.dist,a.end.pixel.getCanal(Canal.GREEN_CH)+1);
			lista.add(a.end.dist,a.end.pixel.getCanal(Canal.GREEN_CH));
		}
		plot.addPoints(lista.x, lista.y,4);
		lista=new Draw().new ListOfPoints(); n=1;
		for(Segment a:vale){
			//plot.addText("l",a.start.dist,a.start.pixel.canal(Canal.GREEN_CH)-3);
			plot.addText("L"+(n++),a.end.dist,a.end.pixel.getCanal(Canal.GREEN_CH)-3);
			lista.add(a.end.dist,a.end.pixel.getCanal(Canal.GREEN_CH));
		}
		plot.addPoints(lista.x, lista.y,5);
		plot.updateImage();
	}
}
