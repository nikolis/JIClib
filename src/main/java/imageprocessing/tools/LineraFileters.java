package imageprocessing.tools ; 


import imageprocessing.utilities.GeneralImagingOperations;

import java.awt.Color;
import java.awt.PageAttributes.OriginType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class LineraFileters {
	//This is the representation of The kernels to be used
	//The 1 denotes a digits that will be take in account the 2 is the target
	//Digit and the 0 is the digits that we won't care for 
	public static int[][]  linearFilterKernel1 = {{1, 1, 1}, {1, 2, 1}, {0, 0, 0}} ;
	public static int[][]  linearFilterKernel2 = {{0, 1, 1}, {0, 2, 1}, {0, 1, 1}};
	public static int[][]  linearFilterKernel3 = {{1, 1, 1}, {1, 2, 1}, {0, 0, 0}} ;
	public static int[][]  linearFilterKernel4 = {{1, 1, 1}, {1, 2, 1}, {0, 0, 0}} ;
	
	
	public static BufferedImage generalLinerFiltering (BufferedImage originalImage ,int[][] kernel){
		
		BufferedImage newImage = originalImage ;           
		for(int j=0 ; j<originalImage.getHeight() ; j++)
		{
			for(int i=0 ; i<originalImage.getWidth() ; i++)
			{
				if(conditionsToApplyFilter(originalImage, j, i, kernel))
				{
					ArrayList<Integer> mustBeEqual = new ArrayList<>() ; 
					int targetHeight=0 ; 
					int targetWidth =0;
					int areaOfInterest[][] = findAreaOfInterest(originalImage , 3, 3, j, i) ; 
					
					for(int i2=0 ; i2<kernel.length; i2++)
					{
						for(int j2=0 ; j2<kernel[0].length; j2++)
						{
							switch (kernel[i2][j2]) {
							case 0:
								break;
							case 1: 
								mustBeEqual.add(areaOfInterest[i2][j2]) ;
								break ; 
							case 2:
								targetHeight=i2 ; 
								targetWidth= j2 ;
								break ; 
							default:
								break;
							}//switch case
						}
					}	
					boolean areEqual  = true ;
					int value = mustBeEqual.get(0) ;
					for(int number : mustBeEqual){
						if(number!=value){
							areEqual=false ; 
						}
					}
					if(areEqual){
						int newPixel= GeneralImagingOperations.colorToRGB(new Color(newImage.getRGB(i+targetWidth, j+targetHeight)).getAlpha() , value, value, value) ;
						newImage.setRGB(i+targetWidth, j+targetHeight, newPixel);
					}
				}//condition
			}
		}
		return newImage ; 
	}
			
	public static int[][] findAreaOfInterest(BufferedImage image, int kernelLength , int kernelHeight, int currentHeight, int currentWidth)
	{
		int[][] areaOfInterest = new int[kernelHeight][kernelLength] ; 
		for(int i=0; i< areaOfInterest.length; i++){
			for(int j=0; j<areaOfInterest[0].length; j++){
				areaOfInterest[i][j]= new Color(image.getRGB(i+currentWidth, j+currentHeight)).getRed()  ;
			}
		}
		for(int number[] : areaOfInterest){
			for(int number2: number){
				//System.out.println(number2);
			}
		}
		//System.out.println("----------------------------------------------->"); 
		return areaOfInterest ; 
	}
	
	
	public static boolean conditionsToApplyFilter(BufferedImage image, int currentHeight, int currentWidth, int[][] kernel)
	{
		boolean conditions ; 
		int kernelHeight = kernel.length ;
		int kernelWidth = kernel[0].length ;
		
		if(currentHeight + kernelHeight - 1 <image.getHeight() && currentWidth+kernelWidth-1 <image.getWidth()){
			conditions = true  ;
		}else{
			conditions = false  ;  
		}
		return conditions ; 
	}
		
	
	public static void main(String args[])
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		try{
			image = ImageIO.read(new File("imagestest/binarizedImage.jpg"));
			image = imageprocessing.tools.ThresHolding.grayImage2Bin(image);
			image2=image ; 
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0; i<10; i++)
		{
			image2 = LineraFileters.generalLinerFiltering(image2, linearFilterKernel1) ; 
			image2 = LineraFileters.generalLinerFiltering(image2, linearFilterKernel2) ; 
			image2 = LineraFileters.generalLinerFiltering(image2, linearFilterKernel3) ; 
			image2 = LineraFileters.generalLinerFiltering(image2, linearFilterKernel4) ;
			
		}
			
		try{
			image2 = imageprocessing.tools.ThresHolding.grayImage2Bin(image) ; 
			ImageIO.write(image2,"jpg", new File("imagestest/binarizedImageFiltered.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
	
}