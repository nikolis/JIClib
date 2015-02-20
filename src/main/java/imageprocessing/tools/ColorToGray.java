package imageprocessing.tools;

import imageprocessing.utilities.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;



public class ColorToGray {

	/**
	 * Classical method turning a colored image in a grayscale image 
	 * using the Average method
	 * downside:The pour representation of luminosity 
	 * @param originalImage
	 * @return
	 */
	public static  BufferedImage toGrayAvg(BufferedImage originalImage){
		int alpha , red , green , blue ; //Color intesities
		int sumOfPixelIntensities ; 
		int meanOfPixelIntesities ; 
		int newPixel ; 
		BufferedImage grayImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType()) ; 
		for(int i=0 ;i< originalImage.getWidth(); i++){
			for (int j=0 ; j<originalImage.getHeight(); j++){
			
				//get Pixel's Colors
				alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
				red = new Color(originalImage.getRGB(i, j)).getRed() ;
				green = new Color(originalImage.getRGB(i, j)).getGreen() ; 
				blue = new Color(originalImage.getRGB(i, j)).getBlue();
				sumOfPixelIntensities = red+green+blue ; 
				meanOfPixelIntesities =  sumOfPixelIntensities/3 ; 
				newPixel = GeneralImagingOperations.colorToRGB(alpha, meanOfPixelIntesities, meanOfPixelIntesities, meanOfPixelIntesities) ; 
				
				//sets the color intesities as
				grayImage.setRGB(i, j, newPixel);
			}
		}
		return grayImage ; 
	}
	/**
	 * luminance method plays off the fact that cone density in 
	 * the human eye is not uniform across colors.Humans perceive green more strongly than red, 
	 * and red more strongly than blue.
	 * @param originalImage
	 * @return
	 */
	public static BufferedImage toGrayLM(BufferedImage originalImage){
		int alpha,red,green,blue ; 
		int newPixel ;
		int weightedMean ; 
		BufferedImage greyImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType()) ; 
		
		for(int i=0 ; i<originalImage.getWidth(); i++){
			for(int j=0 ; j<originalImage.getHeight(); j++){
				alpha = new Color(originalImage.getRGB(i, j)).getAlpha() ;
				red = new Color(originalImage.getRGB(i, j)).getRed() ;
				green = new Color(originalImage.getRGB(i, j)).getGreen() ; 
				blue = new Color(originalImage.getRGB(i, j)).getBlue() ;
				weightedMean = (int)((red*0.299)+ (green*0.587) + (0.114*blue)) ;
				Color newColor = new Color(weightedMean, weightedMean, weightedMean) ; 
				greyImage.setRGB(i, j, newColor.getRGB());		
			}
		}
		return greyImage  ;
	}
	
	
	
	public static void main(String args[]){
		//ColorToGray colorMachine = new ColorToGray() ; 
		BufferedImage image = null ;
		BufferedImage endImage ; 
		try{
			image = ImageIO.read(new File("imagestest/original.jpg"));
		}catch(Exception e){
			e.printStackTrace();
		}
		endImage = ColorToGray.toGrayAvg(image) ; 
		try{
			ImageIO.write(endImage,"jpg", new File("imagestest/grayAVG.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
		 
	}
	
}
