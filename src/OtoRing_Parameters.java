/*
 * OtoRing parameters
 * 
 * These values are written in a file (serialized)
 * 
 * 2017 - VVS
 */

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

/**
 * OtoRing_Parameters
 * 
 * Plugin to view and set parameters for OtoRing
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class OtoRing_Parameters implements PlugIn{
	protected ImagePlus image;
	protected ImageProcessor iProc;
	Option opcao = new Option();
	
	private void showAbout(){
		IJ.showMessage("Otolith Ring identifier",
		"\tThis plugin is an automatic attempt to identify rings of an otolith.\n"+
		"\tAfter opening an image containing an otolith, please select a line\n"+
		"that intersects the otolith along which rings are expected.\n"+
		"\tPlease send feedback to vsilva@deetc.isel.ipl.pt and patricia@ipimar.pt\n"
		);	
	}
	
	private boolean getSetParameters() {
		GenericDialog param = new GenericDialog("Otoring Parameters", IJ.getInstance());
		param.addNumericField("Number of filter points", opcao.getNumFilterPoints(), 0);
		param.addNumericField("Base Level", opcao.getBaseLevel(), 2);
		param.addNumericField("Granularity", opcao.getGranularity(), 2);	
		param.addNumericField("Min elevation", opcao.getElevationMin(), 2);
		param.addNumericField("Min depth", opcao.getDepthMin(), 2);		
		param.addCheckbox("Show original profile", opcao.getDrawOriginal());
		param.addCheckbox("Show HL line", opcao.getHLline());
		param.addCheckbox("Show ring mark", opcao.getMarkDraw());
		param.addChoice("  fill mark", Option.markType, Option.markType[opcao.getMarkType()]);;
		param.addNumericField("  mark size X", opcao.getMarkSizeX(), 2);
		param.addNumericField("  mark size Y", opcao.getMarkSizeY(), 2);	
		param.addChoice("  ring H color", Option.coresStr, Option.coresStr[opcao.getMarkColorH()]);
		param.addChoice("  ring L color", Option.coresStr, Option.coresStr[opcao.getMarkColorL()]);		
		param.showDialog();
		if(!param.wasCanceled()) { //maintain the same top down order
			opcao.setNumFilterPoints((int)param.getNextNumber());
			opcao.setBaseLevel(param.getNextNumber());
			opcao.setGranularity(param.getNextNumber());
			opcao.setElevationMin(param.getNextNumber());
			opcao.setDepthMin(param.getNextNumber());
			opcao.setDrawOriginal(param.getNextBoolean());
			opcao.setHLline(param.getNextBoolean());			
			opcao.setMarkDraw(param.getNextBoolean());
			opcao.setMarkType(param.getNextChoiceIndex());
			opcao.setMarkSizeX((int)param.getNextNumber());
			opcao.setMarkSizeY((int)param.getNextNumber());	
			opcao.setMarkColorH(param.getNextChoiceIndex());
			opcao.setMarkColorL(param.getNextChoiceIndex());			
			return true;
		}
		return false;
	}

	@Override
	public void run(String arg0) {
		opcao.readParameters();
		if(getSetParameters()) opcao.saveParameters();
		
	}
}
