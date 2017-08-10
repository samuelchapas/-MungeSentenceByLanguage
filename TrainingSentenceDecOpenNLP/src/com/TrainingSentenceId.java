package com;

import java.io.*;
import java.nio.charset.StandardCharsets;

import opennlp.tools.cmdline.sentdetect.SentenceEvaluationErrorListener;
import opennlp.tools.sentdetect.SentenceDetectorEvaluator;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class TrainingSentenceId {

	static String modelFile = "/home/samuel/Downloads/leipzig/es-sent.bin";
	static String dataFile = "/home/samuel/Downloads/leipzig/mungedTrainSentenceModel/spa/spa.txt";
	static String evalFile = "/home/samuel/Downloads/leipzig/munged300KSampleToEvalSentence/spa/spa.txt";
	public static SentenceModel model;
	
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
	//File inDir = new File(args[0]);
	//File outDir = new File(args[1]);
    	//File modelFile = new File();

    	InputStreamFactory isf = new InputStreamFactory() {  				//I got this from a stachexchange where 
    																			//I have seen that they got the same problem
            public InputStream createInputStream() throws IOException {
                return new FileInputStream(dataFile);
            }
        };
        
    	//InputStream modelIn =  new FileInputStream(
  	    //      new File("/home/samuel/Downloads/leipzig/munged(TrainSentenceModel)/eng/en-sent.train"));
    	
		ObjectStream<String> lineStream = new PlainTextByLineStream( isf, StandardCharsets.UTF_8);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);
		
	
		try{
			
			model = SentenceDetectorME.train("en", sampleStream, true, null, TrainingParameters.defaultParams());
			
			OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
	
		}	catch	(FileNotFoundException	ex)	{
			//	Handle	exception
		}	catch	(IOException	ex)	{
			//	Handle	exception
		}
		
		System.out.println("Ehmmm everything got finished the model training, Now we are going to evaluate");
		
		
		SentenceDetectorME	detector	=	new	SentenceDetectorME(model); // I am reusin the trained model to eval it
		//FileWriter modelWriter 
		//new FileWriter(new File("/home/samuel/Downloads/leipzig/en-sent.bin"));
		//modelWriter.write(model);
		//modelWriter.close();
		
		InputStreamFactory isfEvaluator = new InputStreamFactory() {  				//I got this from a stachexchange where 
			//I have seen that they got the same problem
			public InputStream createInputStream() throws IOException {
				return new FileInputStream(evalFile);
			}
		};
		
		lineStream	=	new	PlainTextByLineStream(isfEvaluator, StandardCharsets.UTF_8);
		sampleStream	=	new	SentenceSampleStream(lineStream);
		
		SentenceDetectorEvaluator	sentenceDetectorEvaluator =	new	SentenceDetectorEvaluator(detector,	new SentenceEvaluationErrorListener());
		sentenceDetectorEvaluator.evaluate(sampleStream);
		
		System.out.println(sentenceDetectorEvaluator.getFMeasure());
    }
    
}