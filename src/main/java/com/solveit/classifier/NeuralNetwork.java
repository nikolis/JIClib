package com.solveit.classifier;

public interface NeuralNetwork {
	public void loadInputs(final double x[][],final double y[][]) ; 
	public double[][]  predict(double Xin[][]) ;
}
