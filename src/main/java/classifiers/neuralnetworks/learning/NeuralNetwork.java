package classifiers.neuralnetworks.learning;




import java.io.IOException;




import classifiers.neuralnetworks.utilities.*;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import com.nikolis.trainingphase.TrainingNeuralNetworks;



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
	Matrix alreadyTrainedTheta1 ; 
	Matrix alreadyTrainedTheta2 ;
	static final  int numberOfFeatures =30   ; 
	static final int numberOfLabeles =14; 
	static final int secondLayerUnits =50 ; 
	
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
	public void batchGradientDescemt(int secondLayerUnits,double alpha,double lambda,int numOfIterations,int numberOfLabels)
	{
		//TODO CREATE RANDOM TO RPEVENT SYMETRY 
		Theta1=NeuralHelper.createsRanomsMatrix(NeuralNetwork.secondLayerUnits, NeuralNetwork.numberOfFeatures+1);
		Theta2=NeuralHelper.createsRanomsMatrix(NeuralNetwork.numberOfLabeles, NeuralNetwork.secondLayerUnits+1);
		convertY(numberOfLabels);
		System.out.println("The y isss  : ");
		NeuralHelper.printMatrix(Y);
		System.out.println("The y isss  : ");
		for(int i=0; i<numOfIterations; i++)
		{
			feedForward(lambda);
			Theta1= Theta1.subtract(Theta1_grad.multiply(alpha).multiply(computeCost(hipothesis, lambda))) ;
			Theta2= Theta2.subtract(Theta2_grad.multiply(alpha).multiply(computeCost(hipothesis, lambda))) ;
			System.out.println(computeCost(hipothesis, lambda)+" at the Iteration : "+i);
			System.out.println("The prediction is ");
		}
		try
		{
			NeuralHelper.writeMatrixToFile(Theta1,"Theta1.csv");
			NeuralHelper.writeMatrixToFile(Theta2,"Theta2.csv");
		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Loads the  Csv files written  from the last workingItOut method execution 
	 * as the csv is written by the last training process AKA execution of the workingItOut method
	 * the size of the alreadyTrainedTheta should be the same as the last time the neuralnetwork was trained
	 * that is why the layers size , numbers of features and number of output units(aka : number of classes)
	 * are saved as static variables and are being set only every time we train the neural network .  
	 */
	public  void loadTrainedThetas()
	{
		alreadyTrainedTheta1 = new Basic2DMatrix(NeuralNetwork.secondLayerUnits,NeuralNetwork.numberOfFeatures+1); 
		alreadyTrainedTheta2 = new Basic2DMatrix(NeuralNetwork.numberOfLabeles,NeuralNetwork.secondLayerUnits+1);
		try {
			alreadyTrainedTheta1=NeuralHelper.loadCsvFileInMatrix("Theta1.csv", alreadyTrainedTheta1.rows(), alreadyTrainedTheta1.columns()) ;
			alreadyTrainedTheta2=NeuralHelper.loadCsvFileInMatrix("Theta2.csv", alreadyTrainedTheta2.rows(), alreadyTrainedTheta2.columns()) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NeuralHelper.printMatrix(alreadyTrainedTheta1);
		NeuralHelper.printMatrix(alreadyTrainedTheta2);
	}
	
	
	
	public int  predict(double Xin[][])
	{
		Matrix X = new Basic2DMatrix(Xin);
		
		Matrix a1 = NeuralHelper.addBias(X);
		Matrix z2 = a1.multiply(alreadyTrainedTheta1.transpose()) ;
		Matrix a2 = NeuralHelper.sigmoid(z2);
		a2=NeuralHelper.addBias(a2);
		Matrix z3 = a2.multiply(alreadyTrainedTheta2.transpose()) ;
		Matrix a3 = NeuralHelper.sigmoid(z3);
		hipothesis=a3 ;
		NeuralHelper.printMatrix(hipothesis);
		double max =-1 ; 
		int maxPosition =-1 ; 
		for(int i=0; i<hipothesis.columns(); i++)
		{
			if(max<hipothesis.get(0, i))
			{
				max=hipothesis.get(0, i) ;
				maxPosition=i;
			}
		}
		return maxPosition ; 
	}
	
	public int  predict2(double Xin[][])
	{
		Matrix X = new Basic2DMatrix(Xin);
		
		Matrix a1 = NeuralHelper.addBias(X);
		Matrix z2 = a1.multiply(Theta1.transpose()) ;
		Matrix a2 = NeuralHelper.sigmoid(z2);
		a2=NeuralHelper.addBias(a2);
		Matrix z3 = a2.multiply(Theta2.transpose()) ;
		Matrix a3 = NeuralHelper.sigmoid(z3);
		hipothesis=a3 ;
		NeuralHelper.printMatrix(hipothesis);
		double max =-1 ; 
		int maxPosition =-1 ; 
		for(int i=0; i<hipothesis.columns(); i++)
		{
			if(max<hipothesis.get(0, i))
			{
				max=hipothesis.get(0, i) ;
				maxPosition=i;
			}
		}
		return maxPosition ; 
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

		NeuralNetwork nn = new NeuralNetwork() ;
		double theXis[][] = new double[3][3];
		theXis[0][0]=1.1652623 ; 
		theXis[0][1]=2.42552 ;
		theXis[0][2]=3.6532365 ; 
		
		theXis[1][0]=11.62363 ; 
		theXis[1][1]=12.6431136 ;
		theXis[1][2]=13.19841875 ; 
		
		theXis[2][0]=33.13656143 ; 
		theXis[2][1]=34.136616 ;
		theXis[2][2]=35.136634643 ; 
		
		double theYis[][] = new double[3][1] ;
		
		theYis[0][0]=0;
		theYis[1][0]=1;
		theYis[2][0]=2;
		 
		nn.loadParameters(theXis, theYis);
		nn.batchGradientDescemt(3, 0.01, 1, 100000, 3);
		
		double testX[][] = new double[1][3] ;
		testX[0][0]=11.62363 ; 
		testX[0][1]=12.6431136 ;
		testX[0][2]=13.19841875 ; 
		
		//nn.loadTrainedThetas();
		System.out.println(nn.predict2(testX)) ;
		
	}
	
}
