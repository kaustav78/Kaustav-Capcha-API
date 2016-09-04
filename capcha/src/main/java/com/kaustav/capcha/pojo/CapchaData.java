package com.kaustav.capcha.pojo;

import java.util.List;

public class CapchaData {
	
	private String sentence = "";
	private List<String> excludes = null;

	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public List<String> getExcludes() {
		return excludes;
	}
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

}
