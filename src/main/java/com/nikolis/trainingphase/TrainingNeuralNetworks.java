package com.nikolis.trainingphase;

import featureexctraction.ZernikeMoments;
import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.Labeling;
import imageprocessing.tools.ThresHolding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TrainingNeuralNetworks {
	double x[][] ;
	double y[][] ;
	ArrayList<ArrayList<Double>> xe = new ArrayList<ArrayList<Double>>(); 
	ArrayList<Double> ye = new ArrayList<Double>() ;
	
	int numberOfMoments = 30 ; 
	
	public void examples()
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		String imageToRead ="images/tests/t7.jpg" ;
		String name =imageToRead.substring(imageToRead.indexOf(".jpg")-2,imageToRead.indexOf(".jpg"))  ;
		try{
			image = ImageIO.read(new File(imageToRead));
		}catch(Exception e){
			e.printStackTrace();
		}
		image2 = ColorToGray.toGrayLM(image) ;
		image2 = ThresHolding.grayImage2Bin(image2) ; 
		Labeling.mainMethod(image2, name);
		try{
			ImageIO.write(image2,"jpg", new File("images/output.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
		//Labeling labeling = new Labeling()
	}
	public int getclassNumber(String classToTrain)
	{
		switch (classToTrain) {
		case "0":
			return 0 ; 
		case "1":
			return 1 ; 
		case "2":
			return 2 ; 
		case "3":
			return 3 ; 
		case "4":
			return 4 ; 
		case "5":
			return 5 ; 
		case "6":
			return 6 ; 
		case "7":
			return 7 ; 
		case "9":
			return 9 ; 
		case "multiply":
			return 10 ; 
		case "divide":
			return 11 ; 
		case "pavla":
			return 12 ; 
		case "plus":
			return 13 ; 
		default:
			return -1 ; 
		}
	}
	
	public void creatingXandYs(String classToTrain)
	{	
		ZernikeMoments zernMom ;
		File folder = new File("images/"+classToTrain);
		File[] listOfFiles = folder.listFiles();
		BufferedImage image ; 
		    for (int i = 0; i < listOfFiles.length; i++) 
		    {
		      if (listOfFiles[i].isFile()) 
		      {
		        try {
					image = ImageIO.read(listOfFiles[i]) ;
					zernMom = new ZernikeMoments(image);
					ArrayList<Double> moments = zernMom.mainProcces(10, 1);
					xe.add(moments);
					ye.add((double)getclassNumber(classToTrain));
				} catch (IOException e) {
					e.printStackTrace();
				}
		      } 
		    }
	}
	
	public void TrainingForAllClasses()
	{
		creatingXandYs("0");
		creatingXandYs("1");
		creatingXandYs("2");
		creatingXandYs("3");
		creatingXandYs("4");
		creatingXandYs("5");
		creatingXandYs("6");
		creatingXandYs("7");
		creatingXandYs("8");
		creatingXandYs("9");
		creatingXandYs("divide");
		creatingXandYs("multiply");
		creatingXandYs("pavla");
		creatingXandYs("plus");
	}
	
	public void convertyx()
	{
		x= new double[xe.size()][30] ;
		y= new double[xe.size()][1];
		for(int i=0;i<xe.size(); i++)
		{
			for(int j=0; j<30;j++)
			{
				x[i][j]=xe.get(i).get(j);
			}
			y[i][1]=ye.get(i);
		}
	}
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks tr = new TrainingNeuralNetworks() ;
		tr.TrainingForAllClasses();
		tr.convertyx();
		
	}
	
}
