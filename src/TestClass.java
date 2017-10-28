import java.io.*;

public class TestClass {

	public static void main(String[] args)
	{
		File text = new File("C:\\Users\\Kevin\\Documents\\Workspace\\TextAnalyzer\\src\\input4.txt");
		
		System.out.println(AnalyzerModule.lineCount(text));
		System.out.println(AnalyzerModule.blankLineCount(text));
		System.out.println(AnalyzerModule.spaceCount(text));
		System.out.println(AnalyzerModule.wordCount(text));
		System.out.println(AnalyzerModule.avgCharPerLine(text));
	}
}
