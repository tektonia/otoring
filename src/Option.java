/*
 * Parameters
 * 
 * 2017 - VVS
 */

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import ij.IJ;

/**
 * 
 * Option
 * 
 *   Definition of the options available to the plugin
 *   The options are stored and retrieved from a file as a serialized object
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class Option implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public class Dados implements Serializable {
	  private static final long serialVersionUID = 1L;
	  private int numFilterPoints = 10;
	  private double baseLevel=20.0;
	  private double elevationMin=10.0;
	  private double depthMin=10.0;    	 
	  private double granularity=3.0;
	  private int markType=0;
	  private boolean hlLine=true;
	  private boolean markDraw=true;
	  private boolean plateau=false;
	  private int markColorH=0; //Color.RED;
	  private int markColorL=2; //Color.GREEN;
	  private int markSizeX=10;
	  private int markSizeY=20;

	  private boolean drawOriginal=true;
	  private Canal canal=new Canal(false, true, false, false); // green channel only
	}

    static final Color[] cores =  {Color.RED, Color.YELLOW,Color.GREEN, Color.CYAN, Color.BLACK};
    static final String[] coresStr = {"RED","YELLOW","GREEN","CYAN","BLACK"};
    
    static final String[] markType = {"Full", "Outline"};
    static final int MAX_MARK_SIZE=50;
    
	public String nomeFich="otoring_param.ser";  // Filename for writing the parameters
	Dados dados=new Dados();

/**
 * saveParameters
 * 
 * Stores the parameters in a file as a serialized object
 * 
 */
  public void saveParameters() {
	  try{
		  OutputStream file = new FileOutputStream(nomeFich);
		  OutputStream buffer = new BufferedOutputStream(file);
		  ObjectOutput output = new ObjectOutputStream(buffer);
		  output.writeObject(dados);
		  buffer.flush();
		  output.close();
		  file.close();
	  }  
	  catch(IOException ex){
		  //IJ.log("Cannot save parameters in <"+nomeFich+"> "+ex.getMessage());
	  }
  }

  /**
   * readParameters
   * 
   * Reads the parameters from a file with a serialized object
   * 
   */
  public void readParameters() {
    try{
      InputStream file = new FileInputStream(nomeFich);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream (buffer);
      dados=(Dados) input.readObject();
      input.close();
      file.close();
    }
    catch(ClassNotFoundException ex){
    	IJ.log("Wrong file version: <"+nomeFich+"> "+"Class not found: "+ex.getMessage());
    }
    catch(IOException ex){
    	//IJ.log("Cannot read parameters from <"+nomeFich+"> "+ex.getMessage());
    }
  }
  
  /**
   * getCanal()
   * 
   * @return Channels used
   */
  public Canal getCanal() {return dados.canal;}

  /**
   * setNumFilterPoints
   *   
   * @param n - number of point for the moving average filter
   */
  public void setNumFilterPoints(int n){ dados.numFilterPoints=n<1?1:n;}
  
  /**
   * getNumFilterPoints
   *   
   * @return number of point for the moving average filter
   */ 
  public int  getNumFilterPoints(){ return dados.numFilterPoints;}

  /**
   * setBaseLevel
   * 
   * @param n Minimum value, defines noise level
   */
  public void setBaseLevel(double n){ dados.baseLevel=n<0?0:n;}
  
  /**
   * getBaseLevel
   * 
   * @return Minimum noise threshold level
   */
  public double getBaseLevel(){ return dados.baseLevel;}	  
  
  /**
   * setElevationMin
   * 
   * @param n Minimum elevation value
   */
  public void setElevationMin(double n){ dados.elevationMin=n<0?0:n;}
  
  /**
   * getElevationMin
   * 
   * @return Minimum elevation value
   */
  public double getElevationMin(){ return dados.elevationMin;}
  
  /**
   * setDepthMin
   * 
   * @param n Minimum depth value
   */
  public void setDepthMin(double n){ dados.depthMin=n<0?0:n;}
  
  /**
   * getDepthMin
   * 
   * @return Minimum depth value
   */
  public double getDepthMin(){ return dados.depthMin;}
   
  /**
   * setMarkSizeX
   * 
   * @param n Marking width
   */
  public void setMarkSizeX(int n){ dados.markSizeX=n<0?0:n>MAX_MARK_SIZE?MAX_MARK_SIZE:n;}
  
  /**
   * getMarkSizeX
   * 
   * @return Marking width
   */
  public int getMarkSizeX(){ return dados.markSizeX;}
  
  /**
   * setMarkSizeY
   * 
   * @param n Marking height
   */
  public void setMarkSizeY(int n){ dados.markSizeY=n<0?0:n>MAX_MARK_SIZE?MAX_MARK_SIZE:n;}
  
  /**
   * getMarkSizeY
   * 
   * @return Marking height
   */
  public int getMarkSizeY(){ return dados.markSizeY;}  
  
  /**
   * setMarkType
   * 
   * @param n Type of marking
   */
  public void setMarkType(int n){ dados.markType=n;}
  
  /**
   * getMarkType
   * 
   * @return Type of marking
   */
  public int getMarkType(){ return dados.markType;}
  
  /**
   * isMarkFill
   * 
   * @return True if type of marking if FILLED
   */
  public boolean isMarkFill(){ return dados.markType==0;}
  
  /**
   * setMarkDraw
   * 
   * @param b True if mark is to be drawn
   */
  public void setMarkDraw(boolean b){ dados.markDraw=b;}
  
  /**
   * getMarkDraw
   * 
   * @return True if mark is to be drawn
   */
  public boolean getMarkDraw(){ return dados.markDraw;}
  
  /**
   * setPlateau
   * 
   * @param b True to use segment with no inclination as significative for the algorithm
   */
  public void setPlateau(boolean b){ dados.plateau=b;}
  
  /**
   * getPlateau
   * 
   * @return True to use segment with no inclination as significative for the algorithm
   */
  public boolean getPlateau(){ return dados.plateau;}
  
  /**
   * setMarkColorH
   * 
   * @param n Choose hill colour marking
   */
  public void setMarkColorH(int n){ dados.markColorH=n;}
  
  /**
   * getMarkColorH
   * 
   * @return Hill colour marking
   */
  public int getMarkColorH(){ return dados.markColorH;}
  
  /**
   * getColorH
   * 
   * @return Hill colour
   */
  public Color getColorH(){ return cores[dados.markColorH];}

  /**
   * setMarkColorL
   * 
   * @param n Choose valley colour marking
   */
  public void setMarkColorL(int n){ dados.markColorL=n;}
  
  /**
   * getMarkColorL
   * 
   * @return Valley colour marking
   */
  public int getMarkColorL(){ return dados.markColorL;}
  
  /**
   * getColorL
   * 
   * @return Valley colour
   */
  public Color getColorL(){ return cores[dados.markColorL];}
  
  /**
   * setDrawOriginal
   * 
   * @param b True if original profile is to be drawn
   */
  public void setDrawOriginal(boolean b) {dados.drawOriginal=b;}
  
  /**
   * getDrawOriginal
   * 
   * @return True if original profile is to be drawn in the resulting graph
   */
  public boolean getDrawOriginal(){ return dados.drawOriginal;}
  
  /**
   * setGranularity
   * 
   * @param d Define value below which two values are indistinguishable
   */
  public void setGranularity(double d) {dados.granularity=d<0?0:d;}
  
  /**
   * getGranularity
   * 
   * @return Value below which two values are indistinguishable
   */
  public double getGranularity() {return dados.granularity; }

  /**
   * getHLline
   * 
   * @return True to draw line in resulting graph with hill and valley distribution
   */
  public boolean getHLline() { return dados.hlLine;}
  
  /**
   * setHLline
   * 
   * @param b True to draw line in resulting graph with hill and valley distribution
   */
  public void setHLline(boolean b) { dados.hlLine=b;}  
  
}
