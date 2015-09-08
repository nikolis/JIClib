package basic;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import classifiers.neuralnetworks.learning.ArtificialNeuralNetwork;
import classifiers.neuralnetworks.utilities.NeuralHelper;
import featureexctraction.ZernikeMoments;
import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.Labeling;
import imageprocessing.tools.LineraFileters;
import imageprocessing.tools.ThresHolding;
import imageprocessing.tools.nearesNeibour;

import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class PreProccessWraper {

	public static void main(String args[]) throws Exception {
		
		ArrayList<String> klasses = new ArrayList<>(); 
		String pathToConfigFile = args[0] ; 
		String pathToOutPut  ;
		File configFile = new File(pathToConfigFile) ;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(configFile);
        doc.getDocumentElement().normalize();
        
        Element basicElement = doc.getDocumentElement() ; 
        Element preProccess = (Element) basicElement.getElementsByTagName("proccess").item(0) ;
        System.out.println("Currently in phase : "+preProccess.getAttribute("name"));
        pathToOutPut = preProccess.getAttribute("path");
        
        NodeList nList = doc.getElementsByTagName("klass");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            	Element eElement = (Element) nNode;
            	String klassName = eElement.getAttribute("name");
            	klasses.add(klassName) ; 
            	String klassImage= eElement.getTextContent() ;
            	BufferedImage originalImage = ImageIO.read(new File(klassImage)) ; 
				originalImage = ColorToGray.toGrayLM(originalImage) ;
				originalImage = ThresHolding.grayImage2Bin(originalImage) ;
				originalImage = LineraFileters.filterImage(originalImage) ;
				Labeling.sliceImage(originalImage, pathToOutPut, klassName);
				nearesNeibour.sizeScaleAllImagesInAdirectory(pathToOutPut+"/"+klassName, 300, 300);
            }
        }
        
        int numberOfClasses = 3; 
        int featureMatrixSize=10; 
        
        double x[][] = new double[90][10];
        double y[][] = new double[90][1] ;
        int i=0 ;
        double yikos =0 ; 
        String previous = klasses.get(0); 
        for(String klassName : klasses){
        	
			String endPath = pathToConfigFile+klassName ;
			File folder = new File(endPath);
			File[] listOfFiles = folder.listFiles();
			if(!previous.equals(klassName))
        		yikos++ ;
			
	        for(int fil=0; fil<listOfFiles.length; fil++) {	
	      
	        	BufferedImage image = ImageIO.read(listOfFiles[fil]) ; 
	        	ZernikeMoments zer = new ZernikeMoments(image);
	        	ArrayList<Double> features = zer.mainProcces(5, 1);
	        	
	        	for(int j=0; j<featureMatrixSize; j++){
	        		x[i][j] = features.get(j); 
	        	}
	        	
	        	y[i][0]=yikos ; 
	        	i++ ;
	        	previous=klassName ;
	        }
        }
        
        ArrayList<Integer> neuralNetworkArch = new ArrayList<>();
        neuralNetworkArch.add(featureMatrixSize) ;
        neuralNetworkArch.add(20) ; 
        neuralNetworkArch.add(20) ;
        neuralNetworkArch.add(numberOfClasses) ;
        ArtificialNeuralNetwork neuralNetwork = new ArtificialNeuralNetwork(neuralNetworkArch) ;
        neuralNetwork.loadInputs(x, y);
		neuralNetwork.batchGradientDescemt(0.001, 0, 1000, 8);
        
		 
	}

}
