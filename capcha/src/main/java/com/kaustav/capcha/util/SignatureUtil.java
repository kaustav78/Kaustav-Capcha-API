package com.kaustav.capcha.util;

import org.springframework.util.DigestUtils;

import com.kaustav.capcha.pojo.CapchaData;

public class SignatureUtil {

	public static String getDigitalSignature(CapchaData data){
		String signature = "";
		String signingtData = data.getSentence();
		if(data.getExcludes() != null 
				&& !data.getExcludes().isEmpty()){
			for(int i = 0; i < data.getExcludes().size(); i++){
				signingtData = signingtData.concat(data.getExcludes().get(i));
			}
		}
		signature = DigestUtils.md5DigestAsHex(signingtData.getBytes());
		return signature;
	}
}
