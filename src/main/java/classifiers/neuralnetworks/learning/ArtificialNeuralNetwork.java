package classifiers.neuralnetworks.learning;

import java.util.ArrayList;
import java.util.List;

import classifiers.neuralnetworks.utilities.*;

import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.Matrix;

import com.nikolis.trainingphase.MatFileGenerator;
import com.solveit.classifier.NeuralNetwork;

/**
 * A class That Implements Artificial Neural Networks 
 * @author Nikolaos Galerakis
 *
 */
public class ArtificialNeuralNetwork implements NeuralNetwork{
 
	/**
	 * The Matrix containing the Features for the training Set 
	 */
	private Matrix featureMatrix ; 
	/**
	 * The Matrix containing the number of the class 
	 * that the corresponding row of featureMatrix  represents
	 */
	private Matrix classeMatrix ;
	/**
	 * 
	 */
	private Matrix hipothesis ; 
	/**
	 * The activations 
	 */
	private Matrix[] allTheAs ; 
	/**
	 * The Capital Delta terms associated with Thetas  
	 */
	private	Matrix[] allTheDs ;
	/**
	 * The lower delta terms associated with activations 
	 */
	private Matrix[] alltheds ;
	/**
	 * Weights 
	 */
	private Matrix[] alltheThetas ; 
	/**
	 * Gradient's Computed by the Capital Delta terms assosiated with Thetas
	 */
	private Matrix[] allTheThetaGreds ; 
	/**
	 * Associated with Thetas and used to assist in Computing the Cost 
	 */
	private Matrix[] allTheRegTerms ; 
	/**
	 *  Output Of the layer used to Compute the activations 
	 */
	private Matrix[] allTheZs ; 
	/**
	 * An arrayList that contains as many elements as the number of the 
	 * NN layers and every element is the number of neuronos for the spesific layer
	 */
	private final  List<Integer> neuralNetworkArchitectures ; 
	
