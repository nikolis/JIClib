package com.nikolis.trainingphase;

import java.util.ArrayList;



import com.nikolis.trainingphase.GenerateFeatureMatrices.featureExtractionTechnique;

import classifiers.neuralnetworks.learning.ArtificialNeuralNetwork;


public class TrainingNeuralNetworks {
	
	
	static final  int numberOfClasses = 4 ; 
	static public featureExtractionTechnique featureExtractionTeqchniqueUsed ; 
	
	public void trainNeuralNetWork()
	{
		final  double X[][] =GenerateFeatureMatrices.readMatFile("trainingSet.mat", "X");
		final  double Y[][] =GenerateFeatureMatrices.readMatFile("trainingSet.mat", "Y");
		
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
	
		thenn.add(X[0].length);
		thenn.add(50);
		thenn.add(30);
		thenn.add(50);
		thenn.add(30);
		thenn.add(50);
		thenn.add(numberOfClasses); 
		
		ArtificialNeuralNetwork neuralNetwork = new ArtificialNeuralNetwork(thenn) ; 
		
		neuralNetwork.loadInputs(X, Y);
		neuralNetwork.batchGradientDescemt(0.001, 0, 10000, numberOfClasses);
	}
	
	
	
	
	
	public void calculateTestSetAccuraccy()
	{
		double Xtemp[][] =GenerateFeatureMatrices.readMatFile("testSet.mat", "X");
		ArrayList<Integer> theNeurons =  new ArrayList<Integer>() ;
		theNeurons.add(Xtemp.length);
		theNeurons.add(50);
		theNeurons.add(30);
		theNeurons.add(50);
		theNeurons.add(30);
		theNeurons.add(50);
		theNeurons.add(numberOfClasses); 
		double[][] hypothesis = ArtificialNeuralNetwork.predict(Xtemp, theNeurons) ;
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
		generateFeatureMatrices.exportTrainingSetMatrices("trainingSet.mat","images") ; 
		GenerateFeatureMatrices generateFeatureMatrices2 = new GenerateFeatureMatrices(TrainingNeuralNetworks.featureExtractionTeqchniqueUsed) ;
		generateFeatureMatrices2.exportTrainingSetMatrices("testSet.mat", "images/tesset");
		
	}
	
	
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		TrainingNeuralNetworks.featureExtractionTeqchniqueUsed = featureExtractionTechnique.ZernikeMoment ; 
		trnn.generateTrainSetMatrices();
		trnn.trainNeuralNetWork();
		trnn.calculateTestSetAccuraccy();
	}
	
}
