package com.nikolis.trainingphase;

import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.ThresHolding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import featureexctraction.ProjectionHistogram;
import featureexctraction.ZernikeMoments;


public class GenerateFeatureMatrices {
	
	private ArrayList<ArrayList<Double>> examplesFeatures ;
	private ArrayList<Double> examplesClasses ;
	private static final int  numberOfMoments =4 ;
	featureExtractionTechnique technique ; 
	
	public GenerateFeatureMatrices(featureExtractionTechnique technique)
	{
		this.technique=technique;
		examplesFeatures =new ArrayList<ArrayList<Double>>(); 
		examplesClasses = new ArrayList<Double>() ;
	}
	
	
	public enum featureExtractionTechnique
	{
		ZernikeMoment,ProjectionHistogram
	}
	
	/**
	 * Uses the appropriate method to extract features for a single Example and adds it To the arrayList containing All the features
	 * and correspondingly adds the number of the class that the Character Represents. 	 
	 * @param sumbolToExtractFeatures
	 * @param pathToRootFile
	 */
	private void creatingXandYs(String sumbolToExtractFeatures,String pathToRootFile, int orderOfFeatures)
	{	
		File folder = new File(pathToRootFile+"/"+sumbolToExtractFeatures);
		File[] listOfFiles = folder.listFiles();
		ArrayList<Double> featuresForSingleExample = null ;
		ArrayList<Double> highOrderFeatures = new ArrayList<Double>() ; 
		BufferedImage image ; 
		    for (int i = 0; i < listOfFiles.length; i++) 
		    {
		    	System.out.println(sumbolToExtractFeatures);
		    	System.out.println(pathToRootFile);
		      if (listOfFiles[i].isFile()) 
		      {
		        try
		        	{
						image = ImageIO.read(listOfFiles[i]) ;
						image = ColorToGray.toGrayLM(image) ; 
						image = ThresHolding.grayImage2Bin(image) ;
						switch (technique) {
							case  ZernikeMoment : 
								ZernikeMoments zernikeMoments = new ZernikeMoments(image) ;
								featuresForSingleExample = zernikeMoments.mainProcces(numberOfMoments, 1);
								break ;
							case ProjectionHistogram :
								featuresForSingleExample = ProjectionHistogram.findHistogram(image, ProjectionHistogram.typeOfProjection.HorizontalAndVertical) ;
								break ; 
							default:
								featuresForSingleExample = ProjectionHistogram.findHistogram(image, ProjectionHistogram.typeOfProjection.HorizontalAndVertical) ;
								break;
					}
					Iterator<Double> iterator = featuresForSingleExample.iterator() ; 
					
					for(int order=2; order<=orderOfFeatures; order++)
					{
						while(iterator.hasNext())
						{
							double feature = iterator.next() ; 
							double highFeature = Math.pow(feature, order);
							highOrderFeatures.add(highFeature) ; 
						}
					}
					featuresForSingleExample.addAll(highOrderFeatures) ; 
					examplesFeatures.add(featuresForSingleExample);
					examplesClasses.add((double)getclassNumber(sumbolToExtractFeatures));
					}  catch (IOException e) 
		        	{
					e.printStackTrace();
		        	}
		      } 
		    }
	}
	
	
	private void createExampleFeatureAndClassesArrays(String filePathToExamples, int orderOfFeatures)
	{
		creatingXandYs("0",filePathToExamples, orderOfFeatures);
		creatingXandYs("1",filePathToExamples, orderOfFeatures);
		creatingXandYs("2",filePathToExamples, orderOfFeatures);
		creatingXandYs("3",filePathToExamples, orderOfFeatures);
		creatingXandYs("4",filePathToExamples, orderOfFeatures);
		creatingXandYs("5",filePathToExamples, orderOfFeatures);
		creatingXandYs("6",filePathToExamples, orderOfFeatures);
		creatingXandYs("7",filePathToExamples, orderOfFeatures);
		creatingXandYs("8",filePathToExamples, orderOfFeatures);
		creatingXandYs("9",filePathToExamples, orderOfFeatures);
		
	}
	
	private int getclassNumber(String SymbolToExtract)
	{
		switch (SymbolToExtract) {
			case "multiply":
				return 10 ; 
			case "divide":
				return 11 ; 
			case "pavla":
				return 12 ; 
			case "plus":
				return 13 ; 
			default:
				return Integer.parseInt(SymbolToExtract) ;  
			}
	}
	
	
	public void exportTrainingSetMatrices(String name,String filePathToExamples, int orderOfFeatures)
	{
		createExampleFeatureAndClassesArrays(filePathToExamples, orderOfFeatures) ;
		MatFileGenerator matfileGen = new MatFileGenerator(name) ; 
		double[][] classes = GeneralArraysUtilityClass.convertArrayListToNormalArray(examplesClasses) ;
		double[][] features = GeneralArraysUtilityClass.convertArrayListToNormalArray2(examplesFeatures) ;
		matfileGen.addArray(classes, "Y");
		matfileGen.addArray(features, "X");
		matfileGen.writeFile(); 
	}
	
	
}
