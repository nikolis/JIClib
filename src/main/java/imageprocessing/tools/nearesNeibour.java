package imageprocessing.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import imageprocessing.utilities.GeneralImagingOperations;

public class nearesNeibour {

	
	
	public static int[] resizePixels(int[] pixels,int w1,int h1,int w2,int h2) {
	    int[] temp = new int[w2*h2] ;
	    double x_ratio = w1/(double)w2 ;
	    double y_ratio = h1/(double)h2 ;
	    double px, py ; 
	    for (int i=0;i<h2;i++) {
	        for (int j=0;j<w2;j++) {
	            px = Math.floor(j*x_ratio) ;
	            py = Math.floor(i*y_ratio) ;
	            temp[(i*w2)+j] = pixels[(int)((py*w1)+px)] ;
	        }
	    }
	    return temp ;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int newHeight, int newWidth) {
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType()) ;
		
		int[] pixelsBuffer  = new int[originalImage.getHeight()*originalImage.getWidth()] ;
		
		for(int i=0; i<originalImage.getWidth(); i++) {
			for(int j=0; j<originalImage.getHeight(); j++){
				pixelsBuffer[j*originalImage.getWidth()+ i] =  new Color(originalImage.getRGB(i, j)).getRed()  ;
			}
		}
		
		pixelsBuffer = resizePixels(pixelsBuffer, originalImage.getWidth(), originalImage.getHeight(), newWidth, newHeight) ; 
		int alpha = new Color(originalImage.getRGB(1, 1)).getAlpha();
		
		for(int i=0; i<newWidth; i++) {
			for(int j=0; j<newHeight; j++) {
				int newPixel = GeneralImagingOperations.colorToRGB(alpha, pixelsBuffer[j*newWidth+i], pixelsBuffer[j*newWidth+i], pixelsBuffer[j*newWidth+i]) ;		
				newImage.setRGB(i, j, newPixel);
			}
		}
		
		return newImage ; 
	}
	
	public static void main(String args[]) {
		BufferedImage image = null ;
		BufferedImage image2 = null ;
		BufferedImage endImage2 ; 
		BufferedImage endImage ; 
		try{
			image = ImageIO.read(new File("/home/nikolis/Pictures/3s2.jpg"));
			image2 = ImageIO.read(new File("/home/nikolis/Pictures/4s2.jpg"));
		}catch(Exception e){
			e.printStackTrace();
		}
		endImage = resizeImage(image, 300, 300) ; 
		endImage2= resizeImage(image2, 300, 300) ; 
		try{
			ImageIO.write(endImage,"jpg", new File("/home/nikolis/Pictures/4s22.jpg")) ;
			ImageIO.write(endImage2,"jpg", new File("/home/nikolis/Pictures/5s22.jpg")) ;
		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
	
}
