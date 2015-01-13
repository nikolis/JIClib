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
import javax.security.auth.x500.X500Principal;

import classifiers.neuralnetworks.learning.NeuralNetwork;
import classifiers.neuralnetworks.utilities.NeuralHelper;

public class TrainingNeuralNetworks {
	double x[][] ;
	double y[][] ;
	ArrayList<ArrayList<Double>> xe = new ArrayList<ArrayList<Double>>(); 
	ArrayList<Double> ye = new ArrayList<Double>() ;
	double x2[][];
	ArrayList<Double> xe2 = new ArrayList<Double>(); 
	int numberOfMoments = 30 ; 
	
	public void examples()
	{
		BufferedImage image = null ; 
		BufferedImage image2 = null ; 
		String imageToRead ="images/tests/t23.jpg" ;
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
		case "8":
			return 8 ; 
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
	
	public void createXandYsForSpesificImage(String pathToFile) 
	{ 
		BufferedImage image;
		try {
			image = ImageIO.read(new File(pathToFile));
			ZernikeMoments zernMom  = new ZernikeMoments(image);
			xe2 = zernMom.mainProcces(10, 1);
			convertx() ; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			y[i][0]=ye.get(i);
		}
	}
	
	public void convertx()
	{
		x2= new double[1][30] ;
		
			for(int j=0; j<30;j++)
			{
				x2[0][j]=xe2.get(j);
			}
	}
	public static void training()
	{
		
	}
	
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks tr = new TrainingNeuralNetworks() ;
		//tr.examples();
		tr.TrainingForAllClasses();
		tr.convertyx();
		NeuralNetwork nn = new NeuralNetwork() ; 
		nn.loadParameters(tr.x, tr.y);
		nn.workingItOut(25, 0.001, 1, 1000, 14);
		//NeuralNetwork nn = new NeuralNetwork() ;
		//TrainingNeuralNetworks tr = new TrainingNeuralNetworks() ; 
		nn.loadTRainedThetas(25, 14);
		tr.createXandYsForSpesificImage("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\4\\subImage215.jpg") ; 
		System.out.println(nn.predict(tr.x2));
		
	}
	
}
