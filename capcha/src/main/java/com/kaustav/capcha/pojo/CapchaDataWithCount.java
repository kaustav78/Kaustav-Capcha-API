package com.kaustav.capcha.pojo;

import java.util.List;

public class CapchaDataWithCount extends CapchaData{

	public List<CapchaWordCount> wordCount= null;
	
	public List<CapchaWordCount> getWordCount() {
		return wordCount;
	}
	public void setWordCount(List<CapchaWordCount> wordCount) {
		this.wordCount = wordCount;
	}
}
