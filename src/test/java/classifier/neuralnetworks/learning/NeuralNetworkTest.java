package classifier.neuralnetworks.learning;



import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

import classifiers.neuralnetworks.learning.NeuralNetwork;
/**
 * A Class designed to later contain the unit testing for Neural Networks 
 * at the moments is just demonstrating it's usage
 * @author Nikolaos Galerakis
 *
 */
public class NeuralNetworkTest {
	
	/*public static void main(String args[])
	{
		NeuralNetwork nncost = new NeuralNetwork();
		try {
			MatFileReader matfilereader = new MatFileReader("ex4weights.mat") ;
			MLDouble heta1 = (MLDouble)matfilereader.getMLArray("Theta1");
			MLDouble heta2 = (MLDouble)matfilereader.getMLArray("Theta2");
			MatFileReader matfilereader2 = new MatFileReader("ex4data1.mat") ;
			MLDouble heta = (MLDouble)matfilereader2.getMLArray("X");
			MLDouble yeta = (MLDouble)matfilereader2.getMLArray("y");

			double x[][] = heta.getArray() ; 
			double theta1[][]= heta1.getArray();
			double theta2[][]= heta2.getArray() ;
			double y[][]= yeta.getArray() ;
			nncost.loadParameters(x, y, theta1, theta2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		//nncost.loadParameters(x, y);
		nncost.workingItOut(25,10,0.001,1,500000000);
	}*/
}
