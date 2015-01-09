package imageprocessing.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;


import imageprocessing.utilities.*;


public class LineraFileters {
	BufferedImage originalImage  ; 
	BufferedImage newImage  ; 
	
	//This is the representation of The kernels to be used
	//The 1 denotes a digits that will be take in account the 2 is the target
	//Digit and the 0 is the digits that we won't care for 
	public static int[]  linearFilterKernel1 = {1, 1, 1, 1, 2, 1, 0, 0, 0} ;
	public static int[]  linearFilterKernel2 = {0, 1, 1, 0, 2, 1, 0, 1, 1};
	public static int[]  linearFilterKernel3 = {1, 1, 1, 1, 2, 1, 0, 0, 0} ;
	public static int[]  linearFilterKernel4 = {1, 1, 1, 1, 2, 1, 0, 0, 0} ;
	
	
	
	public LineraFileters(BufferedImage originalImage)
	{
		this.originalImage = originalImage ; 
		this.newImage=originalImage ; 
	}
	
	
	public BufferedImage applyLinearFilters(int[] currentFilter)
	{
		int variableOfScale = 3 ; 
		int[] areaOfInterest = new int[variableOfScale*variableOfScale] ;
		int counter=0 ; 
		int targetPixelH =0 ; 
		int targetPixelW=0 ; 
		
		
		//TODO more dinamic imageHeight and width should
		//be increased according to the size of the kernel being appalyed
		for(int imageHeight=2, startH=0; imageHeight<originalImage.getHeight(); imageHeight+=variableOfScale,startH+=variableOfScale)
		{
			for(int imageWidth=2, startW=0; imageWidth<originalImage.getWidth(); imageWidth+=variableOfScale,startW+=variableOfScale)
			{	//System.out.println("--------------------------------------------------->");
				//System.out.println("region is width from: "+ startW +" until : "+ imageWidth);
				//System.out.println("region is height from: "+ startH+ " ubtil : "+ imageHeight);
				
				
				for(int inRegionHeight=startH; inRegionHeight<=imageHeight; inRegionHeight++){
					for(int inRegionWidth=startW; inRegionWidth<=imageWidth; inRegionWidth++){
						areaOfInterest[counter++]=new Color(originalImage.getRGB(imageWidth, imageHeight)).getRed()  ;
						//System.out.println("The position Of The Number currently taken is width : "+ inRegionWidth +"and the heights is : "+ inRegionHeight );
						//System.out.println("counter is : "+ counter); 
						
						if(counter==findTarget(currentFilter)){
							targetPixelH=inRegionHeight ; 
							targetPixelW=inRegionWidth ; 
							//System.out.println("And the target Pixel is in width : "+targetPixelW +"  and in the height : "+targetPixelH);
						}
					}
				}
				int pixelValue = findPixelValu(currentFilter, areaOfInterest);
				if(pixelValue!=-1){
					int alpha = new Color(originalImage.getRGB(targetPixelW, targetPixelH)).getAlpha()  ;
					int newPixel ; 
					newPixel= GeneralImagingOperations.colorToRGB(alpha, pixelValue, pixelValue, pixelValue) ; 
					newImage.setRGB(targetPixelW, targetPixelH, newPixel);
				}
				targetPixelW=0 ; 
				targetPixelH =0 ;
				counter =0 ; 
				
				//areaOfInterest[counter]= new Color(originalImage.getRGB(imageWidth, imageHeight)).getRed()  ;
				//int alpha = new Color(grayImage.getRGB(i, j)).getAlpha()  ;
			}
		}
		return newImage ;
	}
	
	public static int findTarget(int[] theKernel)
	{
		int sizeOfKernel = theKernel.length ; 
		int position = 0 ; 
		for(int i=0; i<sizeOfKernel; i++){
			if(theKernel[i]==2){
				position= i ; 
			}
		}
		return position ; 
	}
	
	
	public static int findPixelValu(int[] kernel, int[] regionOfInterest){
		boolean mustChange = true    ; 
		int firstDigiValue  ; 
		ArrayList<Integer> mustBeEqual = new ArrayList<>() ;
		for(int i=0 ;i<kernel.length; i++){
			if(kernel[i]==1){
				mustBeEqual.add(regionOfInterest[i]) ;
			}
		}
		firstDigiValue= mustBeEqual.get(0) ; 
		for(int number: mustBeEqual){
			//System.out.println("Digits that must be equal are : "+ number); 
			if(firstDigiValue!=number){
				mustChange=false  ; 
			} 
		}

		//System.out.println("Does this have to change : "+ mustChange ); 
		//System.out.println("and the value would be  : "+ firstDigiValue );
		if(mustChange){
			return firstDigiValue  ; 
		}else{
			return -1 ;
		}
	}
	
	
	
	
	
	
	
	public static void main(String args[])
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		try{
			image = ImageIO.read(new File("images/nasdf43.jpg"));
			image = ThresHolding.grayImage2Bin(image);
		}catch(Exception e){
			e.printStackTrace();
		}
		LineraFileters linearFilter = new LineraFileters(image) ; 
		for(int i=0 ; i<40 ; i++)
		{
			image2 = linearFilter.applyLinearFilters(linearFilterKernel1) ; 
			image2 = linearFilter.applyLinearFilters(linearFilterKernel2) ;
			image2 = linearFilter.applyLinearFilters(linearFilterKernel3) ;
			image2 = linearFilter.applyLinearFilters(linearFilterKernel4) ;
		}
		try{
			ImageIO.write(image2,"jpg", new File("images/lalaba.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
	
}
