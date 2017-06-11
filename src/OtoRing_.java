/*
 * OtoRing plugin
 * 
 * Name must have an underscore _
 * 
 * This is the entrypoint. The execution continues in ProcessaV6
 * 
 * 2017 - VVS
 */

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

/**
 * OtoRing_
 * 
 * Plugin to aid Otolith ring counting
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class OtoRing_ implements PlugInFilter{
	protected ImagePlus image;
	protected ImageProcessor iProc;
	private Roi roi;
	Option opcao = new Option();
	
	public int setup(String arg, ImagePlus image) {
		if(arg.equals("about")){
			showAbout();
			return DONE;
		}
		this.image =image;
		return DOES_ALL+ROI_REQUIRED;
	}
	private void showAbout(){
		IJ.showMessage("Otolith Ring identifier",
		"\tThis plugin is an automatic attempt to identify rings of an otolith.\n"+
		"\tAfter opening an image containing an otolith, please select a line\n"+
		"that intersects the otolith along which rings are expected.\n"+
		"\tPlease send feedback to vsilva@deetc.isel.ipl.pt and patricia@ipimar.pt\n"
		);	
	}
	
	
	@Override
	public void run(ImageProcessor ip) {
		iProc=ip;
		if(getROI()){
			opcao.readParameters();
			Process.exec(ip, roi, image, opcao);  // the algorithm
		}
	}
	
	private boolean getROI(){
		roi = image.getRoi();
		if (roi==null || roi.getType()!=Roi.LINE) {
			IJ.error("Line selection required.");
			return false;
		}		
		return true;
	}

}
