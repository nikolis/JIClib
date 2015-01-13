package classifiers.neuralnetworks.learning;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import classifiers.neuralnetworks.utilities.*;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;



/**
 * This class is designed to be an implementation of the neural networks algorithm with
 * one hidden layer one input layer and one output layer the number of units in every layer can varie
 * depending on the users needs . 
 * @author Nikolaos Galerakis
 *
 */
public class NeuralNetwork {

	Matrix Theta1 ; 
	Matrix Theta2 ; 
	Matrix X ; 
	Matrix Y ;
	Matrix Theta1_grad ;  
	Matrix Theta2_grad ; 
	Matrix hipothesis ; 
	
	
	/**
	 * A method that loads the parameters x,y the main input of the neural network 
	 * x containing the training example with their features and y the predefind class that 
	 * the training set entrys belong to 
	 * @param x
	 * @param y
	 */
	public void loadParameters(double x[][],double y[][])
	{
			this.X=new Basic2DMatrix(x);
			this.Y= new Basic2DMatrix(y);		
			NeuralHelper.printMatrix(this.Y);

	}
	/**
	 * Same method  as previous just adds the possibility of entering the Theta terms already trained 
	 * in order to give the implementation the possibility to later use advanced optimization methods.
	 * @param x
	 * @param y
	 * @param theta1
	 * @param theta2
	 */
	public void loadParameters(double x[][],double y[][],double theta1[][],double theta2[][])
	{
			this.X=new Basic2DMatrix(x);
			this.Y= new Basic2DMatrix(y);
			Theta1 = new Basic2DMatrix(theta1);
			Theta2 = new Basic2DMatrix(theta2);		
	}
	/**
	 * This is a method implementing the gradient Descent algorithm witch is an algorithm
	 * that iterates as many times as inputed in numOfIteration in order to reduce the cost j of our hipothesis 
	 *it simple does that by performing feedforward and the subtract the gradient computed from the error terms 
	 *multiplied by our learning rate alpha
	 * @param secondLayerUnits
	 * @param outputLayerUnits
	 * @param alpha
	 * @param lambda
	 * @param numOfIterations
	 */
	public void workingItOut(int secondLayerUnits,double alpha,double lambda,int numOfIterations,int numberOfLabels)
	{
		Theta1=NeuralHelper.createOnesMatrix(secondLayerUnits, X.columns()+1);
		Theta2=NeuralHelper.createOnesMatrix(numberOfLabels, secondLayerUnits+1);
		convertY(numberOfLabels);
		//Theta1=neutralOperations.createMatrixOfDoublesWithRandomValues(0.1, 1.5, secondLayerUnits, X.columns()+1);
		//Theta2=neutralOperations.createMatrixOfDoublesWithRandomValues(0.1, 1.5, outputLayerUnits, secondLayerUnits+1);
		for(int i=0; i<numOfIterations; i++)
		{
			feedForward(lambda);
			Theta1= Theta1.subtract(Theta1_grad.multiply(alpha).multiply(computeCost(hipothesis, lambda))) ;
			Theta2= Theta2.subtract(Theta2_grad.multiply(alpha).multiply(computeCost(hipothesis, lambda))) ;
			System.out.println(computeCost(hipothesis, lambda));
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter("Theta1.txt", "UTF-8");
			for(int i=0; i<Theta1.rows(); i++)
			{
				for(int j=0; j<Theta1.columns();j++)
				{
					writer.write(String.valueOf(Theta1.get(i, j))+",");
				}
				writer.write("\n");
			}
			
			writer.close();

			writer = new PrintWriter("Theta2.txt", "UTF-8");
			for(int i=0; i<Theta2.rows(); i++)
			{
				for(int j=0; j<Theta2.columns();j++)
				{
					writer.write(String.valueOf(Theta2.get(i, j))+",");
				}
				writer.write("\n");
			}
			writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public  void loadTRainedThetas(int secondLayerUnits, int numberOfLabels)
	{
		Theta1=NeuralHelper.createOnesMatrix(secondLayerUnits, X.columns()+1);
		Theta2=NeuralHelper.createOnesMatrix(numberOfLabels, secondLayerUnits+1);
		 try {
			BufferedReader br2 = new BufferedReader(new FileReader("Theta2.txt"));
			BufferedReader br = new BufferedReader(new FileReader("Theta1.txt"));
			for(int i=0; i<Theta1.rows(); i++)
			{
				String something = br.readLine();
			
				String Theta1rra[] = something.split(",");
				
				for(int j=0; j<Theta1rra.length; j++)
				{
					Theta1.set(i, j, Double.parseDouble(Theta1rra[j]));
				}
				
				
			}
			
			for(int i=0; i<Theta2.rows(); i++)
			{	
				String something2 = br2.readLine();
				String Theta2rra[] = something2.split(",");
				for(int j=0; j<Theta2rra.length; j++)
				{
					Theta2.set(i, j, Double.parseDouble(Theta2rra[j]));
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// NeuralHelper.printMatrix(Theta1);
		// NeuralHelper.printMatrix(Theta2);
	}
	
	
	public int  predict(double Xin[][])
	{
		Matrix X = new Basic2DMatrix(Xin);
		Matrix a1 = NeuralHelper.addBias(X);
		Matrix z2 = a1.multiply(Theta1.transpose()) ;
		Matrix a2 = NeuralHelper.sigmoid(z2);
		a2=NeuralHelper.addBias(a2);
		Matrix z3 = a2.multiply(Theta2.transpose()) ;
		Matrix a3 = NeuralHelper.sigmoid(z3);
		NeuralHelper.printMatrix(a3);
		int max=-1 ; 
		double maxValue = -1 ; 
		for(int i=0; i<a3.rows(); i++)
		{
			for(int j=0; j<a3.columns(); j++)
			{
				if(a3.get(i, j)>=maxValue)
				{
					maxValue=a3.get(i, j);
					max = j ;
				}
			}
		}	
		return max;
	}
	
	
	
	
	
	
	/**
	 * The y parameter is inputed as an array with rows equal to the number of examples and
	 * it has just one column containing the numberf of the class that they belong
	 * in order though to compare the y with the hipothesis output we need convert it to a
	 * matrix with the same rows but the number of columns should be the number of different classes
	 * and only the the number of the column corresponding to the class the example belongs to should be 1 
	 * and the others should just contain 0 .This is what this functon is doing . 
	 * @param numberOfLabels
	 */
	public void convertY(int numberOfLabels)
	{
		Matrix realY = new Basic2DMatrix(Y.rows(),numberOfLabels) ;
		for(int i=0; i<Y.rows(); i++)
		{	
			int num=(int)Y.get(i, 0) ;	
			realY.set(i, num, 1);
		}
		Y=realY ; 
	}
	/**
	 * This function is performing the feed forward  and calculates the delta terms and the gradients for the 
	 * Theta matrixes containing the weight that is going to be used in our hipothesis.
	 * 
	 * 
	 * @param lambda
	 */
	public void feedForward(double lambda)
	{		
		
		Matrix a1 = NeuralHelper.addBias(X);
		Matrix z2 = a1.multiply(Theta1.transpose()) ;
		Matrix a2 = NeuralHelper.sigmoid(z2);
		a2=NeuralHelper.addBias(a2);
		Matrix z3 = a2.multiply(Theta2.transpose()) ;
		Matrix a3 = NeuralHelper.sigmoid(z3);
		hipothesis=a3 ;
	
		Matrix delta3 = a3.subtract(Y) ;
		
		Matrix temp = NeuralHelper.combineTwoMatrix(NeuralHelper.createOnesMatrix(z2.rows(), 1),z2);
		Matrix delta2 = delta3.multiply(Theta2).hadamardProduct(NeuralHelper.sigmoidGradient(temp)) ;
		Matrix delta22 = NeuralHelper.returnAllRowsAndGivenCollumns(delta2, 1);
		Matrix regularization =NeuralHelper.combineTwoMatrix(NeuralHelper.createMatrixOfZeros(Theta1.rows(), 1), NeuralHelper.returnAllRowsAndGivenCollumns(Theta1, 1)).multiply(lambda/X.rows()) ;
		Matrix regularization2 =NeuralHelper.combineTwoMatrix(NeuralHelper.createMatrixOfZeros(Theta2.rows(), 1), NeuralHelper.returnAllRowsAndGivenCollumns(Theta2, 1)).multiply(lambda/X.rows()) ;
	
		Matrix Delta1 = delta22.transpose().multiply(a1) ;
		Matrix Delta2 = delta3.transpose().multiply(a2) ; 
		Theta1_grad=Delta1.divide(X.rows()).add(regularization) ; 
		Theta2_grad=Delta2.divide(X.rows()).add(regularization2);
	}
	/**
	 * This is method is used to compute the regularization terms  that is going to be added to the gradient in order
	 * to help us control the problem of overfiting . 
	 * @param lambda
	 * @return
	 */
	double computeRegTerm(double lambda)
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
		regTheta2= regTheta2.hadamardProduct(regTheta2);
		double subRegTerm = regTheta1.sum()+regTheta2.sum(); 
		return (lambda/(2*X.rows())) * subRegTerm ;
	}
	/**
	 * This method get as input the hipothesis and computes the cost at the given hipothesis
	 * note that the neural network learning target is just to reduce the value outputed 
	 * by this method .  
	 * @param hipothesis
	 * @param lambda
	 * @return
	 */
	public double computeCost(Matrix hipothesis,double lambda)
	{
		Matrix hipothesisLog = new Basic2DMatrix(hipothesis.rows(),hipothesis.columns()) ;
		for(int i=0; i<hipothesis.rows(); i++)
		{
			for(int j=0; j<hipothesis.columns(); j++)
			{
				hipothesisLog.set(i, j, Math.log(hipothesis.get(i, j)));
			}
		}
		
		Matrix hipothesislogminus = new Basic2DMatrix(hipothesis.rows(), hipothesis.columns());
		for(int i=0; i<hipothesis.rows(); i++)
		{
			for(int j=0; j<hipothesis.columns(); j++)
			{
				hipothesislogminus.set(i, j, Math.log(1-hipothesis.get(i, j)));
			}
		}
		
		Matrix ones = NeuralHelper.createOnesMatrix(Y.rows(), Y.columns()) ;
		
		Matrix temp = Y.multiply(-1);
		temp= temp.hadamardProduct(hipothesis) ; 
		Matrix	temp2 = ones.subtract(Y).hadamardProduct(hipothesislogminus) ;
		temp= temp.subtract(temp2) ;
		
		return (temp.sum()/X.rows())+computeRegTerm(lambda); 
	}
	
	
	public static void main(String args[])
	{
		
	}
	
}
