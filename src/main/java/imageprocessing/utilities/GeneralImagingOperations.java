package imageprocessing.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GeneralImagingOperations {
	/**
	 * This method creates an histogram of the parameter image 
	 * pixel intensities as an array of integers
	 * @param originalImage
	 * @return histogram 
	 */
	public static int[] imageHistogram(BufferedImage originalImage){
		int[] histogram   = new int[256] ;//0 to 255 the scale of the intensity levels 
		for(int i=0 ; i<originalImage.getWidth(); i++){
			for(int j=0 ; j<originalImage.getHeight(); j++){
				int intensity = new Color(originalImage.getRGB(i, j)).getRed();
				histogram[intensity] ++;
			}
		}
		return histogram ; 
	}
	
	public static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }
	
	public static void main(String[] args)
	{
		System.out.println((int) 8<<3);
	}
}
