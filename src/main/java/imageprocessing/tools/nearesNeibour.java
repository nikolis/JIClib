package imageprocessing.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import imageprocessing.utilities.GeneralImagingOperations;

public class nearesNeibour {

	private static int[] resizePixels(int[] pixels, int w1, int h1, int w2, int h2) {
		int[] temp = new int[w2 * h2];
		double x_ratio = w1 / (double) w2;
		double y_ratio = h1 / (double) h2;
		double px, py;
		for (int i = 0; i < h2; i++) {
			for (int j = 0; j < w2; j++) {
				px = Math.floor(j * x_ratio);
				py = Math.floor(i * y_ratio);
				temp[(i * w2) + j] = pixels[(int) ((py * w1) + px)];
			}
		}
		return temp;
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int newHeight, int newWidth) {
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

		int[] pixelsBuffer = new int[originalImage.getHeight() * originalImage.getWidth()];

		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				pixelsBuffer[j * originalImage.getWidth() + i] = new Color(originalImage.getRGB(i, j)).getRed();
			}
		}

		pixelsBuffer = resizePixels(pixelsBuffer, originalImage.getWidth(), originalImage.getHeight(), newWidth,
				newHeight);
		int alpha = new Color(originalImage.getRGB(1, 1)).getAlpha();

		for (int i = 0; i < newWidth; i++) {
			for (int j = 0; j < newHeight; j++) {
				int newPixel = GeneralImagingOperations.colorToRGB(alpha, pixelsBuffer[j * newWidth + i],
						pixelsBuffer[j * newWidth + i], pixelsBuffer[j * newWidth + i]);
				newImage.setRGB(i, j, newPixel);
			}
		}

		return newImage;
	}

	public static void sizeScaleAllImagesInAdirectory(String pathToDirectory, int height, int width) {
		File folder = new File(pathToDirectory);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			BufferedImage inputImage = null;
			BufferedImage scaledImage = null;

			try {
				if (listOfFiles[i].isFile()) {
					System.out.println(listOfFiles[i]);
					inputImage = ImageIO.read(listOfFiles[i]);
					scaledImage = resizeImage(inputImage, width, height);
					ImageIO.write(scaledImage, "jpg", new File(listOfFiles[i].toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String args[]) {
		nearesNeibour.sizeScaleAllImagesInAdirectory("/home/nikolis/Desktop/pictures/twos", 300, 300);
	}

}