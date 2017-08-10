package com.innoteva;

import java.io.*;
import java.util.Random;
import java.util.regex.*;

public class MungeSentenceswithEmptyLines {


    public static void main(String[] args) throws Exception {
	File inDir = new File("/home/samuel/Downloads/leipzig/unpacked300Sample"); //Folder where we thave the data
	File outDir = new File("/home/samuel/Downloads/leipzig/munged300KSampleToEvalSentence"); //Folder where we have the ouput of this data
	String[] languageDirNames = inDir.list();
	for (int i = 0; i < languageDirNames.length; ++i) {
	     if (languageDirNames[i].startsWith(".")) {
		continue;
	    }
	    Pattern pattern = Pattern.compile("[a-zA-Z]+");
	    Matcher matcher = pattern.matcher(languageDirNames[i]);
	    matcher.find();
	    String language = matcher.group();
	    File langDir = new File(inDir,languageDirNames[i]);
	    //String charset = extractCharset(langDir);

	    //String charset = "iso-8859-1";
	    String charset = "UTF-8";
	    transcode(language,langDir,charset,outDir);
	}
    }

    static void transcode(String language, File langDir, String charset, 
			  File outDir) 
	throws IOException {

	File inFile = new File(langDir,"sentences.txt");
	FileInputStream fileIn = new FileInputStream(inFile);
	InputStreamReader isReader = new InputStreamReader(fileIn,charset);
	BufferedReader bufReader = new BufferedReader(isReader);
	
	File langOutDir = new File(outDir,language);
	langOutDir.mkdir();
	File outFile = new File(langOutDir,language + ".txt");
	FileOutputStream fileOut = new FileOutputStream(outFile);
	OutputStreamWriter osWriter = new OutputStreamWriter(fileOut,"UTF-8");
	BufferedWriter bufWriter = new BufferedWriter(osWriter);


	System.out.println("\n" + language);
	System.out.println("reading from=" + inFile + " charset=" + charset);
	System.out.println("writing to=" + outFile + " charset=utf-8");
	
	long totalLength = 0L;
	String line;
	int count, emptyLine;
	emptyLine = 0;
	count = 0;
	Random r = new Random();
	int Low = 5;
	int High = 17;
	while ((line = bufReader.readLine()) != null) {
	    if (line.length() == 0) continue;
	    int index = line.indexOf("\t");
	    String newline = line.substring(index+1,line.length());
	    totalLength += newline.length(); 
		if(count == emptyLine){
			if(emptyLine == 0){
				bufWriter.write(newline);
				bufWriter.newLine();
				emptyLine = r.nextInt(High-Low) + Low;
				count = 0;	
			} else {
				bufWriter.newLine();
				emptyLine = r.nextInt(High-Low) + Low;
				count = 0;
			}
		} else {
		    
		    // System.out.println("line=" + line);
		    // System.out.println("New line=" + newline);
		     
		    bufWriter.write(newline);
		    bufWriter.newLine();
		    //bufWriter.write(" ");            //This is for the model in Lingpipe
		    count++;
		}
	}
	System.out.println("total length=" + totalLength);
	
	bufWriter.close();
	bufReader.close();
    }

}