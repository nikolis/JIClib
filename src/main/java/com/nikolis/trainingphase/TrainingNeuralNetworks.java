package com.nikolis.trainingphase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.nikolis.trainingphase.GenerateFeatureMatrices.featureExtractionTechnique;

import classifiers.neuralnetworks.learning.NeuralNetwork;
import classifiers.neuralnetworks.utilities.NeuralHelper;
import featureexctraction.ProjectionHistogram;
import featureexctraction.ProjectionHistogram.typeOfProjection;
import featureexctraction.ZernikeMoments;


public class TrainingNeuralNetworks {
	double X[][] ;
	double Y[][] ;
	static final  int numberOfClasses = 4 ; 
	
	public enum featureExtraction
	{
		ZernikeMoment,ProjectionHistogram,hubridZernikeHistogram
	}
	
	
	public void trainNeuralNetWork()
	{
		this.X=GenerateFeatureMatrices.readMatFile("trainingSet.mat", "X");
		this.Y=GenerateFeatureMatrices.readMatFile("trainingSet.mat", "Y");
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(this.X[0].length);
		thenn.add(20);
		thenn.add(20);
		thenn.add(numberOfClasses); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		nn.loadParameters(X, Y);
		
		nn.batchGradientDescemt(0.01, 0, 2000, numberOfClasses);
	}
	
	public void testNeuralNetworkPerformance()
	{
		int countCorrect =0 ;
		int countMistakes =0 ; 
		double Xtemp[][] ;
		double Ytemp[][] ;
		double result[][] ; 
		Xtemp=GenerateFeatureMatrices.readMatFile("testSet.mat", "X");
		Ytemp=GenerateFeatureMatrices.readMatFile("testSet.mat", "Y");
		//System.out.println(Xtemp.length);
		//System.out.println(Ytemp.length);
		ArrayList<Integer> thenn =  new ArrayList<Integer>() ;
		thenn.add(Xtemp[0].length);
		thenn.add(20);
		thenn.add(20);
		thenn.add(numberOfClasses); 
		NeuralNetwork nn = new NeuralNetwork(thenn) ; 
		result = nn.predict2(Xtemp);
		
		for(int i=0; i<result.length; i++)
		{
			int maxPOs =-1 ;
			double max =-1 ;
			for(int j=0; j<result[0].length; j++)
			{
				maxPOs =-1 ;
				max =-1 ; 
				if(result[i][j]>max)
				{
					max = result[i][j];
					maxPOs=j ; 
				}
			}
			if(maxPOs==(int)Ytemp[i][0])
			{
				countCorrect++;
			}
			else
			{
				countMistakes++ ; 
			}
		}
		System.out.println("Sostes provlepseis : "+countCorrect);
		System.out.println("Lathos provlepseis : "+countMistakes);

	}
	
	
	public int  predict(String pathToPic,featureExtractionTechnique fetTech )
	{
		int prediction =-1 ; 
		BufferedImage image ; 
		ArrayList<Double> features =null ; 
		try {
			image = ImageIO.read(new File(pathToPic)) ;
			
			switch (fetTech) {
			case  ProjectionHistogram :
				features=ProjectionHistogram.findHistogram(image, typeOfProjection.HorizontalAndVertical) ;
				break;
			case ZernikeMoment :
				ZernikeMoments	zernMom = new ZernikeMoments(image);
				features = zernMom.mainProcces(10, 1);
				break ;
			}
			ArrayList<Integer> thenn =  new ArrayList<Integer>() ; 
			thenn.add(GenerateFeatureMatrices.convertArrayListToNormalArray(features)[0].length) ;
			thenn.add(20);
			thenn.add(20);
			thenn.add(numberOfClasses); 
			NeuralNetwork nn = new NeuralNetwork(thenn) ; 
			prediction = nn.predict(GenerateFeatureMatrices.convertArrayListToNormalArray(features));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prediction;
	}
	
	
	
	public void generateTrainSetMatrices()
	{
		GenerateFeatureMatrices generateFeatureMatrices = new GenerateFeatureMatrices(featureExtractionTechnique.ProjectionHistogram) ; 
		generateFeatureMatrices.exportTrainingSetMatrices("trainingSet.mat","images") ; 
		GenerateFeatureMatrices generateFeatureMatrices2 = new GenerateFeatureMatrices(featureExtractionTechnique.ProjectionHistogram) ; 
		generateFeatureMatrices2.exportTrainingSetMatrices("testSet.mat","images/tesset");
	}
	
	
	
	public static void main(String args[])
	{
		TrainingNeuralNetworks trnn = new TrainingNeuralNetworks() ; 
		//trnn.generateTrainSetMatrices();
		trnn.trainNeuralNetWork();
		
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\0\\01.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\0\\02.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\0\\03.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\0\\04.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\0\\05.jpg",featureExtractionTechnique.ProjectionHistogram));
		
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\1\\11.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\1\\12.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\1\\13.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\1\\14.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\1\\15.jpg",featureExtractionTechnique.ProjectionHistogram));

		
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\2\\21.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\2\\22.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\2\\23.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\2\\24.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\2\\25.jpg",featureExtractionTechnique.ProjectionHistogram));
		
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\3\\31.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\3\\32.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\3\\33.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\3\\34.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\tesset\\3\\35.jpg",featureExtractionTechnique.ProjectionHistogram));
		
		trnn.testNeuralNetworkPerformance();
		
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\0\\subImage2t8.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\0\\subImage2t9.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\0\\subImage5t9.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\3\\nsf23.jpg",featureExtractionTechnique.ProjectionHistogram));
		System.out.println(trnn.predict("C:\\Users\\310176547\\Documents\\workspace-sts-3.6.2.RELEASE\\SolveIT\\images\\3\\subImage2s3.jpg",featureExtractionTechnique.ProjectionHistogram));
	}
	
}
