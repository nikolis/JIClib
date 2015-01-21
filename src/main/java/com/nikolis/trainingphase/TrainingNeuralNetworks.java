package com.nikolis.trainingphase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import classifiers.neuralnetworks.learning.NeuralNetwork;


public class TrainingNeuralNetworks {
	double X[][] ;
	double Y[][] ;

	
	public void trainNeuralNetWork()
	{
		this.X=GenerateFeatureMatrices.readMatFile("trainingset.mat", "X");
		this.Y=GenerateFeatureMatrices.readMatFile("trainingset.mat", "Y");
		
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(30);
		thenn.add(25);
		thenn.add(14); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		nn.loadParameters(X, Y);
		
		nn.batchGradientDescemt(25, 0.001, 1, 1000, 14);
	}
	
	public void generateTrainSetMatrices()
	{
		GenerateFeatureMatrices genfet = new GenerateFeatureMatrices() ; 
		genfet.exportTrainingSetMatrices("trainingset.mat") ; 
	}
	
	
	
	public static void main(String args[])
	{
		PrintStream printStream;
		try {
			printStream = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(printStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		//trnn.generateTrainSetMatrices();
		trnn.trainNeuralNetWork(); 
		
	}
	
}
