package com.kaustav.capcha.util;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.kaustav.capcha.pojo.CapchaDataWithCount;
import com.kaustav.capcha.pojo.CapchaWordCount;

public class WordCountValidator {
	
	private Map<String,Integer> getWordCount(String sentence, List<String>excludes){
		sentence = sentence.replaceAll("\\p{Punct}+", "");
		if(excludes != null && !excludes.isEmpty()){
			for(String exclude : excludes){
				sentence = sentence.replaceAll("(?i)"+exclude, "");
			}
		}
		System.out.println("sentence:"+sentence);
		if(sentence == null || sentence.trim().length() == 0){
			return null;
		}
		 Map<String, Integer> listOfWords = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
	     Scanner scan = new Scanner(sentence);
	     while(scan.hasNext()){
            String word = scan.next();
            int countWord = 0;
            if(!listOfWords.containsKey(word))
            {                           
                listOfWords.put(word, 1);
            }
            else
            {
                countWord = listOfWords.get(word) + 1; 
                listOfWords.remove(word); 
                listOfWords.put(word, countWord); 
            }
       }
	   if(scan != null){
		  scan.close(); 
	   }
        return listOfWords;
	}
	
	public boolean isAlien(CapchaDataWithCount data){
		boolean isAlien = false;
		List<CapchaWordCount> wordCounts = data.getWordCount();
		Map<String, Integer> listOfWords = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
		for(CapchaWordCount wordCount : wordCounts){
			if(wordCount.getWord() == null || wordCount.getWord().trim().length() == 0){
				return true;
			}
			if(wordCount.getCount() == null || wordCount.getCount() == 0){
				return true;
			}
			listOfWords.put(wordCount.getWord(), wordCount.getCount());
		}
		if(listOfWords == null || listOfWords.isEmpty()){
			return true;
		}
		Map<String, Integer> listOfWordsOriginal = getWordCount(data.getSentence(), data.getExcludes());
		if(listOfWords.size() != listOfWordsOriginal.size()){
			return true;
		}
		if(!listOfWords.equals(listOfWordsOriginal)){
			return true;
		}
		
		return isAlien;
	}
		
	/*public static void main(String args[]){
		WordCountUtil util = new WordCountUtil();
		/*System.out.println(util.getWordCount("It was the best of times, it was the worst of times, "
				+ "it was the age of wisdom, it was the age of foolishness, "
				+ "it was the epoch of belief, it was the epoch of incredulity, "
				+ "it was the season of Light, it was the season of Darkness, "
				+ "it was the spring of hope, it was the winter of despair, "
				+ "we had everything before us, we had nothing before us, "
				+ "we were all going direct to Heaven, we were all going direct the other way - "
				+ "in short, the period was so far like the present period, that some of its "
				+ "noisiest authorities insisted on its being received, for good or for evil, "
				+ "in the superlative degree of comparison only."));
		List<String>excludes = new ArrayList<String>();
		excludes.add("Hodor");
		System.out.println(util.getWordCount("Hodor, hodor hodor. Hodor! Hodor hodor hodor hodor hodor hodor.",excludes));
		MasterData masterData = new MasterData();
		CapchaData capchaData = masterData.getCapchaData();
		System.out.println(util.getDigitalSignature(capchaData));
	}*/
}
