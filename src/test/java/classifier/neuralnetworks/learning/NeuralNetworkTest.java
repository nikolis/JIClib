package classifier.neuralnetworks.learning;



import java.util.ArrayList;


import classifiers.neuralnetworks.learning.ArtificialNeuralNetwork;

/**
 * A Class designed to later contain the unit testing for Neural Networks 
 * at the moments is just demonstrating it's usage
 * @author Nikolaos Galerakis
 *
 */
public class NeuralNetworkTest {
	
	public static void main(String[] args)
	{	
		final double x[][] = {{1,1}, {1,1}, {1,1}, {1,1}
		,{20,20}, {20,20}, {20,20}, {20,20},
		{40,40}, {40,40}, {40,40}, {40,40},
		{80,80}, {80,80}, {80,80}, {80,80},
		{120,120}, {120,120}, {120,120}, {120,120},
		{150,150}, {150,150}, {150,150}, {150,150},
		{180,180}, {180,180}, {180,180}, {180,180},
		{210,210}, {210,210}, {210,210}, {210,210}
		};

		final double y[][]={{0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {2}, {2}, {2}, {2}, {3}, {3}, {3}, {3}
		, {4}, {4}, {4}, {4} , {5}, {5}, {5}, {5}, {6}, {6}, {6}, {6}, {7}, {7}, {7}, {7}} ;
		
		final double x2[][] = {{1,1}, {20,20}, {40,40}, {80,80}, {120,120}, {150,150}, {180,180}, {210,210}} ;
		
		
		ArrayList<Integer> neuralNetworkNodes = new ArrayList<Integer>(); 
		neuralNetworkNodes.add(2) ; 
		neuralNetworkNodes.add(20) ; 
		neuralNetworkNodes.add(8) ; 
		
		ArtificialNeuralNetwork neuralNetwork = new ArtificialNeuralNetwork(neuralNetworkNodes) ;
		neuralNetwork.loadInputs(x, y);
		neuralNetwork.batchGradientDescemt(0.001, 0, 1000, 8);
		
		double[][] hypothesis = neuralNetwork.predict(x2) ;
		
		int[] results = new  int[hypothesis.length] ;
		
		for(int i=0; i<results.length; i++)
		{
			int max = -1 ; 
			double maxVal=-0.44 ; 
			
			for(int j=0; j<hypothesis[i].length; j++)
			{
				if(hypothesis[i][j]>=maxVal)
				{
					maxVal=hypothesis[i][j] ;
					max=j ; 
				}
			}
			results[i]=max ;
		}
		
	
	}
	
	
	
}
