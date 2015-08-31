package basic;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import imageprocessing.tools.ColorToGray;
import imageprocessing.tools.Labeling;
import imageprocessing.tools.LineraFileters;
import imageprocessing.tools.ThresHolding;

import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class PreProccessWraper {

	public static void main(String args[]) throws Exception {
		
		
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
            	String klassImage= eElement.getTextContent() ;
            	BufferedImage originalImage = ImageIO.read(new File(klassImage)) ; 
				originalImage = ColorToGray.toGrayLM(originalImage) ;
				originalImage = ThresHolding.grayImage2Bin(originalImage) ;
				originalImage = LineraFileters.filterImage(originalImage) ;
				Labeling.sliceImage(originalImage, pathToOutPut, klassName);
            }
        }
        
		 
	}

}
