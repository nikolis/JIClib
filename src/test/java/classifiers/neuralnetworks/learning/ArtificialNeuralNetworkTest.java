package classifiers.neuralnetworks.learning;

import static org.junit.Assert.*;


import java.util.ArrayList;

import org.junit.Test;
import org.la4j.matrix.dense.Basic2DMatrix;

import general.utilities.MatFileGenerator;

public class ArtificialNeuralNetworkTest {

	
	
	
	//@Test
	/**
	 * This method tests the predict method Using
	 * a tested implementation written in octave
	 */
	public void testPredictFunctionAndCost() {
		       
		ArrayList<Integer> neuralArch = new ArrayList<Integer>() ; 
		neuralArch.add(400) ;
		neuralArch.add(25) ;
		neuralArch.add(10) ;
		ArtificialNeuralNetwork artif = new ArtificialNeuralNetwork(neuralArch) ;
		artif.loadPreTraindeThetas("ex3weights.mat");
		double[][] X =MatFileGenerator.readMatFile("ex3data1.mat", "X") ;
		double[][] y =MatFileGenerator.readMatFile("ex3data1.mat", "y") ;

		y = convert1indexArrayTo0indexArray(y)  ; 
		
		artif.loadInputs(X,y,10);
		artif.predict(X);
		double[][] result =  artif.predict(X);
		
		Boolean assertion = true ; 
	
		double[][] resultFromClass =MatFileGenerator.readMatFile("result.mat", "a3") ;
		
		for(int i=0; i<result.length; i++)
		{
			for(int j=0; j<result[0].length; j++)
			{
				if(String.format("%.5f",resultFromClass[i][j]).replace("\n", "").equals(String.format("%.5f", result[i][j]).replace("\n", ""))==false)
				{
					assertion=false ; 
				}
			}
		}
		double cost = artif.computeCost(new Basic2DMatrix(result), 0) ;
		if(!String.format("%.6f", cost).equals(String.format("%.6f", 0.287629)))
				assertion=false ; 
		
		double costWithReg = artif.computeCost(new Basic2DMatrix(result), 1) ;
		if(!String.format("%.6f", costWithReg).equals(String.format("%.6f", 0.38377)))
			assertion=false ;
		
		artif.backPropagation(1);
		
		assertTrue(assertion);
		
	}
	
	private double[][] convert1indexArrayTo0indexArray(double[][] array)
	{
		for(int i=0; i<array.length; i++)
		{
			for(int j=0; j<array[0].length; j++)
			{
				
					array[i][j]=array[i][j]-1 ; 
			}
		}
		return array ; 
	}
	
	public static  double[][] convert1indexArrayTo0indexArray2(double[][] array)
	{
		for(int i=0; i<array.length; i++)
		{
			double temp = array[i][9] ; 
			array[i][9] =array[i][8] ;
			array[i][8] = array[i][7] ;
			array[i][7] = array[i][6] ;
			array[i][6] = array[i][5] ;
			array[i][5] =array[i][4] ;
			array[i][4] = array[i][3] ;
			array[i][3] = array[i][2]  ;
			array[i][2] = array[i][1] ;
			array[i][0] = temp ;
		}
		return array ; 
	}
	

}