	/**
	 * @param neuralNetworkArchitectures
	 */
	public ArtificialNeuralNetwork(final List<Integer> neuralNetworkArchitectures) {
		allTheZs = new Matrix[neuralNetworkArchitectures.size()-1];
		allTheAs = new Matrix[neuralNetworkArchitectures.size()] ; 
		allTheDs = new Matrix[neuralNetworkArchitectures.size()-1] ; 
		alltheThetas= new Matrix[neuralNetworkArchitectures.size()-1] ;
		allTheThetaGreds = new Matrix[neuralNetworkArchitectures.size()-1] ;
		alltheds = new Matrix[neuralNetworkArchitectures.size()-1] ;
		allTheRegTerms = new Matrix[neuralNetworkArchitectures.size()-1] ; 
		this.neuralNetworkArchitectures=neuralNetworkArchitectures ; 
	}
	
	
	/**
	 * This Method is performing the feed forward  and calculates 
	 * the delta terms and the gradients for the Theta matrixes
	 * @param lambda
	 */
	private void backPropagation(final double lambda)
	{	
		allTheAs[0]=featureMatrix ;
		
		for(int i=1; i<allTheAs.length; i++)
		{
			allTheAs[i]= NeuralHelper.addBias(allTheAs[i-1]) ;
			allTheZs[i-1]=allTheAs[i].multiply(alltheThetas[i-1].transpose());
			allTheAs[i]=NeuralHelper.sigmoid(allTheZs[i-1]) ;
		}
		hipothesis=allTheAs[allTheAs.length-1] ;
		
		alltheds[alltheds.length-1] = allTheAs[allTheAs.length-1].subtract(classeMatrix) ;
		
		for(int i=alltheds.length-2; i>=0; i--)
		{
			final Matrix temperor = NeuralHelper.combineTwoMatrix(NeuralHelper.createOnesMatrix(allTheZs[i].rows(), 1),allTheZs[i]) ;
			final Matrix temp2 = alltheds[i+1].multiply(alltheThetas[i+1]) ;
			alltheds[i]= temp2.hadamardProduct(NeuralHelper.sigmoidGradient(temperor)) ; 
			alltheds[i]=NeuralHelper.returnAllRowsAndGivenCollumns(alltheds[i], 1) ;
		}
		
		for(int i=0; i<allTheRegTerms.length; i++)
		{
			allTheRegTerms[i] = NeuralHelper.combineTwoMatrix(NeuralHelper.createMatrixOfZeros(alltheThetas[i].rows(), 1), NeuralHelper.returnAllRowsAndGivenCollumns(alltheThetas[i], 1)).multiply(lambda/featureMatrix.rows()) ;
		}
		
		for(int i=0; i<allTheDs.length; i++)
		{
			allTheDs[i] =  alltheds[i].transpose() ;
			allTheDs[i] = allTheDs[i].multiply(NeuralHelper.addBias(allTheAs[i])) ; 
		}
		
		for(int i=0; i<allTheThetaGreds.length; i++)
		{
			allTheThetaGreds[i]=allTheDs[i].divide(featureMatrix.rows()).add(allTheRegTerms[i]) ;
		}
	}
	
	
	/**
	 * FeatureMatrix contains features and classMatrix contains
	 * the number of class that the corresponding row of featuresMatrix represents
	 * @param x
	 * @param y
	 */
	public void loadInputs(final double x[][],final double y[][])
	{			
			this.featureMatrix=new Basic2DMatrix(x);
			this.classeMatrix= new Basic2DMatrix(y);
	}
	
	
	/**
	 * 	The method to iteratively reduce the cost computed by the cost  
	 * @param numOfLabels
	 * @param alpha
	 * @param lambda
	 * @param numberOfClasses
	 */
	public void batchGradientDescemt(final double alpha, final double lambda, final int numOfIterations, final int numberOfClasses)
	{
		for(int i=0; i<alltheThetas.length; i++)
		{
			alltheThetas[i]=NeuralHelper.createsRanomsMatrix(this.neuralNetworkArchitectures.get(i+1), neuralNetworkArchitectures.get(i)+1) ; 
		}
	
		this.classeMatrix = NeuralHelper.convertClasseMatrix(numberOfClasses, classeMatrix);
		NeuralHelper.printMatrix(classeMatrix);
		for(int i=0; i<numOfIterations; i++)
		{
			backPropagation(lambda);
			for(int j=0; j<alltheThetas.length; j++)
			{
				alltheThetas[j] = alltheThetas[j].subtract(allTheThetaGreds[j].multiply(alpha).multiply(computeCost(hipothesis, lambda))) ; 
			}
		}
		final MatFileGenerator matfile = new MatFileGenerator("Thetas.mat") ;
		for(int i=0; i<alltheThetas.length; i++)
		{
			matfile.addArray(NeuralHelper.matrix2Array(alltheThetas[i]), "Theta"+i);
		}
		matfile.writeFile() ;

	}

		

	
	
	
	/**
	 * This is method is used to compute the regularisation
	 *  terms  that is going to be added to the gradient 
	 *  in order to help us control the problem of overfiting . 
	 * @param lambda
	 * @return
	 */
	/*double computeRegTerm(double lambda)
	{
		Matrix regTheta1 = new Basic2DMatrix(Theta1.rows(), Theta1.columns()-1);
		Matrix regTheta2 = new Basic2DMatrix(Theta2.rows(), Theta2.columns()-1);
		
		for(int i=0; i<Theta1.rows(); i++)
		{
			for(int j=1; j<Theta1.columns(); j++)
			{
				regTheta1.set(i, j-1, Theta1.get(i, j));
			}
		}
		
		for(int i=0; i<Theta2.rows(); i++)
		{
			for(int j=1; j<Theta2.columns(); j++)
			{
				regTheta2.set(i, j-1, Theta2.get(i, j));
			}
		}
		regTheta1= regTheta1.hadamardProduct(regTheta1);
		regTheta2= regTheta2.hadamardProduct(regTheta2);l
		double subRegTerm = regTheta1.sum()+regTheta2.sum(); 
		return (lambda/(2*featureMatrix.rows())) * subRegTerm ;
	}*/
	
	
	/**
	 * This method get as input the hipothesis and computes the cost 
	 * This is Our Optimization Objective.
	 * @param hipothesis
	 * @param lambda
	 * @return
	 */
	private double computeCost(final Matrix hipothesis,final double lambda)
	{
		final Matrix hipothesislogminus = new Basic2DMatrix(hipothesis.rows(), hipothesis.columns());
		final Matrix hipothesisLog = new Basic2DMatrix(hipothesis.rows(),hipothesis.columns()) ;
		for(int i=0; i<hipothesis.rows(); i++)
		{
			for(int j=0; j<hipothesis.columns(); j++)
			{
				hipothesisLog.set(i, j, Math.log(hipothesis.get(i, j)));
				hipothesislogminus.set(i, j, Math.log(1-hipothesis.get(i, j)));
			}
		}
		
		final Matrix ones = NeuralHelper.createOnesMatrix(classeMatrix.rows(), classeMatrix.columns()) ;
		Matrix temp = classeMatrix.multiply(-1);
		temp= temp.hadamardProduct(hipothesis) ; 
		final Matrix	temp2 = ones.subtract(classeMatrix).hadamardProduct(hipothesislogminus) ;
		temp= temp.subtract(temp2) ;
		
		return temp.sum()/featureMatrix.rows();//+computeRegTerm(lambda); 
	}
	
	
	
	
	
	public static  double[][]  predict(double Xin[][], ArrayList<Integer> theNeurons)
	{	
		Matrix[] alltheThetas= new Matrix[theNeurons.size()-1] ;
		Matrix[] allTheAs = new Matrix[theNeurons.size()] ; 
		Matrix[] allTheZs = new Matrix[theNeurons.size()-1];
		
		Matrix X = new Basic2DMatrix(Xin);
		allTheAs[0]=X ;
		
		for(int i=0; i<alltheThetas.length; i++)
		{
			alltheThetas[i]=new Basic2DMatrix(MatFileGenerator.readMatFile("Thetas.mat", "Theta"+i)) ;
		}
		
		for(int i=1; i<allTheAs.length; i++)
		{
			allTheAs[i]= NeuralHelper.addBias(allTheAs[i-1]) ;
			allTheZs[i-1]=allTheAs[i].multiply(alltheThetas[i-1].transpose());
			allTheAs[i]=NeuralHelper.sigmoid(allTheZs[i-1]) ;
		}
		
		return NeuralHelper.matrix2Array(allTheAs[allTheAs.length-1]) ; 
	}
	
	public double[][]  predict(double Xin[][])
	{	
		Matrix X = new Basic2DMatrix(Xin);
		allTheAs[0]=X ;
	
		for(int i=1; i<allTheAs.length; i++)
		{
			allTheAs[i]= NeuralHelper.addBias(allTheAs[i-1]) ;
			allTheZs[i-1]=allTheAs[i].multiply(alltheThetas[i-1].transpose());
			allTheAs[i]=NeuralHelper.sigmoid(allTheZs[i-1]) ;
		}
		return NeuralHelper.matrix2Array(allTheAs[allTheAs.length-1]) ; 
	}
	
}
