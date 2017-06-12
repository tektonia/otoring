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
     * @param op
     */
	public static void exec(ImageProcessor iProc, Roi roi, ImagePlus image, Option op){

		FloatPolygon polF=roi.getInterpolatedPolygon();
		perfilROI = new OtoProfile(polF, image);
		OtoProfile original = new OtoProfile(polF, image);
		Canal can=op.getCanal();
		Plot plotOriginal=null;
		
if(DEBUG) Draw.plot(original,"1 - Profile of Image intersected by Line", can);
				
		if(op.getDrawOriginal()) plotOriginal=Draw.plot(perfilROI,"With Original profile", can);
		
		Filter fmmp=new MovingAverage(op.getNumFilterPoints(), perfilROI.points()[0].pixel.mean(), op.getBaseLevel());				
		perfilROI.setFilter(fmmp);		
		OtoProfile pmm = new OtoProfile(perfilROI.runFilter(), image);
if(DEBUG) Draw.plot(pmm, "2 - Moving Average Filter", can);			
		pmm.buildSegments();		
		
if(DEBUG) Draw.graph(pmm, "3 - Segments", pmm.getSegments(), null, Color.MAGENTA, false);	
		pmm.smoothSegments(op.getGranularity(),Canal.GREEN_CH);
		
if(DEBUG) Draw.graph(pmm, "4 - Granularity polish", pmm.getSegments(), null, Color.MAGENTA, false);
		pmm.joinSegments();
		OtoProfile pan = new OtoProfile(pmm.segmentsToPoints(), image);

if(DEBUG) Draw.graph(pmm, "5 - Segment join", pmm.getSegments(), null, Color.MAGENTA, false);
		
		ArrayList<Segment> segments = pmm.filterSegments(Canal.GREEN_CH);
		
if(DEBUG) Draw.graph(pan, "6 - Determine Hills and Valleys", segments, null, Color.MAGENTA, false);		
		Plot plot=Draw.graph(pan, "Filtred Profile", segments, plotOriginal, Color.MAGENTA, op.getHLline());
		pmm.setSegments(segments);

		ArrayList <Segment>[] montesVales = pmm.countRings(segments, op, Canal.GREEN_CH);
		int monte=montesVales[0].size();
		int vale=montesVales[1].size();
		Draw.markHillsAndValleys(montesVales[0],montesVales[1], plot);
		
if(DEBUG) Draw.graph(pan, "8 - H and L selection", pmm.getSegments(), null, Color.MAGENTA, true);		
		plot.setColor(Color.BLACK);
		plot.setFont(0, 18);

		plot.addText("high: "+monte+"\nlow: "+vale, (plot.getDrawingFrame().getMaxX()-plot.getDrawingFrame().getMinX())/2 ,40);
		plot.updateImage();
		
		IJ.log(image.getTitle()+" ,"+monte+","+vale);
		if(op.getMarkDraw()) Draw.ringMark(segments, op, Canal.GREEN_CH, iProc);
	}
}
