/*CONTAINS ANALYZER MODULE METHODS
 * 	TEAM 9: Abdul Jasam, Colben Kharrl, Kevin Lough
 * 	
 * 	NOTE: The methods are made to be able to run independently,
 * 		  but if they are always run together (in the same order)
 * 		  the code could be greatly simplified.								 */

import java.io.*;
import java.util.*;
import java.util.regex.*;
	
public class AnalyzerModule 
{		
	//static List<String> lines = new ArrayList<String>();
	//System.out.println(Arrays.toString(list.toArray()));
	
	public static int lineCount(File in)								
	{
		int lc = 0;
		String line;
		
	    try
	    {
	        BufferedReader br = new BufferedReader(new FileReader(in));

	        while ((line = br.readLine()) != null)
	        {
	        	lc++;
	        }
	        br.close();
	    }
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    
		return lc;
	}
		
	public static int blankLineCount(File in)						
	{
		int blc = 0;
		String line;
		
	    try
	    {
	        BufferedReader br = new BufferedReader(new FileReader(in));
	        
	        while ((line = br.readLine()) != null)
	        {
	        	line = line.replaceAll("[ ,\n,\t]", "");
	        	
	        	if(line.equals(""))
	        		blc++;
	        }
	        br.close();
	    }
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    
		return blc;
	}
		
	public static int spaceCount(File in) 								
	{
		int sc = 0;
		String line;
		
	    try
	    {
	        BufferedReader br = new BufferedReader(new FileReader(in));
	        
	        while ((line = br.readLine()) != null)
	        {
	        	line = line.replaceAll("[^ ]", "");
	        	
	        	sc += line.length();
	        }
	        br.close();
	    }
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    
		return sc;
	}
	
	public static int wordCount(File in) 								
	{
		int wc = 0;
		String line;
		List<String> words;
		
	    try
	    {
	        BufferedReader br = new BufferedReader(new FileReader(in));
	        
	        while ((line = br.readLine()) != null)
	        {
	        	line = line.toLowerCase().replaceAll("[^a-z\\s\\d]", "");
	        	if(Pattern.matches("[\\s]", line) || line.isEmpty()){}
	        	
	        	else
	        	{
	        		words = Arrays.asList(line.split("\\s+"));
	        		
	        		wc += words.size();
	        	}
	        }
	        br.close();
	    }
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    
		return wc;
	}	
	
	public static int avgCharPerLine(File in) 								
	{
		int cc = 0, lc = 0;
		String line;
		
	    try
	    {
	        BufferedReader br = new BufferedReader(new FileReader(in));
	        
	        while ((line = br.readLine()) != null)
	        {
	        	line = line.replaceAll("[\\s]", "");
	        	
	        	if(line.isEmpty()){}
	        	
	        	else
	        	{
	        		cc += line.length();
	        	}
	        	
	        	lc++;
	        }
	        br.close();
	    }
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    
		return (int)((double) cc)/lc;
	}	
	
	public static int avgWordLength(File in) 								
	{
		return 0;
	}	
	
	public static int mostCommonWords(File in) 								
	{
		return 0;
	}	
}
