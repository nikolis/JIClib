package imageprocessing.utilities;

import java.awt.image.BufferedImage;
import java.util.List;


public class MathematicalOperations {
	
	/**
	 * This method is used to simple calculate the 
	 * sum of an array of numbers !
	 * @param numb
	 * @return sum 
	 */
	public static double findSum(List <? extends Number> list){
		int sum =0 ; 
		for(Number number : list){
			sum+=number.doubleValue() ;  
		}
		return sum ; 
	}
	
	public static BufferedImage convolve(BufferedImage originalImage , double[][] kernel){
		BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType()) ; 
		for(int i=0 ; i<originalImage.getWidth() ; i++){
			for(int j=0 ; j<originalImage.getHeight() ; j++){
				if((i+kernel[1].length -1< originalImage.getWidth()) && (j+kernel.length-1<originalImage.getHeight())  ){
					
				}else{
					
				}
			}
		}
		
	
		return newImage ; 
	}
	
	/**
	 * This method is used to calculate the sum of weights considering that 
	 * The the weights  for the specific number is the position that he appears
	 * in the list.This happens because every value is the intensity and the position
	 * that it appears in the list is the number of pixels having that intensity
	 * 
	 * @param number
	 * @return
	 */
	public static int calculateWeightsSum(int number){
		int weight=0 ; 
		for(int i=0 ; i<number ; i++){
			weight+=i ; 
		}
		return weight/number ; 
	}
	
	/**
	 * method that calculates the weighted Mean of a set of pixel intensities 
	 * and the corresponding number of pixels found with that intensity in the
	 * gray original image. the link between this numbers is explained in the above
	 * method description
	 * should be given as an array  of  Numbers . 
	 * @param numb
	 * @return doubles
	 * @throws Exception 
	 */
	public static double calculateWeightedMean(int thresgold , int[] weights, boolean isBack){ 
		double numerator = 0 ; 
		double denominator = 0 ; 
		int bonus ; 
		
		if(!isBack){
			bonus=thresgold ; 
		}else{
			bonus=0 ; 
		}
		for(int i=0 ; i<weights.length; i++){
			numerator+=i+bonus*weights[i];
			denominator+=weights[i] ;
		}
		
		
		return numerator/denominator  ; 
	}
	
	
	
	public static double calculateVariance(int threshold , int[] weights, boolean isBack){
		//double variance = 0 ; 
		double numerator = 0 ; 
		double denominator = 0 ; 
		int bonus  ; 
		if(!isBack){
			bonus=threshold ; 
		}else{
			bonus=0 ; 
		}
		double weightedMean = calculateWeightedMean(threshold, weights, isBack) ; 
		for(int i=0 ; i<weights.length; i++){
			denominator+=weights[i] ;
			numerator+= Math.pow(((bonus+i)-weightedMean), 2)*weights[i] ;
		}
		return numerator/denominator  ; 
	}
}
