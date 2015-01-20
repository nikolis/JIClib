package com.nikolis.trainingphase;

import classifiers.neuralnetworks.learning.NeuralNetwork;


public class TrainingNeuralNetworks {
	double X[][] ;
	double Y[][] ;

	
	public void trainNeuralNetWork()
	{
		this.X=GenerateFeatureMatrices.readMatFile("trainingset.mat", "X");
		this.Y=GenerateFeatureMatrices.readMatFile("trainingset.mat", "Y");
		for(int i=0; i<Y.length; i++)
		{
			for(int j=0; j<Y[i].length; j++)
			{
				System.out.println(Y[i][j]);
			}
		}
		NeuralNetwork nn = new NeuralNetwork(3) ; 
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
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		//trnn.generateTrainSetMatrices();
		trnn.trainNeuralNetWork(); 
	}
	
}
