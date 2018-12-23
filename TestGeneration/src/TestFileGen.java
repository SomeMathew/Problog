import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

/*
 * Files on the Shiri Sirs Test link of Github are  .pl files 
 * open the file desired file in raw and copy all data and 
 * paste into a text file and give it the same name with .txt
 * Save input file in root directory.
 * Output file is generated in same place.
 */
public class TestFileGen {
	public static void main(String args[])
	{
		
		try
		{
			String input="graph10000.txt";
			
			Scanner sc= new Scanner(System.in);
			System.out.println("Enter uncertainty value:");
			String uc= sc.nextLine();
			sc.close();
			String output=input.substring(0,input.length()-4)+"_"+uc+".txt";	
			
			String line = null;
			String uncertainty=" : "+uc+".";
			BufferedReader br = new BufferedReader(new FileReader(input));
			br.readLine(); // removes first line of test files
			BufferedWriter wr = new BufferedWriter(new FileWriter(output));
			
			while((line = br.readLine())!= null)
			{
			
				String out= line.substring(0,line.length()-1)+uncertainty;
				wr.write(out);
				wr.newLine();
			}
			System.out.println("File Generated: "+output);
		wr.close();	
		br.close();
		}
		catch (Exception e) {
			System.out.println(e);
			
		}
	}
}
