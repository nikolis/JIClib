package com.nikolis.trainingphase;

import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.ThresHolding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

import featureexctraction.ProjectionHistogram;
import featureexctraction.ZernikeMoments;


public class GenerateFeatureMatrices {
	
	private ArrayList<ArrayList<Double>> examplesFeatures ;
	private ArrayList<Double> examplesClasses ;
	private static final int  numberOfMoments =2 ;
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
	private void creatingXandYs(String sumbolToExtractFeatures,String pathToRootFile)
	{	
		File folder = new File(pathToRootFile+"/"+sumbolToExtractFeatures);
		File[] listOfFiles = folder.listFiles();
		ArrayList<Double> featuresForSingleExample = null ;
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
					examplesFeatures.add(featuresForSingleExample);
					examplesClasses.add((double)getclassNumber(sumbolToExtractFeatures));
					}  catch (IOException e) 
		        	{
					e.printStackTrace();
		        	}
		      } 
		    }
	}
	
	
	private void createExampleFeatureAndClassesArrays(String filePathToExamples)
	{
		creatingXandYs("0",filePathToExamples);
		creatingXandYs("1",filePathToExamples);
		creatingXandYs("2",filePathToExamples);
		creatingXandYs("3",filePathToExamples);
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
	
	public static double[][] readMatFile(String fileName, String arrayName)
	{
		MatFileReader matfilereader;
		double[][] array =null ;
		try
		{
			matfilereader = new MatFileReader(fileName);
			MLDouble heta = (MLDouble)matfilereader.getMLArray(arrayName);
			
			array = heta.getArray() ; 
		}catch (Exception e){
			e.printStackTrace();
		}
		return array ; 
	}
	
	public void exportTrainingSetMatrices(String name,String filePathToExamples)
	{
		createExampleFeatureAndClassesArrays(filePathToExamples) ;
		MatFileGenerator matfileGen = new MatFileGenerator(name) ; 
		double[][] classes = GeneralArraysUtilityClass.convertArrayListToNormalArray(examplesClasses) ;
		double[][] features = GeneralArraysUtilityClass.convertArrayListToNormalArray2(examplesFeatures) ;
		matfileGen.addArray(classes, "Y");
		matfileGen.addArray(features, "X");
		matfileGen.writeFile(); 
	}
	
	
}
