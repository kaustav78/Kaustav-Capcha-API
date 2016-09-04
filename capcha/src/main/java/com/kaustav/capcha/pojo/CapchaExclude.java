package com.kaustav.capcha.pojo;

public class CapchaExclude {
	Integer sequence = 0;
	String excludeWord = "";
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getExcludeWord() {
		return excludeWord;
	}
	public void setExcludeWord(String excludeWord) {
		this.excludeWord = excludeWord;
	}
}
