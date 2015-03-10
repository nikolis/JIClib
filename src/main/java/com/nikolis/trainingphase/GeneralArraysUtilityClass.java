package com.nikolis.trainingphase;

import java.util.ArrayList;

	public class GeneralArraysUtilityClass {
	
		static double[][] convertArrayListToNormalArray(ArrayList<Double> arrayList)
		{
			double[][] normalArray = new double[arrayList.size()][1] ; 
			
			for(int i=0; i<arrayList.size(); i++)
			{
				normalArray[i][0]=arrayList.get(i); 
			}
			
			return normalArray ;
		}
	
	public static double[][] convertArrayListToNormalArray2(ArrayList<ArrayList<Double>> arrayList)
	{
		double[][] normalArray = new double[arrayList.size()][arrayList.get(0).size()] ; 
		for(int i=0; i<arrayList.size(); i++)
		{
			ArrayList<Double> temp = arrayList.get(i) ;
			for(int j=0; j<arrayList.get(0).size(); j++)
			{
				normalArray[i][j] = temp.get(j);
			}
		}
		return normalArray ;
	}
	
	
}
