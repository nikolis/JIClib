package imageprocessing.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;





import imageprocessing.utilities.*;



public class Labeling {

	
	int subImageWidth = 300 ; 
	int subImageHeight = 300 ;
	Stack<Integer[]> searchingStack ; 
	BufferedImage originalImage ;
	ArrayList<Integer> pixelsGiven  ; 
	Random random ; 
 
	BufferedImage returnImage ; 
	int[][] classes ; 
	
	
	public Labeling(BufferedImage originalImage)
	{
		searchingStack = new Stack<>() ; 
		this.originalImage = originalImage ; 
		classes = new int[originalImage.getWidth()][originalImage.getHeight()] ;//TODO check compatibility with image iteration 
		pixelsGiven = new ArrayList<>() ;
		random = new Random(255) ; 
		for(int i=0 ; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
				classes[i][j]=0 ; 
			}
		}
	}
	
	
	public boolean isBlack(int i,int j)
	{
		int red = new Color(originalImage.getRGB(i, j)).getRed()  ;
		return red==0 ; 
	}
	
	public int  putLabels()
	{
		int taxiPragmaton =1 ; 
		for(int i=0; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
				if(isBlack(i, j) && classes[i][j]==0)
				{
					classes[i][j]=++taxiPragmaton ;
					checkForNeibours(i, j, taxiPragmaton);
					while(!searchingStack.isEmpty())
					{
						Integer[] coordinates = searchingStack.pop() ; 
						checkForNeibours(coordinates[0],coordinates[1], taxiPragmaton);
					}
				 
				} 
			}
		}
		return (taxiPragmaton); 
	}
	
	
	public void checkForNeibours(int i, int j,int taksiPragmaton)
	{
		if((i-1>=0)&&(j-1>=0) && classes[i-1][j-1]==0)
		{
			if(isBlack(i-1, j-1))
			{
				classes[i-1][j-1]=taksiPragmaton ;
				Integer[] coordinates = new Integer[2] ; 
				coordinates[0]=i-1; //autoBoxing
				coordinates[1]=j-1 ;//autoBoxing
				searchingStack.push(coordinates) ; 
			}
		}
		if(j-1>=0)
		{
			if(isBlack(i, j-1) && classes[i][j-1]==0)
			{
				classes[i][j-1]=taksiPragmaton ; 
				Integer[] coordinates= new Integer[2] ;
				coordinates[0] = i ; 
				coordinates[1] =j-1 ; 
				searchingStack.push(coordinates) ;
			}
		}
		if(i+1<originalImage.getWidth() && j-1>=0 && classes[i+1][j-1]==0)
		{
			if(isBlack(i+1, j-1) && classes[i+1][j-1]==0)
			{
				classes[i+1][j-1]=taksiPragmaton ; 
				Integer[] coordinates = new Integer[2] ; 
				coordinates[0]=i+1 ; 
				coordinates[1] =j-1 ;
				searchingStack.push(coordinates);
			}
		}
		if(i+1<originalImage.getWidth() && classes[i+1][j]==0)
		{
			if(isBlack(i+1, j) && classes[i+1][j]==0)
			{
				classes[i+1][j]=taksiPragmaton ; 
				Integer[] coordinates = new Integer[2] ; 
				coordinates[0] = i+1 ; 
				coordinates[1] = j ;
				searchingStack.push(coordinates);
			}
		}
		if(i+1<originalImage.getWidth() && j+1<originalImage.getHeight() && classes[i+1][j+1]==0)
		{	
			if(isBlack(i+1, j+1) && classes[i+1][j+1]==0)
			{
				classes[i+1][j+1] = taksiPragmaton ; 
				Integer[] coordinates = new Integer[2] ;
				coordinates[0]=i+1 ; 
				coordinates[1]=j+1 ; 
				searchingStack.push(coordinates) ;
			}
			
		}
		if(j+1<originalImage.getHeight() && classes[i][j+1]==0)
		{
			if(isBlack(i, j+1) && classes[i][j+1]==0)
			{
				classes[i][j+1] =taksiPragmaton ; 
				Integer[] coordinates = new Integer[2] ;
				coordinates[0] =i ; 
				coordinates[1] = j+1 ; 
				searchingStack.push(coordinates) ; 
			}
		}
		if(i-1>=0 && j+1<originalImage.getHeight() && classes[i-1][j+1]==0)
		{
			if(isBlack(i-1, j+1) && classes[i-1][j+1]==0)
			{
				classes[i-1][j+1] = taksiPragmaton ; 
				Integer[] coordinates = new Integer[2] ; 
				coordinates[0] = i-1 ; 
				coordinates[1] = j+1 ; 
				searchingStack.push(coordinates) ; 
			}
		}
		if(i-1>=0 && classes[i-1][j]==0)
		{
			if(isBlack(i-1, j) && classes[i-1][j]==0)
			{
				classes[i-1][j] = taksiPragmaton ; 
				Integer[] coordinates = new Integer[2] ; 
				coordinates[0] = i-1 ; 
				coordinates[1] = j ; 
				searchingStack.push(coordinates) ; 
			}
		}
	}
	public int findOffsetWidth(int klass)
	{
		
		int halfObjectWidth = (findRightMostForClass(klass)-findLeftMostForClass(klass)+1)/2;
		int halfImageWidth=(originalImage.getWidth()/2) ;
		int pointToStart = halfImageWidth-halfObjectWidth  ; 
		int offSet = pointToStart-findLeftMostForClass(klass) ;  
		
		return offSet ;
	}
	
	public int findOffsetHeight(int klass)
	{
		int halfObjectHeight = (findBotMostForClass(klass)-findTopMostForClass(klass)+1)/2 ;
		int halfImageHeight  = (originalImage.getHeight()/2); 
		int pointToStart = halfImageHeight-halfObjectHeight ; 
		int offSet  = pointToStart - findTopMostForClass(klass); 
		
		return offSet ;
	}
	

	
	
	
	public void paintTheRegions(int klass,String tade)
	{
		returnImage  = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType()) ;
		int newPixel ; 
		int alpha = new Color(originalImage.getRGB(0, 0)).getAlpha()  ;
		newPixel= GeneralImagingOperations.colorToRGB(alpha,0, 0, 0) ;
		
		//int divisionWidth = originalImage.getWidth()/(subImageWidth) ;
		//int divisionHeight = originalImage.getHeight()/(subImageHeight) ;
		
		for(int i=0; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
				int alpha2 = new Color(originalImage.getRGB(0, 0)).getAlpha()  ;
				int red = 255 ;
				int green = 255  ;
				int blue = 255  ;
				int newPixel2 = GeneralImagingOperations.colorToRGB(alpha2,red,green,blue) ; 
				returnImage.setRGB(i, j, newPixel2);
			}
		}
		
		
		int offSetWidth = findOffsetWidth(klass) ; 
		int offSetHeight = findOffsetHeight(klass) ;
		
		for(int i=0; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
				if(classes[i][j]==klass)
				{
					returnImage.setRGB(i+offSetWidth, j+offSetHeight, newPixel);
				}
			}
		}
		returnImage = imageScaling(returnImage, klass) ; 
		try {
			ImageIO.write(returnImage,"jpg", new File("images/subImage"+klass+tade+".jpg")) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage imageScaling(BufferedImage originalImage, int klass)
	{
		BufferedImage returnImage = new BufferedImage(subImageWidth, subImageWidth, originalImage.getType()) ; 
		
		int startingPointWidth = findStartingPointForReturnImage(originalImage.getWidth(), subImageWidth) ; 
		int startingPointHeight = findStartingPointForReturnImage(originalImage.getHeight(), subImageHeight) ; 
	 
		for(int i=startingPointWidth, retImWidth=0 ; i<startingPointWidth+300; i++,retImWidth++)
		{			
			for(int j=startingPointHeight,retImageHeight=0; j<startingPointHeight+300; j++,retImageHeight++)
			{
				returnImage.setRGB(retImWidth, retImageHeight, originalImage.getRGB(i, j));
			}
		}
		
		return returnImage ; 
	}
	
	
	public int  findStartingPointForReturnImage(int originalImageValue, int returnImageValue)
	{
		int originalImageValueHalf = originalImageValue/2 ;
		int returnImageValueHalf = returnImageValue/2 ; 
		
		int startingPoint = originalImageValueHalf - returnImageValueHalf;
		
		return startingPoint ; 
	}
	
	
	
	
	public int findRightMostForClass(int klass)
	{
		int max=0 ;
		for(int i=0; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
				if(classes[i][j]==klass && i>max)
				{
					max=i ; 
				}
			}
		}
		
		
		
		
		
		return max ; 
	}
	
	
	public int findLeftMostForClass(int klass)
	{
		int max= originalImage.getWidth();
		
		for(int i=0; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
					if(classes[i][j]==klass && i<max)
					{
						max = i ; 
					}
			}
		}
		return max ; 
	}
	
	public int findTopMostForClass(int klass)
	{
		int max = originalImage.getHeight() ;
		
		for(int i=0; i<originalImage.getWidth(); i++)
		{
			for(int j=0; j<originalImage.getHeight(); j++)
			{
					if(classes[i][j]==klass && j<max)
					{
						max = j ; 
					}
			}
		}
		return max ;
	}
	public int findBotMostForClass(int klass)
	{
		int max = 0  ; 
		
		for(int i=originalImage.getWidth()-1; i>=0; i--)
		{
			for(int j=originalImage.getHeight()-1; j>=0; j--)
			{
					if(classes[i][j]==klass && max<j )
					{
						max = j ; 
					}
			}
		}
		return max ;
	}
	
	public void catTheDifferentComponentsIntoDifferentPhotos(int klass,String tade)
	{
		int right = findRightMostForClass(klass);
		int left = findLeftMostForClass(klass) ; 
		int top = findTopMostForClass(klass); 
		int bot = findBotMostForClass(klass); 
		System.out.println(right);
		System.out.println(left);
		System.out.println(top);
		System.out.println(bot);
		BufferedImage subImage = new  BufferedImage(right-left, bot-top, originalImage.getType()) ;
		for(int i=left; i<right; i++)
		{
			for(int j=top; j<bot; j++)
			{
				subImage.setRGB(i-left, j-top, originalImage.getRGB(i, j));
			}
		}
		try{
			ImageIO.write(subImage,"jpg", new File("images/subImage"+klass+tade+".jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
	
	public static void mainMethod(BufferedImage image,String name)
	{
		image = ThresHolding.grayImage2Bin(image) ;
		Labeling labeling = new Labeling(image) ;
		int klasis = labeling.putLabels();
		for(int i=2; i<=klasis; i++)
		{
			labeling.paintTheRegions(i,name); 
		}
	}
	
	public static void main(String args[])
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		BufferedImage image12=null;
		String filetoread = "images/tests/images5.jpg" ;
		try{
			image = ImageIO.read(new File(filetoread));
			image12 = ThresHolding.grayImage2Bin(image) ;
		}catch(Exception e){
			e.printStackTrace();
		}
		String tade =filetoread.substring(filetoread.indexOf(".jpg")-2,filetoread.indexOf(".jpg"))  ;
		Labeling labeling = new Labeling(image12) ; 
		int klasis = labeling.putLabels();
		for(int i=2; i<=klasis; i++)
		{
			labeling.paintTheRegions(i,tade); 
		}
		image2 = labeling.returnImage ; 
		
		try
		{
			ImageIO.write(labeling.returnImage,"jpg", new File("images/nsf23.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
		
	}
}
