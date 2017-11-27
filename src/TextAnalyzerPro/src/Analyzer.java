/*CONTAINS ANALYZER MODULE METHODS
 * 	TEAM 9: Abdul Jasam, Colben Kharrl, Kevin Lough
 * 	
 * 	NOTE: This is a simplified version of the code that runs in a
 * 		  non-modular fashion.										 */

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.regex.*;
	
public class Analyzer 
{
	public static Record Analyze(File in)								
	{
		//FORWARD DECLARATIONS/VARIABLES
		Record r;
		int lineCnt = 0, blanklineCnt = 0, spaceCnt = 0, wordCnt = 0, charCnt = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0;;
		HashMap<String, Integer> stringOccurences = new HashMap<String, Integer>();
		String line, temp, ACL, ACW, w1 = null, w2 = null, w3 = null, w4 = null;
		String[] values = new String[4], words = null;
		
		//READ FILE CONTENTS/COUNT VALUES
	    try
	    {
	        BufferedReader br = new BufferedReader(new FileReader(in));

	        while ((line = br.readLine()) != null)
	        {
	        	lineCnt++;															//Increments Line Count
	        	
	        	temp = line.replaceAll("[\\s]", "");									//(removes whitespace)
	        	if (temp.equals(""))
	        		blanklineCnt++;													//Increments Blank Line Count
	        	
	        	temp = line.replaceAll("[^ ]", "");										//(only [_] characters)
	        	spaceCnt += temp.length();											//Increments Space Count
	        	
	        	temp = line.toLowerCase().replaceAll("[^a-z\\s\\d]", "");				//(whitespace, letters & numbers)
	        	if (!Pattern.matches("[\\s]", temp) && !temp.isEmpty())									//(ignores blank lines)
	        	{
	        		words = line.split("\\s+");
	        		wordCnt += words.length;										//Increments Word Count
	        	}
	        	
	        	temp = temp.replaceAll("[\\s]", "");									//(letters & numbers)
	        	charCnt += temp.length();											//Increment Character Count
	        	
	        	for (String word : words)
	        	{
	        		if (stringOccurences.containsKey(word))
	        			stringOccurences.put(word, stringOccurences.get(word)+1);	//Increments Occurence count for word
	        		else
	        			stringOccurences.put(word, 1);									//places a new word in the HashMap
	        	}
	        }
	        br.close();
	    }
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    
	    if (lineCnt != 0)
	    	ACL = Integer.toString((int)((double) charCnt)/lineCnt);
	    else
	    	ACL = "";
	    if (wordCnt != 0)	
	    	ACW = Integer.toString((int)((double) charCnt)/wordCnt);
	    else
	    	ACW = "";
	    
        Set<Entry<String, Integer>> text = stringOccurences.entrySet();
        for (Entry<String, Integer> string : text)
        {
            if (string.getValue() > c1)
            {
            	w4 = w3;
            	c4 = c3; 
            	w3 = w2;
            	c3 = c2;
            	w2 = w1;
            	c2 = c1;
                w1 = string.getKey();
                c1 = string.getValue();
            }
            else
            {
            	if (string.getValue() > c2)
                {
                	w4 = w3;
                	c4 = c3; 
                	w3 = w2;
                	c3 = c2;
                	w2 = string.getKey();
                	c2 = string.getValue();
                }
            	else
                {
                	if (string.getValue() > c3)
                    {
                    	w4 = w3;
                    	c4 = c3; 
                    	w3 = string.getKey();
                    	c3 = string.getValue();
                    }
                	else
                    {
                    	if (string.getValue() > c4)
                        {
                        	w4 = string.getKey();
                        	c4 = string.getValue();
                        }
                    }
                }
            }
        }
	    
	    values[0] = w1;
	    values[1] = w2;
	    values[2] = w3;
	    values[3] = w4;
	    
	    //	initialize record
	    
	    r = new Record(in.getPath(), Integer.toString(lineCnt), Integer.toString(blanklineCnt), Integer.toString(spaceCnt), Integer.toString(wordCnt), ACL, ACW, values);
	    
	    //RETURN STRING[] CONTAINING VALUES
		return r;	
	}
	
	public static Vector<String> getAverageStats(Vector<Record> records) {
		Vector<String> ret = new Vector<String>();
		
		//	TODO build average stats
		
		return ret;
	}
}
