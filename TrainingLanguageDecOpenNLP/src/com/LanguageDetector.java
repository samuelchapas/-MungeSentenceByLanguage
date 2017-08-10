package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorFactory;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.langdetect.LanguageDetectorSampleStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class LanguageDetector {

	private static LanguageDetectorModel model;
	static File inDir = new File("/home/samuel/Downloads/leipzig/munged(LanguageId)/mixed/mixed.txt"); //Folder where we thave the data

	public static void main(String[] args){
		LanguageDetectorSampleStream sampleStream = null;
		try {
			InputStreamFactory dataIn = new MarkableFileInputStreamFactory(inDir);
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			sampleStream = new LanguageDetectorSampleStream(lineStream);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

		//training parameters
		TrainingParameters params = new TrainingParameters();
		params.put(TrainingParameters.ITERATIONS_PARAM, 100);
		params.put(TrainingParameters.CUTOFF_PARAM, 5);
		params.put("DataIndexer", "TwoPass");
		params.put(TrainingParameters.ALGORITHM_PARAM, "NAIVEBAYES");
		
		//train the model
		try {
			model = LanguageDetectorME.train(sampleStream, params, new LanguageDetectorFactory());
		} catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("Completed");

		//load the model
		LanguageDetectorME ld = new LanguageDetectorME(model);

		//use the model for predicting the language
		Language[] languages = ld.predictLanguages("Estaba trabajando apasionadamente cuando de repente me sucedió algo increíble");
		System.out.println("Predicted languages...");
		for(Language language:languages){
			//printing the language and the confidence score for the test data to belong to the language
			System.out.println(language.getLang() + " confidence:" + language.getConfidence());
		}
	}
}
