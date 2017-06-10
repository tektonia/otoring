/*
 * For different channel usage.
 * 
 * 2017 - VVS
 */

import java.io.Serializable;

/**
 * Canal
 * 
 * Definition of channels in use RGBY
 * 
 * @author Vitor Vaz da Silva
 *
 */
public class Canal implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean redCh=false, greenCh=false, blueCh=false, yyyCh=false;
	protected static final int RED_CH=0;  // predefined values .../
	protected static final int GREEN_CH=1;
	protected static final int BLUE_CH=2;
	protected static final int YYY_CH=3;
	protected static final String[] nome={"Red", "Green", "Blue", "YYY Channel"};

  /**
   * Canal
   * 
   *   New Canal definition initialised with the channels to be used RGBY
   *   
   * @param r
   * @param g
   * @param b
   * @param y
   */
  Canal(boolean r, boolean g, boolean b, boolean y ){
	  redCh=r; greenCh=g; blueCh=b; yyyCh=y;
  }
  
  /**
   * setRed
   * 
   * 	Use R channel
   * 
   * @param b
   */
  public void setRed(boolean b) {redCh=b;}
  
  /**
   * isRed
   * 
   * Is R channel in use?
   * 
   * @return
   */
  public boolean isRed() {	return redCh;  }
  
  /**
   * setGreen
   * 
   * 	Use G channel
   * 
   * @param b
   */
  public void setGreen(boolean b) {greenCh=b;}
  
  /**
   * isGreen
   * 
   * Is G channel in use?
   * 
   * @return
   */
  public boolean isGreen() { return greenCh; }
  
  /**
   * setBlue
   * 
   * 	Use B channel
   * 
   * @param b
   */
  public void setBlue(boolean b) {blueCh=b; }
  
  /**
   * isBlue
   * 
   * Is B channel in use?
   * 
   * @return
   */
  public boolean isBlue() { return blueCh;  }
  
  /**
   * setYYY
   * 
   * 	Use Y channel
   * 
   * @param b
   */
  public void setYYY(boolean b) {yyyCh=b;}
  
  /**
   * isYYY
   * 
   * Is Y channel in use?
   * 
   * @return
   */
  public boolean isYYY() { return yyyCh;  }

  /**
   * name
   * 
   *   Return channel @param cn name
   *   
   * @param cn
   * @return
   */
  public static String name(int cn) { return nome[cn]; }
}
