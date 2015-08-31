package general.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

/**
 * @author Nikolis The only reason for creating separate class for this is that
 *         we need to store also the Thetas in different classes and If we don't
 *         know in advance the number of Thetas arrays because we don't know in
 *         advance the number of layers in a neuralNetwork
 */
public class MatFileGenerator {

	Collection<MLArray> collectionMl;
	String fileName;

	public MatFileGenerator(String fileName) {
		collectionMl = new ArrayList<MLArray>();
		this.fileName = fileName;
	}

	public void addArray(double[][] array, String arrayName) {
		MLArray mlArray = new MLDouble(arrayName, array);
		collectionMl.add(mlArray);
	}

	public void writeFile() {
		try {
			MatFileWriter matWriter = new MatFileWriter();
			matWriter.write(fileName, collectionMl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static double[][] readMatFile(String fileName, String arrayName) {
		MatFileReader matfilereader;
		double[][] array = null;
		try {
			matfilereader = new MatFileReader(fileName);
			MLDouble heta = (MLDouble) matfilereader.getMLArray(arrayName);

			array = heta.getArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}
}
