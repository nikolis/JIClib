package com.nikolis.trainingphase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import classifiers.neuralnetworks.learning.NeuralNetwork;
import featureexctraction.ProjectionHistogram;
import featureexctraction.ZernikeMoments;


public class TrainingNeuralNetworks {
	double X[][] ;
	double Y[][] ;

	
	public void trainNeuralNetWork()
	{
		this.X=GenerateFeatureMatrices.readMatFile("trainingSet.mat", "X");
		this.Y=GenerateFeatureMatrices.readMatFile("trainingSet.mat", "Y");
		
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(300);
		thenn.add(25);
		thenn.add(25);
		thenn.add(14); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		nn.loadParameters(X, Y);
		
		nn.batchGradientDescemt(25, 0.001, 1, 5000, 14);
	}
	
	public void generateTrainSetMatrices()
	{
		GenerateFeatureMatrices genfet = new GenerateFeatureMatrices() ; 
		genfet.exportTrainingSetMatrices("trainingset.mat") ; 
	}
	
	public int  predict(String pathToPic, int i)
	{
		ArrayList<Double> feature ; 
		int prediction =-1 ; 
		ZernikeMoments zernMom ;
		BufferedImage image ; 
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(300);
		thenn.add(25);
		thenn.add(25);
		thenn.add(14); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		try {
			image = ImageIO.read(new File(pathToPic)) ;
			if(i==0)
			{
				zernMom = new ZernikeMoments(image);
				feature = zernMom.mainProcces(10, 1);
			}else
			{
				feature = ProjectionHistogram.findVerticalHistogram(image);
			}
			System.out.println(GenerateFeatureMatrices.convertArrayListToNormalArray(feature).length) ; 
			prediction = nn.predict(GenerateFeatureMatrices.convertArrayListToNormalArray(feature));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prediction;
	}
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		//trnn.generateTrainSetMatrices();
		//trnn.trainNeuralNetWork(); 
		System.out.println(trnn.predict("/home/nikolis/git/SolveIT/images/8/subImage2019.jpg",1));
		
	}
	
}
