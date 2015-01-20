package general.utilities;

import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.Labeling;
import imageprocessing.tools.ThresHolding;

import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;


public class trainSetPreperation {

	/**
	 * This method is using The main method of labeling in order to extract the subImages 
	 * of an The Input image defined by the number taken as parameter 
	 * @param numberOfExample
	 */
	public static  void examples(int numberOfExample)
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		String imageToRead ="images/tests/t"+String.valueOf(numberOfExample)+".jpg" ;
		String name =imageToRead.substring(imageToRead.indexOf(".jpg")-2,imageToRead.indexOf(".jpg"))  ;
		try{
			image = ImageIO.read(new File(imageToRead));
		}catch(Exception e){
			e.printStackTrace();
		}
		image2 = ColorToGray.toGrayLM(image) ;
		image2 = ThresHolding.grayImage2Bin(image2) ; 
		Labeling.mainMethod(image2, name);
	}
	/**
	 * Overloaded method that the only  difference with the previous is that it can accept the Image as a parameter 
	 * @param numberOfExample
	 */
	public static  void examples(int numberOfExample,String fileImage)
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		String imageToRead =fileImage ;
		String name =imageToRead.substring(imageToRead.indexOf(".jpg")-2,imageToRead.indexOf(".jpg"))  ;
		try{
			image = ImageIO.read(new File(imageToRead));
		}catch(Exception e){
			e.printStackTrace();
		}
		image2 = ColorToGray.toGrayLM(image) ;
		image2 = ThresHolding.grayImage2Bin(image2) ; 
		Labeling.mainMethod(image2, name);
	}
	
	
	
	
	
	
	
	
	
}
