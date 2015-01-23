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
import featureexctraction.ZernikeMoments;


public class TrainingNeuralNetworks {
	double X[][] ;
	double Y[][] ;

	
	public void trainNeuralNetWork()
	{
		this.X=GenerateFeatureMatrices.readMatFile("trainingSet.mat", "X");
		this.Y=GenerateFeatureMatrices.readMatFile("trainingSet.mat", "Y");
		
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(30);
		thenn.add(25);
		thenn.add(25);
		thenn.add(4); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		nn.loadParameters(X, Y);
		
		nn.batchGradientDescemt(25, 0.001, 1, 50000, 4);
	}
	
	public void generateTrainSetMatrices()
	{
		GenerateFeatureMatrices genfet = new GenerateFeatureMatrices() ; 
		genfet.exportTrainingSetMatrices("trainingset.mat") ; 
	}
	
	public int  predict(String pathToPic)
	{
		int prediction =-1 ; 
		ZernikeMoments zernMom ;
		BufferedImage image ; 
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(30);
		thenn.add(25);
		thenn.add(25);
		thenn.add(4); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		try {
			image = ImageIO.read(new File(pathToPic)) ;
			zernMom = new ZernikeMoments(image);
			ArrayList<Double> moments = zernMom.mainProcces(10, 1);
			System.out.println(GenerateFeatureMatrices.convertArrayListToNormalArray(moments).length) ; 
			prediction = nn.predict(GenerateFeatureMatrices.convertArrayListToNormalArray(moments));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prediction;
	}
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		//trnn.generateTrainSetMatrices();
		trnn.trainNeuralNetWork(); 
		//System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\0\\subImage2t9.jpg"));
		
	}
	
}
