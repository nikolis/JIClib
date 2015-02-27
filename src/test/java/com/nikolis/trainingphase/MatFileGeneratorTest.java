package com.nikolis.trainingphase;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatFileGeneratorTest {

	@Test
	public void test()
	{
		boolean equals = true ; 
		MatFileGenerator matFileGen = new MatFileGenerator("test.mat");
		double[][] array1 = {{1,2,3},{4,5,6},{7,8,9}} ;
		double[][] array2 = {{1},{2},{3},{4},{5},{6}} ; 
		matFileGen.addArray(array1, "array1");
		matFileGen.addArray(array2, "array2");
		matFileGen.writeFile();
		
		double[][] array1Imp = MatFileGenerator.readMatFile("test.mat", "array1") ;
		double[][] array2Imp = MatFileGenerator.readMatFile("test.mat", "array2") ;
		
		for(int i=0; i<array1.length; i++)
		{
			for(int j1=0; j1<array1[0].length; j1++)
			{
				if(array1[i][j1]!=array1Imp[i][j1])
				{
					equals = false ; 
				}
			}
			for(int j2=0; j2<array2[0].length; j2++)
			{
				if(array2[i][j2]!=array2Imp[i][j2])
				{
					equals = false ; 
				}
			}
		}
		assertTrue(equals);
	}

}
