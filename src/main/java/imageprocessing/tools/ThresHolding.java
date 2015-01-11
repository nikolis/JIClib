package imageprocessing.tools;

import imageprocessing.utilities.*;



import java.util.Enumeration;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class ThresHolding {
	public static double weightFor ; 
	public static double weightBack ; 
	
	public static BufferedImage grayImage2Bin(BufferedImage originalImage){
		//Immediately tern's the image to grayscale using the ColorToGray class 
		BufferedImage grayImage  =  ColorToGray.toGrayLM(originalImage) ; 
		//int[] histogram = GeneralImagingOperations.imageHistogram(grayImage);
		
		Hashtable<Integer, Double> thresholds =  findThresHoldVariances(grayImage);
		
				
				int optimalThresHoldValue = findBestThreshold(thresholds) ; 
				
				
				for(int i = 0 ; i<grayImage.getWidth(); i++){
					for(int j=0 ; j<grayImage.getHeight(); j++){
						int red = new Color(grayImage.getRGB(i, j)).getRed()  ;
						int alpha = new Color(grayImage.getRGB(i, j)).getAlpha()  ;
						if(red<optimalThresHoldValue){
							int newPixel ; 
							newPixel= GeneralImagingOperations.colorToRGB(alpha, 0, 0, 0) ; 
							grayImage.setRGB(i, j, newPixel);
						}else{
							int newPixel  ; 
							newPixel= GeneralImagingOperations.colorToRGB(alpha, 255, 255, 255) ; 
							grayImage.setRGB(i, j, newPixel);
						}
					}
				}
				
		return grayImage ; 
	}
	
	public static int  findBestThreshold(Hashtable<Integer, Double> thresholds){
		Enumeration<Integer> enumKey = thresholds.keys();
		Double minimymVariance  =null;
		int thresHoldWithMinimymVariance  =9669;  
		while(enumKey.hasMoreElements()) { 
		    Integer intensity = enumKey.nextElement();
			System.out.println("edw intensity : "+ intensity )   ;
		    double variance = thresholds.get(intensity) ; 
		    System.out.println("edw variance : "+ (long) variance )   ;
		    if(minimymVariance == null){
		    	minimymVariance=variance ; 
		    	thresHoldWithMinimymVariance = intensity ;
		    }
		    else if(variance<minimymVariance){
		    	minimymVariance = variance ; 
		    	thresHoldWithMinimymVariance = intensity ; 
				}
		}	
		return thresHoldWithMinimymVariance ; 
	}
	
	
	
	
	public static Hashtable<Integer, Double>  findThresHoldVariances(BufferedImage grayImage){
		Hashtable<Integer, Double> thresHolds = new Hashtable<>();//Integer=Intensity value Double=weights
		int[] intensities = new int[256] ;  
		
		//Initializes all the 
	
		
		for(int thresHold=10 ;thresHold < 256; thresHold+=10){
			for(int i=0 ; i<grayImage.getWidth(); i++){
				for(int j=0 ; j<grayImage.getHeight(); j++){
					int intesity =  new Color(grayImage.getRGB(i, j)).getRed() ; 
						intensities[intesity]++ ; 
				}
			}//fills the array of intensities 
			 
			
			double withinClassVariance = calculateWithinClassVariance(intensities,thresHold)  ;  
		
			//System.out.println("within class variance is : "+ withinClassVariance);
			
			thresHolds.put(thresHold,withinClassVariance) ;
		}
		return thresHolds ; 
	}
	
	
	
	/**
	 * This method is used to calculate the  2 weighted class variances 
	 * that are given as  an array of numbers containing on set along with the threshold
	 * that will be used to distinguish them !
	 * @param numb
	 * @param threshold
	 * @return variances
	 */
	public static double  calculateWithinClassVariance(int[] weights, int threshold){
		//Hashtable<String, Double> variances  = new Hashtable<>() ; 
		int[] backGround = new int[threshold]; 
		int[] foreGround = new int[256-threshold] ;
		//TODO  optimization 
		for(int i =0 ; i< weights.length; i++){
			if(i<threshold){
				backGround[i]=weights[i] ;
			}else{
				foreGround[i-threshold]=weights[i]  ;
			}
		}
		double varianceFor = 0 ; 
		double varianceBack=0 ; 
		varianceFor=MathematicalOperations.calculateVariance(threshold, foreGround, false) ;
		varianceBack=MathematicalOperations.calculateVariance(threshold, backGround, true) ; 
		 
		double weightFor  = 0  ;
		double weightBack = 0  ; 
		double tempSum = 0 ; 
		for(int i=0 ; i<weights.length ; i++){
			if(i<threshold){
				weightBack+=weights[i] ;
				tempSum+=weights[i] ;
			}else{
				weightFor+= weights[i] ;
				tempSum+=weights[i] ;
			}
		}
		weightBack = weightBack/tempSum ;
		weightFor = weightFor/tempSum ; 
		
		return ((varianceFor*weightFor)+(varianceBack*weightBack)) ; 
	}
	
	
	
	
	
	
	
	public static void main(String args[]){
		//ThresHolding nik = new ThresHolding() ;
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		try{
			image = ImageIO.read(new File("images/input.jpg"));
		}catch(Exception e){
			e.printStackTrace();
		}
		image2 = ThresHolding.grayImage2Bin(image) ; 
		try{
			ImageIO.write(image2,"jpg", new File("images/output.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
}
