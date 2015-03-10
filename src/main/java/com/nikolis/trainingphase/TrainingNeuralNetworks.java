package com.nikolis.trainingphase;

import java.util.ArrayList;




import com.nikolis.trainingphase.GenerateFeatureMatrices.featureExtractionTechnique;
import com.solveit.classifier.NeuralNetwork;

import classifiers.neuralnetworks.learning.ArtificialNeuralNetwork;


public class TrainingNeuralNetworks {
	
	
	static final  int numberOfClasses = 4 ; 
	static public featureExtractionTechnique featureExtractionTeqchniqueUsed ; 
	
	public NeuralNetwork trainNeuralNetWork()
	{
		final  double X[][] =MatFileGenerator.readMatFile("trainingSet.mat", "X");
		final  double Y[][] =MatFileGenerator.readMatFile("trainingSet.mat", "Y");
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
	
		thenn.add(X[0].length);
		thenn.add(5);
		thenn.add(5);
		thenn.add(numberOfClasses); 
		
		ArtificialNeuralNetwork neuralNetwork = new ArtificialNeuralNetwork(thenn) ; 
		
		neuralNetwork.loadInputs(X, Y);
		neuralNetwork.batchGradientDescemt(0.1, 0, 100000, numberOfClasses);
		return neuralNetwork ; 
	}
	
	
	
	
	
	public void calculateTestSetAccuraccy(NeuralNetwork neuralNetwork)
	{
		double Xtemp[][] =MatFileGenerator.readMatFile("testSet.mat", "X");
		 
		double[][] hypothesis = neuralNetwork.predict(Xtemp) ;
		int[] results = new  int[hypothesis.length] ;
		
		for(int i=0; i<results.length; i++)
		{
			int max = -1 ; 
			double maxVal=-0.44 ; 
			
			for(int j=0; j<hypothesis[i].length; j++)
			{
				if(hypothesis[i][j]>=maxVal)
				{
					maxVal=hypothesis[i][j] ;
					max=j ; 
				}
			}
			results[i]=max ;
		}
		for(int i=0; i<results.length; i++)
		{
			System.out.println(results[i]);
		}
		
		
	}
	
	
	public void calculateTestSetAccuraccy2(NeuralNetwork neuralNetwork)
	{
		double Xtemp[][] =MatFileGenerator.readMatFile("trainingSet.mat", "X");
		 
		double[][] hypothesis = neuralNetwork.predict(Xtemp) ;
		int[] results = new  int[hypothesis.length] ;
		
		for(int i=0; i<results.length; i++)
		{
			int max = -1 ; 
			double maxVal=-0.44 ; 
			
			for(int j=0; j<hypothesis[i].length; j++)
			{
				if(hypothesis[i][j]>=maxVal)
				{
					maxVal=hypothesis[i][j] ;
					max=j ; 
				}
			}
			results[i]=max ;
		}
		for(int i=0; i<results.length; i++)
		{
			System.out.println(results[i]);
		}
		
		
	}
	
	
	public void generateTrainSetMatrices()
	{
		GenerateFeatureMatrices generateFeatureMatrices = new GenerateFeatureMatrices(TrainingNeuralNetworks.featureExtractionTeqchniqueUsed) ; 
		generateFeatureMatrices.exportTrainingSetMatrices("trainingSet.mat","images", 2) ; 
		GenerateFeatureMatrices generateFeatureMatrices2 = new GenerateFeatureMatrices(TrainingNeuralNetworks.featureExtractionTeqchniqueUsed) ;
		generateFeatureMatrices2.exportTrainingSetMatrices("testSet.mat", "images/tesset", 2);
		
	}
	
	
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		TrainingNeuralNetworks.featureExtractionTeqchniqueUsed = featureExtractionTechnique.ZernikeMoment ; 
		trnn.generateTrainSetMatrices();
		NeuralNetwork nn = trnn.trainNeuralNetWork() ; 
		trnn.calculateTestSetAccuraccy(nn);
		//trnn.calculateTestSetAccuraccy2(nn);
	}
	
}
