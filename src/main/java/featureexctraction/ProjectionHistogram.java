package featureexctraction;

import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.ThresHolding;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ProjectionHistogram {

	public static double[] findHorizontalHistogram(BufferedImage image)
	{
		image = ColorToGray.toGrayAvg(image) ;
		image = ThresHolding.grayImage2Bin(image);
		double[] histogram = new double[image.getWidth()];
		
		for(int i=0; i<image.getWidth(); i++)
		{
			int counter =0 ; 
			
			for(int j=0; j<image.getHeight(); j++)
			{
				if(new Color (image.getRGB(i, j)).getRed()==0 )
					counter++ ; 
			}
			histogram[i]=counter ; 
		}
		return null ; 
	}
	
	public static ArrayList<Double> findVerticalHistogram(BufferedImage image)
	{
		image = ColorToGray.toGrayAvg(image) ;
		image = ThresHolding.grayImage2Bin(image);
		ArrayList<Double> histogram = new ArrayList<Double>() ; 
		for(int i=0; i<image.getHeight(); i++)
		{
			int counter =0 ; 
			
			for(int j=0; j<image.getWidth(); j++)
			{
				if(new Color (image.getRGB(i, j)).getRed()==0 )
					counter++ ; 
			}
			histogram.add((double) counter) ; 
		}
		return histogram ; 
	}
	
	
	
	
	public static void main(String args[])
	{
		BufferedImage image =null;  
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			image = ImageIO.read(new File("/home/nikolis/git/SolveIT/images/1/subImage2t3.jpg"));
		}catch(Exception e){
			e.printStackTrace();
		}
		ProjectionHistogram.findHorizontalHistogram(image) ; 
		
	}
}
