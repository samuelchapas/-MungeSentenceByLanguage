package innoteva.com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceIdentificator {
	static String modelFile = "/home/samuel/Downloads/leipzig/en-sent.bin";
	
	
    public static void main(String[] args) throws Exception {
	//File inDir = new File(args[0]);
	//File outDir = new File(args[1]);
    	//File modelFile = new File();
    	String paragraph = "This eBook is for the use of anyone anywhere at no cost and with " +
    			"almost no restrictions whatsoever.  You may copy it, give it away or " +
    			"re-use it under the terms of the Project Gutenberg License included" +
    			" with this eBook or online at www.gutenberg.org";
    	try (InputStream modelIn = new FileInputStream(modelFile)) {
    		    SentenceModel model = new SentenceModel(modelIn);
				SentenceDetectorME	detector	=	new	SentenceDetectorME(model);
				String	sentences[]	=	detector.sentDetect(paragraph);
					for	(String	sentence	:	sentences)	{
									System.out.println(sentence);
					}
				}	catch	(FileNotFoundException	ex)	{
				//	Handle	exception
				}	catch	(IOException	ex)	{
				//	Handle	exception
				}
    	
    }
}
