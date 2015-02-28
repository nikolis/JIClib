package classifiers.neuralnetworks.utilities;

import static org.junit.Assert.*;

import org.junit.Test;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

public class NeuralHelperTest {

	@Test
	public void tetCreateMatrixOfRandoms()
	{
		double min=-1 ;
		double max= 1 ;
		int rows = 3 ; 
		int columns = 3 ; 
		Matrix test = NeuralHelper.createMatrixOfDoublesWithRandomValues(min, max, rows, columns) ;
		Boolean  assertion =true ;	
		String numberFailed =null; 
			for(int i=0; i<rows; i++)
			{
				for(int j=0; j<columns; j++)
				{
					if(test.get(i, j)>max || test.get(i, j) <min)
					{
						assertion = false ; 
						numberFailed="Test failed with number: "+String.valueOf(test.get(i, j)) ;
					}
				}
			}
		assertTrue(numberFailed, assertion);
	}

	@Test
	public void testConvertClassMatrix()
	{
		Matrix mat = new Basic2DMatrix(6,1) ;
		for(int i=0; i<5; i++)
		{
			
			mat.set(i, 0, i);
			
		}
		mat = NeuralHelper.convertClasseMatrix(6, mat) ; 
		Boolean assertion = true; 
		
		for(int i=0; i<5; i++)
		{
			for(int j=0; j<5; j++)
			{
				if(mat.get(i, i)!=1)
					assertion=false ; 
						
			}
		}
		assertTrue(assertion);
	}
	@Test
	public void testAddBias()
	{
		
		assertTrue(true);
	}
}
