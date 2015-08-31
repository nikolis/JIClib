package imageprocessing.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;

public class PreProccessWraper {

	public static void main(String args[]) throws Exception {
		String usage = "The parameters are not correct";
		BufferedImage originalImage = null ; 
		String originalImagePath = null ;
		
		if (args.length < 1 ) {
			System.err.print(usage);
			return;
		}
		String trainingSetImagesFilePath = args[0];
		File folder = new File(trainingSetImagesFilePath);
		File[] listOfFiles = folder.listFiles();
		try {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					System.out.println("File: " + listOfFiles[i].getName()+" is being Proccessed");
					originalImagePath=trainingSetImagesFilePath+listOfFiles[i].getName() ;
					originalImage = ImageIO.read(new File(originalImagePath)) ; 
					originalImage = ColorToGray.toGrayLM(originalImage) ;
					originalImage = ThresHolding.grayImage2Bin(originalImage) ;
					originalImage = LineraFileters.filterImage(originalImage) ;
					originalImage = Labeling.
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		 
		

	}

}
