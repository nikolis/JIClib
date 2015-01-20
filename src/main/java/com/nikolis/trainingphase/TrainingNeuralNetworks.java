package com.nikolis.trainingphase;


public class TrainingNeuralNetworks {
	double X[][] ;
	double Y[][] ;

	
	public void trainNeuralNetWork()
	{
		this.X=GenerateFeatureMatrices.readMatFile("trainingset.mat", "X");
		this.Y=GenerateFeatureMatrices.readMatFile("trainingset.mat", "Y");
		
	}
	
	public void generateTrainSetMatrices()
	{
		GenerateFeatureMatrices genfet = new GenerateFeatureMatrices() ; 
		genfet.exportTrainingSetMatrices("trainingset.mat") ; 
	}
	
	
	
	
	
}
