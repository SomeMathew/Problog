package edu.comp6591.problog.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * File manipulation methods
 */
public class FileHelper {
	
	/**
	 * Fetch the entire file content as a string
	 * @param path
	 * @return file content
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	public static String getFile(String path) throws FileNotFoundException, IOException {
		StringBuilder content = new StringBuilder();
        FileReader fr = new FileReader(path);
		
		try (BufferedReader br = new BufferedReader(fr))
		{
			String line = br.readLine();
			while (line != null)
			{
				content.append(line);
				line = br.readLine();
			}
		}
		
		return content.toString();
    }
}
