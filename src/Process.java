/*
 * This file contains the algorithm.
 * Presently it is only using the Green channel
 *  
 * 2017 - VVS
 */

import java.awt.Color;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Plot;
import ij.gui.Roi;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;

/**
 * Process
 * 
 * The algorithm to count Otolith rings
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class Process {
	private static OtoProfile perfilROI;
	
    static final boolean DEBUG = false;	// shows intermediate graphs
	
    /**
     * exec
     * 
     * Execute the algorithm over the region of interest @param roi line that lies on  @param image.
     * 
     * @param iProc
     * @param roi
     * @param image
     * @param opcao
     */
	public static void exec(ImageProcessor iProc, Roi roi, ImagePlus image, Option opcao){

		FloatPolygon polF=roi.getInterpolatedPolygon();
		perfilROI = new OtoProfile(polF, image);
		OtoProfile original = new OtoProfile(polF, image);
		Draw draw = new Draw(opcao.getCanal());
		Plot plotOriginal=null;
		
if(DEBUG) draw.plot(original,"1 - Profile of Image intersected by Line");
				
		if(opcao.getDrawOriginal()) plotOriginal=draw.plot(perfilROI,"With Original profile");
		
		Filter fmmp=new MovingAverage(opcao.getNumFilterPoints(), perfilROI.index()[0].pixel.mean(), opcao.getBaseLevel());				
		perfilROI.filtro(fmmp);		
		OtoProfile pmm = new OtoProfile(perfilROI.filtrar(), image);
if(DEBUG) draw.plot(pmm, "2 - Moving Average Filter");			
		pmm.buildSegments();		
		
if(DEBUG) draw.mark(pmm, "3 - Segments", pmm.getSegments(), null, Color.MAGENTA, false);	
		pmm.smoothSegments(opcao.getGranularity(),Canal.GREEN_CH);
		
if(DEBUG) draw.mark(pmm, "4 - Granularity polish", pmm.getSegments(), null, Color.MAGENTA, false);
		pmm.joinSegments();
		OtoProfile pan = new OtoProfile(pmm.segments(), image);

if(DEBUG) draw.mark(pmm, "5 - Segment join", pmm.getSegments(), null, Color.MAGENTA, false);
		
		ArrayList<Segment> aneis = pmm.filterSegments(Canal.GREEN_CH);
		
if(DEBUG) draw.mark(pan, "6 - Determine Hills and Valleys", aneis, null, Color.MAGENTA, false);		
		Plot plot=draw.mark(pan, "Filtred Profile", aneis, plotOriginal, Color.MAGENTA, opcao.getHLline());
		pmm.setSegments(aneis);

		ArrayList <Segment>[] montesVales = pmm.countRings(aneis, opcao, Canal.GREEN_CH);
		int monte=montesVales[0].size();
		int vale=montesVales[1].size();
		draw.markHillsAndValleys(montesVales[0],montesVales[1], plot);
		
if(DEBUG) draw.mark(pan, "8 - H and L selection", pmm.getSegments(), null, Color.MAGENTA, true);		
		plot.setColor(Color.BLACK);
		plot.setFont(0, 18);

		plot.addText("high: "+monte+"\nlow: "+vale, (plot.getDrawingFrame().getMaxX()-plot.getDrawingFrame().getMinX())/2 ,40);
		plot.updateImage();
		
		IJ.log(image.getTitle()+" ,"+monte+","+vale);
		if(opcao.getMarkDraw()) draw.segments(aneis, opcao, Canal.GREEN_CH, iProc);
	}
}
