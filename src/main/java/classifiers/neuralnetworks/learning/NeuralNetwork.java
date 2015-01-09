package classifiers.neuralnetworks.learning;



import java.io.IOException;

import classifiers.neuralnetworks.utilities.*;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic1DMatrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

public class NeuralNetwork {

	Matrix Theta1 ; 
	Matrix Theta2 ; 
	Matrix X ; 
	Matrix Y ;
	Matrix Theta1_grad ;  
	Matrix Theta2_grad ; 
	Matrix hipothesis ; 
	
	
	
	public void loadOnlyX()
	{
		MatFileReader matfilereader;
		try {
			matfilereader = new MatFileReader("ex4data1.mat");
			MLDouble heta = (MLDouble)matfilereader.getMLArray("X");
			MLDouble yeta = (MLDouble)matfilereader.getMLArray("y");
			double x[][] = heta.getArray() ; 
			double y[][]= yeta.getArray() ;
			this.X=new Basic2DMatrix(x);
			this.Y= new Basic1DMatrix(y);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void workingItOut(int secondLayerUnits,int outputLayerUnits,double alpha,double lambda,int numOfIterations)
	{
		loadOnlyX();
		Theta1=NeuralHelper.createOnesMatrix(secondLayerUnits, X.columns()+1);
		Theta2=NeuralHelper.createOnesMatrix(outputLayerUnits, secondLayerUnits+1);
		convertY(10);
		//Theta1=neutralOperations.createMatrixOfDoublesWithRandomValues(0.1, 1.5, secondLayerUnits, X.columns()+1);
		//Theta2=neutralOperations.createMatrixOfDoublesWithRandomValues(0.1, 1.5, outputLayerUnits, secondLayerUnits+1);
		for(int i=0; i<numOfIterations; i++)
		{
			
			feedForward(lambda);
			Theta1= Theta1.subtract(Theta1_grad.multiply(alpha).multiply(computeCost(hipothesis, lambda))) ;
			Theta2= Theta2.subtract(Theta2_grad.multiply(alpha).multiply(computeCost(hipothesis, lambda))) ;
			System.out.println(computeCost(hipothesis, lambda));
		}
		
	}
	
	public void loadParameters()
	{
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
			Theta1 = new Basic2DMatrix(theta1);
			Theta2 = new Basic2DMatrix(theta2);
			this.X=new Basic2DMatrix(x);
			//printMatrix(X);
			this.Y= new Basic1DMatrix(y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void convertY(int numberOfLabels)
	{
		Matrix realY = new Basic2DMatrix(Y.rows(),numberOfLabels) ;
		for(int i=0; i<Y.rows(); i++)
		{	
			int num=(int)Y.get(i, 0)-1 ;	
			realY.set(i, (int)(num), 1);
		}
		Y=realY ; 
	}
	
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
		//System.out.println(hipothesis);
		temp= temp.subtract(temp2) ;
		
		return (temp.sum()/X.rows())+computeRegTerm(lambda); 
	}
	
	public void printMatrix(Matrix mat)
	{
		for(int i=0 ; i<mat.rows(); i++)
		{
			for(int j=0 ; j<mat.columns(); j++)
			{
				System.out.print(mat.get(i, j)+ " ");
			} 
			System.out.println();
		}
	}
	
	public static void main(String args[])
	{
		/*PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		NeuralNetwork nncost = new NeuralNetwork();
		nncost.loadParameters();
		nncost.workingItOut(25,10,0.001,1.0,500000000);
	}
	
}
