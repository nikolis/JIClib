package imageprocessing.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;

import imageprocessing.utilities.*;

public class Labeling {

	int subImageWidth = 300;
	int subImageHeight = 300;
	Stack<Integer[]> searchingStack;
	BufferedImage originalImage;
	ArrayList<Integer> pixelsGiven;
	Random random;

	BufferedImage returnImage;
	int[][] classes;

	public Labeling(BufferedImage originalImage) {
		searchingStack = new Stack<>();
		this.originalImage = originalImage;
		classes = new int[originalImage.getWidth()][originalImage.getHeight()];
		pixelsGiven = new ArrayList<>();
		random = new Random(255);
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				classes[i][j] = 0;
			}
		}
	}

	private boolean isBlack(int i, int j) {
		int red = new Color(originalImage.getRGB(i, j)).getRed();
		return red == 0;
	}

	private int putLabels() {
		int classs = 1;
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				if (isBlack(i, j) && classes[i][j] == 0) {
					classes[i][j] = ++classs;
					checkForNeibours(i, j, classs);
					while (!searchingStack.isEmpty()) {
						Integer[] coordinates = searchingStack.pop();
						checkForNeibours(coordinates[0], coordinates[1], classs);
					}
				}
			}
		}
		return (classs);
	}

	public void checkForNeibours(int i, int j, int taksiPragmaton) {
		if ((i - 1 >= 0) && (j - 1 >= 0) && classes[i - 1][j - 1] == 0) {
			if (isBlack(i - 1, j - 1)) {
				classes[i - 1][j - 1] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i - 1; // autoBoxing
				coordinates[1] = j - 1;// autoBoxing
				searchingStack.push(coordinates);
			}
		}

		if (j - 1 >= 0) {
			if (isBlack(i, j - 1) && classes[i][j - 1] == 0) {
				classes[i][j - 1] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i;
				coordinates[1] = j - 1;
				searchingStack.push(coordinates);
			}
		}

		if (i + 1 < originalImage.getWidth() && j - 1 >= 0 && classes[i + 1][j - 1] == 0) {
			if (isBlack(i + 1, j - 1) && classes[i + 1][j - 1] == 0) {
				classes[i + 1][j - 1] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i + 1;
				coordinates[1] = j - 1;
				searchingStack.push(coordinates);
			}
		}

		if (i + 1 < originalImage.getWidth() && classes[i + 1][j] == 0) {
			if (isBlack(i + 1, j) && classes[i + 1][j] == 0) {
				classes[i + 1][j] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i + 1;
				coordinates[1] = j;
				searchingStack.push(coordinates);
			}
		}

		if (i + 1 < originalImage.getWidth() && j + 1 < originalImage.getHeight() && classes[i + 1][j + 1] == 0) {
			if (isBlack(i + 1, j + 1) && classes[i + 1][j + 1] == 0) {
				classes[i + 1][j + 1] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i + 1;
				coordinates[1] = j + 1;
				searchingStack.push(coordinates);
			}

		}

		if (j + 1 < originalImage.getHeight() && classes[i][j + 1] == 0) {
			if (isBlack(i, j + 1) && classes[i][j + 1] == 0) {
				classes[i][j + 1] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i;
				coordinates[1] = j + 1;
				searchingStack.push(coordinates);
			}
		}

		if (i - 1 >= 0 && j + 1 < originalImage.getHeight() && classes[i - 1][j + 1] == 0) {
			if (isBlack(i - 1, j + 1) && classes[i - 1][j + 1] == 0) {
				classes[i - 1][j + 1] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i - 1;
				coordinates[1] = j + 1;
				searchingStack.push(coordinates);
			}
		}

		if (i - 1 >= 0 && classes[i - 1][j] == 0) {
			if (isBlack(i - 1, j) && classes[i - 1][j] == 0) {
				classes[i - 1][j] = taksiPragmaton;
				Integer[] coordinates = new Integer[2];
				coordinates[0] = i - 1;
				coordinates[1] = j;
				searchingStack.push(coordinates);
			}
		}
	}

	public int findOffsetWidth(int klass) {
		int halfObjectWidth = (findRightMostForClass(klass) - findLeftMostForClass(klass) + 1) / 2;
		int halfImageWidth = (originalImage.getWidth() / 2);
		int pointToStart = halfImageWidth - halfObjectWidth;
		int offSet = pointToStart - findLeftMostForClass(klass);

		return offSet;
	}

	private int findOffsetHeight(int klass) {
		int halfObjectHeight = (findBotMostForClass(klass) - findTopMostForClass(klass) + 1) / 2;
		int halfImageHeight = (originalImage.getHeight() / 2);
		int pointToStart = halfImageHeight - halfObjectHeight;
		int offSet = pointToStart - findTopMostForClass(klass);

		return offSet;
	}

	private void paintTheRegions(int klass, String tade, int numberOfExamples) {
		returnImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
		int newPixel;
		int alpha = new Color(originalImage.getRGB(0, 0)).getAlpha();
		newPixel = GeneralImagingOperations.colorToRGB(alpha, 0, 0, 0);
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				int alpha2 = new Color(originalImage.getRGB(0, 0)).getAlpha();
				int red = 255;
				int green = 255;
				int blue = 255;
				int newPixel2 = GeneralImagingOperations.colorToRGB(alpha2, red, green, blue);
				returnImage.setRGB(i, j, newPixel2);
			}
		}

		int offSetWidth = findOffsetWidth(klass);
		int offSetHeight = findOffsetHeight(klass);
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				if (classes[i][j] == klass) {
					returnImage.setRGB(i + offSetWidth, j + offSetHeight, newPixel);
				}
			}
		}

		returnImage = createImageFromKlass(returnImage, klass);
		try {
			ImageIO.write(returnImage, "jpg", new File("/home/nikolis/Pictures/" + klass + tade + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage createImageFromKlass(BufferedImage originalImage, int klass) {
		int rigtMost = findRightMostForClass(klass);
		int leftMost = findLeftMostForClass(klass);
		int topMost = findTopMostForClass(klass);
		int botMost = findBotMostForClass(klass);
		BufferedImage returnImage = new BufferedImage(rigtMost - leftMost, botMost - topMost, originalImage.getType());

		for (int j = topMost, retImageHeight = 0; j < botMost; j++, retImageHeight++) {
			for (int i = leftMost, retImWidth = 0; i < rigtMost; i++, retImWidth++) {
				if (classes[i][j] == klass) {
					returnImage.setRGB(retImWidth, retImageHeight, 0);
				} else {
					returnImage.setRGB(retImWidth, retImageHeight, -1);
				}
			}
		}
		return returnImage;
	}

	private int findRightMostForClass(int klass) {
		int max = 0;
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				if (classes[i][j] == klass && i > max) {
					max = i;
				}
			}
		}
		return max;
	}

	private int findLeftMostForClass(int klass) {
		int max = originalImage.getWidth();
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				if (classes[i][j] == klass && i < max) {
					max = i;
				}
			}
		}
		return max;
	}

	private int findTopMostForClass(int klass) {
		int max = originalImage.getHeight();
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				if (classes[i][j] == klass && j < max) {
					max = j;
				}
			}
		}
		return max;
	}

	private int findBotMostForClass(int klass) {
		int max = 0;
		for (int i = originalImage.getWidth() - 1; i >= 0; i--) {
			for (int j = originalImage.getHeight() - 1; j >= 0; j--) {
				if (classes[i][j] == klass && max < j) {
					max = j;
				}
			}
		}
		return max;
	}

	public static void mainMethod(BufferedImage image, String name) {
		image = ThresHolding.grayImage2Bin(image);
		Labeling labeling = new Labeling(image);
		int klasis = labeling.putLabels();
		for (int i = 2; i <= klasis; i++) {
			labeling.paintTheRegions(i, name);
		}
	}

	public static void sliceImage(BufferedImage image, String path, int numberOfExamplesPerKlass) {
	Labeling labeling = new Labeling(image);
		int klasis = labeling.putLabels();
		for (int i = 2; i <= klasis; i++) {
			labeling.paintTheRegions(i, "testStoLimit", numberOfExamplesPerKlass);
		}
	}
}
