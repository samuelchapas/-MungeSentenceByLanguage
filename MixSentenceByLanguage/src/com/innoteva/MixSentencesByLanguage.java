package com.innoteva;

import java.io.*;
import java.util.regex.*;

public class MixSentencesByLanguage {
	static File inDir = new File("/home/samuel/Downloads/leipzig/unpacked"); //Folder where we thave the data
	static boolean keepGoing = false;
	
    public static void main(String[] args) throws Exception {
	
		File outDir = new File("/home/samuel/Downloads/leipzig/munged(LanguageId)/mixed"); //Folder where we have the ouput of this data
		String[] languageDirNames = inDir.list();
		
		transcode(languageDirNames, outDir);
    }

    static void transcode(String[] languageDirNames, File outDir) throws IOException {
    	
    	long totalLength = 0L;
    	BufferedReader[] bufReader = new BufferedReader[2];
    	String[] language = new String[2];
    	
    	File langOutDir = new File(outDir.getAbsolutePath());
    	langOutDir.mkdir();
    	File outFile = new File(langOutDir, "mixed.txt");
    	FileOutputStream fileOut = new FileOutputStream(outFile);
    	OutputStreamWriter osWriter = new OutputStreamWriter(fileOut,"UTF-8");
    	BufferedWriter bufWriter = new BufferedWriter(osWriter);
    	
    	for (int i = 0; i < languageDirNames.length; ++i) {
		     if (languageDirNames[i].startsWith(".")) {
		    	 continue;
		    }
		    Pattern pattern = Pattern.compile("[a-zA-Z]+");
		    Matcher matcher = pattern.matcher(languageDirNames[i]);
		    matcher.find();
		    language[i] = matcher.group();
		    File langDir = new File(inDir,languageDirNames[i]);
		    //String charset = extractCharset(langDir);
	
		    //String charset = "iso-8859-1";
		    String charset = "UTF-8";
		    File inFile = new File(langDir,"sentences.txt");
			FileInputStream fileIn = new FileInputStream(inFile);
			InputStreamReader isReader = new InputStreamReader(fileIn,charset);
			 bufReader[i] = new BufferedReader(isReader);
			 keepGoing = true;
    	}
    
    while(keepGoing){
    	for (int i = 0; i < bufReader.length; ++i) {
			
			String line;
			if ((line = bufReader[i].readLine()) != null) {
			    if (line.length() == 0) continue;
			    int index = line.indexOf("\t");
			    String newline = line.substring(index+1,line.length());
			    // System.out.println("line=" + line);
			    // System.out.println("New line=" + newline);
			    totalLength += newline.length();  
			    bufWriter.write(language[i] + "\t" + newline);
			    bufWriter.newLine();            //This is for the model in Lingpipe
			    keepGoing = true;
			} else {
				if(i == 0){
					keepGoing = true;
					bufReader[i].close();
				} else {
					keepGoing = false;
					bufReader[i].close();
				}
			}
		}
    }
	//System.out.println("\n" + language);
	//System.out.println("reading from=" + inFile + " charset=" + charset);
	//System.out.println("writing to=" + outFile + " charset=utf-8");
	
	System.out.println("Completed! total length=" + totalLength);
	
	bufWriter.close();
	
    }

}