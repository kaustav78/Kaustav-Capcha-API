package com.kaustav.capcha.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kaustav.capcha.pojo.CapchaData;
import com.kaustav.capcha.pojo.CapchaDataWithCount;
import com.kaustav.capcha.repo.CapchaSentenceMaster;
import com.kaustav.capcha.util.SignatureUtil;
import com.kaustav.capcha.util.WordCountValidator;

@RestController
@RequestMapping(value = "/api")
public class CapchaController {

	
	@RequestMapping(value="/getcapcha",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CapchaData> getCapcha(HttpServletResponse response){
		try{
			CapchaSentenceMaster master = CapchaSentenceMaster.getInstance();
			CapchaData data = master.getCapchaData();
			//MasterData masterData = new MasterData();
			//CapchaData data = masterData.getCapchaData();
			String signature = SignatureUtil.getDigitalSignature(data);
			response.addHeader("signature", signature);
			return new ResponseEntity<>(data, HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value="/submitcapcha",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> submitCapcha(@RequestBody CapchaDataWithCount data, HttpServletRequest request){
		try{
			if(request.getHeader("signature") == null || request.getHeader("signature").length() == 0){
				return new ResponseEntity<>("Sorry, that's wrong. Nice try space troll. Signature Missing", HttpStatus.BAD_REQUEST);
			}
			if(data == null){
				return new ResponseEntity<>("Sorry, that's wrong. Nice try space troll. Data missing", HttpStatus.BAD_REQUEST);
			}
			if(data.getSentence() == null || data.getSentence().length() == 0){
				return new ResponseEntity<>("Sorry, that's wrong. Nice try space troll. Sentence missing", HttpStatus.BAD_REQUEST);
			}
			if(data.getWordCount() == null || data.getWordCount().isEmpty()){
				return new ResponseEntity<>("Sorry, that's wrong. Nice try space troll. Not passing the count", HttpStatus.BAD_REQUEST);
			}
			String signature = SignatureUtil.getDigitalSignature(data);
			if(!request.getHeader("signature").equals(signature)){
				return new ResponseEntity<>("Sorry, that's wrong. Nice try space troll. Trying to cheat", HttpStatus.BAD_REQUEST);
			}
			WordCountValidator validator = new WordCountValidator();
			if(validator.isAlien(data)){
				return new ResponseEntity<>("Sorry, that's wrong. Nice try space troll", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>("Looks Great.Shabaash", HttpStatus.OK);
		}catch(Exception ex){
			ex.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
