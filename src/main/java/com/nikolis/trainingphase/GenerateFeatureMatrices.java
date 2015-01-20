package com.nikolis.trainingphase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;


import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

import featureexctraction.ZernikeMoments;

public class GenerateFeatureMatrices {
	
	private ArrayList<ArrayList<Double>> examplesFeatures ;
	private ArrayList<Double> examplesClasses ;
	private static final int  numberOfMoments =10 ;
	private Collection<MLArray>  collectionMl ;
	
	public GenerateFeatureMatrices()
	{
		examplesFeatures =new ArrayList<ArrayList<Double>>(); 
		examplesClasses = new ArrayList<Double>() ;
		collectionMl = new ArrayList<MLArray>();
	}
	
	
	private void exportTheMatrices(ArrayList<Double> examplesClasses, ArrayList<ArrayList<Double>> examplesFeatures,String trainSetFileName)
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
	
	private double[][] convertArrayListToNormalArray(ArrayList<Double> arrayList)
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
	
	
	
	private void creatingXandYs(String classToTrain)
	{	
		ZernikeMoments zernMom ;
		File folder = new File("images/"+classToTrain);
		File[] listOfFiles = folder.listFiles();
		BufferedImage image ; 
		    for (int i = 0; i < listOfFiles.length; i++) 
		    {
		    	System.out.println(classToTrain);
		      if (listOfFiles[i].isFile()) 
		      {
		        try {
					image = ImageIO.read(listOfFiles[i]) ;
					zernMom = new ZernikeMoments(image);
					ArrayList<Double> moments = zernMom.mainProcces(numberOfMoments, 1);
					examplesFeatures.add(moments);
					examplesClasses.add((double)getclassNumber(classToTrain));
				} catch (IOException e) {
					e.printStackTrace();
				}
		      } 
		    }
	}
	
	
	private void generateCsvOfExamples()
	{
		creatingXandYs("0");
		creatingXandYs("1");
		creatingXandYs("2");
		creatingXandYs("3");
		creatingXandYs("4");
		creatingXandYs("5");
		creatingXandYs("6");
		creatingXandYs("7");
		creatingXandYs("8");
		creatingXandYs("9");
		creatingXandYs("divide");
		creatingXandYs("multiply");
		creatingXandYs("pavla");
		creatingXandYs("plus");
		
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
	
	public void exportTrainingSetMatrices(String name)
	{
		generateCsvOfExamples() ;
		exportTheMatrices(examplesClasses, examplesFeatures, name);
	}
	
	public static void main(String args[])
	{
		
	}
	
	
	
}
