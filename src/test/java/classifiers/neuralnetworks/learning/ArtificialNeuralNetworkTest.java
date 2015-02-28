package classifiers.neuralnetworks.learning;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.nikolis.trainingphase.MatFileGenerator;

public class ArtificialNeuralNetworkTest {

	
	
	
	@Test
	/**
	 * This method tests the predict method Using
	 * a tested implementation writen in octave
	 */
	public void testPredictFunction() {
		ArrayList<Integer> neuralArch = new ArrayList<Integer>() ; 
		neuralArch.add(400) ;
		neuralArch.add(25) ;
		neuralArch.add(10) ;
		ArtificialNeuralNetwork artif = new ArtificialNeuralNetwork(neuralArch) ;
		artif.loadPreTraindeThetas("ex3weights.mat");
		double[][] X =MatFileGenerator.readMatFile("ex3data1.mat", "X") ;
		double[][] y =MatFileGenerator.readMatFile("ex3data1.mat", "y") ;
		artif.loadInputs(X, y);
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
					System.out.println(String.format("%.5f", result[i][j]).replace("\n", ""));
					System.out.println(String.format("%.5f",resultFromClass[i][j]));
					assertion=false ; 
				}
				
			}
		}
		
		assertTrue(assertion);
	}

}
