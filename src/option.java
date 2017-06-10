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
  
  public Canal getCanal() {return dados.canal;}

  public void setNumFilterPoints(int n){ dados.numFilterPoints=n<1?1:n;}
  public int  getNumFilterPoints(){ return dados.numFilterPoints;}

  public void setBaseLevel(double n){ dados.baseLevel=n<0?0:n;}
  public double getBaseLevel(){ return dados.baseLevel;}	  
  
  public void setElevationMin(double n){ dados.elevationMin=n<0?0:n;}
  public double getElevationMin(){ return dados.elevationMin;}
  
  public void setDepthMin(double n){ dados.depthMin=n<0?0:n;}
  public double getDepthMin(){ return dados.depthMin;}
   
  public void setMarkSizeX(int n){ dados.markSizeX=n<0?0:n>MAX_MARK_SIZE?MAX_MARK_SIZE:n;}
  public int getMarkSizeX(){ return dados.markSizeX;}
  
  public void setMarkSizeY(int n){ dados.markSizeY=n<0?0:n>MAX_MARK_SIZE?MAX_MARK_SIZE:n;}
  public int getMarkSizeY(){ return dados.markSizeY;}  
  
  public void setMarkType(int n){ dados.markType=n;}
  public int getMarkType(){ return dados.markType;}
  public boolean isMarkFill(){ return dados.markType==0;}
  
  public void setMarkDraw(boolean b){ dados.markDraw=b;}
  public boolean getMarkDraw(){ return dados.markDraw;}
  
  public void setPlateau(boolean b){ dados.plateau=b;}
  public boolean getPlateau(){ return dados.plateau;}
  
  public void setMarkColorH(int n){ dados.markColorH=n;}
  public int getMarkColorH(){ return dados.markColorH;}
  public Color getColorH(){ return cores[dados.markColorH];}

  public void setMarkColorL(int n){ dados.markColorL=n;}
  public int getMarkColorL(){ return dados.markColorL;}
  public Color getColorL(){ return cores[dados.markColorL];}
  
  public void setDrawOriginal(boolean b) {dados.drawOriginal=b;}
  public boolean getDrawOriginal(){ return dados.drawOriginal;}

  public double getGranularity() {return dados.granularity; }
  public void setGranularity(double d) {dados.granularity=d<0?0:d;}

  public boolean getHLline() { return dados.hlLine;}
  public void setHLline(boolean b) { dados.hlLine=b;}  
  
}
