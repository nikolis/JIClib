package com.nikolis.trainingphase;

import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.ThresHolding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.imageio.ImageIO;









import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

import featureexctraction.ProjectionHistogram;
import featureexctraction.ZernikeMoments;

public class GenerateFeatureMatrices {
	
	private ArrayList<ArrayList<Double>> examplesFeatures ;
	private ArrayList<Double> examplesClasses ;
	private static final int  numberOfMoments =10 ;
	private Collection<MLArray>  collectionMl ;
	featureExtractionTechnique technique ; 
	
	public GenerateFeatureMatrices(featureExtractionTechnique technique)
	{
		this.technique=technique;
		examplesFeatures =new ArrayList<ArrayList<Double>>(); 
		examplesClasses = new ArrayList<Double>() ;
		collectionMl = new ArrayList<MLArray>();
	}
	
	
	public enum featureExtractionTechnique
	{
		ZernikeMoment,ProjectionHistogram
	}
	
	
	
	private  void exportTheMatrices(ArrayList<Double> examplesClasses, ArrayList<ArrayList<Double>> examplesFeatures,String trainSetFileName )
	{
		addArrayListToMlarray(examplesClasses, "Y");
		addArrayListToMlarray2(examplesFeatures, "X");
		try {
			MatFileWriter matWriter = new MatFileWriter() ;
			matWriter.write(trainSetFileName, collectionMl);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void exportTheMatricesGeneral(double[][] array1 , double[][] array2,String nameArray1, String nameArray2, String nameFile)
	{
		Collection<MLArray>  collectionMl = new ArrayList<MLArray>();
		randomlySuffleArray(array1,array2);
		
		MLArray mlArray1 = new MLDouble(nameArray1,array1) ;
		MLArray mlArray2 = new MLDouble(nameArray2,array2) ;
		
		collectionMl.add(mlArray1) ;
		collectionMl.add(mlArray2) ;
		
		try {
			MatFileWriter matWriter = new MatFileWriter() ;
			matWriter.write(nameFile, collectionMl);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public static void randomlySuffleArray(double[][] array,double[][] array2)
	{
		for(int j=0; j<20; j++)
		{
				
			for(int i=0; i>array.length; i++)
			{
				double[] temp = array[i];
				double[] temp2=array[i];
				int random = randInt(0, array.length-1);
				array[i]=array[random] ;
				array[random]=temp ;
				array2[i]=array[random] ;
				array[random]=temp2 ; 
			}
		
		}
	}
	
	
	/**
	 * Adds an arrayList of doubles in an ml array which will afterwords will be 
	 * saved in a .mat file 
	 * @param arrayList
	 * @param name
	 */
	private void addArrayListToMlarray(ArrayList<Double> arrayList,String name)
	{
		MLArray mlArray = new MLDouble(name,convertArrayListToNormalArray(arrayList) ) ;
		collectionMl.add(mlArray) ;	 
	}
	/**
	 * Adds an arrayList of arrayLists of doubles in an ml array which will afterwards will be 
	 * saved in a .mat file 
	 * @param arrayList
	 * @param name
	 */
	private void addArrayListToMlarray2(ArrayList<ArrayList<Double>> arrayList,String name)
	{
		MLArray mlArray = new MLDouble(name,convertArrayListToNormalArray2(arrayList) ) ;
		collectionMl.add(mlArray) ;
	}
	
	
	
	static double[][] convertArrayListToNormalArray(ArrayList<Double> arrayList)
	{
		double[][] normalArray = new double[arrayList.size()][1] ; 
		
		for(int i=0; i<arrayList.size(); i++)
		{
			normalArray[i][0]=arrayList.get(i); 
		}
		
		return normalArray ;
	}
	
	private double[][] convertArrayListToNormalArray2(ArrayList<ArrayList<Double>> arrayList)
	{
		double[][] normalArray = new double[arrayList.size()][arrayList.get(0).size()] ; 
		for(int i=0; i<arrayList.size(); i++)
		{
			ArrayList<Double> temp = arrayList.get(i) ;
			for(int j=0; j<temp.size(); j++)
			{
				normalArray[i][j] = temp.get(j);
			}
		}
		return normalArray ;
	}
	
	
	
	private void creatingXandYs(String classToTrain,String pathToExamplesFile)
	{	
		
		File folder = new File(pathToExamplesFile+"/"+classToTrain);
		File[] listOfFiles = folder.listFiles();
		ArrayList<Double> features = null ;
		BufferedImage image ; 
		    for (int i = 0; i < listOfFiles.length; i++) 
		    {
		    	System.out.println(classToTrain);
		    	System.out.println(pathToExamplesFile);
		      if (listOfFiles[i].isFile()) 
		      {
		        try {
					image = ImageIO.read(listOfFiles[i]) ;
					image = ColorToGray.toGrayLM(image) ; 
					image = ThresHolding.grayImage2Bin(image) ;
					switch (technique) {
						case  ZernikeMoment : 
							ZernikeMoments zernikeMoments = new ZernikeMoments(image) ;
							features = zernikeMoments.mainProcces(numberOfMoments, 1);
							break ;
						case ProjectionHistogram :
							features = ProjectionHistogram.findHistogram(image, ProjectionHistogram.typeOfProjection.HorizontalAndVertical) ;
							break ; 
						default:
							features = ProjectionHistogram.findHistogram(image, ProjectionHistogram.typeOfProjection.HorizontalAndVertical) ;
							break;
					}
					examplesFeatures.add(features);
					examplesClasses.add((double)getclassNumber(classToTrain));
				} catch (IOException e) {
					e.printStackTrace();
				}
		      } 
		    }
	}
	
	
	private void createExampleFeatureAndClassesArraysZernike(String filePathToExamples)
	{
		creatingXandYs("0",filePathToExamples);
		creatingXandYs("1",filePathToExamples);
		creatingXandYs("2",filePathToExamples);
		creatingXandYs("3",filePathToExamples);
		//creatingXandYs("4");
		//creatingXandYs("5");
		//creatingXandYs("6");
		//creatingXandYs("7");
		//creatingXandYs("8");
		//creatingXandYs("9");
		//creatingXandYs("divide");
		//creatingXandYs("multiply");
		//creatingXandYs("pavla");
		//creatingXandYs("plus");
		
	}
	
	
	
	private int getclassNumber(String classToTrain)
	{
		switch (classToTrain) {
		case "0":
			return 0 ; 
		case "1":
			return 1 ; 
		case "2":
			return 2 ; 
		case "3":
			return 3 ; 
		case "4":
			return 4 ; 
		case "5":
			return 5 ; 
		case "6":
			return 6 ; 
		case "7":
			return 7 ; 
		case "8":
			return 8 ; 
		case "9":
			return 9 ; 
		case "multiply":
			return 10 ; 
		case "divide":
			return 11 ; 
		case "pavla":
			return 12 ; 
		case "plus":
			return 13 ; 
		default:
			return -1 ; 
		}
	}
	
	public static double[][] readMatFile(String fileName, String arrayName)
	{
		MatFileReader matfilereader;
		double[][] array =null ;
		try {
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
		createExampleFeatureAndClassesArraysZernike(filePathToExamples) ;
		exportTheMatrices(examplesClasses, examplesFeatures, name);
	}
	
	
}
