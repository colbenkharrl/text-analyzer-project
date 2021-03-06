import java.io.*;
import java.util.*;

public class RecordList {

	public Vector<Record> records;
	private String file;
	
	public RecordList(String fileName) {
				
		file = fileName;
		records = new Vector<Record>();
		parseFile(file);
	}
	
	private void parseFile(String fileName) {
		
		InputStream fileIs = null;
        ObjectInputStream objIs = null;
        Record r;
        try {
            fileIs = new FileInputStream(fileName);
            objIs = new ObjectInputStream(fileIs);
            
            try {
            	while (true) {
            		r = (Record) objIs.readObject();
            		addRecord(r);
            	}
            } catch (EOFException e) {
            	objIs.close();
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            try {
                if(objIs != null) objIs.close();
            } catch (Exception ex){
                 
            }
        }
	}
	
	public void addRecord(Record r) {
		records.add(r);
	}
	
	public void empty() {
		records.removeAllElements();
		writeFile();
	}
	
	public void writeFile() {
		OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
            ops = new FileOutputStream(file);
            objOps = new ObjectOutputStream(ops);
            
            for (int i = 0; i < records.size(); i++) {
            	objOps.writeObject(records.get(i));
                //objOps.flush();
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                objOps.close();
            } catch (Exception ex){
                 
            }
        }
	}
}
